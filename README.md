# H.E.A.R *(A Schedule Management System)*

## Development Environment:
1. IntelliJ IDEA Ultimate Edition (cuz it facilitates development with Spring Boot)
2. JDK 25
3. Maven 4.0.0
4. MySQL 8.0+
5. Docker v28.4.0 (I believe any recent version would work just fine)

## Setting Up The Database Thing:
To overcome the annoying process of setting up the DB correctly without the need to update the DBMS and creating a connection with the same username, password and port, I Decided to use a **docker  compose container**.

To run the container:
1. Make sure docker is installed and the service works just fine.
2. Open Terminal & Navigate to the containing directory.
3. Run:
```
docker compose up -d
```
4. Try running the project to make sure everything works just fine.
```
HEAR/src/main/java/com/hear/hear
```

## *Enjoy Development :D*



