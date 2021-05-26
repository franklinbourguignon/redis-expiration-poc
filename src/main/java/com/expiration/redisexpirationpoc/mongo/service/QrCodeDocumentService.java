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
import static com.expiration.redisexpirationpoc.redis.utils.KeysUtils.*;

@Slf4j
@Service
public class QrCodeDocumentService {

    @Autowired
    private QRCodeDocumentRepository qrCodeDocumentRepository;

    @Autowired
    private RedisService redisService;

    public void saveQrCode(QrCodeDocument qrCodeDocument) {
        final String key = qrCodeDocument.getId();
        qrCodeDocument.setStatus(PENDING);
        qrCodeDocument.setUpdatedAt(LocalDateTime.now());

        qrCodeDocumentRepository.save(qrCodeDocument);

        createPendingKey(key,10);

        log.info("PERSIST QRCODE [{}] IN MONGODB", key);
    }

    public void updateQRCodeStatus(String key) {

        final String expiredKey = generateExpiredKey(key);

        if(!isAlreadyExpired(expiredKey)){
            Optional<QrCodeDocument> qrCode = qrCodeDocumentRepository.findByIdAndStatus(key, PENDING);
            try {
                qrCode.ifPresentOrElse((q) -> {

                    q.setStatus(EXPIRED);
                    q.setUpdatedAt(LocalDateTime.now());

                    qrCodeDocumentRepository.save(q);

                    createExpiredKey(expiredKey);

                    log.info("QRCODE [{}] EXPIRED WITH SUCESS IN MONGODB", key);
                }, () ->
                        log.info("QRCODE [{}] IS ALREADY EXPIRED IN MONGODB", key));
            } catch (Exception e){
                log.info("AN ERROR HAS OCCURRED WHEN UPDATE STATUS [{}] ", key);
            }
        } else {
            log.info("QRCODE [{}] IS ALREADY EXPIRED IN REDIS", key);
        }

    }

    private void createPendingKey(String key, Integer timeToLive){
        redisService.createKeyWithExpirationTime(generatePendingKey(key), "", timeToLive);
    }

    private void createExpiredKey(String keyWithPrefix){
        redisService.createKeyWithExpirationTime(keyWithPrefix, "", ONE_MINUTE_TTL);
    }

    private boolean isAlreadyExpired(String keyWithPrefix){
        return redisService.keyExists(keyWithPrefix);
    }

}
