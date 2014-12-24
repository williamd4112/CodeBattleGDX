package com.codebattle.network.services;

public abstract class PeerService implements Runnable {
    /*
     * Running states
     */
    private boolean isRunning = true;
    private boolean isFinished = false;

    @Override
    public void run() {
        while (this.isRunning) {
            this.runService();
        }

        this.isFinished = true;
    }

    protected abstract void runService();

    public boolean isFinished() {
        return this.isFinished;
    }

    public void stop() {
        this.isRunning = false;
    }
}
