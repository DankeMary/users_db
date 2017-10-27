package utils;

import entity.User;
import exception.EmailException;
import exception.LoginException;

import java.io.*;
import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FileUtils {
    public ArrayList<User> importInfo(String fileName) throws FileNotFoundException {
        BufferedReader br = null;
        String line = "";
        String csvSplitBy = ",";
        File file = null;
        ArrayList<User> users = new ArrayList<User>();
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            file = new File(classLoader.getResource(fileName).getFile());
            br = new BufferedReader(new FileReader(file));

            while ((line = br.readLine()) != null) {
                String[] data = line.split(csvSplitBy);
                User user = new User();
                user.setFirstName(HelpUtils.formatString(data[0].trim()));
                user.setLastName(HelpUtils.formatString(data[1].trim()));
                user.setLogin(data[2].trim().toLowerCase());
                user.setEmail(data[3].trim().toLowerCase());
                users.add(user);
            }
            return users;
        //} catch (FileNotFoundException e) {
        //    return null;
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
    public boolean exportInfo(String fileName, ResultSet rs) {
        try {
            PrintWriter pw = new PrintWriter(fileName);
            StringBuilder sb = new StringBuilder();
            try {
                while (rs.next()) {
                    rs.getInt(1);
                    sb.append(rs.getString(2)).append(',');
                    sb.append(rs.getString(3)).append(',');
                    sb.append(rs.getString(4)).append(',');
                    sb.append(rs.getString(5)).append('\n');
                    pw.write(sb.toString());
                    sb.setLength(0);
                }
            } catch (SQLException e) { return false;}
            finally {
                pw.close();
            }
        } catch (FileNotFoundException e) {
            //return false;
        } catch (IOException e) {
        } finally {
            //  try { if (rs != null) rs.close(); } catch (Exception e) {};
        }
        return true;
    }
}
