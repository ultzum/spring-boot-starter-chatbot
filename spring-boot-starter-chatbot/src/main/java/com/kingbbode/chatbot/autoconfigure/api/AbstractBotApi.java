package com.kingbbode.chatbot.autoconfigure.api;


import com.kingbbode.chatbot.autoconfigure.common.request.BrainRequest;
import com.kingbbode.chatbot.autoconfigure.common.result.BrainResult;

/**
 * Created by YG on 2017-01-26.
 */
public abstract class AbstractBotApi<T extends ApiData> implements BotApi<T> {
    
    public BrainResult execute(BrainRequest brainRequest) {
        T api = getRequest(brainRequest.getContent());

        return new BrainResult.Builder()
                .room(brainRequest.getRoom())
                .result(get(api))
                .type(api.response())
                .build();
    }
}
