package com.ortiz.app.events;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ortiz.app.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataSync {

    @Value("${sqs.data-sync-queue-endpoint}")
    private String dataSyncQueueEndpoint;

    @Autowired
    private AmazonSQS sqsClient;

    @Autowired
    private ObjectMapper objectMapper;

    public List<DBMessage> getMessages() {
        try {
            List<Message> messages = sqsClient.receiveMessage(dataSyncQueueEndpoint).getMessages();
            List<DBMessage> dbMessages = messages.stream().map(message -> {
                try {
                    String messageId = message.getMessageId();
                    String receiptHandle = message.getReceiptHandle();
                    DBMessage dbMessage = objectMapper.readValue(message.getBody(), DataSnsMessage.class).getMessage();
                    dbMessage.setMessageId(messageId);
                    dbMessage.setReceiptHandle(receiptHandle);
                    return dbMessage;
                } catch (Exception exc) {
                    throw new CustomException("Error to deserialize message.", exc);
                }
            }).collect(Collectors.toList());
            return dbMessages;
        } catch (Exception exc) {
            throw new CustomException("Error to send message to SQS", exc);
        }
    }

    public void publishMessage(DBMessage message) {
        try {
            SendMessageRequest sqsMessage = new SendMessageRequest()
                    .withQueueUrl(dataSyncQueueEndpoint)
                    .withMessageBody(objectMapper.writeValueAsString(message))
                    .withDelaySeconds(0);
            sqsClient.sendMessage(sqsMessage);
        } catch (Exception exc) {
            throw new CustomException("Error to send message to SQS", exc);
        }
    }

    public void deleteMessage(DBMessage message) {
        try {
            DeleteMessageRequest deleteMessageRequest = new DeleteMessageRequest()
                    .withQueueUrl(dataSyncQueueEndpoint)
                    .withReceiptHandle(message.getReceiptHandle());
            sqsClient.deleteMessage(deleteMessageRequest);
        } catch (Exception exc) {
            throw new CustomException("Error to delete message from SQS", exc);
        }
    }
}
