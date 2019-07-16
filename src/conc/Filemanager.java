package conc;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class Filemanager {

    private JSONObject jo;

    public Filemanager(boolean test){

        String path;
        if(test){
            path = "./resources/directoriestest.json";
        }
        else{
            path = "./resources/directories.json";
        }

        Object obj = null;
        try {
            obj = new JSONParser().parse(new FileReader(path));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        jo = (JSONObject) obj;

    }

    public String getvalor(String valor){

        return (String) jo.get(valor);
    }
}
