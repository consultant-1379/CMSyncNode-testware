#!/bin/bash

./netsim/netsim_cfg > /dev/null 2>&1 

NETSIMDIR=${NETSIMDIR:-$HOME/netsimdir}
INSTALLATION=/netsim/inst

if [ $# -lt 2 ] ; then
    echo "Usage: $0 standard start|standard stop|show | /netsim/inst/netsim_pipe"
    echo "Usage: $0 peak     start|peak     stop|show | /netsim/inst/netsim_pipe"
    echo "Usage: $0 storm    start|storm    stop|show | /netsim/inst/netsim_pipe"	
    exit 1
fi

LOAD_TYPE=$1
TASK=$2

SIM_LIST=""

STD_NOTIF_RATE_PER_SEC=0.00172
# STANDARD ASSUMING 4000 nodes and 9 AVC Processes per Node  = 61.92 N/S  
PEAK_NOTIF_RATE_PER_SEC=0.005
# PEAK ASSUMING 25 SIMS and 9 AVC Processes per Sim  =  190 N/S  
STORM_NOTIF_RATE_PER_SEC=0.023
# STORM ASSUMING 25 SIMS and 9 AVC Processes per Sim  = 801 N/S

##############STD LOAD BEGIN #########################################

STD_SLEEP_DURATION=3
STD_NUM_OF_NODES=640
STD_NUM_EVENTS=6
STD_DURATION=50

ID1=1
FDN1="ManagedElement=1,TransportNetwork=1,Sctp=1"
ATTRIBUTE1="userLabel"
ATTRIBUTE_VAL11="D1"
ATTRIBUTE_VAL12="D2"

ID2=2
FDN2="ManagedElement=1,Equipment=1,AntennaUnitGroup=1,AntennaNearUnit=1,RetSubUnit=1"

ID3=3
FDN3="ManagedElement=1,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1,AntennaSubunit=1,AuPort=1"

ID4=4
FDN4="ManagedElement=1,ENodeBFunction=1,AdmissionControl=1"
ATTRIBUTE4="nrOfPaConnReservationsPerCell"
ATTR_VAL41=1
ATTR_VAL42=2

ID5=5
#FDN5="ManagedElement=1,ENodeBFunction=1,GeraNetwork=1,GeranFreqGroup=11"
#ATTRIBUTE5="frequencyGroupId"
ATTR_VAL51=10
ATTR_VAL52=21
FDN5="ManagedElement=1,ENodeBFunction=1,AnrFunction=1,AnrFunctionUtran=1"
ATTRIBUTE5="anrUtranMeasReportMin"


ID6=6
FDN6="ManagedElement=1,ENodeBFunction=1,LoadBalancingFunction=1"
ATTRIBUTE6="lbUtranOffloadBackoffTime"
ATTR_VAL61=10
ATTR_VAL62=21

ID7=7
FDN7="ManagedElement=1,Equipment=1,ExternalNode=1"
ATTRIBUTE7="informationOnly"
ATTR_VAL71=true
ATTR_VAL72=false


ID8=8
FDN8="ManagedElement=1,SectorEquipmentFunction=1"
ATTRIBUTE8="mixedModeRadio"
ATTR_VAL81=true
ATTR_VAL82=false

ID9=9
FDN9="ManagedElement=1,ENodeBFunction=1"
ATTRIBUTE9="ulSchedulerDynamicBWAllocationEnabled"
ATTR_VAL91=true
ATTR_VAL92=false
################################STD LOAD END #########################################

################################ STORM LOAD BEGIN################################
STORM_NUM_OF_NODES=640
STORM_NUM_EVENTS=6
STORM_DURATION=240
STORM_IDLE_TIME=900

ID10=10
FDN10="ManagedElement=1,SystemFunctions=1,WebServer=1"
ATTRIBUTE10="userLabel"
ATTRIBUTE_VAL101="WebServer1"
ATTRIBUTE_VAL102="WebServer2"

ID11=11
FDN11="ManagedElement=1,ENodeBFunction=1,Cdma2000Network=1,Cdma2000FreqBand=1,Cdma2000Freq=1"

ID12=12
FDN12="ManagedElement=1,ENodeBFunction=1,QciTable=1,LogicalChannelGroup=1"


ID13=13
FDN13="ManagedElement=1,IpOam=1,Ip=1"
ATTRIBUTE13="udpChecksumState"
ATTR_VAL131=2
ATTR_VAL132=3


ID14=14
FDN14="ManagedElement=1,TransportNetwork=1,Sctp=1"
ATTRIBUTE14="tSack"
ATTR_VAL141=55
ATTR_VAL142=60

ID15=15
FDN15="ManagedElement=1,ENodeBFunction=1,LoadBalancingFunction=1"
ATTRIBUTE15="lbUeEvaluationTimer"
ATTR_VAL151=92
ATTR_VAL152=101


ID16=16
FDN16="ManagedElement=1,EquipmentSupportFunction=1"
ATTRIBUTE16="supportSystemControl"
ATTR_VAL161=true
ATTR_VAL162=false


ID17=17
FDN17="ManagedElement=1,ManagedElementData=1"
ATTRIBUTE17="ntpServiceActivePrimary"
ATTR_VAL171=true
ATTR_VAL172=false

ID18=18
FDN18="ManagedElement=1,NodeManagementFunction=1"
ATTRIBUTE18="alarmSuppressed"
ATTR_VAL181=true
ATTR_VAL182=false
################################ STORM LOAD END################################

################################ PEAK LOAD BEGIN################################
PEAK_NUM_OF_NODES=640
PEAK_NUM_EVENTS=6
PEAK_DURATION=60
PEAK_IDLE_TIME=300

ID21=21
FDN21="ManagedElement=1,TransportNetwork=1,Synchronization=1"
ATTRIBUTE21="userLabel"
ATTR_VAL211="C"
ATTR_VAL212="D"

ID22=22
FDN22="ManagedElement=1,ENodeBFunction=1,AnrFunction=1,AnrFunctionUtran=1"
ATTRIBUTE22="anrUtranMeasReportMin"
ATTR_VAL221=5
ATTR_VAL222=6

ID23=23
FDN23="ManagedElement=1,IpSystem=1,IpAccessHostEt=1,IpSyncRef=1"

ID24=24
FDN24="ManagedElement=1,ENodeBFunction=1,EUtraNetwork=1,EUtranFrequency=1"
ATTRIBUTE24="freqBand"
ATTR_VAL241=2
ATTR_VAL242=3

ID25=25
FDN25="ManagedElement=1,ENodeBFunction=1,Paging=1"
ATTRIBUTE25="noOfDefPagCyclPrim"
ATTR_VAL251=7
ATTR_VAL252=9

ID26=26
FDN26="ManagedElement=1,ENodeBFunction=1,PmEventService=1"
ATTRIBUTE26="cellTraceHighPrioReserve"
ATTR_VAL261=2
ATTR_VAL262=3

ID27=27
FDN27="ManagedElement=1,ENodeBFunction=1,LoadBalancingFunction=1"
ATTRIBUTE27="mbmsDisabledLoadBalancing"
ATTR_VAL271=true 
ATTR_VAL272=false

ID28=28
FDN28="ManagedElement=1,ENodeBFunction=1"
ATTRIBUTE28="x2IpAddrViaS1Active"
ATTR_VAL281=true
ATTR_VAL282=false

ID29=29
FDN29="ManagedElement=1,ENodeBFunction=1,AutoCellCapEstFunction=1"
ATTRIBUTE29="useEstimatedCellCap"
ATTR_VAL291=true
ATTR_VAL292=false

################################ PEAK LOAD END################################

#######################COMMON METHODS BEGIN ################################
getSims() {
	for filename in `ls $NETSIMDIR | grep LTE | grep -v zip`; do
		SIM_LIST+=($filename)
	done
}

show() {
    for SIM in ${SIM_LIST[*]} 
	do
	
        echo .open $SIM

        echo .select network
        echo "showbursts;"
        
    done
}


#######################COMMON METHODS END################################

start_standard() {
    for SIM in ${SIM_LIST[*]} 
	do
	
        echo .open $SIM

        echo .select network
	printf 'avcburst:id=%d,num_events=%d,freq=%s,loop=true,avcdata=\"[{\\\"%s\\\",[{\\\"%s\\\",\\\"%s\\\"}]},{\\\"%s\\\",[{\\\"%s\\\",\\\"%s\\\"}]}]\";\n' \
                $ID1 $STD_NUM_EVENTS $STD_FREQ $FDN1 $ATTRIBUTE1 $ATTRIBUTE_VAL11 ${FDN1} ${ATTRIBUTE1} ${ATTRIBUTE_VAL12}
	printf 'avcburst:id=%d,num_events=%d,freq=%s,loop=true,avcdata=\"[{\\\"%s\\\",[{\\\"%s\\\",\\\"%s\\\"}]},{\\\"%s\\\",[{\\\"%s\\\",\\\"%s\\\"}]}]\";\n' \
                $ID2 $STD_NUM_EVENTS $STD_FREQ $FDN2 $ATTRIBUTE1 $ATTRIBUTE_VAL11 ${FDN2} ${ATTRIBUTE1} ${ATTRIBUTE_VAL12}
	printf 'avcburst:id=%d,num_events=%d,freq=%s,loop=true,avcdata=\"[{\\\"%s\\\",[{\\\"%s\\\",\\\"%s\\\"}]},{\\\"%s\\\",[{\\\"%s\\\",\\\"%s\\\"}]}]\";\n' \
               $ID3 $STD_NUM_EVENTS $STD_FREQ $FDN3 $ATTRIBUTE1 $ATTRIBUTE_VAL11 ${FDN3} ${ATTRIBUTE1} ${ATTRIBUTE_VAL12}
	printf 'avcburst:id=%d,num_events=%d,freq=%s,loop=true,avcdata=\"[{\\\"%s\\\",[{\\\"%s\\\",\\\"%d\\\"}]},{\\\"%s\\\",[{\\\"%s\\\",\\\"%d\\\"}]}]\";\n' \
                $ID4 $STD_NUM_EVENTS $STD_FREQ $FDN4 $ATTRIBUTE4 $ATTR_VAL41 ${FDN4} ${ATTRIBUTE4} ${ATT_VAL42}

    printf 'avcburst:id=%d,num_events=%d,freq=%s,loop=true,avcdata=\"[{\\\"%s\\\",[{\\\"%s\\\",\\\"%d\\\"}]},{\\\"%s\\\",[{\\\"%s\\\",\\\"%d\\\"}]}]\";\n' \
               $ID5 $STD_NUM_EVENTS $STD_FREQ $FDN5 $ATTRIBUTE5 $ATTR_VAL51 ${FDN5} ${ATTRIBUTE5} ${ATTR_VAL52}

    printf 'avcburst:id=%d,num_events=%d,freq=%s,loop=true,avcdata=\"[{\\\"%s\\\",[{\\\"%s\\\",\\\"%d\\\"}]},{\\\"%s\\\",[{\\\"%s\\\",\\\"%d\\\"}]}]\";\n' \
               $ID6 $STD_NUM_EVENTS $STD_FREQ $FDN6 $ATTRIBUTE6 $ATTR_VAL61 ${FDN6} ${ATTRIBUTE6} ${ATTR_VAL62}

    printf 'avcburst:id=%d,num_events=%d,freq=%s,loop=true,avcdata=\"[{\\\"%s\\\",[{\\\"%s\\\",\\\"%s\\\"}]},{\\\"%s\\\",[{\\\"%s\\\",\\\"%s\\\"}]}]\";\n' \
               $ID7 $STD_NUM_EVENTS $STD_FREQ $FDN7 $ATTRIBUTE7 $ATTR_VAL71 ${FDN7} ${ATTRIBUTE7} ${ATTR_VAL72}

    printf 'avcburst:id=%d,num_events=%d,freq=%s,loop=true,avcdata=\"[{\\\"%s\\\",[{\\\"%s\\\",\\\"%s\\\"}]},{\\\"%s\\\",[{\\\"%s\\\",\\\"%s\\\"}]}]\";\n' \
               $ID8 $STD_NUM_EVENTS $STD_FREQ $FDN8 $ATTRIBUTE8 $ATTR_VAL81 ${FDN8} ${ATTRIBUTE8} ${ATTR_VAL82}

   printf 'avcburst:id=%d,num_events=%d,freq=%s,loop=true,avcdata=\"[{\\\"%s\\\",[{\\\"%s\\\",\\\"%s\\\"}]},{\\\"%s\\\",[{\\\"%s\\\",\\\"%s\\\"}]}]\";\n' \
               $ID9 $STD_NUM_EVENTS $STD_FREQ $FDN9 $ATTRIBUTE9 $ATTR_VAL91 ${FDN9} ${ATTRIBUTE9} ${ATTR_VAL92}

	sleep ${STD_SLEEP_DURATION}
        
    done
}

stop_standard() {
    for SIM in ${SIM_LIST[*]} 
	do
	
        echo .open $SIM

        echo .select network
        echo "stopburst:id=$ID1;"
        echo "stopburst:id=$ID2;"
		echo "stopburst:id=$ID3;" 
		echo "stopburst:id=$ID4;"
		echo "stopburst:id=$ID5;"
		echo "stopburst:id=$ID6;"
		echo "stopburst:id=$ID7;"
		echo "stopburst:id=$ID8;"
		echo "stopburst:id=$ID9;"
    done
}

start_storm() {
    for SIM in ${SIM_LIST[*]} 
	do
	
        echo .open $SIM

        echo .select network

	printf 'avcburst:id=%d,duration=%d,freq=%s,idle_time=%d,loop=true,avcdata=\"[{\\\"%s\\\",[{\\\"%s\\\",\\\"%s\\\"}]},{\\\"%s\\\",[{\\\"%s\\\",\\\"%s\\\"}]}]\";\n' \
                $ID10 $STORM_DURATION $STORM_FREQ $STORM_IDLE_TIME $FDN10 $ATTRIBUTE10 $ATTRIBUTE_VAL101 ${FDN10} ${ATTRIBUTE10} ${ATTRIBUTE_VAL102}
	 printf 'avcburst:id=%d,duration=%d,freq=%s,idle_time=%d,loop=true,avcdata=\"[{\\\"%s\\\",[{\\\"%s\\\",\\\"%s\\\"}]},{\\\"%s\\\",[{\\\"%s\\\",\\\"%s\\\"}]}]\";\n' \
                $ID11 $STORM_DURATION $STORM_FREQ $STORM_IDLE_TIME $FDN11 $ATTRIBUTE10 $ATTRIBUTE_VAL101 ${FDN11} ${ATTRIBUTE10} ${ATTRIBUTE_VAL102}
	 printf 'avcburst:id=%d,duration=%d,freq=%s,idle_time=%d,loop=true,avcdata=\"[{\\\"%s\\\",[{\\\"%s\\\",\\\"%s\\\"}]},{\\\"%s\\\",[{\\\"%s\\\",\\\"%s\\\"}]}]\";\n' \
                $ID12 $STORM_DURATION $STORM_FREQ $STORM_IDLE_TIME $FDN12 $ATTRIBUTE10 $ATTRIBUTE_VAL101 ${FDN12} ${ATTRIBUTE10} ${ATTRIBUTE_VAL102}
	printf 'avcburst:id=%d,duration=%d,freq=%s,idle_time=%d,loop=true,avcdata=\"[{\\\"%s\\\",[{\\\"%s\\\",\\\"%d\\\"}]},{\\\"%s\\\",[{\\\"%s\\\",\\\"%d\\\"}]}]\";\n' \
                $ID13 $STORM_DURATION $STORM_FREQ $STORM_IDLE_TIME $FDN13 $ATTRIBUTE13 $ATTR_VAL131 ${FDN13} ${ATTRIBUTE13} ${ATTR_VAL132}

    printf 'avcburst:id=%d,duration=%d,freq=%s,idle_time=%d,loop=true,avcdata=\"[{\\\"%s\\\",[{\\\"%s\\\",\\\"%d\\\"}]},{\\\"%s\\\",[{\\\"%s\\\",\\\"%d\\\"}]}]\";\n' \
               $ID14 $STORM_DURATION $STORM_FREQ $STORM_IDLE_TIME $FDN14 $ATTRIBUTE14 $ATTR_VAL141 ${FDN5} ${ATTRIBUTE14} ${ATTR_VAL142}

    printf 'avcburst:id=%d,duration=%d,freq=%s,idle_time=%d,loop=true,avcdata=\"[{\\\"%s\\\",[{\\\"%s\\\",\\\"%d\\\"}]},{\\\"%s\\\",[{\\\"%s\\\",\\\"%d\\\"}]}]\";\n' \
               $ID15 $STORM_DURATION $STORM_FREQ $STORM_IDLE_TIME $FDN15 $ATTRIBUTE15 $ATTR_VAL151 ${FDN15} ${ATTRIBUTE15} ${ATTR_VAL152}

    printf 'avcburst:id=%d,duration=%d,freq=%s,idle_time=%d,loop=true,avcdata=\"[{\\\"%s\\\",[{\\\"%s\\\",\\\"%s\\\"}]},{\\\"%s\\\",[{\\\"%s\\\",\\\"%s\\\"}]}]\";\n' \
               $ID16 $STORM_DURATION $STORM_FREQ $STORM_IDLE_TIME $FDN16 $ATTRIBUTE16 $ATTR_VAL161 ${FDN16} ${ATTRIBUTE16} ${ATTR_VAL162}

    printf 'avcburst:id=%d,duration=%d,freq=%s,idle_time=%d,loop=true,avcdata=\"[{\\\"%s\\\",[{\\\"%s\\\",\\\"%s\\\"}]},{\\\"%s\\\",[{\\\"%s\\\",\\\"%s\\\"}]}]\";\n' \
               $ID17 $STORM_DURATION $STORM_FREQ $STORM_IDLE_TIME $FDN17 $ATTRIBUTE17 $ATTR_VAL171 ${FDN17} ${ATTRIBUTE17} ${ATTR_VAL172}

   printf 'avcburst:id=%d,duration=%d,freq=%s,idle_time=%d,loop=true,avcdata=\"[{\\\"%s\\\",[{\\\"%s\\\",\\\"%s\\\"}]},{\\\"%s\\\",[{\\\"%s\\\",\\\"%s\\\"}]}]\";\n' \
               $ID18 $STORM_DURATION $STORM_FREQ $STORM_IDLE_TIME $FDN18 $ATTRIBUTE18 $ATTR_VAL181 ${FDN18} ${ATTRIBUTE18} ${ATTR_VAL182}			
	sleep 1 
        
    done
}

stop_storm() {
    for SIM in ${SIM_LIST[*]} 
	do
	
        echo .open $SIM

        echo .select network
        echo "stopburst:id=$ID10;"
        echo "stopburst:id=$ID11;"
		echo "stopburst:id=$ID12;" 
		echo "stopburst:id=$ID13;"
		echo "stopburst:id=$ID14;"
		echo "stopburst:id=$ID15;"
		echo "stopburst:id=$ID16;"
		echo "stopburst:id=$ID17;"
		echo "stopburst:id=$ID18;"
    done
}


start_peak() {
    for SIM in ${SIM_LIST[*]} 
	do
	
        echo .open $SIM

        echo .select network
	 printf 'avcburst:id=%d,duration=%d,freq=%s,idle_time=%d,loop=true,avcdata=\"[{\\\"%s\\\",[{\\\"%s\\\",\\\"%s\\\"}]},{\\\"%s\\\",[{\\\"%s\\\",\\\"%s\\\"}]}]\";\n' \
                $ID21 $PEAK_DURATION $PEAK_FREQ $PEAK_IDLE_TIME $FDN21 $ATTRIBUTE21 $ATTR_VAL211 ${FDN21} ${ATTRIBUTE21} ${ATTR_VAL212}
	 printf 'avcburst:id=%d,duration=%d,freq=%s,idle_time=%d,loop=true,avcdata=\"[{\\\"%s\\\",[{\\\"%s\\\",\\\"%s\\\"}]},{\\\"%s\\\",[{\\\"%s\\\",\\\"%s\\\"}]}]\";\n' \
		$ID22 $PEAK_DURATION $PEAK_FREQ $PEAK_IDLE_TIME $FDN22 $ATTRIBUTE22 $ATTR_VAL221 ${FDN22} ${ATTRIBUTE22} ${ATTR_VAL222}
     printf 'avcburst:id=%d,duration=%d,freq=%s,idle_time=%d,loop=true,avcdata=\"[{\\\"%s\\\",[{\\\"%s\\\",\\\"%s\\\"}]},{\\\"%s\\\",[{\\\"%s\\\",\\\"%s\\\"}]}]\";\n' \
                $ID23 $PEAK_DURATION $PEAK_FREQ $PEAK_IDLE_TIME $FDN23 $ATTRIBUTE21 $ATTR_VAL211 ${FDN23} ${ATTRIBUTE21} ${ATTR_VAL212}
	printf 'avcburst:id=%d,duration=%d,freq=%s,idle_time=%d,loop=true,avcdata=\"[{\\\"%s\\\",[{\\\"%s\\\",\\\"%d\\\"}]},{\\\"%s\\\",[{\\\"%s\\\",\\\"%d\\\"}]}]\";\n' \
                $ID24  $PEAK_DURATION $PEAK_FREQ $PEAK_IDLE_TIME $FDN24 $ATTRIBUTE24 $ATTR_VAL241 ${FDN24} ${ATTRIBUTE24} ${ATTR_VAL242}

    printf 'avcburst:id=%d,duration=%d,freq=%s,idle_time=%d,loop=true,avcdata=\"[{\\\"%s\\\",[{\\\"%s\\\",\\\"%d\\\"}]},{\\\"%s\\\",[{\\\"%s\\\",\\\"%d\\\"}]}]\";\n' \
               $ID25  $PEAK_DURATION $PEAK_FREQ $PEAK_IDLE_TIME $FDN25 $ATTRIBUTE25 $ATTR_VAL251 ${FDN25} ${ATTRIBUTE25} ${ATTR_VAL252}

    printf 'avcburst:id=%d,duration=%d,freq=%s,idle_time=%d,loop=true,avcdata=\"[{\\\"%s\\\",[{\\\"%s\\\",\\\"%d\\\"}]},{\\\"%s\\\",[{\\\"%s\\\",\\\"%d\\\"}]}]\";\n' \
               $ID26  $PEAK_DURATION $PEAK_FREQ $PEAK_IDLE_TIME $FDN26 $ATTRIBUTE26 $ATTR_VAL261 ${FDN26} ${ATTRIBUTE26} ${ATTR_VAL262}

    printf 'avcburst:id=%d,duration=%d,freq=%s,idle_time=%d,loop=true,avcdata=\"[{\\\"%s\\\",[{\\\"%s\\\",\\\"%s\\\"}]},{\\\"%s\\\",[{\\\"%s\\\",\\\"%s\\\"}]}]\";\n' \
               $ID27  $PEAK_DURATION $PEAK_FREQ $PEAK_IDLE_TIME $FDN27 $ATTRIBUTE27 $ATTR_VAL271 ${FDN27} ${ATTRIBUTE27} ${ATTR_VAL272}

    printf 'avcburst:id=%d,duration=%d,freq=%s,idle_time=%d,loop=true,avcdata=\"[{\\\"%s\\\",[{\\\"%s\\\",\\\"%s\\\"}]},{\\\"%s\\\",[{\\\"%s\\\",\\\"%s\\\"}]}]\";\n' \
               $ID28  $PEAK_DURATION $PEAK_FREQ $PEAK_IDLE_TIME $FDN28 $ATTRIBUTE28 $ATTR_VAL281 ${FDN28} ${ATTRIBUTE28} ${ATTR_VAL282}

   printf 'avcburst:id=%d,duration=%d,freq=%s,idle_time=%d,loop=true,avcdata=\"[{\\\"%s\\\",[{\\\"%s\\\",\\\"%s\\\"}]},{\\\"%s\\\",[{\\\"%s\\\",\\\"%s\\\"}]}]\";\n' \
               $ID29  $PEAK_DURATION $PEAK_FREQ $PEAK_IDLE_TIME $FDN29 $ATTRIBUTE29 $ATTR_VAL291 ${FDN29} ${ATTRIBUTE29} ${ATTR_VAL292}
	sleep 1 
        
    done
}


stop_peak() {
    for SIM in ${SIM_LIST[*]} 
	do
	
        echo .open $SIM

        echo .select network
        echo "stopburst:id=$ID21;"
		echo "stopburst:id=$ID22;" 
		echo "stopburst:id=$ID23;" 
		echo "stopburst:id=$ID24;"
		echo "stopburst:id=$ID25;"
		echo "stopburst:id=$ID26;"
		echo "stopburst:id=$ID27;"
		echo "stopburst:id=$ID28;"
		echo "stopburst:id=$ID29;"
    done
}

initializeLoadParameters() {
	NUM_OF_SIMS=$((${#SIM_LIST[@]}-1))
	echo $NUM_OF_SIMS
        STD_FREQ=$(echo "scale=5;$STD_NOTIF_RATE_PER_SEC"  | bc | sed 's/^\./0./')
#	STD_FREQ=$(echo "scale=4;$STD_NOTIF_RATE_PER_SEC*$NUM_OF_SIMS" | bc | sed 's/^\./0./')
        PEAK_FREQ=$(echo "scale=5;$PEAK_NOTIF_RATE_PER_SEC"  | bc | sed 's/^\./0./')
#	PEAK_FREQ=$(echo "scale=4;$PEAK_NOTIF_RATE_PER_SEC*$NUM_OF_SIMS" | bc | sed 's/^\./0./')
	STORM_FREQ=$(echo "scale=5;$STORM_NOTIF_RATE_PER_SEC"  | bc | sed 's/^\./0./')
#	STORM_FREQ=$(echo "scale=4;$STORM_NOTIF_RATE_PER_SEC*$NUM_OF_SIMS" | bc | sed 's/^\./0./')

}

################################################CLI parsing begin####################
#echo ${TASK}
#echo ${LOAD_TYPE}
getSims
initializeLoadParameters

if [ "${TASK}" = "start" ] ; then
    if [ "${LOAD_TYPE}" = "standard" ] ; then
	    start_standard
    elif [ "${LOAD_TYPE}" = "peak" ] ; then
	    start_peak
    elif [ "${LOAD_TYPE}" = "storm" ] ; then
	    start_storm
    else
	   echo "load type should be one of standard | peak | storm "
    fi
elif [ "${TASK}" = "stop" ] ; then
    if [ "${LOAD_TYPE}" = "standard" ] ; then
	    stop_standard
    elif [ "${LOAD_TYPE}" = "peak" ] ; then
	    stop_peak
    elif [ "${LOAD_TYPE}" = "storm" ] ; then
	    stop_storm
    else
           echo "load type should be one of standard | peak | storm "
    fi
elif [ "${TASK}" = "show" ] ; then
    show
else
    echo "please use either start | stop | show as first argument "
fi

################################################CLI parsing end ####################



























