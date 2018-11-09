/**
 * Classe DeniedResponse : Réponse négative à une action effectué par l'utilisateur
 * @author Samuel Guillot
 * @date 08/11/2018
 */

package server;

public class DeniedResponse extends Response {

	public void setResponseDenied(){
		super.setResponse("FAILED");
	}
	
	@Override
	public String getResponse(){
		return "FAILED";
	}
}
