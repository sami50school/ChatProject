package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import shared.MessageTopic;
import shared.Topic;

public class TopicsRequest {
	
	private final String TOPICS_FOLDER_NAME = "Topics";
	private static ArrayList<Topic> topics;
	private File topicFile = null;
	
	private ArrayList<MessageTopic> messagesTopic;
	
	public void initializeListTopics(){
		File repertoire = new File(TOPICS_FOLDER_NAME);
        String liste[] = repertoire.list();      

        System.out.println(liste.length);
        
		if(topics == null){
			topics = new ArrayList<Topic>();
		}
        
        for(int i = 0; i < liste.length; ++i){
        	System.out.println(liste[i]);
    		int indexExtension = liste[i].indexOf(".txt");
    		liste[i] = liste[i].substring(0, indexExtension);
    		

    		
    		int count = 0;
    		for(int j = 0; j < topics.size(); ++j){
    			if(!(topics.get(j).getName().equals(liste[i]))){
    				++count;
    			}
    		}
    		
    		if(count <= topics.size()){
    			topics.add(new Topic(liste[i]));
    		}
        }
	}
	
    public String[] listTopics(){
        File repertoire = new File(TOPICS_FOLDER_NAME);
        String liste[] = repertoire.list();      

        if (liste != null){     
        	return liste;
        } else {
            System.err.println("Nom de repertoire invalide");
            return null;
        }
    }
    
    public String convertToStringArrayTopics(String[] arrayTopics){
    	
    	String listTopics = "";
    	for (int i = 0; i < arrayTopics.length; i++) {
    		
    		/* On enlève l'extension */
    		int indexExtension = arrayTopics[i].indexOf(".txt");
    		arrayTopics[i] = arrayTopics[i].substring(0, indexExtension);
    		
    		if(i < (arrayTopics.length - 1)){
    			listTopics += arrayTopics[i]+" - ";
    		}
    		else{
    			listTopics += arrayTopics[i];
    		}
        }
    	
    	return listTopics;
    }
    
    
    /**
     * Verify if the the topic already exist. If the topics list is null 
     * we create a new one
     * @param topicName The name of the topic who want to be create
     * @return true if The topic doesn't exist else false
     * 
     */
    public boolean verifyIfNameTopicDontExist(String topicName){
    	if(topics != null){
	    	for(int i = 0; i < topics.size(); ++i){
	    		if(topics.get(i).getName().equals(topicName))
	    			return false;
	    	}
    	}
    	else{
    		topics = new ArrayList<Topic>();
    	}
    	return true;
    }
    
    public void createTopic(Topic topic, MessageTopic messageTopic) throws FileNotFoundException, IOException{

    	messagesTopic = new ArrayList<MessageTopic>();
    	messagesTopic.add(messageTopic);
    	
    	this.topicFile = new File(TOPICS_FOLDER_NAME+"\\"+topic.getName()+".txt");
    	System.out.println("Name of the Topic "+this.topicFile.getPath());
        ObjectOutputStream DBtoFILE = new ObjectOutputStream(new FileOutputStream(this.topicFile));
        //DBtoFILE.writeObject(messageTopic);
        DBtoFILE.writeObject(messagesTopic);
        DBtoFILE.close();
    	
    	topics.add(topic);
    }
    
    public void writeOnTopic(String nomTopic, MessageTopic messageTopic) throws FileNotFoundException, IOException, ClassNotFoundException{
    	
    	if(this.topicFile == null){
    		this.topicFile = new File(TOPICS_FOLDER_NAME+"\\"+nomTopic+".txt");
    	}
    	
    	messagesTopic = this.readTopic(nomTopic);
    	messagesTopic.add(messageTopic);
    	
		ObjectOutputStream DBtoFILE = new ObjectOutputStream(new FileOutputStream(this.topicFile));
        //DBtoFILE.writeObject(messageTopic);
		DBtoFILE.writeObject(messagesTopic);
        DBtoFILE.close();
    }
    
    public Topic joinTopic(String topicName){
    	for (int i = 0; i < topics.size(); ++i){
    		if(topics.get(i).getName().equals(topicName)){
    			return topics.get(i);
    		}
    	}
    	return null;
    }
    
    @SuppressWarnings("unchecked")
	public ArrayList<MessageTopic> readTopic(String nomTopic) throws FileNotFoundException, IOException, ClassNotFoundException{
		ArrayList<MessageTopic> messages = new ArrayList<MessageTopic>();
		//Account data = null;
		
		if(this.topicFile == null){
			this.topicFile = new File(TOPICS_FOLDER_NAME+"\\"+nomTopic+".txt");
		}
		
		// This checks if the file actually exists
		if(this.topicFile.exists() && !this.topicFile.isDirectory()) { 
            //ObjectInputStream FILEtoDB = new ObjectInputStream(new FileInputStream(this.file.getName()));
			ObjectInputStream FILEtoDB = new ObjectInputStream(new FileInputStream(this.topicFile));
			messages = (ArrayList<MessageTopic>) FILEtoDB.readObject();
			//messages.add((MessageTopic) FILEtoDB.readObject());
            FILEtoDB.close();
                    
		}else {
			System.out.println("Le fichier de topic n'existe pas.");
		}
		
		System.out.println(messages.size() + " messages found.");
		return messages;
    }
    
    
    public String convertToStringArrayMessagesTopics(ArrayList<MessageTopic> arrayMessages){
    	
    	String messagesTopic = "";
    	for (int i = 0; i < arrayMessages.size(); i++) {
    		messagesTopic += arrayMessages.get(i).toString()+"\n";
        }
    	
    	return messagesTopic;
    }
}
