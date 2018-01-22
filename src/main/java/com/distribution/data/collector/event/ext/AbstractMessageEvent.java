package com.distribution.data.collector.event.ext;

public abstract class AbstractMessageEvent<T> implements MessageEvent<T> {
    String action;
    T message;
    String type;
    int seq;

    public AbstractMessageEvent(){
    }

    public AbstractMessageEvent(String type, String action, T message) {
        this.type = type;
        this.action = action;
        this.message = message;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getSeq() {
        return this.seq;
    }

    @Override
    public String getAction() {
        return action;
    }

    @Override
    public T getMessage() {
        return message;
    }

    @Override
    public String getType() {
        return type;
    }
}
