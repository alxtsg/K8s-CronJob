# K8s-CronJob #

## Description ##

A simple project that demonstrates the use of CronJob in Google Kubernetes
Engine.

This project consists of the following components:

* A Java program.
* A Dockerfile.
* A Kubernetes manifest.

The Java program (`Event.java` and `Main.java`) gets a list of events with
timestamps and count the items in the list. In real-world application the list
may be obtained from a database, an API, or any data source. For demonstration
purpose, the data in the Java program is generated randomly.

The Dockerfile (`Dockerfile`) specifies what are being included in the Docker
image being built. In this project, the Docker image being built is based on the
public Docker image `openjdk:15` which has OpenJDK installed in it. With OpenJDK
in the Docker image, we can compile and run the Java program inside the Docker
container. When the Docker image is built, we have to publish the image for
Kubernetes to retrieve and run it.

The Kubernetes manifest specifies the Kubernetes resources to be setup. In this
project, we define a CronJob that executes a job periodically. The
`.spec.schedule` specifies the frequency and when to execute the job, in
crontab(5) format. In this project, we specify the schedule as `"*/1 * * * *"`,
which means the job is executed every 1 minute. The job itself is defined by
`.spec.jobTemplate`, and in this project the default Docker image being used
(`0xd8c4eb/simple-counter:1.0.0`) was built using the Dockerfile and published
to the Docker Registry on Docker Hub. The `restartPolicy` field is set to
`OnFailure` such that the container is restarted when it failed to exit
successfully (e.g. the container runs out of memory, or the list counting in the
Java program failed for some reasons).

## Requirements ##

* Java SDK (`>=15`).
* Docker.
* Kubernetes.

## Configuration ##

You should have a Docker Hub account and a repository to host the Docker image.

You should have a Google Cloud Platform account ready and have the Google
Kubernetes Engine API enabled.

In your project on Google Cloud Platform, navigate to Kubernetes Engine and
create a cluster.

## Usage ##

To setup the complete project, we will do the following:

* Build the Docker image.
* Publish the Docker image to the Docker Registry on Docker Hub.
* Setup the workload on the Kubernetes cluster.

### Build the Docker image ###

To build the Docker image on your computer:

```
docker build -t simple-counter .
```

You can verify that the Docker image is built successfully by:

```
docker image ls | grep simple-counter
```

You should be able to see a line similar to the following:

```
simple-counter            latest              72805cd35eb8        27 hours ago        524MB
```

### Publish the Docker image ###

Before the Docker image can be published, tag the Docker image:

```
docker image tag simple-counter:latest your-repo-name/simple-counter:1.0.0
```

Replace `your-repo-name` with your own repository on Docker Hub. From now on, we
refer to your Docker image as `your-repo-name/simple-counter:1.0.0`.

You can verify that the Docker image has been tagged by:

```
docker image ls | grep simple-counter
```

You should be able to see a line similar to the following:

```
your-repo-name/simple-counter   1.0.0               72805cd35eb8        27 hours ago        524MB
```

Now we can publish the Docker image:

```
docker push your-repo-name/simple-counter:1.0.0
```

You should be able to see the Docker image on Docker Hub.

### Setup the workload on the Kubernetes cluster ###

On Google Cloud Platform, in your project, navigate to Kubernetes Engine and
activate a Cloud Shell. Click on the Connect button, run the `gcloud` command
in the Cloud Shell to configure `kubectl`. You should be able to find the
configuration in `~/.kube/config` in the Cloud Shell.

In `k8s-cronjob.yaml`, change the `image` field to point to the Docker image
you published, i.e. `your-repo-name/simple-counter:1.0.0`, so that your own
Docker image will be used.

Now upload the `k8s-cronjob.yaml` file to the Cloud Shell and run:

```
kubectl apply -f k8s-cronjob.yaml
```

You should be able to see a workload called `k8s-cronjob` in the Workloads page.
When the workload has been setup successfully, the job is executed every 1
minute as specified in the Kubernetes manifest. Click on `k8s-cronjob` and
navigate to the Logs tab, you should be able to see lots of messages:

```
...
2020-10-23T21:52:05.887408625Z 1603489925835: Event click_logo
2020-10-23T21:52:05.887626262Z 1603489925835: Event click_logo
2020-10-23T21:52:05.887818735Z 1603489925835: Event click_logo
2020-10-23T21:52:05.888058931Z 1603489925835: Event click_logo
2020-10-23T21:52:05.888274228Z 1603489925835: Event click_logo
2020-10-23T21:52:05.888749916Z There are 49 matching events.
```

The logs are produced when the Docker container runs and the Java program counts
the timestamp.

Check the log again 1 minute later, you will be able to find another set of
logs created.

In real-world application, the processing results may be stored on a database
directly, or a report is generated and placed on an object storage like S3.

## License ##

[The BSD 3-Clause License](http://opensource.org/licenses/BSD-3-Clause)
