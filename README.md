# coffeeshop

    Technology used : Java 8,Maven,Springboot,JPA,Swagger UI, Docker
    Database : H2 In memory DB
    version Control : github
    Tools : InteijIDEA,Postman, Docker Desktop,SonarQube
 ## How to Run
    step 1 : Configure springboot application and click the run  button
    Step 2 : execute below link for Swagger ui and Database

     H2 In Memeory database is used and below is the localurl for H2. 
     Database link : http://localhost:8080/h2-console
              username : sa   
              Password : password
              
     Swagger UI is used to document the APIs and below is the local url Swagger.     
     Swagger Ui link : http://localhost:8080/swagger-ui.html
     
   #### Docker Configuration
   
     Docker file is added to dockerised the project below is the steps to dockeried the project
     Step 1 : Run Docker Desktop in the windows machine
     Step 2 : Open command promt
     Step 3 : Execute docker commands
     Step 4 : build docker image "docker build -t coffee-shop-app-docker ."
     Step 5 : Check if docker image is created or not "docker images"
     Step 6 : run the docker image "docker run -p8002:8080 <dockerId>"
     Step 7 : Application started and check on "localhost:8002/api/toppings"
     
 #### Execute the postman collection APIs     
 
   
