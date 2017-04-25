package com.jkmalan.Multithreading.Queue;

import java.time.LocalDateTime;

/**
 * @author jkmalan (aka John Malandrakis)
 */
public class Producer implements Runnable {

    private QueueSynchronized queue;

    private static int MAX_LOOPS;

    public Producer(QueueSynchronized q, int max_loops) {
        queue = q;
        MAX_LOOPS = max_loops;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < MAX_LOOPS; i++) {
                String dt = LocalDateTime.now().toString();
                queue.add(dt);
                System.out.println(i + ": Added: " + dt);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted!");
        }
    }

}
