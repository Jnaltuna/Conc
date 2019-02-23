package conc;

import java.util.concurrent.Semaphore;

/*
 * Clase que implementa el Mutex utilizado en el Monitor
 * */

class Mutex {

    /*
     * Atributo:
     * semaphore -> se utiliza la clase semaforo
     * */
    private final Semaphore semaphore;

    /*
     * Constructor:
     * Inicializa el semaforo con un elemento, ya que solo puede haber uno en el monitor
     * */

    Mutex() {
        semaphore = new Semaphore(1, true);
    }

    /*
     * Adquiere el semaforo
     * */

    void acquire() {

        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /*
     * Libera el semaforo
     * */


    void release() {
        semaphore.release();
    }

}
