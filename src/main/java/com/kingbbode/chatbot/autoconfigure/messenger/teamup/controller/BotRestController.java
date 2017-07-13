package com.kingbbode.chatbot.autoconfigure.messenger.teamup.controller;

import com.kingbbode.chatbot.autoconfigure.common.result.BrainResult;
import com.kingbbode.chatbot.autoconfigure.messenger.teamup.message.MessageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by YG on 2016-09-19.
 */
@RestController
@ConditionalOnProperty(prefix = "chatbot.teamup", name = "enabled", havingValue = "true")
public class BotRestController {
    
    @Autowired
    private MessageService messageService;

    @RequestMapping(value = "/api/send", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity send(@RequestBody Map<String, String> data){
        String key = data.get("key");
        String type = data.get("type");
        String room = data.get("room");
        String message = data.get("message");
        if(StringUtils.isEmpty(key) || StringUtils.isEmpty(type) || StringUtils.isEmpty(room) || StringUtils.isEmpty(message) || !StringUtils.isNumeric(room)){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }else if(!"rnjsdydrmsWkd".equals(key)){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }else{
            if("message".equals(type)){
                messageService.sendMessage(
                        BrainResult.builder()
                            .message(message)
                            .room(room)
                            .build()
                );
            }else if("feed".equals(type)){
                messageService.writeFeed(
                        BrainResult.builder()
                            .message(message)
                            .room(room)
                            .build()
                );
            }else{
                return ResponseEntity.badRequest().build();
            }
        }
        return ResponseEntity.ok().build();
    }
}
