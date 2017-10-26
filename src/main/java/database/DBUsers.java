package database;

import entity.User;
import exception.EmailException;
import exception.LoginException;
import utils.HelpUtils;

import javax.swing.plaf.nimbus.State;
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
    public boolean disconnect(){
        try {
            conn.close();
            return true;
        } catch (SQLException e) {return false;}
    }
    private boolean infoExists(String query) {
        Statement st = null;
        ResultSet rs = null;
        try {
            st = conn.createStatement();

            rs = st.executeQuery(query);
            return (rs.next());
        } catch (SQLException e) {
            return false;
        }//finally { /*close resset -> statement*/
         finally {
               // try { if (rs != null) rs.close(); } catch (Exception e) {};
               // try { if (st != null) st.close(); } catch (Exception e) {};
        }
       // return false;
    }
    public int countDuplicates (int id, String query) {
        int cnt = 0;
        Statement st = null;
        ResultSet rs = null;
        try {
            st = conn.createStatement();

            rs = st.executeQuery(query);
            while (rs.next())
                if (rs.getInt(1) != id)
                    cnt++;
            return cnt;
        } catch (SQLException e) {
            //return false;
            return 0;
        } finally {
           // try { if (rs != null) rs.close(); } catch (Exception e) {};
           // try { if (st != null) st.close(); } catch (Exception e) {};
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
        Statement st = null;
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
                //todo: !!!! return something
                // return false;
            }finally {
              //  try { if (st != null) st.close(); } catch (Exception e) {};
            }
    }

    public User getUserByID(int id) {
        Statement st = null;
        ResultSet rs = null;
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
            }finally {
               // try { if (rs != null) rs.close(); } catch (Exception e) {};
               // try { if (st != null) st.close(); } catch (Exception e) {};
            }
        else
            return null;
    }

    public boolean editInfo(User user) {
        Statement st = null;
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
            }finally {
             //   try { if (st != null) st.close(); } catch (Exception e) {};
            }
        }
    }

    public boolean deleteInfo(int id) {
        Statement st = null;
        if (!infoExists("SELECT * FROM user WHERE ID =" + id))
            return false;
        else {
            try {
                st = conn.createStatement();
                st.executeUpdate("DELETE FROM user WHERE ID=" + id);
                return true;
            } catch (SQLException e) {
                return false;
            }finally {
              //  try { if (st != null) st.close(); } catch (Exception e) {};
            }
        }
    }

    public boolean isEmpty() {
        String query_id = "SELECT * FROM user";
        Statement st = null;
        ResultSet rs = null;
        try {
            st = conn.createStatement();

            rs = st.executeQuery(query_id);
            return !rs.next();
        } catch (SQLException e) {
            //return false;
        } finally {
           // try { if (rs != null) rs.close(); } catch (Exception e) {};
           // try { if (st != null) st.close(); } catch (Exception e) {};
        }
        return true; //todo: ????? needed?
    }

    private ResultSet runQuery(String query) {
        Statement st = null;
        try {
            st = conn.createStatement();

            return st.executeQuery(query);
        } catch (SQLException e) {
            return null;
        }finally {
          //  try { if (st != null) st.close(); } catch (Exception e) {};
        }
    }
    public ResultSet sortUsers() {
        String query_sort = "SELECT * FROM user ORDER BY `LOGIN`";
        return runQuery(query_sort);
        //return null;
    }

    public ResultSet filterUsers(String criterion) {
        String query_filter = "SELECT * FROM user WHERE EMAIL LIKE '" + criterion + "'";
        return runQuery(query_filter);
        //return null;
    }

    public ResultSet getAllUsers() {
        String query = "SELECT * FROM user";
        return runQuery(query);
        //return null;
    }

    public boolean importInfo(String fileName) {
        BufferedReader br = null;
        String line = "";
        String csvSplitBy = ",";
        File file = null;
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            file = new File(classLoader.getResource(fileName).getFile());
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
        ResultSet rs = null;
        try {
            PrintWriter pw = new PrintWriter(fileName);
            StringBuilder sb = new StringBuilder();
            rs = getAllUsers();
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
            return false;
        } finally {
          //  try { if (rs != null) rs.close(); } catch (Exception e) {};
        }
        return true;
    }
}
