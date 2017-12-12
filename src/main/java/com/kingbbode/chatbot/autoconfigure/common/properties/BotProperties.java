package com.kingbbode.chatbot.autoconfigure.common.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by YG on 2016-10-13.
 */
@Getter
@Setter
public class BotProperties {
    private String name;
    private String commandPrefix;
    private String emoticonPrefix;
    private boolean testMode;

    public BotProperties(String name, String commandPrefix, String emoticonPrefix, boolean testMode) {
        this.name = name;
        this.commandPrefix = commandPrefix;
        this.emoticonPrefix = emoticonPrefix;
        this.testMode = testMode;
    }
    
    public boolean isTestMode() {
        return testMode;
    }
}
