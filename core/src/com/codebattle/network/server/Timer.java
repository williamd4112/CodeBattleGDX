package com.codebattle.network.server;

public class Timer {

    private long startTime;
    private long timeout;

    private TimerCallBack callback;
    private CountDownThread countDownThread;

    public Timer(long defaultTimeout) {
        this.timeout = defaultTimeout;
    }

    public void start() {
        this.startTime = System.currentTimeMillis();
        this.countDownThread = new CountDownThread();
        this.countDownThread.start();
    }

    public void reset() {
        if (this.countDownThread != null)
            this.countDownThread.interrupt();
    }

    public void setCallback(TimerCallBack callback) {
        this.callback = callback;
    }

    private class CountDownThread extends Thread {
        @Override
        public void run() {
            while ((System.currentTimeMillis() - startTime) < timeout) {
                if (this.isInterrupted())
                    return;
            }
            callback.onTimeout();
        }
    }
}
