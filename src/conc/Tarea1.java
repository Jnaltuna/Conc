package conc;

import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

/*
 * Clase de prueba. Ejecuto determinadas tareas
 * */

public class Tarea1 implements Runnable{

    private GestorDeMonitor mon;

    public Tarea1(GestorDeMonitor mon) {

        this.mon=mon;
    }

    @Override
    public void run() {

		/*try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}*/

		for(int i = 0;i<10;i++){
		    mon.dispararTransicion(0);
            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
