#creating an image with default image as base
FROM alpine
# creates an image of Target war when run "docker run"
COPY ./build/libs/Target.war ./Target.war

