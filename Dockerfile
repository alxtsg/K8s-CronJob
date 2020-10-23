FROM openjdk:15

COPY . /usr/src/myapp

WORKDIR /usr/src/myapp

RUN javac Main.java Event.java

CMD ["java", "Main"]
