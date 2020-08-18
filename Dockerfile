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

ARG env="local"

# Run Maven build
RUN if [ "$env" != "ci" ]; then mvn clean install -DskipTests ; fi

# Tests coverage executed on CI only
RUN if [ "$env" = "ci" ]; then mvn clean install && mvn jacoco:report coveralls:report -Dbranch=master; fi

ENTRYPOINT [ "java", "-jar", "target/scrooge-api.jar"]