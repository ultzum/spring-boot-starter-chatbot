package com.kingbbode.chatbot.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by YG on 2017-07-10.
 */
@ConfigurationProperties(prefix = "chatbot.teamup")
@Data
public class TeamUpProperties {
    private String id;
    private String password;
    private String clientId;
    private String clientSecret;
    private String testRoom;
    private String testFeed;
}
