FROM tomcat:8.5-jdk8-temurin

# 将 target 目录下的 WAR 包复制到 Tomcat 的 webapps 目录下
COPY target/mybookshop.war /usr/local/tomcat/webapps/

# 暴露 Tomcat 默认端口
EXPOSE 8080
