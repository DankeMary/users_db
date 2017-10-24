package database;

import entity.User;
import exception.EmailException;
import exception.LoginException;
import utils.HelpUtils;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;

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

    public boolean emailExists(String email){
        return infoExists("SELECT * FROM user WHERE EMAIL='" + email + "'");
    }
    public boolean loginlExists(String login){
        return infoExists("SELECT * FROM user WHERE LOGIN='" + login + "'");
    }
    /*
        String query_id = "SELECT * FROM user WHERE ID =" + id;

        String query_login = "SELECT * FROM user WHERE LOGIN='" + login +"'";

        String query_email = "SELECT * FROM user WHERE EMAIL='" + email + "'";

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
    public User getUserByID(int id){
        if (infoExists("SELECT * FROM user WHERE ID=" + id))
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM user WHERE ID=" + id);

                User user = new User();
                rs.next();
                user.setId(rs.getInt(1));
                user.setFirstName(rs.getString(2));
                user.setLastName(rs.getString(3));
                user.setLogin(rs.getString(4));
                user.setEmail(rs.getString(5));
                return user;
            }
            catch (SQLException e) { return null; }
        else
            return null;
    }

    public boolean editInfo(User user){
        if (!infoExists("SELECT * FROM user WHERE ID =" + user.getId()))
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
/*
    public void test(){
        try {
            Statement st = conn.createStatement();

            st.executeUpdate("UPDATE user SET NAME='', LAST_NAME='', LOGIN='billie1', EMAIL='bla' WHERE ID=2");

        }
        catch (SQLException e) { System.out.println("Such data already exists");}
    }*/

    public boolean deleteInfo(int id){
        if (!infoExists("SELECT * FROM user WHERE ID =" + id))
            return false;
        else {
            try {
                Statement st = conn.createStatement();
                st.executeUpdate("DELETE FROM user WHERE ID=" + id);
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

    public ResultSet filterUsers(String criterion){
        String query_filter = "SELECT * FROM user WHERE EMAIL LIKE '" + criterion + "'";
        try {
            Statement st = conn.createStatement();

            return st.executeQuery(query_filter);
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

    public boolean importInfo(String fileName){
        BufferedReader br = null;
        String line = "";
        String csvSplitBy = ",";
        try{
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource(fileName).getFile());
            br = new BufferedReader(new FileReader(file));
            ArrayList<User> users = new ArrayList<User>();
            User user = new User();

            while ((line = br.readLine()) != null) {
                String[] data = line.split(csvSplitBy);
                try {
                    System.out.println(line);
                    user.setFirstName(HelpUtils.formatString(data[0].trim()));
                    user.setLastName(HelpUtils.formatString(data[1].trim()));
                    if (HelpUtils.checkLogin(data[2].trim()))
                        user.setLogin(data[2].trim().toLowerCase());
                    else continue;
                    if (HelpUtils.checkEmail(data[3].trim()))
                        user.setEmail(data[3].trim().toLowerCase());
                    else continue;

                    addInfo(user);
                } catch (LoginException e) {}
                catch (EmailException e) {}
            }
            return true;
        }
        catch(FileNotFoundException e) {
            return false;
        }
        catch (IOException e) {
            return false;
        }
        finally {
            if (br != null)
                try{
                    br.close();
                }
                catch(IOException e){}
        }
    }

    public boolean exportInfo(String fileName){
        try {
            //ClassLoader classLoader = getClass().getClassLoader();
           // File file = new File(classLoader.getResource(fileName).getFile());
        /*try {
            if(file.exists() && !file.isDirectory()) {
                // do something
            } else {
                FileWriter writer = new FileWriter(fileName);

            }
        } //catch (FileNotFoundException e) {}
        catch (IOException e) {}*/
            /*try {
                FileWriter writer = new FileWriter(fileName);
                StringBuilder sb = new StringBuilder();


            } catch (IOException e) {
            }*/

            PrintWriter pw = new PrintWriter(fileName);
            StringBuilder sb = new StringBuilder();
            ResultSet rs = getAllUsers();
            try {
                while (rs.next()) {
                    rs.getInt(1);
                    sb.append(rs.getString(2) + ',');
                    sb.append(rs.getString(3) + ',');
                    sb.append(rs.getString(4) + ',');
                    sb.append(rs.getString(5));
                    sb.append('\n');
                    pw.write(sb.toString());
                    sb.setLength(0);
                }
            } catch (SQLException e) {}
            finally {pw.close();}
        } catch (FileNotFoundException e) {return false;}

        return true;
    }
}
