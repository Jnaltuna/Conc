package conc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class verifplaza {

    private ArrayList<ArrayList<Integer>> invariantes;
    private ArrayList<Integer> res;


    public verifplaza(String dir){

        cargarInv(dir);

    }

    private void cargarInv(String direccion) {

        invariantes = new ArrayList<>();
        res = new ArrayList<>();
        int i = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(direccion))) {
            String line;

            while ((line = br.readLine()) != null) {

                String[] valores = line.split("\t");
                boolean eq = false;

                invariantes.add(new ArrayList<>());

                for (int j = 0; j < valores.length; j++) {
                    if(valores[j].equals("=")){
                        eq = true;
                        continue;
                    }
                    if(!eq) {
                        invariantes.get(i).add(Integer.parseInt(valores[j]));
                    }
                    else{
                        res.add(Integer.parseInt(valores[j]));
                    }
                }
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean verificar(ArrayList<Integer> marcaact){

        for(int i = 0; i<invariantes.size();i++){
            int suma = 0;

            for(int j = 0; j<invariantes.get(i).size(); j++){
                suma+= marcaact.get(j) * invariantes.get(i).get(j);
            }

            if(suma != res.get(i)){
                System.out.println("No se verifico el invariante " + i);
                return false;
            }
        }

        return true;

    }

    public ArrayList<ArrayList<Integer>> getInvariantes() {
        return invariantes;
    }

    public ArrayList<Integer> getRes() {
        return res;
    }



}
