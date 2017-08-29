FROM java:alpine
RUN apk add --update bash && rm -rf /var/cache/apk/*
EXPOSE  8195
EXPOSE  8125/udp
WORKDIR /srv
ADD build/libs/spring-boot-kubernetes-*.jar /srv/spring-boot-kubernetes.jar
CMD java -jar spring-boot-kubernetes.jar
