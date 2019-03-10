package conc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * Esta clase crea los objetos runnable necesarios y los envia a un thread executor para que sean ejecutados.
 * Lee de un txt que tareas ejecuta cada hilo, lo crea y lo ejecuta.
 */
public class ThreadManager {

    private ThreadPoolExecutor executor;
    private ArrayList<ArrayList<Integer>> tareashilos;
    private GestorDeMonitor mon;
    private Integer tiempoentredisp = 100; //TODO ver si hace falta el tiempo entre disparos
    private ArrayList<CreadorTareas> threadlist;

    /**
     * Constructor
     *
     * @param mon      Monitor a utilizar
     * @param dirhilos Direccion en la cual se encuentran todos los txt
     */
    public ThreadManager(GestorDeMonitor mon, String dirhilos) {

        this.mon = mon;
        tareashilos = cargarconfig(dirhilos);
        threadlist = new ArrayList<>();
        executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();

    }

    /**
     * Leo de un txt los numeros correspondientes a las transiciones que debe disparar cada hilo
     *
     * @param dirhilos Direccion en la cual encuentro la configuracion
     * @return Doble arraylist con los valores cargados
     */
    private ArrayList<ArrayList<Integer>> cargarconfig(String dirhilos) {

        ArrayList<ArrayList<Integer>> tareashilos = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(dirhilos))) {

            String line;

            int i = 0;

            while ((line = br.readLine()) != null) {

                String[] valores = line.split("\t");

                tareashilos.add(new ArrayList<>());

                for (String valore : valores) {
                    tareashilos.get(i).add(Integer.parseInt(valore));
                }
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tareashilos;
    }

    /**
     * Creo los objetos runnable con la clase CreadorTareas, pasandole como parametro el monitor, las tareas que debe realizar y el tiempo entre disparos
     * Los hilos creados los almaceno en un arraylist
     */
    void crearHilos() {

        for (ArrayList<Integer> tareas : tareashilos) {
            threadlist.add(new CreadorTareas(mon, tareas, tiempoentredisp));
        }
    }

    /**
     * Tomo los elementos del ArrayList con los objetos runnable y se los envio al executor para que cree los hilos y lo ejecute
     */
    void iniciarHilos() {

        for (CreadorTareas hilo : threadlist) {
            executor.execute(hilo);
        }
    }

    /**
     * Llama al metodo finalizar de cada hilo que se encuentra en el arraylist
     */
    public void finalizarHilos() {

        for (CreadorTareas hilo : threadlist) {
            hilo.finalizar();
        }
    }

    /**
     * Finalizo el executor en el caso que hayan terminado todas las tareas.
     */
    void terminarExecutor() {
        //executor.shutdownNow();
        while (true) {
            if (executor.getCompletedTaskCount() == threadlist.size()) {
                executor.shutdown();
                break;
            }
        }
    }

}
