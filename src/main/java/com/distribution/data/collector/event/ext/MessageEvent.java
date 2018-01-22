package com.distribution.data.collector.event.ext;

public interface MessageEvent<T> {
    public String getType();
    public String getAction();
    public T getMessage();
}
