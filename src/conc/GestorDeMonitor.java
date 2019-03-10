package conc;

import java.util.ArrayList;
import java.sql.Timestamp;

import static java.lang.Thread.sleep;

public class GestorDeMonitor {

    /**
     * Atributos de la clase:
     * k   ->variable auxiliar para determinar como continuo
     * mut ->Mutex para que solo entre uno al monitor
     * red ->Red de Petri
     * col ->Colas del sistema
     * pol ->Politicas del sistema
     * m   ->Para det si puedo disparar alguna de las colas
     */
    private Boolean k;
    private Mutex mut;
    private RdP red;
    private Colas col;
    private Politicas pol;
    private int m;

    /**
     * Constructor:
     * Incializo Mutex, Rdp, Colas(le paso la cantidad de transiciones) y Politicas
     */
    public GestorDeMonitor(String[] direccion, String dirpol) {

        mut = new Mutex();
        red = new RdP(direccion);
        col = new Colas(red.getCantT());
        pol = new Politicas(dirpol);

    }

    /**
     * @param disp Numero de transicion a disparar
     *             <p>
     *             Metodo principal del Monitor.
     */
    public void dispararTransicion(Integer disp) {

        //Adquiero el mutex para asegurarme que solo tenga un hilo al mismo tiempo.
        //Establezco k en true para entrar al bucle
        mut.acquire();
        k = true;

        //TODO para test: System.out.println("El hilo " + Thread.currentThread().getId() + " ingresa al monitor, dispara " + disp+ " " + new Timestamp(System.currentTimeMillis()));

        while (k) {

            //Intento disparar la transicion deseada.
            //Dependiendo del resultado hago una u otra cosa

            k = red.disparar(disp);

            //En el caso que haya disparado la transicion
            if (k) {

                //TODO para test:
                System.out.println("Transicion " + disp + " exitosa " + new Timestamp(System.currentTimeMillis()));

                ArrayList<Integer> VS;
                ArrayList<Integer> VC;

                VS = red.calcsens();        //Obtengo ArrayList con las transiciones sensibilizadas
                VC = col.quienesEstan();   //Obtengo ArrayList con los hilos que esperan para disparar una T

                for (int f = 0; f < red.getCantT(); f++) { //TODO Test: Imprimo transiciones sensibilizadas
                    System.out.print(VS.get(f) + " ");
                }
                System.out.println();

                //Inicializo m en 0 para llevar una cuenta
                //Inicializo un ArrayList en el que guardo las transiciones que estan sens y hay hilo esperando
                m = 0;
                ArrayList<Integer> arr = new ArrayList<>();

                /*
                 * AND de VS y VC
                 *En arr voy a tener un 1 en el elemento correspondiente a una transicion sensibilizada y que tenga hilo esperando
                 *Un cero si no esta sensibilizada o no hay hilo esperando
                 *Incremento m cuando hay un uno
                 */
                for (int i = 0; i < VS.size(); i++) {

                    if (VS.get(i) != 0 && VC.get(i) != 0) {
                        m++;
                        arr.add(1);
                    } else {
                        arr.add(0);
                    }
                }

                //Si m es distinto de cero significa que puedo dispara una transicion
                if (m != 0) {
                    //La politica utiliza el arreglo que obtuve para decirme cual disparar
                    //int cual = pol.Cual(arr);
                    int cual = pol.Cualv2(arr); //TODO ver si anda bien

                    col.release(cual); //Libero el hilo que tengo que disparar para que entre al monitor nuevamente

                    return; //Hilo actual sale del monitor.

                }
                //Si m es cero no puedo disparar. Pongo k en false para salir del bucle
                else {

                    k = false;

                }
            } else { //Entro en el caso que no se dispare la transicion

                //Libero el mutex y obtengo el tiempo que deberia dormir el hilo
                mut.release();
                Long tiempo = red.getSleepT(disp);

                if (tiempo == 0) {//En el caso que el tiempo sea 0, ess una transicion sin tiempo o no sens por lo que entra a la cola
                    col.acquire(disp);
                } else {
                    //En el caso que sea distinto de 0, duermo ese tiempo y luego intento ingresar nuevamente al monitor
                    //TODO para test :System.out.println("Duermo " + tiempo + " tiempo al disparar " + disp);
                    try {
                        sleep(tiempo);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mut.acquire();
                    k = true;
                }

            }
        }

        //Hilo se va del monitor liberando el mutex
        mut.release();

    }

    Politicas getPol(){
        return pol;
    }

}
