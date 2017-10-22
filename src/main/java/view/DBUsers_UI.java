package view;

import database.DBUsers;
import entity.User;
import exception.EmailException;
import exception.LoginException;
import utils.HelpUtils;
import utils.LogUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

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
        HelpUtils.getUser(user);

        while(true) {
            if (db.loginlExists(user.getLogin())){
                System.out.println("User with such login already exists! Change the login");
                System.out.print("Login: ");
                user.setLogin(HelpUtils.getString());
                continue;
            }
            if (db.emailExists(user.getEmail())){
                System.out.println("User with such email already exists! Change the email");
                System.out.print("Email: ");
                user.setEmail(HelpUtils.getEmail());
                continue;
            }
            break;
        }
        db.editInfo(user);
        LogUtils.printActionResult(userID, "edited");
    }

    public void deleteUser(){
        if (db.isEmpty()) {
            System.out.println("Database is empty");//LogUtils.printEmptyListMessage();
            return;
        }
        System.out.println("Print the list?");
        if (HelpUtils.getBool(false))
            printUsers();

        LogUtils.printInputIDForActionMessage("delete");
        int userID = HelpUtils.getInt(1, Integer.MAX_VALUE);
        if (db.deleteInfo(userID))
            LogUtils.printActionResult(userID, "deleted");
        else
            LogUtils.printItemNotFoundMessage(userID);
    }
    private void deleteUser(String id) {
        try {
            int itemID = Integer.parseInt(id);
            if (db.deleteInfo(itemID))
                LogUtils.printActionResult(itemID, "removed");
            else {
                LogUtils.printItemNotFoundMessage(itemID);
            }
        } catch (NumberFormatException e) {
            LogUtils.printWrongInputFormatMessage(id);
        }
    }

    private void deleteSeveralUsers() {
        if (db.isEmpty()) {
            System.out.println("Database is empty");//LogUtils.printEmptyListMessage();
            return;
        }
        System.out.println("Print the list?");
        if (HelpUtils.getBool(false))
            printUsers();

        Scanner in = new Scanner(System.in);
        System.out.print("Input IDs of users to erase: ");
        while (true) {
            String numbers = in.nextLine().trim();
            if (numbers.equals("")) {
                LogUtils.printEmptyInputError();
                continue;
            }
            String[] ids = numbers.split(" +");
            for (String num : ids)
                deleteUser(num);
            break;
        }
    }

    public void sortInfo(){
        if (db.isEmpty()) {
            System.out.println("Database is empty");//LogUtils.printEmptyListMessage();
            return;
        }
        printUsers(db.sortUsers());
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
