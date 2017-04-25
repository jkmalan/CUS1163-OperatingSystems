package com.jkmalan.Multithreading.LinkedList;

public class RunLinkedListThread {
    public static void main(String[] args) {
        Thread thread1 = new Thread(new LinkedListThread());
        thread1.start();
        Thread thread2 = new Thread(new LinkedListThread());
        thread2.start();
    }
}
