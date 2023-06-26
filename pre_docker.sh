rm -rf ./docker
./gradlew clean
./gradlew build -x test
mkdir docker
cp ./build/libs/jwtauth-1.jar ./docker/app.jar
cd docker
jar -xf ./app.jar