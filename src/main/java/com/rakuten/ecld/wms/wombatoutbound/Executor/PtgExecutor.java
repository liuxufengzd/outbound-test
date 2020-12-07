//package com.rakuten.ecld.wms.wombatoutbound.Executor;
//
//import com.rakuten.ecld.wms.wombatoutbound.temp.RequestObject;
//import org.springframework.stereotype.Component;
//
//import java.io.*;
//
//@Component
//public class PtgExecutor implements CommandExecutor {
//    @Override
//    public void execute() {
//        try {
//            File file = new File("src/main/resources/ptg.csv");
//            BufferedReader reader = new BufferedReader(new FileReader(file));
//            String inString;
//            while ((inString = reader.readLine()) != null) {
//                System.out.println(inString);
//                RequestObject requestObject = makeRequest();
//            }
//            reader.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private RequestObject makeRequest(){
//        RequestObject requestObject = new RequestObject();
//        requestObject.setInput("ptg");
//        requestObject.setProcess("ptg");
//        return requestObject;
//    }
//}
