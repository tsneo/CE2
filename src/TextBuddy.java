/** This program is done by
 * Name: Neo Tian Soon
 * Matriculation Number: A0111935L
 * Tutorial Grp: F13
 */

/**
 * This class is used to store text message and display it on the text UI Every
 * text message will be stored in the text file and the user can remove a text
 * from it.
 * For example, when you execute the file with the text file 'mytextfile.txt'
 * 
 Welcome to TextBuddy. mytextfile.txt is ready to use
 command: add little brown fox
 added to mytextfile.txt: "little brown fox"
 command: display
 1. little brown fox
 command: add jumped over the moon
 added to mytextfile.txt: "jumped over the moon"
 command: display
 1. little brown fox
 2. jumped over the moon
 command: delete 2	
 deleted from mytextfile.txt: "jumped over the moon"
 command: display
 1. little brown fox
 command: clear
 all content deleted from mytextfile.txt
 command: display
 mytextfile.txt is empty
 command: exit


 * @author Neo Tian Soon
 * 
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class TextBuddy {

	// This arraylist will store the list of texts
	private static ArrayList<String> strList = new ArrayList<String>();

	// This string will store the filename from the argument
	//private static String filename;

	// List of Display Messages for welcome message, add, delete, clear and file
	// empty
	private static final String MESSAGE_WELCOME = "Welcome to TextBuddy. %1$s is ready to use";
	private static final String MESSAGE_ADDED = "added to %1$s: \"%2$s\"";
	private static final String MESSAGE_DELETED = "deleted from %1$s: \"%2$s\"";
	private static final String MESSAGE_CLEARED = "all content deleted from %1$s";
	private static final String MESSAGE_FILEISEMPTY = "%1$s is empty";

	private static Scanner scanner = new Scanner(System.in);

	/*
	 * This will store the list of commands
	 */
	public enum Command_Types {
		ADD, DELETE, DISPLAY, CLEAR, EXIT, INVALID, SEARCH, SORT
	}

	public static void main(String[] args) {

		// call method to check if file exists
		String fileName = checkAndLoadFile(args);

		// print out welcome message
		printMessage(String.format(MESSAGE_WELCOME, fileName));

		// call method for user entering commands
		commandExecutionUntilExit(fileName);
	}

	/*
	 * This method set up the Command Interface and exit if the user enter exit
	 * command
	 */
	private static void commandExecutionUntilExit(String fileName) {
		String[] inputCmd;
		System.out.print("command: ");
		// user will enter command here until exit is being read
		while (true) {
			inputCmd = scanner.nextLine().trim().split(" ");
			printMessage(executeCommand(fileName, inputCmd));
			System.out.print("command: ");
		}
	}
	
	public static String executeCommand(String fileName, String[] cmd){
		Command_Types commandType = determineCommandType(cmd);

		switch (commandType) {
		case ADD:
			return addText(fileName, cmd);
		case DELETE:
			return deleteText(fileName, cmd);
		case DISPLAY:
			return display(fileName, cmd);
		case CLEAR:
			return clearContents(fileName, cmd);
		case SEARCH:
			return searchAndReturnList(cmd);
		case SORT:
			return sortArrayList(fileName, cmd);
		case INVALID:
			return "Invalid command";
		case EXIT:
			System.exit(0);
			break;
		default:
			// throw an error if the command is not recognized
			return "Unrecognized command type";
		}
		return "No command";
	}

	// This method determine whether it is a command type
	private static Command_Types determineCommandType(String[] commandTypeString) {
		if (commandTypeString == null || commandTypeString.length == 0) {
			return Command_Types.INVALID;
		}

		if (commandTypeString[0].equalsIgnoreCase("add")) {
			return Command_Types.ADD;
		} else if (commandTypeString[0].equalsIgnoreCase("delete")) {
			return Command_Types.DELETE;
		} else if (commandTypeString[0].equalsIgnoreCase("display")) {
			return Command_Types.DISPLAY;
		} else if (commandTypeString[0].equalsIgnoreCase("clear")) {
			return Command_Types.CLEAR;
		} else if (commandTypeString[0].equalsIgnoreCase("exit")) {
			return Command_Types.EXIT;
		} else if (commandTypeString[0].equalsIgnoreCase("search")) {
			return Command_Types.SEARCH;
		} else if (commandTypeString[0].equalsIgnoreCase("sort")) {
			return Command_Types.SORT;
		} else {
			return Command_Types.INVALID;
		}

	}

	// Check if file exists and load it to a list
	public static String checkAndLoadFile(String[] args) {
		// Call to check argument
		exitIfNoArgument(args);

		// get the filename
		String fileName = args[0];

		// Call to check for file format
		exitIfWrongFileFormat(fileName);

		File filepath = new File(fileName);

		/*
		 * Check if the file exists, it will load the data into the arraylist
		 * else it will create a new file
		 */
		if (filepath.exists() && !filepath.isDirectory()) {
			loadToArrayList(fileName);
		} else {
			try {
				filepath.createNewFile();
			} catch (IOException e) {
				printErrorMessageAndExit(e.toString());
			}
		}
		return fileName;
	}

	// This method will check whether there is an argument input
	private static void exitIfNoArgument(String[] args) {
		if (args.length == 0) {
			printErrorMessageAndExit("There is no argument.");
		}
	}

	// This method will check if the file is in correct format
	// and exit if incorrect
	private static void exitIfWrongFileFormat(String fileName) {
		boolean isFileContainsADot = fileName.contains(".");
		int fileExtLength = fileName.length() - fileName.indexOf(".");
		String isFileExtCorrect = fileName.substring(fileName.length() - 4,
				fileName.length());

		if (!(isFileContainsADot) || !(fileExtLength == 4)
				|| !(isFileExtCorrect.equals(".txt"))) {
			printErrorMessageAndExit("Wrong file format");
		}

	}

	// This method will load the contents from the text file to ArrayList
	private static void loadToArrayList(String fileName) {
		try {
			FileReader reader = new FileReader(fileName);
			BufferedReader bufferRead = new BufferedReader(reader);
			String txtLine = "";
			try {
				while ((txtLine = bufferRead.readLine()) != null) {
					strList.add(txtLine);
				}
			} catch (IOException e) {
				printErrorMessageAndExit(e.toString());
			}

		} catch (FileNotFoundException e) {
			printErrorMessageAndExit(e.toString());
		}
	}

	// Add text to file
	private static String addText(String fileName, String[] inputArr) {
		if(inputArr.length < 2){
			return "No text to add";
		}
		
		String txtToAdd = "";// declare a string to put the words into it

		// loop and write it to txtToAdd
		for (int i = 1; i < inputArr.length; i++) {
			txtToAdd += inputArr[i];// append or add on

			// check if can add a space after the last letter
			if ((i + 1) != inputArr.length){
				txtToAdd += " ";
			}
		}

		// Call and add the text
		addToArrayList(txtToAdd);

		// call to write arraylist to text file
		writeToFile(fileName);
		// call to print the added item message
		return String.format(MESSAGE_ADDED, fileName, txtToAdd);
	}

	/*
	 * This method will add text to arraylist
	 */
	private static void addToArrayList(String text) {
		strList.add(text);
	}


	/**
	 * This method will remove a line from the text file
	 * @param inputCmd
	 * @return
	 */
	private static String deleteText(String fileName, String[] inputCmd) {
		/*
		 * This will check if the argument has only 2, 1 is delete word and
		 * another one is the index
		 */
		if (inputCmd.length != 2 || !IsStringAnInteger(inputCmd[1])){
			return "Invalid arguments";
		}
			
		int listIndex = Integer.parseInt(inputCmd[1]);

		/*
		 * This checks for the position of the arraylist
		 */
		if (listIndex > 0 && listIndex <= strList.size()) {
			String removedText = strList.get(listIndex - 1);
			strList.remove(listIndex - 1);
			writeToFile(fileName);
			return String.format(MESSAGE_DELETED, fileName, removedText);
		}
		
		return "No such item exist";

	}
	
	private static boolean IsStringAnInteger(String inputStr){
		try { 
	        Integer.parseInt(inputStr); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }

		return true;
	}

	/**
	 * This will display the list of text stored in the file
	 * @param fileName the filename which use to display as a text
	 * @param cmd the cmd input for verification of the command
	 * @return a list of text
	 */
	private static String display(String fileName, String[] cmd) {
		if(cmd.length != 1){
			return "Invalid command for display. Please remove the contents after the word 'display'";
		}
		
		if (strList.isEmpty()){
			return String.format(MESSAGE_FILEISEMPTY, fileName);
		}
		String displayText = new String();
		for (int i = 0; i < strList.size(); i++) {
			displayText += (i + 1) + ". " + strList.get(i).toString();
			if((i+1) != strList.size()){
				displayText += "\n";
			}
		}
		return displayText;

	}

	// clear all contents
	public static String clearContents(String fileName, String[] cmd) {
		if(cmd.length != 1){
			return "Invalid command for clear. Please remove the contents after the word 'clear'";
		}
		
		clearArrayList();
		try {
			FileWriter fw = new FileWriter(fileName);// setup a file writer with
														// nothing inside
			fw.close();
		} catch (IOException e) {
			printErrorMessageAndExit(e.toString());
			return "Failed to clear";
		}
		return String.format(MESSAGE_CLEARED, fileName);
	}
	
	/**
	 * This method clear the contents from the AL
	 */
	private static void clearArrayList(){
		strList.clear();// clear all contents from arraylist
	}

	// write contents to the text file
	private static void writeToFile(String fileName) {

		// Add the string to the file
		try {
			FileWriter fw = new FileWriter(fileName);// setup a file writer
			fw.flush();
			BufferedWriter bw = new BufferedWriter(fw);
			for (int i = 0; i < strList.size(); i++) {
				bw.write(strList.get(i).toString());
				bw.newLine();
			}
			bw.close();
			fw.close();
		} catch (IOException e) {
			printErrorMessageAndExit(e.toString());
		}
	}

	/*
	 * This method prints out the string with the given string parameter
	 */
	private static void printMessage(String message) {
		System.out.println(message);
	}

	/*
	 * This method will print out the error message before exiting the system
	 */
	private static void printErrorMessageAndExit(String message) {
		System.out.println(message);
		System.exit(0);
	}
	
	/*
	 * This method will search for the word and print out the results
	 */
	private static String searchAndReturnList(String[] cmd){
		if (cmd.length != 2){
			return "Invalid arguments for search";
		}
		
		String searchWord = cmd[1];
		String filteredLines = new String();
		int indexNum = 1;
		for(int i = 0; i < strList.size(); i++){
			if(compareString(searchWord, strList.get(i))){
				filteredLines += indexNum + ". " + strList.get(i) + "\n";
				indexNum++;
			}
		}
		if(filteredLines.isEmpty()){
			return "No matches";
		}
		
		filteredLines = filteredLines.substring(0, filteredLines.length() -1);
		return filteredLines;
	}
	
	/*
	 * This method compare the string with the given word and return
	 * true if there is a match, false if not
	 */
	private static boolean compareString(String searchWord, String compareWord){
		return compareWord.toLowerCase().contains(searchWord.toLowerCase());
	}
	
	/*
	 * This method sort the arrayList
	 */
	private static String sortArrayList(String fileName, String[] cmd){
		if(cmd.length != 1){
			return "Invalid command for sort. Please remove the contents after the word 'sort'";
		}
		
		if(strList.size() == 0){
			return "There is no items to sort";
		}
		
		Collections.sort(strList);
		writeToFile(fileName);
		return "Sort complete";
	}
}
