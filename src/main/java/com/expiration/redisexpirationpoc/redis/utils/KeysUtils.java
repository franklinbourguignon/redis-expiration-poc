package com.expiration.redisexpirationpoc.redis.utils;

public class KeysUtils {

    private KeysUtils(){}

    private static final String PENDING= "pending";
    private static final String EXPIRED = "expired";
    private static final String PENDING_PREFIX = PENDING.concat(":%s");
    private static final String EXPIRED_PREFIX = EXPIRED.concat(":%s");
    public static final Integer ONE_MINUTE_TTL = 60;

    public static String clearKeyPrefix(String key){
        return key.replace(PENDING,"")
                .replace(EXPIRED,"")
                .replace(":","");
    }

    public static String generatePendingKey(String key){
        return String.format(PENDING_PREFIX, key);
    }

    public static String generateExpiredKey(String key){
        return String.format(EXPIRED_PREFIX, key);
    }

    public static boolean isPendingKey(String keyWithPrefix){
        return keyWithPrefix.contains(PENDING);
    }
}
