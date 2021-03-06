C MEMBER SPGMDR
C-----------------------------------------------------------------------
C
C DESC PRINT MDR BOX GRID POINT ADDRESS (GMDR) PARAMETERS
C
      SUBROUTINE SPGMDR (IVGMDR,MDRWCL,MDRNCL,MDRSRW,MDRNRW,NMDRGP,
     *   UNUSED,ISTAT)
C
      INTEGER LZEROS/4H0000/
      INTEGER HEADER(3)/4HMDR ,4HCOLU,4HMN  /
      INTEGER ROW/3HROW/
      INTEGER PLUS/1H+/,MINUS/1H-/
      INTEGER*2 NMDRGP(42,1)
      LOGICAL*1 LINE1(133),LINE2(133),LINE3(133)
C
      DIMENSION LINEX(42)
      DIMENSION UNUSED(1)
C
      INCLUDE 'uio'
      INCLUDE 'scommon/sudbgx'
C
C    ================================= RCS keyword statements ==========
      CHARACTER*68     RCSKW1,RCSKW2
      DATA             RCSKW1,RCSKW2 /                                 '
     .$Source: /fs/hseb/ob72/rfc/ofs/src/ppinit_print/RCS/spgmdr.f,v $
     . $',                                                             '
     .$Id: spgmdr.f,v 1.1 1995/09/17 19:14:08 dws Exp $
     . $' /
C    ===================================================================
C
C
C
      IF (ISTRCE.GT.0) WRITE (IOSDBG,100)
      IF (ISTRCE.GT.0) CALL SULINE (IOSDBG,1)
C
C  SET DEBUG LEVEL
      LDEBUG=ISBUG(4HGMDR)
C
      ISTAT=0
C
      LINEL=133
      LINEXL=42*4
      ILSTRT=2
      MCHAR=2
C
C  CHECK NUMBER OF LINES LEFT ON PAGE
      IF (ISLEFT(19+MDRNRW).GT.0) CALL SUPAGE
C
C  PRINT HEADING
      WRITE (LP,110)
      CALL SULINE (LP,2)
      WRITE (LP,120)
      CALL SULINE (LP,2)
      WRITE (LP,130)
      CALL SULINE (LP,2)
C
C  PRINT PARAMETER ARRAY VERSION NUMBER
      IF (LDEBUG.EQ.0) GO TO 10
         WRITE (LP,140) IVGMDR
         CALL SULINE (LP,2)
C
C  PRINT MDR SUBSET
10    WRITE (LP,150) MDRWCL,MDRNCL
      CALL SULINE (LP,2)
      WRITE (LP,160) MDRSRW,MDRNRW
      CALL SULINE (LP,1)
C
C  PRINT TITLE LINE
      IF (ISLEFT(7+MDRNRW).GT.0) CALL SUPAGE
      WRITE (LP,170)
      CALL SULINE (LP,2)
      WRITE (LP,180)
      CALL SULINE (LP,2)
      CALL UREPET (1H ,LINE1,LINEL)
      NCOL=MDRNCL
      MCOL=MDRWCL+MDRNCL-1
      ICENTR=(NCOL*3-10)/2+ILSTRT
      IF (ICENTR+12.GT.LINEL) GO TO 80
      CALL SUBSTR (HEADER,1,12,LINE1,ICENTR)
      WRITE (LP,130)
      WRITE (LP,200) LINE1
      CALL SULINE (LP,2)
C
C  PRINT MDR COLUMN NUMBER HEADING LINE
      CALL UREPET (1H ,LINE2,LINEL)
      INDENT=7
      CALL UINTCH (MDRWCL,MCHAR,CHAR,NFILL,IERR)
      IF (ILSTRT+INDENT.GT.LINEL) GO TO 80
      CALL SUBSTR (CHAR,1,MCHAR,LINE2,ILSTRT-MCHAR+INDENT)
      INT1=(MDRWCL+9)/10*10
      INT2=(MCOL-9)/10*10
      NUM=(INT2-INT1)/10+1
      LCOL=(INT1-MDRWCL)*3+ILSTRT-MCHAR+INDENT
      DO 20 I=1,NUM
         INT=INT1+(I-1)*10
         CALL UINTCH (INT,MCHAR,CHAR,NFILL,IERR)
         IF (LCOL+MCHAR.GT.LINEL) GO TO 80
         CALL SUBSTR (CHAR,1,MCHAR,LINE2,LCOL)
         LCOL=LCOL+30
20       CONTINUE
      CALL UINTCH (MCOL,MCHAR,CHAR,NFILL,IERR)
      LCOL=(NCOL-1)*3+ILSTRT-MCHAR+INDENT
      IF (LCOL+MCHAR.GT.LINEL) GO TO 80
      CALL SUBSTR (CHAR,1,MCHAR,LINE2,LCOL)
      WRITE (LP,200) LINE2
      CALL SULINE (LP,1)
C
C  PRINT DASH/PLUS HEADING LINE
      CALL UREPET (1H ,LINE3,LINEL)
      CALL SUBSTR (ROW,1,3,LINE3,2)
      LCOL=INDENT-1
      DO 30 I=1,NCOL
         IF (LCOL.GT.LINEL) GO TO 80
         CALL SUBSTR (MINUS,1,1,LINE3,LCOL)
         IF (LCOL+1.GT.LINEL) GO TO 80
         CALL SUBSTR (MINUS,1,1,LINE3,LCOL+1)
         IF (LCOL+2.GT.LINEL) GO TO 80
         CALL SUBSTR (PLUS,1,1,LINE3,LCOL+2)
         LCOL=LCOL+3
30       CONTINUE
      WRITE (LP,200) LINE3
      CALL SULINE (LP,1)
C
C  PRINT GRID POINT ADDRESS OF GRID POINT NEAREST CENTROID OF MDR BOX
      ICOL=MDRWCL
      NCOL=MDRNCL
      LCOL=ICOL+NCOL-1
      IROW=MDRSRW
      NROW=MDRNRW
      LROW=IROW+NROW-1
      NIR=LROW
      DO 50 IR=IROW,LROW
         CALL UREPET (1H ,LINEX,LINEXL)
         DO 40 IC=ICOL,LCOL
            NIC=IC-ICOL+1
            IVAL=NMDRGP(NIC,NIR)/100
            IF (IVAL.EQ.0) GO TO 35
               CALL UINTCH (IVAL,4,CHAR,NFILL,IERR)
               CALL SUBSTR (CHAR,2,4,LINEX(NIC),1)
35          IF (LDEBUG.GT.1) WRITE (IOSDBG,205) NIC,IVAL,LINEX(NIC)
            IF (LDEBUG.GT.1) CALL SULINE (IOSDBG,1)
40          CONTINUE
         WRITE (LP,210) NIR,LINEX
         CALL SULINE (LP,1)
         NIR=NIR-1
50       CONTINUE
      WRITE (LP,185)
      CALL SULINE (LP,2)
      IF (ISLEFT(7+MDRNRW).GT.0) CALL SUPAGE
      WRITE (LP,130)
      CALL SULINE (LP,1)
      WRITE (LP,190)
      CALL SULINE (LP,2)
      WRITE (LP,130)
      CALL SULINE (LP,1)
      WRITE (LP,200) LINE1
      CALL SULINE (LP,1)
      WRITE (LP,200) LINE2
      CALL SULINE (LP,1)
      WRITE (LP,200) LINE3
      CALL SULINE (LP,1)
      NIR=LROW
      DO 70 IR=IROW,LROW
         CALL UREPET (1H ,LINEX,LINEXL)
         DO 60 IC=ICOL,LCOL
            NIC=IC-ICOL+1
            IVAL=NMDRGP(NIC,NIR)
            IVAL=IABS(IVAL)
            IF (IVAL.EQ.0) GO TO 55
               IVAL=IVAL-(IVAL/100*100)
               CALL UINTCH (IVAL,4,CHAR,NFILL,IERR)
               LINEX(NIC)=LZEROS
               CALL SUBSTR (CHAR,5-NFILL,NFILL,LINEX(NIC),3-NFILL)
55          IF (LDEBUG.GT.1) WRITE (IOSDBG,205) NIC,IVAL,LINEX(NIC)
            IF (LDEBUG.GT.1) CALL SULINE (IOSDBG,1)
60          CONTINUE
         WRITE (LP,215) NIR,LINEX
         CALL SULINE (LP,1)
         NIR=NIR-1
70       CONTINUE
C
C  PRINT UNUSED POSITIONS
      IF (LDEBUG.EQ.0) GO TO 90
         NUNSED=2
         WRITE (LP,220) NUNSED
         GO TO 90
C
C  ATTEMPT TO WRITE PAST END OF PRINT RECORD
80    WRITE (LP,230) LINEL
      CALL SUERRS (LP,2,-1)
C
90    WRITE (LP,110)
      CALL SULINE (LP,2)
C
      IF (ISTRCE.GT.0) WRITE (IOSDBG,240)
      IF (ISTRCE.GT.0) CALL SULINE (IOSDBG,1)
C
      RETURN
C
C- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
C
100   FORMAT (' *** ENTER SPGMDR')
110   FORMAT (1H0,132(1H-))
120   FORMAT ('0*--> GMDR PARAMETERS')
130   FORMAT (1H )
140   FORMAT ('0PARAMETER ARRAY VERSION NUMBER = ',I2)
150   FORMAT ('0MDR SUBSET :  WESTERN COLUMN=',I3,5X,
     *   'NUMBER OF COLUMNS=',I3)
160   FORMAT (T16,'SOUTHERN ROW=',I3,5X,'NUMBER OF ROWS=',I3)
170   FORMAT ('0GRID POINT ADDRESS OF GRID POINT NEAREST CENTROID OF ',
     *   'MDR BOX')
180   FORMAT ('0- GRID BOX NUMBERS -')
185   FORMAT ('0NOTE: NEGATIVE GRID BOX NUMBER INDICATES MDR PCPN ',
     *   'ESTIMATES ARE TO BE USED REGARDLESS OF ANY MDR RUN-TIME ',
     *   'OPTIONS.')
190   FORMAT ('0- TENTHS OF DEGREE NORTH OF SOUTHEAST CORNER AND ',
     *   'AND WEST OF SOUTHEAST CORNER -')
200   FORMAT (133A1)
205   FORMAT (' NIC=',I2,3X,'IVAL=',I5,3X,'LINEX(NIC)=',A4)
210   FORMAT (1H ,I2,2X,42A3)
215   FORMAT (1H ,I2,2X,42(1X,A2))
220   FORMAT ('0NUMBER OF UNUSED POSITIONS = ',I2)
230   FORMAT ('0*** ERROR - IN SPGMDR - ATTEMPT TO WRITE PAST END OF ',
     *   'PRINTER LINE. MAXIMUM CHARACTERS ALLOWED IS ',I3,'.')
240   FORMAT (' *** EXIT SPGMDR')
C
      END
