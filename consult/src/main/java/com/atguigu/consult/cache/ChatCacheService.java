package com.atguigu.consult.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * Unified Cache：基于 Redis 的 LLM 回答缓存
 *
 * 设计要点：
 * 1. 缓存 Key 为"归一化后用户问题"的 MD5，避免标点/空白差异导致 miss
 * 2. 仅缓存无历史上下文的首次提问（FAQ 场景），多轮对话不缓存，避免上下文污染
 * 3. 命中/未命中计数持久化在 Redis，支持命中率统计接口
 */
@Service
public class ChatCacheService {

    private static final Logger log = LoggerFactory.getLogger(ChatCacheService.class);

    private static final String ANSWER_PREFIX = "cache:chat:answer:";
    private static final String KEY_HITS = "cache:stats:hits";
    private static final String KEY_MISSES = "cache:stats:misses";

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${app.cache.chat-answer-ttl-seconds:1800}")
    private long ttlSeconds;

    @Value("${app.cache.enabled:true}")
    private boolean cacheEnabled;

    /**
     * 生成缓存 Key：问题归一化（去空白、转小写）后取 MD5
     */
    public String buildCacheKey(String message) {
        String normalized = message == null ? "" : message.replaceAll("\\s+", "").toLowerCase();
        return DigestUtils.md5DigestAsHex(normalized.getBytes(StandardCharsets.UTF_8));
    }

    public String getCachedAnswer(String cacheKey) {
        if (!cacheEnabled) {
            return null;
        }
        try {
            return redisTemplate.opsForValue().get(ANSWER_PREFIX + cacheKey);
        } catch (Exception e) {
            log.warn("Redis 读取缓存失败，降级为直接调用模型: {}", e.getMessage());
            return null;
        }
    }

    public void cacheAnswer(String cacheKey, String answer) {
        if (!cacheEnabled || answer == null || answer.isBlank()) {
            return;
        }
        try {
            redisTemplate.opsForValue().set(ANSWER_PREFIX + cacheKey, answer, ttlSeconds, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.warn("Redis 写入缓存失败: {}", e.getMessage());
        }
    }

    public void recordHit() {
        increment(KEY_HITS);
    }

    public void recordMiss() {
        increment(KEY_MISSES);
    }

    private void increment(String key) {
        try {
            redisTemplate.opsForValue().increment(key);
        } catch (Exception e) {
            log.warn("Redis 计数失败: {}", e.getMessage());
        }
    }

    public CacheStats getStats() {
        long hits = getLong(KEY_HITS);
        long misses = getLong(KEY_MISSES);
        double hitRate = (hits + misses) == 0 ? 0.0 : hits / (double) (hits + misses);
        return new CacheStats(hits, misses, hitRate);
    }

    public void resetStats() {
        try {
            redisTemplate.delete(KEY_HITS);
            redisTemplate.delete(KEY_MISSES);
        } catch (Exception e) {
            log.warn("Redis 重置统计失败: {}", e.getMessage());
        }
    }

    private long getLong(String key) {
        try {
            String v = redisTemplate.opsForValue().get(key);
            return v == null ? 0L : Long.parseLong(v);
        } catch (Exception e) {
            return 0L;
        }
    }

    public record CacheStats(long hits, long misses, double hitRate) {
    }
}
