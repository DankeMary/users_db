package utils;

import entity.User;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileUtils {
    /*public static User[] readFromCSV(){
        String csvFile = "/Users/mkyong/csv/country.csv";
        String line = "";
        String cvsSplitBy = ",";
        BufferedReader br;
        try{
            br = new BufferedReader(new FileReader(csvFile));
        //try (br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] values = line.split(cvsSplitBy);
                for(String value : values)
                {

                }
                //System.out.println("Country [code= " + country[4] + " , name=" + country[5] + "]");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/
    public static ArrayList<User> readFromCSV(String fileName) throws FileNotFoundException, IOException{
        String csvFile = "/Users/mkyong/csv/country.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        br = new BufferedReader(new FileReader(csvFile));
        ArrayList<User> users = new ArrayList<User>();
        User user = new User();

        while ((line = br.readLine()) != null) {
            // use comma as separator
            String[] data = line.split(cvsSplitBy);
            for(int i = 0; i < data.length; i++)
                if (data[i].length() > 1)
                    data[i] = data[i].substring(1, data[i].length() - 1);

            user.setFirstName(data[0]);
            user.setLastName(data[1]);
            user.setLogin(data[2]);
            user.setEmail(data[3]);
            users.add(user);
        }
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return users;

        /*} catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {*/


    }

}
