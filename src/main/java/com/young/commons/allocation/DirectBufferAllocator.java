package com.young.commons.allocation;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicLong;

public class DirectBufferAllocator implements BufferAllocator {
    private static final AtomicLong ALLOCATED_MEMORY = new AtomicLong();

    @Override
    public AllocatedBuffer allocate(final int capacity) {
        final AllocatedDirectBuffer buffer =
            new AllocatedDirectBuffer(ByteBuffer.allocateDirect(capacity), DirectBufferAllocator::onFree);
        ALLOCATED_MEMORY.addAndGet(capacity);

        return buffer;
    }

    private static void onFree(final AllocatedDirectBuffer buffer) {
        ALLOCATED_MEMORY.addAndGet(-buffer.capacity());
    }

    public static long getAllocatedMemoryInKb() {
        return ALLOCATED_MEMORY.get() / 1024;
    }
}
