#!/bin/bash
set -x

# Create dynamodb tables
awslocal dynamodb --endpoint-url=http://localstack:4566 -- create-table \
    --table-name Post \
    --attribute-definitions \
        AttributeName=id,AttributeType=S \
    --key-schema \
        AttributeName=id,KeyType=HASH \
    --provisioned-throughput \
            ReadCapacityUnits=5,WriteCapacityUnits=5

awslocal dynamodb --endpoint-url=http://localstack:4566 -- create-table \
    --table-name Author \
    --attribute-definitions \
        AttributeName=id,AttributeType=S \
    --key-schema \
        AttributeName=id,KeyType=HASH \
    --provisioned-throughput \
            ReadCapacityUnits=5,WriteCapacityUnits=5

awslocal dynamodb --endpoint-url=http://localstack:4566 -- create-table \
    --table-name RelationshipAuthorPost \
    --attribute-definitions \
        AttributeName=postId,AttributeType=S \
        AttributeName=role,AttributeType=S \
        AttributeName=authorId,AttributeType=S \
    --key-schema \
        AttributeName=postId,KeyType=HASH \
        AttributeName=role,KeyType=RANGE \
    --provisioned-throughput \
            ReadCapacityUnits=5,WriteCapacityUnits=5 \
    --local-secondary-indexes \
        "[
            {
                \"IndexName\": \"RelationshipPostAuthorIndex\",
                \"KeySchema\": [{\"AttributeName\":\"postId\",\"KeyType\":\"HASH\"},
                                {\"AttributeName\":\"authorId\",\"KeyType\":\"RANGE\"}],
                \"Projection\":{
                    \"ProjectionType\":\"ALL\"
                }
            }
        ]" \
    --global-secondary-indexes \
        "[
            {
                \"IndexName\": \"RelationshipAuthorPostIndex\",
                \"KeySchema\": [{\"AttributeName\":\"authorId\",\"KeyType\":\"HASH\"},
                                {\"AttributeName\":\"postId\",\"KeyType\":\"RANGE\"}],
                \"Projection\":{
                    \"ProjectionType\":\"ALL\"
                },
                \"ProvisionedThroughput\": {
                    \"ReadCapacityUnits\": 5,
                    \"WriteCapacityUnits\": 5
                }
            }
        ]"

awslocal dynamodb put-item --table-name Author --item "{\"id\":{\"S\":\"2b325354-f11d-4530-8b27-70c63988d095\"}, \"first_name\":{\"S\":\"Marcelo\"}, \"last_name\":{\"S\":\"Santana\"}}" --endpoint-url=http://localstack:4566 --region=sa-east-1
awslocal dynamodb put-item --table-name Author --item "{\"id\":{\"S\":\"b70ac1cf-ff78-45e8-8017-520763065f68\"}, \"first_name\":{\"S\":\"João\"}, \"last_name\":{\"S\":\"Boça\"}}" --endpoint-url=http://localstack:4566 --region=sa-east-1
awslocal dynamodb put-item --table-name Author --item "{\"id\":{\"S\":\"b989e6df-2704-4265-981f-3913babf134e\"}, \"first_name\":{\"S\":\"Gil\"}, \"last_name\":{\"S\":\"Brother\"}}" --endpoint-url=http://localstack:4566 --region=sa-east-1

# Create s3 bucket
awslocal s3 mb s3://posts1245 --endpoint-url=http://localstack:4566 --region=sa-east-1
# Create sns topic
awslocal sns create-topic --name sync-data-topic --endpoint-url=http://localstack:4566 --region=sa-east-1
# Create sqs queue
awslocal sqs create-queue --queue-name sync-data-queue --endpoint-url=http://localstack:4566 --region=sa-east-1
# Subscribe topic with sqs
awslocal sns subscribe --topic-arn arn:aws:sns:sa-east-1:000000000000:sync-data-topic --protocol sqs --notification-endpoint http://localstack:4566/000000000000/sync-data-queue

set +x
