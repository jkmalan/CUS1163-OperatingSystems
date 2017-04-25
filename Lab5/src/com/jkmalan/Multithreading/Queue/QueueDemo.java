package com.jkmalan.Multithreading.Queue;

public class QueueDemo {

    public static void main(String[] args) {
        QueueSynchronized q = new QueueSynchronized();

        Thread producer = new Thread(new Producer(q, 100));
        producer.start();
        Thread consumer = new Thread(new Consumer(q, 100));
        consumer.start();

    }

}
