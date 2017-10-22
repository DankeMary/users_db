package view;

import database.DBUsers;
import entity.User;
import exception.EmailException;
import exception.LoginException;
import utils.HelpUtils;
import utils.LogUtils;

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

    public void editInfo(){

            if (db.isEmpty()) {
                System.out.println("Database is empty");//LogUtils.printEmptyListMessage();
                return;
            }
            LogUtils.printInputIDForActionMessage("edit");
            int userID = HelpUtils.getInt(1, Integer.MAX_VALUE);
            User user = db.getUserByID(userID);
            if (user == null){
                System.out.println("User with such ID wasn't found"); //LogUtils.printItemNotFoundMessage(userID);
                return;
            }

            /*User user = productsList.editItem(itemID);

            if (product == null) {
                LogUtils.printItemNotFoundMessage(itemID);
                return;
            }

            while (productsList.countDuplicates(product.getID()) > 1) {
                System.out.println("Item with such ID already exists! Change the id");
                System.out.print("ID: ");
                product.setID(HelpUtils.getInt(1, Integer.MAX_VALUE));
            }
            LogUtils.printActionResult(itemID, "edited");*/

    }
    /*
    public void test(){
        db.test();
    }*/
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
