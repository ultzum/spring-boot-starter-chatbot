package com.kingbbode.chatbot.autoconfigure.base;

import com.kingbbode.chatbot.autoconfigure.base.emoticon.component.EmoticonComponent;
import com.kingbbode.chatbot.autoconfigure.base.knowledge.component.KnowledgeComponent;
import com.kingbbode.chatbot.autoconfigure.base.stat.StatComponent;
import com.kingbbode.chatbot.autoconfigure.brain.cell.ApiBrainCell;
import com.kingbbode.chatbot.autoconfigure.brain.cell.CommonBrainCell;
import com.kingbbode.chatbot.autoconfigure.brain.factory.BrainFactory;
import com.kingbbode.chatbot.autoconfigure.common.annotations.Brain;
import com.kingbbode.chatbot.autoconfigure.common.annotations.BrainCell;
import com.kingbbode.chatbot.autoconfigure.common.enums.BrainResponseType;
import com.kingbbode.chatbot.autoconfigure.common.request.BrainRequest;
import com.kingbbode.chatbot.autoconfigure.common.util.BrainUtil;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by YG-MAC on 2017. 1. 27..
 */
@Brain
public class BaseBrain {

    @Autowired
    private BrainFactory brainFactory;
    
    @Autowired
    private KnowledgeComponent knowledgeComponent;
    
    @Autowired
    private EmoticonComponent emoticonComponent;
    
    @Autowired
    private StatComponent statComponent;

    @BrainCell(key = "기능", explain = "기능 목록 출력", function = "function")
    public String explain(BrainRequest brainRequest) {
        return  "**** 기능 목록 **** \n" +
                BrainUtil.explainDetail(brainFactory.findBrainCellByType(CommonBrainCell.class)) +
                "\n**** API 기능 목록 **** \n" +
                BrainUtil.explainDetail(brainFactory.findBrainCellByType(ApiBrainCell.class)) +
                "\n**** 이모티콘 목록 **** \n" +
                BrainUtil.explainForEmoticon(emoticonComponent.getEmoticons()) +
                "\n**** 학습 목록 **** \n" +
                BrainUtil.explainForKnowledge(knowledgeComponent.getCommands());
    }
       
    @BrainCell(key = "고유정보", explain = "고유 정보 추출",function = "teamup")
    public String teamupId(BrainRequest brainRequest){
        return brainRequest.toString();
    }
    
    @BrainCell(key = "나가", explain = "쫓아내기", function = "getout", type = BrainResponseType.OUT)
    public String outRoom(BrainRequest brainRequest) {
        return "바이바이~";
    }
    
    @BrainCell(key = "기능통계", explain = "뭘 많이 쓰는지..", function = "statstat")
    public String getStat(BrainRequest brainRequest) {
        return statComponent.toString();
    }
}
