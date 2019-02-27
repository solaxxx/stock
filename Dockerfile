FROM registry-vpc.cn-beijing.aliyuncs.com/rsq-public/tomcat:8.0.50-jre8-memcached-v3

LABEL name="quilty-system" \
       description="quilty-system" \
       maintainer="rishiqing group" \
       org="rishiqing"

# set default time zone to +0800 (Shanghai)
ENV TIME_ZONE=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TIME_ZONE /etc/localtime && echo $TIME_ZONE > /etc/timezone

ENV CATALINA_HOME=/usr/local/tomcat
WORKDIR $CATALINA_HOME

ADD stock.war webapps/stock.war

