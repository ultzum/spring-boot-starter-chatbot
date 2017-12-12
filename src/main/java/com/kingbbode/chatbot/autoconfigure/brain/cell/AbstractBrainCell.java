package com.kingbbode.chatbot.autoconfigure.brain.cell;


import com.kingbbode.chatbot.autoconfigure.common.request.BrainRequest;
import com.kingbbode.chatbot.autoconfigure.common.result.BrainResult;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by YG on 2017-01-26.
 */
public abstract class AbstractBrainCell implements BrainCell {
    @Override
    public String explain() {
        return "";
    }

    public static AbstractBrainCell NOT_FOUND_BRAIN_CELL = new AbstractBrainCell() {
        @Override
        public BrainResult execute(BrainRequest brainRequest) {
            return BrainResult.NONE;
        }

        @Override
        public String explain() {
            return "존재하지 않는 기능입니다.";
        }
    };
}
