package conc.test;

import conc.GestorDeMonitor;
import conc.Politicas;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class PoliticasTest {

    private Politicas pol = null;
    private ArrayList<Integer> disp;
    private String dir = "C:\\Users\\Altuna\\Desktop\\test\\politicatest.txt";


    @BeforeEach
    void iniciar() {
        pol = new Politicas(dir);
    }

    @Test
    void Testprimergrupo() {
        Integer esperado = 3;
        disp = new ArrayList<>(Arrays.asList(0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        assertEquals(esperado, pol.Cualv2(disp));
    }

    @Test
    void Testsaltogrupo() {
        Integer esperado = 7;
        disp = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        assertEquals(esperado, pol.Cualv2(disp));
    }

    @Test
    void Testprioridad() {
        Integer esperado = 7;
        disp = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        assertEquals(esperado, pol.Cualv2(disp));
    }

    @Test
    void Testprioridadbaja() {
        Integer esperado = 6;
        disp = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        assertEquals(esperado, pol.Cualv2(disp));
    }

    @Test
    void Testmultiplesdisp() {
        Integer esperado1 = 18;
        Integer esperado2 = 7;
        Integer esperado3 = 10;
        disp = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0));

        assertEquals(esperado1, pol.Cualv2(disp));
        assertEquals(esperado2, pol.Cualv2(disp));
        assertEquals(esperado3, pol.Cualv2(disp));
    }

    @Test
    void Testcarga() {

        ArrayList<ArrayList<Integer>> grupo = new ArrayList<>();
        ArrayList<ArrayList<Integer>> subpgrupos = new ArrayList<>();

        grupo.add(new ArrayList<>(Arrays.asList(18, 3, 19, 4, 20, 5)));
        grupo.add(new ArrayList<>(Arrays.asList(7, 6, 21)));
        grupo.add(new ArrayList<>(Arrays.asList(9, 22, 8)));
        grupo.add(new ArrayList<>(Arrays.asList(10, 11)));
        grupo.add(new ArrayList<>(Arrays.asList(12, 14, 13, 15)));
        grupo.add(new ArrayList<>(Arrays.asList(16, 17)));
        grupo.add(new ArrayList<>(Arrays.asList(0, 1, 2)));

        subpgrupos.add(new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0)));
        subpgrupos.add(new ArrayList<>(Arrays.asList(2, 1, 1)));
        subpgrupos.add(new ArrayList<>(Arrays.asList(0, 0, 0)));
        subpgrupos.add(new ArrayList<>(Arrays.asList(0, 0)));
        subpgrupos.add(new ArrayList<>(Arrays.asList(0, 0, 0, 0)));
        subpgrupos.add(new ArrayList<>(Arrays.asList(0, 0)));
        subpgrupos.add(new ArrayList<>(Arrays.asList(0, 0, 0)));

        assertEquals(grupo, pol.getGrupos());
        assertEquals(subpgrupos, pol.getSubprioridadgrupo());

    }

    @Test
    void TestchangeP(){

        ArrayList<Integer> nuevaP = new ArrayList<>(Arrays.asList(1,1,1));
        Integer numerogrupo = 1;

        pol.changePolitica(numerogrupo,nuevaP);

        assertEquals(nuevaP,pol.getSubprioridadgrupo().get(1));
    }

}
