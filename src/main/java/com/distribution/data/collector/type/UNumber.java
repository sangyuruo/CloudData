package com.distribution.data.collector.type;

import java.math.BigInteger;

public abstract class UNumber extends Number {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -7666221938815339843L;

    /**
     * Get this number as a {@link BigInteger}. This is a convenience method for
     * calling <code>new BigInteger(toString())</code>
     */
    public BigInteger toBigInteger() {
        return new BigInteger(toString());
    }
}
