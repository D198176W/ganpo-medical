package com.atguigu.consult.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * 敏感信息加密与脱敏工具
 *
 * 加密体系：
 * 1. {@link #encrypt} / {@link #decrypt}：AES-256-GCM（随机 IV），用于无需检索的敏感字段
 * 2. {@link #encryptDeterministic}：确定性加密（同明文同密文），用于需要等值查询的字段（如身份证号），
 *    工程取舍：以部分机密性换取可检索性，为可检索加密的业界常见折中方案
 * 3. 脱敏方法：日志与接口返回时对手机号/身份证号打码
 *
 * 密钥通过环境变量 ENCRYPT_KEY 注入，禁止硬编码进仓库。
 */
@Component
public class SensitiveDataEncryptor {

    private static final String GCM_TRANSFORMATION = "AES/GCM/NoPadding";
    private static final String ECB_TRANSFORMATION = "AES/ECB/PKCS5Padding";
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 128;

    private final SecretKeySpec keySpec;
    private final SecureRandom secureRandom = new SecureRandom();

    public SensitiveDataEncryptor(@Value("${app.encrypt.key}") String encryptKey) {
        this.keySpec = new SecretKeySpec(sha256(encryptKey), "AES");
    }

    /** AES-256-GCM 加密（随机 IV），输出 Base64(IV + 密文) */
    public String encrypt(String plaintext) {
        if (plaintext == null || plaintext.isEmpty()) {
            return plaintext;
        }
        try {
            byte[] iv = new byte[GCM_IV_LENGTH];
            secureRandom.nextBytes(iv);
            Cipher cipher = Cipher.getInstance(GCM_TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, new GCMParameterSpec(GCM_TAG_LENGTH, iv));
            byte[] ciphertext = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
            byte[] combined = new byte[iv.length + ciphertext.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(ciphertext, 0, combined, iv.length, ciphertext.length);
            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            throw new IllegalStateException("敏感数据加密失败", e);
        }
    }

    /** AES-256-GCM 解密 */
    public String decrypt(String encoded) {
        if (encoded == null || encoded.isEmpty()) {
            return encoded;
        }
        try {
            byte[] combined = Base64.getDecoder().decode(encoded);
            byte[] iv = new byte[GCM_IV_LENGTH];
            byte[] ciphertext = new byte[combined.length - GCM_IV_LENGTH];
            System.arraycopy(combined, 0, iv, 0, GCM_IV_LENGTH);
            System.arraycopy(combined, GCM_IV_LENGTH, ciphertext, 0, ciphertext.length);
            Cipher cipher = Cipher.getInstance(GCM_TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, new GCMParameterSpec(GCM_TAG_LENGTH, iv));
            return new String(cipher.doFinal(ciphertext), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException("敏感数据解密失败", e);
        }
    }

    /** 确定性加密：同一明文始终产出同一密文，支持数据库等值查询 */
    public String encryptDeterministic(String plaintext) {
        if (plaintext == null || plaintext.isEmpty()) {
            return plaintext;
        }
        try {
            Cipher cipher = Cipher.getInstance(ECB_TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new IllegalStateException("敏感数据加密失败", e);
        }
    }

    /** 确定性解密 */
    public String decryptDeterministic(String encoded) {
        if (encoded == null || encoded.isEmpty()) {
            return encoded;
        }
        try {
            Cipher cipher = Cipher.getInstance(ECB_TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            return new String(cipher.doFinal(Base64.getDecoder().decode(encoded)), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException("敏感数据解密失败", e);
        }
    }

    /** 手机号脱敏：138****1234 */
    public String maskPhone(String phone) {
        if (phone == null || phone.length() < 7) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4);
    }

    /** 身份证号脱敏：360101********1234 */
    public String maskIdCard(String idCard) {
        if (idCard == null || idCard.length() < 10) {
            return idCard;
        }
        return idCard.substring(0, 6) + "********" + idCard.substring(idCard.length() - 4);
    }

    private static byte[] sha256(String key) {
        try {
            return MessageDigest.getInstance("SHA-256").digest(key.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new IllegalStateException("密钥派生失败", e);
        }
    }
}
