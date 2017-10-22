package view;

import database.DBUsers;
import entity.User;
import exception.EmailException;
import exception.LoginException;
import utils.HelpUtils;
import utils.LogUtils;
import utils.MenuUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class DBUsers_UI {
    private static DBUsers db;

    public DBUsers_UI(){
        db = new DBUsers();
    }

    public static void start(){
        int choice = 1;
        while (choice != 0) {
            MenuUtils.printMainMenu();
            choice = MenuUtils.getMenuItem(0, 7);
            switch (choice) {
                case 1:
                    addUser();
                    break;
                case 2:
                    editUser();
                    break;
                case 3:
                    deleteUser();
                    break;
                case 4:
                    deleteSeveralUsers();
                    break;
                case 5:
                    printUsers();
                    break;
                case 6:
                    sortUsers();
                    break;
                case 7:
                    filterUsers();
                    break;
                case 8:
                    readFromFile();
                    break;
                case 9:
                    saveToFile();
                    break;
                case 0:
                default:
                    break;
            }
        }
    }
    private static void addUser() {
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

    private static void editUser(){
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

    private static void deleteUser(){
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
    private static void deleteUser(String id) {
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

    private static void deleteSeveralUsers() {
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

    private static void sortUsers(){
        if (db.isEmpty()) {
            System.out.println("Database is empty");//LogUtils.printEmptyListMessage();
            return;
        }
        printUsers(db.sortUsers());
    }
    private static void filterUsers(){
        if (db.isEmpty())
            LogUtils.printEmptyListMessage();
        else {
            MenuUtils.printFilterCriteria();
            int criterion = MenuUtils.getMenuItem(0, 2);
            if (criterion == 0)
                return;

            System.out.print("Input the criterion string: ");
            String criterionStr = HelpUtils.getString();
            String query;
            if(criterion == 1)
                query = criterionStr + "%";
            else
                query = "%" + criterionStr;

            ResultSet res = db.filterUsers(query);
            printUsers(res);
        }
    }

    private static void readFromFile(){}

    private static void saveToFile(){}

    private static void printUsers() {
        ResultSet rs = db.getAllUsers();
        printUsers(rs);
    }

    private static void printUsers(ResultSet rs){
        try {
            if (!rs.next())
                System.out.println("No data was found");
            else {
                rs.beforeFirst();
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
        }
        catch(SQLException e) {}
    }
}
