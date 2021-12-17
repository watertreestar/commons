package com.young.commons.allocation;

public final class BufferAllocators {
    private final static DirectBufferAllocator DIRECT_BUFFER_ALLOCATOR = new DirectBufferAllocator();

    private BufferAllocators() {
    }

    public static AllocatedBuffer allocate(int capacity) {
        return DIRECT_BUFFER_ALLOCATOR.allocate(capacity);
    }
}
