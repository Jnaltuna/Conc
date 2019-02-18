package conc;

import java.util.concurrent.TimeUnit;

/*
 * Clase de prueba. Ejecuto determinadas tareas
 * */

public class Tarea2 implements Runnable{

    private GestorDeMonitor mon;

    public Tarea2(GestorDeMonitor mon) {

        this.mon=mon;
    }

    @Override
    public void run() {
		/*try {
			TimeUnit.SECONDS.sleep(10);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}*/
        System.out.println("Tarea 2 es "+ Thread.currentThread().getName());

        mon.dispararTransicion(3);
        mon.dispararTransicion(3);
        mon.dispararTransicion(3);

    }

}
