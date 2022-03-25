package com.ortiz.app.repository;

import com.ortiz.app.CustomException;
import com.ortiz.app.domains.Post;
import com.ortiz.app.events.DBMessage;
import com.ortiz.app.events.DataSync;
import com.ortiz.app.events.DataSyncSNS;
import com.ortiz.app.events.SourceDB;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.Random;
import java.util.UUID;


@Repository("postFacadeRepository")
public class PostFacadeRepository implements IPostRepository {

    private static Logger LOGGER = LoggerFactory.getLogger(PostFacadeRepository.class);

    @Autowired
    private DataSyncSNS dataSyncSNS;
    @Autowired
    @Qualifier("postDynamoRepository")
    private IPostRepository primaryRepository;
    @Autowired
    @Qualifier("postS3Repository")
    private IPostRepository fallbackRepository;

    private static final int ERROR_THRESHOLD = 80;

    @CircuitBreaker(name = "repository", fallbackMethod = "fallbackSave")
    @Override
    public Post save(Post post) {
        LOGGER.info("Primary post...");
        // Just for simulate error
        checkIfThrowError();
        Post postPersisted = primaryRepository.save(post);

        DBMessage dbMessage = DBMessage.builder().post(post).timestamp(ZonedDateTime.now()).sourceDB(SourceDB.DYNAMODB).build();
        dataSyncSNS.publishMessage(dbMessage);

        return postPersisted;
    }

    public Post fallbackSave(Post post, Throwable exc) {
        LOGGER.info("Fallback post...");

        Post postPersisted = fallbackRepository.save(post);

        DBMessage dbMessage = DBMessage.builder().post(post).timestamp(ZonedDateTime.now()).sourceDB(SourceDB.S3).build();
        dataSyncSNS.publishMessage(dbMessage);

        return postPersisted;
    }

    @CircuitBreaker(name = "repository", fallbackMethod = "fallbackGetById")
    @Override
    public Post getById(UUID id) {
        LOGGER.info("Primary getById...");

        // Just for simulate error
        checkIfThrowError();
        return primaryRepository.getById(id);
    }

    public Post fallbackGetById(UUID id, Throwable exc) {
        LOGGER.info("Fallback getById...");

        return fallbackRepository.getById(id);
    }

    private void checkIfThrowError() {
        Random rand = new Random(); //instance of random class
        int randomNumber = rand.nextInt(100);
        boolean flagError = randomNumber < ERROR_THRESHOLD;
        if (flagError) {
            throw new CustomException("Simulated error...");
        }
    }

}
