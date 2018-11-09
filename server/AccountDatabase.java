/**
 * Classe AccountDatabase : communication avec le fichier d'account pour lire et sauvegarder des accounts
 * @author Samuel Guillot
 * @date 08/11/2018
 */

package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import shared.Account;


public class AccountDatabase implements Serializable{

	private static final long serialVersionUID = 6529685098267757690L; 

	private File file;

	/**
	 * Constructeur de la classe AccountDatabase
	 * @param fileName : le nom du fichier a ouvrir
	 */
	public AccountDatabase(String fileName) {
		this.file = new File(fileName);
	}
	

	/**
	 * 
	 * @return une liste d'account
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Account> loadAccounts() throws IOException, ClassNotFoundException {
		
		ArrayList<Account> data = new ArrayList<Account>();
		
		// This checks if the file actually exists
		if(this.file.exists() && !this.file.isDirectory()) { 
            ObjectInputStream FILEtoDB = new ObjectInputStream(new FileInputStream(this.file));
            data= (ArrayList<Account>) FILEtoDB.readObject();
            FILEtoDB.close();
                    
		}else {
			System.out.println("Le fichier de sauvegarde n'existe pas.");
		}
		
		System.out.println(data.size() + " account found.");
		return data;
	}
	
	
	/**
	 * Créé un nouvel utilisateur en l'enregistrant dans le fichier d'accounts
	 * @param data : la liste d'utilisateurs a enregistrer
	 * @throws IOException
	 */
	public void createAccount(ArrayList<Account> data) throws IOException {
		
        ObjectOutputStream DBtoFILE = new ObjectOutputStream(new FileOutputStream(this.file));
        DBtoFILE.writeObject(data);
        DBtoFILE.close();
		
		System.out.println("Creation of the account");
	}
}
