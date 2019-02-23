package conc;

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
        manager.iniciarHilos(); //TODO iniciar los hilos

        try {
            sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //manager.finalizarHilos();
        manager.terminarExecutor();

        System.exit(0);

    }

}
