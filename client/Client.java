/**
 * Classe Client : partie utilisateur en interaction avec le serveur
 * @author Samuel Guillot
 * @date 08/11/2018
 */

package client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.InputMismatchException;
import java.util.Scanner;



import server.AccountRequest;
import shared.Request;

import java.io.IOException;


@SuppressWarnings("resource")
public class Client{

	private static final String SERVER_HOST = null; // localhost
	private static final int    SERVER_PORT = 123;
	
	/**
	 * The client socket
	 */
	private Socket client;
	
	/**
	 * The client output stream
	 */
	private ObjectOutputStream outputStream;
	
	/**
	 * The client input stream
	 */
	private ObjectInputStream inputStream;
	
	
	/**
	 * Constructeur d'un client. On y créé une socket de communication avec le serveur
	 * ainsi que la création des input et output stream
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public Client()throws UnknownHostException, IOException {

		/*
		 * initialization of the socket, the input stream, and the output stream
		 */
	
		this.client = new Socket(SERVER_HOST, SERVER_PORT);
		this.outputStream = new ObjectOutputStream(this.client.getOutputStream());
		this.inputStream = new ObjectInputStream(this.client.getInputStream());
	
	}
	
	
	/**
	 * Fonction principale du programme client
	 * @param args
	 * @throws ClassNotFoundException
	 * @throws SocketException
	 */
	public static void main(String[] args) throws ClassNotFoundException, SocketException{
		try{
			Client user = new Client();
			boolean isConnected = true;
			
			while(isConnected){
				//Affichage du menu
				user.readResponse();

				int choix = 0;
				
				Scanner clavier   = new Scanner(System.in);
				
				//On vérifie que l'utilisateur entre bien un nombre entier
				try{
					choix  = clavier.nextInt();
				}
				catch(InputMismatchException ime){
					System.out.println("Votre choix devait correspondre à un element de la liste, "
							+ "vous allez être déconnecté !");
				}
				
				switch(choix){
					case 1 :
						user.outputStream.writeObject(Request.AUTHENTIFICATION);
						user.outputStream.flush();
						
						//message to enter pseudo and password
						user.readResponse();
						
						String pseudo = clavier.next();
						String password = clavier.next();
						
						user.authentification(pseudo, password);
						break;
					case 2 : 
						user.outputStream.writeObject(Request.CREATE_ACCOUNT);
						user.readResponse();
						
						pseudo = clavier.next();
						password = clavier.next();
						
						user.createAccount(pseudo, password);
						break;
					case 3 :
						user.outputStream.writeObject(Request.LIST_TOPICS);
						user.readResponse();
						break;
					case 4 : 
						user.outputStream.writeObject(Request.CREATE_TOPIC);
						user.readResponse();
						
						String topicName = clavier.next();
						user.createTopic(topicName);

						break;
					case 5 : 
						user.outputStream.writeObject(Request.JOIN_TOPICS);
						user.readResponse();
						
						String joinTopicName = clavier.next();
						
						user.joinTopic(joinTopicName);

						break;
					case 6 :
						user.disconnect();
						break;
					case 7 : 
						//on ferme l'application
						isConnected = false;
						user.closeApplication();
						break;
					default : 
						//on ferme l'application
						isConnected = false;
						user.closeApplication();
						break;
				}
			}
			
			user.inputStream.close();
			user.outputStream.close();
			user.client.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param pseudo : pseudo de l'utilisateur pour la connexion
	 * @param psw : pseudo de l'utilisateur pour la connexion
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void authentification(String pseudo, String psw) throws IOException, ClassNotFoundException{
		AccountRequest req = new AccountRequest(pseudo, psw);
		
		this.outputStream.writeObject(req);
		this.outputStream.flush();

		String reponse = (String)this.inputStream.readObject();
		
		if(reponse.equals("SUCCESS"))
			System.out.println("CONNEXION OK");
		else
			System.out.println("CONNEXION KO");
	}
	
	/**
	 * 
	 * @param pseudo : pseudo de l'utilisateur pour la création du compte
	 * @param psw : mot de passe de l'utilisateur pour la création du compte
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void createAccount(String pseudo, String psw) throws IOException, ClassNotFoundException{
		AccountRequest req = new AccountRequest(pseudo, psw);
		
		this.outputStream.writeObject(req);
		this.outputStream.flush();

		String reponse = (String)this.inputStream.readObject();

		
		if(reponse.equals("SUCCESS"))
			System.out.println("Success, your account has been created !");
		else
			System.out.println("Failed, your account hasn't been created !");
	}
	
	/**
	 * 
	 * @param topicName : le nom du topic que l'on veut créer
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void createTopic(String topicName) throws IOException, ClassNotFoundException{
		
		//On entre un nom de topic
		outputStream.writeObject(topicName);
		
		String reponse = (String)this.inputStream.readObject();
		//On vérifie que le nom de topic n'existe pas déjà
		if(reponse.equals("SUCCESS")){
			//On affiche un message du serveur qui indique qu'on peut entrer un premier message
			this.readResponse();
			
			Scanner sc = new Scanner(System.in);
			
			//On écrit un premier message pour décrire le topic
			String firstMessageDescribeTopic = sc.nextLine();
			this.outputStream.writeObject(firstMessageDescribeTopic);

			String responseCreateTopic = (String)this.inputStream.readObject();
			if(responseCreateTopic.equals("SUCCESS"))
				System.out.println("Le Topic "+topicName+" vient d'être créé !");
			else
				System.out.println("Une erreur est survenue dans la création du Topic !");
		}
		else{
			System.out.println("Le Topic que vous essayez de créer existe déjà !");
		}
	}
	
	
	/**
	 * 
	 * @param joinTopicName : le nom du topic sur lequel on veut participer
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void joinTopic(String joinTopicName) throws IOException, ClassNotFoundException{
		
		this.outputStream.writeObject(joinTopicName);
		
		String responseTopicExist = (String)this.inputStream.readObject();
		
		if(responseTopicExist.equals("SUCCESS")){
		
			System.out.println("Vous êtes connectés au Topic "+joinTopicName);
			
			//Affichage des différents messages du topic
			this.readResponse();
			
			//Affichage du message indiquant la possibilité d'ecriture sur le topic
			this.readResponse();
			
			Scanner scanner = new Scanner(System.in);
			String retourServeur = "", messageTopic = "";
			
			//On ecrit un premier message
	        messageTopic = scanner.nextLine();
	        this.outputStream.writeObject(messageTopic);
			
			while(!(retourServeur = (String)this.inputStream.readObject()).equals("END_WRITE")){
				this.readResponse(retourServeur);
		        
				
		        messageTopic = scanner.nextLine();
		        this.outputStream.writeObject(messageTopic);
		        
				//Affichage des différents messages du topic
		        this.readResponse();
			}
			
			System.out.println("je sors de la boucle while");
		}
		else{
			System.out.println("le topic que vous essayer de joindre n'existe pas !");
		}
	}
	
	/**
	 * Permet au client de se déconnecter
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void disconnect() throws IOException, ClassNotFoundException{
		outputStream.writeObject(Request.LOG_OUT);
                
		String reponse = (String)this.inputStream.readObject();
		
		if(reponse.equals("SUCCESS"))
			System.out.println("Success, your are disconnected !");
		else
			System.out.println("Failed, your aren't disconnected !");
	}
	
	/**
	 * Permet au client de quitter l'application en fermant la socket
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void closeApplication() throws IOException, ClassNotFoundException{
		outputStream.writeObject(Request.CLOSE);
                
		String reponse = (String)this.inputStream.readObject();
		
		if(reponse.equals("SUCCESS")){
			System.out.println("Vous venez de quitez l'application.");
			outputStream.close();
		    inputStream.close();
		    client.close();
		}
		else
			System.out.println("Erreur, vous n'avez pas pu quitter l'application !");
	}
	
	
	/**
	 * Read the response from the server
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private void readResponse() throws ClassNotFoundException, IOException {
		System.out.println("Server's answer : ");
		System.out.println(inputStream.readObject());
	}
	
	
	/**
	 * Redefinition de la méthode readResponse en passant la reponse directement en paramètre
	 */
	private void readResponse(String response){
        System.out.println("Server's answer : ");
        System.out.println(response);
	}
	
	
}
