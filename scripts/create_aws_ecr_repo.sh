#!/bin/bash

# one time deal
aws ecr create-repository --repository-name my-ecr-repo --region us-east-1

#xsizxenjins:services-kotlin-spring-boot-test cevherdogan$ aws ecr create-repository --repository-name my-ecr-repo --region us-east-1
#{
#    "repository": {
#        "repositoryArn": "arn:aws:ecr:us-east-1:978131921537:repository/my-ecr-repo",
#        "registryId": "978131921537",
#        "repositoryName": "my-ecr-repo",
#        "repositoryUri": "978131921537.dkr.ecr.us-east-1.amazonaws.com/my-ecr-repo",
#        "createdAt": 1574219076.0,
#        "imageTagMutability": "MUTABLE"
#    }
#}
