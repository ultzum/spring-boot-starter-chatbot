package com.kingbbode.chatbot.autoconfigure.common.interfaces;

/**
 * Created by YG on 2017-07-10.
 */
public interface Dispatcher<T> {
    void dispatch(T event);
}
