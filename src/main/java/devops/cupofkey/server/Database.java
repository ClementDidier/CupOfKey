package devops.cupofkey.server;

import java.util.List;
import devops.cupofkey.core.DataType;
import devops.cupofkey.core.ResponseType;
import devops.cupofkey.core.Response;

/**
 * Base de donnee locale du serveur.
 * Permet d'effectuer des operations simples sur les donnes presente dans cette Base
 * Les donnees sont encapsuler dans des Objet implementant DBEntry afin d'encaspuler les different types de donnees possible.
 * La base de donnee ne retourne que des String au client, toutefois les donnees de type Integer sont stocker comme telle et peuvent etre modifier comme telle.
 */
public class Database {
	
	/**
	 * Map representant la Base de donnee.
	 * Prend comme cle une String qui sera definit par le client
	 * Stocke des Objet DBEntry encapsulant le type reel des donnees stockee
	 */
	private final CachedDB masterDB;
	
	/**
	 * Initialise la Base de donnee
	 */
	protected Database(){
		this.masterDB = new CachedDB();
		this.masterDB.start();
	}
	
	/**
	 * @param key la cle definie par le client associe aux donnees a traiter
	 * @return une Response contenant la liste des donnees assiee a cette cle ou une erreur si cette cle est introuvable
	 */
	protected Response get(String key){
		if(this.masterDB.containsKey(key)){
			return new Response(this.masterDB.get(key).getEntry());
		}
		return new Response(ResponseType.NO_DATA);
	}
	
	/**
	 * @param key la cle definie par le client associe aux donnees a traiter
	 * @param indice l'indice dans la liste a cette emplacement sur lequel recuperer les donnees
	 * @return une Response contenant la liste des donnees assiee a cette cle ou une erreur si cette cle est introuvable
	 */
	protected Response get(String key, int indice){
		if(this.masterDB.containsKey(key)){
			return new Response(this.masterDB.get(key).getEntry(indice));
		}
		return new Response(ResponseType.NO_DATA);
	}
	
	/**
	 * @param key la cle definie par le client associe aux donnees a traiter
	 * @param data une liste de String representant les donnees a ajouter a cet emplacement
	 * @param type type de des donnees a ajouter
	 * @return une Response en fonction de la reussite ou de l'echec de cet ajout.
	 */ 
	protected Response push(String key, List<String> data, DataType type){
		if(this.masterDB.containsKey(key)){
			if(this.masterDB.get(key).addEntry(data)){
				return new Response(ResponseType.NO_ERROR);
			}
			return new Response(ResponseType.TYPE_ERROR);
		}
		
		DBEntry entry;
		if(type == DataType.INTEGER){
			entry = new IntDBEntry(key);
		}
		else{
			entry = new StringDBEntry(key);
		}
		entry.addEntry(data);
		this.masterDB.put(key, entry);
		return new Response(ResponseType.NO_ERROR);
		
	}
	
	/**
	 * @param key la cle definie par le client associe aux donnees a traiter (supprimer puis ajouter)
	 * @param data une liste de String representant les donnees a ajouter a cet emplacement
	 * @param type type de des donnees a ajouter
	 * @return une Response en fonction de la reussite ou de l'echec de cet suppression et ajout.
	 */ 
	protected Response set(String key, List<String> data, DataType type){
		
		clear(key);
		
		Response pushResponse = push(key, data, type);
		
		if(pushResponse.getResponseType() == ResponseType.NO_ERROR ){
			return pushResponse;
		}
		return new Response(ResponseType.UNHANDLED_ERROR);
	}
	
	/**
	 * Supprime une valeur de la liste presente a un emplacement et le libère si la liste présente est devenue vide.
	 * @param key la cle definie par le client associe aux donnees a traiter
	 * @param indice indice de la liste des donnees stockees sur lequel supprimer la donnee
	 * @return une Reponse avec NO_ERROR si l'on a pu supprimer la donnee de la liste, ou NO_DATA si la cle n'existait pas dans la masterMap
	 */
	protected Response delete(String key, int indice){
		if(this.masterDB.containsKey(key)){
			this.masterDB.get(key).removeEntry(indice);
			
			if(this.masterDB.get(key).isEmpty()){
				this.masterDB.remove(key);
			}
			
			return new Response(ResponseType.NO_ERROR);
		}
		return new Response(ResponseType.NO_DATA);
	}
	
	/**
	 * permet de supprimer toutes les donnees presente a un emplacement de la base de donnee
	 * @param key la cle definie par le client associe aux donnees a traiter
	 * @return retourne NO_DATA lorsque la clé donnée est inexistante et NO_ERROR lorsque la suppression s'est faite avec succés
	 */
	protected Response clear(String key){
		if(this.masterDB.containsKey(key))
		{
			this.masterDB.remove(key);
			return new Response(ResponseType.NO_ERROR);
		}
		return new Response(ResponseType.NO_DATA);
	}
	
	/**
	 * permet de verifier si l'emplacement est libre sur la base de donnees
	 * @param key la cle definie par le client associe aux donnees a traiter
	 * @return un reponse ayant son champs Erreur a TRUE si l'emplacement visee par la cle n'est pas vide, FALSE sinon
	 */
	protected Response isEmpty(String key){
		if(this.masterDB.containsKey(key)){
			return new Response(ResponseType.FALSE);
		}
		return new Response(ResponseType.TRUE);
	}
	
	/**
	 * @param key la cle definie par le client associe aux donnees a traiter
	 * @param value Entier a ajouter aux membre de la liste d'entier presente a cet emplacement
	 * @return une reponse ayant son champs Error a NO_ERROR si l'on a pu incrementer correctement les donnee en fonction de value, TYPE_ERROR si il s'agissait d'une liste de String ou NO_DATA si il n'y avait pas de donnee a cet emplacement
	 */
	protected Response increment(String key, int value){
		if(this.masterDB.containsKey(key)){
			if(this.masterDB.get(key).increment(value)){
				return new Response(ResponseType.NO_ERROR);
			}
			return new Response(ResponseType.TYPE_ERROR);
		}
		return new Response(ResponseType.NO_DATA);
	}
	
	/**
	 * @param key la cle definie par le client associe aux donnees a traiter
	 * @param value Entier a multiplier aux membres de la liste d'entier presente a cet emplacement
	 * @return une reponse ayant son champs Error a NO_ERROR si l'on a pu multiplier correctement les donnee en fonction de value, TYPE_ERROR si il s'agissait d'une liste de String ou NO_DATA si il n'y avait pas de donnee a cet emplacement
	 */
	protected Response multiply(String key, int value){
		if(this.masterDB.containsKey(key)){
			if(this.masterDB.get(key).multiply(value)){
				return new Response(ResponseType.NO_ERROR);
			}
			return new Response(ResponseType.TYPE_ERROR);
		}
		return new Response(ResponseType.NO_DATA);
	}
}
