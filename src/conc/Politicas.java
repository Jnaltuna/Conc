package conc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Clase Politicas
 * Implementa la politica tomada para la ejecucion del programa
 */

public class Politicas {

    private ArrayList<ArrayList<Integer>> grupos;
    private ArrayList<ArrayList<Integer>> subprioridadgrupo;
    private Integer grupoactual;

    /**
     * Constructor de la clase.
     *
     * @param dirpol direccion en la cual encuentro el txt con la politica adoptada
     */
    public Politicas(String dirpol) {

        grupos = new ArrayList<>();
        subprioridadgrupo = new ArrayList<>();
        grupoactual = 0;

        cargarpoliticas(dirpol);

        System.out.println(grupos);

    }

    //Recibo el arreglo con los elementos que puedo disparar. Devuelvo el primero
    Integer Cual(ArrayList<Integer> arr) {

        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i) == 1) {
                return i;
            }
        }

        return 0;
    }

    /**
     * @param arr Arreglo con los elementos que puedo disparar
     * @return Entero correspondiente a la transicion que decido disparar
     * Utilizo los grupos que obtengo del txt y en cada llamado de la funcion ingreso a un grupo distinto
     * Dentro del grupo analizo la prioridad de todas las transiciones. Busco la o las transiciones disponibles
     * con la maxima prioridad y disparo una de ellas de forma aleatoria.
     */

    public Integer Cualv2(ArrayList<Integer> arr) {

        ArrayList<Integer> puedodisp = new ArrayList<>();
        boolean valorgrupo = true;

        while (valorgrupo) { //Itero hasta que el arreglo 'puedodisp' tenga algo.

            boolean valorprioridad = true;
            int max = getMaxpriority(subprioridadgrupo.get(grupoactual));   //obtengo la maxima prioridad del grupo actual

            puedodisp.clear();

            while (valorprioridad) { //Itero hasta encontrar una prioridad valida dentro del grupo
                for (int i = 0; i < grupos.get(grupoactual).size(); i++) { //recorro todos los elementos del grupo, buscando los que tengan la prioridad determinada en 'max'

                    if (subprioridadgrupo.get(grupoactual).get(i) == max) {

                        int elem = grupos.get(grupoactual).get(i);

                        if (arr.get(elem) == 1) {   //Analizo el elemento que encontre para ver si lo puedo disparar, segun el arreglo que recibo como parametro
                            puedodisp.add(elem);
                        }
                    }

                }

                if (puedodisp.isEmpty() && max != 0) { //Si la prioridad actual no me da resultado y aun puedo
                    max--;                             //seguir disminuyendo, le resto uno
                } else {
                    valorprioridad = false;
                }

            }

            grupoactual++; //aumento el grupo para la siguiente iteracion o para el proximo hilo.
            if (grupoactual == grupos.size()) {
                grupoactual = 0;
            }

            if (!puedodisp.isEmpty()) { //Cuando el arreglo tiene algo, pongo en false para salir del bucle
                valorgrupo = false;
            }
        }
        //Math.random() devuelve numero aleatorio entre 0.0 y 1.0
        //Lo multiplico por la cantidad de elementos que tiene el arreglo puedodisp
        //Al castear a int me va a devolver el indice de uno de los elementos
        //int random = (int) (Math.random() * (puedodisp.size() - 1));//TODO testing
        Random rand = new Random();
        int random = rand.nextInt( puedodisp.size());

        return puedodisp.get(random); //Disparo una transicion aleatoria dentro de las que puedo disparar

    }

    /**
     * @param prioridad Lista con prioridades del grupo actual.
     * @return valor maximo de prioridad del grupo.
     */
    private Integer getMaxpriority(ArrayList<Integer> prioridad) {

        int max = 0;
        for (Integer elem : prioridad) {
            if (elem > max) {
                max = elem;
            }
        }

        return max;
    }

    /**
     * @param dir Direccion en la cual encuentro el archivo con la politica.
     *            Abro el archivo, tomando los valores separados por un tab ('\t').
     *            En cada linea tengo dos elementos distintos. Grupo y subprioridad de grupo.
     *            Los elementos son separados por el caracter ':', el cual uso para cambiar el arreglo en el cual almaceno los valores.
     */
    private void cargarpoliticas(String dir) {

        try (BufferedReader br = new BufferedReader(new FileReader(dir))) {

            String line;

            int i = 0;

            while ((line = br.readLine()) != null) {

                String[] valores = line.split("\t");

                grupos.add(new ArrayList<>());
                subprioridadgrupo.add(new ArrayList<>());
                int l = 0;

                for (String valore : valores) {

                    if (valore.equals(":")) {
                        l++;
                        continue;
                    }
                    if (l == 0) {
                        grupos.get(i).add(Integer.parseInt(valore));
                    } else if (l == 1) {
                        subprioridadgrupo.get(i).add(Integer.parseInt(valore));
                    }

                }
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Boolean changePolitica(Integer numeroGrupo,ArrayList<Integer> nuevaP) {

        if(nuevaP.size() != subprioridadgrupo.get(numeroGrupo).size()){
            return false;
        }
        for(int i = 0; i<nuevaP.size();i++){
            subprioridadgrupo.get(numeroGrupo).set(i,nuevaP.get(i));
        }
        return true;
    }

    public ArrayList<ArrayList<Integer>> getGrupos() {
        return grupos;
    }


    public ArrayList<ArrayList<Integer>> getSubprioridadgrupo() {
        return subprioridadgrupo;
    }

}

/*
   Tengo un arreglo con distintos grupos.
   En la primera ejecucion, disparo una transicion del primer grupo. Luego incremento el numero de grupo actual
   para ir recorriendo todos los grupos.
   Dentro del disparo de cada grupo tomo en cuenta la prioridad asignada a cada transicion. Busco la prioridad mayor y veo
   si puedo disparar alguna transicion con esa prioridad. De lo contrario, voy disminuyendo la prioridad hasta que encuentre una
   disponible.
   Si no encuentro ninguna en ese grupo, me voy al siguiente.
* */