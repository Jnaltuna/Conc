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

    private timerdp redcontiempo;
    //private String direccion = "C:\\Users\\Altuna\\Desktop\\rdp.txt";
    //private String direccion2 = "C:\\Users\\Altuna\\Desktop\\marca.txt";

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

        this.rdp = cargarRDP(direccion[0]); //cargo la rdp y la marca inicial de un txt
        this.marca = cargarM0(direccion[1]);
        this.inhibicion = cargarRDP(direccion[2]);


        cantp=rdp.get(0).size();
        cantT=rdp.size();

        this.vectSens = calcsens();

        //redcontiempo = new timerdp("C:\\Users\\Altuna\\Desktop\\time.txt",vectSens); //TODO acomodar para unittests

    }

    /*
     * @param disp Numero de transicion que quiero disparar
     * @return true si se disparo correctamente, false de lo contrario
     *
     * A la marca actual le sumo la columna correspondiente a la transicion que quiero disparar.
     * Valor menor a cero provoca que falle el disparo.
     * */
   /* public Boolean disparar(Integer disp) { //TODO version vieja

        ArrayList<Integer> res = new ArrayList<>();

        for(int i=0;i<cantp;i++) {
            if(inhibicion.get(disp).get(i) == 1){
                if(marca.get(i) > 0){
                    return false;
                }
            }

            res.add(marca.get(i)+rdp.get(disp).get(i));

            if(res.get(i)<0) {
                return false;
            }
        }

        marca.clear();
        marca.addAll(res);

        return true;
    }*/

    public Boolean disparar(Integer disp){

        ArrayList<Integer> res = new ArrayList<>();

        if(vectSens.get(disp) == 1){

            for(int i=0;i<cantp;i++){
                res.add(marca.get(i)+rdp.get(disp).get(i));
            }

            marca.clear();
            marca.addAll(res);

            vectSens = calcsens();

            return true;
        }
        else{
            return false;
        }
    }

    /*
     * No recibe ningun parametro
     * @return ArrayList con un 1 en la posicion cuya transicion se puede disparar
     *  TODO borrar si no lo estoy usando
     * */
    public ArrayList<Integer> sensibilizadas() {

        ArrayList<Integer> sens = new ArrayList<>();

        for(int i=0;i<cantT;i++) { //Supongo inicialmente que estan todas sensibilizadas
            sens.add(1);
        }

        //Para cada transicion, hago la suma de la marca con la columna correspondiente a esa transicion.
        //Si en alguna suma me da un resultado menor a 0, es porque esa transicion no estaba sensibilizada
        //Pongo el element j de sens en 0
        for(int j=0;j<cantT;j++) {

            ArrayList<Integer> tmp = new ArrayList<>();

            for(int k=0;k<cantp;k++) {

                    tmp.add(marca.get(k) + rdp.get(j).get(k));

                    if (tmp.get(k) < 0) {
                        sens.set(j, 0);
                    }
                    if(inhibicion.get(j).get(k) == 1 && marca.get(k) > 0){
                        sens.set(j,0);
                    }
            }
        }

        return sens;
    }

    public ArrayList<Integer> calcsens(){

        ArrayList<Integer> E;// = new ArrayList<>();
        ArrayList<Integer> B;// = new ArrayList<>();
        ArrayList<Integer> Ex;

            //Calculo de E
        E = calcE();
            //Calculo de B
        B = calcB();
            //And entre E y B
        Ex = andvectores(E,B);

        return Ex;

    }

    public ArrayList<Integer> calcE(){ //TODO cambiar nombre

        ArrayList<Integer> vectE = new ArrayList<>();

        for(int i=0;i<cantT;i++) { //Supongo inicialmente que estan todas sensibilizadas
            vectE.add(1);
        }

        //Para cada transicion, hago la suma de la marca con la columna correspondiente a esa transicion.
        //Si en alguna suma me da un resultado menor a 0, es porque esa transicion no estaba sensibilizada
        //Pongo el element j de sens en 0
        for(int j=0;j<cantT;j++) {

            ArrayList<Integer> tmp = new ArrayList<>();

            for(int k=0;k<cantp;k++) { // TODO hacer una funcion que calcule esto

                tmp.add(marca.get(k) + rdp.get(j).get(k));

                if (tmp.get(k) < 0) {
                    vectE.set(j, 0);
                }
            }
        }

        return vectE;


    }

    //TODO comentar y hacer test
    public ArrayList<Integer> calcB(){ //TODO cambiar nombre

        ArrayList<Integer> vectB = new ArrayList<>();

        ArrayList<Integer> vectQ = calcQ(); //obtengo vectorQ

        for(int k=0;k<cantT;k++) {

            ArrayList<Integer> tmp = new ArrayList<>();

            vectB.add(0);

            for(int j=0;j<cantp;j++) { //

                int valor = vectB.get(k)+vectQ.get(j)*inhibicion.get(k).get(j);
                vectB.set(k,valor);
                //inhibicion.get(j).get(k);
            }
        }

        for(int n=0;n<cantT;n++){ //TODO ver bien si da valores binarios
            if(vectB.get(n) == 0){//TODO
                vectB.set(n,1);   //TODO
            }
            else{
                vectB.set(n,0);
            }
        }

        return vectB;
    }

    public ArrayList<Integer> calcQ(){ //TODO HACER TEST

        ArrayList<Integer> vectQ = new ArrayList<>();

        for(int i=0;i<cantp;i++){ //TODO revisar si esta bien Q
            if(marca.get(i) !=0){
                vectQ.add(1);
            }
            else{
                vectQ.add(0);
            }
        }

        return  vectQ;
    }

    public ArrayList<Integer> andvectores(ArrayList<Integer> a,ArrayList<Integer> b){ //TODO hacer test

        ArrayList<Integer> resultado = new ArrayList<>();

        for(int i = 0;i<cantT;i++){
            if(a.get(i) == 1 && b.get(i) == 1){
                resultado.add(1);
            }
            else{
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
            boolean first=true;
            while ((line = br.readLine()) != null) {

                String[] valores = line.split("\t");

                if(first) {
                    for(int i=0;i<valores.length;i++) {
                        rdp.add(new ArrayList<>());
                    }
                    first=false;
                }

                for(int j=0;j<valores.length;j++) {
                    rdp.get(j).add(Integer.parseInt(valores[j]));
                }
            }
        }
        catch (IOException e) {
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

                for(int j=0;j<valores.length;j++) {
                    marca.add(Integer.parseInt(valores[j]));
                }
            }
        }
        catch (IOException e) {
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
    public Integer getCantT() {
        return cantT;
    }

    public ArrayList<ArrayList<Integer>> getred(){
        return rdp;
    }

    public ArrayList<Integer> getmarca(){
        return marca;
    }

}
