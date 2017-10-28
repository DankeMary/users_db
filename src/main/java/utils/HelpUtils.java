package utils;

import entity.User;

import java.util.Scanner;

public class HelpUtils {
    private static final String PROHIBITED_CHARS = "~#%&*{}*\\/:<>?+|";

    public static boolean isInRange(int num, int min, int max) {
        return (num >= min && num <= max);
    }

    public static Integer getIntFromString(String str, int min, int max) {
        try {
            Integer num = Integer.parseInt(str.trim());
            if (isInRange(num, min, max))
                return num;
            else
                LogUtils.printNumberOutOfRange(min, max);
        } catch (NumberFormatException e) {
            LogUtils.printWrongInputFormatMessage();
        }
        return null;
    }

    public static Integer getInt(int min, int max) {
        Scanner in = new Scanner(System.in);
        String str;

        while (true) {
            str = in.nextLine().trim();
            if (str.equals(""))
                LogUtils.printEmptyInputError();
            else {
                Integer num = getIntFromString(str, min, max);
                if (num != null)
                    return num;
            }
        }
    }

    public static int countMatches(String str, char ch) {
        int cnt = 0;
        for (int i = 0; i < str.length(); i++)
            if (str.charAt(i) == ch)
                cnt++;
        return cnt;
    }

    public static boolean checkName(String str) {
        char[] chars = str.toCharArray();

        for (char c : chars) {
            if (!Character.isLetter(c) && c != ' ' && c != '-') {
                return false;
            }
        }
        return true;
    }

    public static boolean checkEmail(String email) {
        return (email.length() <= 30 && email.indexOf('@') != -1 && email.indexOf('.') != -1
                && countMatches(email, '@') == 1 && email.indexOf(' ') == -1);
    }

    public static boolean checkLogin(String login) {
        return (login.length() <= 30 && login.indexOf(' ') == -1);
    }

    public static String formatString(String str) {
        return (str.equals("")) ? str : str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    public static String getString() {
        Scanner in = new Scanner(System.in);

        String str;
        while (true) {
            str = in.nextLine().trim();
            if (str.equals(""))
                LogUtils.printEmptyInputError();
            else
                return str.trim().toLowerCase();
        }
    }

    public static String getString(String basic) {
        Scanner in = new Scanner(System.in);
        while (true) {
            String str = in.nextLine().trim();
            if (str.equals(""))
                return basic;
            else
                return str.trim().toLowerCase();
        }
    }

    public static String getName(String basic) {
        String name;
        while (true) {
            name = getString(basic).trim();

            if (checkName(name))
                return formatString(name);
            else
                LogUtils.printWrongInputCharsFormatMessage();
        }
    }

    public static String getLogin() {
        String login;
        while (true) {
            login = getString().trim();

            if (checkLogin(login))
                return login.toLowerCase();
            else
                LogUtils.printWrongInputFormatMessage();
        }
    }

    public static String getLogin(String basic) {
        String login;
        while (true) {
            login = getString(basic).trim();

            if (checkLogin(login))
                return login.toLowerCase();
            else
                LogUtils.printWrongInputFormatMessage();
        }
    }

    public static String getEmail() {
        String email;
        while (true) {
            email = getString().trim();

            if (checkEmail(email))
                return email.toLowerCase();
            else
                LogUtils.printWrongInputFormatMessage();
        }
    }

    public static String getEmail(String basic) {
        String email;
        while (true) {
            email = getString(basic).trim();

            if (checkEmail(email))
                return email.toLowerCase();
            else
                LogUtils.printWrongInputFormatMessage();
        }
    }

    public static String getFileName(String basic) {
        String fileName = HelpUtils.getString(basic);

        for (int i = 0; i < fileName.length(); i++)
            if (PROHIBITED_CHARS.contains(fileName.substring(i, i + 1)))
                return null;
        return fileName;
    }

    public static boolean getBool(boolean basic) {
        Scanner in = new Scanner(System.in);
        String str = in.nextLine().trim();
        if (str.equals("")) {
            return basic;
        } else
            return (str.equals("+") || str.equals("yes") || str.equals("y"));
    }

    public static User getUser() {
        User aUser = new User();
        System.out.print("First Name: ");
        aUser.setFirstName(getName(""));

        System.out.print("Last Name: ");
        aUser.setLastName(getName(""));

        System.out.print("Login: ");
        aUser.setLogin(getLogin());

        System.out.print("Email: ");
        aUser.setEmail(getEmail());
        return aUser;
    }

    public static void getUser(User basic) {
        System.out.print("First Name: ");
        basic.setFirstName(getName(basic.getFirstName()));

        System.out.print("Last Name: ");
        basic.setLastName(getName(basic.getLastName()));

        System.out.print("Login: ");
        basic.setLogin(getLogin(basic.getLogin()));

        System.out.print("Email: ");
        basic.setEmail(getEmail(basic.getEmail()));
    }


}