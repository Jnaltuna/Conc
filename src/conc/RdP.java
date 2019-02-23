package conc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/*
 * Esta clase implementa la matriz de la red de petri
 * Permite que otras clases la utilizen
 *
 */


public class RdP {
    /*
     * Atributos de la clase:
     * rdp                    -> matriz de incidencia
     * marca				  -> marca inicial/actual
     * cantp y cantT          -> cantidad de plazas y transiciones que tengo
     *
     * */
    private ArrayList<ArrayList<Integer>> rdp;
    private ArrayList<Integer> marca;
    private ArrayList<ArrayList<Integer>> inhibicion;
    private ArrayList<Integer> vectSens;
    private Integer cantp;// = 5;
    private Integer cantT;// = 4;
    private timerdp redT;

    /*
     * Constructor.
     * Inicializo rdp y marca.
     * Almaceno la rdp con ArrayList. Cada elemento de 'rdp' es una columna de la rdp.
     * Almaceno la marca inicial.
     * Obtengo la cantidad de plazas y transiciones.
     */
    public RdP(String[] direccion) {

        rdp = new ArrayList<>();
        marca = new ArrayList<>();

        //Cargo la rdp,marca y matriz de inhibicion desde txt.
        this.rdp = cargarRDP(direccion[0]);
        this.marca = cargarM0(direccion[1]);
        this.inhibicion = cargarRDP(direccion[2]);

        cantp = rdp.get(0).size();
        cantT = rdp.size();

        this.vectSens = calcsens();

        redT = new timerdp(direccion[3], vectSens);

    }

    /*
     * @param disp Numero de transicion que quiero disparar
     * @return true si se disparo correctamente, false de lo contrario
     *
     * Verifico si la transicion esta sensibilizada, tomando en cuenta la marca y las ventanas de tiempo.
     * En el caso de estar sensibilizada, sumo la columna de la matriz de incidencia con la marca actual.
     * Ademas, actualizo el vector de transiciones sensibilizadas y los timestamps.
     * */
    public Boolean disparar(Integer disp) {

        ArrayList<Integer> res = new ArrayList<>();

        boolean k = estaSens(disp);
        boolean exito;

        if (k) {

            for (int i = 0; i < cantp; i++) {
                res.add(marca.get(i) + rdp.get(disp).get(i));
            }
            marca.clear();
            marca.addAll(res);

            redT.resetEsperando(disp);//TODO ver si esta bien aca

            vectSens = calcsens();
            redT.calctimestamps(vectSens);

            exito = true;
        } else {
            exito = false;
        }

        return exito;

    }

    private Boolean estaSens(Integer disp) {

        boolean k = false;

        if (vectSens.get(disp) == 1) {

            boolean ventana = redT.testVentanaTiempoo(disp);

            if (ventana) {
                //TODO revisar logica!!
                if (!redT.getEsperando(disp) ||
                        (redT.getEsperando(disp) && redT.getEsperandoID(disp) == Thread.currentThread().getId())) {
                    redT.setNuevoTimeStamp(disp, false);
                    k = true;
                } else {//TODO ver si hace falta un else if
                    k = false;
                }
            } else {
                boolean antes = redT.antesDeLaVentana(disp);

                if (antes) {
                    redT.setEsperando(disp);
                    redT.setSleepT(disp);
                    k = false;
                } else {
                    k = false;
                }
            }
        }

        return k;

    }

    /*
     * No recibe ningun parametro
     * @return ArrayList con un 1 en la posicion cuya transicion se puede disparar
     *  TODO borrar si no lo estoy usando
     * */
    public ArrayList<Integer> sensibilizadas() {

        ArrayList<Integer> sens = new ArrayList<>();

        for (int i = 0; i < cantT; i++) { //Supongo inicialmente que estan todas sensibilizadas
            sens.add(1);
        }

        //Para cada transicion, hago la suma de la marca con la columna correspondiente a esa transicion.
        //Si en alguna suma me da un resultado menor a 0, es porque esa transicion no estaba sensibilizada
        //Pongo el element j de sens en 0
        for (int j = 0; j < cantT; j++) {

            ArrayList<Integer> tmp = new ArrayList<>();

            for (int k = 0; k < cantp; k++) {

                tmp.add(marca.get(k) + rdp.get(j).get(k));

                if (tmp.get(k) < 0) {
                    sens.set(j, 0);
                }
                if (inhibicion.get(j).get(k) == 1 && marca.get(k) > 0) {
                    sens.set(j, 0);
                }
            }
        }

        return sens;
    }

    //public
    ArrayList<Integer> calcsens() {

        ArrayList<Integer> E;// = new ArrayList<>();
        ArrayList<Integer> B;// = new ArrayList<>();
        ArrayList<Integer> Ex;

        //Calculo de E
        E = calcE();
        //Calculo de B
        B = calcB();
        //And entre E y B
        Ex = andvectores(E, B);

        return Ex;

    }

    public ArrayList<Integer> calcE() { //TODO cambiar nombre

        ArrayList<Integer> vectE = new ArrayList<>();

        for (int i = 0; i < cantT; i++) { //Supongo inicialmente que estan todas sensibilizadas
            vectE.add(1);
        }

        //Para cada transicion, hago la suma de la marca con la columna correspondiente a esa transicion.
        //Si en alguna suma me da un resultado menor a 0, es porque esa transicion no estaba sensibilizada
        //Pongo el element j de sens en 0
        for (int j = 0; j < cantT; j++) {

            ArrayList<Integer> tmp = new ArrayList<>();

            for (int k = 0; k < cantp; k++) { // TODO hacer una funcion que calcule esto

                tmp.add(marca.get(k) + rdp.get(j).get(k));

                if (tmp.get(k) < 0) {
                    vectE.set(j, 0);
                }
            }
        }

        return vectE;


    }

    //TODO comentar y hacer test
    public ArrayList<Integer> calcB() { //TODO cambiar nombre

        ArrayList<Integer> vectB = new ArrayList<>();

        ArrayList<Integer> vectQ = calcQ(); //obtengo vectorQ

        for (int k = 0; k < cantT; k++) {

            ArrayList<Integer> tmp = new ArrayList<>();

            vectB.add(0);

            for (int j = 0; j < cantp; j++) { //

                int valor = vectB.get(k) + vectQ.get(j) * inhibicion.get(k).get(j);
                vectB.set(k, valor);
                //inhibicion.get(j).get(k);
            }
        }

        for (int n = 0; n < cantT; n++) { //TODO ver bien si da valores binarios
            if (vectB.get(n) == 0) {//TODO
                vectB.set(n, 1);   //TODO
            } else {
                vectB.set(n, 0);
            }
        }

        return vectB;
    }

    public ArrayList<Integer> calcQ() { //TODO HACER TEST

        ArrayList<Integer> vectQ = new ArrayList<>();

        for (int i = 0; i < cantp; i++) { //TODO revisar si esta bien Q
            if (marca.get(i) != 0) {
                vectQ.add(1);
            } else {
                vectQ.add(0);
            }
        }

        return vectQ;
    }

    private ArrayList<Integer> andvectores(ArrayList<Integer> a, ArrayList<Integer> b) { //TODO hacer test

        ArrayList<Integer> resultado = new ArrayList<>();

        for (int i = 0; i < cantT; i++) {
            if (a.get(i) == 1 && b.get(i) == 1) {
                resultado.add(1);
            } else {
                resultado.add(0);
            }
        }

        return resultado;
    }

    /*
     * Utiliza la direccion establecida previamente para abrir un archivo
     * En la primera iteracion crea tantos arraylist como columnas tenga
     * Luego va agregando los valores hasta llegar al final del archivo
     * */
    private ArrayList<ArrayList<Integer>> cargarRDP(String direccion) {

        ArrayList<ArrayList<Integer>> rdp = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(direccion))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {

                String[] valores = line.split("\t");

                if (first) {
                    for (int i = 0; i < valores.length; i++) {
                        rdp.add(new ArrayList<>());
                    }
                    first = false;
                }

                for (int j = 0; j < valores.length; j++) {
                    rdp.get(j).add(Integer.parseInt(valores[j]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rdp;

    }

    /*
     * Toma direccion2 establecida previamente
     * Carga los valores en un arraylist
     * */
    private ArrayList<Integer> cargarM0(String direccion2) {

        ArrayList<Integer> marca = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(direccion2))) {
            String line;
            while ((line = br.readLine()) != null) {

                String[] valores = line.split("\t");

                for (int j = 0; j < valores.length; j++) {
                    marca.add(Integer.parseInt(valores[j]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return marca;

    }

    /*
     *  Devuelve la cantidad de plazas de la red de petri
     * */
    public Integer getCantp() {
        return cantp;
    }

    /*
     * Devuelve la cantidad de transiciones de la red de petri
     * */
    //public
    Integer getCantT() {
        return cantT;
    }

    public ArrayList<ArrayList<Integer>> getred() {
        return rdp;
    }

    public ArrayList<Integer> getmarca() {
        return marca;
    }

    public Long getSleepT(Integer disp) {
        return redT.getSleepT(disp); //TODO ver!!!
    }

}
