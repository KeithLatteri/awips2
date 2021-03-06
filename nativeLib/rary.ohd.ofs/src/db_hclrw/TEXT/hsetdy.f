C MODULE HSETDY
C-----------------------------------------------------------------------
C    ROUTINE TO REPLACE A * VALUE FOR DATE WITH THE SYSTEM DATE
C
C             VERSION:  1.0.0
C                DATE:  10-15-81
C              AUTHOR:  JIM ERLANDSON
C                       DATA SCIENCES INC
C***********************************************************************
C          ARGUMENT LIST:
C
C         NAME    TYPE  I/O   DIM   DESCRIPTION
C
C         IARR     I     O     7    OUTPUT ARRAY FOR DATE
C                                    IARR(1)=JULIAN HOUR
C                                    IARR(2)=MONTH
C                                    IARR(3)=DAY OF MONTH
C                                    IARR(4)=YEAR (2 DIGIT)
C                                    IARR(5)=HOUR OF DAY (FOR TZC)
C                                    IARR(6)=TIME ZONE CODE
C                                    IARR(7)=HOUR*100+MINUTES
C***********************************************************************
      SUBROUTINE HSETDY (IARR)

      INCLUDE 'udebug'
      INCLUDE 'udatas'
      INCLUDE 'hclcommon/hdflts'

      DIMENSION IARR(7),IDATE(8)
C
C    ================================= RCS keyword statements ==========
      CHARACTER*68     RCSKW1,RCSKW2
      DATA             RCSKW1,RCSKW2 /                                 '
     .$Source: /fs/hseb/ob72/rfc/ofs/src/db_hclrw/RCS/hsetdy.f,v $
     . $',                                                             '
     .$Id: hsetdy.f,v 1.2 1998/07/02 20:03:17 page Exp $
     . $' /
C    ===================================================================
C

C  GET DEFAULT HOUR AND TIME ZONE CODE

      CALL HSETHR (IARR)

C  CHECK FOR THE DATE CODE OF # IN THE MONTH POSITION TO SEE IF
C   RELATIVE DATING IS TO OCCUR (I.E. RELATIVE TO CURRENT TIME)

      IF (IARR(2).EQ.IPOUND) THEN

C  # (POUND) SIGN FOUND.  SEE WHAT THE DATE SHOULD BE SET TO.
C   FIRST, GET CURRENT TIME, THEN SEE WHAT DIRECTION THE TIME SHOULD
C   BE SET RELATIVE TO NOW (IN THE DAY OF MONTH POS'N; 0 FOR NEGATIVE,
C   1 FOR POSITIVE), AND SEE AT WHAT EVEN INTERVAL OF THE DAY
C   THE DATE SHOULD BE SET TO (IN THE HOUR OF THE DAY POS'N - IF LESS
C   THAN ZERO USE THE DEFAULT INTERVAL STORED IN /HDFLTS/).

         CALL HGETTM (IDATE,'INTL')
         IDIR=IARR(3)
         IF (IARR(5).EQ.0) THEN
            INTVL=INTDFL
         ELSE
            INTVL=IARR(5)
         ENDIF

         ISUB=MOD(IDATE(1),INTVL)
         IF (IDIR.GT.0) THEN

C  LOOKING FORWARD TO THE NEXT INTERVAL

            NJULHR=IDATE(1)+INTVL-ISUB

         ELSE

C  LOOKING BACKWARD TO THE PREVIOUS INTERVAL

            NJULHR=IDATE(1)-ISUB

         ENDIF

C   GET MONTH/DAY/YEAR FOR NEW JULIAN HOUR USING THE DEFAULT TIME
C   ZONE CODE

         JD=NJULHR/24+1
         JH=MOD(NJULHR,24)
         CALL MDYH2(JD,JH,IARR(2),IARR(3),IARR(4),IARR(5),IDUM1,IDUM2,
     *      TIME(3))
         IARR(1)=NJULHR
CC       IARR(4)=MOD(IARR(4),100)
         IARR(6)=TIME(3)
         IARR(7)=IARR(5)*100+IDATE(6)
         GO TO 20
         ENDIF

C     -    -    -    -    -    -    -    -    -    -    -    -    -    -

C   CHECK FOR THE '%' SIGN (SET NEW TIME TO NEXT OR PREVIOUS
C   TIME OF DAY FOR A PARTICULAR TIME ZONE CODE

      IF (IARR(2).EQ.IPRCNT) THEN

C  % (POUND) SIGN FOUND.  SEE WHAT THE DATE SHOULD BE SET TO.
C   FIRST, GET CURRENT TIME, THEN SEE WHAT DIRECTION THE TIME SHOULD
C   BE SET RELATIVE TO NOW (IN THE DAY OF MONTH POS'N; -1 FOR NEGATIVE,
C   +1 FOR POSITIVE), AND SEE AT WHAT TIME OF THE DAY
C   THE DATE SHOULD BE SET TO (IN THE HOUR OF THE DAY POS'N), AND FOR
C   WHICH TIME ZONE CODE (IN THE TZC POS'N).

C  STRATEGY:  GET CURRENT TIME IN THE SPECIFIED TIME ZONE; SUBSTITUTE
C   THE TIME OF DAY FOR THE CURRENT HOUR; DETERMINE JULIAN HOUR FOR
C   THAT NEW TIME.  COMPARE NEW JULIAN HOUR FOR CURRENT JULIAN HOUR.
C   FOR FORWARD MOVEMENT, ADD 24 TO NEW JULIAN HOUR IF NEW JULIAN HOUR
C   IS LESS THAN OR EQUAL TO  CURRENT JULIAN HOUR; OTHERWISE USE NEW
C   JULIAN HOUR DIRECTLY.
C   FOR BACKWARD MOVEMENT, SUBTRACT 24 FROM NEW JULIAN HOUR IF NEW
C   JULIAN HOUR IS GREATER THAN THE CURRENT JULIAN HOUR; OTHERWISE USE
C   NEW JULIAN HOUR DIRECTLY.

         IDIR=IARR(3)
         ITOD=IARR(5)
         ITZC=IARR(6)
         CALL HGETTM (IDATE,ITZC)
         CALL FCITZC(IDUM1,IDUM2,ITZC)
         CALL JULDA(JD,JH,IDATE(2),IDATE(3),IDATE(4),ITOD,IDUM1,IDUM2,
     *      ITZC)
         NJULHR=(JD-1)*24+JH
         IF (IDIR.GT.0) THEN
            IF (NJULHR.LE.IDATE(1)) NJULHR=NJULHR+24
         ELSE
            IF (NJULHR.GT.IDATE(1)) NJULHR=NJULHR-24
         ENDIF

C   GET MONTH/DAY/YEAR FOR NEW JULIAN HOUR USING THE SPECIFIED TIME
C   ZONE CODE

         JD=NJULHR/24+1
         JH=MOD(NJULHR,24)
         CALL MDYH2(JD,JH,IARR(2),IARR(3),IARR(4),IARR(5),IDUM1,IDUM2,
     *      ITZC)
         IARR(1)=NJULHR
CC       IARR(4)=MOD(IARR(4),100)
         IARR(6)=ITZC
         IARR(7)=IARR(5)*100+IDATE(6)
         GO TO 20
      ENDIF

C     -    -    -    -    -    -    -    -    -    -    -    -    -    -

C   CHECK FOR OTHER DATE CODES (* FOR 'STAR' DATE, OTHERWISE
C   DATE IS ALREADY EXPANDED AND JUST CHECK TO ESTABLISH HOUR)

C  REMAINING TYPE DATES NEED FULL EXPANSION ('STAR' DATES ONLY
C   NEED THE HOURS EXPANDED).

C   SEE IF 'STAR' DATE NEEDS TO BE EXPANDED. (THIS IS ESTABLISHED
C   AT START OF RUN OR OVERRIDDEN BY THE @SETTODAY COMMAND).

      IF (IARR(2).NE.IASTR) GO TO 20

      IF (IARR(3).EQ.0) GO TO 10

C          HAVE TO CALCULATE NEW DATE

      IADD=24*IARR(3)
      CALL DDGCH2 (IJUL,TDATES(4),TDATES(2),TDATES(3),IARR(5))
      IJUL=IJUL+IADD
      CALL DDGHC2 (IJUL,IARR(4),IARR(2),IARR(3),IHR)
CC    IARR(4)=MOD(IARR(4),100)
      CALL HCKDTC (IARR(6),ICOD,IERR)
      CALL UJLNTC (IARR(2),IARR(3),IARR(4),IARR(5),IARR(1),ICOD)
      GO TO 20

C      MOVE SYSDATE IN

10    CALL UMEMOV (TDATES(2),IARR(2),3)
      CALL HCKDTC (IARR(6),ICOD,IERR)
      CALL UJLNTC (IARR(2),IARR(3),IARR(4),IARR(5),IARR(1),ICOD)

20    IF (IHCLTR.GT.2) WRITE (IOGDB,30) IARR
30    FORMAT (' EXIT HSETDY - IARR=',5(I7),1X,A4,I5)

      RETURN
      END
