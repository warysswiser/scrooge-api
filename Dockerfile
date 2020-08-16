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
RUN mvn clean install jacoco:report coveralls:report

ENTRYPOINT [ "java", "-jar", "target/scrooge-api.jar"]

EXPOSE 8089