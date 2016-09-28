
set JDK=C:\Program Files\Java\jdk1.8.0_25

:: 8. create keystore
set CERT_NAME="CN=company name,OU=organisational unit,O=organisation,L=location,S=state,C=country code"
"%JDK%\bin\keytool" -genkeypair -validity 10000 -dname %CERT_NAME% -keystore %DEV_HOME%\AndroidTest.keystore -storepass password -keypass password -alias AndroidTestKey -keyalg RSA -v