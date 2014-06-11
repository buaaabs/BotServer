package hha.clips;

import CLIPSJNI.*;

public class Test {

	static Environment clips;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		clips = new Environment(); 
		clips.load("autodemo.clp");

		clips.reset();
		clips.run();
	}

}
