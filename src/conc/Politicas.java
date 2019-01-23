package conc;

import java.util.ArrayList;

/*
 * Clase Politicas
 * Implementa la politica tomada para la ejecucion del programa
 * TODO
 * TODO
 * TODO
 * */

public class Politicas {

    /* TODO revisar*/

    public Politicas() {

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