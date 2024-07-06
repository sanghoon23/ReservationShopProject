package tyml.reservationshop.service.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class UtilRedis {

    private final StringRedisTemplate redisTemplate;

    public ValueOperations<String, String> getValueOperations() {
        return redisTemplate.opsForValue();
    }

    public String getDate(String key) {
        return getValueOperations().get(key);
    }

    public void setData(String key, String value) {
        getValueOperations().set(key, value);
    }

        public void setDataAndTime(String key, String value, long time) {
            getValueOperations().set(key, value);
            redisTemplate.expire(key, Duration.ofSeconds(time));
        }

    public void deleteData(String key) {
        redisTemplate.delete(key);
    }


}
