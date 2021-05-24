package com.expiration.redisexpirationpoc.mongo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Status {

    PENDING("Pending"),
    EXPIRED("Expired");

    @Getter  private String value;
}
