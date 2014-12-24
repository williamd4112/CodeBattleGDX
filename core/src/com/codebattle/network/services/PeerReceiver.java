package com.codebattle.network.services;

import com.codebattle.network.models.PassingData;

import java.io.IOException;
import java.io.ObjectInput;
import java.util.concurrent.BlockingQueue;

public class PeerReceiver extends PeerService {
    /*
     * Streams
     */
    private final ObjectInput inputStream;

    /*
     * Data
     */
    private final BlockingQueue<PassingData> inputQueue;

    public PeerReceiver(final ObjectInput inputStream,
            final BlockingQueue<PassingData> inputQueue) {
        this.inputStream = inputStream;
        this.inputQueue = inputQueue;
    }

    @Override
    protected void runService() {
        PassingData passingData;

        // Receive an object from remote.
        passingData = this.tryReadObject();

        if (null != passingData) {
            this.tryPutObjectToQueue(passingData);
        }
    }

    private PassingData tryReadObject() {
        try {
            final Object inObject = this.inputStream.readObject();

            if (PassingData.class == inObject.getClass()) {
                return (PassingData) inObject;
            } else {
                return null;
            }
        } catch (final ClassNotFoundException e) {
            return null;
        } catch (final IOException e) {
            return null;
        }
    }

    private void tryPutObjectToQueue(final PassingData passingData) {
        try {
            this.inputQueue.put(passingData);
        } catch (final InterruptedException e) {
            // Ignore it
        }
    }

}
