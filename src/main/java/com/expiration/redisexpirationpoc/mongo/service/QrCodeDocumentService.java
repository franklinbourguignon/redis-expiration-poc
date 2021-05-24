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

    private static final String MONGO_PREFIX_KEY = "mongo:%s";

    @Autowired
    private QRCodeDocumentRepository qrCodeDocumentRepository;

    @Autowired
    private RedisService redisService;

    public void saveQrCode(QrCodeDocument qrCodeDocument) {
        final String key = qrCodeDocument.getId();

        qrCodeDocument.setStatus(PENDING.name());
        qrCodeDocument.setUpdatedAt(LocalDateTime.now());

        persistQRCode(qrCodeDocument);

        redisService.createKeyWithExpirationTime(String.format(MONGO_PREFIX_KEY,key), "", 10);

        log.info("PERSIST QRCODE [{}] IN MONGODB", key);
    }

    public void updateQRCodeStatus(String id) {
        String idWithoutPrefix = id.replace("mongo:","");
        Optional<QrCodeDocument> qrCode = qrCodeDocumentRepository.findByIdAndStatus(idWithoutPrefix, PENDING.name());

        try {
            qrCode.ifPresentOrElse((q) -> {
                q.setStatus(EXPIRED.name());
                q.setUpdatedAt(LocalDateTime.now());
                qrCodeDocumentRepository.save(q);
                log.info("QRCODE [{}] EXPIRED WITH SUCESS IN MONGODB", id);
            }, () ->
                    log.info("QRCODE [{}] IS ALREADY EXPIRED IN MONGODB", id));
        }catch (Exception e){
            log.info("QRCODE [{}] IS ALREADY EXPIRED IN MONGODB", id);
        }
    }

    private void persistQRCode(QrCodeDocument qrCodeDocument) {
        qrCodeDocumentRepository.save(qrCodeDocument);
    }

}
