#ifndef METANAME_H
#define METANAME_H

#include "degrib_inc/type.h"
#include "degrib_inc/meta.h"

void ParseElemName (unsigned short int center, unsigned short int subcenter,
                    int prodType, int templat, int cat, int subcat,
                    long int lenTime, char **name, char **comment,
                    char **unit, int *convert);

int ComputeUnit (int convert, sChar f_unit, double *unitM, double *unitB,
                 char *name);
/*
int ComputeUnit (int prodType, int templat, int cat, int subcat, sChar f_unit,
                 double *unitM, double *unitB, char *name);
*/
int Table45Index (int i);

int IsData_NDFD (unsigned short int center, unsigned short int subcenter);

void ParseLevelName (unsigned short int center, unsigned short int subcenter,
                     uChar surfType, double value, sChar f_sndValue,
                     double sndValue, char **shortLevelName,
                     char **longLevelName);

#endif
