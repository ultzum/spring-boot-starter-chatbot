package com.kingbbode.chatbot.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.Protocol;
import redis.clients.util.Pool;

/**
 * Created by YG on 2017-07-10.
 */
@ConfigurationProperties(prefix = "chatbot")
@Data
public class ChatbotProperties {
    private String basePackage = "";
    private boolean enableBase = true;
    private boolean enableEmoticon = false;
    private boolean enableKnowledge = false;

    private String hostName = "localhost";
    private int port = Protocol.DEFAULT_PORT;
    private int timeout = Protocol.DEFAULT_TIMEOUT;
    private String password;
    private boolean usePool = true;
    private boolean useSsl = false;
    private int dbIndex = 0;
    private String clientName;
    private boolean convertPipelineAndTxResults = true;
}
