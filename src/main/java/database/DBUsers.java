package database;

import entity.User;

import java.sql.*;

public class DBUsers {
    private static final String URL = "jdbc:mysql://localhost:3306/users?useUnicode=true&useSSL=true&useJDBCCompliantTimezoneShift=true" +
            "&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String user = "root";
    private static final String password = "root";

    private static Connection conn;

    public DBUsers() {
        try {
            conn = DriverManager.getConnection(URL, user, password);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }

    private boolean infoExists(User user) {
        //String query_id = "SELECT * FROM user WHERE ID ='" + user.getId()+"'";
        String query_login ="SELECT * FROM user WHERE Login ='" + user.getLogin()+"'";
        String query_email ="SELECT * FROM user WHERE Email ='" + user.getEmail()+"'";
        try {
            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery(query_login);
            if(rs.next())
                return true;

            rs = st.executeQuery(query_email);
            if (rs.next())
                return true;
        }
        catch(SQLException e) {
            //return false;
        }
        return false;
    }

    private boolean infoExists(int id) {
        String query_id = "SELECT * FROM user WHERE ID =" + id;
        try {
            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery(query_id);
            if(rs.next())
                return true;
        }
        catch(SQLException e) {
            //return false;
        }
        return false;
    }

    public boolean addInfo(User user){
        if (infoExists(user))
            return false;
        else {
            try {
                Statement st = conn.createStatement();
                st.execute("INSERT INTO user(NAME, LAST_NAME, LOGIN, EMAIL) VALUES(" + user.getFirstName() + ", "
                        + user.getLastName() + ", " + user.getLogin() + ", " + user.getEmail() + ");");
                return true;
            }
            catch (SQLException e) { return false; }
        }
    }

    public boolean editInfo(User user){
        if (!infoExists(user.getId()))
            return false;
        else {
            try {
                Statement st = conn.createStatement();
                st.executeUpdate("UPDATE user SET NAME=" + user.getFirstName() +", LAST_NAME=" + user.getLastName()
                        + ", LOGIN=" + user.getLogin() +", EMAIL=" + user.getEmail() + " WHERE ID="+user.getId());
                return true;
            }
            catch (SQLException e) { return false; }
        }
    }

    public boolean deleteInfo(User user){
        if (!infoExists(user.getId()))
            return false;
        else {
            try {
                Statement st = conn.createStatement();
                st.executeUpdate("DELETE FROM user WHERE WHERE ID="+user.getId());
                return true;
            }
            catch (SQLException e) { return false; }
        }
    }
}
