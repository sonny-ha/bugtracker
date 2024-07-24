# Bugtracker Spring Boot CLI App

This is a Spring Boot application that will open a shell on boot, allowing you to write and "close" to bugtracker.csv. Currently, 
there are issues when running in from a jar because resourceloader cannot find lastid.txt in BOOT-INF/classes (it is in the jar).

Running it in the IDE should work. Also, I was not able to get Docker to work. Had issues installing Docker on my machine and also
when running "mvn jib:dockerBuild" I would get this error: ###"The configured platforms don't match the Docker Engine's OS and architecture (/)"

## Commands
* **create** --description --parentId --link
* **close** --id
