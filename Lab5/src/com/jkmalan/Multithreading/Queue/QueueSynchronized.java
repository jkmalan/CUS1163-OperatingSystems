package com.jkmalan.Multithreading.Queue;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author jkmalan (aka John Malandrakis)
 */
public class QueueSynchronized extends Queue {

    private Lock queueLock;
    private Condition elementsEmpty;

    public QueueSynchronized() {
        super();
        queueLock = new ReentrantLock();
        elementsEmpty = queueLock.newCondition();
    }

    public QueueSynchronized(int size) {
        super(size);
        queueLock = new ReentrantLock();
        elementsEmpty = queueLock.newCondition();
    }

    @Override
    public void add(String item) {
        queueLock.lock();
        try {
            super.add(item);
            elementsEmpty.signalAll();
        } finally {
            queueLock.unlock();
        }
    }

    @Override
    public String remove() {
        String element = null;
        queueLock.lock();
        try {
            while (super.isEmpty()) {
                elementsEmpty.await();
            }
            element = super.remove();
        } catch (InterruptedException e) {
            System.out.println("Removal was interrupted, skipping removal!");
        } finally {
            queueLock.unlock();
        }
        return element;
    }

}
