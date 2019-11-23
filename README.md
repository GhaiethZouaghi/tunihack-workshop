# Docker Demo

## Dockerfile Hello World

1. Create Hello World Dockerfile
2. Run it

## Create Network and Volume
1. Create tunihack network
```
./scripts/create-tunihack-network.sh
```

2. Create sbt-data volume
```
./scripts/create-sbt-data.sh
```

## Create Database container
```
docker-compose -f cassandra.yml up -d
```

## Create Schema Manager image and run it
```
pushd schema-manager
docker build -t ghaiethzouaghi/schema-manager:tunihack .
popd

docker run ghaiethzouaghi/schema-manager:tunihack
```

## Build the APIs
### Build the SBT image
```
pushd sbt
docker build -t cognira/sbt .
popd
```

### Package the Student API 
```
docker-compose -f studentapi/build.yml run --rm sbt sbt pack
```

### Package the Teacher API
```
docker-compose -f teacherapi/build.yml run --rm sbt sbt pack
```

### Build & Run the API images
```
docker-compose up --build -d
```

## Test the APIs using PostMan

# Kubernetes Demo

## Start Minikube
```
cd kubernetes
minikube start
```

## Create Namespace
```
kubectl apply -f namespace.yaml
```

## Deploy Database Pod
```
kubectl apply -f cassandra.yaml
```

## Run the Schema Manager
```
kubectl apply -f schema-manager.yaml
```

## Deploy the APIs
```
kubectl apply -f kubernetes-deployment.yaml
```

## Get the URLs and test
```
minikube service student-service -n=tunihack --url
minikube service teacher-service -n=tunihack --url
```