/*
** Generated by X-Designer
*/
/*
**LIBS: -lXm -lXt -lX11
*/

#include <stdlib.h>
#include <X11/Xatom.h>
#include <X11/Intrinsic.h>
#include <X11/Shell.h>

#include <Xm/Xm.h>
#include <Xm/CascadeB.h>
#include <Xm/DialogS.h>
#include <Xm/DrawingA.h>
#include <Xm/Form.h>
#include <Xm/PushB.h>
#include <Xm/RowColumn.h>
#include <Xm/Separator.h>
#include <Xm/ToggleB.h>


#include "TimeSeries.h"

Widget TimeSeriesDS = (Widget) NULL;
Widget TSFO = (Widget) NULL;
Widget TSMB = (Widget) NULL;
Widget TS_FileCB = (Widget) NULL;
Widget TSFileMenu = (Widget) NULL;
Widget TSCDialogPB = (Widget) NULL;
Widget TSSavePB = (Widget) NULL;
Widget TSPrintCB = (Widget) NULL;
Widget TSPrintMO = (Widget) NULL;
Widget PInversePB = (Widget) NULL;
Widget PnonInversePB = (Widget) NULL;
Widget TSClosePB = (Widget) NULL;
Widget TSPgDn = (Widget) NULL;
Widget TSPgUp = (Widget) NULL;
Widget TSGraph = (Widget) NULL;
Widget TSGraphMenu = (Widget) NULL;
Widget TSZoomCB = (Widget) NULL;
Widget TSZoomMO = (Widget) NULL;
Widget TSSetTB = (Widget) NULL;
Widget TSResetTB = (Widget) NULL;
Widget TSPcAsPPCB = (Widget) NULL;
Widget TSShowPCAsPPMO = (Widget) NULL;
Widget TSOffTB = (Widget) NULL;
Widget TSInterpolateTB = (Widget) NULL;
Widget TSAssignTB = (Widget) NULL;
Widget TSShowFcstTB = (Widget) NULL;
Widget TSscalePB = (Widget) NULL;
Widget TSscaleMO = (Widget) NULL;
Widget TSScaleDataOnlyTB = (Widget) NULL;
Widget TSScaleDataShowCategTB = (Widget) NULL;
Widget TSScaleDataCategoriesTB = (Widget) NULL;
Widget TSCDialog = (Widget) NULL;
Widget TSCDialogMenu = (Widget) NULL;
Widget TSGridLinesTB = (Widget) NULL;
Widget TSPointLine = (Widget) NULL;
Widget TSPlotMO = (Widget) NULL;
Widget TSPointsTB = (Widget) NULL;
Widget TSLinesTB = (Widget) NULL;
Widget TSBothTB = (Widget) NULL;
Widget TSBatchScale = (Widget) NULL;
Widget TSBatchScaleMO = (Widget) NULL;
Widget TSBatchDataOnlyTB = (Widget) NULL;
Widget TSBatchShowCategTB = (Widget) NULL;
Widget TSBatchCategoriesTB = (Widget) NULL;
Widget TSEditCB = (Widget) NULL;
Widget TSEditMO = (Widget) NULL;
Widget TSTracePB = (Widget) NULL;
Widget TSAddPB = (Widget) NULL;
Widget TSDeletePB = (Widget) NULL;
Widget TSMovePB = (Widget) NULL;
Widget TSSetMissingPB = (Widget) NULL;
Widget TSEditDonePB = (Widget) NULL;
Widget TSEditCancelPB = (Widget) NULL;
Widget TSDrawArea = (Widget) NULL;



void create_TimeSeriesDS (Widget parent)
{
	Widget children[9];      /* Children to manage */
	Arg al[64];                    /* Arg List */
	register int ac = 0;           /* Arg Count */
	XrmValue from_value, to_value; /* For resource conversion */
	XPointer to_address; /* For Thread-safe resource conversion */ 
	XtPointer tmp_value;             /* ditto */
	XmString xmstrings[16];    /* temporary storage for XmStrings */
	Widget separator3 = (Widget)NULL;
	Widget separator4 = (Widget)NULL;

	XtSetArg(al[ac], XmNx, 202); ac++;
	XtSetArg(al[ac], XmNy, 163); ac++;
	XtSetArg(al[ac], XmNwidth, 990); ac++;
	XtSetArg(al[ac], XmNheight, 900); ac++;
	XtSetArg(al[ac], XmNallowShellResize, TRUE); ac++;
	XtSetArg(al[ac], XmNoverrideRedirect, FALSE); ac++;
	XtSetArg(al[ac], XmNsaveUnder, TRUE); ac++;
	XtSetArg(al[ac], XmNtitle, "Time Series Display"); ac++;
	TimeSeriesDS = XmCreateDialogShell ( parent, (char *) "TimeSeriesDS", al, ac );
	ac = 0;
	XtSetArg(al[ac], XmNx, 202); ac++;
	XtSetArg(al[ac], XmNy, 163); ac++;
	XtSetArg(al[ac], XmNwidth, 990); ac++;
	XtSetArg(al[ac], XmNheight, 900); ac++;
	XtSetArg(al[ac], XmNdialogStyle, XmDIALOG_MODELESS); ac++;
	XtSetArg(al[ac], XmNresizePolicy, XmRESIZE_GROW); ac++;
	XtSetArg(al[ac], XmNautoUnmanage, FALSE); ac++;
	TSFO = XmCreateForm ( TimeSeriesDS, (char *) "TSFO", al, ac );
	ac = 0;
	TSMB = XmCreateMenuBar ( TSFO, (char *) "TSMB", al, ac );
	xmstrings[0] = XmStringGenerate ( (XtPointer) "File", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNlabelString, xmstrings[0]); ac++;
	TS_FileCB = XmCreateCascadeButton ( TSMB, (char *) "TS_FileCB", al, ac );
	ac = 0;
	XmStringFree ( xmstrings [ 0 ] );
	XtSetArg(al[ac], XmNtearOffModel, XmTEAR_OFF_ENABLED); ac++;
	TSFileMenu = XmCreatePulldownMenu ( TSMB, (char *) "TSFileMenu", al, ac );
	ac = 0;
	xmstrings[0] = XmStringGenerate ( (XtPointer) "Time Series Control", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNlabelString, xmstrings[0]); ac++;

	from_value.addr = "T" ;
	from_value.size = strlen( from_value.addr ) + 1;
	to_value.size = sizeof(KeySym);
	to_value.addr = (XPointer) &to_address;
	XtConvertAndStore (TSFileMenu, XmRString, &from_value, XmRKeySym, &to_value);

	if ( to_value.addr ) {
		XtSetArg(al[ac], XmNmnemonic, (*((KeySym*) to_value.addr))); ac++;
	}
	XtSetArg(al[ac], XmNaccelerator, "Ctrl<Key>T"); ac++;
	xmstrings[1] = XmStringGenerate ( (XtPointer) "Ctrl-T", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNacceleratorText, xmstrings[1]); ac++;
	TSCDialogPB = XmCreatePushButton ( TSFileMenu, (char *) "TSCDialogPB", al, ac );
	ac = 0;
	XmStringFree ( xmstrings [ 0 ] );
	XmStringFree ( xmstrings [ 1 ] );
	xmstrings[0] = XmStringGenerate ( (XtPointer) "Save", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNlabelString, xmstrings[0]); ac++;

	from_value.addr = "S" ;
	from_value.size = strlen( from_value.addr ) + 1;
	to_value.size = sizeof(KeySym);
	to_value.addr = (XPointer) &to_address;
	XtConvertAndStore (TSFileMenu, XmRString, &from_value, XmRKeySym, &to_value);

	if ( to_value.addr ) {
		XtSetArg(al[ac], XmNmnemonic, (*((KeySym*) to_value.addr))); ac++;
	}
	XtSetArg(al[ac], XmNaccelerator, "Ctrl<Key>S"); ac++;
	xmstrings[1] = XmStringGenerate ( (XtPointer) "Ctrl-S", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNacceleratorText, xmstrings[1]); ac++;
	TSSavePB = XmCreatePushButton ( TSFileMenu, (char *) "TSSavePB", al, ac );
	ac = 0;
	XmStringFree ( xmstrings [ 0 ] );
	XmStringFree ( xmstrings [ 1 ] );
	xmstrings[0] = XmStringGenerate ( (XtPointer) "Print", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNlabelString, xmstrings[0]); ac++;
	TSPrintCB = XmCreateCascadeButton ( TSFileMenu, (char *) "TSPrintCB", al, ac );
	ac = 0;
	XmStringFree ( xmstrings [ 0 ] );
	XtSetArg(al[ac], XmNtearOffModel, XmTEAR_OFF_DISABLED); ac++;
	TSPrintMO = XmCreatePulldownMenu ( TSFileMenu, (char *) "TSPrintMO", al, ac );
	ac = 0;
	xmstrings[0] = XmStringGenerate ( (XtPointer) "Reverse Video", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNlabelString, xmstrings[0]); ac++;
	PInversePB = XmCreatePushButton ( TSPrintMO, (char *) "PInversePB", al, ac );
	ac = 0;
	XmStringFree ( xmstrings [ 0 ] );
	xmstrings[0] = XmStringGenerate ( (XtPointer) "Normal", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNlabelString, xmstrings[0]); ac++;
	PnonInversePB = XmCreatePushButton ( TSPrintMO, (char *) "PnonInversePB", al, ac );
	ac = 0;
	XmStringFree ( xmstrings [ 0 ] );
	xmstrings[0] = XmStringGenerate ( (XtPointer) "Quit", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNlabelString, xmstrings[0]); ac++;

	from_value.addr = "Q" ;
	from_value.size = strlen( from_value.addr ) + 1;
	to_value.size = sizeof(KeySym);
	to_value.addr = (XPointer) &to_address;
	XtConvertAndStore (TSFileMenu, XmRString, &from_value, XmRKeySym, &to_value);

	if ( to_value.addr ) {
		XtSetArg(al[ac], XmNmnemonic, (*((KeySym*) to_value.addr))); ac++;
	}
	XtSetArg(al[ac], XmNaccelerator, "Ctrl<Key>Q"); ac++;
	xmstrings[1] = XmStringGenerate ( (XtPointer) "Ctrl-Q", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNacceleratorText, xmstrings[1]); ac++;
	TSClosePB = XmCreatePushButton ( TSFileMenu, (char *) "TSClosePB", al, ac );
	ac = 0;
	XmStringFree ( xmstrings [ 0 ] );
	XmStringFree ( xmstrings [ 1 ] );
	xmstrings[0] = XmStringGenerate ( (XtPointer) "PgDown", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNlabelString, xmstrings[0]); ac++;
	TSPgDn = XmCreateCascadeButton ( TSMB, (char *) "TSPgDn", al, ac );
	ac = 0;
	XmStringFree ( xmstrings [ 0 ] );
	xmstrings[0] = XmStringGenerate ( (XtPointer) "PgUp", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNlabelString, xmstrings[0]); ac++;
	TSPgUp = XmCreateCascadeButton ( TSMB, (char *) "TSPgUp", al, ac );
	ac = 0;
	XmStringFree ( xmstrings [ 0 ] );
	xmstrings[0] = XmStringGenerate ( (XtPointer) "Graph", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNlabelString, xmstrings[0]); ac++;
	TSGraph = XmCreateCascadeButton ( TSMB, (char *) "TSGraph", al, ac );
	ac = 0;
	XmStringFree ( xmstrings [ 0 ] );
	XtSetArg(al[ac], XmNtearOffModel, XmTEAR_OFF_ENABLED); ac++;
	TSGraphMenu = XmCreatePulldownMenu ( TSMB, (char *) "TSGraphMenu", al, ac );
	ac = 0;
	xmstrings[0] = XmStringGenerate ( (XtPointer) "Zoom", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNlabelString, xmstrings[0]); ac++;
	TSZoomCB = XmCreateCascadeButton ( TSGraphMenu, (char *) "TSZoomCB", al, ac );
	ac = 0;
	XmStringFree ( xmstrings [ 0 ] );
	TSZoomMO = XmCreatePulldownMenu ( TSGraphMenu, (char *) "TSZoomMO", al, ac );
	xmstrings[0] = XmStringGenerate ( (XtPointer) "Set", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNlabelString, xmstrings[0]); ac++;

	from_value.addr = "Z" ;
	from_value.size = strlen( from_value.addr ) + 1;
	to_value.size = sizeof(KeySym);
	to_value.addr = (XPointer) &to_address;
	XtConvertAndStore (TSZoomMO, XmRString, &from_value, XmRKeySym, &to_value);

	if ( to_value.addr ) {
		XtSetArg(al[ac], XmNmnemonic, (*((KeySym*) to_value.addr))); ac++;
	}
	XtSetArg(al[ac], XmNaccelerator, "Ctrl<Key>Z"); ac++;
	xmstrings[1] = XmStringGenerate ( (XtPointer) "Ctrl-Z", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNacceleratorText, xmstrings[1]); ac++;
	TSSetTB = XmCreateToggleButton ( TSZoomMO, (char *) "TSSetTB", al, ac );
	ac = 0;
	XmStringFree ( xmstrings [ 0 ] );
	XmStringFree ( xmstrings [ 1 ] );
	xmstrings[0] = XmStringGenerate ( (XtPointer) "Reset", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNlabelString, xmstrings[0]); ac++;

	from_value.addr = "R" ;
	from_value.size = strlen( from_value.addr ) + 1;
	to_value.size = sizeof(KeySym);
	to_value.addr = (XPointer) &to_address;
	XtConvertAndStore (TSZoomMO, XmRString, &from_value, XmRKeySym, &to_value);

	if ( to_value.addr ) {
		XtSetArg(al[ac], XmNmnemonic, (*((KeySym*) to_value.addr))); ac++;
	}
	XtSetArg(al[ac], XmNaccelerator, " Ctrl<Key>R"); ac++;
	xmstrings[1] = XmStringGenerate ( (XtPointer) "Ctrl-R", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNacceleratorText, xmstrings[1]); ac++;
	TSResetTB = XmCreateToggleButton ( TSZoomMO, (char *) "TSResetTB", al, ac );
	ac = 0;
	XmStringFree ( xmstrings [ 0 ] );
	XmStringFree ( xmstrings [ 1 ] );
	xmstrings[0] = XmStringGenerate ( (XtPointer) "Show PC as 1Hr. PP", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNlabelString, xmstrings[0]); ac++;
	TSPcAsPPCB = XmCreateCascadeButton ( TSGraphMenu, (char *) "TSPcAsPPCB", al, ac );
	ac = 0;
	XmStringFree ( xmstrings [ 0 ] );
	TSShowPCAsPPMO = XmCreatePulldownMenu ( TSGraphMenu, (char *) "TSShowPCAsPPMO", al, ac );
	xmstrings[0] = XmStringGenerate ( (XtPointer) "Off", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNlabelString, xmstrings[0]); ac++;

	from_value.addr = "O" ;
	from_value.size = strlen( from_value.addr ) + 1;
	to_value.size = sizeof(KeySym);
	to_value.addr = (XPointer) &to_address;
	XtConvertAndStore (TSShowPCAsPPMO, XmRString, &from_value, XmRKeySym, &to_value);

	if ( to_value.addr ) {
		XtSetArg(al[ac], XmNmnemonic, (*((KeySym*) to_value.addr))); ac++;
	}
	XtSetArg(al[ac], XmNaccelerator, "Ctrl<Key>O"); ac++;
	xmstrings[1] = XmStringGenerate ( (XtPointer) "Ctrl-O", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNacceleratorText, xmstrings[1]); ac++;
	TSOffTB = XmCreateToggleButton ( TSShowPCAsPPMO, (char *) "TSOffTB", al, ac );
	ac = 0;
	XmStringFree ( xmstrings [ 0 ] );
	XmStringFree ( xmstrings [ 1 ] );
	xmstrings[0] = XmStringGenerate ( (XtPointer) "Interpolate", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNlabelString, xmstrings[0]); ac++;

	from_value.addr = "I" ;
	from_value.size = strlen( from_value.addr ) + 1;
	to_value.size = sizeof(KeySym);
	to_value.addr = (XPointer) &to_address;
	XtConvertAndStore (TSShowPCAsPPMO, XmRString, &from_value, XmRKeySym, &to_value);

	if ( to_value.addr ) {
		XtSetArg(al[ac], XmNmnemonic, (*((KeySym*) to_value.addr))); ac++;
	}
	XtSetArg(al[ac], XmNaccelerator, "Ctrl<Key>I"); ac++;
	xmstrings[1] = XmStringGenerate ( (XtPointer) "Ctrl-I", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNacceleratorText, xmstrings[1]); ac++;
	TSInterpolateTB = XmCreateToggleButton ( TSShowPCAsPPMO, (char *) "TSInterpolateTB", al, ac );
	ac = 0;
	XmStringFree ( xmstrings [ 0 ] );
	XmStringFree ( xmstrings [ 1 ] );
	xmstrings[0] = XmStringGenerate ( (XtPointer) "Assign", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNlabelString, xmstrings[0]); ac++;

	from_value.addr = "A" ;
	from_value.size = strlen( from_value.addr ) + 1;
	to_value.size = sizeof(KeySym);
	to_value.addr = (XPointer) &to_address;
	XtConvertAndStore (TSShowPCAsPPMO, XmRString, &from_value, XmRKeySym, &to_value);

	if ( to_value.addr ) {
		XtSetArg(al[ac], XmNmnemonic, (*((KeySym*) to_value.addr))); ac++;
	}
	XtSetArg(al[ac], XmNaccelerator, "Ctrl<Key>A"); ac++;
	xmstrings[1] = XmStringGenerate ( (XtPointer) "Ctrl-A", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNacceleratorText, xmstrings[1]); ac++;
	TSAssignTB = XmCreateToggleButton ( TSShowPCAsPPMO, (char *) "TSAssignTB", al, ac );
	ac = 0;
	XmStringFree ( xmstrings [ 0 ] );
	XmStringFree ( xmstrings [ 1 ] );
	xmstrings[0] = XmStringGenerate ( (XtPointer) "Show Latest Forecast Only", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNlabelString, xmstrings[0]); ac++;

	from_value.addr = "F" ;
	from_value.size = strlen( from_value.addr ) + 1;
	to_value.size = sizeof(KeySym);
	to_value.addr = (XPointer) &to_address;
	XtConvertAndStore (TSGraphMenu, XmRString, &from_value, XmRKeySym, &to_value);

	if ( to_value.addr ) {
		XtSetArg(al[ac], XmNmnemonic, (*((KeySym*) to_value.addr))); ac++;
	}
	XtSetArg(al[ac], XmNset, XmSET); ac++;
	XtSetArg(al[ac], XmNvisibleWhenOff, TRUE); ac++;
	XtSetArg(al[ac], XmNaccelerator, "Ctrl<Key>F"); ac++;
	xmstrings[1] = XmStringGenerate ( (XtPointer) "Ctrl-F", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNacceleratorText, xmstrings[1]); ac++;
	TSShowFcstTB = XmCreateToggleButton ( TSGraphMenu, (char *) "TSShowFcstTB", al, ac );
	ac = 0;
	XmStringFree ( xmstrings [ 0 ] );
	XmStringFree ( xmstrings [ 1 ] );
	xmstrings[0] = XmStringGenerate ( (XtPointer) "Scale Stages", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNlabelString, xmstrings[0]); ac++;
	TSscalePB = XmCreateCascadeButton ( TSGraphMenu, (char *) "TSscalePB", al, ac );
	ac = 0;
	XmStringFree ( xmstrings [ 0 ] );
	TSscaleMO = XmCreatePulldownMenu ( TSGraphMenu, (char *) "TSscaleMO", al, ac );
	xmstrings[0] = XmStringGenerate ( (XtPointer) "Data Only", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNlabelString, xmstrings[0]); ac++;

	from_value.addr = "1" ;
	from_value.size = strlen( from_value.addr ) + 1;
	to_value.size = sizeof(KeySym);
	to_value.addr = (XPointer) &to_address;
	XtConvertAndStore (TSscaleMO, XmRString, &from_value, XmRKeySym, &to_value);

	if ( to_value.addr ) {
		XtSetArg(al[ac], XmNmnemonic, (*((KeySym*) to_value.addr))); ac++;
	}
	XtSetArg(al[ac], XmNaccelerator, "Ctrl<Key>1"); ac++;
	xmstrings[1] = XmStringGenerate ( (XtPointer) "Ctrl-1", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNacceleratorText, xmstrings[1]); ac++;
	TSScaleDataOnlyTB = XmCreateToggleButton ( TSscaleMO, (char *) "TSScaleDataOnlyTB", al, ac );
	ac = 0;
	XmStringFree ( xmstrings [ 0 ] );
	XmStringFree ( xmstrings [ 1 ] );
	xmstrings[0] = XmStringGenerate ( (XtPointer) "Data Only, Show Categories", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNlabelString, xmstrings[0]); ac++;

	from_value.addr = "2" ;
	from_value.size = strlen( from_value.addr ) + 1;
	to_value.size = sizeof(KeySym);
	to_value.addr = (XPointer) &to_address;
	XtConvertAndStore (TSscaleMO, XmRString, &from_value, XmRKeySym, &to_value);

	if ( to_value.addr ) {
		XtSetArg(al[ac], XmNmnemonic, (*((KeySym*) to_value.addr))); ac++;
	}
	XtSetArg(al[ac], XmNaccelerator, "Ctrl<Key>2"); ac++;
	xmstrings[1] = XmStringGenerate ( (XtPointer) "Ctrl-2", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNacceleratorText, xmstrings[1]); ac++;
	TSScaleDataShowCategTB = XmCreateToggleButton ( TSscaleMO, (char *) "TSScaleDataShowCategTB", al, ac );
	ac = 0;
	XmStringFree ( xmstrings [ 0 ] );
	XmStringFree ( xmstrings [ 1 ] );
	xmstrings[0] = XmStringGenerate ( (XtPointer) "Data and Categories", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNlabelString, xmstrings[0]); ac++;

	from_value.addr = "3" ;
	from_value.size = strlen( from_value.addr ) + 1;
	to_value.size = sizeof(KeySym);
	to_value.addr = (XPointer) &to_address;
	XtConvertAndStore (TSscaleMO, XmRString, &from_value, XmRKeySym, &to_value);

	if ( to_value.addr ) {
		XtSetArg(al[ac], XmNmnemonic, (*((KeySym*) to_value.addr))); ac++;
	}
	XtSetArg(al[ac], XmNaccelerator, "Ctrl<Key>3"); ac++;
	xmstrings[1] = XmStringGenerate ( (XtPointer) "Ctrl-3", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNacceleratorText, xmstrings[1]); ac++;
	TSScaleDataCategoriesTB = XmCreateToggleButton ( TSscaleMO, (char *) "TSScaleDataCategoriesTB", al, ac );
	ac = 0;
	XmStringFree ( xmstrings [ 0 ] );
	XmStringFree ( xmstrings [ 1 ] );
	xmstrings[0] = XmStringGenerate ( (XtPointer) "Options", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNlabelString, xmstrings[0]); ac++;
	TSCDialog = XmCreateCascadeButton ( TSMB, (char *) "TSCDialog", al, ac );
	ac = 0;
	XmStringFree ( xmstrings [ 0 ] );
	XtSetArg(al[ac], XmNtearOffModel, XmTEAR_OFF_ENABLED); ac++;
	TSCDialogMenu = XmCreatePulldownMenu ( TSMB, (char *) "TSCDialogMenu", al, ac );
	ac = 0;
	xmstrings[0] = XmStringGenerate ( (XtPointer) "Grid Lines", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNlabelString, xmstrings[0]); ac++;

	from_value.addr = "G" ;
	from_value.size = strlen( from_value.addr ) + 1;
	to_value.size = sizeof(KeySym);
	to_value.addr = (XPointer) &to_address;
	XtConvertAndStore (TSCDialogMenu, XmRString, &from_value, XmRKeySym, &to_value);

	if ( to_value.addr ) {
		XtSetArg(al[ac], XmNmnemonic, (*((KeySym*) to_value.addr))); ac++;
	}
	XtSetArg(al[ac], XmNset, XmUNSET); ac++;
	XtSetArg(al[ac], XmNvisibleWhenOff, TRUE); ac++;
	XtSetArg(al[ac], XmNaccelerator, "Ctrl<Key>G"); ac++;
	xmstrings[1] = XmStringGenerate ( (XtPointer) "Ctrl-G", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNacceleratorText, xmstrings[1]); ac++;
	TSGridLinesTB = XmCreateToggleButton ( TSCDialogMenu, (char *) "TSGridLinesTB", al, ac );
	ac = 0;
	XmStringFree ( xmstrings [ 0 ] );
	XmStringFree ( xmstrings [ 1 ] );
	xmstrings[0] = XmStringGenerate ( (XtPointer) "Plot", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNlabelString, xmstrings[0]); ac++;
	TSPointLine = XmCreateCascadeButton ( TSCDialogMenu, (char *) "TSPointLine", al, ac );
	ac = 0;
	XmStringFree ( xmstrings [ 0 ] );
	TSPlotMO = XmCreatePulldownMenu ( TSCDialogMenu, (char *) "TSPlotMO", al, ac );
	xmstrings[0] = XmStringGenerate ( (XtPointer) "Points", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNlabelString, xmstrings[0]); ac++;

	from_value.addr = "P" ;
	from_value.size = strlen( from_value.addr ) + 1;
	to_value.size = sizeof(KeySym);
	to_value.addr = (XPointer) &to_address;
	XtConvertAndStore (TSPlotMO, XmRString, &from_value, XmRKeySym, &to_value);

	if ( to_value.addr ) {
		XtSetArg(al[ac], XmNmnemonic, (*((KeySym*) to_value.addr))); ac++;
	}
	XtSetArg(al[ac], XmNaccelerator, "Ctrl<Key>P"); ac++;
	xmstrings[1] = XmStringGenerate ( (XtPointer) "Ctrl-P", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNacceleratorText, xmstrings[1]); ac++;
	TSPointsTB = XmCreateToggleButton ( TSPlotMO, (char *) "TSPointsTB", al, ac );
	ac = 0;
	XmStringFree ( xmstrings [ 0 ] );
	XmStringFree ( xmstrings [ 1 ] );
	xmstrings[0] = XmStringGenerate ( (XtPointer) "Lines", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNlabelString, xmstrings[0]); ac++;

	from_value.addr = "L" ;
	from_value.size = strlen( from_value.addr ) + 1;
	to_value.size = sizeof(KeySym);
	to_value.addr = (XPointer) &to_address;
	XtConvertAndStore (TSPlotMO, XmRString, &from_value, XmRKeySym, &to_value);

	if ( to_value.addr ) {
		XtSetArg(al[ac], XmNmnemonic, (*((KeySym*) to_value.addr))); ac++;
	}
	XtSetArg(al[ac], XmNaccelerator, "Ctrl<Key>L"); ac++;
	xmstrings[1] = XmStringGenerate ( (XtPointer) "Ctrl-L", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNacceleratorText, xmstrings[1]); ac++;
	TSLinesTB = XmCreateToggleButton ( TSPlotMO, (char *) "TSLinesTB", al, ac );
	ac = 0;
	XmStringFree ( xmstrings [ 0 ] );
	XmStringFree ( xmstrings [ 1 ] );
	xmstrings[0] = XmStringGenerate ( (XtPointer) "Both", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNlabelString, xmstrings[0]); ac++;

	from_value.addr = "B" ;
	from_value.size = strlen( from_value.addr ) + 1;
	to_value.size = sizeof(KeySym);
	to_value.addr = (XPointer) &to_address;
	XtConvertAndStore (TSPlotMO, XmRString, &from_value, XmRKeySym, &to_value);

	if ( to_value.addr ) {
		XtSetArg(al[ac], XmNmnemonic, (*((KeySym*) to_value.addr))); ac++;
	}
	XtSetArg(al[ac], XmNaccelerator, "Ctrl<Key>B"); ac++;
	xmstrings[1] = XmStringGenerate ( (XtPointer) "Ctrl-B", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNacceleratorText, xmstrings[1]); ac++;
	TSBothTB = XmCreateToggleButton ( TSPlotMO, (char *) "TSBothTB", al, ac );
	ac = 0;
	XmStringFree ( xmstrings [ 0 ] );
	XmStringFree ( xmstrings [ 1 ] );
	xmstrings[0] = XmStringGenerate ( (XtPointer) "Batch Scale Stages", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNlabelString, xmstrings[0]); ac++;
	TSBatchScale = XmCreateCascadeButton ( TSCDialogMenu, (char *) "TSBatchScale", al, ac );
	ac = 0;
	XmStringFree ( xmstrings [ 0 ] );
	TSBatchScaleMO = XmCreatePulldownMenu ( TSCDialogMenu, (char *) "TSBatchScaleMO", al, ac );
	xmstrings[0] = XmStringGenerate ( (XtPointer) "Data Only", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNlabelString, xmstrings[0]); ac++;

	from_value.addr = "F1" ;
	from_value.size = strlen( from_value.addr ) + 1;
	to_value.size = sizeof(KeySym);
	to_value.addr = (XPointer) &to_address;
	XtConvertAndStore (TSBatchScaleMO, XmRString, &from_value, XmRKeySym, &to_value);

	if ( to_value.addr ) {
		XtSetArg(al[ac], XmNmnemonic, (*((KeySym*) to_value.addr))); ac++;
	}
	XtSetArg(al[ac], XmNaccelerator, "Ctrl<Key>F1"); ac++;
	xmstrings[1] = XmStringGenerate ( (XtPointer) "Ctrl-F1", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNacceleratorText, xmstrings[1]); ac++;
	TSBatchDataOnlyTB = XmCreateToggleButton ( TSBatchScaleMO, (char *) "TSBatchDataOnlyTB", al, ac );
	ac = 0;
	XmStringFree ( xmstrings [ 0 ] );
	XmStringFree ( xmstrings [ 1 ] );
	xmstrings[0] = XmStringGenerate ( (XtPointer) "Data Only, Show Categories", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNlabelString, xmstrings[0]); ac++;

	from_value.addr = "F2" ;
	from_value.size = strlen( from_value.addr ) + 1;
	to_value.size = sizeof(KeySym);
	to_value.addr = (XPointer) &to_address;
	XtConvertAndStore (TSBatchScaleMO, XmRString, &from_value, XmRKeySym, &to_value);

	if ( to_value.addr ) {
		XtSetArg(al[ac], XmNmnemonic, (*((KeySym*) to_value.addr))); ac++;
	}
	XtSetArg(al[ac], XmNaccelerator, "Ctrl<Key>F2"); ac++;
	xmstrings[1] = XmStringGenerate ( (XtPointer) "Ctrl-F2", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNacceleratorText, xmstrings[1]); ac++;
	TSBatchShowCategTB = XmCreateToggleButton ( TSBatchScaleMO, (char *) "TSBatchShowCategTB", al, ac );
	ac = 0;
	XmStringFree ( xmstrings [ 0 ] );
	XmStringFree ( xmstrings [ 1 ] );
	xmstrings[0] = XmStringGenerate ( (XtPointer) "Data and Categories", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNlabelString, xmstrings[0]); ac++;

	from_value.addr = "F3" ;
	from_value.size = strlen( from_value.addr ) + 1;
	to_value.size = sizeof(KeySym);
	to_value.addr = (XPointer) &to_address;
	XtConvertAndStore (TSBatchScaleMO, XmRString, &from_value, XmRKeySym, &to_value);

	if ( to_value.addr ) {
		XtSetArg(al[ac], XmNmnemonic, (*((KeySym*) to_value.addr))); ac++;
	}
	XtSetArg(al[ac], XmNaccelerator, "Ctrl<Key>F3"); ac++;
	xmstrings[1] = XmStringGenerate ( (XtPointer) "Ctrl-F3", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNacceleratorText, xmstrings[1]); ac++;
	TSBatchCategoriesTB = XmCreateToggleButton ( TSBatchScaleMO, (char *) "TSBatchCategoriesTB", al, ac );
	ac = 0;
	XmStringFree ( xmstrings [ 0 ] );
	XmStringFree ( xmstrings [ 1 ] );
	xmstrings[0] = XmStringGenerate ( (XtPointer) "Edit ", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNlabelString, xmstrings[0]); ac++;
	TSEditCB = XmCreateCascadeButton ( TSMB, (char *) "TSEditCB", al, ac );
	ac = 0;
	XmStringFree ( xmstrings [ 0 ] );
	XtSetArg(al[ac], XmNtearOffModel, XmTEAR_OFF_ENABLED); ac++;
	TSEditMO = XmCreatePulldownMenu ( TSMB, (char *) "TSEditMO", al, ac );
	ac = 0;
	xmstrings[0] = XmStringGenerate ( (XtPointer) "Select Trace", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNlabelString, xmstrings[0]); ac++;

	from_value.addr = "e" ;
	from_value.size = strlen( from_value.addr ) + 1;
	to_value.size = sizeof(KeySym);
	to_value.addr = (XPointer) &to_address;
	XtConvertAndStore (TSEditMO, XmRString, &from_value, XmRKeySym, &to_value);

	if ( to_value.addr ) {
		XtSetArg(al[ac], XmNmnemonic, (*((KeySym*) to_value.addr))); ac++;
	}
	XtSetArg(al[ac], XmNaccelerator, "Ctrl<Key>E"); ac++;
	xmstrings[1] = XmStringGenerate ( (XtPointer) "Ctrl-E", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNacceleratorText, xmstrings[1]); ac++;
	TSTracePB = XmCreatePushButton ( TSEditMO, (char *) "TSTracePB", al, ac );
	ac = 0;
	XmStringFree ( xmstrings [ 0 ] );
	XmStringFree ( xmstrings [ 1 ] );
	separator3 = XmCreateSeparator ( TSEditMO, (char *) "separator3", al, ac );
	xmstrings[0] = XmStringGenerate ( (XtPointer) "Insert", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNlabelString, xmstrings[0]); ac++;

	from_value.addr = "n" ;
	from_value.size = strlen( from_value.addr ) + 1;
	to_value.size = sizeof(KeySym);
	to_value.addr = (XPointer) &to_address;
	XtConvertAndStore (TSEditMO, XmRString, &from_value, XmRKeySym, &to_value);

	if ( to_value.addr ) {
		XtSetArg(al[ac], XmNmnemonic, (*((KeySym*) to_value.addr))); ac++;
	}
	XtSetArg(al[ac], XmNaccelerator, "Ctrl<Key>N"); ac++;
	xmstrings[1] = XmStringGenerate ( (XtPointer) "Ctrl-N", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNacceleratorText, xmstrings[1]); ac++;
	TSAddPB = XmCreatePushButton ( TSEditMO, (char *) "TSAddPB", al, ac );
	ac = 0;
	XmStringFree ( xmstrings [ 0 ] );
	XmStringFree ( xmstrings [ 1 ] );
	xmstrings[0] = XmStringGenerate ( (XtPointer) "Delete", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNlabelString, xmstrings[0]); ac++;
	XtSetArg(al[ac], XmNaccelerator, "Ctrl<Key>Y"); ac++;
	xmstrings[1] = XmStringGenerate ( (XtPointer) "Ctrl-Y", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNacceleratorText, xmstrings[1]); ac++;
	TSDeletePB = XmCreatePushButton ( TSEditMO, (char *) "TSDeletePB", al, ac );
	ac = 0;
	XmStringFree ( xmstrings [ 0 ] );
	XmStringFree ( xmstrings [ 1 ] );
	xmstrings[0] = XmStringGenerate ( (XtPointer) "Move", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNlabelString, xmstrings[0]); ac++;

	from_value.addr = "M" ;
	from_value.size = strlen( from_value.addr ) + 1;
	to_value.size = sizeof(KeySym);
	to_value.addr = (XPointer) &to_address;
	XtConvertAndStore (TSEditMO, XmRString, &from_value, XmRKeySym, &to_value);

	if ( to_value.addr ) {
		XtSetArg(al[ac], XmNmnemonic, (*((KeySym*) to_value.addr))); ac++;
	}
	XtSetArg(al[ac], XmNaccelerator, "Ctrl<Key>M"); ac++;
	xmstrings[1] = XmStringGenerate ( (XtPointer) "Ctrl-M", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNacceleratorText, xmstrings[1]); ac++;
	TSMovePB = XmCreatePushButton ( TSEditMO, (char *) "TSMovePB", al, ac );
	ac = 0;
	XmStringFree ( xmstrings [ 0 ] );
	XmStringFree ( xmstrings [ 1 ] );
	xmstrings[0] = XmStringGenerate ( (XtPointer) "Set Missing", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNlabelString, xmstrings[0]); ac++;

	from_value.addr = "S" ;
	from_value.size = strlen( from_value.addr ) + 1;
	to_value.size = sizeof(KeySym);
	to_value.addr = (XPointer) &to_address;
	XtConvertAndStore (TSEditMO, XmRString, &from_value, XmRKeySym, &to_value);

	if ( to_value.addr ) {
		XtSetArg(al[ac], XmNmnemonic, (*((KeySym*) to_value.addr))); ac++;
	}
	XtSetArg(al[ac], XmNaccelerator, "Ctrl<Key>S"); ac++;
	xmstrings[1] = XmStringGenerate ( (XtPointer) "Ctrl-S", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNacceleratorText, xmstrings[1]); ac++;
	TSSetMissingPB = XmCreatePushButton ( TSEditMO, (char *) "TSSetMissingPB", al, ac );
	ac = 0;
	XmStringFree ( xmstrings [ 0 ] );
	XmStringFree ( xmstrings [ 1 ] );
	separator4 = XmCreateSeparator ( TSEditMO, (char *) "separator4", al, ac );
	xmstrings[0] = XmStringGenerate ( (XtPointer) "Save to Database", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNlabelString, xmstrings[0]); ac++;

	from_value.addr = "D" ;
	from_value.size = strlen( from_value.addr ) + 1;
	to_value.size = sizeof(KeySym);
	to_value.addr = (XPointer) &to_address;
	XtConvertAndStore (TSEditMO, XmRString, &from_value, XmRKeySym, &to_value);

	if ( to_value.addr ) {
		XtSetArg(al[ac], XmNmnemonic, (*((KeySym*) to_value.addr))); ac++;
	}
	XtSetArg(al[ac], XmNaccelerator, "Ctrl<Key>D"); ac++;
	xmstrings[1] = XmStringGenerate ( (XtPointer) "Ctrl-D", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNacceleratorText, xmstrings[1]); ac++;
	TSEditDonePB = XmCreatePushButton ( TSEditMO, (char *) "TSEditDonePB", al, ac );
	ac = 0;
	XmStringFree ( xmstrings [ 0 ] );
	XmStringFree ( xmstrings [ 1 ] );
	xmstrings[0] = XmStringGenerate ( (XtPointer) "Cancel Changes", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNlabelString, xmstrings[0]); ac++;

	from_value.addr = "C" ;
	from_value.size = strlen( from_value.addr ) + 1;
	to_value.size = sizeof(KeySym);
	to_value.addr = (XPointer) &to_address;
	XtConvertAndStore (TSEditMO, XmRString, &from_value, XmRKeySym, &to_value);

	if ( to_value.addr ) {
		XtSetArg(al[ac], XmNmnemonic, (*((KeySym*) to_value.addr))); ac++;
	}
	XtSetArg(al[ac], XmNaccelerator, "Ctrl<Key>C"); ac++;
	xmstrings[1] = XmStringGenerate ( (XtPointer) "Ctrl-C", XmFONTLIST_DEFAULT_TAG, XmCHARSET_TEXT, NULL );
	XtSetArg(al[ac], XmNacceleratorText, xmstrings[1]); ac++;
	TSEditCancelPB = XmCreatePushButton ( TSEditMO, (char *) "TSEditCancelPB", al, ac );
	ac = 0;
	XmStringFree ( xmstrings [ 0 ] );
	XmStringFree ( xmstrings [ 1 ] );
	XtSetArg(al[ac], XmNmappedWhenManaged, TRUE); ac++;
	XtSetArg(al[ac], XmNsensitive, TRUE); ac++;
	TSDrawArea = XmCreateDrawingArea ( TSFO, (char *) "TSDrawArea", al, ac );
	ac = 0;


	XtSetArg(al[ac], XmNtopAttachment, XmATTACH_FORM); ac++;
	XtSetArg(al[ac], XmNtopOffset, 5); ac++;
	XtSetArg(al[ac], XmNbottomAttachment, XmATTACH_OPPOSITE_FORM); ac++;
	XtSetArg(al[ac], XmNbottomOffset, -35); ac++;
	XtSetArg(al[ac], XmNleftAttachment, XmATTACH_FORM); ac++;
	XtSetArg(al[ac], XmNleftOffset, 0); ac++;
	XtSetArg(al[ac], XmNrightAttachment, XmATTACH_OPPOSITE_FORM); ac++;
	XtSetArg(al[ac], XmNrightOffset, -995); ac++;
	XtSetValues ( TSMB, al, ac );
	ac = 0;

	XtSetArg(al[ac], XmNtopAttachment, XmATTACH_FORM); ac++;
	XtSetArg(al[ac], XmNtopOffset, 40); ac++;
	XtSetArg(al[ac], XmNbottomAttachment, XmATTACH_FORM); ac++;
	XtSetArg(al[ac], XmNbottomOffset, 5); ac++;
	XtSetArg(al[ac], XmNleftAttachment, XmATTACH_FORM); ac++;
	XtSetArg(al[ac], XmNleftOffset, 5); ac++;
	XtSetArg(al[ac], XmNrightAttachment, XmATTACH_FORM); ac++;
	XtSetArg(al[ac], XmNrightOffset, 5); ac++;
	XtSetValues ( TSDrawArea, al, ac );
	ac = 0;
	if ((children[ac] = PInversePB) != (Widget) 0) { ac++; }
	if ((children[ac] = PnonInversePB) != (Widget) 0) { ac++; }
	if (ac > 0) { XtManageChildren(children, ac); }
	ac = 0;
	XtSetArg(al[ac], XmNsubMenuId, TSPrintMO); ac++;
	XtSetValues(TSPrintCB, al, ac );
	ac = 0;
	if ((children[ac] = TSCDialogPB) != (Widget) 0) { ac++; }
	if ((children[ac] = TSSavePB) != (Widget) 0) { ac++; }
	if ((children[ac] = TSPrintCB) != (Widget) 0) { ac++; }
	if ((children[ac] = TSClosePB) != (Widget) 0) { ac++; }
	if (ac > 0) { XtManageChildren(children, ac); }
	ac = 0;
	XtSetArg(al[ac], XmNsubMenuId, TSFileMenu); ac++;
	XtSetValues(TS_FileCB, al, ac );
	ac = 0;
	if ((children[ac] = TSSetTB) != (Widget) 0) { ac++; }
	if ((children[ac] = TSResetTB) != (Widget) 0) { ac++; }
	if (ac > 0) { XtManageChildren(children, ac); }
	ac = 0;
	XtSetArg(al[ac], XmNsubMenuId, TSZoomMO); ac++;
	XtSetValues(TSZoomCB, al, ac );
	ac = 0;
	if ((children[ac] = TSOffTB) != (Widget) 0) { ac++; }
	if ((children[ac] = TSInterpolateTB) != (Widget) 0) { ac++; }
	if ((children[ac] = TSAssignTB) != (Widget) 0) { ac++; }
	if (ac > 0) { XtManageChildren(children, ac); }
	ac = 0;
	XtSetArg(al[ac], XmNsubMenuId, TSShowPCAsPPMO); ac++;
	XtSetValues(TSPcAsPPCB, al, ac );
	ac = 0;
	if ((children[ac] = TSScaleDataOnlyTB) != (Widget) 0) { ac++; }
	if ((children[ac] = TSScaleDataShowCategTB) != (Widget) 0) { ac++; }
	if ((children[ac] = TSScaleDataCategoriesTB) != (Widget) 0) { ac++; }
	if (ac > 0) { XtManageChildren(children, ac); }
	ac = 0;
	XtSetArg(al[ac], XmNsubMenuId, TSscaleMO); ac++;
	XtSetValues(TSscalePB, al, ac );
	ac = 0;
	if ((children[ac] = TSZoomCB) != (Widget) 0) { ac++; }
	if ((children[ac] = TSPcAsPPCB) != (Widget) 0) { ac++; }
	if ((children[ac] = TSShowFcstTB) != (Widget) 0) { ac++; }
	if ((children[ac] = TSscalePB) != (Widget) 0) { ac++; }
	if (ac > 0) { XtManageChildren(children, ac); }
	ac = 0;
	XtSetArg(al[ac], XmNsubMenuId, TSGraphMenu); ac++;
	XtSetValues(TSGraph, al, ac );
	ac = 0;
	if ((children[ac] = TSPointsTB) != (Widget) 0) { ac++; }
	if ((children[ac] = TSLinesTB) != (Widget) 0) { ac++; }
	if ((children[ac] = TSBothTB) != (Widget) 0) { ac++; }
	if (ac > 0) { XtManageChildren(children, ac); }
	ac = 0;
	XtSetArg(al[ac], XmNsubMenuId, TSPlotMO); ac++;
	XtSetValues(TSPointLine, al, ac );
	ac = 0;
	if ((children[ac] = TSBatchDataOnlyTB) != (Widget) 0) { ac++; }
	if ((children[ac] = TSBatchShowCategTB) != (Widget) 0) { ac++; }
	if ((children[ac] = TSBatchCategoriesTB) != (Widget) 0) { ac++; }
	if (ac > 0) { XtManageChildren(children, ac); }
	ac = 0;
	XtSetArg(al[ac], XmNsubMenuId, TSBatchScaleMO); ac++;
	XtSetValues(TSBatchScale, al, ac );
	ac = 0;
	if ((children[ac] = TSGridLinesTB) != (Widget) 0) { ac++; }
	if ((children[ac] = TSPointLine) != (Widget) 0) { ac++; }
	if ((children[ac] = TSBatchScale) != (Widget) 0) { ac++; }
	if (ac > 0) { XtManageChildren(children, ac); }
	ac = 0;
	XtSetArg(al[ac], XmNsubMenuId, TSCDialogMenu); ac++;
	XtSetValues(TSCDialog, al, ac );
	ac = 0;
	if ((children[ac] = TSTracePB) != (Widget) 0) { ac++; }
	if ((children[ac] = separator3) != (Widget) 0) { ac++; }
	if ((children[ac] = TSAddPB) != (Widget) 0) { ac++; }
	if ((children[ac] = TSDeletePB) != (Widget) 0) { ac++; }
	if ((children[ac] = TSMovePB) != (Widget) 0) { ac++; }
	if ((children[ac] = TSSetMissingPB) != (Widget) 0) { ac++; }
	if ((children[ac] = separator4) != (Widget) 0) { ac++; }
	if ((children[ac] = TSEditDonePB) != (Widget) 0) { ac++; }
	if ((children[ac] = TSEditCancelPB) != (Widget) 0) { ac++; }
	if (ac > 0) { XtManageChildren(children, ac); }
	ac = 0;
	XtSetArg(al[ac], XmNsubMenuId, TSEditMO); ac++;
	XtSetValues(TSEditCB, al, ac );
	ac = 0;
	if ((children[ac] = TS_FileCB) != (Widget) 0) { ac++; }
	if ((children[ac] = TSPgDn) != (Widget) 0) { ac++; }
	if ((children[ac] = TSPgUp) != (Widget) 0) { ac++; }
	if ((children[ac] = TSGraph) != (Widget) 0) { ac++; }
	if ((children[ac] = TSCDialog) != (Widget) 0) { ac++; }
	if ((children[ac] = TSEditCB) != (Widget) 0) { ac++; }
	if (ac > 0) { XtManageChildren(children, ac); }
	ac = 0;
	if ((children[ac] = TSMB) != (Widget) 0) { ac++; }
	if ((children[ac] = TSDrawArea) != (Widget) 0) { ac++; }
	if (ac > 0) { XtManageChildren(children, ac); }
	ac = 0;

/*  ==============  Statements containing RCS keywords:  */
{static char rcs_id1[] = "$Source$";
 static char rcs_id2[] = "$Id$";}
/*  ===================================================  */

}

