FROM java:8-jre

ADD ./target/currency-exchange-service-0.0.1.jar /app/

ENTRYPOINT ["java", "-Xmx200m", "-jar", "/app/currency-exchange-service-0.0.1.jar"]
EXPOSE 8000