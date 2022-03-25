package com.ortiz.app.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ortiz.app.domains.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DBMessage {
    @JsonProperty("message_id")
    private String messageId;
    @JsonProperty("receipt_handle")
    private String receiptHandle;
    @JsonProperty("post")
    private Post post;
    @JsonProperty("source_db")
    private SourceDB sourceDB;
    @JsonProperty("timestamp")
    private ZonedDateTime timestamp;
}
