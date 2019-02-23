package conc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Clase que implementa lo relacionado con la parte temporal de las redes de petri.
 */
public class timerdp {

    private ArrayList<ArrayList<Integer>> tiempo;
    private ArrayList<Long> timestamps;
    private ArrayList<Boolean> esperando;
    private ArrayList<Long> esperandoid;
    private ArrayList<Integer> oldsens;
    private ArrayList<Long> sleepT;

    /**
     * Constructor de la clase
     *
     * @param dir      Direccion en la cual se encuentra el txt con los intervalos temporales
     * @param vectsens Vector que contiene las transiciones sensibilizadas al iniciar la red
     */
    public timerdp(String dir, ArrayList<Integer> vectsens) {

        tiempo = new ArrayList<>();
        timestamps = new ArrayList<>();
        esperando = new ArrayList<>();
        esperandoid = new ArrayList<>();
        sleepT = new ArrayList<>();

        tiempo = cargartiempo(dir);

        iniciarvect(vectsens);

        oldsens = vectsens;

    }

    /**
     * Metodo que inicializa los atributos de la clase, colocando en ellos los valores necesarios para comenzar.
     *
     * @param vectsens Vector que contiene las transiciones sensibilizadas al iniciar la red
     */
    private void iniciarvect(ArrayList<Integer> vectsens) {

        for (int i = 0; i < tiempo.size(); i++) {

            if (vectsens.get(i) == 1) {
                timestamps.add(System.currentTimeMillis());
            } else {
                timestamps.add(0L);
            }
            esperando.add(false);
            esperandoid.add(null);
            sleepT.add(0L);
        }

    }

    /**
     * Realiza la lecutra del txt y carga los valores en un ArrayList
     *
     * @param dir Direccion en la cual se encuentra el txt
     * @return ArrayList con todos los valores cargados
     */
    private ArrayList<ArrayList<Integer>> cargartiempo(String dir) {

        ArrayList<ArrayList<Integer>> time = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(dir))) {

            String line;

            int i = 0;

            while ((line = br.readLine()) != null) {

                String[] valores = line.split("\t");

                time.add(new ArrayList<>());

                for (String valore : valores) {
                    time.get(i).add(Integer.parseInt(valore));
                }
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return time;
    }

    /**
     * @param disp Numero de transicion que me interesa
     * @return ArrayList con los valores a y b del intervalo temporal [a,b]
     */
    public ArrayList<Integer> valores(Integer disp) {

        ArrayList<Integer> val = new ArrayList<>();

        val.add(tiempo.get(disp).get(0));
        val.add(tiempo.get(disp).get(1));

        return val;
    }

    /**
     * @param disp Numero de transicion que quiero analizar
     * @return True si esta dentro del intervalo o es una transicion sin tiempo. False si esta fuera del intervalo.
     */
    public Boolean testVentanaTiempoo(Integer disp) {

        long dif = System.currentTimeMillis() - timestamps.get(disp);

        if (tiempo.get(disp).get(0) == 0 && tiempo.get(disp).get(1) == 0) { //TODO ver??
            return true;
        }
        //Realizo el calculo para saber si estoy dentro del intervalo
        return dif / 1000 >= tiempo.get(disp).get(0) && dif / 1000 <= tiempo.get(disp).get(1);
    }

    /**
     * Dependiendo del booleano que recibo como parametro, agrego o elimino el timestamp de la transicion
     *
     * @param disp Transicion que estoy disparando
     * @param add  True si quiero agregar un nuevo timestamp, false si lo quiero eliminar
     */
    void setNuevoTimeStamp(Integer disp, boolean add) {

        if (add) {
            timestamps.set(disp, System.currentTimeMillis());
        } else {
            timestamps.set(disp, 0L);
        }

    }

    /**
     * Modifico los timestamps de todas las transiciones. Si la transicion se sensibiliza lo agrego y si deja de estar
     * sensibilizada lo elimino.
     * Almaceno el ArrayList que recibo como argumento para comparar en el proximo llamado
     *
     * @param actsens ArrayList con las transiciones que se sensibilizan producto de un nuevo disparo
     */
    void calctimestamps(ArrayList<Integer> actsens) {
        for (int i = 0; i < actsens.size(); i++) {
            if (actsens.get(i) == 1 && oldsens.get(i) == 0) {
                setNuevoTimeStamp(i, true);
            } else if (actsens.get(i) == 0 && oldsens.get(i) == 1) {
                setNuevoTimeStamp(i, false);
            }
        }
        oldsens.clear();
        oldsens.addAll(actsens);
    }

    /**
     * Verifico si intente disparar la transicion antes de la ventana de tiempo o no.
     *
     * @param disp Numero de transicion que estoy analizando.
     * @return True si estoy antes de la ventana, false de lo contrario.
     */
    public Boolean antesDeLaVentana(Integer disp) {

        long dif = System.currentTimeMillis() - timestamps.get(disp);

        return dif / 1000 < tiempo.get(disp).get(0);

    }

    /**
     * En base al timestamp, el tiempo actual y el valor 'a' del intervalo [a,b], defino el tiempo que debe dormir el hilo.
     * Tiempo en [ms] TODO ver si esta bien
     *
     * @param disp Numero de transicion que estoy analizando
     */
    void setSleepT(Integer disp) {

        long valor;
        if (valores(disp).get(0) == 0 && valores(disp).get(0) == 0) {
            valor = 0L;
        } else {
            long times = timestamps.get(disp) - System.currentTimeMillis();
            valor = times + valores(disp).get(0) * 1000;
        }
        sleepT.set(disp, valor);
    }

    /**
     * Devuelve el tiempo que debe dormir el hilo, eliminandolo de la lista simultaneamente.
     *
     * @param disp Numero de transicion que estoy analizando
     * @return Tiempo en [ms] que debe dormir el hilo. TODO ver si esta bien
     */
    Long getSleepT(Integer disp) {
        Long valor = sleepT.get(disp);
        sleepT.set(disp, 0L);
        return valor;
    }

    /**
     * En el arreglo esperando pongo true para indicar que esta esperando y en el arreglo esperandoid indico la
     * identificacion del hilo que lo puso en la posicion de esperando.
     *
     * @param disp Numero de transicion que estoy analizando
     */
    public void setEsperando(Integer disp) {
        esperando.set(disp, true);
        esperandoid.set(disp, Thread.currentThread().getId());//TODO ver y testear
    }

    /**
     * @param disp Numero de transicion que estoy analizando
     * @return Situacion actual de esperando de la transicion disp
     */
    public Boolean getEsperando(Integer disp) {
        return esperando.get(disp);
    }

    /**
     * @param disp Numero de transicion que estoy analizando
     * @return Identificador del hilo almacenado.
     */
    Long getEsperandoID(Integer disp) {
        return esperandoid.get(disp);
    }

    /**
     * Pone en false el elemento de esperando y en null el de esperandoid correspondiente a la transicion
     *
     * @param disp Numero de transicion que estoy analizando
     */
    public void resetEsperando(Integer disp) {

        esperando.set(disp, false);
        esperandoid.set(disp, null);
    }

    /**
     * @return Devuelve arreglo que contiene los tiempos.
     */
    public ArrayList<ArrayList<Integer>> getred() {
        return tiempo;
    }
}
