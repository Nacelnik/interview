mvn clean package dockerfile:build
docker run -d -p 27017:27017 --name closeit-interview-db mongo
docker start closeit-interview-db
docker run -d -p 5000:8080 closeit-interview:0.0.1 --name closeit-interview-app