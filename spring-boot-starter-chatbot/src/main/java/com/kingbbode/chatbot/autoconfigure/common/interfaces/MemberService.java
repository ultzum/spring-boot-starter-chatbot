package com.kingbbode.chatbot.autoconfigure.common.interfaces;

import com.kingbbode.chatbot.autoconfigure.messenger.teamup.response.RoomInfoResponse;

/**
 * Created by YG on 2017-07-10.
 */
public interface MemberService {
    void update();

    String get(String memberId);

     boolean contains(String memberId);

    RoomInfoResponse getRoomInfo(String room);
}
