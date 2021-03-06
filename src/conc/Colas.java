package conc;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 * Clase Colas
 * Implementa las colas necesarias para el disparo de transiciones utilizando el monitor
 */
class Colas {

    /**
     * Parametros
     * colas ->ArrayList de semaforos. Un semaforo por cada transicion
     * cantT ->Numero de transiciones de la red de petri. Necesario para establecer la cantidad de semaforos
     */
    private ArrayList<Semaphore> colas = new ArrayList<>();
    private Integer cantT;

    /**
     * Constructor:
     *
     * @param cant Cantidad de transiciones de la rdp
     *             <p>
     *             Crea tantos semaforos como transiciones tenga
     */
    Colas(Integer cant) {

        cantT = cant;

        for (int i = 0; i < cant; i++) {
            colas.add(new Semaphore(0, true));
        }

    }

    /**
     * @return Un ArrayList con un 1 en cada posicion que haya un hilo en esa cola. Un 0 si no hay hilos esperando.
     */
    ArrayList<Integer> quienesEstan() {

        ArrayList<Integer> res = new ArrayList<>();

        for (int j = 0; j < cantT; j++) {
            //Si el metodo devuelve true es porque hay hilos esperando, agrego un 1. De lo contrario agrego un cero
            if (colas.get(j).hasQueuedThreads()) {
                res.add(1);
            } else {
                res.add(0);
            }
        }

        return res;
    }

    /**
     * @param cual Numero de transicion de la cual tengo que liberar un hilo
     *             <p>
     *             El hilo que se esta ejecutando libera al hilo indicado por el parametro
     */
    void release(Integer cual) {

        colas.get(cual).release();

    }

    /**
     * @param cual Transicion que intento disparar el hilo actual
     *             <p>
     *             El hilo que se esta ejecutando entra a la cola de la transicion que intento disparar
     */
    void acquire(Integer cual) {

        try {
            colas.get(cual).acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
