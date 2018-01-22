package com.distribution.data.collector.event.ext;

public class ObjectMessageEvent implements MessageEvent<Object> {

    String action;
    Object message;
    String type;
    int seq;

    @Override
    public String getAction() {
        return action;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public int getSeq() {
        return seq;
    }

    @Override
    public void setSeq(int seq) {
        this.seq = seq;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ObjectMessageEvent() {
        super();
    }


}

