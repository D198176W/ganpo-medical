package com.atguigu.consult.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 敏感信息加密/脱敏单元测试
 */
class SensitiveDataEncryptorTest {

    private final SensitiveDataEncryptor encryptor = new SensitiveDataEncryptor("test-key-32-bytes-for-unit-test!!");

    @Test
    void gcmEncryptDecryptRoundTrip() {
        String plaintext = "360101199901011234";
        String encrypted = encryptor.encrypt(plaintext);
        assertNotEquals(plaintext, encrypted);
        assertEquals(plaintext, encryptor.decrypt(encrypted));
    }

    @Test
    void gcmUsesRandomIv() {
        String plaintext = "13800001111";
        // 同一明文两次加密结果不同（随机 IV）
        assertNotEquals(encryptor.encrypt(plaintext), encryptor.encrypt(plaintext));
    }

    @Test
    void deterministicEncryptIsSearchable() {
        String plaintext = "360101199901011234";
        String e1 = encryptor.encryptDeterministic(plaintext);
        String e2 = encryptor.encryptDeterministic(plaintext);
        // 确定性加密：同明文同密文，支持数据库等值查询
        assertEquals(e1, e2);
        assertEquals(plaintext, encryptor.decryptDeterministic(e1));
    }

    @Test
    void nullAndEmptyPassThrough() {
        assertNull(encryptor.encrypt(null));
        assertEquals("", encryptor.encrypt(""));
        assertNull(encryptor.encryptDeterministic(null));
    }

    @Test
    void maskPhone() {
        assertEquals("138****1234", encryptor.maskPhone("13812341234"));
    }

    @Test
    void maskIdCard() {
        assertEquals("360101********1234", encryptor.maskIdCard("360101199901011234"));
    }
}
