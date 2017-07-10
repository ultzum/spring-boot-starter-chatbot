package com.kingbbode.chatbot.autoconfigure.messenger.teamup;

import com.kingbbode.chatbot.autoconfigure.messenger.teamup.oauth2.OAuth2Token;
import com.kingbbode.chatbot.autoconfigure.messenger.teamup.templates.template.Oauth2Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by YG on 2016-05-11.
 */
public class TeamUpTokenManager {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    TeamUpEventSensor teamUpEventSensor;

    private OAuth2Token token;
    
    @Autowired
    Oauth2Template oauth2Template;
    
    @PostConstruct
    void init(){
        token = oauth2Template.token(token);
        if(token !=null && token.getAccessToken()!= null && !"".equals(token.getAccessToken())) {
            teamUpEventSensor.setReady(true);
        }else{
            logger.error("Authentication Failed");
            System.exit(0);
        }
    }

    public String getAccessToken() {
        token = oauth2Template.token(token);
        return token.getAccessToken();
    }
}
