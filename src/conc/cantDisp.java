package conc;

public class cantDisp {

    private int disparos = 1500;

    public synchronized boolean dism(){
        disparos--;
        if(disparos <= 0){
            System.exit(0);
            return false;
        }
        return true;
    }
}
