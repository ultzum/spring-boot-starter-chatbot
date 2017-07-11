package com.kingbbode.chatbot.autoconfigure.messenger.teamup;

import com.kingbbode.chatbot.autoconfigure.common.interfaces.MemberService;
import com.kingbbode.chatbot.autoconfigure.messenger.teamup.response.OrganigrammeResponse;
import com.kingbbode.chatbot.autoconfigure.messenger.teamup.response.RoomInfoResponse;
import com.kingbbode.chatbot.autoconfigure.messenger.teamup.templates.template.AuthTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

/**
 * Created by YG on 2017-03-31.
 */
@Transactional
public class TeamUpMemberService implements MemberService {
    
    @Autowired
    private TeamUpMemberCached memberCached;
    
    @Autowired
    private AuthTemplate authTemplate;

    @PostConstruct
    @Override
    public void update(){
        OrganigrammeResponse response = authTemplate.readOrganigramme();
        if(response == null){
            return;
        }
        memberCached.updateMember(response);
    }

    @Override
    public String get(String memberId){
        return memberCached.getMemberName(Long.valueOf(memberId));
    }

    @Override
    public boolean contains(String memberId){
        if(!memberCached.containsMember(Long.valueOf(memberId))){
            update();
        }
        return memberCached.containsMember(Long.valueOf(memberId));
    }

    @Override
    public RoomInfoResponse getRoomInfo(String room) {
        return authTemplate.getMembersInRoom(room);
    }
}
