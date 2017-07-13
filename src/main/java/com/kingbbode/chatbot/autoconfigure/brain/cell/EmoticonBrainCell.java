package com.kingbbode.chatbot.autoconfigure.brain.cell;

import com.kingbbode.chatbot.autoconfigure.base.emoticon.component.EmoticonComponent;
import com.kingbbode.chatbot.autoconfigure.common.enums.BrainResponseType;
import com.kingbbode.chatbot.autoconfigure.common.request.BrainRequest;
import com.kingbbode.chatbot.autoconfigure.common.result.BrainResult;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by YG on 2017-01-26.
 */
public class EmoticonBrainCell extends AbstractBrainCell{
    
    private EmoticonComponent emoticonComponent;
/*
    public EmoticonBrainCell(String emoticon) {
        this.builder = BrainResult.builder()
                .message(emoticon);
    }*/

    public EmoticonBrainCell(EmoticonComponent emoticonComponent) {
        this.emoticonComponent = emoticonComponent;
    }

    @Override
    public BrainResult execute(BrainRequest brainRequest) throws InvocationTargetException, IllegalAccessException {
        String result = emoticonComponent.get(brainRequest.getContent());
        if(result == null){
            return BrainResult.NONE;
        }
        return BrainResult.builder()
                .message(result)
                .room(brainRequest.getRoom())
                .type(BrainResponseType.EMOTICON)
                .build();


    }
}
