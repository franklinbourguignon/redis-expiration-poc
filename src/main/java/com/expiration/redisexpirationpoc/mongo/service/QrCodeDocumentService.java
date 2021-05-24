package com.expiration.redisexpirationpoc.mongo.service;


import com.expiration.redisexpirationpoc.mongo.model.QrCodeDocument;
import com.expiration.redisexpirationpoc.mongo.repository.QRCodeDocumentRepository;
import com.expiration.redisexpirationpoc.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.expiration.redisexpirationpoc.mongo.model.Status.EXPIRED;
import static com.expiration.redisexpirationpoc.mongo.model.Status.PENDING;

@Slf4j
@Service
public class QrCodeDocumentService {

    @Autowired
    private QRCodeDocumentRepository qrCodeDocumentRepository;

    @Autowired
    private RedisService redisService;

    public void saveQrCode(QrCodeDocument qrCodeDocument) {
        final String key = qrCodeDocument.getId();

        qrCodeDocument.setStatus(PENDING.name());
        qrCodeDocument.setUpdatedAt(LocalDateTime.now());

        persistQRCode(qrCodeDocument);

        redisService.createKeyWithExpirationTime(key, "", 10);

        log.info("PERSIST QRCODE [{}] IN MONGODB", key);
    }

    public void updateQRCodeStatus(String id) {
        Optional<QrCodeDocument> qrCode = qrCodeDocumentRepository.findByIdAndStatus(id, PENDING.name());

        qrCode.ifPresentOrElse((q) -> {
            q.setStatus(EXPIRED.name());
            q.setUpdatedAt(LocalDateTime.now());
            qrCodeDocumentRepository.save(q);
            log.info("QRCODE [{}] EXPIRED WITH SUCESS IN MONGODB", id);
        }, () ->
                log.info("QRCODE [{}] IS ALREADY EXPIRED IN MONGODB", id));

    }

    private void persistQRCode(QrCodeDocument qrCodeDocument) {
        qrCodeDocumentRepository.save(qrCodeDocument);
    }

}
