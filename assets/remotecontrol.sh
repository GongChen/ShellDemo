# Script to start "remotecontrol" on the device, which has a very rudimentary
# shell.
#
export CLASSPATH=/data/data/com.molo.demo/remotecontrol.jar
exec app_process /system/bin com.molo.remote.RemoteControl $*
