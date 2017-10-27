package database;

import entity.User;
import exception.EmailException;
import exception.LoginException;
import utils.FileUtils;
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

    public DBUsers() throws SQLException{
        try {
            conn = DriverManager.getConnection(URL, user, password);
        }catch (SQLException e) {/*error trying to access db*/ }
    }

    public boolean disconnect(){
        try {
            conn.close();
            return true;
        } catch (SQLException e) {return false;}
    }
    private boolean infoExists(String query) {
        Statement st;
        ResultSet rs;
        try {
            st = conn.createStatement();

            rs = st.executeQuery(query);
            return (rs.next());
        } catch (SQLException e) {
            return false;
        }
    }
    public int countDuplicates (int id, String query) {
        int cnt = 0;
        Statement st;
        ResultSet rs;
        try {
            st = conn.createStatement();

            rs = st.executeQuery(query);
            while (rs.next())
                if (rs.getInt(1) != id)
                    cnt++;
            return cnt;
        } catch (SQLException e) {
            return -1;
        }
    }

    public int emailExists(int id, String email) {
        return countDuplicates(id, "SELECT * FROM user WHERE EMAIL='" + email + "'");
    }

    public int loginExists(int id, String login) {
        return countDuplicates(id, "SELECT * FROM user WHERE LOGIN='" + login + "'");
    }

    public void addInfo(User user) throws LoginException, EmailException {
        Statement st;
        if (infoExists("SELECT * FROM user WHERE LOGIN='" + user.getLogin() + "'"))
            throw new LoginException();
        else if (infoExists("SELECT * FROM user WHERE EMAIL='" + user.getEmail() + "'"))
            throw new EmailException();
        else
            try {
                st = conn.createStatement();
                st.execute(String.format("INSERT INTO user(NAME, LAST_NAME, LOGIN, EMAIL) VALUES('%s', '%s', '%s', '%s')",
                        user.getFirstName(), user.getLastName(), user.getLogin(), user.getEmail()));
            } catch (SQLException e) {
                // return false;
            }
    }

    public User getUserByID(int id) {
        Statement st;
        ResultSet rs;
        if (infoExists("SELECT * FROM user WHERE ID=" + id))
            try {
                st = conn.createStatement();
                rs = st.executeQuery("SELECT * FROM user WHERE ID=" + id);

                User user = new User();
                rs.next();
                user.setId(rs.getInt(1));
                user.setFirstName(rs.getString(2));
                user.setLastName(rs.getString(3));
                user.setLogin(rs.getString(4));
                user.setEmail(rs.getString(5));
                return user;
            } catch (SQLException e) {
                return null;
            }
        else
            return null;
    }

    public boolean editInfo(User user) {
        Statement st;
        if (!infoExists("SELECT * FROM user WHERE ID =" + user.getId()))
            return false;
        else {
            try {
                st = conn.createStatement();
                st.executeUpdate("UPDATE user SET NAME='" + user.getFirstName() + "', LAST_NAME='" + user.getLastName()
                        + "', LOGIN='" + user.getLogin() + "', EMAIL='" + user.getEmail() + "' WHERE ID=" + user.getId());
                return true;
            } catch (SQLException e) {
                return false;
            }
        }
    }

    public boolean deleteInfo(int id) {
        Statement st;
        if (!infoExists("SELECT * FROM user WHERE ID =" + id))
            return false;
        else {
            try {
                st = conn.createStatement();
                st.executeUpdate("DELETE FROM user WHERE ID=" + id);
                return true;
            } catch (SQLException e) {
                return false;
            }
        }
    }

    public boolean isEmpty() {
        String query_id = "SELECT * FROM user";
        Statement st;
        ResultSet rs;
        try {
            st = conn.createStatement();
            rs = st.executeQuery(query_id);
            return !rs.next();
        } catch (SQLException e) {
            //return false;
        }
        return true;
    }

    private ResultSet runQuery(String query) {
        Statement st;
        try {
            st = conn.createStatement();
            return st.executeQuery(query);
        } catch (SQLException e) {
            return null;
        }
    }
    public ResultSet sortUsers() {
        String query_sort = "SELECT * FROM user ORDER BY `LOGIN`";
        return runQuery(query_sort);
    }

    public ResultSet filterUsers(String criterion) {
        String query_filter = "SELECT * FROM user WHERE EMAIL LIKE '" + criterion + "'";
        return runQuery(query_filter);
    }

    public ResultSet getAllUsers() {
        String query = "SELECT * FROM user";
        return runQuery(query);
    }

    private boolean checkData(User user) {
        if (!HelpUtils.checkName(user.getFirstName()))
            return false;
        if (!HelpUtils.checkName(user.getLastName()))
            return false;
        if (!HelpUtils.checkLogin(user.getLogin()))
            return false;
        return HelpUtils.checkEmail(user.getEmail());
    }

    public boolean importInfo(String fileName) {
        FileUtils fileConnector = new FileUtils();
        try {
            ArrayList<User> users = fileConnector.importInfo(fileName);
            for (User u : users) {
                try {
                    if (checkData(u))
                        addInfo(u);
                } catch(LoginException e){
                }
                  catch(EmailException e){
                }
            }
        } catch (FileNotFoundException e) {
            return false;
        }
        return true;
    }

    public boolean exportInfo(String fileName) {
        ResultSet rs = getAllUsers();
        FileUtils fileConnector = new FileUtils();
        return fileConnector.exportInfo(fileName, rs);
    }
}
