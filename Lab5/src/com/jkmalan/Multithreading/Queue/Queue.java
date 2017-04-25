package com.jkmalan.Multithreading.Queue;

import java.util.ArrayList;

/**
 * This class implements the FIFO queue data structure.
 */
public class Queue {
    private ArrayList<String> queue;
    private int size = 0;
    private static final int DEFAULT_SIZE = 10;

    /**
     * Constructs the maximum size of the queue to default size.
     */
    public Queue() {
        size = DEFAULT_SIZE;
        queue = new ArrayList<String>(size);
    }

    /**
     * Constructs the maximum size of the queue to the given size.
     *
     * @param aSize the maximum size of the queue
     */
    public Queue(int aSize) {
        size = aSize;
        queue = new ArrayList<String>(size);
    }

    /**
     * Adds a string into the queue.
     *
     * @param item the item to add
     */
    public void add(String item) {
        queue.add(item);
    }

    /**
     * Removes one item from the queue.
     *
     * @return the first item of the queue
     */
    public String remove() {
        String element = null;
        element = queue.remove(0).toString();
        return element;
    }

    /**
     * Check if the queue is empty.
     *
     * @return true if the queue is empty, else false
     */
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    /**
     * Check if the queue is full.
     *
     * @return true if queue size equals to maximum size, else false
     */
    public boolean isFull() {
        return queue.size() == size;
    }
}
