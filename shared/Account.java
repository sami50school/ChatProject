/**
 * Classe Account : Information d'un utilisateur
 * @author Samuel Guillot
 * @date 08/11/2018
 */

package shared;

import java.io.Serializable;

public class Account implements Serializable{

	
	private static final long serialVersionUID = 6529685098267757690L;
	private String pseudo;
	private String psw;
	private boolean isConnected = false;
	
	public Account(String pseudo, String psw) {
		this.pseudo = pseudo;
		this.psw = psw;
	}
	
	public String toString() {
		return "User " + this.pseudo;  
	}
	
	public String getPseudo(){
		return this.pseudo;
	}
	
	public String getPassword(){
		return this.psw;
	}

	public boolean getIsConnected(){
		return this.isConnected;
	}
	
	public void isConnected(){
		this.isConnected = true;
	}
	
	public void isDisconnected(){
		this.isConnected = false;
	}
}
