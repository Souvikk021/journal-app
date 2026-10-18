
package net.engineeringdigest.journalApp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RedisService {

    private final RedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisService(
            RedisTemplate redisTemplate,
            ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public <T> T get(String key, Class<T> entityClass) {
        try {
            Object value = redisTemplate.opsForValue().get(key);

            // Handle a cache miss
            if (value == null) {
                log.debug("Cache miss for key: {}", key);
                return null;
            }

            return objectMapper.readValue(
                    value.toString(),
                    entityClass
            );

        } catch (Exception e) {
            log.error("Error getting object from Redis for key: {}", key, e);
            return null;
        }
    }

    public void set(String key, Object value, Long ttl) {
        try {
            String jsonValue = objectMapper.writeValueAsString(value);

            redisTemplate.opsForValue().set(
                    key,
                    jsonValue,
                    ttl,
                    TimeUnit.SECONDS
            );

        } catch (Exception e) {
            log.error("Error setting object in Redis for key: {}", key, e);
        }
    }
}
