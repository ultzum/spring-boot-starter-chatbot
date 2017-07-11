package com.kingbbode.chatbot.autoconfigure.brain.cell;

import com.kingbbode.chatbot.autoconfigure.common.enums.BrainResponseType;
import com.kingbbode.chatbot.autoconfigure.common.exception.ArgumentInvalidException;
import com.kingbbode.chatbot.autoconfigure.common.exception.BrainException;
import com.kingbbode.chatbot.autoconfigure.common.exception.InvalidReturnTypeException;
import com.kingbbode.chatbot.autoconfigure.common.request.BrainRequest;
import com.kingbbode.chatbot.autoconfigure.common.result.BrainCellResult;
import com.kingbbode.chatbot.autoconfigure.common.result.BrainResult;
import org.apache.commons.lang3.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by YG on 2017-01-23.
 */
public class CommonBrainCell extends AbstractBrainCell{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private String name;
    private Method active;
    private Object object;
    private BeanFactory beanFactory;
    private com.kingbbode.chatbot.autoconfigure.common.annotations.BrainCell brain;

    public CommonBrainCell(com.kingbbode.chatbot.autoconfigure.common.annotations.BrainCell brain, Class<?> clazz, Method active, BeanFactory beanFactory) {
        this.name = WordUtils.uncapitalize(clazz.getSimpleName());
        this.brain = brain;
        this.active = active;
        this.beanFactory = beanFactory;
    }

    @Override
    public String explain() {
        return brain.explain();
    }

    @Override
    public BrainResult execute(BrainRequest brainRequest) throws InvocationTargetException, IllegalAccessException {
        if (!inject()) {
            return BrainResult.Builder.FAILED.room(brainRequest.getRoom()).build();
        }
        Object result;
        try {
            result = active.invoke(object, brainRequest);
        }catch(Throwable e){
            if(e.getCause() instanceof BrainException){
                return new BrainResult.Builder()
                        .message(e.getCause().getMessage())
                        .room(brainRequest.getRoom())
                        .type(BrainResponseType.MESSAGE)
                        .build();
            }else if(e.getCause() instanceof ArgumentInvalidException){
                return new BrainResult.Builder()
                        .message(active.getAnnotation(com.kingbbode.chatbot.autoconfigure.common.annotations.BrainCell.class).example())
                        .room(brainRequest.getRoom())
                        .type(BrainResponseType.MESSAGE)
                        .build();
            }else if(e.getCause() instanceof InvalidReturnTypeException){
                return new BrainResult.Builder()
                        .message("Method Return Type Exception!")
                        .room(brainRequest.getRoom())
                        .type(BrainResponseType.MESSAGE)
                        .build();
            }
            return new BrainResult.Builder()
                    .message("Server Error : " + e.getMessage())
                    .room(brainRequest.getRoom())
                    .type(BrainResponseType.MESSAGE)
                    .build();
        }
        if(result instanceof BrainCellResult){
            return new BrainResult.Builder()
                    .result((BrainCellResult) result)
                    .type(brain.type())
                    .build();
        }        
        
        return new BrainResult.Builder()
                .message((String) result)
                .room(brainRequest.getRoom())
                .type(brain.type())
                .build();
    }

    private boolean inject() {
        if (object != null) {
            return true;
        }
        if (beanFactory.containsBean(name)) {
            object = beanFactory.getBean(name);
        }

        return object != null;
    }
}
