/**
 * Classe Response : Réponse positive à une action effectué par l'utilisateur
 * @author Samuel Guillot
 * @date 08/11/2018
 */
package server;


public class SuccessfullResponse extends Response {
	
	public void setResponseSuccess(){
		super.setResponse("SUCCESS");
	}
	
	@Override
	public String getResponse(){
		return "SUCCESS";
	}
}
