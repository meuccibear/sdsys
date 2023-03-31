#FROM tomcat:8.5-jre8-temurin-focal
FROM dordoka/tomcat:tomcat8.5
LABEL description="chx"
ADD api/target/*.war /opt/tomcat/webapps
ADD ui/src /opt/tomcat/webapps/ui
RUN rm -f /etc/localtime \
&& ln -sv /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
&& echo "Asia/Shanghai" > /etc/timezone \
env JAVA_OPTS="\
-server \
-Xms2g \
-Xmx2g "

#
