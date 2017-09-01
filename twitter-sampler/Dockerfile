FROM java:alpine
RUN apk add --update bash && apk add --update curl && rm -rf /var/cache/apk/*
EXPOSE  8080
EXPOSE  8080/udp
WORKDIR /srv
ADD build/libs/twitter-sampler-*.jar /srv/twitter-sampler.jar
CMD java -jar twitter-sampler.jar
