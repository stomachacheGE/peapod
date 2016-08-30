# Welcome to Peapod

Peapod is a binary storage system built in front of Amazon S3 to allow attaching metadata to the stored binaries.

This command line tool is everything you need to interact with the Peapod system. Everything you do with the command line tool will be visible in the Green Page http://api01.peapod.hh-wev.ggs-net.com/ .

Peapod is very simple, and in order to use it you just need to understand a few concepts:

### Pod
A Pod is a container for binaries. A Pod is mapped to an Amazon S3 bucket, but you don't need to know about this.
Each Pod is also a separate name-space.
It's the maven equivalent of the groupId

### Pea
A Pea is an abstraction over a file which can exist in different versions.
For example, in Pod "MyGame" I can have several peas named "server", "AndroidClient", "iOSClient".
It's the maven equivalent of the artifactId.

### Version
I am not going to explain to you what a version is, am I?

### Artifact
A specific version of a pea in a pod. An artifact is a binary file stored in the Peapod system, identified by <Pod>:<Pea>:<Version>.

### Comment
I am not going to explain to you what a comment is, am I?

### Tag
A tag is a label attached to an artifact. Equivalent to a git tag or a jira label.

### Attribute
A key-value pair that you can attach to an artifact.

----------------------------------------------------------------------

# Command line client for Peapod binary archiving system.

## Requirements
In order to use the peapod client, you will need Java 7 or above.

## Dependencies
Since `maven`, together with `assembly` plugin, was used to build the project, all dependencies are already included in the `.jar` file.

## How to configure peapod

The peapod client reads the credentials needed either from `peapod.credentials` file in the project root folder or you can specify credentials when you run commands.

If you need a peapod user and token, please access a link like `api01.peapod.hh-wev.ggs-net.com:50000/request_token/<user>`, where <user> is your AD account.
You will receive the credentials in your GGS email.

For any other requests (requesting for permissions, creation of a pod, creation of a peapod service user) please contact (what remains of) TI.

### Set credentials in a file

Here is an example of `peapod.credentials` file:

```bash
name=<your_username>
token=<your_token>
```
You do not need create this file manually. Instead, you can run the following command:

```bash
java -jar peapod.jar -setCredentials <your_username>:<your_token>

```
### Set credentials when running command

As an alternative, you can specify the credentials as system properties:

```bash
java -Duser=<username> -Dtoken=<token> -jar peapod.jar -listPods
```

----------------------------------------------------------------------

# Example Commands

## Help:
```bash
java -jar peapod.jar -h | --help
```
## Credentials:
```bash
java -jar peapod.jar -setCredentials <username>:<token>
```

## Pods
### List all pods
```bash
java -jar peapod.jar -listPods
```

### Create a pod
```bash
java -jar peapod.jar -createPod <myPod>
```

### Delete a pod
```bash
java -jar peapod.jar -deletePod <myPod>
```


## Peas
### Create a pea in a specific pod
```bash
java -jar peapod.jar -createPea <myPod>:<myPea> [-description <description>]
```


### Delete a pod
```bash
java -jar peapod.jar -deletePea <myPod>:<myPea>
```

## Artifacts

### Get information on an artifact or artifacts of a pea
```bash
java -jar peapod.jar -getArtifact <myPod>:<myPea>[:<version>]
```

### Upload an artifact of a specific pea
```bash
java -jar peapod.jar -uploadArtifact <myPod>:<myPea>:<version> -filepath <filepath>
```

### Delete an artifact
```bash
java -jar peapod.jar -deleteArtifact <myPod>:<myPea>:<version>
```

### Download an artifact
```bash
java -jar peapod.jar -downloadArtifact <myPod>:<myPea>:<version>
```


## Tags
### Create a tag
```bash
java -jar peapod.jar -createTag <myPod>:<myPea>:<version> -tag <tag>
```

### Delete a tag
Note that each tag gets converted to capital letters
```bash
java -jar peapod.jar -createTag <myPod>:<myPea>:<version> -tag <TAG>
```


## Comments
### Create a comment
```bash
java -jar peapod.jar -createComment <myPod>:<myPea>:<version> -comment <comment>
```

### Delete a comment
In order to delete a comment you need to have the corresponding comment-id
```bash
java -jar peapod.jar -deleteComment <commentID>
```

----------------------------------------------------
# Build project with maven

With the `pom.xml` given in the project, you can easily build the project using:

```bash
mvn clean compile assembly:single
```
