#!/bin/sh
#------------------------------------------------------------------------------
# HT Connected Home Server start/stop script 
#------------------------------------------------------------------------------
# programmer : hyocheol ahn
#------------------------------------------------------------------------------
# start date : 2014/03/31
#------------------------------------------------------------------------------

# program directory
#root_dir="/home/websearch/HyundaiTel"
root_dir="."
program="iot.cloud.server"
program_name="HTCloudServer"

jar_list="ht-iot-connected-home-backend-server-*.jar"

logfile="$root_dir/serverstartlog.log"
this_script="iot-connected-home-server.sh"
JAVA="java -Xmx256m"

# get process pid
getpids(){
	pids=`ps -Af | grep "$1" | grep -v grep | sed -e 's/^........ *//' -e 's/ .*//'`
	echo "$pids"
}

# start function
start() {
	# Start daemons.
	pids=`getpids $program`
	if [ "$pids" != "" ]; then
		echo "$program_name: already running"
  else
    echo "$program_name: Starting..."
    ulimit -c unlimited
    ulimit -n 65530
    $JAVA -jar $jar_list $program &
	fi
}

# stop function
stop() {
	# Stop daemons.
	pids=`getpids $program`
	if [ "$pids" != "" ]; then
  	echo "$program_name : Stopping... "
  	kill $pids
  fi
}

# status function
status() {
	pids=`getpids $program`
	if [ "$pids" != "" ]; then
		echo "$program_name: running(pid:$pids)"
  else
		echo "$program_name: not running"
	fi
	
	pids=`getpids "$this_script watchdog"`
	if [ "$pids" != "" ]; then
		echo "watchdog: running(pid:$pids)"
  else
		echo "watchdog: not running"
	fi
}

start_program(){
	d=`date +%Y/%m/%d_%H:%M:%S`

  echo "[$d] $1: Starting..." >> $logfile
  ulimit -c unlimited
  ulimit -n 65534
  $JAVA -jar $jar_list $program &
}

watchdog(){
	# 최소 실행시 프로세스가 정상적으로 동작할 수 있도록 일정시간 대기한다.
	echo "watchdog: Starting..."
	sleep 60
	while [ 1 ]; do
	
		# 프로세스가 존재하지 않는 경우, 프로세스를 다시 시작하여 준다.
		pids=`getpids $program`
		if [ "$pids" = "" ]; then
			start_program
		fi
		
		sleep 10
	done
}

# watchdog start
watchdog_start(){
	$root_dir/$this_script watchdog &
}

# watchdog stop
watchdog_stop(){
	pids=`getpids "$this_script watchdog"`
	for p in $pids
	do
		kill $p
	done
	echo "watchdog : Stopped"
}

# main function.
case "$1" in
	start)
		start
		watchdog_start
		;;
	stop)
		watchdog_stop
		stop
		;;
	status)
		status
		;;
	restart)
		stop
		sleep 10
		start
		;;
	watchdog)
		watchdog
		;;
	*)
		echo $"Usage: $0 {start|stop|status|restart}"
		exit 1
		;;
esac

