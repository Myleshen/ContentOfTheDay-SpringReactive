FROM ghcr.io/graalvm/jdk-community:21

COPY target/content-notification-0.0.1-SNAPSHOT.jar /content-notification.jar

CMD ["java", "-jar", "/content-notification.jar"]