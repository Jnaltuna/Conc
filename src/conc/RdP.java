package conc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Esta clase implementa la matriz de la red de petri
 * Permite que otras clases la utilizen
 */


public class RdP {
    /**
     * Atributos de la clase:
     * rdp                    -> matriz de incidencia
     * marca				  -> marca inicial/actual
     * cantp y cantT          -> cantidad de plazas y transiciones que tengo
     */
    private ArrayList<ArrayList<Integer>> rdp;
    private ArrayList<Integer> marca;
    private ArrayList<ArrayList<Integer>> inhibicion;
    private ArrayList<Integer> vectSens;
    private Integer cantp;// = 5;
    private Integer cantT;// = 4;
    private timerdp redT;
    private log Log;
    private verifplaza Verifplaza;
    private int VERIFPLAZA = 1; //poner en 1 para verificar inv de plaza

    /**
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

        System.out.println(marca);

        cantp = rdp.get(0).size();
        cantT = rdp.size();

        this.vectSens = calcsens();

        redT = new timerdp(direccion[3], vectSens);

        Log = new log();

        Verifplaza = new verifplaza();

    }

    /**
     * @param disp Numero de transicion que quiero disparar
     * @return true si se disparo correctamente, false de lo contrario
     * <p>
     * Verifico si la transicion esta sensibilizada, tomando en cuenta la marca y las ventanas de tiempo.
     * En el caso de estar sensibilizada, sumo la columna de la matriz de incidencia con la marca actual.
     * Ademas, actualizo el vector de transiciones sensibilizadas y los timestamps.
     */
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

            for (Integer integer : marca) { //TODO Test: Imprimo marca
                System.out.print(integer + " ");
            }
            System.out.println();

            //Verifico invariante de plaza
            if(VERIFPLAZA == 1){
                boolean var = Verifplaza.verificar(marca);
                System.out.println("Verificacion inv plaza " + var);
                if(!var){
                    System.exit(0);
                }
            }

            Log.escribir(Integer.toString(disp));

            redT.resetEsperando(disp);

            vectSens = calcsens();
            redT.calctimestamps(vectSens);

            exito = true;
        } else {
            exito = false;
        }

        return exito;

    }

    /**
     * @param disp Numero de transicion a disparar
     * @return true si puedo disparar, false de lo contrario
     * <p>
     * En este metodo verifico si al momento de disparar la transicion la misma esta sensibilizada y tomo
     * distintas acciones en base a si estoy dentro o fuera de la ventana de tiempo(en el caso de ser una trans con tiempo)
     * Si estoy dentro de la ventana, debo ver si hay otro hilo esperando o no.
     * Si estoy fuera de la ventana, debo verificar si estoy antes o despues.
     */
    private Boolean estaSens(Integer disp) {

        boolean k = false;

        if (vectSens.get(disp) == 1) {

            boolean ventana = redT.testVentanaTiempoo(disp);

            if (ventana) {
                //Si ningun hilo lo seteo en esperando o si fue seteado en esperando y el hilo actual es el que
                //lo seteo previamente
                if (!redT.getEsperando(disp) ||
                        (redT.getEsperando(disp) && redT.getEsperandoID(disp) == Thread.currentThread().getId())) {
                    redT.setNuevoTimeStamp(disp, false);
                    k = true;
                } else {
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

    /**
     * Implemento la multiplicacion de matrices para obtener las transiciones sensibilizadas, teniendo en cuenta
     * la matriz de incidencia, la marca y la matriz de inhibicion
     *
     * @return Vector con las transiciones sensibilizadas
     */
    public ArrayList<Integer> calcsens() {

        ArrayList<Integer> E;
        ArrayList<Integer> B;
        ArrayList<Integer> Ex;

        //Calculo de E
        E = calcE();
        //Calculo de B
        B = calcB();
        //And entre E y B
        Ex = andvectores(E, B);

        return Ex;

    }

    /**
     * Hago el calculo del vector de transiciones sensibilizadas segun la matriz de incidencia
     *
     * @return vector calculado
     */
    public ArrayList<Integer> calcE() {

        ArrayList<Integer> vectE = new ArrayList<>();

        for (int i = 0; i < cantT; i++) { //Supongo inicialmente que estan todas sensibilizadas
            vectE.add(1);
        }

        //Para cada transicion, hago la suma de la marca con la columna correspondiente a esa transicion.
        //Si en alguna suma me da un resultado menor a 0, es porque esa transicion no estaba sensibilizada
        //Pongo el element j de sens en 0
        for (int j = 0; j < cantT; j++) {

            ArrayList<Integer> tmp = new ArrayList<>();

            for (int k = 0; k < cantp; k++) {

                tmp.add(marca.get(k) + rdp.get(j).get(k));

                if (tmp.get(k) < 0) {
                    vectE.set(j, 0);
                }
            }
        }

        return vectE;


    }

    /**
     * Calculo del vector de transiciones des-sensibilizadas por arcos inhibidores
     *
     * @return vector calculado
     */
    public ArrayList<Integer> calcB() {

        ArrayList<Integer> vectB = new ArrayList<>();

        ArrayList<Integer> vectQ = calcQ(); //obtengo vectorQ

        for (int k = 0; k < cantT; k++) {

            vectB.add(0);

            for (int j = 0; j < cantp; j++) {

                int valor = vectB.get(k) + vectQ.get(j) * inhibicion.get(k).get(j);
                vectB.set(k, valor);
                //inhibicion.get(j).get(k);
            }
        }

        for (int n = 0; n < cantT; n++) {
            if (vectB.get(n) == 0) {
                vectB.set(n, 1);
            } else {
                vectB.set(n, 0);
            }
        }

        return vectB;
    }

    /**
     * Calculo del vector que contiene '1' si la plaza correspondiente es cero.
     *
     * @return vector calculado
     */
    public ArrayList<Integer> calcQ() {

        ArrayList<Integer> vectQ = new ArrayList<>();

        for (int i = 0; i < cantp; i++) {
            if (marca.get(i) != 0) {
                vectQ.add(1);
            } else {
                vectQ.add(0);
            }
        }

        return vectQ;
    }

    /**
     * @param a Primer vector que utilizo para hacer AND
     * @param b Segundo vector que utilizo para hacer AND
     * @return AND logico entre ambos vectores recibidos
     */
    private ArrayList<Integer> andvectores(ArrayList<Integer> a, ArrayList<Integer> b) {

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

    /**
     * @param direccion Direccion en la cual se encuentra el archivo con la red
     * @return Red de Petri cargada en un doble ArrayList, en forma de matriz
     * Utiliza la direccion establecida previamente para abrir un archivo
     * En la primera iteracion crea tantos arraylist como columnas tenga
     * Luego va agregando los valores hasta llegar al final del archivo
     */
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

    /**
     * @param direccion2 Direccion en la cual se encuentra el archivo con la marca
     * @return ArrayList que contiene la marca del archivo
     */
    private ArrayList<Integer> cargarM0(String direccion2) {

        ArrayList<Integer> marca = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(direccion2))) {
            String line;
            while ((line = br.readLine()) != null) {

                String[] valores = line.split("\t");

                for (String valore : valores) {
                    marca.add(Integer.parseInt(valore));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return marca;

    }

    /**
     * @return Cantidad de plazas de la red de petri
     */
    public Integer getCantp() {
        return cantp;
    }

    /**
     * @return Cantidad de transiciones de la red de petri
     */
    Integer getCantT() {
        return cantT;
    }

    /**
     * @return Devuelve la red, para testing
     */
    public ArrayList<ArrayList<Integer>> getred() {
        return rdp;
    }

    /**
     * @return Devuelve la marca, para testing
     */
    public ArrayList<Integer> getmarca() {
        return marca;
    }

    /**
     * @param disp Numero de transicion
     * @return Obtiene el tiempo que debe dormir el hilo de la clase timerdp
     */
    Long getSleepT(Integer disp) {
        return redT.getSleepT(disp);
    }

}
