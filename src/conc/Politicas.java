package conc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/*
 * Clase Politicas
 * Implementa la politica tomada para la ejecucion del programa
 * */

public class Politicas {

    private ArrayList<ArrayList<Integer>> grupos;
    private ArrayList<Integer> prioridadgrupos;
    private ArrayList<ArrayList<Integer>> subprioridadgrupo;
    private Integer grupoactual;

    /*
    Constructor de la clase.
    @param dirpol direccion en la cual encuentro el txt con la politica adoptada
     */
    public Politicas(String dirpol) {

        grupos = new ArrayList<>();
        prioridadgrupos = new ArrayList<>();
        subprioridadgrupo = new ArrayList<>();
        grupoactual = 0;

        cargarpoliticas(dirpol);

        //System.out.println(grupos);
        //System.out.println(prioridadgrupos);
        //System.out.println(subprioridadgrupo);

    }

    //Recibo el arreglo con los elementos que puedo disparar. Devuelvo el primero
    //TODO Borrar, implementar que la rdp use Cualv2
    //public
    Integer Cual(ArrayList<Integer> arr) {

        for(int i=0;i<arr.size();i++) {
            if(arr.get(i)==1) {
                return i;
            }
        }

        return 0; //TODO ????
    }

    /*
    @param arr Arreglo con los elementos que puedo disparar
    @return Entero correspondiente a la transicion que decido disparar
    Utilizo los grupos que obtengo del txt y en cada llamado de la funcion ingreso a un grupo distinto
    Dentro del grupo analizo la prioridad de todas las transiciones. Busco la o las transiciones disponibles
    con la maxima prioridad y disparo una de ellas de forma aleatoria.
     */

    public Integer Cualv2(ArrayList<Integer> arr){

        ArrayList<Integer> puedodisp = new ArrayList<>();
        boolean valorgrupo = true;
        //boolean valorprioridad = true;

        while(valorgrupo){

            boolean valorprioridad = true;
            int max = getMaxpriority(subprioridadgrupo.get(grupoactual));

            puedodisp.clear();

            while(valorprioridad) {
                for (int i = 0; i < grupos.get(grupoactual).size(); i++) {

                    if (subprioridadgrupo.get(grupoactual).get(i) == max) {

                        int elem = grupos.get(grupoactual).get(i);

                        if (arr.get(elem) == 1) {
                            puedodisp.add(elem);
                        }
                    }

                }

                if(puedodisp.isEmpty() && max!=0){
                    max--;
                }
                else{
                    valorprioridad=false;
                }

            }

            grupoactual++;

            if(!puedodisp.isEmpty()){
                valorgrupo = false;
            }
        }

        int random = (int) (Math.random()*(puedodisp.size()- 1));

        return puedodisp.get(random);

    }

    /*
    @param prioridad Lista con prioridades del grupo actual.
    @return valor maximo de prioridad del grupo.
     */
    private Integer getMaxpriority(ArrayList<Integer> prioridad){

        int max = 0;
        for(Integer elem : prioridad){
            if(elem > max)
            {
                max = elem;
            }
        }

        return max;
    }

    /*
    @param dir Direccion en la cual encuentro el archivo con la politica.
    Abro el archivo, tomando los valores separados por un tab ('\t').
    En cada linea tengo tres elementos distintos. Grupo, prioridad de grupo y subprioridad de grupo.
    Los elementos son separados por el caracter ':', el cual uso para cambiar el arreglo en el cual almaceno los valores.
    TODO ver que hacer con prioridad de grupo.
     */
    private void cargarpoliticas(String dir){

        try (BufferedReader br = new BufferedReader(new FileReader(dir))) {

            String line;

            int i = 0;

            while ((line = br.readLine()) != null) {

                String[] valores = line.split("\t");

                grupos.add(new ArrayList<>());
                subprioridadgrupo.add(new ArrayList<>());
                int l = 0;

                for(int j=0;j<valores.length;j++) {

                    if(valores[j].equals(":")){
                        l++;
                        continue;
                    }
                    if(l == 0){
                        grupos.get(i).add(Integer.parseInt(valores[j]));
                    }
                    else if(l == 1){
                        prioridadgrupos.add(Integer.parseInt(valores[j]));
                    }
                    else if(l == 2){
                        subprioridadgrupo.get(i).add(Integer.parseInt(valores[j]));
                    }

                }
                i++;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ArrayList<Integer>> getGrupos() {
        return grupos;
    }

    public ArrayList<Integer> getPrioridadgrupos() {
        return prioridadgrupos;
    }

    public ArrayList<ArrayList<Integer>> getSubprioridadgrupo() {
        return subprioridadgrupo;
    }

}

/* TODO Explicacion politica
   Tengo un arreglo con distintos grupos.
   En la primera ejecucion, disparo una transicion del primer grupo. Luego incremento el numero de grupo actual
   para ir recorriendo todos los grupos.
   Dentro del disparo de cada grupo tomo en cuenta la prioridad asignada a cada transicion. Busco la prioridad mayor y veo
   si puedo disparar alguna transicion con esa prioridad. De lo contrario, voy disminuyendo la prioridad hasta que encuentre una
   disponible.
   Si no encuentro ninguna en ese grupo, me voy al siguiente.
* */