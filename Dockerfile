FROM ubuntu

# Install dependencies
RUN apt-get update && apt-get -y install \
    openjdk-14-jre \
    maven \
    tesseract-ocr \
    tesseract-ocr-eng \
    tesseract-ocr-fra

ADD . /scrooge-api

WORKDIR /scrooge-api

ARG env="prod"

# Run Maven build
RUN if [ "$env" = "ic" ]; then mvn clean install; else mvn clean install -DskipTests; fi

ENTRYPOINT [ "java", "-jar", "target/scrooge-api.jar"]