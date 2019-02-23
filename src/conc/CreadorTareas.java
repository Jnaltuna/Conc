package conc;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

/**
 * Clase que implementa la interfaz runnable. En el metodo run se ejecutan las diversas transiciones cada cierto tiempo.
 */
public class CreadorTareas implements Runnable {

    private GestorDeMonitor mon;
    private ArrayList<Integer> disparos;
    private Boolean continuar = true;
    private Integer tiempoentredisp;

    /**
     * Constructor de la clase.
     *
     * @param mon      El monitor a utilizar por el hilo
     * @param disparos Lista con las transiciones que debe disparar el hilo
     * @param time     Tiempo entre disparos
     */
    public CreadorTareas(GestorDeMonitor mon, ArrayList<Integer> disparos, Integer time) {

        this.mon = mon;
        this.disparos = disparos;
        tiempoentredisp = time;
    }

    /**
     * Mientras el atributo del objeto sea true, ejecuta secuencialmente las transiciones que recibio en el constructor.
     * Duerme entre disparos un cierto tiempo.
     */
    public void run() {

        while (continuar) {

            for (Integer valor : disparos) {

                mon.dispararTransicion(valor);
                try {
                    sleep(tiempoentredisp);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Al llamar a este metodo, modifico el atributo del objeto impidiendo que se disparen nuevas transiciones.
     */
    void finalizar() {
        this.continuar = false;
    }
}
