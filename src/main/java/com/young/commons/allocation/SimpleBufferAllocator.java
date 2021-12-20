package com.young.commons.allocation;

import java.nio.ByteBuffer;

public class SimpleBufferAllocator extends AbstractBufferAllocator {

    @Override
    public AllocatedBuffer allocate(int capacity) {
        final AllocatedSimpleBuffer buffer =
            new AllocatedSimpleBuffer(ByteBuffer.allocate(capacity));
        ALLOCATED_MEMORY.addAndGet(capacity);
        return buffer;
    }
}
