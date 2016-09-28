set ASDK=T:\sdk
set AVD_NAME=test-dev

:: 17.start emul
::%ASDK%\tools\emulator %*
%ASDK%\tools\emulator -wipe-data -datadir T:\android\ @%AVD_NAME%
