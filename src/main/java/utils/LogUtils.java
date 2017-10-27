package utils;

public class LogUtils {
    public static void printEmptyResMessage(){
        System.out.println("No data to work with.");
    }

    public static void printInputIDForActionMessage(String action){
        System.out.print("Input ID of the item you want to " + action + "\n ID: ");
    }
    public static void printActionResult(int itemID, String action){
        System.out.println("Item with ID = " + itemID + " was " + action + ".");
    }
    public static void printUserNotFoundMessage(int userID) {
        System.out.println("User with ID = " + userID + " wasn't found");
    }
    public static void printUserAlreadyExistsdMessage(String field) {
        System.out.println("User with such " + field + " already exists. Change the " + field);
    }
    public static void printWrongInputFormatMessage(){
        System.out.println("Wrong input!");
    }

    public static void printWrongInputCharsFormatMessage(){
        System.out.println("Wrong chars in the input!");
    }

    public static void printWrongInputFormatMessage(String input){
        System.out.println("Wrong input! (" + input + ")");
    }
    public static void printNumberOutOfRange(int min, int max){
        System.out.println("The number is out of range (" + min + "..." + max + "). Repeat.");
    }
    public static void printNumberOutOfRange(double min, double max){
        System.out.println("The number is out of range (" + min + "..." + max + "). Repeat.");
    }
    public static void printEmptyInputError(){
        System.out.println("Empty input is not allowed! Repeat.");
    }
    public static void printConnectionError(){
        System.out.println("Problems while connecting to database");
    }

}
