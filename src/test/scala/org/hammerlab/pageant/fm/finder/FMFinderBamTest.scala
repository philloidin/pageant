package org.hammerlab.pageant.fm.finder

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.serializer.DirectFileRDDSerializer._
import org.bdgenomics.adam.rdd.ADAMContext._
import org.hammerlab.pageant.fm.index.SparkFM
import SparkFM._
import org.hammerlab.pageant.fm.utils.{Utils, FMSuite}
import Utils._
import org.hammerlab.pageant.fm.index.SparkFM
import org.hammerlab.pageant.fm.utils.FMSuite
import org.hammerlab.pageant.utils.Utils.{resourcePath, rev}

abstract class FMFinderBamTest extends FMSuite with FMFinderTest {

  def initFM(sc: SparkContext): SparkFM = {
    val sa = sc.directFile[Long](resourcePath("normal.bam.sa"))
    val count = 1383018
    sa.count should be(count)

    sc.setCheckpointDir("tmp")

    val reads: RDD[String] =
      sc.loadAlignments(resourcePath("normal.bam")).map(_.getSequence + '$').repartition(4)

    reads.getNumPartitions should be(4)

    val us: RDD[Char] =
      for {
        read <- reads
        b <- read
      } yield b

    val ts: RDD[Int] = us.map(toI)

    ts.getNumPartitions should be(4)

    SparkFM(sa.zipWithIndex(), ts.zipWithIndex().map(rev), count, N = 6)
  }

  def testLF(name: String, tuples: (String, Int, Int)*): Unit = {
    test(name) {
      val strs = tuples.map(_._1)
      val needles: Seq[Array[Int]] = strs.map(_.toArray.map(toI))

      val needlesRdd = sc.parallelize(needles, 2)
      val actual = fmf.occ(needlesRdd).collect.map(p => (p._1.map(toC).mkString(""), p._2.toTuple))

      actual should be(tuples.toArray.map(t => (t._1, (t._2, t._3))))
    }
  }

  testLF(
    "normal-1",
    ("$", 0, 13559),
    ("A", 13559, 416285),
    ("C", 416285, 712409),
    ("G", 712409, 972719),
    ("T", 972719, 1381818),
    ("N", 1381818, 1383018)
  )

  testLF(
    "normal-2",
    ("$$", 0, 0),
    ("$A", 0, 4027),
    ("$C", 4027, 7055),
    ("$G", 7055, 9377),
    ("$T", 9377, 13538),
    ("$N", 13538, 13559),
    ("A$", 13559, 17674),
    ("AA", 17674, 145141),
    ("AC", 145141, 216818),
    ("AG", 216818, 307348),
    ("AT", 307348, 416252),
    ("AN", 416252, 416285),
    ("C$", 416285, 418970),
    ("CA", 418970, 525475),
    ("CC", 525475, 603695),
    ("CG", 603695, 612747),
    ("CT", 612747, 712383),
    ("CN", 712383, 712409),
    ("G$", 712409, 715038),
    ("GA", 715038, 792195),
    ("GC", 792195, 848270),
    ("GG", 848270, 909892),
    ("GT", 909892, 972695),
    ("GN", 972695, 972719),
    ("T$", 972719, 976820),
    ("TA", 976820, 1064356),
    ("TC", 1064356, 1151461),
    ("TG", 1151461, 1248229),
    ("TT", 1248229, 1381786),
    ("TN", 1381786, 1381818),
    ("N$", 1381818, 1381847),
    ("NA", 1381847, 1381881),
    ("NC", 1381881, 1381900),
    ("NG", 1381900, 1381916),
    ("NT", 1381916, 1381954),
    ("NN", 1381954, 1383018)
  )

  testLF(
    "normal-3",
    ("$$$", 0, 0),
    ("$$A", 0, 0),
    ("$$C", 0, 0),
    ("$$G", 0, 0),
    ("$$T", 0, 0),
    ("$$N", 0, 0),
    ("$A$", 0, 0),
    ("$AA", 0, 1314),
    ("$AC", 1314, 1991),
    ("$AG", 1991, 2924),
    ("$AT", 2924, 4022),
    ("$AN", 4022, 4027),
    ("$C$", 4027, 4027),
    ("$CA", 4027, 5166),
    ("$CC", 5166, 5979),
    ("$CG", 5979, 6085),
    ("$CT", 6085, 7052),
    ("$CN", 7052, 7055),
    ("$G$", 7055, 7055),
    ("$GA", 7055, 7817),
    ("$GC", 7817, 8315),
    ("$GG", 8315, 8815),
    ("$GT", 8815, 9375),
    ("$GN", 9375, 9377),
    ("$T$", 9377, 9377),
    ("$TA", 9377, 10337),
    ("$TC", 10337, 11177),
    ("$TG", 11177, 12175),
    ("$TT", 12175, 13536),
    ("$TN", 13536, 13538),
    ("$N$", 13538, 13538),
    ("$NA", 13538, 13541),
    ("$NC", 13541, 13546),
    ("$NG", 13546, 13547),
    ("$NT", 13547, 13550),
    ("$NN", 13550, 13559),
    ("A$$", 13559, 13559),
    ("A$A", 13559, 14821),
    ("A$C", 14821, 15785),
    ("A$G", 15785, 16480),
    ("A$T", 16480, 17670),
    ("A$N", 17670, 17674),
    ("AA$", 17674, 19033),
    ("AAA", 19033, 65723),
    ("AAC", 65723, 87760),
    ("AAG", 87760, 114061),
    ("AAT", 114061, 145134),
    ("AAN", 145134, 145141),
    ("AC$", 145141, 145796),
    ("ACA", 145796, 175056),
    ("ACC", 175056, 193661),
    ("ACG", 193661, 196099),
    ("ACT", 196099, 216814),
    ("ACN", 216814, 216818),
    ("AG$", 216818, 217748),
    ("AGA", 217748, 248268),
    ("AGC", 248268, 267246),
    ("AGG", 267246, 286985),
    ("AGT", 286985, 307342),
    ("AGN", 307342, 307348),
    ("AT$", 307348, 308476),
    ("ATA", 308476, 336072),
    ("ATC", 336072, 357429),
    ("ATG", 357429, 383000),
    ("ATT", 383000, 416245),
    ("ATN", 416245, 416252),
    ("AN$", 416252, 416258),
    ("ANA", 416258, 416261),
    ("ANC", 416261, 416263),
    ("ANG", 416263, 416265),
    ("ANT", 416265, 416271),
    ("ANN", 416271, 416285),
    ("C$$", 416285, 416285),
    ("C$A", 416285, 417078),
    ("C$C", 417078, 417676),
    ("C$G", 417676, 418157),
    ("C$T", 418157, 418967),
    ("C$N", 418967, 418970),
    ("CA$", 418970, 420058),
    ("CAA", 420058, 448629),
    ("CAC", 448629, 469292),
    ("CAG", 469292, 496097),
    ("CAT", 496097, 525473),
    ("CAN", 525473, 525475),
    ("CC$", 525475, 526184),
    ("CCA", 526184, 554982),
    ("CCC", 554982, 576493),
    ("CCG", 576493, 579163),
    ("CCT", 579163, 603694),
    ("CCN", 603694, 603695),
    ("CG$", 603695, 603794),
    ("CGA", 603794, 606137),
    ("CGC", 606137, 608357),
    ("CGG", 608357, 610549),
    ("CGT", 610549, 612746),
    ("CGN", 612746, 612747),
    ("CT$", 612747, 613691),
    ("CTA", 613691, 634602),
    ("CTC", 634602, 658125),
    ("CTG", 658125, 685878),
    ("CTT", 685878, 712379),
    ("CTN", 712379, 712383),
    ("CN$", 712383, 712386),
    ("CNA", 712386, 712388),
    ("CNC", 712388, 712392),
    ("CNG", 712392, 712394),
    ("CNT", 712394, 712397),
    ("CNN", 712397, 712409),
    ("G$$", 712409, 712409),
    ("G$A", 712409, 713187),
    ("G$C", 713187, 713783),
    ("G$G", 713783, 714242),
    ("G$T", 714242, 715031),
    ("G$N", 715031, 715038),
    ("GA$", 715038, 715763),
    ("GAA", 715763, 740985),
    ("GAC", 740985, 753887),
    ("GAG", 753887, 772589),
    ("GAT", 772589, 792187),
    ("GAN", 792187, 792195),
    ("GC$", 792195, 792726),
    ("GCA", 792726, 811533),
    ("GCC", 811533, 826774),
    ("GCG", 826774, 828186),
    ("GCT", 828186, 848269),
    ("GCN", 848269, 848270),
    ("GG$", 848270, 848905),
    ("GGA", 848905, 867131),
    ("GGC", 867131, 880335),
    ("GGG", 880335, 896199),
    ("GGT", 896199, 909888),
    ("GGN", 909888, 909892),
    ("GT$", 909892, 910550),
    ("GTA", 910550, 923955),
    ("GTC", 923955, 936177),
    ("GTG", 936177, 954115),
    ("GTT", 954115, 972690),
    ("GTN", 972690, 972695),
    ("GN$", 972695, 972698),
    ("GNA", 972698, 972701),
    ("GNC", 972701, 972703),
    ("GNG", 972703, 972704),
    ("GNT", 972704, 972707),
    ("GNN", 972707, 972719),
    ("T$$", 972719, 972719),
    ("T$A", 972719, 973903),
    ("T$C", 973903, 974769),
    ("T$G", 974769, 975454),
    ("T$T", 975454, 976813),
    ("T$N", 976813, 976820),
    ("TA$", 976820, 977760),
    ("TAA", 977760, 1003420),
    ("TAC", 1003420, 1018810),
    ("TAG", 1018810, 1036595),
    ("TAT", 1036595, 1064348),
    ("TAN", 1064348, 1064356),
    ("TC$", 1064356, 1065144),
    ("TCA", 1065144, 1093643),
    ("TCC", 1093643, 1115691),
    ("TCG", 1115691, 1118115),
    ("TCT", 1118115, 1151449),
    ("TCN", 1151449, 1151461),
    ("TG$", 1151461, 1152424),
    ("TGA", 1152424, 1177726),
    ("TGC", 1177726, 1198900),
    ("TGG", 1198900, 1222226),
    ("TGT", 1222226, 1248223),
    ("TGN", 1248223, 1248229),
    ("TT$", 1248229, 1249600),
    ("TTA", 1249600, 1274254),
    ("TTC", 1274254, 1303411),
    ("TTG", 1303411, 1327912),
    ("TTT", 1327912, 1381778),
    ("TTN", 1381778, 1381786),
    ("TN$", 1381786, 1381793),
    ("TNA", 1381793, 1381796),
    ("TNC", 1381796, 1381798),
    ("TNG", 1381798, 1381801),
    ("TNT", 1381801, 1381803),
    ("TNN", 1381803, 1381818),
    ("N$$", 1381818, 1381818),
    ("N$A", 1381818, 1381828),
    ("N$C", 1381828, 1381832),
    ("N$G", 1381832, 1381834),
    ("N$T", 1381834, 1381847),
    ("N$N", 1381847, 1381847),
    ("NA$", 1381847, 1381850),
    ("NAA", 1381850, 1381860),
    ("NAC", 1381860, 1381868),
    ("NAG", 1381868, 1381872),
    ("NAT", 1381872, 1381878),
    ("NAN", 1381878, 1381881),
    ("NC$", 1381881, 1381883),
    ("NCA", 1381883, 1381885),
    ("NCC", 1381885, 1381887),
    ("NCG", 1381887, 1381889),
    ("NCT", 1381889, 1381895),
    ("NCN", 1381895, 1381900),
    ("NG$", 1381900, 1381902),
    ("NGA", 1381902, 1381906),
    ("NGC", 1381906, 1381907),
    ("NGG", 1381907, 1381908),
    ("NGT", 1381908, 1381911),
    ("NGN", 1381911, 1381916),
    ("NT$", 1381916, 1381916),
    ("NTA", 1381916, 1381926),
    ("NTC", 1381926, 1381932),
    ("NTG", 1381932, 1381939),
    ("NTT", 1381939, 1381948),
    ("NTN", 1381948, 1381954),
    ("NN$", 1381954, 1381964),
    ("NNA", 1381964, 1381984),
    ("NNC", 1381984, 1381988),
    ("NNG", 1381988, 1381995),
    ("NNT", 1381995, 1382016),
    ("NNN", 1382016, 1383018)
  )

  def testCountBidi(strTuples: ((String, String, String), List[Int])*): Unit = {
    val tuples = for {
      ((left, middle, right), counts) <- strTuples.toList
      str = List(left, middle, right).mkString("")
      start = left.length
      end = start + middle.length
    } yield {
      ((str, start, end), counts)
    }
    val strs = tuples.map(_._1._1)
    val expectedMap =
      for {
        ((str, start, end), counts) <- tuples.toMap
      } yield {
        counts.length should be((start + 1) * (str.length - end + 1))
        str -> (for {
          (idx, count) <- counts.zipWithIndex.map(rev).toMap
          r = idx / (start + 1)
          c = end + (idx % (start + 1))
        } yield {
          (r, c) -> count
        })
      }

    test(s"countBidi-${strs.mkString(",")}") {
      val needles: Seq[(Array[Int], TPos, TPos)] =
        for {
          ((str, start, end), _) <- tuples
        } yield {
          (str.toArray.map(toI), start, end)
        }

      val needlesRdd = sc.parallelize(needles, 2)

      val actualMap = for {
        (ts, map) <- fmf.countBidi(needlesRdd).collectAsMap()
        str = ts.map(toC).mkString("")
      } yield {
        str -> (for {
          (r, rm) <- map.m.toList
          (c, count) <- rm.toList
        } yield {
          (r, c) -> count
        }).sortBy(_._1).toMap
      }

      for {
        (str, expected) <- expectedMap
        counts = actualMap(str)
      } {
        withClue(s"$str") {
          counts.size should be(expected.size)
          for {
            ((r, c), expectedCount) <- expected
            actualCount = counts(r, c)
          } {
            withClue(s"($r,$c)") {
              actualCount should be(expectedCount)
            }
          }
        }
      }
    }
  }

  testCountBidi(
    ("GGCCC", "TAAACAGGTG", "GTAAG") -> List(
              /*   15  16  17  18  19  20   */
              /*    G   G   T   A   A   G   */

      /*  0: G  */  31, 31, 30, 30, 28, 26,
      /*  1: G  */  31, 31, 30, 30, 28, 26,
      /*  2: C  */  32, 32, 31, 31, 29, 27,
      /*  3: C  */  32, 32, 31, 31, 29, 27,
      /*  4: C  */  32, 32, 31, 31, 29, 27,
      /*  5: T  */  34, 34, 33, 33, 31, 29
    )
  )
}

class BroadcastFinderBamTest extends FMFinderBamTest with BroadcastFinderTest
class AllTFinderBamTest extends FMFinderBamTest with AllTFinderTest
