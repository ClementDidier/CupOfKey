package devops.cupofkey;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import devops.cupofkey.core.DataType;
import devops.cupofkey.server.DBEntry;
import devops.cupofkey.server.IntDBEntry;
import devops.cupofkey.server.StringDBEntry;

/**
 * Permet de tester les objets de type DBEntry et leurs operations
 */
public class DBEntryTest {
	
	/**
	 * Verifie l'instanciation d'une entree de type IntDBEntry
	 */
	@Test
	public void intDBEntryBasicTest()
	{
		DBEntry entry = new IntDBEntry("someKey");
		assertTrue("Verification de la cle", entry.getKey() == "someKey");
		assertTrue("Verification du type initial" , entry.getType() == DataType.INTEGER);
		assertTrue("Verification de la liste initiale" , entry.getEntry().size() == 0);
	
	}
	
	/**
	 * Verifie l'ajout d'element dans une entree de type IntDBEntry
	 */
	@Test
	public void intDBEntryAddTest()
	{
		DBEntry entry = new IntDBEntry("someKey");
		List<String> testEntries = new ArrayList<String>();
		
		entry.addEntry(testEntries);
		
		assertTrue("Verification de la liste apres ajout null" , entry.getEntry().size() == 0);
		
		testEntries.add("42");
		entry.addEntry(testEntries);
		
		assertTrue("Verification de la taille de la liste apres ajout 42" , entry.getEntry().size() == 1);
		assertTrue("Verification de la liste apres ajout 42" , entry.getEntry().get(0).equals("42"));
		
		testEntries.add("21");
		entry.addEntry(testEntries);
		
		assertTrue("Verification de la taille de la liste apres ajout 42-42-21" , entry.getEntry().size() == 3);
		assertTrue("Verification de la liste apres ajout 42-42-21" , entry.getEntry().get(0).equals("42"));
		assertTrue("Verification de la liste apres ajout 42-42-21" , entry.getEntry().get(1).equals("42"));
		assertTrue("Verification de la liste apres ajout 42-42-21" , entry.getEntry().get(2).equals("21"));
		assertTrue("Verification de la liste apres ajout 42-42-21 a l'indice 2" , entry.getEntry(2).get(0).equals("21"));

	}
	
	/**
	 * Verifie la bonne execution des operation d'addition et de soustraction sur une entree de type IntDBEntry
	 */
	@Test
	public void intDBEntryOpTest()
	{
		DBEntry entry = new IntDBEntry("someKey");
		List<String> testEntries = new ArrayList<String>();
				
		testEntries.add("42");
		entry.addEntry(testEntries);
		testEntries.add("21");
		entry.addEntry(testEntries);

		entry.increment(2);
		
		assertTrue("Verification de la liste apres incrementation" , entry.getEntry().get(0).equals("44"));
		assertTrue("Verification de la liste apres incrementation" , entry.getEntry().get(1).equals("44"));
		assertTrue("Verification de la liste apres incrementation" , entry.getEntry().get(2).equals("23"));
		
		entry.multiply(2);
		
		assertTrue("Verification de la liste apres incrementation" , entry.getEntry().get(0).equals("88"));
		assertTrue("Verification de la liste apres incrementation" , entry.getEntry().get(1).equals("88"));
		assertTrue("Verification de la liste apres incrementation" , entry.getEntry().get(2).equals("46"));
	}
	
	/**
	 * Verifie la bonne execution de la suppression d'elements sur un objet de type IntDBEntry
	 */
	@Test
	public void intDBEntryRemoveTest()
	{
		DBEntry entry = new IntDBEntry("someKey");
		List<String> testEntries = new ArrayList<String>();
				
		testEntries.add("42");
		entry.addEntry(testEntries);
		
		testEntries.add("21");
		entry.addEntry(testEntries);

		entry.increment(2);
		entry.multiply(2);

		
		entry.removeEntry(1);
		
		assertTrue("Verification de la taille de la liste apres supression de l'index 1" , entry.getEntry().size() == 2);
		assertTrue("Verification de la liste apres suppr de 1" , entry.getEntry().get(0).equals("88"));
		assertTrue("Verification de la liste apres suppr de 1" , entry.getEntry().get(1).equals("46"));		
	}
	
	/**
	 * Verifie l'instanciation d'une entree de type StringDBEntry
	 */
	@Test
	public void stringDBEntryBasicTest()
	{
		DBEntry entry = new StringDBEntry("someKey");
		assertTrue("Verification de la cle", entry.getKey() == "someKey");
		assertTrue("Verification du type initial" , entry.getType() == DataType.STRING);
		assertTrue("Verification de la liste initiale" , entry.getEntry().size() == 0);
	
	}
	
	/**
	 * Verifie l'ajout d'element dans une entree de type StringDBEntry
	 */
	@Test
	public void stringDBEntryAddTest()
	{
		DBEntry entry = new StringDBEntry("someKey");
		List<String> testEntries = new ArrayList<String>();
		
		entry.addEntry(testEntries);
		
		assertTrue("Verification de la liste apres ajout null" , entry.getEntry().size() == 0);
		
		testEntries.add("42");
		entry.addEntry(testEntries);
		
		assertTrue("Verification de la taille de la liste apres ajout 42" , entry.getEntry().size() == 1);
		assertTrue("Verification de la liste apres ajout 42" , entry.getEntry().get(0).equals("42"));
		
		testEntries.add("Hubert Bonisseur de La Bath");
		entry.addEntry(testEntries);
		
		assertTrue("Verification de la taille de la liste apres ajout 42-42-21" , entry.getEntry().size() == 3);
		assertTrue("Verification de la liste apres ajout 42-42-21" , entry.getEntry().get(0).equals("42"));
		assertTrue("Verification de la liste apres ajout 42-42-21" , entry.getEntry().get(1).equals("42"));
		assertTrue("Verification de la liste apres ajout 42-42-21" , entry.getEntry().get(2).equals("Hubert Bonisseur de La Bath"));
		assertTrue("Verification de la liste apres ajout 42-42-21 a l'indice 2" , entry.getEntry(2).get(0).equals("Hubert Bonisseur de La Bath"));

	}
	
	/**
	 * Verifie la bonne execution des non-execution des operation d'addition et de soustraction sur une entree de type StringDBEntry
	 */
	@Test
	public void stringDBEntryOpTest()
	{
		DBEntry entry = new StringDBEntry("someKey");
		List<String> testEntries = new ArrayList<String>();
				
		testEntries.add("42");
		entry.addEntry(testEntries);
		testEntries.add("Hubert Bonisseur de La Bath");
		entry.addEntry(testEntries);

		assertFalse(entry.increment(2));
		
		assertTrue("Verification de la liste apres tentative incrementation" , entry.getEntry().get(0).equals("42"));
		assertTrue("Verification de la liste apres tentative incrementation" , entry.getEntry().get(1).equals("42"));
		assertTrue("Verification de la liste apres tentative incrementation" , entry.getEntry().get(2).equals("Hubert Bonisseur de La Bath"));
		
		assertFalse(entry.multiply(2));
		
		assertTrue("Verification de la liste apres tentative incrementation" , entry.getEntry().get(0).equals("42"));
		assertTrue("Verification de la liste apres tentative incrementation" , entry.getEntry().get(1).equals("42"));
		assertTrue("Verification de la liste apres tentative incrementation" , entry.getEntry().get(2).equals("Hubert Bonisseur de La Bath"));
	}
	
	/**
	 * Verifie la bonne execution de la suppression d'elements sur un objet de type StringDBEntry
	 */
	@Test
	public void stringDBEntryRemoveTest()
	{
		DBEntry entry = new StringDBEntry("someKey");
		List<String> testEntries = new ArrayList<String>();
		testEntries.add("42");
		entry.addEntry(testEntries);
		testEntries.add("Hubert Bonisseur de La Bath");
		entry.addEntry(testEntries);
		entry.increment(2);
		entry.multiply(2);
		
		entry.removeEntry(1);
		
		assertTrue("Verification de la taille de la liste apres supression de l'index 1" , entry.getEntry().size() == 2);
		assertTrue("Verification de la liste apres suppr de 1" , entry.getEntry().get(0).equals("42"));
		assertTrue("Verification de la liste apres suppr de 1" , entry.getEntry().get(1).equals("Hubert Bonisseur de La Bath"));		
	}
}
