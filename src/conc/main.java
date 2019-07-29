package conc;

import java.util.ArrayList;
import java.util.Arrays;


import static java.lang.Thread.sleep;

public class main {

    @SuppressWarnings("MethodNameSameAsClassName")
    public static void main(String[] args) {

        //Creo un monitor

        Filemanager file = new Filemanager(false);

        GestorDeMonitor mot = new GestorDeMonitor(file);

        ThreadManager manager = new ThreadManager(mot, file.getvalor("hilos"));

        manager.crearHilos();
        manager.iniciarHilos();

        try {
            sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //manager.finalizarHilos();
        manager.terminarExecutor();

        System.exit(0);

    }

    @SuppressWarnings("unused")
    public void setPllenado(GestorDeMonitor mot){
        ArrayList<Integer> nuevaP = new ArrayList<>(Arrays.asList(1,2,1));
        mot.getPol().changePolitica(2,nuevaP);
    }

    @SuppressWarnings("unused")
    public void clearPllenado(GestorDeMonitor mot){
        ArrayList<Integer> nuevaP = new ArrayList<>(Arrays.asList(0,0,0));
        mot.getPol().changePolitica(2,nuevaP);
    }

    @SuppressWarnings("unused")
    public void setPsalida(GestorDeMonitor mot){
        ArrayList<Integer> nuevaP = new ArrayList<>(Arrays.asList(0,1));
        mot.getPol().changePolitica(4,nuevaP);
    }

    @SuppressWarnings("unused")
    public void clearPsalida(GestorDeMonitor mot){
        ArrayList<Integer> nuevaP = new ArrayList<>(Arrays.asList(0,0));
        mot.getPol().changePolitica(4,nuevaP);
    }

}
