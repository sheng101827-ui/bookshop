FROM tomcat:8.5

MAINTAINER YourName <youremail@example.com>

COPY target/mybookshop.war /usr/local/tomcat/webapps/

EXPOSE 8080

CMD ["catalina.sh", "run"]
