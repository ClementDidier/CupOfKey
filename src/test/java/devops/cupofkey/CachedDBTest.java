package devops.cupofkey;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import devops.cupofkey.core.DataType;
import devops.cupofkey.server.CachedDB;
import devops.cupofkey.server.DBEntry;
import devops.cupofkey.server.IntDBEntry;

public class CachedDBTest {

	/**
	 * Verifie l'instanciation et le lancement du service du cache de la base de donnees
	 * Accorde un délai maximal pour le lancement et l'arret du Thread
	 * @throws InterruptedException si le test est interrompu lors de l'attente du Thread CachedDB
	 */
	@Test(timeout=5000)
	public void cachedDBBasicTest() throws InterruptedException{
		CachedDB db = new CachedDB();
		db.start();
		assertTrue("Taille de la mastermap vide" , db.size() == 0);
		assertTrue("Le Thread du service de cache est bien en execution" , db.isAlive());
		db.interrupt();
		db.join();
		assertFalse("Le Thread du service de cache est bien termine" , db.isAlive());
	}
	
	/**
	 * Verifie que l'ajout et la recuperation simple d'une entree dans la base de donnee avec cache sont realises correctement
	 */
	@Test(timeout=5000)
	public void cachedDBAddTest(){
		CachedDB db = new CachedDB();
		db.start();
		
		DBEntry entry = new IntDBEntry("someKey");
		DBEntry otherEntry = new IntDBEntry("someOtherKey");
		
		List<String>testList = new ArrayList<String>();
		testList.add("42");
		entry.addEntry(testList);
		testList.add("21");
		otherEntry.addEntry(testList);
		
		assertTrue(db.put("someKey", entry));
		assertTrue(db.put("someOtherKey", otherEntry));
		
		assertTrue("Verification de la recuperation de entry" , db.get("someKey").getKey().equals("someKey"));
		assertTrue("Verification de la recuperation de entry" , db.get("someKey").getEntry().size() == 1);
		assertTrue("Verification de la recuperation de entry" , db.get("someKey").getEntry().get(0).equals("42"));
		
		assertTrue("Verification de la recuperation de otherEntry" , db.get("someOtherKey").getKey().equals("someOtherKey"));
		assertTrue("Verification de la recuperation de otherEntry" , db.get("someOtherKey").getEntry().size() == 2);
		assertTrue("Verification de la recuperation de otherEntry" , db.get("someOtherKey").getEntry().get(1).equals("21"));
		
		db.interrupt();
	}
	
	/**
	 * Verifie que la recuperation d'element n'existant pas retourne null
	 * Verifie egalement le bon fonctionnement du test d'existance d'une cle dans la base
	 */
	@Test(timeout=5000)
	public void cachedDBFailAddTest(){
		CachedDB db = new CachedDB();
		db.start();
		
		DBEntry entry = new IntDBEntry("someKey");
		DBEntry otherEntry = new IntDBEntry("someOtherKey");
		
		List<String>testList = new ArrayList<String>();
		testList.add("42");
		entry.addEntry(testList);
		testList.add("21");
		otherEntry.addEntry(testList);
		
		assertTrue(db.put("someKey", entry));
		assertTrue(db.put("someOtherKey", otherEntry));
		
		assertTrue("Verification que la Base contient une entree someKey" , db.containsKey("someKey"));
		assertFalse(
				"Verification de la Base ne contient pas d'entree 'Hubert Bonisseur de La Bath'" ,
				db.containsKey("Hubert Bonisseur de La Bath")
		);
		
		assertTrue("Verification de la recuperation de Hubert" , db.get("Hubert") == null);
		
		db.interrupt();
	}
	
	/**
	 * Verifie que la suppression d'un element dans la base de donnees avec cache fonctionne
	 */
	public void cachedDBRemoveTest(){
		CachedDB db = new CachedDB();
		db.start();
		
		DBEntry entry = new IntDBEntry("someKey");
		DBEntry otherEntry = new IntDBEntry("someOtherKey");
		
		List<String>testList = new ArrayList<String>();
		testList.add("42");
		entry.addEntry(testList);
		testList.add("21");
		otherEntry.addEntry(testList);
		
		assertFalse("Verification de la non suppression de someKey" , db.get("someKey") == null);
		
		assertTrue(db.put("someKey", entry));
		assertTrue(db.put("someOtherKey", otherEntry));
		assertTrue(db.remove("someKey"));

		assertTrue("Verification de la suppression de someKey" , db.get("someKey") == null);
		
		db.interrupt();
	}
	
	/**
	 * Verifie le bon fonctionnement de la base de donnees lors de l'ajout d'un grand nombre d'entree (> taille du cache).
	 * Insert 1000 element dans la base,
	 * Attends 11000 secondes afin d'etre sur que le gestionnaire du cache se sera execute et a stocke les elements sur le disque.
	 * Verifie que l'acces aux donnes dans le cache est plus rapide que l'acces aux donnees hors cache
	 * Verifie la taille du cache
	 * Verifie que l'acces aux element hors-cache et dans le cache sont coherent
	 */
	@Test(timeout=20000)
	public void cachedDBMassAddTest(){
		CachedDB db = new CachedDB();
		db.start();
		
		List<String>testList = new ArrayList<String>();
		testList.add("42");
		
		for(int i = 0; i < 1000; i++){
			assertTrue(db.put(String.valueOf(i), new IntDBEntry(String.valueOf(i))));
		}
		
		synchronized (this) {
			try {
				this.wait(11000);
			} catch (InterruptedException e) {
				// void
			}
		}
		
		assertTrue("Verification de la taille du cache", db.size() == 500);
		
		long start = System.currentTimeMillis();
		
		assertTrue("recuperation d'un element hors cache" , db.get("10") != null);
		
		long endNoCache = System.currentTimeMillis();
		
		long totalNoCache = endNoCache - start;
		
		start = System.currentTimeMillis();
		
		assertTrue("recuperation d'un element du cache" , db.get("800") != null);
		
		long endCache = System.currentTimeMillis();
		
		long totalCache = endCache - start;
		
		assertTrue("Verification que l'acces aux donnees dans le cache est plus rapide : ", totalNoCache > totalCache);
		
		assertTrue(db.get("42").getKey().equals("42"));
		assertTrue(db.get("42").getType() == DataType.INTEGER);
		
		db.interrupt();
	}
}
