import entity.User;
import utils.FileUtils;
import utils.HelpUtils;
import view.DBUsers_UI;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.sql.*;
import java.util.ArrayList;

public class Main {
    private static Connection conn;
    private static final String URL = "jdbc:mysql://localhost:3306/users?useUnicode=true&useSSL=true&useJDBCCompliantTimezoneShift=true" +
            "&useLegacyDatetimeCode=false&serverTimezone=UTC";
    //private static final String URL = "jdbc:mysql://localhost:3306/users";
    private static final String user = "root";
    private static final String password = "root";

    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;

    public static void main(String args[]) {
       /*String query = "select id, name from user";

        try {
            conn = DriverManager.getConnection(URL, user, password);
            if(!conn.isClosed()){
                System.out.println("Соединение с БД установлено");
            }
            stmt = conn.createStatement();

            rs = stmt.executeQuery(query);

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                System.out.println("id: " + id + ", name: " + name);
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            try { conn.close(); } catch(SQLException se) { }
            try { stmt.close(); } catch(SQLException se) {  }
            try { rs.close(); } catch(SQLException se) { }
        }
*/
        DBUsers_UI smth = new DBUsers_UI();
        //smth.start();
       //try{
        String str = "blabla";
        if (str.charAt(1)==('a'));

        URL resource = Main.class.getResource("data.csv");
        //System.out.println(resource.getFile());
        //System.out.println(resource.getPath());
        File file = new File(URLDecoder.decode(resource.getFile()));
        FileUtils fu = new FileUtils();
        ArrayList<User> users = fu.readFromCSV(file); //"src/files/data.csv"
           for(User u:users)
               System.out.println(user);



       //System.out.println(FileUtils.getFileName("text.txt"));

      /* } catch(IOException e){
           System.out.println("Problems...");
       }*/

        /*String[] data = HelpUtils.getString().split(",");
        for (String str: data) {
            System.out.print(str + ' ');
        }
        System.out.println();
        for(int i = 0; i < data.length; i++)
            if (data[i].length() > 1)
                data[i] = data[i].substring(1, data[i].length() - 1);

        for (String str: data) {
            System.out.print(str + " ;");
        }
        System.out.println();*/

        //smth.printUsers();
        //System.out.println();
        //smth.deleteSeveralUsers();
        //smth.addUser();
       //smth.addUser();
       // smth.printUsers();
       // smth.filterUsers();
       // System.out.println();
       // smth.printUsers();

        /*System.out.println(HelpUtils.getName());
        System.out.println(HelpUtils.getEmail());*/
        /*smth.addInfo();
        smth.printUsers();*/
        /*String str = null;
        System.out.print(str);*/

    }

}