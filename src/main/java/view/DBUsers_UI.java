package view;

import database.DBUsers;
import entity.User;
import exception.EmailException;
import exception.LoginException;
import utils.HelpUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUsers_UI {
    private DBUsers db;

    public DBUsers_UI(){
        db = new DBUsers();
    }

    public void addInfo() {
        System.out.println("Input data");
        User newUser = HelpUtils.getUser();

        while (true){
            try {
                db.addInfo(newUser);
                System.out.println("New data was added.");
                return;
            }
            catch (LoginException e){
                System.out.println("User with such login already exists! Would you like to change it?");
                if (HelpUtils.getBool(false)) {
                    System.out.print("Login: ");
                    newUser.setLogin(HelpUtils.getString());
                } else return;
            }
            catch(EmailException e){
                System.out.println("User with such email already exists! Would you like to change it?");
                if (HelpUtils.getBool(false)) {
                    System.out.print("Email: ");
                    newUser.setEmail(HelpUtils.getString());
                } else return;
            }
        }
    }
    public void printUsers() {
        ResultSet rs = db.getAllUsers();
        printUsers(rs);
    }

    public void printUsers(ResultSet rs){
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
}
