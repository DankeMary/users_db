package utils;

import entity.User;

import java.io.*;
import java.util.ArrayList;

public class FileUtils {
    private static final String DELIMETER = ",";

    public static ArrayList<User> importInfo(String fileName) throws FileNotFoundException {
        BufferedReader br = null;
        String line;
        ArrayList<User> users = new ArrayList<User>();
        try {
            br = new BufferedReader(new FileReader(fileName));

            while ((line = br.readLine()) != null) {
                String[] data = line.split(DELIMETER);
                User user = new User();
                user.setFirstName(HelpUtils.formatString(data[0].trim()));
                user.setLastName(HelpUtils.formatString(data[1].trim()));
                user.setLogin(data[2].trim().toLowerCase());
                user.setEmail(data[3].trim().toLowerCase());
                users.add(user);
            }
            return users;
        } catch (IOException e) {
            return null;
        } catch (NullPointerException e) {
            return null;
        } finally {
            if (br != null)
                try {
                    br.close();
                } catch (IOException e) {
                }
        }
    }

    public static boolean exportInfo(String fileName, ArrayList<User> users) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(fileName);
            StringBuilder sb = new StringBuilder();
            for (User u : users) {
                sb.append(u.getFirstName()).append(',');
                sb.append(u.getLastName()).append(',');
                sb.append(u.getLogin()).append(',');
                sb.append(u.getEmail()).append('\n');
                pw.write(sb.toString());
                sb.setLength(0);
            }
        } catch (IOException e) {
            return false;
        } finally {
            if (pw != null)
                pw.close();
        }
        return true;
    }
}
