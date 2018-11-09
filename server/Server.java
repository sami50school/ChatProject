/**
 * Classe Server : Classe serveur qui attend la connexion des client
 * @author Samuel Guillot
 * @date 08/11/2018
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;



public class Server extends Thread{

	private static final int    SERVER_PORT = 123;
	
	/**
	 * fonction qui redirige les clients vers le service "ServiceChat"
	 */
	public void run(){
		System.out.println("Serveur lancé");
		ServerSocket ss;
		try {
			ss = new ServerSocket(SERVER_PORT);
			while(true){
				Socket s = ss.accept();
				System.out.println("Socket accepte");
				new ServiceChat(s).start();
				System.out.println("ServiceChat lance");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]){
		new Server().start();
	}

}
