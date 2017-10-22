package database;

import entity.User;
import exception.EmailException;
import exception.LoginException;

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

    /*private void infoExists(User user) throws LoginException, EmailException{
        //String query_id = "SELECT * FROM user WHERE ID ='" + user.getId()+"'";
        String query_login ="SELECT * FROM user WHERE Login ='" + user.getLogin()+"'";
        String query_email ="SELECT * FROM user WHERE Email ='" + user.getEmail()+"'";
        try {
            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery(query_login);
            if(rs.next()){
                throw new LoginException("User with such login already exists!");
            }

            rs = st.executeQuery(query_email);
            if (rs.next()) {
                throw new EmailException("User with such email already exists!");
            }
        }
        catch(SQLException e) {
            //return false;
        }
        //return false;
    }*/

    private boolean infoExists(String query) {
        try {
            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery(query);
            return (rs.next());
        }
        catch(SQLException e) {
            //return false;
        }
        return false;
    }
    /*private boolean idExists(int id) {
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
    private boolean loginExists(String login) {
        String query_login = "SELECT * FROM user WHERE LOGIN='" + login +"'";
        try {
            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery(query_login);
            if(rs.next())
                return true;
        }
        catch(SQLException e) {
            //return false;
        }
        return false;
    }
    private boolean emailExists(String email) {
        String query_email = "SELECT * FROM user WHERE EMAIL='" + email + "'";
        try {
            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery(query_email);
            if(rs.next())
                return true;
        }
        catch(SQLException e) {
            //return false;
        }
        return false;
    }*/
//todo: how to deal with exceptions???
    public void addInfo(User user) throws LoginException, EmailException{ //todo: is THROWS needed here?
        if (infoExists("SELECT * FROM user WHERE LOGIN='" + user.getLogin() +"'"))
            throw new LoginException();
        else if (infoExists("SELECT * FROM user WHERE EMAIL='" + user.getEmail() + "'"))
            throw new EmailException();
        else
            try {
                Statement st = conn.createStatement();
                st.execute(String.format("INSERT INTO user(NAME, LAST_NAME, LOGIN, EMAIL) VALUES('%s', '%s', '%s', '%s')",
                        user.getFirstName(), user.getLastName(),user.getLogin(), user.getEmail()));
            } catch (SQLException e) {
                //return false;
            }
    }

    public boolean editInfo(User user){
        if (!idExists(user.getId()))
            return false;
        else {
            try {
                Statement st = conn.createStatement();
                st.executeUpdate("UPDATE user SET NAME='" + user.getFirstName() +"', LAST_NAME='" + user.getLastName()
                        + "', LOGIN='" + user.getLogin() +"', EMAIL='" + user.getEmail() + "' WHERE ID="+user.getId());
                return true;
            }
            catch (SQLException e) { return false; }
        }
    }

    public boolean deleteInfo(User user){
        if (!idExists(user.getId()))
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

    public boolean isEmpty(){
        String query_id = "SELECT * FROM user";
        try {
            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery(query_id);
            return !rs.next();
        }
        catch(SQLException e) {
            //return false;
        }
        return true;
    }

    public ResultSet sortUsers(){
        String query_sort = "SELECT * FROM user ORDER BY `LAST_NAME`, 'NAME'";
        try {
            Statement st = conn.createStatement();

            return st.executeQuery(query_sort);
        }
        catch(SQLException e) {
            // return null;
        }
        return null;

    }

    /*sort: (SELECT * FROM user ORDER BY `LAST_NAME`, 'NAME';

    */
    public ResultSet getAllUsers(){
        String query = "SELECT * FROM user";
        try {
            Statement st = conn.createStatement();

            return st.executeQuery(query);
        }
        catch(SQLException e) {
            // return null;
        }
        return null;
    }
}
