package utils;

import entity.User;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
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
    public static ArrayList<User> readFromCSV(File fileName)/* throws FileNotFoundException, IOException*/{
        //String csvFile = "/Users/mkyong/csv/country.csv";

        BufferedReader br = null;
        String line = "";
        String csvSplitBy = ";";
try{
        //File file = new File(resource.getFile());
        br = new BufferedReader(new FileReader(fileName));
        ArrayList<User> users = new ArrayList<User>();
        User user = new User();

        while ((line = br.readLine()) != null) {
            // use comma as separator
            String[] data = line.split(csvSplitBy);
            /*for(int i = 0; i < data.length; i++)
                if (data[i].length() > 1)
                    data[i] = data[i].substring(1, data[i].length() - 1);*/

            System.out.println(line);
            user.setFirstName(HelpUtils.formatString(data[0].trim()));
            user.setLastName(HelpUtils.formatString(data[1].trim()));
            user.setLogin(data[2].trim().toLowerCase());
            user.setEmail(data[3].trim().toLowerCase());
            users.add(user);
        }

        //try {
            br.close();return users;
        }
        catch(FileNotFoundException e) {
            System.out.println("File not found");
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
return null;


        /*} catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {*/


    }

}
