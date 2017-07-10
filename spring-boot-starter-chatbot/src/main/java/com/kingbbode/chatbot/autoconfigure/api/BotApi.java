package com.kingbbode.chatbot.autoconfigure.api;


import com.kingbbode.chatbot.autoconfigure.common.result.BrainCellResult;

import java.util.Collection;

/**
 * Created by YG on 2017-01-26.
 */
public interface BotApi<T> {
    void update();

    BrainCellResult get(T request);

    T getRequest(String command);

    Collection<String> getCommands();
}
