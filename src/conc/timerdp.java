package conc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class timerdp {

    private ArrayList<ArrayList<Integer>> tiempo;
    private ArrayList<Long> timestamps;
    private ArrayList<Boolean> esperando;
    private ArrayList<Long> esperandoid;
    private ArrayList<Integer> oldsens;
    private ArrayList<Long> sleepT;

    public timerdp(String dir,ArrayList<Integer> vectsens){

        tiempo = new ArrayList<>();
        timestamps = new ArrayList<>();
        esperando = new ArrayList<>();
        esperandoid = new ArrayList<>();
        sleepT = new ArrayList<>();

        tiempo = cargartiempo(dir);

        for(int i =0;i<tiempo.size();i++){

            if(vectsens.get(i) == 1){
                timestamps.add(System.currentTimeMillis());
            }
            else{
                timestamps.add(0L);
            }
            esperando.add(false);
            esperandoid.add(null);
            sleepT.add(0L);
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
        oldsens.clear();
        oldsens.addAll(actsens);
    }

    public Boolean antesDeLaVentana(Integer disp){

        long dif = System.currentTimeMillis() - timestamps.get(disp);

        if(dif/1000 < tiempo.get(disp).get(0)){
            return true;
        }
        else
            return false;


    }

    public void setSleepT(Integer disp){

        Long valor;
        if(valores(disp).get(0) == 0 && valores(disp).get(0) == 0){
            valor = 0L;
        }
        else {
            Long times = timestamps.get(disp) - System.currentTimeMillis();
            valor = times + valores(disp).get(0)*1000;
        }
        sleepT.set(disp,valor);
    }

    public Long getSleepT(Integer disp){ //TODO ver si esta bien borrar cuando hago un get!!!
        Long valor = sleepT.get(disp);
        sleepT.set(disp,0L);
        return valor;
    }

    public void setEsperando(Integer disp){
        esperando.set(disp,true);
        esperandoid.set(disp,Thread.currentThread().getId());//TODO ver y testear
    }

    public Boolean getEsperando(Integer disp){
        return esperando.get(disp);
    }

    public Long getEsperandoID(Integer disp){return esperandoid.get(disp);}//TODO agregar que se use al preguntar por esperando para verificar que sea el hilo que lo llamo inicialmente

    public void resetEsperando(Integer disp){ //TODO reset de todos o solo el actual???

            esperando.set(disp,false);
            esperandoid.set(disp,null);
    }

    public ArrayList<ArrayList<Integer>> getred(){
        return tiempo;
    }
}
