/**
 * Classe ServiceChat : Traite les requêtes du client
 * @author Samuel Guillot
 * @date 08/11/2018
 */

package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import shared.Account;
import shared.MessageTopic;
import shared.Request;
import shared.Topic;

public class ServiceChat extends Thread{

	
	private Socket client; 
	Account account = null;
	
	/**
	 * Constructeur de ServiceChat
	 * @param s : la socket que vient de renvoyer le serveur 
	 */
	public ServiceChat(Socket s) {
		// TODO Auto-generated constructor stub
		this.client = s;
	}
	
	
	/**
	 * Fonction principale qui va traiter les requêtes du client
	 */
	public void run(){
		try {
			//On déclare nos input et output stream
			ObjectInputStream ois = new ObjectInputStream(this.client.getInputStream());
			ObjectOutputStream oos = new ObjectOutputStream(this.client.getOutputStream());

			boolean running = true;
			
			//Tant que le client n'a pas fermé l'application
			while(running){
				//On vérifie si l'utilisateur est connecté pour lui envoyer un menu
				if(this.account == null)
					oos.writeObject(displayMenuNonConnected());
				else
					oos.writeObject(displayMenuConnected());
				
				//Requête reçu par l'utilisateur
				Request request = (Request) ois.readObject();
				
				switch(request){
					case AUTHENTIFICATION :

						oos.writeObject("Enter your Pseudo and your password :");
						
						AccountRequest authRequest = (AccountRequest) ois.readObject();
						
						this.account = authRequest.authorizeConnection();
						//On vérifie la connexion
						if(this.account != null){
							//Successful response
							Response resp = new SuccessfullResponse();
							oos.writeObject(resp.getResponse());
						}
						else{
							//Denied response
							Response resp = new DeniedResponse();
							oos.writeObject(resp.getResponse());
						}
						
						break;
					case CREATE_ACCOUNT :
						oos.writeObject("Enter a pseudo and a password :");
						
						AccountRequest createAccountRequest = (AccountRequest) ois.readObject();
						
						//On créé l'account
						createAccountRequest.createAccount();
						
						Response resp = new SuccessfullResponse();
						oos.writeObject(resp.getResponse());
						
						break;
					case LIST_TOPICS :
						TopicsRequest topicReq = new TopicsRequest();
						
						//On récupère une liste de topics
						String[] listTopic = topicReq.listTopics();
						
						oos.writeObject(topicReq.convertToStringArrayTopics(listTopic));
						break;
					case CREATE_TOPIC : 
						oos.writeObject("Enter a new Topic name :");
						String topicName = (String) ois.readObject();
						
						TopicsRequest topicRequest = new TopicsRequest();
						
						topicRequest.initializeListTopics();
						Response topicAlreadyExist; 
						//On vérifie si le topic existe déjà
						if(topicRequest.verifyIfNameTopicDontExist(topicName)){
							
							topicAlreadyExist = new SuccessfullResponse();
							oos.writeObject(topicAlreadyExist.getResponse());
							
							Topic topic = new Topic(topicName);
							
							oos.writeObject("Enter a first message to describe the topic :");
							
							String firstMessageDescribeTopic = (String) ois.readObject();
							//On créer une classe messageTopic pour avoir un format dans lequel on ecrit dans les topics
							MessageTopic messageTopic = new MessageTopic(this.account, firstMessageDescribeTopic);
							
							//On créé le topic avec le premier message qu'il contiendra
							topicRequest.createTopic(topic, messageTopic);
							
							Response successCreateTopic = new SuccessfullResponse();
							oos.writeObject(successCreateTopic.getResponse());
						}
						else{
							//Si le topic existe déjà on envoie une denied response
							topicAlreadyExist = new DeniedResponse();
							oos.writeObject(topicAlreadyExist.getResponse());
						}
						break;
					case JOIN_TOPICS : 
						oos.writeObject("Enter an existing Topic name :");
						String joinTopicName = (String) ois.readObject();
						
						TopicsRequest joinTopicRequest = new TopicsRequest();
						
						joinTopicRequest.initializeListTopics();
						
						Response topicNameExist; 
						
						//The topic exist
						if(!(joinTopicRequest.verifyIfNameTopicDontExist(joinTopicName))){
							
							//On indique que l'utilisateur est bien connecté au topic
							topicNameExist = new SuccessfullResponse();
							oos.writeObject(topicNameExist.getResponse());
							
							Topic topic = joinTopicRequest.joinTopic(joinTopicName);
							//utilisateur connecté au topic
							topic.followAccount(account);
						
							
							//We read the topic contain 
							String messages = joinTopicRequest.convertToStringArrayMessagesTopics(joinTopicRequest.readTopic(joinTopicName));
						
							oos.writeObject(messages);
							
							oos.writeObject("You can write on the Topic. Enter exit to quit the topic.");
							
							String messageTopic = "";
							//tant que le message envoyé par l'utilisateur n'est pas "exit" on écrit dans le topic
							while(!(messageTopic = (String) ois.readObject()).toUpperCase().equals("EXIT")){

								MessageTopic messageTop = new MessageTopic(this.account, messageTopic);
								joinTopicRequest.writeOnTopic(joinTopicName, messageTop);
								
								//We read the topic contain 
								messages = joinTopicRequest.convertToStringArrayMessagesTopics(joinTopicRequest.readTopic(joinTopicName));
								//System.out.println(messages);
								oos.writeObject(messages);
								
								//We write on the topic 
								oos.writeObject("You can write on the Topic. Enter exit to quit the topic.");
							}
							//On envoie le message "END_WRITE" pour indiquer que l'ecriture dans le fichier est terminée
							oos.writeObject("END_WRITE");
						}
						else{
							//Le nom du topic n'existe pas
							topicNameExist = new DeniedResponse();
							oos.writeObject(topicNameExist.getResponse());
						}
						break;
					case LOG_OUT : 
						//Deconnexion de l'utilisateur
					    this.account = null;
					    
					    Response successResp = new SuccessfullResponse();
						oos.writeObject(successResp.getResponse());
					    
						break;
					case CLOSE : 
						//On ferme l'application pour ce client
					    Response successClose = new SuccessfullResponse();
						oos.writeObject(successClose.getResponse());
						running = false;
						break;
					default : 
						System.out.println("DEFAULT");
						oos.writeObject("DEFAULT, DEVELOPPER LES NOUVELLES FONCTIONNALITES");
				}
			}

			//on ferme les input output stream et la socket. 
			ois.close();
			oos.close();
			client.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Menu pour les utilisateurs non connectés
	 * @return the string of the menu
	 */
	public String displayMenuNonConnected(){
		
		String menu ="1 - Authentification\n2 - Create Account\n3 - List Topics\n7 - Close Application\nVotre choix ?";
				
		return menu;
	}
	
	/**
	 * Menu pour les utilisateurs connectés
	 * @return the string of the menu
	 */
	public String displayMenuConnected(){
		
		String menu ="3 - List Topics\n4 - Create Topic \n5 - Join Topic\n6 - Disconnect\n7 - Close Application\nVotre choix ?";
				
		return menu;
	}

}
