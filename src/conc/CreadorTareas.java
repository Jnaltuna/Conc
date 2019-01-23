package conc;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

/*
   Clase que implementa la interfaz runnable. En el metodo run se ejecutan las diversas transiciones cada cierto tiempo.
 */

public class CreadorTareas implements Runnable {

    private GestorDeMonitor mon;
    private ArrayList<Integer> disparos;
    private Boolean continuar = true;
    private Integer tiempoentredisp;

    public CreadorTareas(GestorDeMonitor mon,ArrayList<Integer> disparos,Integer time){

        this.mon=mon;
        this.disparos=disparos;
        tiempoentredisp = time;
    }

    public void run(){

        while(continuar){

            for(Integer valor : disparos){

                mon.dispararTransicion(valor);
                try {
                    sleep(tiempoentredisp);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    public void finalizar(){
        this.continuar=false;
    }
}
