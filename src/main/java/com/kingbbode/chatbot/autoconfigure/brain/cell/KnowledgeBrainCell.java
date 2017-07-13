package com.kingbbode.chatbot.autoconfigure.brain.cell;

import com.kingbbode.chatbot.autoconfigure.base.knowledge.component.KnowledgeComponent;
import com.kingbbode.chatbot.autoconfigure.common.request.BrainRequest;
import com.kingbbode.chatbot.autoconfigure.common.result.BrainResult;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Random;

/**
 * Created by YG on 2017-01-26.
 */
public class KnowledgeBrainCell extends AbstractBrainCell {
    private KnowledgeComponent knowledgeComponent;

    public KnowledgeBrainCell(KnowledgeComponent knowledgeComponent) {
        this.knowledgeComponent = knowledgeComponent;
    }

    @Override
    public String explain() {
        return "사용자 학습 기능 입니다.";
    }
    
    @Override
    public BrainResult execute(BrainRequest brainRequest) throws InvocationTargetException, IllegalAccessException {
        List<String> results = knowledgeComponent.get(brainRequest.getContent());
        if(results == null){
            return BrainResult.NONE;
        }

        return BrainResult.builder()
                .message(results.get(new Random().nextInt(results.size())))
                .room(brainRequest.getRoom())
                .build();
    }
}
