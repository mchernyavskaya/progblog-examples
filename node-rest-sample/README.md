# Sample Node Express application 

Created from a template, changed to contain `hello` endpoint and dockerized.

## How to run

```
  cd node-rest-sample
  ./startup.sh
  
  # or with Dockerfile
  docker run -p 3000:3000 marynacherniavska/node-rest-sample
```

## How to use the /hello endpoint

```
curl -XGET http://localhost:3000/hello/alex
```

Will return something like:

```
{
  "hello": "alex"
}
```