import static org.junit.Assert.*;

import org.junit.Test;


public class TextBuddyTest {

	@Test
	public void testTextBuddy() {
		TextBuddy.checkAndLoadFile(new String[]{"mytestfile.txt"});
		testCommand("add text", "added to mytestfile.txt: \"little brown fox\"", "add little brown fox");
		testCommand("display command", "1. little brown fox", "display");
		testCommand("add text", "added to mytestfile.txt: \"jumped over the moon\"", "add jumped over the moon");
		testCommand("display command", "1. little brown fox\n2. jumped over the moon", "display");
		testCommand("delete 2nd line", "deleted from mytestfile.txt: \"jumped over the moon\"", "delete 2");
		testCommand("display command", "1. little brown fox", "display");
		testCommand("clear command", "all content deleted from mytestfile.txt", "clear");
		testCommand("display empty", "mytestfile.txt is empty", "display");
		
		testCommand("invalid command", "Invalid command", "del 2");
		
		testCommand("delete invalid argument", "Invalid arguments", "delete 2 3cce wefw");
		testCommand("delete no such item", "No such item exist", "delete 2");
		
		
	}
	
	private void testCommand(String description, String expected, String command){
		assertEquals(description, expected, TextBuddy.executeCommand(command.trim().split(" "))); 
	}

}
