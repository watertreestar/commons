package com.young.commons.allocation;

import java.util.concurrent.atomic.AtomicLong;

public abstract class AbstractBufferAllocator implements BufferAllocator {
    protected static final AtomicLong ALLOCATED_MEMORY = new AtomicLong();

    protected static void onFree(final AllocatedDirectBuffer buffer) {
        ALLOCATED_MEMORY.addAndGet(-buffer.capacity());
    }

    public static long getAllocatedMemoryInKb() {
        return ALLOCATED_MEMORY.get() / 1024;
    }
}
