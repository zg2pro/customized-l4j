rem    Used to generate key pair.
rem    The private key will be used for creating license files
rem    and public key will be used for validating license files.
java -cp ..\..\lib\license4j-1.5.1.jar;..\lib\demo.jar;..\lib\asc-for-license4j.jar; com.smardec.license4j.demo.CreateKeyPair keys.txt