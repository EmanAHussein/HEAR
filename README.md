# H.E.A.R *(A Collaborative Student Platform)*

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
5. Put the Entities package outside the project temporarily
6. Try running the project to make sure everything works just fine.
7. Return the Entities package back to its place in 
```
HEAR/src/main/java/com/hear/hear
```
***Explanation: I'm using Flyway to manage and run DB migrations but when the Java classes of the entities already exist Flyway trys to create the DB from the Entity Classes not from the initial migration***

- **If the whole thing seems ridiculous and sophisticated just do this:**
1. Update MYSQL to 8.0+
2. Create new connection with 
   1. user: root
   2. password: root
   3. port: 3307
4. continue from step **5.** of setting up the container thing.

## *Enjoy Development :D*



