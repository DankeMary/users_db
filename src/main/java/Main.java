import utils.HelpUtils;
import view.DBUsers_UI;

import java.sql.*;

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
        smth.printUsers();
        System.out.println();
        smth.deleteInfo();
        System.out.println();
        smth.printUsers();

        /*System.out.println(HelpUtils.getName());
        System.out.println(HelpUtils.getEmail());*/
        /*smth.addInfo();
        smth.printUsers();*/
        /*String str = null;
        System.out.print(str);*/

    }

}