/**
 * Classe AccountRequest : Gère les requêtes de la connexion
 * @author Samuel Guillot
 * @date 08/11/2018
 */
package server;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import shared.Account;

public class AccountRequest implements Serializable{

	private static final long serialVersionUID = 6529685098267757690L;
	
	private String username, psw;
	private ArrayList<Account> accounts;

	private AccountDatabase accountDB;
	private final String DB_FILE_NAME = "Accounts\\accountDB.txt";
	
	/**
	 * 
	 * @param username : le nom de l'utilisateur
	 * @param psw : le mot de passe de l'utilisateur
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public AccountRequest(String username, String psw) throws ClassNotFoundException, IOException {
		accountDB = new AccountDatabase(DB_FILE_NAME);
		
		this.psw = psw;
		this.username = username;
	}
	
	public String getPsw() {
		return psw;
	}
	
	public String getUsername() {
		return username;
	}
	
	/**
	 * Teste la connexion d'un utilisateur
	 * @return un Account si la connexion est acceptée, null sinon
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public Account authorizeConnection() throws ClassNotFoundException, IOException{
		accounts = accountDB.loadAccounts();

		
        if (this.accounts.size() == 0) {

			System.out.println("FICHIER ACCOUNT VIDE");
            
		}else{
            System.out.println(accounts);
            for(int i = 0; i < accounts.size(); ++i){
            	if(accounts.get(i).getPseudo().equals(this.username) 
            			&& accounts.get(i).getPassword().equals(this.psw)){
            		accounts.get(i).isConnected();
            		return accounts.get(i);
            	}
            }
		}
		
		return null;
	}
	
	/**
	 * Lance la requête de création d'un account avec les attributs pseudo et psw
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void createAccount() throws IOException, ClassNotFoundException{

		accounts = accountDB.loadAccounts();
		accounts.add(new Account(this.username, this.psw));
		
		//On balance une liste d'account en ayant récupéré auparavant la liste de tous les accounts
		accountDB.createAccount(accounts);
	}
}
