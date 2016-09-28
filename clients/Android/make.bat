@echo off
cls 

rd /q/s bin obj
md bin obj

set DEV_HOME=%1
if .%DEV_HOME%==. set DEV_HOME=.
set JDK=C:\Program Files\Java\jdk1.7.0_79
set ASDK=T:\sdk

set BUILD_TOOLS=%ASDK%\build-tools\android-4.4.2
set API-LEVEL=android-4.4.2

set DEX=%BUILD_TOOLS%\dx

set AAPT=%BUILD_TOOLS%\aapt
set AAPT_OPT=package -v -f -M %DEV_HOME%\AndroidManifest.xml -S %DEV_HOME%\res -I %ASDK%\platforms\%API-LEVEL%\android.jar

set JAVA_CLASSPATH=%ASDK%\platforms\%API-LEVEL%\android.jar;%DEV_HOME%\obj
set ZIPALIGN=%BUILD_TOOLS%\zipalign

:: 11.create R.java
  "%AAPT%" %AAPT_OPT% -m -J %DEV_HOME%\src
:: 12.compile code
  "%JDK%\bin\javac" -verbose -d %DEV_HOME%\obj -classpath %JAVA_CLASSPATH% -sourcepath %DEV_HOME%\src %DEV_HOME%\src\my\drafts\wifi\leds\*.java
:: 13.create dex
  call "%DEX%" --dex --verbose --output=%DEV_HOME%\bin\classes.dex %DEV_HOME%\obj %DEV_HOME%\lib
::exit
:: 14.create apk
  "%AAPT%" %AAPT_OPT% -F %DEV_HOME%\bin\AndroidTest.unsigned.apk %DEV_HOME%\bin
:: 15.sign
  "%JDK%\bin\jarsigner" -verbose -keystore %DEV_HOME%\AndroidTest.keystore -storepass password -keypass password -signedjar %DEV_HOME%\bin\AndroidTest.signed.apk %DEV_HOME%\bin\AndroidTest.unsigned.apk AndroidTestKey
:: 16.zip
  "%ZIPALIGN%" -v -f 4 %DEV_HOME%\bin\AndroidTest.signed.apk %DEV_HOME%\bin\AndroidTest.apk

