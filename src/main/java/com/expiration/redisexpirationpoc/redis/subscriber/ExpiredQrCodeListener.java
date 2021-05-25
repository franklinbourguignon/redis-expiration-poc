package com.expiration.redisexpirationpoc.redis.subscriber;

import com.expiration.redisexpirationpoc.mongo.service.QrCodeDocumentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ExpiredQrCodeListener implements MessageListener {

    @Autowired
    private QrCodeDocumentService qrCodeDocumentService;

    @Override
    public void onMessage(Message message, byte[] bytes) {
        String key = new String(message.getBody());
        log.info("QRCODE EXPIRED: [{}]", key);
        if(!key.contains("finished")) {
            qrCodeDocumentService.updateQRCodeStatus(key);
        }
    }
}
