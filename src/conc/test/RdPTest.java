package conc.test;

import conc.RdP;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class RdPTest {

    private String dir1 = "C:\\Users\\Altuna\\Desktop\\rdptest.txt";
    private String dir2 = "C:\\Users\\Altuna\\Desktop\\marcatest.txt";
    private String dir3 = "C:\\Users\\Altuna\\Desktop\\inhibiciontest.txt";
    private String dir4 = "C:\\Users\\Altuna\\Desktop\\timetest.txt";

    private String[] direccion = new String[] {dir1,dir2,dir3,dir4};
    private RdP red = null;

    @BeforeEach
    void iniciar(){
        red = new RdP(direccion);
    }


    @Test
    void dispararred() {

        ArrayList<Integer> esperado = new ArrayList<>(Arrays.asList(0,1,0,0,1,0,0,3,1,1));

        red.disparar(0);
        red.disparar(1);
        red.disparar(6);
        red.disparar(7);

        assertEquals(esperado,red.getmarca());

    }

    @Test
    void disparartrue() {

        boolean valor,valor1,valor2;

        valor = red.disparar(0);
        valor1 = red.disparar(1);
        valor2 = red.disparar(6);

        assertTrue(valor);
        assertTrue(valor1);
        assertTrue(valor2);
    }

    @Test
    void dispararredfail() {

        ArrayList<Integer> esperado = new ArrayList<>(Arrays.asList(1,0,0,1,0,0,0,3,0,0));

        red.disparar(3);
        red.disparar(2);

        assertEquals(esperado,red.getmarca());

    }
    @Test
    void dispararfalse(){

        boolean valor,valor1;

        valor = red.disparar(3);
        valor1 = red.disparar(2);

        assertFalse(valor);
        assertFalse(valor1);

    }

    @Test
    void sensibilizadas() { //TODO revisar

        ArrayList<Integer> esperado = new ArrayList<>(Arrays.asList(1,0,0,0,0,0,0,1));

        assertEquals(esperado,red.sensibilizadas());

    }

    @Test
    void sensibilizadascondisp(){

        ArrayList<Integer> esperado = new ArrayList<>(Arrays.asList(0,0,1,1,0,0,1,1));


        red.disparar(0);
        red.disparar(7);
        red.disparar(1);

        assertEquals(esperado,red.sensibilizadas());

    }

    @Test
    void testcalcE(){

        ArrayList<Integer> esperado = new ArrayList<>(Arrays.asList(1,0,0,0,0,0,1,1));

        assertEquals(esperado,red.calcE());
    }

    @Test
    void testcalcB(){

        ArrayList<Integer> esperado = new ArrayList<>(Arrays.asList(1,1,1,1,1,1,0,1));

        assertEquals(esperado,red.calcB());
    }

    @Test
    void testcalcQ(){

        ArrayList<Integer> esperado = new ArrayList<>(Arrays.asList(1,0,0,1,0,0,0,1,0,0));

        assertEquals(esperado,red.calcQ());
    }

    @Test
    void cargarRDP() {
        ArrayList<ArrayList<Integer>> esperado = new ArrayList<>();
        esperado.add(new ArrayList<>(Arrays.asList(-1,1,0,0,0,0,1,-1,0,0)));
        esperado.add(new ArrayList<>(Arrays.asList(0,0,0,-1,1,0,-1,1,0,0)));
        esperado.add(new ArrayList<>(Arrays.asList(0,0,0,0,-1,1,0,0,0,0)));
        esperado.add(new ArrayList<>(Arrays.asList(0,-1,1,0,0,0,0,0,0,0)));
        esperado.add(new ArrayList<>(Arrays.asList(1,0,-1,0,0,0,0,0,0,0)));
        esperado.add(new ArrayList<>(Arrays.asList(0,0,0,1,0,-1,0,0,0,0)));
        esperado.add(new ArrayList<>(Arrays.asList(0,0,0,0,0,0,0,0,1,0)));
        esperado.add(new ArrayList<>(Arrays.asList(0,0,0,0,0,0,0,0,0,1)));


        assertEquals(esperado,red.getred());

    }

    @Test
    void cargarM0() {
        ArrayList<Integer> esperado = new ArrayList<>(Arrays.asList(1,0,0,1,0,0,0,3,0,0));

        assertEquals(esperado,red.getmarca());
    }

    @Test
    void inhibidortrue(){
        ArrayList<Integer> esperado = new ArrayList<>(Arrays.asList(1,0,0,1,0,0,0,3,0,2));

        red.disparar(7);
        red.disparar(7);

        assertEquals(esperado,red.getmarca());
    }

    @Test
    void inhibidorfalse(){
        ArrayList<Integer> esperado = new ArrayList<>(Arrays.asList(1,0,0,1,0,0,0,3,0,0));

        red.disparar(6);
        red.disparar(6);

        assertEquals(esperado,red.getmarca());

    }

    @Test
    void inhibidormult(){
        ArrayList<Integer> esperado = new ArrayList<>(Arrays.asList(1,0,0,1,0,0,0,3,0,3));

        red.disparar(7);
        red.disparar(6);
        red.disparar(6);
        red.disparar(7);
        red.disparar(7);

        assertEquals(esperado,red.getmarca());
    }

}