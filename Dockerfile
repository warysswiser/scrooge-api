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
RUN mvn clean install -DskipTests

# Tests coverage executed on CI only
ARG env="local"
RUN if [ "$env" = "ci" ]; then mvn test && mvn jacoco:report coveralls:report ; fi

ENTRYPOINT [ "java", "-jar", "target/scrooge-api.jar"]