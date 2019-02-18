package conc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class timerdp {

    private ArrayList<ArrayList<Integer>> tiempo;
    private ArrayList<Long> timestamps;
    private ArrayList<Boolean> esperando;
    private ArrayList<Integer> oldsens;

    public timerdp(String dir,ArrayList<Integer> vectsens){

        tiempo = new ArrayList<>();
        timestamps = new ArrayList<>();
        esperando = new ArrayList<>();

        tiempo = cargartiempo(dir);

        for(int i =0;i<tiempo.size();i++){ //TODO agrego timestamp si esta sens, null de lo contrario

            if(vectsens.get(i) == 1){
                timestamps.add(System.currentTimeMillis());
            }
            else{
                timestamps.add(0L);
            }
            esperando.add(false);
        }

        oldsens = vectsens;
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

    public void setNuevoTimeStamp(Integer disp,boolean add){ //TODO si add es true, agrego nuevo TS. Si es false pongo 0

        if(add) {
            timestamps.set(disp, System.currentTimeMillis());
        }
        else{
            timestamps.set(disp, 0L);
        }

    }

    public void calctimestamps(ArrayList<Integer> actsens){ //TODO revisar, llamar desde RDP cuando calculo las nuevas sens
        for(int i = 0;i<actsens.size();i++){
            if(actsens.get(i) == 1 && oldsens.get(i) == 0){
                setNuevoTimeStamp(i,true);
            }
            else if(actsens.get(i) == 0 && oldsens.get(i) == 1){
                setNuevoTimeStamp(i,false);
            }
        }
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

    public void resetEsperando(Integer disp){ //TODO reset de todos o solo el actual???

            esperando.set(disp,false);

    }

    public ArrayList<ArrayList<Integer>> getred(){
        return tiempo;
    }
}
