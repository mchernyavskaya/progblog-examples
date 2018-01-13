# docker build . -t marynacherniavska/node-rest-sample
FROM node:9.4.0-alpine

COPY bin /app/bin
COPY public /app/public
COPY routes /app/routes
COPY views /app/views
COPY *.js *.json *.sh app/

RUN chmod +x /app/startup.sh

CMD ["sh", "/app/startup.sh"]
