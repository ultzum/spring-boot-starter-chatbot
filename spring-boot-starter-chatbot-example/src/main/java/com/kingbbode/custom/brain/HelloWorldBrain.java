package com.kingbbode.custom.brain;

import com.kingbbode.chatbot.autoconfigure.common.annotations.Brain;
import com.kingbbode.chatbot.autoconfigure.common.annotations.BrainCell;
import com.kingbbode.chatbot.autoconfigure.common.exception.ArgumentInvalidException;
import com.kingbbode.chatbot.autoconfigure.common.request.BrainRequest;

/**
 * Created by YG on 2017-07-10.
 */
@Brain
public class HelloWorldBrain {
    @BrainCell(key = "Hello", explain = "시작 샘플", example = "#Hello", function = "hello")
    public String vacationY(BrainRequest brainRequest) {
        return "World!";
    }
}
