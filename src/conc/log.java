package conc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;

public class log {

    private String FILENAME = "C:\\Users\\Altuna\\Desktop\\log.txt";
    private BufferedWriter bw;
    private FileWriter fw;

    public log(){

        File f = new File(FILENAME);
        if(f.exists()){
            f.delete();
        }

    }

    public void escribir(String valor){

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILENAME,true))) {

            valor = valor + "\t" + new Timestamp(System.currentTimeMillis()) + "\n";

            bw.write(valor);


        } catch (IOException e) {

            e.printStackTrace();

        }

    }
}
