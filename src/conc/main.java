package conc;

import conc.runnables.*;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.Executors;

import static java.lang.Thread.sleep;

public class main {

    public static void main(String[] args) {

        //Creo un monitor

        String dir1 = "C:\\Users\\Altuna\\Desktop\\rdp.txt";
        String dir2 = "C:\\Users\\Altuna\\Desktop\\marca.txt";
        String dir3 = "C:\\Users\\Altuna\\Desktop\\inhibicion.txt";
        String dir4 = "C:\\Users\\Altuna\\Desktop\\hilos.txt";
        String dir5 = "C:\\Users\\Altuna\\Desktop\\politica.txt";

        String[] direccion = new String[] {dir1,dir2,dir3};

        GestorDeMonitor mot = new GestorDeMonitor(direccion,dir5);

        ThreadManager manager = new ThreadManager(mot,dir4);

        manager.crearHilos();
        //manager.iniciarHilos();

        try {
            sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //manager.finalizarHilos();
        //manager.terminarExecutor();

       /* ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newCachedThreadPool();
        Entrada1 ent1 = new Entrada1(mot);
         Entrada2 ent2 = new Entrada2(mot);

        executor.execute(ent1);
        executor.execute(ent2);

        while(true) {
            if (executor.getCompletedTaskCount() == 2) {
                executor.shutdown();
                break;
            }
        }*/

        //Creo e inicializo dos hilos, cada uno tiene distintas tareas

        //Tarea1 tar= new Tarea1(mot);
       // Tarea2 tar2=new Tarea2(mot);
        //Thread thread=new Thread(tar);
        //Thread thread2=new Thread(tar2);

        //thread.start();
        //thread2.start();

        /*for(int i = 0;i<27;i++) {
            System.out.format("%3d",i);
        }
        System.out.println();

        mot.dispararTransicion(0);
        mot.dispararTransicion(18);
        mot.dispararTransicion(3);

        mot.dispararTransicion(6);
        mot.dispararTransicion(8);
        mot.dispararTransicion(10);

        mot.dispararTransicion(11);
        mot.dispararTransicion(12);
        mot.dispararTransicion(14);*/



    }

}


/*
 * TODO Agregar tiempo a la RdP
 * TODO Ver politica
 * */

/* PREGUNTAS
 *
 * Mutex -> fairness?
 * GestorDeMontior -> ver parte de k=false
 * 				   -> col.release() y return estan bien??
 * 				   -> final, cuando salgo del monitor
 * Rdp ->ArrayList?
 * Politicas ->Hacerla en base a la rdp?
 * 			 ->Cargar desde txt?
 * Colas -> Semaforo?
 * 		 -> 1 para cada transicion?
 * Tareas ->Cada hilo hace x tareas?
 *
 * Tiempo ->Que pasa cuando vuelve del sleep?
 * */
