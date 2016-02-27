package org.hammerlab.pageant.fm.bwt

class LargerBWTTest extends BWTTest {

  val strings = List(
    "GAGTGCCAGG$",
    "ACCAGATCCT$",
    "AGGAAGCTTG$",
    "GAGTGCCAGG$",
    "CAGGTGGTAA$",
    "CAGATCCTGG$",
    "GGCCCTAAAC$",
    "TGCCAGGACC$",
    "TCGTGGCCCT$",
    "CTGGTCCTAA$"
  )

  val blocks100: List[List[(String, String)]] = List(
    ("0  0  0  0  0  0", "GTGGAGCCTA") :: Nil,
    ("0  0  0  0  0  0", "GTGGAGCCTAAAACGTGGCC") :: Nil,
    ("0  0  0  0  0  0", "GTGGAGCCTAAATTAACACCGTGGAATCCT") :: Nil,
    ("0  0  0  0  0  0", "GTGGAGCCTAAATTAAGCCACATCCCGTGGAATCCGCTCC") :: Nil,
    ("0  0  0  0  0  0", "GTGGAGCCTAAATTTAAGCCACCCAGTCCCCCGGTGGGAATGCCGCATCC") :: Nil,
    ("0  0  0  0  0  0", "GTGGAGCCTAAATTTAAGCCGACCCAGGGTCTTCCCCGGTGGGGAAATATGCCGCCATCC") :: Nil,
    ("0  0  0  0  0  0", "GTGGAGCCTAAATTTAAGACCCGACCCAGGGTCTTCCCCCGGTGGGATTGAAATATTGCCGCCAGATCGC") :: Nil,
    ("0  0  0  0  0  0", "GTGGAGCCTAAATTTAGAGCACCCGGACCCCAGGGTCTCTCCCCCGGTGGGATTGAAATATTGGGCCGCCAGATGGCGGC") :: Nil,
    ("0  0  0  0  0  0", "GTGGAGCCTAAATTTAGAGCACCCGGACCCCCAGGGGGTCTCTCCCCCGGTGGGGAATTGAAATATTTAGGAACGCCGCCAGATGGCGGC") :: Nil,
    ("0  0  0  0  0  0", "GTGGAGCCTAAATTTAGAGCCACCCCGGGGACCCCCAAGGGGGTCTCTTCCCCCGGTGGGGAATTTGGAAATAATTTAGGAACGCCGCCAGATGGCGGCC") :: Nil,

    ("0  0  0  0  0  0", "GTGGAGCCTAAATTTAGAG$CCACC$CCGGGGACC$CCC$AAGGGGGTCTCTTCCCCC$GGTGGGG$$AATTTGGAAATAAT$TTAGGAACGCCGCCAGA") ::
    ("8 22 25 28 17  0", "$TGG$CGGCC") :: Nil
  )

  val bounds100: List[List[Int]] = List(
    100 :: Nil,
    100 :: Nil,
    100 :: Nil,
    100 :: Nil,
    100 :: Nil,
    100 :: Nil,
    100 :: Nil,
    100 :: Nil,
    100 :: Nil,
    100 :: Nil,
    100 :: 200 :: Nil
  )

  val stepCounts: List[String] = List(
    "0 10 10 10 10 10",
    "0 10 12 14 18 20",
    "0 10 15 20 27 30",
    "0 10 19 26 33 40",
    "0 10 20 33 42 50",
    "0 10 21 38 51 60",
    "0 10 23 41 58 70",
    "0 10 26 46 65 80",
    "0 10 26 49 75 90",
    "0 10 30 55 84 100",
    "0 10 32 60 92 110"
  )

  val blocksMap = Map(
    100 → blocks100
  )

  val boundsMap = Map(
    100 → bounds100
  )

  testFn(100)

}

/*

From .bam:

GAGTGCCAGG$
ACCAGATCCT$
AGGAAGCTTG$
GAGTGCCAGG$
CAGGTGGTAA$
CAGATCCTGG$
GGCCCTAAAC$
TGCCAGGACC$
TCGTGGCCCT$
CTGGTCCTAA$

// 20
GAGTGCCAG G $
ACCAGATCC T $
AGGAAGCTT G $
GAGTGCCAG G $
CAGGTGGTA A $
CAGATCCTG G $
GGCCCTAAA C $
TGCCAGGAC C $
TCGTGGCCC T $
CTGGTCCTA A $
 CAGGTGGT A A$
 CTGGTCCT A A$
 GGCCCTAA A C$
 TGCCAGGA C C$
 GAGTGCCA G G$
 AGGAAGCT T G$
 GAGTGCCA G G$
 CAGATCCT G G$
 ACCAGATC C T$
 TCGTGGCC C T$

// 30
GAGTGCCAG G $
ACCAGATCC T $
AGGAAGCTT G $
GAGTGCCAG G $
CAGGTGGTA A $
CAGATCCTG G $
GGCCCTAAA C $
TGCCAGGAC C $
TCGTGGCCC T $
CTGGTCCTA A $
 CAGGTGGT A A$
 CTGGTCCT A A$
  CAGGTGG T AA$
  CTGGTCC T AA$
  GGCCCTA A AC$
 GGCCCTAA A C$
 TGCCAGGA C C$
  TGCCAGG A CC$
  ACCAGAT C CT$
  TCGTGGC C CT$
 GAGTGCCA G G$
 AGGAAGCT T G$
 GAGTGCCA G G$
 CAGATCCT G G$
  GAGTGCC A GG$
  GAGTGCC A GG$
  CAGATCC T GG$
 ACCAGATC C T$
 TCGTGGCC C T$
  AGGAAGC T TG$


// 40
GAGTGCCAG G $
ACCAGATCC T $
AGGAAGCTT G $
GAGTGCCAG G $
CAGGTGGTA A $
CAGATCCTG G $
GGCCCTAAA C $
TGCCAGGAC C $
TCGTGGCCC T $
CTGGTCCTA A $
 CAGGTGGT A A$
 CTGGTCCT A A$
  CAGGTGG T AA$
  CTGGTCC T AA$
   GGCCCT A AAC$
  GGCCCTA A AC$
   TGCCAG G ACC$
   GAGTGC C AGG$
   GAGTGC C AGG$
 GGCCCTAA A C$
 TGCCAGGA C C$
  TGCCAGG A CC$
   ACCAGA T CCT$
   TCGTGG C CCT$
  ACCAGAT C CT$
  TCGTGGC C CT$
 GAGTGCCA G G$
 AGGAAGCT T G$
 GAGTGCCA G G$
 CAGATCCT G G$
  GAGTGCC A GG$
  GAGTGCC A GG$
  CAGATCC T GG$
 ACCAGATC C T$
 TCGTGGCC C T$
   CAGGTG G TAA$
   CTGGTC C TAA$
  AGGAAGC T TG$
   CAGATC C TGG$
   AGGAAG C TTG$

// 50
GAGTGCCAG G $
ACCAGATCC T $
AGGAAGCTT G $
GAGTGCCAG G $
CAGGTGGTA A $
CAGATCCTG G $
GGCCCTAAA C $
TGCCAGGAC C $
TCGTGGCCC T $
CTGGTCCTA A $
 CAGGTGGT A A$
 CTGGTCCT A A$
  CAGGTGG T AA$
  CTGGTCC T AA$
    GGCCC T AAAC$
   GGCCCT A AAC$
  GGCCCTA A AC$
   TGCCAG G ACC$
   GAGTGC C AGG$
   GAGTGC C AGG$
 GGCCCTAA A C$
 TGCCAGGA C C$
    GAGTG C CAGG$
    GAGTG C CAGG$
  TGCCAGG A CC$
    TCGTG G CCCT$
   ACCAGA T CCT$
   TCGTGG C CCT$
  ACCAGAT C CT$
  TCGTGGC C CT$
    CTGGT C CTAA$
    CAGAT C CTGG$
    AGGAA G CTTG$
 GAGTGCCA G G$
 AGGAAGCT T G$
 GAGTGCCA G G$
 CAGATCCT G G$
    TGCCA G GACC$
  GAGTGCC A GG$
  GAGTGCC A GG$
  CAGATCC T GG$
    CAGGT G GTAA$
 ACCAGATC C T$
 TCGTGGCC C T$
   CAGGTG G TAA$
   CTGGTC C TAA$
    ACCAG A TCCT$
  AGGAAGC T TG$
   CAGATC C TGG$
   AGGAAG C TTG$

// 60
GAGTGCCAG G $
ACCAGATCC T $
AGGAAGCTT G $
GAGTGCCAG G $
CAGGTGGTA A $
CAGATCCTG G $
GGCCCTAAA C $
TGCCAGGAC C $
TCGTGGCCC T $
CTGGTCCTA A $
 CAGGTGGT A A$
 CTGGTCCT A A$
  CAGGTGG T AA$
  CTGGTCC T AA$
    GGCCC T AAAC$
   GGCCCT A AAC$
  GGCCCTA A AC$
   TGCCAG G ACC$
   GAGTGC C AGG$
   GAGTGC C AGG$
     ACCA G ATCCT$
 GGCCCTAA A C$
 TGCCAGGA C C$
    GAGTG C CAGG$
    GAGTG C CAGG$
  TGCCAGG A CC$
     GAGT G CCAGG$
     GAGT G CCAGG$
    TCGTG G CCCT$
   ACCAGA T CCT$
   TCGTGG C CCT$
     CTGG T CCTAA$
     CAGA T CCTGG$
  ACCAGAT C CT$
  TCGTGGC C CT$
    CTGGT C CTAA$
    CAGAT C CTGG$
    AGGAA G CTTG$
 GAGTGCCA G G$
 AGGAAGCT T G$
 GAGTGCCA G G$
 CAGATCCT G G$
    TGCCA G GACC$
     TCGT G GCCCT$
     AGGA A GCTTG$
  GAGTGCC A GG$
  GAGTGCC A GG$
  CAGATCC T GG$
     TGCC A GGACC$
     CAGG T GGTAA$
    CAGGT G GTAA$
 ACCAGATC C T$
 TCGTGGCC C T$
   CAGGTG G TAA$
   CTGGTC C TAA$
     GGCC C TAAAC$
    ACCAG A TCCT$
  AGGAAGC T TG$
   CAGATC C TGG$
   AGGAAG C TTG$

// 70
GAGTGCCAG G $
ACCAGATCC T $
AGGAAGCTT G $
GAGTGCCAG G $
CAGGTGGTA A $
CAGATCCTG G $
GGCCCTAAA C $
TGCCAGGAC C $
TCGTGGCCC T $
CTGGTCCTA A $
 CAGGTGGT A A$
 CTGGTCCT A A$
  CAGGTGG T AA$
  CTGGTCC T AA$
    GGCCC T AAAC$
   GGCCCT A AAC$
  GGCCCTA A AC$
   TGCCAG G ACC$
      AGG A AGCTTG$
   GAGTGC C AGG$
   GAGTGC C AGG$
      TGC C AGGACC$
     ACCA G ATCCT$
 GGCCCTAA A C$
 TGCCAGGA C C$
    GAGTG C CAGG$
    GAGTG C CAGG$
  TGCCAGG A CC$
     GAGT G CCAGG$
     GAGT G CCAGG$
    TCGTG G CCCT$
   ACCAGA T CCT$
   TCGTGG C CCT$
     CTGG T CCTAA$
     CAGA T CCTGG$
  ACCAGAT C CT$
  TCGTGGC C CT$
    CTGGT C CTAA$
      GGC C CTAAAC$
    CAGAT C CTGG$
    AGGAA G CTTG$
 GAGTGCCA G G$
 AGGAAGCT T G$
 GAGTGCCA G G$
 CAGATCCT G G$
    TGCCA G GACC$
      ACC A GATCCT$
      GAG T GCCAGG$
      GAG T GCCAGG$
     TCGT G GCCCT$
     AGGA A GCTTG$
  GAGTGCC A GG$
  GAGTGCC A GG$
  CAGATCC T GG$
     TGCC A GGACC$
      TCG T GGCCCT$
     CAGG T GGTAA$
    CAGGT G GTAA$
 ACCAGATC C T$
 TCGTGGCC C T$
   CAGGTG G TAA$
   CTGGTC C TAA$
     GGCC C TAAAC$
    ACCAG A TCCT$
      CTG G TCCTAA$
      CAG A TCCTGG$
  AGGAAGC T TG$
   CAGATC C TGG$
      CAG G TGGTAA$
   AGGAAG C TTG$

// 80
GAGTGCCAG G $
ACCAGATCC T $
AGGAAGCTT G $
GAGTGCCAG G $
CAGGTGGTA A $
CAGATCCTG G $
GGCCCTAAA C $
TGCCAGGAC C $
TCGTGGCCC T $
CTGGTCCTA A $
 CAGGTGGT A A$
 CTGGTCCT A A$
  CAGGTGG T AA$
  CTGGTCC T AA$
    GGCCC T AAAC$
   GGCCCT A AAC$
       AG G AAGCTTG$
  GGCCCTA A AC$
   TGCCAG G ACC$
       AC C AGATCCT$
      AGG A AGCTTG$
   GAGTGC C AGG$
   GAGTGC C AGG$
      TGC C AGGACC$
     ACCA G ATCCT$
       CA G ATCCTGG$
 GGCCCTAA A C$
 TGCCAGGA C C$
    GAGTG C CAGG$
    GAGTG C CAGG$
       TG C CAGGACC$
  TGCCAGG A CC$
     GAGT G CCAGG$
     GAGT G CCAGG$
    TCGTG G CCCT$
   ACCAGA T CCT$
   TCGTGG C CCT$
     CTGG T CCTAA$
       GG C CCTAAAC$
     CAGA T CCTGG$
  ACCAGAT C CT$
  TCGTGGC C CT$
    CTGGT C CTAA$
      GGC C CTAAAC$
    CAGAT C CTGG$
    AGGAA G CTTG$
 GAGTGCCA G G$
 AGGAAGCT T G$
 GAGTGCCA G G$
 CAGATCCT G G$
    TGCCA G GACC$
      ACC A GATCCT$
      GAG T GCCAGG$
      GAG T GCCAGG$
     TCGT G GCCCT$
     AGGA A GCTTG$
  GAGTGCC A GG$
  GAGTGCC A GG$
  CAGATCC T GG$
     TGCC A GGACC$
      TCG T GGCCCT$
     CAGG T GGTAA$
    CAGGT G GTAA$
       CT G GTCCTAA$
       CA G GTGGTAA$
 ACCAGATC C T$
 TCGTGGCC C T$
   CAGGTG G TAA$
   CTGGTC C TAA$
     GGCC C TAAAC$
    ACCAG A TCCT$
      CTG G TCCTAA$
      CAG A TCCTGG$
  AGGAAGC T TG$
       GA G TGCCAGG$
       GA G TGCCAGG$
   CAGATC C TGG$
       TC G TGGCCCT$
      CAG G TGGTAA$
   AGGAAG C TTG$

// 90
GAGTGCCAG G $
ACCAGATCC T $
AGGAAGCTT G $
GAGTGCCAG G $
CAGGTGGTA A $
CAGATCCTG G $
GGCCCTAAA C $
TGCCAGGAC C $
TCGTGGCCC T $
CTGGTCCTA A $
 CAGGTGGT A A$
 CTGGTCCT A A$
  CAGGTGG T AA$
  CTGGTCC T AA$
    GGCCC T AAAC$
   GGCCCT A AAC$
       AG G AAGCTTG$
  GGCCCTA A AC$
   TGCCAG G ACC$
       AC C AGATCCT$
      AGG A AGCTTG$
   GAGTGC C AGG$
   GAGTGC C AGG$
      TGC C AGGACC$
     ACCA G ATCCT$
       CA G ATCCTGG$
 GGCCCTAA A C$
 TGCCAGGA C C$
        A C CAGATCCT$
    GAGTG C CAGG$
    GAGTG C CAGG$
       TG C CAGGACC$
  TGCCAGG A CC$
     GAGT G CCAGG$
     GAGT G CCAGG$
        T G CCAGGACC$
    TCGTG G CCCT$
        G G CCCTAAAC$
   ACCAGA T CCT$
   TCGTGG C CCT$
     CTGG T CCTAA$
       GG C CCTAAAC$
     CAGA T CCTGG$
  ACCAGAT C CT$
  TCGTGGC C CT$
    CTGGT C CTAA$
      GGC C CTAAAC$
    CAGAT C CTGG$
    AGGAA G CTTG$
 GAGTGCCA G G$
 AGGAAGCT T G$
 GAGTGCCA G G$
 CAGATCCT G G$
        A G GAAGCTTG$
    TGCCA G GACC$
      ACC A GATCCT$
        C A GATCCTGG$
      GAG T GCCAGG$
      GAG T GCCAGG$
     TCGT G GCCCT$
     AGGA A GCTTG$
  GAGTGCC A GG$
  GAGTGCC A GG$
  CAGATCC T GG$
     TGCC A GGACC$
      TCG T GGCCCT$
     CAGG T GGTAA$
        C T GGTCCTAA$
        C A GGTGGTAA$
    CAGGT G GTAA$
       CT G GTCCTAA$
        G A GTGCCAGG$
        G A GTGCCAGG$
        T C GTGGCCCT$
       CA G GTGGTAA$
 ACCAGATC C T$
 TCGTGGCC C T$
   CAGGTG G TAA$
   CTGGTC C TAA$
     GGCC C TAAAC$
    ACCAG A TCCT$
      CTG G TCCTAA$
      CAG A TCCTGG$
  AGGAAGC T TG$
       GA G TGCCAGG$
       GA G TGCCAGG$
   CAGATC C TGG$
       TC G TGGCCCT$
      CAG G TGGTAA$
   AGGAAG C TTG$


// 100
GAGTGCCAG G $
ACCAGATCC T $
AGGAAGCTT G $
GAGTGCCAG G $
CAGGTGGTA A $
CAGATCCTG G $
GGCCCTAAA C $
TGCCAGGAC C $
TCGTGGCCC T $
CTGGTCCTA A $
 CAGGTGGT A A$
 CTGGTCCT A A$
  CAGGTGG T AA$
  CTGGTCC T AA$
    GGCCC T AAAC$
   GGCCCT A AAC$
       AG G AAGCTTG$
  GGCCCTA A AC$
   TGCCAG G ACC$
       AC C AGATCCT$
        $ C AGATCCTGG$
      AGG A AGCTTG$
   GAGTGC C AGG$
   GAGTGC C AGG$
      TGC C AGGACC$
        $ C AGGTGGTAA$
        $ G AGTGCCAGG$
        $ G AGTGCCAGG$
     ACCA G ATCCT$
       CA G ATCCTGG$
 GGCCCTAA A C$
 TGCCAGGA C C$
        A C CAGATCCT$
    GAGTG C CAGG$
    GAGTG C CAGG$
       TG C CAGGACC$
  TGCCAGG A CC$
        $ A CCAGATCCT$
     GAGT G CCAGG$
     GAGT G CCAGG$
        T G CCAGGACC$
    TCGTG G CCCT$
        G G CCCTAAAC$
   ACCAGA T CCT$
   TCGTGG C CCT$
     CTGG T CCTAA$
       GG C CCTAAAC$
     CAGA T CCTGG$
        $ T CGTGGCCCT$
  ACCAGAT C CT$
  TCGTGGC C CT$
    CTGGT C CTAA$
      GGC C CTAAAC$
    CAGAT C CTGG$
    AGGAA G CTTG$
 GAGTGCCA G G$
 AGGAAGCT T G$
 GAGTGCCA G G$
 CAGATCCT G G$
        A G GAAGCTTG$
    TGCCA G GACC$
      ACC A GATCCT$
        C A GATCCTGG$
      GAG T GCCAGG$
      GAG T GCCAGG$
        $ T GCCAGGACC$
     TCGT G GCCCT$
        $ G GCCCTAAAC$
     AGGA A GCTTG$
  GAGTGCC A GG$
  GAGTGCC A GG$
  CAGATCC T GG$
        $ A GGAAGCTTG$
     TGCC A GGACC$
      TCG T GGCCCT$
     CAGG T GGTAA$
        C T GGTCCTAA$
        C A GGTGGTAA$
    CAGGT G GTAA$
       CT G GTCCTAA$
        G A GTGCCAGG$
        G A GTGCCAGG$
        T C GTGGCCCT$
       CA G GTGGTAA$
 ACCAGATC C T$
 TCGTGGCC C T$
   CAGGTG G TAA$
   CTGGTC C TAA$
     GGCC C TAAAC$
    ACCAG A TCCT$
      CTG G TCCTAA$
      CAG A TCCTGG$
  AGGAAGC T TG$
       GA G TGCCAGG$
       GA G TGCCAGG$
   CAGATC C TGG$
       TC G TGGCCCT$
      CAG G TGGTAA$
        $ C TGGTCCTAA$
   AGGAAG C TTG$

// 110
GAGTGCCAG G $
ACCAGATCC T $
AGGAAGCTT G $
GAGTGCCAG G $
CAGGTGGTA A $
CAGATCCTG G $
GGCCCTAAA C $
TGCCAGGAC C $
TCGTGGCCC T $
CTGGTCCTA A $
 CAGGTGGT A A$
 CTGGTCCT A A$
  CAGGTGG T AA$
  CTGGTCC T AA$
    GGCCC T AAAC$
   GGCCCT A AAC$
       AG G AAGCTTG$
  GGCCCTA A AC$
   TGCCAG G ACC$
        - $ ACCAGATCCT$
       AC C AGATCCT$
        $ C AGATCCTGG$
      AGG A AGCTTG$
   GAGTGC C AGG$
   GAGTGC C AGG$
        - $ AGGAAGCTTG$
      TGC C AGGACC$
        $ C AGGTGGTAA$
        $ G AGTGCCAGG$
        $ G AGTGCCAGG$
     ACCA G ATCCT$
       CA G ATCCTGG$
 GGCCCTAA A C$
 TGCCAGGA C C$
        A C CAGATCCT$
        - $ CAGATCCTGG$
    GAGTG C CAGG$
    GAGTG C CAGG$
       TG C CAGGACC$
        - $ CAGGTGGTAA$
  TGCCAGG A CC$
        $ A CCAGATCCT$
     GAGT G CCAGG$
     GAGT G CCAGG$
        T G CCAGGACC$
    TCGTG G CCCT$
        G G CCCTAAAC$
   ACCAGA T CCT$
   TCGTGG C CCT$
     CTGG T CCTAA$
       GG C CCTAAAC$
     CAGA T CCTGG$
        $ T CGTGGCCCT$
  ACCAGAT C CT$
  TCGTGGC C CT$
    CTGGT C CTAA$
      GGC C CTAAAC$
    CAGAT C CTGG$
        - $ CTGGTCCTAA$
    AGGAA G CTTG$
 GAGTGCCA G G$
 AGGAAGCT T G$
 GAGTGCCA G G$
 CAGATCCT G G$
        A G GAAGCTTG$
    TGCCA G GACC$
        - $ GAGTGCCAGG$
        - $ GAGTGCCAGG$
      ACC A GATCCT$
        C A GATCCTGG$
      GAG T GCCAGG$
      GAG T GCCAGG$
        $ T GCCAGGACC$
     TCGT G GCCCT$
        $ G GCCCTAAAC$
     AGGA A GCTTG$
  GAGTGCC A GG$
  GAGTGCC A GG$
  CAGATCC T GG$
        $ A GGAAGCTTG$
     TGCC A GGACC$
      TCG T GGCCCT$
        - $ GGCCCTAAAC$
     CAGG T GGTAA$
        C T GGTCCTAA$
        C A GGTGGTAA$
    CAGGT G GTAA$
       CT G GTCCTAA$
        G A GTGCCAGG$
        G A GTGCCAGG$
        T C GTGGCCCT$
       CA G GTGGTAA$
 ACCAGATC C T$
 TCGTGGCC C T$
   CAGGTG G TAA$
   CTGGTC C TAA$
     GGCC C TAAAC$
    ACCAG A TCCT$
      CTG G TCCTAA$
      CAG A TCCTGG$
        - $ TCGTGGCCCT$
  AGGAAGC T TG$
       GA G TGCCAGG$
       GA G TGCCAGG$
        - $ TGCCAGGACC$
   CAGATC C TGG$
       TC G TGGCCCT$
      CAG G TGGTAA$
        $ C TGGTCCTAA$
   AGGAAG C TTG$


 */
