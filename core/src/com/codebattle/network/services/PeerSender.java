package com.codebattle.network.services;

import com.codebattle.network.models.PassingData;

import java.io.IOException;
import java.io.ObjectOutput;
import java.util.concurrent.BlockingQueue;

public class PeerSender extends PeerService {
    /*
     * Streams
     */
    private final ObjectOutput outputStream;

    /*
     * Data
     */
    private final BlockingQueue<PassingData> outputQueue;

    public PeerSender(final ObjectOutput outputStream,
            final BlockingQueue<PassingData> outputQueue) {
        this.outputStream = outputStream;
        this.outputQueue = outputQueue;
    }

    @Override
    protected void runService() {
        PassingData passingData;

        // Send an object to remote.
        passingData = this.tryTakeObjectFromQueue();

        if (null != passingData) {
            this.tryWriteObject(passingData);
        }
    }

    private void tryWriteObject(final PassingData out) {
        try {
            this.outputStream.writeObject(out);
        } catch (final IOException e) {
            // Ignore it
        }
    }

    private PassingData tryTakeObjectFromQueue() {
        try {
            return this.outputQueue.take();
        } catch (final InterruptedException e) {
            return null;
        }
    }
}
