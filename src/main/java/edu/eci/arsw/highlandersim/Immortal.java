package edu.eci.arsw.highlandersim;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Immortal extends Thread {

    private ImmortalUpdateReportCallback updateCallback = null;

    private AtomicInteger health;

    private int defaultDamageValue;

    private final CopyOnWriteArrayList<Immortal> immortalsPopulation;

    private final String name;

    private final Random r = new Random(System.currentTimeMillis());

    public static final AtomicInteger nPausados = new AtomicInteger(0);

    public static Object monitor1 = new Object();

    private  volatile boolean vivo = true;


    public Immortal(String name, CopyOnWriteArrayList<Immortal> immortalsPopulation, int health, int defaultDamageValue, ImmortalUpdateReportCallback ucb) {
        super(name);
        this.updateCallback = ucb;
        this.name = name;
        this.immortalsPopulation = immortalsPopulation;
        this.health = new AtomicInteger(health);
        this.defaultDamageValue = defaultDamageValue;
    }

    public void run() {

        while (!ControlFrame.stop && vivo) {
            if (!ControlFrame.pause) {
                Immortal im=null;
                int myIndex = immortalsPopulation.indexOf(this);
                boolean valid=false;
                while (!valid) {
                    int nextFighterIndex = r.nextInt(immortalsPopulation.size());
                    if (nextFighterIndex == myIndex) {
                        nextFighterIndex = ((nextFighterIndex + 1) % immortalsPopulation.size());
                    }
                    try {
                        im = immortalsPopulation.get(nextFighterIndex);
                        valid=true;
                    }
                    catch (Exception e){
                    }
                }
                this.fight(im);

                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                pausar();
            }
        }

    }

    private void pausar() {
        nPausados.incrementAndGet();

        synchronized (ControlFrame.monitor) {
            if (nPausados.get() == immortalsPopulation.size()) {
                ControlFrame.monitor.notifyAll();
            }
        }
        synchronized (monitor1) {
            if (ControlFrame.pause) {
                try {
                    monitor1.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void fight(Immortal i2) {
        boolean attack = false;
        if (getHealth().get() <= 0) {
            immortalsPopulation.remove(this);
            vivo = false;
        }
        else {
            synchronized (i2) {
                attack = i2.getHealth().get() > 0;
                if (attack) {
                    i2.changeHealth(-defaultDamageValue);
                }
            }
            synchronized (this) {
                if (attack) {
                    this.changeHealth(defaultDamageValue);
                }
            }
            updateCallback.processReport("Fight: " + this + " vs " + i2 + "\n");
        }
        //else {
        //    updateCallback.processReport(this + " says:" + i2 + " is already dead!\n");
        //}
    }

    public void changeHealth(int v) {
        health.addAndGet(v);
    }


    public AtomicInteger getHealth() {
        return health;
    }

    @Override
    public String toString() {

        return name + "[" + health + "]";
    }

}
