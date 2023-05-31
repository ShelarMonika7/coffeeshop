FROM openjdk:8
ADD target/coffee-shop-app.jar coffee-shop-app.jar
ENTRYPOINT ["java", "-jar","/coffee-shop-app.jar"]