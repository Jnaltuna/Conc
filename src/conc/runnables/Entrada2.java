package conc.runnables;

import conc.*;

public class Entrada2 implements Runnable {

    GestorDeMonitor mon;

    public Entrada2(GestorDeMonitor mon) {
        this.mon = mon;
    }

    @Override
    public void run() {

        mon.dispararTransicion(21);

        mon.dispararTransicion(11);
        mon.dispararTransicion(12);
        mon.dispararTransicion(14);
    }
}