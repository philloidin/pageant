package org.hammerlab.pageant.fmi

import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.hammerlab.pageant.fmi.SparkFM._
import org.hammerlab.pageant.fmi.SparkFMBroadcastTs.TMap
import org.hammerlab.pageant.suffixes.PDC3
import org.hammerlab.pageant.utils.Utils.rev

/**
  * SparkFM implementation that performs LF-mappings using a broadcasted Map of all target sequences.
  *
  * If the target sequences are (in aggregate) larger than makes sense to broadcast, this can run in to problems.
  */
case class SparkFMBroadcastTs(saZipped: RDD[(V, Idx)],
                              tZipped: RDD[(Idx, T)],
                              count: Long,
                              N: Int,
                              blockSize: Int = 100) extends SparkFM[PosNeedle](saZipped, tZipped, count, N, blockSize) {

  def occAll(tssRdd: RDD[AT]): RDD[(AT, BoundsMap)] = {
    val (tssi, tsBC) = tssToBroadcast(tssRdd)

    val cur: RDD[(BlockIdx, PosNeedle)] =
      for {
        (tIdx, ts) <- tssi
        end <- ts.indices
        bound: Bound <- List(LoBound(0L), HiBound(count))   // Starting bounds
        blockIdx = bound.blockIdx(blockSize)
      } yield
        (
          blockIdx,
          PosNeedle(tIdx, end+1, end+1, bound)
        )

    val occs = occRec(
      cur,
      sc.emptyRDD[Needle],
      tsBC,
      emitIntermediateRanges = true
    )

    joinBounds(occsToBoundsMap(occs), tssi)
  }

  def occ(tssRdd: RDD[AT]): RDD[(AT, Bounds)] = {
    val (tssi, tsBC) = tssToBroadcast(tssRdd)

    val cur: RDD[(BlockIdx, PosNeedle)] =
      for {
        (tIdx, ts) <- tssi
        bound <- List(LoBound(0L), HiBound(count))   // Starting bounds
        blockIdx = bound.blockIdx(blockSize)
      } yield
        (
          blockIdx,
          PosNeedle(tIdx, ts.length, ts.length, bound)
        )

    val occs = occRec(
      cur,
      sc.emptyRDD[Needle],
      tsBC,
      emitIntermediateRanges = false
    )

    joinBounds(occsToBounds(occs), tssi)
  }

  def occRec(cur: RDD[(BlockIdx, PosNeedle)],
             finished: RDD[Needle],
             tsBC: Broadcast[Map[Idx, Map[TPos, T]]],
             emitIntermediateRanges: Boolean = true): RDD[Needle] = {
    val next: RDD[(BlockIdx, PosNeedle)] =
      cur.cogroup(bwtBlocks).flatMap {
        case (blockIdx, (tuples, blocks)) =>
          assert(blocks.size == 1, s"Got ${blocks.size} blocks for block idx $blockIdx")
          val block = blocks.head
          val totalSumsV = totalSumsBC.value
          val tss = tsBC.value
          for {
            PosNeedle(idx, start, end, bound) <- tuples
            lastT = tss(idx)(start - 1)
            c = totalSumsV(lastT)
            o = block.occ(lastT, bound)
            newBound = bound.move(c + o)
          } yield {
            (
              newBound.blockIdx(blockSize),
              PosNeedle(idx, start - 1, end, newBound)
            )
          }
      }

    val (newFinished, notFinished, numLeft) = findFinished(next, emitIntermediateRanges)
    if (numLeft > 0) {
      occRec(notFinished, finished ++ newFinished, tsBC, emitIntermediateRanges)
    } else {
      finished ++ newFinished
    }
  }

  private def tssToBroadcast(tssRdd: RDD[AT]): (RDD[(Idx, AT)], Broadcast[TMap]) = {
    val tssi = tssRdd.zipWithIndex().map(rev).setName("tssi")
    (
      tssi,
      sc.broadcast(
        (for {
          (tIdx, ts) <- tssi.collect
        } yield {
          tIdx -> ts.zipWithIndex.map(rev).toMap
        }).toMap
      )
      )
  }
}

object SparkFMBroadcastTs {
  type TMap = Map[Idx, Map[TPos, T]]
  def apply[U](us: RDD[U],
               N: Int,
               toT: (U) => T,
               blockSize: Int = 100): SparkFMBroadcastTs = {
    @transient val sc = us.context
    us.cache()
    val count = us.count
    @transient val t: RDD[T] = us.map(toT)
    t.cache()
    @transient val tZipped: RDD[(Idx, T)] = t.zipWithIndex().map(rev)
    @transient val sa = PDC3(t.map(_.toLong), count)
    @transient val saZipped: RDD[(V, Idx)] = sa.zipWithIndex()

    SparkFMBroadcastTs(saZipped, tZipped, count, N, blockSize)
  }
}
