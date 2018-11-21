package com.yoogurt.taxi.licences.common.redis;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 这是一个坑：http://blog.csdn.net/u014481096/article/details/54134904
 */
@Configuration
public class RedisConfig {

    /**
     * 基于Hessian技术的序列化器，可读性不强，但是通用，
     * 且性能较好，适合做数据传输。
     *
     * @return HessianRedisSerializer
     */
    @Bean(name = "hessianRedisSerializer")
    public RedisSerializer getHessianSerializer() {
        return new HessianRedisSerializer<>();
    }


    /**
     * 依赖taxi-common的module如有需要，可以对RedisTemplate进行相关定制，默认使用如下RedisTemplate
     */
    @Bean
    @ConditionalOnMissingBean(RedisTemplate.class)
    public RedisTemplate<String, Object> getRedisTemplate(RedisConnectionFactory redisConnectionFactory) {

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericToStringSerializer<>(Object.class));
        template.setHashKeySerializer(new StringRedisSerializer());
        //使用hessian序列化较为通用
        template.setHashValueSerializer(getHessianSerializer());
        template.afterPropertiesSet();
        return template;
    }
}
