package com.kingbbode.chatbot.autoconfigure.base.emoticon.component;

import com.kingbbode.chatbot.autoconfigure.base.stat.StatComponent;
import com.kingbbode.chatbot.autoconfigure.common.properties.BotProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by YG on 2016-11-01.
 */
public class EmoticonComponent {

    @Resource(name="redisTemplate")
    private HashOperations<String, String, String> hashOperations;
    
    @Autowired
    private StatComponent statComponent;
    
    @Autowired
    private BotProperties botProperties;

    private static final String REDIS_KEY_EMOTICON = ":emoticon";
    
    private String key;
    private Map<String, String> emoticons;

    @PostConstruct
    public void init() {
        this.key = botProperties.getName() + REDIS_KEY_EMOTICON;
        Map<String, String> map = new ConcurrentHashMap<>();
        Map<String, String> entries = hashOperations.entries(this.key);
        for(Map.Entry<String, String> entry : entries.entrySet()){
            map.put(entry.getKey(), entry.getValue());
        }
        emoticons = map;
    }
    
    public String get(String content){
        String candi = emoticons.getOrDefault(content, null);
        if(candi != null){
            statComponent.plus("이모티콘:" + content);
        }
        return candi;
    }
    
    public Map<String, String> getEmoticons(){
        return emoticons;
    }
    
    public void put(String key, String value){
        hashOperations.put(this.key, botProperties.getEmoticonPrefix() + key, value);
        emoticons.put(botProperties.getEmoticonPrefix() + key, value);
    }
    
    public boolean contains(String key){
        return emoticons.containsKey(botProperties.getEmoticonPrefix() + key);
    }

    public void remove(String key) {
        hashOperations.delete(this.key, botProperties.getEmoticonPrefix() + key);
        emoticons.remove(botProperties.getEmoticonPrefix() + key);
    }
}
