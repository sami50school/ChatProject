/**
 * Classe MessageTopic : Messages qui seront insérés dans les fichiers de topic
 * @author Samuel Guillot
 * @date 08/11/2018
 */
package shared;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MessageTopic implements Serializable{

	
	private static final long serialVersionUID = 6529685098267757690L;
	private Account user; 
	private String  message; 
	
	/**
	 * 
	 * @param user : utilisateur qui écrit le message
	 * @param message : String représentant le message
	 */
	public MessageTopic(Account user, String message){
		this.user    = user;
		this.message = message; 
	}
	
	/**
	 * return String : format de message qui sera inséré dans la base 
	 */
	public String toString(){
		System.out.println("MESSAGE TOPIC : "+this.user.getPseudo()+" : "+this.message);
		return this.user.getPseudo()+" : "+this.message;
	}
	
	
}
