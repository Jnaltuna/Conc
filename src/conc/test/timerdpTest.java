package conc.test;

import conc.RdP;
import conc.timerdp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;


public class timerdpTest {

    private String dir = "C:\\Users\\Altuna\\Desktop\\test\\timetest.txt";
    private timerdp time = null;
    private ArrayList<Integer> sens = new ArrayList<>(Arrays.asList(0, 0, 1, 1, 0, 0, 0, 0));

    @BeforeEach
    void iniciar() {
        time = new timerdp(dir, sens);
    }

    @Test
    void cargarvalores() {

        ArrayList<ArrayList<Integer>> esperado = new ArrayList<>();
        esperado.add(new ArrayList<>(Arrays.asList(0, 0)));
        esperado.add(new ArrayList<>(Arrays.asList(0, 0)));
        esperado.add(new ArrayList<>(Arrays.asList(1, 5)));
        esperado.add(new ArrayList<>(Arrays.asList(3, 8)));
        esperado.add(new ArrayList<>(Arrays.asList(0, 0)));
        esperado.add(new ArrayList<>(Arrays.asList(0, 0)));
        esperado.add(new ArrayList<>(Arrays.asList(0, 0)));
        esperado.add(new ArrayList<>(Arrays.asList(0, 0)));


        assertEquals(esperado, time.getred());

    }

    @Test
    void testvalores() {

        ArrayList<Integer> esperado = new ArrayList<>();
        esperado.add(3);
        esperado.add(8);

        assertEquals(esperado, time.valores(3));
    }

    @Test
    void testventanasinT() {

        Boolean val = time.testVentanaTiempoo(0);

        assertEquals(true, val);

    }

    @Test
    void testventanaT() {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Boolean val = time.testVentanaTiempoo(2);

        assertEquals(true, val);

    }

    @Test
    void antesTesttrue() {

        Boolean val = time.antesDeLaVentana(2);

        assertEquals(true, val);
    }

    @Test
    void antesTestfalse() {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Boolean val = time.antesDeLaVentana(2);

        assertEquals(false, val);
    }

    @Test
    void testEsperandoo() {

        Boolean antes = time.getEsperando(0);
        time.setEsperando(0);
        Boolean despues = time.getEsperando(0);
        time.resetEsperando(0);
        Boolean reset = time.getEsperando(0);

        assertEquals(false, antes);
        assertEquals(true, despues);
        assertEquals(false, reset);
    }
}
