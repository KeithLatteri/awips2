//------------------------------------------------------------------------------
// TSIdent::getIntervalMult - get the interval multiplier
//------------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
//------------------------------------------------------------------------------
// Notes:	(1)	This routine returns the interval multiplier.
//------------------------------------------------------------------------------
// History:
// 
// 16 Sep 1997	Steven A. Malers, RTi	Initial version.
//------------------------------------------------------------------------------
// Variables:	I/O	Description		
//
//
//------------------------------------------------------------------------------

#include "resj/TSIdent.h"

int TSIdent :: getIntervalMult ( void )
{
	return( _interval_mult );

/*  ==============  Statements containing RCS keywords:  */
{static char rcs_id1[] = "$Source: /fs/hseb/ob72/rfc/ofs/src/resj_ts/RCS/TSIdent_getIntervalMult.cxx,v $";
 static char rcs_id2[] = "$Id: TSIdent_getIntervalMult.cxx,v 1.2 2000/05/19 13:06:34 dws Exp $";}
/*  ===================================================  */

}
