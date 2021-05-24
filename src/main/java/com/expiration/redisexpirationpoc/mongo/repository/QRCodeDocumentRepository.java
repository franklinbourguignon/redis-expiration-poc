package com.expiration.redisexpirationpoc.mongo.repository;

import com.expiration.redisexpirationpoc.mongo.model.QrCodeDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface QRCodeDocumentRepository extends MongoRepository<QrCodeDocument, String> {
        Optional<QrCodeDocument> findByIdAndStatus(String id, String status);
}