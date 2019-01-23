package conc;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/*
 * Clase Colas
 * Implementa las colas necesarias para el disparo de transiciones utilizando el monitor
 *
 * TODO Con semaforos?
 *
 * */
public class Colas {

    /*
     * Parametros
     * colas ->ArrayList de semaforos. Un semaforo por cada transicion
     * cantT ->Numero de transiciones de la red de petri. Necesario para establecer la cantidad de semaforos
     * */
    private ArrayList<Semaphore> colas = new ArrayList<Semaphore>();
    private Integer cantT;

    /*
     * Constructor:
     * @param cant Cantidad de transiciones de la rdp
     *
     * Crea tantos semaforos como transiciones tenga
     * */
    public Colas(Integer cant) {

        cantT=cant;

        for(int i=0 ; i<cant ; i++) {
            colas.add(new Semaphore(0,true)); //TODO fairness?
        }

    }

    /*
     * @return Un ArrayList con un 1 en cada posicion que haya un hilo en esa cola. Un 0 si no hay hilos esperando.
     *
     * */
    public ArrayList<Integer> quienesEstan() {

        ArrayList<Integer> res = new ArrayList<Integer>();

        for(int j=0;j<cantT;j++) {
            //Si el metodo devuelve true es porque hay hilos esperando, agrego un 1. De lo contrario agrego un cero
            if(colas.get(j).hasQueuedThreads()) {
                res.add(1);
            } else {
                res.add(0);
            }
        }

        return res;
    }

    /*
     * @param cual Numero de transicion de la cual tengo que liberar un hilo
     *
     * El hilo que se esta ejecutando libera al hilo indicado por el parametro
     */
    public void release(Integer cual) {

        colas.get(cual).release();

    }

    /*
     * @param cual Transicion que intento disparar el hilo actual
     *
     * El hilo que se esta ejecutando entra a la cola de la transicion que intento disparar
     * */
    public void acquire(Integer cual) {

        try {
            colas.get(cual).acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
