package com.distribution.data.collector.event.ext;

public abstract class AbstractMessageEvent<T> implements MessageEvent<T> {
    String action;
    T messages;
    String type;

    public AbstractMessageEvent(String type, String action, T messages) {
        this.type = type;
        this.action = action;
        this.messages = messages;
    }

    @Override
    public String getAction() {
        return action;
    }

    @Override
    public T getMessage() {
        return messages;
    }

    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
