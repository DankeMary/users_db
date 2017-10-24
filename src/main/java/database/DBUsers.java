package database;

import entity.User;
import exception.EmailException;
import exception.LoginException;
import utils.HelpUtils;

import java.io.*;
import java.sql.*;


//todo:     CLOSING CONNECTIONS (when)
/*
    String query_id = "SELECT * FROM user WHERE ID =" + id;

    String query_login = "SELECT * FROM user WHERE LOGIN='" + login +"'";

    String query_email = "SELECT * FROM user WHERE EMAIL='" + email + "'";

}*/
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

    //disconnect conn close
    private boolean infoExists(String query) {
        try {
            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery(query);
            return (rs.next());
        } catch (SQLException e) {
            //return false;
        }finally { /*close resset -> statement*/}
        return false;
    }
    public int countDuplicates (int id, String query) {
        int cnt = 0;
        try {
            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery(query);
            while (rs.next())
                if (rs.getInt(1) != id)
                    cnt++;
            return cnt;
        } catch (SQLException e) {
            //return false;
            return 0;
        }
    }

   /* public boolean emailExists(String email) {
        return infoExists("SELECT * FROM user WHERE EMAIL='" + email + "'");
    }

    public boolean loginExists(String login) {
        return infoExists("SELECT * FROM user WHERE LOGIN='" + login + "'");
    }*/

    public int emailExists(int id, String email) {
        return countDuplicates(id, "SELECT * FROM user WHERE EMAIL='" + email + "'");
    }

    public int loginExists(int id, String login) {
        return countDuplicates(id, "SELECT * FROM user WHERE LOGIN='" + login + "'");
    }

    //todo: how to deal with exceptions???
    public void addInfo(User user) throws LoginException, EmailException {
        if (infoExists("SELECT * FROM user WHERE LOGIN='" + user.getLogin() + "'"))
            throw new LoginException();
        else if (infoExists("SELECT * FROM user WHERE EMAIL='" + user.getEmail() + "'"))
            throw new EmailException();
        else
            try {
                Statement st = conn.createStatement();
                st.execute(String.format("INSERT INTO user(NAME, LAST_NAME, LOGIN, EMAIL) VALUES('%s', '%s', '%s', '%s')",
                        user.getFirstName(), user.getLastName(), user.getLogin(), user.getEmail()));
            } catch (SQLException e) {
                //return false;
            }
    }

    public User getUserByID(int id) {
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
            } catch (SQLException e) {
                return null;
            }
        else
            return null;
    }

    public boolean editInfo(User user) {
        if (!infoExists("SELECT * FROM user WHERE ID =" + user.getId()))
            return false;
        else {
            try {
                Statement st = conn.createStatement();
                st.executeUpdate("UPDATE user SET NAME='" + user.getFirstName() + "', LAST_NAME='" + user.getLastName()
                        + "', LOGIN='" + user.getLogin() + "', EMAIL='" + user.getEmail() + "' WHERE ID=" + user.getId());
                return true;
            } catch (SQLException e) {
                return false;
            }
        }
    }

    public boolean deleteInfo(int id) {
        if (!infoExists("SELECT * FROM user WHERE ID =" + id))
            return false;
        else {
            try {
                Statement st = conn.createStatement();
                st.executeUpdate("DELETE FROM user WHERE ID=" + id);
                return true;
            } catch (SQLException e) {
                return false;
            }
        }
    }

    public boolean isEmpty() {
        String query_id = "SELECT * FROM user";
        try {
            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery(query_id);
            return !rs.next();
        } catch (SQLException e) {
            //return false;
        }
        return true;
    }

    public ResultSet sortUsers() {
        String query_sort = "SELECT * FROM user ORDER BY `LOGIN`";
        try {
            Statement st = conn.createStatement();

            return st.executeQuery(query_sort);
        } catch (SQLException e) {
            // return null;
        }
        return null;

    }

    public ResultSet filterUsers(String criterion) {
        String query_filter = "SELECT * FROM user WHERE EMAIL LIKE '" + criterion + "'";
        try {
            Statement st = conn.createStatement();

            return st.executeQuery(query_filter);
        } catch (SQLException e) {
            // return null;
        }
        return null;
    }

    public ResultSet getAllUsers() {
        String query = "SELECT * FROM user";
        try {
            Statement st = conn.createStatement();

            return st.executeQuery(query);
        } catch (SQLException e) {
            // return null;
        }
        return null;
    }

    public boolean importInfo(String fileName) {
        BufferedReader br = null;
        String line = "";
        String csvSplitBy = ",";
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource(fileName).getFile());
            br = new BufferedReader(new FileReader(file));
            User user = new User();

            while ((line = br.readLine()) != null) {
                String[] data = line.split(csvSplitBy);
                try {
                    System.out.println(line);
                    if (HelpUtils.checkName(data[0].trim()))
                        user.setFirstName(HelpUtils.formatString(data[0].trim()));
                    else continue;
                    if (HelpUtils.checkName(data[1].trim()))
                        user.setLastName(HelpUtils.formatString(data[1].trim()));
                    else continue;
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
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        } finally {
            if (br != null)
                try {
                    br.close();
                } catch (IOException e) {
                }
        }
    }

    public boolean exportInfo(String fileName) {
        try {
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
            } catch (SQLException e) {
            } finally {
                pw.close();
            }
        } catch (FileNotFoundException e) {
            return false;
        }  //todo:????

        return true;
    }
}
