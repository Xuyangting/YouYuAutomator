. /etc/profile
# kill appium server
PID=`ps -ef | grep /usr/local/bin/appium | grep 4727 | awk '{print $2}' | tail -n 1`
echo $PID
if [ "$PID" != "" ]; then
	echo "kill appium server"
        kill -9 $PID
else
	echo "appium server is not open"
fi

# sudo /usr/local/bin/authorize-ios
# -U "1e4b0cceec2ad4059e29138bf73e116a369e8952"
/usr/local/bin/node /usr/local/bin/appium -a 127.0.0.1 -p 4727 --no-reset
