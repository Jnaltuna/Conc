package conc.test;

import conc.verifplaza;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class verifplazaTest {

    private verifplaza ver = null;

    @BeforeEach
    void iniciar() { ver = new verifplaza(); }

    @Test
    void testinit() {

        System.out.println(ver.getInvariantes());
        System.out.println(ver.getRes());

    }

    @Test
    void testverifF() {

        ArrayList<Integer> marca = new ArrayList<>(Arrays.asList(1,0,0,1,0,0,1,1));

        boolean resultado = ver.verificar(marca);

        assertEquals(false,resultado);
    }

    @Test
    void testverifT() {

        ArrayList<Integer> marca = new ArrayList<>(Arrays.asList(1,0,0,1,0,0,2,1));

        boolean resultado = ver.verificar(marca);

        assertEquals(true,resultado);
    }

}
