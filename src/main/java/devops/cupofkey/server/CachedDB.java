package devops.cupofkey.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import devops.cupofkey.core.DataType;

/**
 * permet une abstraction du cache de la base de donnee.
 * Utilise une Map afin d'acceder aux element en cache et le systeme de fichier pour le reste
 * Sauvegarde regulierement le cache
 */
public class CachedDB extends Thread {
	
	/**
	 * Represente un prefixe d'entier
	 */
	private static final char INTEGER = 'i';
	
	/**
	 * Represente un prefixe de chaine
	 */
	private static final char STRING = 's';

	/**
	 * Temps entre deux actualisation du cache
	 */
	private static final int REFRESH_RATE = 5000;
	
	/**
	 * Nombre maximum d'element en cache
	 */
	private static final int MAX_CACHED_ENTRIES = 500;

	/**
	 * Map representant le cache de la Base de donnee.
	 * Prend comme cle une String qui sera definit par le client
	 * Stocke des Objet DBEntry encapsulant le type reel des donnees stockee
	 */
	private final Map<String, DBEntry> masterMap;
	
	/**
	 * Represente une queue avec les cle de la base de donnee utilise dans l'ordre
	 * A chaque fois que l'on utilise une cle on l'a rajoute au debut de la queue
	 */
	private final Queue<String> cacheQueue;
	
	/**
	 * Initialise une nouvelle instance de la base de donnee avec cache
	 */
	public CachedDB(){
		this.masterMap	= new ConcurrentHashMap<String, DBEntry>();
		this.cacheQueue	= new ConcurrentLinkedQueue<String>();
	}
	
	@Override
	public void run(){
		while(!isInterrupted()){
			while(this.cacheQueue.size() > MAX_CACHED_ENTRIES){
				writeOnDisk(this.masterMap.get(this.cacheQueue.poll()));
			}
			backupMasterList();
			syncWait();
		}
	}
	
	/**
	 * Sauvegarde la base de donnee sur le systeme de fichier
	 */
	private void backupMasterList() {
		for (Map.Entry<String, DBEntry> entry : this.masterMap.entrySet())
		{
		    writeOnDisk(entry.getValue());
		}
	}

	/**
	 * Attends pendant une duree predefinie
	 */
	synchronized public void syncWait(){
		try {
			this.wait(REFRESH_RATE);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
	
	/**
	 * @param key une String representant une cle de la base de donnee
	 * @return vrai si la cle existe dans la base de donne (soit en cache, soit sur le systeme de fichier)
	 */
	public boolean containsKey(String key){
		return this.masterMap.containsKey(key) || containsKeyOnDisk(key);
	}
	
	/**
	 * @param key une cle associee a une entree de la base de donnee
	 * @return vrai si une entree de la base de donne associee a cette cle existe sur le disque, faux sinon
	 */
	private boolean containsKeyOnDisk(String key) {
		File fileInt = new File(INTEGER + key);
		File fileStr = new File(STRING + key);
		return fileInt.exists() || fileStr.exists();
	}

	/**
	 * Retourne une entree de la base de donnee et la met az jour dans le cache
	 * @param key une String representant une cle de la base de donnee
	 * @return l'entree associe a cette cle dans la base de donnees (en cache ou non). Retourne null si non trouve
	 */
	public DBEntry get(String key){
		DBEntry entry = this.masterMap.get(key);
		if(entry == null){
			entry = readFromDisk(key);
			if(entry != null){
				updateCache(key);
				this.masterMap.put(key, entry);
			}
		}
		else{
			updateCache(key);
		}

		return entry;
	}
	
	/**
	 * met a jour la file du cache avec cette cle
	 * @param key une entree de la base de donnee a ajouter dans le cache
	 */
	private void updateCache(String key) {
		this.cacheQueue.remove(key);
		this.cacheQueue.add(key);
	}
	

	/**
	 * Ajoute une nouvelle entree dans la base de donne (l'ajoute dans le cache et dans le systeme de fichier)
	 * @param key une String representant une cle de la base de donnee
	 * @param entry une nouvelle entree dans la base de donnee
	 * @return vrai
	 */
	public boolean put(String key, DBEntry entry){
		writeOnDisk(entry);
		this.masterMap.put(key, entry);
		updateCache(key);
		return true;
	}
	
	
	/**
	 * @param key une String representant une cle de la base de donnee
	 * @return vrai
	 */
	public boolean remove(String key){
		deleteFromDisk(key);
		this.masterMap.remove(key);
		this.cacheQueue.remove(key);
		return true;
	}
	
	/**
	 * @param entry une nouvelle entree a ecrire sur le systeme de fichier
	 */
	synchronized private void writeOnDisk(DBEntry entry){
		
		char typeCode = INTEGER;
		if(entry.getType() == DataType.STRING){
			typeCode = STRING;
		}
		
		try {
			File file = new File(typeCode + entry.getKey());
			ObjectOutputStream oos =  new ObjectOutputStream(new FileOutputStream(file)) ;
			oos.writeObject(entry);
			oos.close();
		} 
		catch (Exception e) {
			// erreur d'ecriture
		}
		
	}
	
	/**
	 * @param key une cle associe a une entree de la base de donnee
	 * @return une entree DBEntry si le fichier associe a cette entree a ete trouve, null sinon
	 */
	synchronized private DBEntry readFromDisk(String key){
		
		try {
			File file;

			file = new File(INTEGER + key);
			if(file.exists()){
				ObjectInputStream ois =  new ObjectInputStream(new FileInputStream(file)) ;
				IntDBEntry entry = (IntDBEntry)ois.readObject() ;
				ois.close();
				return entry;
			}
			
			file = new File(STRING + key);
			if(file.exists()){
				ObjectInputStream ois =  new ObjectInputStream(new FileInputStream(file)) ;
				StringDBEntry entry = (StringDBEntry)ois.readObject() ;
				ois.close();
				return entry;
			}
		} 
		catch (Exception e) {
			// fichier non trouve
		}
		return null;
	}
	
	/**
	 * Supprime l'image disk associe a cette entree
	 * @param key cle d'une entree de la base de donnee
	 */
	synchronized private void deleteFromDisk(String key){
		File file;
		
		file = new File(INTEGER + key);
		file.delete();
		file = new File(STRING + key);
		file.delete();
	}
}