package com.expiration.redisexpirationpoc.mongo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Document(collection = "qrcode")
public class QrCodeDocument implements Serializable {

    @Id
    private String id;
    private String emv;
    private String status;
    private LocalDateTime updatedAt;
    @Version
    Long version;

}
