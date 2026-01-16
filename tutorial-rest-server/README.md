# Tutorial REST Server

RESTful OpenDMA service providing read-only access to tutorial repository.

## Usage

Run with
```
docker run -p 8080:8080 ghcr.io/opendma/tutorial-xmlrepo:latest
```

Access the restful service with http://localhost:8080/opendma/

If the GitHub Container Registry (ghcr.io) is blocked in your environment, you can use our mirror on Docker Hub:
```
docker run -p 8080:8080 opendma/tutorial-xmlrepo:latest
```

If the local port 8080 is already in use, you can map to a different port, e.g. 8090:
```
docker run -p 8090:8080 ghcr.io/opendma/tutorial-xmlrepo:latest
```

To run in deamon mode in the background:
```
docker run -d --name opendma-tutorial-xmlrepo -p 8080:8080 ghcr.io/opendma/tutorial-xmlrepo:latest
```

## Explore service

Service root listing all available repositories:  
http://localhost:8080/opendma/

Description object of repository `sample-repo`:  
http://localhost:8080/opendma/obj/sample-repo

Root of the class hierarchy (i.e. `opendma:Object`):  
http://localhost:8080/opendma/obj/sample-repo/opendmaClassObject

Root folder of repository:  
http://localhost:8080/opendma/obj/sample-repo/sample-folder-root

Folder A in the repository:  
http://localhost:8080/opendma/obj/sample-repo/sample-folder-a

Document A1 in the repository:  
http://localhost:8080/opendma/obj/sample-repo/sample-document-a1

## Build

This maven build combines the [opendma-rest-server](https://github.com/OpenDMA/opendma-rest-server) with the
[opendma-java-xmlrepo](https://github.com/OpenDMA/opendma-java-xmlrepo) and the
[SampleRepository.xml](../src/main/resources/SampleRepository.xml) from this tutorial.

The final server is then packaged as docker image and deployed to GitHub Container Registry and Docker Hub.

Run `mvn package` to build the docker image locally or `mvn deploy` to build multi arch docker image
and push it to GHCR and Docker Hub.