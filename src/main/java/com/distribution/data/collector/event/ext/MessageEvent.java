package com.distribution.data.collector.event.ext;

import java.io.Serializable;

public interface MessageEvent<T> extends Serializable{
    public int getSeq();
    public void setSeq(int seq);
    public String getType();
    public String getAction();
    public T getMessage();
}
