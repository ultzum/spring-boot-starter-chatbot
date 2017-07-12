package com.kingbbode.chatbot.autoconfigure.conversation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kingbbode.chatbot.autoconfigure.common.properties.BotProperties;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;

/**
 * Created by YG on 2017-04-03.
 */
public class ConversationService {
    private int expireTime;

    @Autowired
    Environment environment;

    @Resource(name="redisTemplate")
    private RedisTemplate<String, String> redisTemplate;

    @Resource(name="redisTemplate")
    private ListOperations<String, String> listOperations;


    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BotProperties botProperties;
    private static String PREFIX = "ultron:conversation:";

    @PostConstruct
    private void init(){
        PREFIX = botProperties.isTestMode()?"test:" + PREFIX:PREFIX;
        expireTime = environment.acceptsProfiles("dev") ? 300 : 30;
    }
    
    
    public Conversation pop(String userId) throws IOException {
        String result = listOperations.rightPop(PREFIX+userId);
        if(result != null) {
            redisTemplate.expireAt(PREFIX+userId, new DateTime().plusSeconds(expireTime).toDate());
        }
        return result != null ? objectMapper.readValue(listOperations.rightPop(PREFIX+userId), Conversation.class) : null;
    }
    
    public void push(String userId, Conversation value) throws JsonProcessingException {
        listOperations.rightPush(PREFIX+userId, objectMapper.writeValueAsString(value));
        redisTemplate.expireAt(PREFIX+userId, new DateTime().plusSeconds(expireTime).toDate());
    }
    
    public Conversation getLatest(String userId) throws IOException {
        String result = listOperations.index(PREFIX+userId, listOperations.size(PREFIX+userId)-1);
        if(result != null) {
            redisTemplate.expireAt(PREFIX+userId, new DateTime().plusSeconds(expireTime).toDate());
        }
        return  result != null ? objectMapper.readValue(result, Conversation.class) : null;
    }
    
    public void delete(String userId){
        redisTemplate.opsForValue().getOperations().delete(PREFIX+userId);
    }
}
