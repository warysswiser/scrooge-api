FROM ubuntu

# Install dependencies
RUN apt-get update && apt-get -y install \
    openjdk-14-jre \
    maven \
    tesseract-ocr \
    tesseract-ocr-eng \
    tesseract-ocr-fra
#    tesseract-ocr-all

ADD . /scrooge-api

WORKDIR /scrooge-api

# Run Maven build
RUN mvn clean install

COPY "target/scrooge-api.jar" app.jar

ENTRYPOINT [ "java", "-jar", "app.jar"]

EXPOSE 8089