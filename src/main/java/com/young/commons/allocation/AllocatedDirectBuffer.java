package com.young.commons.allocation;

import java.nio.ByteBuffer;
import java.util.function.Consumer;

public class AllocatedDirectBuffer extends AllocatedBuffer {
    private final Consumer<AllocatedDirectBuffer> closeCallback;

    public AllocatedDirectBuffer(ByteBuffer buffer, Consumer<AllocatedDirectBuffer> closeCallback) {
        super(buffer);
        this.closeCallback = closeCallback;
    }

    public void doClose() {
        closeCallback.accept(this);
    }
}
