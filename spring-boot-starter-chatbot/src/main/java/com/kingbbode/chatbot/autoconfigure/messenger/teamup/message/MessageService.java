package com.kingbbode.chatbot.autoconfigure.messenger.teamup.message;

import com.kingbbode.chatbot.autoconfigure.common.result.BrainResult;
import com.kingbbode.chatbot.autoconfigure.messenger.teamup.request.FileRequest;
import com.kingbbode.chatbot.autoconfigure.messenger.teamup.response.EventResponse;
import com.kingbbode.chatbot.autoconfigure.messenger.teamup.response.FileUploadResponse;
import com.kingbbode.chatbot.autoconfigure.messenger.teamup.response.MessageResponse;
import com.kingbbode.chatbot.autoconfigure.messenger.teamup.templates.template.EdgeTemplate;
import com.kingbbode.chatbot.autoconfigure.messenger.teamup.templates.template.FileTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

/**
 * Created by YG on 2016-03-28.
 */
public class MessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);
    
    @Autowired
    private EdgeTemplate edgeTemplate;
    
    @Autowired
    private FileTemplate fileTemplate;

    /*public String excuteMessageForChat(String content, String command) {
        return getMessageResult("999999999999", "8170", content, command);
    }*/

    public MessageResponse.Message readMessage(EventResponse.Event.Chat chat) {
        MessageResponse readResponse = edgeTemplate.readMessage(chat.getMsg(), chat.getRoom());
        if (!ObjectUtils.isEmpty(readResponse) && readResponse.getMsgs().size() > 0) {
            return  readResponse.getMsgs().get(0);
        }
        return null;
    }

    public void sendMessage(BrainResult result) {
        edgeTemplate.sendMessage(result.getMessage(), result.getRoom());
    }

    public void sendEmoticon(BrainResult result) {
        edgeTemplate.sendEmoticon(result.getMessage(), result.getRoom());
    }

    public void writeFeed(BrainResult result) {
        edgeTemplate.writeFeed(result.getMessage(), result.getRoom());
    }
    
    public FileUploadResponse writeImage(FileRequest fileRequest) {
        byte[] bytes = fileTemplate.download(fileRequest);
        if(bytes == null){
            return null;
        }
        return fileTemplate.upload(fileRequest, bytes);
    }
    
    public void outRoom(BrainResult result) {
        edgeTemplate.sendMessage(result.getMessage(), result.getRoom());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            logger.warn("InterruptedException");
        }
        edgeTemplate.outRoom(result.getRoom());
    }
}
