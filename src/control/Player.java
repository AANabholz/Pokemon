package control;

import java.awt.Image;

public class Player {
	
	String name;

	public Image img;
	
	public Player(String n) {
		name = n;
	}

	public void setImage(Image i) {
		img = i;
	}
}
