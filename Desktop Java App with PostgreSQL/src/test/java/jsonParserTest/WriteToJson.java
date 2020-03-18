package jsonParserTest;

import me.entities.Tema;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WriteToJson {
    @Test
    public void writeToJson() {
        JSONObject obj = new JSONObject();
        Tema tema = new Tema(1L, "fdf", 1, 2);
        obj.put("mapp", "3");
        obj.put("nota:", "4");
        obj.put("predata in saptamana:", "9");
        obj.put("deadline:", "9");
        obj.put("feedback:", "foarte bine");


        try(FileWriter file = new FileWriter("E:\\An2 Sem 1\\MAP\\Lab3Final\\src\\test\\resources\\exemplu.json")){
            file.write(obj.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
