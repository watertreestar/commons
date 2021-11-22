package com.young.commons.retelimit;

public abstract class RateLimiter {
    protected int qps;

    public RateLimiter(int qps) {
        this.qps = qps;
    }

    public abstract boolean tryAcquire();
}
