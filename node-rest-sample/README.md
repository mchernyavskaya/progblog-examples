# Sample Node Express application to deploy to EC2 

Created from a template, changed to contain `hello` endpoint and dockerized.

## How to run

```bash
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

```json
{
  "hello": "alex"
}
```

## How to deploy to EC2 instance

### Create a tiny free-tier instance

### Create and download key file XXX.pem

### Copy file to safe location on your disk and change permissions for it

```bash
chmod 400 ~/aws-keys/sample-instance.pem
```

### Log in to EC2 instance with a command:

```bash
ssh -i ~/aws-keys/sample-instance.pem ec2-user@ec2-18-194-88-37.eu-central-1.compute.amazonaws.com

```

You should see output like this:

```text
       __|  __|_  )
       _|  (     /   Amazon Linux AMI
      ___|\___|___|

https://aws.amazon.com/amazon-linux-ami/2017.09-release-notes/
[ec2-user@ip-172-31-33-179 ~]$ sudo yum update -y
Failed to set locale, defaulting to C
Loaded plugins: priorities, update-motd, upgrade-helper
amzn-main                                          | 2.1 kB     00:00
amzn-updates                                       | 2.5 kB     00:00
No packages marked for update
[ec2-user@ip-172-31-33-179 ~]$

```

### Install docker on EC2 instance

Use the manual from [here](https://docs.aws.amazon.com/AmazonECS/latest/developerguide/docker-basics.html#install_docker).

### Run your docker image on the EC2 instance

```bash
# bind port 3000 on the container to port 8080 on EC2
docker run --name node-rest --rm -d -p 8080:3000 marynacherniavska/node-rest-sample
```

### Set up load balancer

### Create a security group for the Node application with port 8080 open

Edit the instance security groups to add it to the application instance.

### Create a security group for the load balancer with ports 80 and 443

Edit the load balancer security groups to add it to the load balancer.

### Check the load balancer availability on port 80

```bash
curl http://node-rest-sample-785409892.eu-central-1.elb.amazonaws.com/hello/you
{"hello":"you"}
```

## Register a domain

Find a free hosting for your application, for example [here](http://www.dot.tk/en/index.html?lang=en)


