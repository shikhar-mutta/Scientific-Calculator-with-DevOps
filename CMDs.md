sudo su - jenkins

sudo docker attach scientific-calculator 

sudo docker images

sudo docker ps -a

docker rm -f $(docker ps -aq)

docker run -it shikhar68/scientific-calculator:latest
