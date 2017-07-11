package com.kingbbode.chatbot.autoconfigure.base.emoticon.brain;

import com.kingbbode.chatbot.autoconfigure.base.emoticon.component.EmoticonComponent;
import com.kingbbode.chatbot.autoconfigure.common.annotations.Brain;
import com.kingbbode.chatbot.autoconfigure.common.annotations.BrainCell;
import com.kingbbode.chatbot.autoconfigure.common.exception.BrainException;
import com.kingbbode.chatbot.autoconfigure.common.request.BrainRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.util.Set;

/**
 * Created by YG on 2017-04-07.
 */
@Brain
@ConditionalOnBean(EmoticonComponent.class)
@ConditionalOnProperty(prefix = "chatbot", name = "enableEmoticon", havingValue = "true")
public class EmoticonBrain {
    
    @Autowired
    private EmoticonComponent emoticonComponent;

    @BrainCell(key = "이모티콘", explain = "이모티콘 조회 및 등록", example = "#이모티콘", function = "emoticon")
    public String emoticon(BrainRequest brainRequest){
        return "조회하려면 '조회'\n" + 
                "등록하려면 '등록'\n" +
                "삭제하려면 '삭제'\n" +
                "을 입력해주세요.";
    }
    
        @BrainCell(parent = "emoticon", key = "조회", function = "emoticonList")
        public String emoticonList(BrainRequest brainRequest){
            Set<String> emoticonEntrySet = emoticonComponent.getEmoticons().keySet();
            if(emoticonEntrySet.size()>0){
                StringBuilder result = new StringBuilder();
                result
                        .append("총 ")
                        .append(emoticonEntrySet.size())
                        .append("개\n");

                for (String key : emoticonEntrySet) {
                    result
                    .append(key)
                    .append("\n");
                }
                return result.toString();
            }else{
                return "등록된 이모티콘이 업습니다";
            }
        }

        @BrainCell(parent = "emoticon", key = "등록", function = "emoticonReg0")
        public String emoticonReg(BrainRequest brainRequest){
            return "등록할 명령어를 입력해주세요.";
        }

            @BrainCell(parent = "emoticonReg0", function = "emoticonReg1")
            public String emoticonReg1(BrainRequest brainRequest){
                if(emoticonComponent.contains(brainRequest.getContent())){
                    throw new BrainException(null, "이미 등록된 이름입니다. 다시 입력해주세요.");
                }
                brainRequest.getConversation().put("name", brainRequest.getContent());
                return "등록할 이미지를 전송해주세요.\n" +
                        "jpg나 png만 허용합니다.\n" + 
                        "파일이름에 공백 또는 . 을 허용하지 않습니다.";
            }

                @BrainCell(parent = "emoticonReg1", function = "emoticonReg2")
                public String emoticonReg2(BrainRequest brainRequest){
                    throw new BrainException(null, "등록할 이미지를 전송해주세요.");
                }
    
        @BrainCell(parent = "emoticon", key = "삭제", function = "emoticonDelete")
        public String emoticonDelete(BrainRequest brainRequest){
            return "삭제할 명령어를 입력해주세요.";
        }

            @BrainCell(parent = "emoticonDelete", function = "emoticonDelete1")
            public String emoticonDelete1(BrainRequest brainRequest){
                if(!emoticonComponent.contains(brainRequest.getContent())){
                    throw new BrainException(null, "등록되지 않은 이모티콘입니다. 다시 입력해주세요.");
                }
                emoticonComponent.remove(brainRequest.getContent());
                return brainRequest.getContent() + " 이모티콘이 삭제되었습니다.";
            }
}
