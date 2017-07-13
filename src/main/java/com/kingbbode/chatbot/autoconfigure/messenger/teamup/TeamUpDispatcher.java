package com.kingbbode.chatbot.autoconfigure.messenger.teamup;

import com.kingbbode.chatbot.autoconfigure.base.emoticon.component.EmoticonComponent;
import com.kingbbode.chatbot.autoconfigure.brain.DispatcherBrain;
import com.kingbbode.chatbot.autoconfigure.common.enums.BrainResponseType;
import com.kingbbode.chatbot.autoconfigure.common.interfaces.Dispatcher;
import com.kingbbode.chatbot.autoconfigure.common.request.BrainRequest;
import com.kingbbode.chatbot.autoconfigure.common.result.BrainResult;
import com.kingbbode.chatbot.autoconfigure.conversation.Conversation;
import com.kingbbode.chatbot.autoconfigure.conversation.ConversationService;
import com.kingbbode.chatbot.autoconfigure.messenger.teamup.message.MessageService;
import com.kingbbode.chatbot.autoconfigure.messenger.teamup.request.FileRequest;
import com.kingbbode.chatbot.autoconfigure.messenger.teamup.response.EventResponse;
import com.kingbbode.chatbot.autoconfigure.messenger.teamup.response.FileUploadResponse;
import com.kingbbode.chatbot.autoconfigure.messenger.teamup.response.MessageResponse;
import com.kingbbode.chatbot.autoconfigure.messenger.teamup.util.ImagesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * Created by YG on 2017-07-10.
 */
public class TeamUpDispatcher implements Dispatcher<EventResponse.Event> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private static final String EVENT_MESSAGE = "chat.message";
    private static final String EVENT_JOIN = "chat.join";
    private static final String ULTRON_USER_ID = "10849";
    private static final int MESSAGE_TYPE = 1;
    private static final int FILE_TYPE = 2;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private EmoticonComponent emoticonComponent;
    
    @Autowired
    private DispatcherBrain dispatcherBrain;

    @Override
    public void dispatch(EventResponse.Event event) {
        if (EVENT_MESSAGE.equals(event.getType())) {
            if (!event.getChat().getUser().equals(ULTRON_USER_ID)) {
                classification(event);
            }
        } else if (EVENT_JOIN.equals(event.getType())) {
            send(BrainResult.Builder.GREETING.room( event.getChat().getRoom()).build());
        }
    }

    private void classification(EventResponse.Event event) {
        EventResponse.Event.Chat chat = event.getChat();
        MessageResponse.Message message = messageService.readMessage(event.getChat());
        BrainRequest brainRequest = BrainRequest.builder()
                                    .user(String.valueOf(message.getUser()))
                                    .room(chat.getRoom())
                                    .content(message.getContent()!=null?message.getContent().trim():null)
                                    .messageNo(String.valueOf(message.getMsg()))
                                    .team(chat.getTeam())
                                    .build();
        switch (message.getType()) {
            case MESSAGE_TYPE:
                if (!brainRequest.isValid()) {
                    break;
                }
                send(dispatcherBrain.execute(brainRequest));
                break;
            case FILE_TYPE:
                MessageResponse.File file = message.getFile();
                if (!ImagesUtils.isImage(file)) {
                    break;
                }
                try {
                    Conversation conversation = conversationService.getLatest(String.valueOf(message.getUser()));
                    if(isEmoticonAction(conversation)){
                        String[] fileName = file.getName().toLowerCase().split("\\.");
                        FileUploadResponse response =messageService.writeImage(new FileRequest.Builder()
                                .request(brainRequest)
                                .id(file.getId())
                                .name(message.getUser() + fileName[0])
                                .type(ImagesUtils.selectMediaTypeForImage(fileName[1]))
                                .build()
                        );
                        if(response == null || response.getFiles() == null || response.getFiles().size()<1){
                            conversationService.delete(String.valueOf(message.getUser()));
                            send(BrainResult.builder()
                                    .message("문제가 있네요.. 다음에 이용해주세요..ㅜㅜ")
                                    .room(event.getChat().getRoom())
                                    .type(BrainResponseType.MESSAGE)
                                    .build());
                            break;
                        }
                        emoticonComponent.put(conversation.getParam().get("name"), response.getFiles().get(0).getId());
                        logger.info("emoticon reg - user : {}, emoticon: {}", message.getUser(), conversation.getParam().get("name"));
                        conversationService.delete(String.valueOf(message.getUser()));
                        send(BrainResult.builder()
                                .message(conversation.getParam().get("name") + " 이모티콘이 등록되었습니다.")
                                .room(event.getChat().getRoom())
                                .type(BrainResponseType.MESSAGE)
                                .build());
                        break;
                    }
                    break;
                } catch (IOException e) {
                    logger.error("emoticon reg exception - user : {}", message.getUser());
                    break;
                }
        }
    }

    private boolean isEmoticonAction(Conversation conversation) {
        return conversation != null && conversation.getFunction().equals("emoticonReg1") && conversation.getParam().containsKey("name");
    }

    private void send(BrainResult result) {
        if(result!=null) {
            switch (result.type()) {
                case MESSAGE:
                    messageService.sendMessage(result);
                    break;
                case FEED:
                    messageService.writeFeed(result);
                    break;
                case EMOTICON:
                    messageService.sendEmoticon(result);
                    break;
                case OUT:
                    messageService.outRoom(result);
                    break;
            }
        }
    }
}
