SERVICE_NAME=
PATH_TO_JAR=
JVM_OPTIONS=

CNT=`ps -ef | grep $SERVICE_NAME | grep java | grep jar | wc -l`
if [ $CNT -ne 0 ]
then
                echo "$SERVICE_NAME is already running"
fi

while :
do
        CNT=$(ps -ef | grep "$SERVICE_NAME" | grep java | grep jar | wc -l)
        if [ "$CNT" -eq 0 ]
        then
                echo "Starting $SERVICE_NAME..."
                java "$JVM_OPTIONS" -jar "$PATH_TO_JAR" 1>/dev/null 2>&1 &
                echo "Started $SERVICE_NAME..."
        fi
sleep 10
done