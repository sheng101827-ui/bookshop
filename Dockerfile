FROM tomcat:8.5-jdk8-temurin

LABEL maintainer="bookshop"
LABEL description="MyBookshop WAR application deployed on Tomcat 8.5"

RUN rm -rf /usr/local/tomcat/webapps/*

COPY target/mybookshop.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]