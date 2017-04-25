package com.jkmalan.Multithreading.Queue;

/**
 * @author jkmalan (aka John Malandrakis)
 */
public class Consumer implements Runnable {

    private QueueSynchronized queue;

    private static int MAX_LOOPS;

    public Consumer(QueueSynchronized q, int max_loops) {
        queue = q;
        MAX_LOOPS = max_loops;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < MAX_LOOPS; i++) {
                String dt = queue.remove();
                System.out.println(i + ": Removed: " + dt);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted!");
        }
    }

}
