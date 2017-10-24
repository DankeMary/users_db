package utils;

import entity.User;
import sun.applet.Main;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;

public class FileUtils {
    private static final String PROHIBITED_CHARS = "~#%&*{}*\\/:<>?+|";
    public static String getFileName(String basic){
        String fileName = HelpUtils.getString(basic);
        if (/*fileName.charAt(0) == '_' || */fileName.charAt(0) == '.')
            return null;
        for(int i = 0; i < fileName.length(); i++)
            if (PROHIBITED_CHARS.contains(fileName.substring(i,i + 1)))
                return null;
        return fileName;
    }
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
    public ArrayList<User> readFromCSV(File fileName) throws FileNotFoundException, IOException{

        BufferedReader br = null;
        String line = "";
        String csvSplitBy = ";";
        try{
        //from here
            URL resource = Main.class.getResource("data.csv");
        //    System.out.println(resource.getFile());
            //System.out.println(resource.getPath());
            //File file = new File(URLDecoder.decode(resource.getFile()));
            //to here
                //File file = new File(resource.getFile());
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("data.csv").getFile());


            br = new BufferedReader(new FileReader(file));
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

    public static ArrayList<User> readFromCSV(String fileName)/* throws FileNotFoundException, IOException*/{

        BufferedReader br = null;
        String line = "";
        String csvSplitBy = ";";
        try{
            URL resource = FileUtils.class.getResource("data.csv");
            //System.out.println(resource.getFile());
            System.out.println(resource.getPath());
           // File file = new File(URLDecoder.decode(resource.getFile()));
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
    }

}
