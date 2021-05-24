package com.expiration.redisexpirationpoc.mongo.controller;

import com.expiration.redisexpirationpoc.mongo.model.QrCodeDocument;
import com.expiration.redisexpirationpoc.mongo.service.QrCodeDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/qrcodedocument")
public class QrCodeDocumentController {

    @Autowired
    private QrCodeDocumentService qrCodeDocumentService;

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody QrCodeDocument qrCodeDocument){
        qrCodeDocumentService.saveQrCode(qrCodeDocument);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
