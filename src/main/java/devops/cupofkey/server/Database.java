package devops.cupofkey.server;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import devops.cupofkey.core.DataType;
import devops.cupofkey.core.ErrorType;
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
	private final Map<String, DBEntry> masterMap;
	
	/**
	 * Initialise la Base de donnee
	 */
	protected Database(){
		this.masterMap = new ConcurrentHashMap<>();
	}
	
	/**
	 * @param key la cle definie par le client associe aux donnees a traiter
	 * @return une Response contenant la liste des donnees assiee a cette cle ou une erreur si cette cle est introuvable
	 */
	protected Response get(String key){
		if(this.masterMap.containsKey(key)){
			return new Response(this.masterMap.get(key).getEntry());
		}
		return new Response(ErrorType.NO_DATA);
	}
	
	/**
	 * @param key la cle definie par le client associe aux donnees a traiter
	 * @param indice l'indice dans la liste a cette emplacement sur lequel recuperer les donnees
	 * @return une Response contenant la liste des donnees assiee a cette cle ou une erreur si cette cle est introuvable
	 */
	protected Response get(String key, int indice){
		if(this.masterMap.containsKey(key)){
			return new Response(this.masterMap.get(key).getEntry(indice));
		}
		return new Response(ErrorType.NO_DATA);
	}
	
	/**
	 * @param key la cle definie par le client associe aux donnees a traiter
	 * @param data une liste de String representant les donnees a ajouter a cet emplacement
	 * @param type type de des donnees a ajouter
	 * @return une Response en fonction de la reussite ou de l'echec de cet ajout.
	 */ 
	protected Response push(String key, List<String> data, DataType type){
		if(this.masterMap.containsKey(key)){
			if(this.masterMap.get(key).addEntry(data)){
				return new Response(ErrorType.NO_ERROR);
			}
			return new Response(ErrorType.TYPE_ERROR);
		}
		
		DBEntry entry;
		if(type == DataType.INTEGER){
			entry = new IntDBEntry();
		}
		else{
			entry = new StringDBEntry();
		}
		entry.addEntry(data);
		this.masterMap.put(key, entry);
		return new Response(ErrorType.NO_ERROR);
		
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
		
		if(pushResponse.getError() == ErrorType.NO_ERROR ){
			return pushResponse;
		}
		return new Response(ErrorType.UNHANDLED_ERROR);
	}
	
	/**
	 * Supprime une valeur de la liste presente a un emplacement et le libère si la liste présente est devenue vide.
	 * @param key la cle definie par le client associe aux donnees a traiter
	 * @param indice indice de la liste des donnees stockees sur lequel supprimer la donnee
	 * @return une Reponse avec NO_ERROR si l'on a pu supprimer la donnee de la liste, ou NO_DATA si la cle n'existait pas dans la masterMap
	 */
	protected Response delete(String key, int indice){
		if(this.masterMap.containsKey(key)){
			this.masterMap.get(key).removeEntry(indice);
			
			if(this.masterMap.get(key).isEmpty()){
				this.masterMap.remove(key);
			}
			
			return new Response(ErrorType.NO_ERROR);
		}
		return new Response(ErrorType.NO_DATA);
	}
	
	/**
	 * permet de supprimer toutes les donnees presente a un emplacement de la base de donnee
	 * @param key la cle definie par le client associe aux donnees a traiter
	 * @return retourne toujours une reponse de type d'erreur NO_ERROR.
	 */
	protected Response clear(String key){
		this.masterMap.remove(key);
		return new Response(ErrorType.NO_ERROR);
	}
	
	/**
	 * permet de verifier si l'emplacement est libre sur la base de donnees
	 * @param key la cle definie par le client associe aux donnees a traiter
	 * @return un reponse ayant son champs Erreur a TRUE si l'emplacement visee par la cle n'est pas vide, FALSE sinon
	 */
	protected Response isEmpty(String key){
		if(this.masterMap.containsKey(key)){
			return new Response(ErrorType.FALSE);
		}
		return new Response(ErrorType.TRUE);
	}
	
	/**
	 * @param key la cle definie par le client associe aux donnees a traiter
	 * @param value Entier a ajouter aux membre de la liste d'entier presente a cet emplacement
	 * @return une reponse ayant son champs Error a NO_ERROR si l'on a pu incrementer correctement les donnee en fonction de value, TYPE_ERROR si il s'agissait d'une liste de String ou NO_DATA si il n'y avait pas de donnee a cet emplacement
	 */
	protected Response increment(String key, int value){
		if(this.masterMap.containsKey(key)){
			if(this.masterMap.get(key).increment(value)){
				return new Response(ErrorType.NO_ERROR);
			}
			return new Response(ErrorType.TYPE_ERROR);
		}
		return new Response(ErrorType.NO_DATA);
	}
	
	/**
	 * @param key la cle definie par le client associe aux donnees a traiter
	 * @param value Entier a multiplier aux membres de la liste d'entier presente a cet emplacement
	 * @return une reponse ayant son champs Error a NO_ERROR si l'on a pu multiplier correctement les donnee en fonction de value, TYPE_ERROR si il s'agissait d'une liste de String ou NO_DATA si il n'y avait pas de donnee a cet emplacement
	 */
	protected Response multiply(String key, int value){
		if(this.masterMap.containsKey(key)){
			if(this.masterMap.get(key).multiply(value)){
				return new Response(ErrorType.NO_ERROR);
			}
			return new Response(ErrorType.TYPE_ERROR);
		}
		return new Response(ErrorType.NO_DATA);
	}
}
