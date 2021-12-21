package com.young.commons.allocation;

import java.nio.ByteBuffer;

public class DirectBufferAllocator extends AbstractBufferAllocator {

    @Override
    public AllocatedBuffer allocate(final int capacity) {
        final AllocatedDirectBuffer buffer =
            new AllocatedDirectBuffer(ByteBuffer.allocateDirect(capacity), DirectBufferAllocator::onFree);
        ALLOCATED_MEMORY.addAndGet(capacity);

        return buffer;
    }
}
