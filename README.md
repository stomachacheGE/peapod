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

## Comment
I am not going to explain to you what a comment is, am I?

## Tag
A tag is a label attached to an artifact. Equivalent to a git tag or a jira label.

## Attribute
A key-value pair that you can attach to an artifact.

----------------------------------------------------------------------

# Command line client for Peapod binary archiving system.

## Requirements
In order to use the peapod client, you will need Python 2.7.10, Pip and Virtualenv.


### Install Python 2.7.10
I am not going to teach you how to install Python, am I?
### Install Pip
Pip is included in python 2.7.10, so just run the following:
```bash
python -m "ensurepip"
```
### Install Virtualenv
```bash
python -m "pip" install virtualenv
```

## How to setup your virtualenv
Go in the peapod-client folder and run the following commands to create your virtualenv and install all required dependencies
```bash
git clone git@source.services.ggs-net.com:peapod/peapod-client.git
cd peapod-client
# create a virtualenv named 'venv'
python -m "virtualenv" venv
# activate the virtualenv
source ./venv/bin/activate
# in windows OS, just run .\venv\Scripts\activate
# install the requirements in the virtualenv
python -m "pip" install -r requirements.txt

# You are ready to go now!
# Type the following to get a useful help message:
python peapod.py
```

## How to configure peapod

The peapod client reads all the configuration it needs from a single file named config.ini.
By default, the file config.ini located in the same folder as the peapod.py file will be used.
It is possible to make peapod use a different config file specifying its path with the option --configfile <configPath>

Here is an example of config.ini file, with the configuration to access the peapod production instance:

```bash
[user]
name=sbruno
token=ABCDNFHXXUTLFUOHIBLWWFPSNTLTWVYNWDPZFUAZTPJSADPPSQWBYNOBHYYLLRFI

[server]
address=api01.peapod.hh-wev.ggs-net.com
port=50000
```

If you need a peapod user and token, please access a link like api01.peapod.hh-wev.ggs-net.com:50000/request_token/<user>, where <user> is your AD account.
You will receive the credentials in your GGS email.

For any other requests (requesting for permissions, creation of a pod, creation of a peapod service user) please contact (what remains of) TI.

----------------------------------------------------------------------

# Example Commands

### Help:
```bash
python peapod.py -h | --help
```

## Pods
### List all pods
```bash
python peapod.py --listpods
```

### Create a pod
```bash
python peapod.py --createpod myPod
```

### Delete a pod
```bash
python peapod.py --deletepod myPod
```


## Peas
### Create a pea in a specific pod
```bash
python peapod.py --createpea myPod:myPea
```


### Delete a pod
```bash
python peapod.py --deletepea myPod:myPea
```

## Artifacts
### Upload an artifact of a specific pea
```bash
python peapod.py --upload myPod:myPea:1.0 --filepath target/myPeaBuild_1.0.jar
```

### Delete an artifact
```bash
python peapod.py --deleteartifact myPod:myPea:1.0
```

### Download an artifact
```bash
python peapod.py --download myPod:myPea:1.0
```


## Tags
### Create a tag
```bash
python peapod.py --createtag released --location myPod:myPea:1.0
```

### Delete a tag
Note that each tag gets converted to capital letters
```bash
python peapod.py --deletetag RELEASED --location myPod:myPea:1.0
```


## Comments
### Create a comment
```bash
python peapod.py --createcomment "It worked on my PC..." --location myPod:myPea:1.0
```

### Delete a comment
In order to delete a comment you need to have the corresponding comment-id
```bash
python peapod.py --deletecomment 6 --location myPod:myPea:1.0
```
