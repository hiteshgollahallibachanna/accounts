FROM openjdk:11

ENV ACCOUNTS_OPTS='-Djava.security.egd=file:/dev/./urandom -Dserver.port=8080 $JAVA_JVM_ARG_HEAP_SIZE_MIN $JAVA_JVM_ARG_HEAP_SIZE_MAX'
ADD build/distributions/*.tar /
EXPOSE 8080

ENTRYPOINT ["/accounts/bin/accounts"]
