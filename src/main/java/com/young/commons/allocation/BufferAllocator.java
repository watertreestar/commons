package com.young.commons.allocation;

public interface BufferAllocator {
    AllocatedBuffer allocate(int capacity);
}
