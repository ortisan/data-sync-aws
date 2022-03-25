package com.ortiz.app.events;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ortiz.app.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DataSyncSNS {

    @Autowired
    private AmazonSNS snsClient;

    @Value("${sns.data-sync-topic-arn}")
    private String dataSyncTopicArn;

    @Autowired
    private ObjectMapper objectMapper;

    public String publishMessage(DBMessage dbMessage) {
        try {
            PublishRequest request = new PublishRequest(dataSyncTopicArn, objectMapper.writeValueAsString(dbMessage));
            PublishResult publish = snsClient.publish(request);
            return publish.getMessageId();
        } catch (Exception exc) {
            throw new CustomException("Error to serialize object.", exc);
        }
    }

}
