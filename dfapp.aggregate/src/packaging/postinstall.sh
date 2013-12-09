#!/bin/sh

/usr/share/defense4all/install/config_cassandra.bash
if [ $? -ne 0 ]; then
	echo "Fail to configure cassandra for defense4all"
	exit -1
fi

/usr/share/defense4all/install/config_rsyslog.bash
if [ $? -ne 0 ]; then
	echo "Fail to configure rsyslog for defense4all"
	exit -1
fi

# create start-up run levels
if [ -e /sbin/chkconfig ] ; then
	/sbin/chkconfig --add cassandra 
	/sbin/chkconfig --add defense4all
elif [ -e /usr/sbin/update-rc.d ] ; then	
	/usr/sbin/update-rc.d cassandra defaults 20 80
	/usr/sbin/update-rc.d defense4all defaults 90 15
fi	

service cassandra   restart  > /dev/null 2>&1
service defense4all start 
