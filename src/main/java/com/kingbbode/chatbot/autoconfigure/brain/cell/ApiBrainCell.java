package com.kingbbode.chatbot.autoconfigure.brain.cell;

import com.kingbbode.chatbot.autoconfigure.common.request.BrainRequest;
import com.kingbbode.chatbot.autoconfigure.common.result.BrainResult;
import org.apache.commons.lang3.text.WordUtils;
import org.springframework.beans.factory.BeanFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by YG on 2017-01-26.
 */
public class ApiBrainCell extends AbstractBrainCell{
    private String name;
    private Method active;
    private Object object;
    private BeanFactory beanFactory;

    public ApiBrainCell(Class<?> clazz, Method active, BeanFactory beanFactory) {
        this.name = WordUtils.uncapitalize(clazz.getSimpleName());
        this.active = active;
        this.beanFactory = beanFactory;
    }

    @Override
    public BrainResult execute(BrainRequest brainRequest) throws InvocationTargetException, IllegalAccessException {
        if (!inject()) {
            return BrainResult.Builder.FAILED.room(brainRequest.getRoom()).build();
        }
        return (BrainResult) active.invoke(object, brainRequest);
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
