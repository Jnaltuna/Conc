package conc;

import java.util.ArrayList;

public class GestorDeMonitor {

    /*
     * Atributos de la clase:
     * k   ->variable auxiliar para determinar como continuo
     * mut ->Mutex para que solo entre uno al monitor
     * red ->Red de Petri
     * col ->Colas del sistema
     * pol ->Politicas del sistema
     * m   ->Para det si puedo disparar alguna de las colas
     * */
    private Boolean k;
    private Mutex mut;
    private RdP red;
    private Colas col;
    private Politicas pol;
    private int m; //TODO ver si es int o vector/arreglo
    private Integer cantT = 4;

    /*
     * Constructor:
     * Incializo Mutex, Rdp, Colas(le paso la cantidad de transiciones) y Politicas
     */
    public GestorDeMonitor(String[] direccion,String dirpol) {

        mut = new Mutex();
        red = new RdP(direccion);
        col = new Colas(red.getCantT());
        pol = new Politicas(dirpol);

    }

    /*
     * @param disp Numero de transicion a disparar
     *
     * Metodo principal del Monitor.
     *
     * TODO Ver si se puede separar en varios metodos
     * */
    public void dispararTransicion(Integer disp) {

        //Adquiero el mutex para asegurarme que solo tenga un hilo al mismo tiempo.
        //Establezco k en true para entrar al bucle
        mut.acquire();
        k = true;

        while(k) {

            //Intento disparar la transicion deseada.
            //Dependiendo del resultado hago una u otra cosa
            k = red.disparar(disp);

            //En el caso que haya disparado la transicion
            if(k) { //antes - > k == true

                ArrayList<Integer> VS;
                ArrayList<Integer> VS1;//TODO solo testing
                ArrayList<Integer> VC;

                //VS = red.sensibilizadas(); //Obtengo ArrayList con las transiciones sensibilizadas
                VS= red.calcsens();
                //VS1 = red.calcsens();//TODO solo test
                VC = col.quienesEstan();   //Obtengo ArrayList con los hilos que esperan para disparar una T

                for(int f =0;f<red.getCantT();f++){ //TODO solo test
                    System.out.print(VS.get(f) + " ");
                }
                System.out.println();

                //Inicializo m en 0 para llevar una cuenta
                //Inicializo un ArrayList en el que guardo las transiciones que estan sens y hay hilo esperando
                m=0;
                ArrayList<Integer> arr = new ArrayList<>();

                /*
                 * AND de VS y VC
                 *En arr voy a tener un 1 en el elemento correspondiente a una transicion sensibilizada y que tenga hilo esperando
                 *Un cero si no esta sensibilizada o no hay hilo esperando
                 *Incremento m cuando hay un uno
                 */
                for(int i=0;i<VS.size();i++) {

                    if(VS.get(i)!=0 && VC.get(i)!=0) {
                        m++;
                        arr.add(1);
                    }
                    else {
                        arr.add(0);
                    }
                }

                //Si m es distinto de cero significa que puedo dispara una transicion
                if(m != 0) {
                    //La politica utiliza el arreglo que obtuve para decirme cual disparar
                    int cual = pol.Cual(arr);

                    col.release(cual); //Libero el hilo que tengo que disparar para que entre al monitor nuevamente

                    return; //Hilo actual sale del monitor.

                }
                //Si m es cero no puedo disparar. Pongo k en false para salir del bucle
                else {

                    k=false;

                }
            }
            //En el caso que no haya podido disparar la transicion libero el mutex y entro a una cola
            else {

                mut.release();
                col.acquire(disp);

            }
        }

        //Hilo se va del monitor liberando el mutex
        mut.release();

    }

}
