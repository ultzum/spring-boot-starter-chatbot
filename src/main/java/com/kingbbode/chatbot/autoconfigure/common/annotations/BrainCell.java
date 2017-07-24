package com.kingbbode.chatbot.autoconfigure.common.annotations;


import com.kingbbode.chatbot.autoconfigure.common.enums.BrainResponseType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  Bot 두뇌의 지식을 지정하는 Annotation
 *  @author YG
 *  @key 명령어 Key
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BrainCell {
    String parent() default "";
    String key() default "query";
    String explain() default "";
    String example() default "";
    String function();
    boolean cancelable() default true;
    BrainResponseType type() default BrainResponseType.MESSAGE;
}
