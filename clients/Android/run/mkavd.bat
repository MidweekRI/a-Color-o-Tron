set AVD_HOME=T:\avd\test
md %AVD_HOME%

call a.bat create avd -n test-dev -c 128M -d 15 -p %AVD_HOME% -t android-24 -s 240x320 --force

::exit
set AVD_INI=%AVD_HOME%\config.ini

echo disk.dataPartition.size=200M >> %AVD_INI%
echo hw.gpu.enabled=yes >> %AVD_INI%
echo hw.keyboard=yes  	>> %AVD_INI%
echo skin.dynamic=no 	>> %AVD_INI%
::echo skin.name=240x320 >> %AVD_INI%
::echo skin.path=240x320 >> %AVD_INI%