package conc.runnables;

import conc.*;

public class Entrada1 implements Runnable {

    GestorDeMonitor mon;

    public Entrada1(GestorDeMonitor mon) {
        this.mon = mon;
    }

    @Override
    public void run() {
        mon.dispararTransicion(0);
        mon.dispararTransicion(18);
        mon.dispararTransicion(3);

        mon.dispararTransicion(6);

        mon.dispararTransicion(22);
        mon.dispararTransicion(8);
        mon.dispararTransicion(10);


    }
}