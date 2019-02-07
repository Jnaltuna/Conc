package conc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/*
 * Clase Politicas
 * Implementa la politica tomada para la ejecucion del programa
 * TODO

 * */

public class Politicas {

    /* TODO revisar*/
    private ArrayList<ArrayList<Integer>> grupos;
    private ArrayList<Integer> prioridadgrupos;
    private ArrayList<ArrayList<Integer>> subprioridadgrupo;
    private Integer grupoactual;

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
    public Integer Cual(ArrayList<Integer> arr) {

        for(int i=0;i<arr.size();i++) {
            if(arr.get(i)==1) {
                return i;
            }
        }

        return 0; //TODO ????
    }

    public Integer Cualv2(ArrayList<Integer> arr){

        ArrayList<Integer> puedodisp = new ArrayList<>();
        boolean valor = true;

        while(valor){

            int max = getMaxpriority(subprioridadgrupo.get(grupoactual));

            puedodisp.clear();

            for(int i = 0 ; i<grupos.get(grupoactual).size();i++){

                if(subprioridadgrupo.get(grupoactual).get(i) == max){

                    int elem = grupos.get(grupoactual).get(i);

                    if (arr.get(elem) == 1){
                        puedodisp.add(elem);
                    }
                }

            }

            grupoactual++;

            if(!puedodisp.isEmpty()){
                valor = false;
            }
        }

        int random = (int) (Math.random()*(puedodisp.size()- 1));

        return puedodisp.get(random);

    }

    public Integer getMaxpriority(ArrayList<Integer> prioridad){

        int max = 0;
        for(Integer elem : prioridad){
            if(elem > max)
            {
                max = elem;
            }
        }

        return max;
    }

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

/* TODO idea para politica
*
* Crear un txt en el cual en cada linea tenga un grupo de tareas.
* Asignar a cada grupo una prioridad. Cada tarea dentro del grupo tiene una subprioridad.
* Al momento de elegir una transicion, elijo el grupo con mayor prioridad.
* Si todos los grupos disponibles tienen la misma prioridad, elijo uno al azar.
* Dentro de cada grupo, disparo la transicion con mayor prioridad.
*
* Para los hilos: TODO
* 1 sola clase, modificar los hilos que se ejecutan con el constructor. Definir en otro txt.
* */