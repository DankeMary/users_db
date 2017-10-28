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

    public DBUsers() throws SQLException {
        try {
            conn = DriverManager.getConnection(URL, user, password);
        } catch (SQLException e) {/*error trying to access db*/ }
    }

    public boolean disconnect() {
        try {
            conn.close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    private boolean infoExists(String query, String parameter) {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, parameter);
            ResultSet rs = preparedStatement.executeQuery();
            return (rs.next());
        } catch (SQLException e) {
            return false;
        }
    }

    public int countDuplicates(int id, String query, String parameter) {
        int cnt = 0;

        PreparedStatement preparedStatement;
        try {
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, parameter);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next())
                if (rs.getInt(1) != id)
                    cnt++;
            return cnt;
        } catch (SQLException e) {
            return -1;
        }
    }

    public int emailExists(int id, String email) {
        return countDuplicates(id, "SELECT ID FROM user WHERE EMAIL=?", email);
    }

    public int loginExists(int id, String login) {
        return countDuplicates(id, "SELECT ID FROM user WHERE LOGIN=?", login);
    }

    public void addInfo(User user) throws LoginException, EmailException {
        if (infoExists("SELECT ID FROM user WHERE LOGIN=?", user.getLogin()))
            throw new LoginException();
        else if (infoExists("SELECT ID FROM user WHERE EMAIL=?", user.getEmail()))
            throw new EmailException();

        try {
            PreparedStatement preparedStatement =
                    conn.prepareStatement("INSERT INTO user(NAME, LAST_NAME, LOGIN, EMAIL) VALUES(?, ?, ?, ?)");
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getLogin());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
        }
    }

    public User getUserByID(int id) {
        //if (infoExists("SELECT * FROM user WHERE ID=" + id))
        if (infoExists("SELECT * FROM user WHERE ID=?", String.valueOf(id)))
            try {
                PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM user WHERE ID=?");
                preparedStatement.setInt(1, id);
                ArrayList<User> users = convertToList(preparedStatement.executeQuery());
                return (users == null) ? null : users.get(0);
            } catch (SQLException e) {
                return null;
            }
        else
            return null;
    }

    public boolean editInfo(User user) {
        if (!infoExists("SELECT * FROM user WHERE ID=?", String.valueOf(user.getId())))
            return false;
        else {
            try {
                PreparedStatement preparedStatement =
                        conn.prepareStatement("UPDATE user SET NAME=?, LAST_NAME=?, LOGIN=?, EMAIL=? WHERE ID=?");
                preparedStatement.setString(1, user.getFirstName());
                preparedStatement.setString(2, user.getLastName());
                preparedStatement.setString(3, user.getLogin());
                preparedStatement.setString(4, user.getEmail());
                preparedStatement.setInt(5, user.getId());
                preparedStatement.executeUpdate();
                return true;
            } catch (SQLException e) {
                return false;
            }
        }
    }

    public boolean deleteInfo(int id) {
        if (!infoExists("SELECT * FROM user WHERE ID =?", String.valueOf(id)))
            return false;
        else {
            try {
                PreparedStatement preparedStatement = conn.prepareStatement("DELETE FROM user WHERE ID=?");
                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();
                return true;
            } catch (SQLException e) {
                return false;
            }
        }
    }

    public boolean isEmpty() {
        try {
            return !getAllData().next();
        } catch (SQLException e) {
            return true;
        }
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

    public ArrayList<User> sortUsers() {
        String query_sort = "SELECT * FROM user ORDER BY `LOGIN`";
        return convertToList(runQuery(query_sort));
    }

    public ArrayList<User> filterUsers(String criterion) {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM user WHERE EMAIL LIKE ?");
            preparedStatement.setString(1, criterion);
            return convertToList(preparedStatement.executeQuery());
        } catch (SQLException e) {
            return null;
        }
    }

    public ResultSet getAllData() {
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
        try {
            ArrayList<User> users = FileUtils.importInfo(fileName);
            if (users != null)
                for (User u : users) {
                    try {
                        if (checkData(u))
                            addInfo(u);
                    } catch (LoginException e) {
                    } catch (EmailException e) {
                    }
                }
        } catch (FileNotFoundException e) {
            return false;
        }
        return true;
    }

    public ArrayList<User> getAllUsers() {
        return convertToList(getAllData());
    }

    public boolean exportInfo(String fileName) {
        return FileUtils.exportInfo(fileName, getAllUsers());
    }

    private ArrayList<User> convertToList(ResultSet rs) {
        ArrayList<User> users = new ArrayList<User>();
        try {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt(1));
                user.setFirstName(rs.getString(2));
                user.setLastName(rs.getString(3));
                user.setLogin(rs.getString(4));
                user.setEmail(rs.getString(5));
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            return null;
        }
    }
}
