      SUBROUTINE STAT55(PO,Z,STT,LTSTT,STC,LTSTC,QTC,LTQTC,TII,NGAGE,
     . STTNAM,NGS,GZ,ST0,QT0,YDI,QDI,RMS,AVD,STE,LTSTE,IRF,NSLCE,
     . NB,ISTRT,INOW,KSTG,STQ,LTSTQ,K1,K2,K4,K28,K29)
C
C      THIS SUBROUTINE DETERMINES THE STATISTICS ASSOCIATED WITH THE
C          COMPUTED VS OBSERVED HYDROGRAPHS
C
      COMMON/IONUM/IN,IPR,IPU
      COMMON/M155/NU,JN,JJ,KIT,G,DT,TT,TIMF,F1
      COMMON/M655/KTIME,DTHYD,J1
      COMMON/M3255/IOBS,KTERM,KPL,JNK,TEH
      COMMON/NPC55/NP,NPST,NPEND
      COMMON/TPL55/DTHPLT
      COMMON/METR55/METRIC
      COMMON/IDOS55/IDOS,IFCST
      COMMON/FNOPR/NOPROT
      COMMON/FPLTAB/IPLHY,IPRHY
      COMMON/NSLC21/NSLICE

      INCLUDE 'common/ofs55'

      DIMENSION PO(*),Z(*),STT(*),LTSTT(*),STC(*),LTSTC(*),QTC(*)
      DIMENSION LTQTC(*),TII(*),STE(*),LTSTE(*),STQ(*),LTSTQ(*),IRF(*)
      DIMENSION ST0(K28),QT0(K28),NB(K1)
      DIMENSION RMS(K4,K1),AVD(K4,K1),YDI(K2,K1),QDI(K2,K1),NSLCE(K1)
      DIMENSION GZ(K4,K1),NGS(K4,K1),NGAGE(K1),STTNAM(5,K28)
      CHARACTER*8 SNAME
      DATA SNAME/'STAT55  '/
C
C    ================================= RCS keyword statements ==========
      CHARACTER*68     RCSKW1,RCSKW2
      DATA             RCSKW1,RCSKW2 /                                 '
     .$Source: /fs/hseb/ob72/rfc/ofs/src/fcst_fldwav/RCS/stat55.f,v $
     . $',                                                             '
     .$Id: stat55.f,v 1.6 2000/12/19 15:56:16 dws Exp $
     . $' /
C    ===================================================================
C
C
      CALL FPRBUG(SNAME,1,55,IBUG)

      INO=1
      INC=1
      IPLT=1
      LSLC=0

      IF(METRIC.EQ.0) THEN
        FT=1.
        CFS=1.
      ELSE
        FT=3.281
        CFS=35.32
      ENDIF

      IF(DTHYD.LT.DTHPLT) INO=DTHPLT/DTHYD+0.01
      IF(DTHYD.GT.DTHPLT) THEN
        INC=DTHYD/DTHPLT+0.01
        IPLT=0
      ENDIF
      NSTT=5
      IF(IDOS.GE.1) NSTT=3
      IF(NPEND.EQ.0.OR.NPEND.GT.KTIME) NPEND=KTIME

      DO 2000 J=1,JN
      NGAG=NGAGE(J)
      IF(NGAG.EQ.0) GO TO 2000
      IF(DTHYD.LE.0.0) GO TO 2000
      DO 1990 KK=1,NGAG
      MISS=0
      NGA=NGS(KK,J)
      GZERO=GZ(KK,J)
      KKJ=LCAT21(KK,J,NGAGE)
      IF(IDOS.GE.3) THEN
        WRITE(IPR,2135) J,NGS(KK,J),(STTNAM(KX,KKJ), KX = 1,3)
      ELSE
        WRITE(IPR,2140) J,NGS(KK,J),(STTNAM(KX,KKJ), KX = 1,5)
      ENDIF

      LSTC=LTSTC(KKJ)-1
      LQTC=LTQTC(KKJ)-1
      LSTT=LTSTT(KKJ)-1
      IF(IOBS.GT.1) LSTE=LTSTE(KKJ)-1
      IF(KPL.EQ.3) LSTQ=LTSTQ(KKJ)-1

      IF(IOBS.EQ.1) GO TO 20
      IF(KK.EQ.NGAG.AND.NGA.EQ.NB(J).AND.J.EQ.1.AND.NGAG.GT.1) GO TO 15

c  divide flows by 1000 to match what dwoper does in adjts21
      DO 5 L=1,KTIME
        QTC(LQTC+L)=QTC(LQTC+L)*0.001
 5    CONTINUE

      NSLICE=NSLCE(J)
      CALL ADJTS21(KKJ,J,PO(LONQSL),PO(LOSLIC+LSLC),PO(LOFRMO+LSLC),
     .  PO(LOFBIO+LSLC),PO(LORRMO+LSLC),PO(LORBIO+LSLC),STE(LSTE+1),IRF,
     .  Z(LZFRMS),Z(LZFBIA),Z(LZRRMS),Z(LZRBIA),STC(LSTC+1),
     .  QTC(LQTC+1),STT(LSTT+1),KTIME,JNK)

c  restore flows back to original form
      DO 10 L=1,KTIME
        QTC(LQTC+L)=QTC(LQTC+L)*1000.
   10 CONTINUE

   15 LSLC=LSLC+K29

   20 ISTQ=0
   30 SDHOE=0.
      SDHO=0.
      SDE=0.
      SD=0.
      ISTQ=ISTQ+1


      IF(IDOS.GE.3) THEN
        IF(KPL.EQ.2) THEN
          QSMX=QT0(KKJ)
          QSMN=QT0(KKJ)
          STTMX=QDI(NGA,J)
          STTMN=QDI(NGA,J)
        ELSE
          QSMX=ST0(KKJ)
          QSMN=ST0(KKJ)
          STTMX=YDI(NGA,J)
          STTMN=YDI(NGA,J)
        ENDIF
      ELSE
        IF(KPL.EQ.2) THEN
          QSMX=QTC(1+LQTC)
          QSMN=QTC(1+LQTC)
        ELSE
          QSMX=STC(1+LSTC)
          QSMN=STC(1+LSTC)
        ENDIF
        STTMX=STT(1+LSTT)
        STTMN=STT(1+LSTT)
      ENDIF
      KTTTT = 0
      N1=NPST
cc      IF(IDOS.GE.3) N1=NPST+1
      DO 1970 K=N1,NPEND
      KO=INO*(K-1)+1
      KC=INC*(K-1)+1
      T=TII(KO)
      IF(IPLT.EQ.0) THEN
        IF(IOBS.GT.1) SE=TERP21(T,KTIME,IT1,IT2,TII,STE,LSTE)
        SC=TERP21(T,KTIME,IT1,IT2,TII,STC,LSTC)
        QC=TERP21(T,KTIME,IT1,IT2,TII,QTC,LQTC)
      ELSE
        IF(IOBS.GT.1) SE=STE(KC+LSTE)
        SC=STC(KC+LSTC)
        QC=QTC(KC+LQTC)
      ENDIF
      ST=STT(KO+LSTT)
      IF(KPL.EQ.3.AND.ISTQ.GT.1) ST=STQ(KO+LSTQ)
      NHRS = ISTRT+(K-1)*DTHYD
      IF(NHRS.GT.INOW) GO TO 1970
C
C         CHECK FOR MISSING DATA POINT AND SKIP IF IT IS
C
      IK=IFMSNG(ST)
      IF(IK.EQ.1) THEN
        DHO=-999.
        DHOE=-999.
        DHC=SC-SE
        MISS=MISS+1
        GO TO 1940
      ENDIF
C
      IF(KSTG.EQ.1.AND.ISTQ.LE.1) ST=ST+GZERO
      KTTTT = KTTTT + 1
      IF(KPL.NE.2) THEN
        DHO=(SC-ST)/FT
        DHC=(SE-ST)/FT
      ELSE
        DHO=(QC-ST)/CFS
        DHC=(QC-SE)/CFS
      ENDIF
      DHOE=SE-ST

      SDHO=SDHO+DHO*DHO
      SDHOE=SDHOE+DHOE*DHOE
      SD=SD+DHO
      SDE=SDE+DHOE
      IF(KPL.EQ.2.OR.ISTQ.GT.1) GO TO 1948
      IF(SC.GT.QSMX) QSMX=SC
      IF(SC.LT.QSMN) QSMN=SC
      GO TO 1947
 1948 IF(QC.GT.QSMX) QSMX=QC
      IF(QC.LT.QSMN) QSMN=QC
 1947 IF(ST.GT.STTMX) STTMX=ST
      IF(ST.LT.STTMN) STTMN=ST

 1940 IF(JNK.LE.4.OR.NOPROT.EQ.1) GO TO 1965
      PQTC=QC*0.001
C
      IF(IOBS.EQ.1.OR.(KK.EQ.NGAG.AND.NGA.EQ.NB(J).AND.J.EQ.1.AND.
     .               NGAG.GT.1)) THEN
        IF(KPL.NE.2.AND.KSTG.NE.1.AND.ISTQ.LE.1) THEN
            IF(K.EQ.N1.AND.METRIC.EQ.0) WRITE(IPR,1949)
            IF(K.EQ.N1.AND.METRIC.EQ.1) WRITE(IPR,1950)
        END IF
        IF(KPL.GE.2.AND.ISTQ.GT.1) THEN
          IF(K.EQ.N1.AND.METRIC.EQ.0) WRITE(IPR,1951)
          IF(K.EQ.N1.AND.METRIC.EQ.1) WRITE(IPR,1952)
          PSTT=SC*0.001
          IF(KPL.EQ.3.AND.ISTQ.GT.1) PSTT=ST*0.001
        END IF
        IF(KPL.NE.2.AND.KSTG.EQ.1.AND.ISTQ.LE.1) THEN
          IF(K.EQ.N1.AND.METRIC.EQ.0) WRITE(IPR,1953)
          IF(K.EQ.N1.AND.METRIC.EQ.1) WRITE(IPR,1954)
        END IF
      ELSE
        IF(KPL.NE.2.AND.KSTG.NE.1.AND.ISTQ.LE.1) THEN
          IF(K.EQ.N1.AND.METRIC.EQ.0) WRITE(IPR,1941)
          IF(K.EQ.N1.AND.METRIC.EQ.1) WRITE(IPR,1942)
        ENDIF
        IF(KPL.EQ.2.OR.ISTQ.GT.1) THEN
          IF(K.EQ.N1.AND.METRIC.EQ.0) WRITE(IPR,1943)
          IF(K.EQ.N1.AND.METRIC.EQ.1) WRITE(IPR,1944)
          PSTT=SC*0.001
        END IF
        IF(KPL.NE.2.AND.KSTG.EQ.1.AND.ISTQ.LE.1) THEN
          IF(K.EQ.N1.AND.METRIC.EQ.0) WRITE(IPR,1945)
          IF(K.EQ.N1.AND.METRIC.EQ.1) WRITE(IPR,1946)
        END IF
      ENDIF
      IF(IOBS.LE.1) THEN
        IF(KPL.NE.2.AND.ISTQ.LE.1) 
     .         WRITE(IPR,2115) KO,ST/FT,SC/FT,DHO,PQTC/CFS
        IF(KPL.EQ.2.OR.ISTQ.GT.1) 
     .         WRITE(IPR,2110) KO,PSTT/CFS,PQTC/CFS,DHO,SC/FT
      ELSE
        IF(KPL.NE.2.AND.ISTQ.LE.1) 
     .  WRITE(IPR,2105) KO,ST/FT,SC/FT,SE/FT,DHO,DHOE,DHC,PQTC/CFS
        IF(KPL.EQ.2.OR.ISTQ.GT.1) 
     .   WRITE(IPR,2100) KO,PSTT/CFS,PQTC/CFS,SE/FT,DHO,DHOE,DHC,SC/FT
      ENDIF

 1965 IF(KO.EQ.KTIME.OR.KC.EQ.KTIME) GO TO 1975
 1970 CONTINUE

 1975 STTD=STTMX-STTMN
      PE=0.0
      IF(STTD.GT.0.001) PE=(QSMX-STTMX)*100.0/STTD
      IF(ABS(STTMX).LT.0.01) STTMX=0.1
      IF(KTTTT.EQ.0) THEN
        RMS(KK,J)=-999.
        AVD(KK,J)=-999.
        SEAN=-999.
        AVDN=-999.
      ELSE
        RMS(KK,J)=SQRT(SDHO/KTTTT)
        AVD(KK,J)=SD/KTTTT
        SEAN = SQRT(SDHOE/KTTTT)
        AVDN = SDE/KTTTT
      ENDIF
      IF(NOPROT.EQ.0.AND.MISS.EQ.0) WRITE(IPR,1980) RMS(KK,J),AVD(KK,J)
      IF(NOPROT.EQ.0.AND.MISS.GT.0) WRITE(IPR,1981) RMS(KK,J),AVD(KK,J),
     . MISS
      IF(IOBS.GT.1) WRITE(IPR,1985) SEAN,AVDN
      IF(ISTQ.EQ.1.AND.KPL.EQ.3) GO TO 30
 1990 CONTINUE
 2000 CONTINUE

 2100 FORMAT(5X,I5,6F12.1,F12.2)
 2105 FORMAT(5X,I5,6F12.2,F15.1)
 2110 FORMAT(5X,I5,12X,F12.1,8X,F12.1,4X,F12.1,9X,F12.2)
 2115 FORMAT(5X,I5,12X,F12.2,8X,F12.2,4X,F12.2,9X,F12.1)
 2135 FORMAT (/17H           RIVER ,I3,10H, STATION ,I3,5X,2A4,2X,A4)
 2140 FORMAT (/17H           RIVER ,I3,10H, STATION ,I3,5X,5A4)
 1941     FORMAT (/6X,'TIME',11X,'ELEVATION (FT-MSL)',20X,
     .    'DIFFERENCE (FT)',14X,'DISCHARGE'/14X,'OBSERVED',4X,'COMPUTED'
     .    ,3X,'ESTIMATED',5X,'COM-OBS',5X,'EST-OBS',5X,'COM-EST',7X,
     .    '(1000 CFS)')
 1942     FORMAT (/6X,'TIME',11X,'ELEVATION (M-MSL)',20X,
     .    'DIFFERENCE (FT)',14X,'DISCHARGE'/14X,'OBSERVED',4X,'COMPUTED'
     .    ,3X,'ESTIMATED',5X,'COM-OBS',5X,'EST-OBS',5X,'COM-EST',7X,
     .    '(1000 CMS)')
 1943     FORMAT(/6X,'TIME',10X,'DISCHARGE(1000 CFS)',12X,
     .    'DIFFERENCE(CFS)',14X,'OBSERVED',4X,'COMPUTED',3X,'ESTIMATED'
     .    ,5X,'COM-OBS',5X,'EST-OBS',5X,'COM-EST',7X,'WSEL(FT-MSL)')
 1944     FORMAT(/6X,'TIME',10X,'DISCHARGE(1000 CMS)',12X,
     .    'DIFFERENCE(CMS)',14X,'OBSERVED',4X,'COMPUTED',3X,'ESTIMATED'
     .    ,5X,'COM-OBS',5X,'EST-OBS',5X,'COM-EST',7X,'WSEL(M-MSL)')
 1945     FORMAT (/6X,'TIME',19X,'STAGE (FT)',20X,'DIFFERENCE (FT)',
     .      14X,'DISCHARGE'/14X,'OBSERVED',4X,'COMPUTED',3X,'ESTIMATED',
     .      5X,'COM-OBS',5X,'EST-OBS',5X,'COM-EST',9X,'(1000 CFS)')
 1946     FORMAT (/6X,'TIME',19X,'STAGE (M)',20X,'DIFFERENCE (FT)',
     .     14X,'DISCHARGE'/14X,'OBSERVED',4X,'COMPUTED',3X,'ESTIMATED',
     .     5X,'COM-OBS',5X,'EST-OBS',5X,'COM-EST',9X,'(1000 CMS)')
 1949   FORMAT(6X,4HTIME,11X,18HOBSERVED ELEVATION,2X,
     .    18HCOMPUTED ELEVATION,3X,11HCOMP - OBS ,9X,9HDISCHARGE/16X,
     .    2(6X,14H(FT ABOVE MSL)),9X,4H(FT),12X,10H(1000 CFS))
 1950   FORMAT(6X,4HTIME,11X,18HOBSERVED ELEVATION,2X,
     .    18HCOMPUTED ELEVATION,3X,11HCOMP - OBS ,9X,9HDISCHARGE/16X,
     .  2(6X,13H(M ABOVE MSL)),10X,3H(M),12X,10H(1000 CMS))
 1951   FORMAT(6X,4HTIME,11X,18HOBSERVED DISCHARGE,2X,
     .    18HCOMPUTED DISCHARGE,3X,11HCOMP - OBS ,9X,9HELEVATION/16X,
     .    2(6X,14H( 1000 CFS   )),9X,4H(FT),8X,14H(FT ABOVE MSL))
 1952   FORMAT(6X,4HTIME,11X,18HOBSERVED DISCHARGE,2X,
     .    18HCOMPUTED DISCHARGE,3X,11HCOMP - OBS ,9X,9HELEVATION/16X,
     .    2(6X,14H(  1000 CMS  )),8X,5H(CMS),7X,13H(M ABOVE MSL))
 1953 FORMAT(6X,4HTIME,11X,'OBSERVED STAGE',10X,'COMPUTED STAGE',3X,
     . 11HCOMP - OBS ,9X,9HDISCHARGE/16X,
     .  2(16X,'(FT)'),9X,4H(FT),12X,10H(1000 CFS))
 1954 FORMAT(6X,4HTIME,11X,'OBSERVED STAGE',10X,'COMPUTED STAGE',3X,
     . 11HCOMP - OBS ,9X,9HDISCHARGE/16X,
     .  2(12X,'(M)'),10X,3H(M),12X,10H(1000 CMS))
 1980 FORMAT(12X,13H RMS ERROR = ,F12.3,8H BIAS = ,F12.3)
 1981 FORMAT(12X,13H RMS ERROR = ,F12.3,8H BIAS = ,F12.3,
     . 5X,I3,' POINTS MISSING')
 1985 FORMAT(8X,17H NEW RMS ERROR = ,F12.3,8H BIAS = ,F12.3)


      IF(ITRACE.EQ.1) WRITE(IODBUG,9000) SNAME
 9000 FORMAT(1H0,2H**,1X,2A4,8H EXITED.)
      RETURN
      END
