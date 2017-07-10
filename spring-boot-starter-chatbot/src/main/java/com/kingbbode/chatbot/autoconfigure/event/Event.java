package com.kingbbode.chatbot.autoconfigure.event;

import com.kingbbode.chatbot.autoconfigure.common.interfaces.Dispatcher;
import lombok.Data;

/**
 * Created by YG on 2017-07-10.
 */
@Data
public class Event<T> {
    private Dispatcher<T> dispatcher;
    private T item;

    public Event(Dispatcher<T> dispatcher, T item) {
        this.dispatcher = dispatcher;
        this.item = item;
    }

    public void execute(){
        dispatcher.dispatch(item);
    }
}
