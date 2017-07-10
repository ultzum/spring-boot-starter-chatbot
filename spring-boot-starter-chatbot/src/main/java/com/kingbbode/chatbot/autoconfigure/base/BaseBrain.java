package com.kingbbode.chatbot.autoconfigure.base;

import com.kingbbode.chatbot.autoconfigure.common.annotations.Brain;
import com.kingbbode.chatbot.autoconfigure.common.annotations.BrainCell;
import com.kingbbode.chatbot.autoconfigure.brain.cell.ApiBrainCell;
import com.kingbbode.chatbot.autoconfigure.brain.cell.CommonBrainCell;
import com.kingbbode.chatbot.autoconfigure.base.emoticon.component.EmoticonComponent;
import com.kingbbode.chatbot.autoconfigure.brain.factory.BrainFactory;
import com.kingbbode.chatbot.autoconfigure.base.knowledge.component.KnowledgeComponent;
import com.kingbbode.chatbot.autoconfigure.base.stat.StatComponent;
import com.kingbbode.chatbot.autoconfigure.common.enums.BrainResponseType;
import com.kingbbode.chatbot.autoconfigure.common.request.BrainRequest;
import com.kingbbode.chatbot.autoconfigure.common.util.BrainUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * Created by YG-MAC on 2017. 1. 27..
 */
@Brain
@ConditionalOnBean(BrainFactory.class)
@ConditionalOnProperty(prefix = "chatbot", name = "enableBase", havingValue = "true")
public class BaseBrain {

    @Autowired
    private BrainFactory brainFactory;
    
    @Autowired
    private KnowledgeComponent knowledgeComponent;
    
    @Autowired
    private EmoticonComponent emoticonComponent;
    
    @Autowired
    private StatComponent statComponent;

    @BrainCell(key = "기능", explain = "기능 목록 출력", example = "#기능", function = "function")
    public String explain(BrainRequest brainRequest) {
        return "울트론 v4.5.0 \n" +
                "!픽미(랜덤으로 방에서 한명 뽑기) 기능을 조정훈 담당님께서 추가해주셨습니다..\n" + 
                "!학습 기능 버그가 수정되었습니다..\n" +
                "!나가 기능이 추가되었습니다..\n" +
                "!버그 제보는 포털개발팀 권용근 담당에게 부탁드립니다!\n\n" +
                "**** 기능 목록 **** \n" +
                BrainUtil.explainDetail(brainFactory.findBrainCellByType(CommonBrainCell.class)) +
                "\n**** API 기능 목록 **** \n" +
                BrainUtil.explainDetail(brainFactory.findBrainCellByType(ApiBrainCell.class)) +
                "\n**** 이모티콘 목록 **** \n" +
                BrainUtil.explainForEmoticon(emoticonComponent.getEmoticons()) +
                "\n**** 학습 목록 **** \n" +
                BrainUtil.explainForKnowledge(knowledgeComponent.getCommands());
    }
       
    @BrainCell(key = "고유정보", explain = "고유 정보 추출", example = "#팀업고유정보", function = "teamup")
    public String teamupId(BrainRequest brainRequest){
        return brainRequest.toString();
    }
    
    @BrainCell(key = "나가", explain = "쫓아내기", example = "#나가", function = "getout", type = BrainResponseType.OUT)
    public String outRoom(BrainRequest brainRequest) {
        return "바이바이~";
    }
    
    @BrainCell(key = "울트론통계", explain = "뭘 많이 쓰는지..", example = "#울트론통계", function = "statstat")
    public String getStat(BrainRequest brainRequest) {
        return statComponent.toString();
    }
}
