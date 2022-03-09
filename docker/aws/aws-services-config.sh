#!/bin/bash
set -x
# Create s3 bucket
awslocal s3 mb s3://posts1245 --endpoint-url=http://localstack:4566 --region=sa-east-1
set +x
