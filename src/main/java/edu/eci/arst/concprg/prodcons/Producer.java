/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arst.concprg.prodcons;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hcadavid
 */
public class Producer extends Thread {

    private LinkedBlockingQueue<Integer>  queue = null;

    private int dataSeed = 0;
    private Random rand=null;
    private final long stockLimit;

    public Producer(LinkedBlockingQueue<Integer> queue, long stockLimit) {
        this.queue = queue;
        rand = new Random(System.currentTimeMillis());
        this.stockLimit=stockLimit;
    }

    @Override
    public void run() {
        while (true) {

            dataSeed = dataSeed + rand.nextInt(100);
            try {
                queue.put(dataSeed);
                System.out.println("Producer added " + dataSeed);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
