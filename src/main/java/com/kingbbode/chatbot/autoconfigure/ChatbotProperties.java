package com.kingbbode.chatbot.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

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
}
