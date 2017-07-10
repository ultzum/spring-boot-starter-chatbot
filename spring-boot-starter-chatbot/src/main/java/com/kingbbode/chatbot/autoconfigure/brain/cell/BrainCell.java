package com.kingbbode.chatbot.autoconfigure.brain.cell;


import com.kingbbode.chatbot.autoconfigure.common.request.BrainRequest;
import com.kingbbode.chatbot.autoconfigure.common.result.BrainResult;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by YG on 2017-01-26.
 */
public interface BrainCell {
    String explain();
    BrainResult execute(BrainRequest brainRequest) throws InvocationTargetException, IllegalAccessException;
}
