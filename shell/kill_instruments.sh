# 判断instruments是否未响应，导致mac book卡
Number=`ps -ef | grep -c /Applications/Xcode.app/Contents/Applications/Instruments.app/Contents/MacOS/Instruments`
PID=`ps -ef | grep /Applications/Xcode.app/Contents/Applications/Instruments.app/Contents/MacOS/Instruments | awk '{print $2}' | head -n 1`
number=`ps -ef | grep -c instruments`
pid=`ps -ef | grep -c instruments | awk '{print $2}' | head -n 1`
if [ $number == 1 ]; then
	echo "[Info]: instruments is not open"
elif [ "$pid" != "" ]; then
        echo "[Info]: Kill instruments"
        kill -9 $pid
else
        echo "[Info]: instruments is not open"
fi
if [ $Number == 1 ]; then
        echo "[Info]: Instruments is not open"
elif [ "$PID" != "" ]; then
        echo "[Info]: Kill Instruments"
        kill -9 $PID
else
        echo "[Info]: Instruments is not open"
fi
