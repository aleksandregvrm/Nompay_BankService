FROM ubuntu:latest
LABEL authors="sandrogvaramia"

ENTRYPOINT ["top", "-b"]