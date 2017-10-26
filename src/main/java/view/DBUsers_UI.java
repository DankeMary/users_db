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

    public static void start() {
        try {
            db = new DBUsers();

            int choice = 1;
            while (choice != 0) {
                MenuUtils.printMainMenu();
                choice = MenuUtils.getMenuItem(0, 9);
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
            db.disconnect();
        } catch (SQLException e) {
            System.out.println("Problems while connecting to database");
        }
    }

    private static void addUser() {
        System.out.println("Input data");
        User newUser = HelpUtils.getUser();

        while (true) {
            try {
                db.addInfo(newUser);
                System.out.println("New data was added.");
                return;
            } catch (LoginException e) {
                System.out.println("User with such login already exists! Would you like to change it?");
                if (HelpUtils.getBool(false)) {
                    System.out.print("Login: ");
                    newUser.setLogin(HelpUtils.getString());
                } else return;
            } catch (EmailException e) {
                System.out.println("User with such email already exists! Would you like to change it?");
                if (HelpUtils.getBool(false)) {
                    System.out.print("Email: ");
                    newUser.setEmail(HelpUtils.getString());
                } else return;
            }
        }
    }

    private static void editUser() {
        if (db.isEmpty()) {
            LogUtils.printEmptyResMessage();
            return;
        }

        LogUtils.printInputIDForActionMessage("edit");
        int userID = HelpUtils.getInt(1, Integer.MAX_VALUE);
        User user = db.getUserByID(userID);
        if (user == null) {
            System.out.println("User with such ID wasn't found"); //LogUtils.printItemNotFoundMessage(userID);
            return;
        }
        HelpUtils.getUser(user);

        while (true) {
            if (db.loginExists(user.getId(), user.getLogin()) > 0) {
                System.out.println("User with such login already exists! Change the login");
                System.out.print("Login: ");
                user.setLogin(HelpUtils.getString());
                continue;
            }
            if (db.emailExists(user.getId(), user.getEmail()) > 0) {
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

    private static void deleteUser() {
        if (db.isEmpty()) {
            LogUtils.printEmptyResMessage();
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
            LogUtils.printEmptyResMessage();
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

    private static void sortUsers() {
        if (db.isEmpty()) {
            LogUtils.printEmptyResMessage();
            return;
        }
        printUsers(db.sortUsers());
    }

    private static void filterUsers() {
        if (db.isEmpty()) {
            LogUtils.printEmptyResMessage();
            return;
        }
        MenuUtils.printFilterCriteria();
        int criterion = MenuUtils.getMenuItem(0, 2);
        if (criterion == 0)
            return;

        System.out.print("Input the criterion string: ");
        String criterionStr = HelpUtils.getString();
        String query;
        if (criterion == 1)
            query = criterionStr + "%";
        else
            query = "%" + criterionStr;

        ResultSet res = db.filterUsers(query);
        printUsers(res);

    }

    public static void readFromFile() {
        System.out.println("Input filename (default - data.csv)");
        String fileName = HelpUtils.getFileName("data.csv");
        if (fileName == null)
            System.out.println("Filename has prohibited chars!");
        else {
            if (db.importInfo(fileName))
                System.out.println("Data from file was uploaded");
            else
                System.out.println("File with such name wasn't found!");
        }
    }

    public static void saveToFile() {
        if (db.isEmpty()) {
            LogUtils.printEmptyResMessage();
            return;
        }

        System.out.println("Input filename (default - test.csv)");
        String fileName = HelpUtils.getFileName("test.csv");
        if (fileName == null)
            System.out.println("Filename has prohibited chars!");
        else {
            if (db.exportInfo(fileName))
                System.out.println("Data from database was saved to file " + fileName);
            else
                System.out.println("File with such name wasn't found!");
        }

    }

    private static void printUsers() {
        ResultSet rs = db.getAllUsers();
        printUsers(rs);
    }

    private static void printUsers(ResultSet rs) {
        try {
            if (!rs.next())
                LogUtils.printEmptyResMessage();
            else {
                rs.beforeFirst();
                while (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt(1));
                    user.setFirstName(rs.getString(2));
                    user.setLastName(rs.getString(3));
                    user.setLogin(rs.getString(4));
                    user.setEmail(rs.getString(5));
                    System.out.println(user);
                }
            }
        } catch (SQLException e) {
            System.out.println("Problems while connecting to database");
        }
    }
}
