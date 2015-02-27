import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TextBuddyTest {

	@Before
	public void setUp() {
		TextBuddy.checkAndLoadFile(new String[] { "mytestfile.txt" });
	}

	@Test
	public void testAddRegular() {
		String[] command = { "add", "little brown fox" };
		String expected = "added to mytestfile.txt: \"little brown fox\"";
		assertEquals(expected,
				TextBuddy.executeCommand("mytestfile.txt", command));
	}

	@Test
	public void testAddEmpty() {
		String[] command = { "add" };
		String expected = "No text to add";
		assertEquals(expected,
				TextBuddy.executeCommand("mytestfile.txt", command));
	}

	@Test
	public void testDeleteRegular() {
		String[] addCommands = { "add", "little brown fox" };
		TextBuddy.executeCommand("mytestfile.txt", addCommands);
		String[] addCommands2 = { "add", "jumped over the moon" };
		TextBuddy.executeCommand("mytestfile.txt", addCommands2);

		String[] command = { "delete", "2" };
		String expected = "deleted from mytestfile.txt: \"jumped over the moon\"";
		assertEquals(expected,
				TextBuddy.executeCommand("mytestfile.txt", command));
	}

	@Test
	public void testDeleteOutOfRangeIndex() {
		String[] addCommands = { "add", "little brown fox" };
		TextBuddy.executeCommand("mytestfile.txt", addCommands);
		String[] addCommands2 = { "add", "jumped over the moon" };
		TextBuddy.executeCommand("mytestfile.txt", addCommands2);

		String[] command = { "delete", "5" };
		String expected = "No such item exist";
		assertEquals(expected,
				TextBuddy.executeCommand("mytestfile.txt", command));
	}

	@Test
	public void testDeleteAlphanumeric() {
		String[] addCommands = { "add", "little brown fox" };
		TextBuddy.executeCommand("mytestfile.txt", addCommands);
		String[] addCommands2 = { "add", "jumped over the moon" };
		TextBuddy.executeCommand("mytestfile.txt", addCommands2);

		String[] command = { "delete", "3cce wefw" };
		String expected = "Invalid arguments";
		assertEquals(expected,
				TextBuddy.executeCommand("mytestfile.txt", command));
	}

	@Test
	public void testDeleteEmpty() {
		String[] addCommands = { "add", "little brown fox" };
		TextBuddy.executeCommand("mytestfile.txt", addCommands);
		String[] addCommands2 = { "add", "jumped over the moon" };
		TextBuddy.executeCommand("mytestfile.txt", addCommands2);

		String[] command = { "delete" };
		String expected = "Invalid arguments";
		assertEquals(expected,
				TextBuddy.executeCommand("mytestfile.txt", command));
	}

	@Test
	public void testDisplay() {
		String[] addCommands = { "add", "little brown fox" };
		TextBuddy.executeCommand("mytestfile.txt", addCommands);
		String[] addCommands2 = { "add", "jumped over the moon" };
		TextBuddy.executeCommand("mytestfile.txt", addCommands2);

		String[] command = { "display" };
		String expected = "1. little brown fox\n2. jumped over the moon";
		assertEquals(expected,
				TextBuddy.executeCommand("mytestfile.txt", command));
	}

	@Test
	public void testDisplayEmpty() {
		String[] command = { "display" };
		String expected = "mytestfile.txt is empty";
		assertEquals(expected,
				TextBuddy.executeCommand("mytestfile.txt", command));
	}

	@Test
	public void testClearRegular() {
		String[] addCommands = { "add", "little brown fox" };
		TextBuddy.executeCommand("mytestfile.txt", addCommands);
		String[] addCommands2 = { "add", "jumped over the moon" };
		TextBuddy.executeCommand("mytestfile.txt", addCommands2);
		String[] command = { "clear" };
		String expected = "all content deleted from mytestfile.txt";
		assertEquals(expected,
				TextBuddy.executeCommand("mytestfile.txt", command));
	}

	@Test
	public void testClearWithArguments() {
		String[] addCommands = { "add", "little brown fox" };
		TextBuddy.executeCommand("mytestfile.txt", addCommands);
		String[] addCommands2 = { "add", "jumped over the moon" };
		TextBuddy.executeCommand("mytestfile.txt", addCommands2);
		String[] command = { "clear", "abc" };
		String expected = "Invalid command for clear. Please remove the contents after the word 'clear'";
		assertEquals(expected,
				TextBuddy.executeCommand("mytestfile.txt", command));
	}

	@Test
	public void testSortRegular() {
		String[] addCommands = { "add", "little brown fox" };
		TextBuddy.executeCommand("mytestfile.txt", addCommands);
		String[] addCommands2 = { "add", "jumped over the moon" };
		TextBuddy.executeCommand("mytestfile.txt", addCommands2);
		String[] command = { "sort" };
		String expected = "Sort complete";
		assertEquals(expected,
				TextBuddy.executeCommand("mytestfile.txt", command));
	}

	@Test
	public void testSortEmptyItem() {
		String[] command = { "sort" };
		String expected = "There is no items to sort";
		assertEquals(expected,
				TextBuddy.executeCommand("mytestfile.txt", command));
	}

	@Test
	public void testSortRegularWithDisplay() {
		String[] addCommands = { "add", "little brown fox" };
		TextBuddy.executeCommand("mytestfile.txt", addCommands);
		String[] addCommands2 = { "add", "jumped over the moon" };
		TextBuddy.executeCommand("mytestfile.txt", addCommands2);
		String[] commandSort = { "sort" };
		TextBuddy.executeCommand("mytestfile.txt", commandSort);
		String[] commandDisplay = { "display" };
		String expected = "1. jumped over the moon\n2. little brown fox";
		assertEquals(expected,
				TextBuddy.executeCommand("mytestfile.txt", commandDisplay));
	}

	@Test
	public void testSearchRegular() {
		String[] addCommands = { "add", "little brown fox" };
		TextBuddy.executeCommand("mytestfile.txt", addCommands);
		String[] addCommands2 = { "add", "jumped over the moon" };
		TextBuddy.executeCommand("mytestfile.txt", addCommands2);
		String[] command = { "search", "over" };
		String expected = "1. jumped over the moon";
		assertEquals(expected,
				TextBuddy.executeCommand("mytestfile.txt", command));
	}

	@Test
	public void testSearchNoMatch() {
		String[] addCommands = { "add", "little brown fox" };
		TextBuddy.executeCommand("mytestfile.txt", addCommands);
		String[] addCommands2 = { "add", "jumped over the moon" };
		TextBuddy.executeCommand("mytestfile.txt", addCommands2);
		String[] command = { "search", "hello" };
		String expected = "No matches";
		assertEquals(expected,
				TextBuddy.executeCommand("mytestfile.txt", command));
	}

	@Test
	public void testSearchEmpty() {
		String[] addCommands = { "add", "little brown fox" };
		TextBuddy.executeCommand("mytestfile.txt", addCommands);
		String[] addCommands2 = { "add", "jumped over the moon" };
		TextBuddy.executeCommand("mytestfile.txt", addCommands2);
		String[] command = { "search" };
		String expected = "Invalid arguments for search. Please enter a word after the 'search' word.";
		assertEquals(expected,
				TextBuddy.executeCommand("mytestfile.txt", command));
	}

	@Test
	public void testSearchMoreThanTwoArgs() {
		String[] addCommands = { "add", "little brown fox" };
		TextBuddy.executeCommand("mytestfile.txt", addCommands);
		String[] addCommands2 = { "add", "jumped over the moon" };
		TextBuddy.executeCommand("mytestfile.txt", addCommands2);
		String[] command = { "search", "word", "abc" };
		String expected = "Invalid arguments for search. Please enter a word after the 'search' word.";
		assertEquals(expected,
				TextBuddy.executeCommand("mytestfile.txt", command));
	}

	@After
	public void tearDown() {
		TextBuddy.clearArrayList();
		File txtFile = new File("mytestfile.txt");
		txtFile.delete();
	}

}
