package conc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;

class log {

    private String filename;

    log(){

        filename = "C:\\Users\\Altuna\\Desktop\\log.txt";
        File f = new File(filename);
        if(f.exists()){
            f.delete();
        }

    }

    void escribir(String valor){

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename,true))) {

            valor = valor + "\t" + new Timestamp(System.currentTimeMillis()) + "\n";

            bw.write(valor);


        } catch (IOException e) {

            e.printStackTrace();

        }

    }
}
