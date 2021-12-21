package com.young.commons.allocation;

import java.nio.ByteBuffer;

public class SimpleBufferAllocator extends AbstractBufferAllocator {

    @Override
    public AllocatedBuffer allocate(int capacity) {
        final AllocatedDirectBuffer buffer =
            new AllocatedDirectBuffer(ByteBuffer.allocate(capacity), SimpleBufferAllocator::onFree);
        ALLOCATED_MEMORY.addAndGet(capacity);

        return buffer;
    }
}
