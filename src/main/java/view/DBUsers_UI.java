package view;

import database.DBUsers;
import entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUsers_UI {
    private DBUsers db;

    public DBUsers_UI(){
        db = new DBUsers();
    }

    public void printUsers() {
        ResultSet rs = db.getAllUsers();
        try {

            while (rs.next()) {
                User user = new User(); //todo: is it needed?
                user.setId(rs.getInt(1));
                user.setFirstName(rs.getString(2));
                user.setLastName(rs.getString(3));
                user.setLogin(rs.getString(4));
                user.setEmail(rs.getString(5));
                System.out.println(user);
            }
        }
        catch(SQLException e) {}
    }

    public void printUsers(ResultSet rs){

    }
}
