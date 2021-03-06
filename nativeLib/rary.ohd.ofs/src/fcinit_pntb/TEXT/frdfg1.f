C MODULE FRDFG1
C-----------------------------------------------------------------------
C
      SUBROUTINE FRDFG1(IDT,NFPM,ISC,ITSC,NFCO,NXFCO)
C
C   THIS SUBROUTINE READS SAC-SMA INPUT CARDS RELATED TO FROZEN GROUND.
C
C     SUBROUTINE WRITTEN BY - ERIC ANDERSON-HRL JUNE 1980
C
      CHARACTER*80 CARD
C
      INCLUDE 'common/ionum'
      COMMON/FINFG1/TAID(2),TATYPE,ITTA,WEID(2),WETYPE,ITWE,LWE,FIID(2),
     1 ITFI,LFI,FGPM(10),FGCO(6),PTA,PWE
C
C    ================================= RCS keyword statements ==========
      CHARACTER*68     RCSKW1,RCSKW2
      DATA             RCSKW1,RCSKW2 /                                 '
     .$Source: /fs/hseb/ob72/rfc/ofs/src/fcinit_pntb/RCS/frdfg1.f,v $
     . $',                                                             '
     .$Id: frdfg1.f,v 1.2 2002/02/11 13:07:21 michaelo Exp $
     . $' /
C    ===================================================================
C
      DATA FGIX/4HFGIX/
      DATA TEMP,DL/4HTEMP,4HL   /
      DATA BLANK/4H    /
C
C
      NXLOC=18
      NFCO=6
      NXFCO=1
C
C  READ FROZEN GROUND CARD NUMBER 1
      READ (IN,900) TAID,TATYPE,ITTA,WEID,WETYPE,ITWE,FIID,ITFI
  900 FORMAT (2X,2A4,1X,A4,3X,I2,17X,2A4,1X,A4,3X,I2,2X,2A4,3X,I2)
      IF ((ITTA/IDT)*IDT.EQ.ITTA) GO TO 101
      WRITE (IPR,901) TAID,TATYPE,ITTA,IDT
  901 FORMAT ('0**ERROR** THE TIME INTERVAL OF TIME SERIES (ID=',
     1 2A4,' TYPE=',A4,' ',I2,' HR) IS NOT A MUTIPLE OF THE ',
     2 'TIME INTERVAL (',I2,' HR) OF THE RAIN+MELT DATA.')
      CALL ERROR
  101 CALL CHEKTS (TAID,TATYPE,ITTA,1,TEMP,0,1,IERR)
      LWE=0
      IF ((WEID(1).EQ.BLANK).AND.(WEID(2).EQ.BLANK)) GO TO 105
      LWE=NXLOC
      NXLOC=NXLOC+4
      NXFCO=NXFCO+1
      CALL CHEKTS (WEID,WETYPE,ITWE,1,DL,0,1,IERR)
      IF (ISC.EQ.0) GO TO 102
      IF (ITWE.EQ.ITSC) GO TO 105
      WRITE (IPR,902) ITWE,ITSC
  902 FORMAT ('0**ERROR** TIME INTERVAL OF WATER-EQUIVALENT DATA (',
     1 I2,' HR) IS NOT EQUAL TO THE TIME INTERVAL OF THE ',
     2 'AREAL SNOW COVER DATA (',I2,' HR).')
      CALL ERROR
      GO TO 105
  102 IF ((ITWE/IDT)*IDT.EQ.ITWE) GO TO 105
      WRITE (IPR,901) WEID,WETYPE,ITWE,IDT
      CALL ERROR
  105 LFI=0
      IF ((FIID(1).EQ.BLANK).AND.(FIID(2).EQ.BLANK)) GO TO 110
      LFI=NXLOC
      NXLOC=NXLOC+3
      CALL CHEKTS (FIID,FGIX,ITFI,1,TEMP,0,1,IERR)
      IF (( ITFI/IDT)*IDT.EQ.ITFI) GO TO 110
      WRITE (IPR,901) FIID,FGIX,ITFI,IDT
      CALL ERROR
C
C  READ FROZEN GROUND CARD NUMBER 2
  110 READ (IN,'(A)') CARD
      READ (CARD,903,ERR=115) FGPM
  903 FORMAT (10F5.0)
      GO TO 116
  115 CALL FRDERR (IPR,' ',CARD)
116   NFPM=NXLOC-1
C
C  READ FROZEN GROUND CARD NUMBER 3 - INITIAL CARRYOVER
      READ (IN,'(A)') CARD
      READ (CARD,903,ERR=117) FGCO,PTA,PWE
      GO TO 118
  117 CALL FRDERR (IPR,' ',CARD)
C
118   RETURN
C
      END
