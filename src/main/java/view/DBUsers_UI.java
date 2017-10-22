package view;

import database.DBUsers;
import entity.User;
import utils.HelpUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUsers_UI {
    private DBUsers db;

    public DBUsers_UI(){
        db = new DBUsers();
    }

    public void addInfo() {
        User newUser = HelpUtils.getUser();

        while (!db.addInfo(newUser)) {
            System.out.println("User with such login or email already exists! Would you like to change the id?");
            if (HelpUtils.getBool()) {
                System.out.print("ID: ");
                newProduct.setID(HelpUtils.getInt(1, Integer.MAX_VALUE));
            } else return;
        }

        if (!db.addInfo(newUser))
            System.out.println("User wasn't added!");
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
