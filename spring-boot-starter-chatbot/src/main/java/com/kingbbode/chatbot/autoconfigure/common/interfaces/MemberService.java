package com.kingbbode.chatbot.autoconfigure.common.interfaces;

/**
 * Created by YG on 2017-07-10.
 */
public interface MemberService {
    void update();

    String get(String memberId);

     boolean contains(String memberId);
}
