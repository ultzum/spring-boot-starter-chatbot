package com.kingbbode.chatbot.autoconfigure.base.stat;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by YG on 2017-06-02.
 */
@Component
public class StatComponent {
    private Map<String, Integer> statMap;
    
    @PostConstruct
    public void init(){
        statMap = new HashMap<>();
    }
    
    public void plus(String key) {
        int current = 0;
        if(statMap.containsKey(key)){
            current = statMap.get(key);
        }
        statMap.put(key, current + 1);
    }
    
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        statMap.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEach(entry-> stringBuilder.append(entry.getKey()).append(":").append(entry.getValue()).append("\n"));
        return stringBuilder.toString();
    }
}
