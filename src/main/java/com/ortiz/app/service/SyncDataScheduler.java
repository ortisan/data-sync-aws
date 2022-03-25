package com.ortiz.app.service;

import com.ortiz.app.events.DataSync;
import com.ortiz.app.events.SourceDB;
import com.ortiz.app.repository.IPostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SyncDataScheduler {

    private static Logger LOGGER = LoggerFactory.getLogger(SyncDataScheduler.class);

    @Autowired
    private DataSync dataSync;

    @Autowired
    @Qualifier("postDynamoRepository")
    private IPostRepository primaryRepository;

    @Autowired
    @Qualifier("postS3Repository")
    private IPostRepository fallbackRepository;

    @Scheduled(fixedRate = 1000)
    public void consumeDBMessage() {
        LOGGER.info("Syncing data...");

        dataSync.getMessages().stream().forEach(dbMessage -> {
            try {
                if (dbMessage.getSourceDB() == SourceDB.DYNAMODB) {
                    fallbackRepository.save(dbMessage.getPost());
                } else {
                    primaryRepository.save(dbMessage.getPost());
                }
                dataSync.deleteMessage(dbMessage);
            } catch (Exception exc) {
                LOGGER.error("Error to sync data on databases... trying later.", exc);
            }
        });
    }
}
