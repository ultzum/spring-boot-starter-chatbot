package com.kingbbode.chatbot.autoconfigure.common.properties;

/**
 * Created by YG on 2016-10-13.
 */
public class BotProperties {
    private String prefix;
    private boolean testMode;

    public BotProperties(String prefix, boolean testMode) {
        this.prefix = prefix;
        this.testMode = testMode;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public boolean isTestMode() {
        return testMode;
    }

    public void setTestMode(boolean testMode) {
        this.testMode = testMode;
    }
}
