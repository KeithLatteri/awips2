#!/bin/csh
#
# A script wrapper that is meant to get inventories of satellite data
# from the A-II database.  The data is output to stdout as ASCII.
# This version can adapt to use a python stub that calls the
# data access framework.
#
# Usage:
#
#  a2invsat.csh {p} sector channel {satid}
#
#    p - A literal p. (optional)
#    sector - sector id
#    channel - channel id
#    satid - (optional) satellite id
#
#  Returns a list of times with data for the specified sector/channel.
#
#   The ids can be either D-2D integer ids, or AWIPS-II ascii ids, in which
#   case they need to be quoted on the command line.
#
#   Integer ids can be looked up in a2satInfo.txt, channel id corresponds to
#   the physicalElement, and satid corresponds to the creatingEntity.
#
#  The literal p option means preserve the final version of the python
#  submitted to the UEngine instead of cleaning it up.  The path to the
#  finalized python is /tmp/a2invsatNNNNN.py where NNNNN is a unix process id.
# 
#
set rmpy = yes
if ( "$1" == "p" ) then
    set rmpy = no
    shift
endif
#
# Identify directory this script is in, will be one of the directories we
# search for other files in.
#
set mydir = `dirname $0`
set d1 = `echo $mydir | cut -c1`
if ( "$mydir" == '.' ) then
     set mydir = $PWD
else if ( "$d1" != "/" ) then
     set mydir = $PWD/$mydir
endif
set mydir = `(cd $mydir ; pwd)`
if ( ! $?FXA_HOME ) set FXA_HOME = xxxx
#
# Locate python stub that we will modify to create the final python logic.
#
if ( -e ./a2invsatStub.py ) then
    set stubpy = ./a2invsatStub.py
else if ( -e $mydir/a2invsatStub.py ) then
    set stubpy = $mydir/a2invsatStub.py
else if ( -e $FXA_HOME/src/dm/sat/a2invsatStub.py ) then
    set stubpy = $FXA_HOME/src/dm/sat/a2invsatStub.py
else if ( -e $FXA_HOME/bin/a2invsatStub.py ) then
    set stubpy = $FXA_HOME/bin/a2invsatStub.py
else
    bash -c "echo could not find a2invsatStub.py 1>&2"
    exit
endif
#
# Determine if we are using the data access framework or the uEngine.
#
grep DataAccessLayer $stubpy >& /dev/null
if ( $status == 0 ) then
    set method = "daf"
else
    #
    # Set up the environment we need to run the UEngine.
    #
    set method = "uengine"
    if ( -e ./UEngine.cshsrc ) then
        set ueenv = ./UEngine.cshsrc
    else if ( -e $mydir/UEngine.cshsrc ) then
        set ueenv = $mydir/UEngine.cshsrc
    else if ( -e $FXA_HOME/src/dm/point/UEngine.cshsrc ) then
        set ueenv = $FXA_HOME/src/dm/point/UEngine.cshsrc
    else if ( -e $FXA_HOME/bin/UEngine.cshsrc ) then
        set ueenv = $FXA_HOME/bin/UEngine.cshsrc
    else
        bash -c "echo could not find UEngine.cshsrc 1>&2"
        exit
    endif
    source $ueenv
endif
#
# Locate file containing mapping between D-2D interger ids and AWIPS-II ascii
# ids for sectors, channels, and satellites.
#
if ( -e ./a2satInfo.txt ) then
    set satInf = ./a2satInfo.txt
else if ( -e $mydir/a2satInfo.txt ) then
    set satInf = $mydir/a2satInfo.txt
else if ( -e $FXA_HOME/src/dm/sat/a2satInfo.txt ) then
    set satInf = $FXA_HOME/src/dm/sat/a2satInfo.txt
else if ( -e $FXA_HOME/data/a2satInfo.txt ) then
    set satInf = $FXA_HOME/data/a2satInfo.txt
else
    bash -c "echo could not find a2satInfo.txt 1>&2"
    exit
endif
#
#
set sss = `grep "^ *$1|.*sectorID" $satInf | cut '-d|' -f3`
if ( "$sss" == "" ) set sss = "$1"
set ccc = `grep "^ *$2|.*physicalElement" $satInf | cut '-d|' -f3`
if ( "$ccc" == "" ) set ccc = "$2"
shift
shift
#
#  Modify the text of special tags in stub to create finalized script.
#
set specpy = /tmp/a2invsat${$}.py
rm -rf $specpy >& /dev/null
touch $specpy
chmod 775 $specpy
if ( "$1" == "" ) then
  cat $stubpy | sed "s/SSSSS/$sss/g" | sed "s/CCCCC/$ccc/g" | \
      sed 's/^.*EEEEE.*$//g' >> $specpy
else
  set eee = `grep "^ *$1|.*creatingEntity" $satInf | cut '-d|' -f3`
  if ( "$eee" == "" ) set eee = "$1"
  cat $stubpy | sed "s/SSSSS/$sss/g" | sed "s/CCCCC/$ccc/g" | \
     sed "s/EEEEE/$eee/g" >> $specpy
endif
#
#  Submit the temporary python script stripping xml stuff, then remove it
#
if ( "$method" == "daf" ) then
     /awips2/python/bin/python $specpy
else
    cd $UE_BIN_PATH
    ( uengine -r python < $specpy ) |& grep attributes | cut '-d"' -f4
endif
if ( "$rmpy" == "yes" ) rm -rf $specpy >& /dev/null
#
