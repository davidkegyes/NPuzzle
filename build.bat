rmdir /Q/S build
mkdir build
javac -d build src/edu/sapi/mestint/*.java
cd build
jar cvfe ../NPuzzle.jar edu.sapi.mestint.NPuzzle edu/sapi/mestint/*.class
cd ..