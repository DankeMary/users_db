package utils;

public class LogUtils {
    public static void printEmptyDBMessage(){
        System.out.println("Database is empty.");
    }

    public static void printInputIDForActionMessage(String action){
        System.out.print("Input ID of the item you want to " + action + "\n ID: ");
    }
    public static void printActionResult(int itemID, String action){
        System.out.println("Item with ID = " + itemID + " was " + action + ".");
    }
    public static void printItemNotFoundMessage(int itemID) {
        System.out.println("Item with ID = " + itemID + " wasn't found");
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
}
