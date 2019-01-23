package conc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class timerdp {

    private ArrayList<ArrayList<Integer>> tiempo;
    private ArrayList<Long> timestamps;
    private ArrayList<Boolean> esperando;

    public timerdp(String dir){

        tiempo = new ArrayList<>();
        timestamps = new ArrayList<>();
        esperando = new ArrayList<>();

        tiempo = cargartiempo(dir);

        for(int i =0;i<tiempo.size();i++){
            timestamps.add(System.currentTimeMillis());
            esperando.add(false); //TODO inicializar en true o false???
        }

    }

    private ArrayList<ArrayList<Integer>> cargartiempo(String dir){ //TODO cambiar ruta

        ArrayList<ArrayList<Integer>> time = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(dir))) {

            String line;

            int i = 0;

            while ((line = br.readLine()) != null) {

                String[] valores = line.split("\t");

                time.add(new ArrayList<>());

                for(int j=0;j<valores.length;j++) {
                    time.get(i).add(Integer.parseInt(valores[j]));
                }
                i++;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return time;
    }

    public ArrayList<Integer> valores(Integer disp){

        ArrayList<Integer> val = new ArrayList<>();

        val.add(tiempo.get(disp).get(0));
        val.add(tiempo.get(disp).get(1));

        return val;
    }

    public Boolean testVentanaTiempoo(Integer disp){

        long dif = System.currentTimeMillis() - timestamps.get(disp);

        if(tiempo.get(disp).get(0) == 0 && tiempo.get(disp).get(1) == 0){ //TODO ver??
        return true;
        }
        if(dif/1000 >= tiempo.get(disp).get(0) && dif/1000 <= tiempo.get(disp).get(1))
            return true; //TODO cambiar
        else
            return false;
    }

    public void setNuevoTimeStamp(Integer disp){
        timestamps.set(disp,System.currentTimeMillis());
    }

    public Boolean antesDeLaVentana(Integer disp){

        long dif = System.currentTimeMillis() - timestamps.get(disp);

        if(dif/1000 < tiempo.get(disp).get(0)){
            return true;
        }
        else
            return false;


    }

    public void setEsperando(Integer disp){
        esperando.set(disp,true);
    }

    public Boolean getEsperando(Integer disp){
        return esperando.get(disp);
    }

    public void resetEsperando(){ //TODO reset de todos o solo el actual???
        for(int i = 0 ; i<esperando.size(); i++){
            esperando.set(i,false);
        }
    }

    public ArrayList<ArrayList<Integer>> getred(){
        return tiempo;
    }
}
