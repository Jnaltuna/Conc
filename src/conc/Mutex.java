package conc;

import java.util.concurrent.Semaphore;

/*
 * Clase que implementa el Mutex utilizado en el Monitor
 * */
//public
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
    //public
    Mutex() {
        semaphore = new Semaphore(1,true); //TODO fairness?
    }

    /*
     * Adquiere el semaforo
     * */
    //public
    void acquire() {

        try {
            semaphore.acquire();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /*
     * Libera el semaforo
     * */

    //public
    void release() {
        semaphore.release();
    }

}
