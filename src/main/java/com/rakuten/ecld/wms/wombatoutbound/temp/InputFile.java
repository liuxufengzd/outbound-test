package com.rakuten.ecld.wms.wombatoutbound.temp;

import org.springframework.stereotype.Component;import java.io.*;

@Component
public class InputFile {
    private static BufferedReader reader;

    static {
        try {
            reader = new BufferedReader(new FileReader(new File("src/main/resources/ptg.csv")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String readData(){
        String data;
        try {
            if ((data = reader.readLine()) != null)
                return data;
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
