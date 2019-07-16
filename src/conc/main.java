package conc;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;

import java.io.IOException;
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

        Filemanager file = new Filemanager(false);

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
        ArrayList<Integer> nuevaP = new ArrayList<>(Arrays.asList(1,2,1));
        mot.getPol().changePolitica(2,nuevaP);
    }

    public void clearPllenado(GestorDeMonitor mot){
        ArrayList<Integer> nuevaP = new ArrayList<>(Arrays.asList(0,0,0));
        mot.getPol().changePolitica(2,nuevaP);
    }

    public void setPsalida(GestorDeMonitor mot){
        ArrayList<Integer> nuevaP = new ArrayList<>(Arrays.asList(0,1));
        mot.getPol().changePolitica(4,nuevaP);
    }

    public void clearPsalida(GestorDeMonitor mot){
        ArrayList<Integer> nuevaP = new ArrayList<>(Arrays.asList(0,0));
        mot.getPol().changePolitica(4,nuevaP);
    }

}
