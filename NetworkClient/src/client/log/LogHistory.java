package client.log;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LogHistory {

    private static final String fileName = "NetworkClient/history_%s.txt";
    private static final int SIZE = 100;

    public static void writeLineToFile (String login, String line){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(getFileName(login), true));
            writer.write(line + System.lineSeparator());
            writer.flush();
            writer.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static List<String> readHistoryFromFile(String login){
        File file = new File(getFileName(login));
        List<String> historyList = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while (historyList.size() <= SIZE){
                String line = reader.readLine();
                if(line == null){
                    break;
                }else {
                    historyList.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return historyList;
    }

    public static String getFileName(String login) {
        return String.format(fileName, login);
    }
}
