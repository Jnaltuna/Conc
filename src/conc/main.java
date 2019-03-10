package conc;

import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Thread.sleep;

public class main {

    public static void main(String[] args) {

        //Creo un monitor

        String dir1 = "C:\\Users\\Altuna\\Desktop\\rdp.txt";
        String dir2 = "C:\\Users\\Altuna\\Desktop\\marca.txt";
        String dir3 = "C:\\Users\\Altuna\\Desktop\\inhibicion.txt";
        String dir4 = "C:\\Users\\Altuna\\Desktop\\hilos.txt";
        String dir5 = "C:\\Users\\Altuna\\Desktop\\politica.txt";
        String dir6 = "C:\\Users\\Altuna\\Desktop\\time.txt";

        String[] direccion = new String[]{dir1, dir2, dir3, dir6};

        GestorDeMonitor mot = new GestorDeMonitor(direccion, dir5);

        ThreadManager manager = new ThreadManager(mot, dir4);

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

    public void setPllenado(GestorDeMonitor mot){
        ArrayList<Integer> nuevaP = new ArrayList<>(Arrays.asList(2,1,1));
        mot.getPol().changePolitica(1,nuevaP);
    }

    public void clearPllenado(GestorDeMonitor mot){
        ArrayList<Integer> nuevaP = new ArrayList<>(Arrays.asList(0,0,0));
        mot.getPol().changePolitica(1,nuevaP);
    }

    public void setPsalida(GestorDeMonitor mot){
        ArrayList<Integer> nuevaP = new ArrayList<>(Arrays.asList(1,2,2,2));
        mot.getPol().changePolitica(4,nuevaP);
    }

    public void clearPsalida(GestorDeMonitor mot){
        ArrayList<Integer> nuevaP = new ArrayList<>(Arrays.asList(0,1,0,1));
        mot.getPol().changePolitica(4,nuevaP);
    }

}
