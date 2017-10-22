package utils;

//Filter by email
//Sort by Last name
//Changes by ID
public class MenuUtils {
    public static int getMenuItem(int min, int max){
        System.out.print("Item: ");
        return HelpUtils.getInt(min, max);
    }

   /* public static void printStartMenu(){
        System.out.println("\n----Start menu----");
        System.out.println("1 - Connect to database");
        System.out.println("0 - Exit");
        System.out.println("------------------");
    }*/

    public static void printMainMenu(){
        System.out.println("\n----Main  menu----");
        System.out.println("1 - Add new user info");
        System.out.println("2 - Edit user info");
        System.out.println("3 - Remove user info");
        System.out.println("4 - Remove several users info");
        System.out.println("5 - Print users");
        System.out.println("6 - Sort users");
        System.out.println("7 - Filter users by email");
        System.out.println("8 - Upload from file");
        System.out.println("0 - Go to Main Menu");
        System.out.println("------------------");
    }
/*
    public static void printProductsOptions(){
        System.out.println("------------------");
        System.out.println("1 - Fruit");
        System.out.println("2 - Book");
        System.out.println("3 - Canned food");
        System.out.println("0 - Go to Main Menu");
        System.out.println("------------------");
    }*/

    public static void printFilterCriteria() {
        System.out.println("------------------");
        System.out.println("1 - Starts with ...");
        System.out.println("2 - Ends with ...");
        System.out.println("0 - Go to Main Menu");
        System.out.println("------------------");
    }

}
