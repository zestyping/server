package org.projectbuendia.logic;

import org.projectbuendia.fileops.Logging;

/**
 * Created by wwadewitte on 10/4/14.
 */
public class BackupThread extends Thread {
    private boolean running = false;
    private long now;
    private long SECOND_NS = 1000000000; // 1 second
    private long DELAY_NS = 10 * SECOND_NS;
    private long lastFrameNs;

    public void run() {
        running = true;
        do {
            try {
                tick();
            } catch (Exception e) {
                Logging.log("BackupThread problem", e);
            }
        } while (running);
    }

    public void tick() {
        while (now - lastFrameNs < DELAY_NS) {
            try {
                Thread.sleep(1);
            } catch (Exception e) {
            }
            now = System.nanoTime();
        }

        lastFrameNs = System.nanoTime();

       performThreadActions();
    }

    public void performThreadActions() {
        //System.out.println("backing up  at " + System.currentTimeMillis());
    }
}
