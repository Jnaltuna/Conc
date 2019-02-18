package conc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


/*
* Esta clase crea los objetos runnable necesarios y los envia a un thread executor para que sean ejecutados.
* Lee de un txt que tareas ejecuta cada hilo, lo crea y lo ejecuta.
*
* */



public class ThreadManager {

    private ThreadPoolExecutor executor;
    private ArrayList<ArrayList<Integer>> tareashilos;
    private GestorDeMonitor mon;
    private Integer tiempoentredisp = 100; //TODO ver si hace falta el tiempo entre disparos
    private ArrayList<CreadorTareas> threadlist;

    public ThreadManager(GestorDeMonitor mon,String dirhilos){

        this.mon = mon;
        tareashilos = cargarconfig(dirhilos);
        threadlist = new ArrayList<>();
        executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();

    }

    private ArrayList<ArrayList<Integer>> cargarconfig(String dirhilos){

        ArrayList<ArrayList<Integer>> tareashilos = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(dirhilos))) {

            String line;

            int i = 0;

            while ((line = br.readLine()) != null) {

                String[] valores = line.split("\t");

                tareashilos.add(new ArrayList<>());

                for(int j=0;j<valores.length;j++) {
                    tareashilos.get(i).add(Integer.parseInt(valores[j]));
                }
                i++;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return tareashilos;
    }

    //public
    void crearHilos(){

        for(ArrayList<Integer> tareas : tareashilos){
            threadlist.add(new CreadorTareas(mon,tareas,tiempoentredisp));
        }
    }

    public void iniciarHilos(){

        for(CreadorTareas hilo : threadlist){
            executor.execute(hilo);
        }
    }

    public void finalizarHilos(){

        for(CreadorTareas hilo :threadlist){
            hilo.finalizar();
        }
    }

    public void terminarExecutor(){
        executor.shutdownNow();
        /*while(true){
            if (executor.getCompletedTaskCount() == threadlist.size()) {
                executor.shutdown();
                break;
            }
        }*/
    }

}
