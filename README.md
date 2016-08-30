# Welcome to Peapod Client

Peapod is a binary storage system built in front of Amazon S3 to allow attaching metadata to the stored binaries.

This command line tool is everything you need to interact with the Peapod system. Everything you do with the command line tool will be visible in the Green Page http://api01.peapod.hh-wev.ggs-net.com/ .

Peapod is very simple, and in order to use it you just need to understand a few concepts:

* **Pod**. A Pod is a container for binaries. A Pod is mapped to an Amazon S3 bucket, but you don't need to know about this.
Each Pod is also a separate name-space. It's the maven equivalent of the groupId.
* **Pea**. A Pea is an abstraction over a file which can exist in different versions.
For example, in Pod "MyGame" I can have several peas named "server", "AndroidClient", "iOSClient".
It's the maven equivalent of the artifactId.
* **Version**. I am not going to explain to you what a version is, am I?
* **Artifact**. A specific version of a pea in a pod. An artifact is a binary file stored in the Peapod system, identified by <Pod>:<Pea>:<Version>.
* **Attribute**. A key-value pair that you can attach to an artifact.
* **Comment**, **Tag**. I am not going to explain to you what they are, am I?

## Dependencies
Since `maven`, together with `assembly` plugin, was used to build the project, all dependencies are already included in the `.jar` file.

## Configuration
The peapod client reads the credentials needed either from `peapod.credentials` file in the project root folder or you can specify credentials when you run commands.


### Set credentials in a file

Here is an example of `peapod.credentials` file:

```sh
name=<your_username>
token=<your_token>
```
If you do not want to create this file manually, you can run the following command:

```sh
java -jar peapod.jar -setCredentials <your_username>:<your_token>

```
### Set credentials when running command

As an alternative, you can specify the credentials as system properties:

```sh
java -Duser=<username> -Dtoken=<token> -jar peapod.jar -listPods
```

----------------------------------------------------------------------

## Usages

#### Help:
```sh
java -jar peapod.jar -h | --help
```
#### Credentials:
```sh
java -jar peapod.jar -setCredentials <username>:<token>
```

#### Pods
List all pods:
```sh
java -jar peapod.jar -listPods
```

Create a pod:
```sh
java -jar peapod.jar -createPod <myPod>
```

Delete a pod:
```sh
java -jar peapod.jar -deletePod <myPod>
```

#### Peas
Create a pea in a specific pod:
```sh
java -jar peapod.jar -createPea <myPod>:<myPea> [-description <description>]
```

Delete a pod:
```sh
java -jar peapod.jar -deletePea <myPod>:<myPea>
```

#### Artifacts

Get information on an artifact or artifacts of a pea:
```sh
java -jar peapod.jar -getArtifact <myPod>:<myPea>[:<version>]
```

Upload an artifact of a specific pea:
```sh
java -jar peapod.jar -uploadArtifact <myPod>:<myPea>:<version> -filepath <filepath>
```

Delete an artifact:
```sh
java -jar peapod.jar -deleteArtifact <myPod>:<myPea>:<version>
```

Download an artifact:
```sh
java -jar peapod.jar -downloadArtifact <myPod>:<myPea>:<version>
```
#### Tags
Create a tag:
```sh
java -jar peapod.jar -createTag <myPod>:<myPea>:<version> -tag <tag>
```
Delete a tag:
Note that each tag gets converted to capital letters
```sh
java -jar peapod.jar -createTag <myPod>:<myPea>:<version> -tag <TAG>
```
#### Comments
Create a comment:
```sh
java -jar peapod.jar -createComment <myPod>:<myPea>:<version> -comment <comment>
```
Delete a comment:
In order to delete a comment you need to have the corresponding comment-id
```sh
java -jar peapod.jar -deleteComment <commentID>
```

## Build project with maven

In order to use the peapod client, you will need JavaSE-1.7 or above. To build the project, you need Maven 3.2.2 or above.

With the `pom.xml` given in the project, you can easily build the project using:

```sh
mvn clean compile assembly:single
```

The project is tested with the following environment:
```sh
Apache Maven 3.2.2 (NON-CANONICAL_2015-04-01T06:56:20_mockbuild; 2015-04-01T08:56:20+02:00)
Java version: 1.8.0_65, vendor: Oracle Corporation
Default locale: en_US, platform encoding: UTF-8
OS name: "linux", version: "4.1.13-100.fc21.x86_64", arch: "amd64", family: "unix"
```
