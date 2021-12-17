package com.young.commons.allocation;

import java.nio.ByteBuffer;

/**
 * Wrapper for java nio ByteBuffer
 */
public abstract class AllocatedBuffer implements AutoCloseable {
    protected ByteBuffer rawBuffer;
    private volatile boolean closed;

    public AllocatedBuffer(final ByteBuffer buffer) {
        rawBuffer = buffer;
        closed = false;
    }

    public ByteBuffer getRawBuffer() {
        return rawBuffer;
    }

    public int capacity() {
        return rawBuffer.capacity();
    }

    public boolean isClosed() {
        return closed;
    }

    @Override
    public void close() {
        if (!closed) {
            closed = true;
            doClose();
            rawBuffer = null;
        }
    }

    public void doClose() {
    }
}
