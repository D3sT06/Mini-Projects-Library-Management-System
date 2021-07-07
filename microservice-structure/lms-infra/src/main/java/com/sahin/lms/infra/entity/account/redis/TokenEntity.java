package com.sahin.lms.infra.entity.account.redis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash(value = "tokens")
@Getter
@Setter
public class TokenEntity {

    @Id
    private String cardBarcode;
    private String token;

    @TimeToLive
    private Long ttl;
}
