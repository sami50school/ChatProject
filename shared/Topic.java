/**
 * Classe Topic : Représente un Topic
 * @author Samuel Guillot
 * @date 08/11/2018
 */
package shared;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Topic implements Serializable{

	
	private static final long serialVersionUID = 6529685098267757690L;
	private String name;
	private int identifiant_topic;
	
	//Arraylist d'utilisateurs qui suivent le topic
	private ArrayList<Account>  followAccounts;
	
	public Topic(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String toString(){
		return "Nom du Topic : " + this.name + " - Id du Topic : " + this.identifiant_topic;  
	}
	
	/**
	 * Permet d'ajouter cet utilisateur a la liste des utilisateurs qui suivent ce Topic
	 * @param acc : utilisateur qui veut suivre le topic
	 */
	public void followAccount(Account acc){
		if(this.followAccounts == null){
			this.followAccounts = new ArrayList<Account>();
		}
		
		this.followAccounts.add(acc);
	}
}