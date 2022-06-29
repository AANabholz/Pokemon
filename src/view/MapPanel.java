package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;

import api_control.PokeAPI;
import api_model.Pokemon;
import control.CommonData;
import control.Player;

public class MapPanel  extends JPanel implements ActionListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private final int B_WIDTH = 1000;
    private final int B_HEIGHT = 1000;
    private final int DOT_SIZE = 50;
    private final int RAND_POS = 1000/(DOT_SIZE-1);
    private final int DELAY = 140;
    private final int PARTY_SIZE = 999; //PARTY_SIZE
    private final int MAX_WILD = 10;

    private int PlayerX;
    private int PlayerY;
    private int PartyX[] = new int[PARTY_SIZE];
    private int PartyY[] = new int[PARTY_SIZE];
    private int TargetX[] = new int[MAX_WILD];
    private int TargetY[] = new int[MAX_WILD];

    private int pokeIdScope = 900; // max is 900
    private int numInParty = 1;
    private int numInWild = 3;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;

    private Timer timer;
    private Player player;
    private Pokemon[] party = new Pokemon[PARTY_SIZE];
    private Pokemon[] wildPokemon = new Pokemon[MAX_WILD];
    
    public PokeAPI api;

    public MapPanel(PokeAPI api) {
    	this.api = api;
        initBoard();
    }
    
    public void initBoard() {
    	requestFocus();
        setBackground(Color.black);
        setFocusable(true);


        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        //setPreferredSize(new Dimension(getWidth(), getHeight()));

        player = new Player("*NAME*");
        player.setImage(loadImage("./sprites/items/poke-ball.png"));
        
        party[0] = new Pokemon(25);
        party[0].setImage(loadImage("./sprites/pokemon/25.png"));
        /*
        loadImageParty(1, Integer.toString(1));
        loadImageParty(2, Integer.toString(4));
        loadImageParty(3, Integer.toString(7));
        loadImageParty(4, Integer.toString(10));
        loadImageParty(5, Integer.toString(16));*/
        
        for (int i = 0; i < numInWild; i++) {
        	wildPokemon[i] = chooseRandomPokemon();
        	
        }
        
        initGame();
    }
    

    private Image loadImage(String path) {
        ImageIcon imgicon = new ImageIcon(path);
        return imgicon.getImage();
    	//player = player.getScaledInstance(2, 2, 2);
        // iiPlayer.getIconWidth() make the image center on the coordinate for all images
    }
    
    private Pokemon chooseRandomPokemon() {
    	int r = (int) (Math.random() * pokeIdScope);
    	Pokemon poke = new Pokemon(r);
    	poke.setImage(loadImage("./sprites/pokemon/"+ r + ".png"));
    	return poke;
    }

    private void initGame() {

    	PlayerX = 100;
    	PlayerY = 100;
    	
        for (int z = 0; z < numInParty; z++) {
        	PartyX[z] = 50 - z * DOT_SIZE;
        	PartyY[z] = 50;
        }

        for(int i = 0; i < numInWild; i++) {
        	randomlyPositionWildPokemon(i);
        }

        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }
    
    private void doDrawing(Graphics g) {
    	
        if (inGame) {
            
        	// Draw Player
            g.drawImage(player.img, PlayerX - 10, PlayerY - 10, this);
            
            // Draw Party
            if (numInParty > 0)
	            for (int z = 0; z < numInParty; z++) {
	            	//ImageIcon imgicon = new ImageIcon(party[z].img);
	            	g.drawImage(party[z].img, PartyX[z] - DOT_SIZE, PartyY[z] - DOT_SIZE, this);
	            }
            
            // Draw Wild
            if (numInWild > 0);
	            for (int z = 0; z < numInWild; z++) {
	            	//ImageIcon imgicon = new ImageIcon(wildPokemon[z].img);
	            	g.drawImage(wildPokemon[z].img, TargetX[z] - DOT_SIZE, TargetY[z] - DOT_SIZE, this);
	            }

            Toolkit.getDefaultToolkit().sync();

        } else {

            //gameOver(g);
        }        
    }

    private void gameOver(Graphics g) {
        
        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
    }

    private void checkBattle() {

    	for (int i = 0; i < numInWild; i++) {
            if ((Math.abs(PlayerX-TargetX[i]) < DOT_SIZE*1.5) && (Math.abs(PlayerY - TargetY[i]) < DOT_SIZE*1.5)) {

                party[numInParty] = wildPokemon[i];
                PartyX[numInParty] = PartyX[numInParty-1];
                PartyY[numInParty] = PartyY[numInParty-1];
                numInParty++;
            	wildPokemon[i] = chooseRandomPokemon();
            	randomlyPositionWildPokemon(i);
            }
    	}
    }

    public void move() {

        for (int z = numInParty - 1; z >= 0; z--) {
        	if (z == 0) {
            	PartyX[z] = PlayerX;
            	PartyY[z] = PlayerY;
        	}
        	if (z >= 1) {
            	PartyX[z] = PartyX[(z - 1)];
            	PartyY[z] = PartyY[(z - 1)];
        	}
        }

        if (leftDirection) {
        	PlayerX -= DOT_SIZE;
        }

        if (rightDirection) {
        	PlayerX += DOT_SIZE;
        }

        if (upDirection) {
        	PlayerY -= DOT_SIZE;
        }

        if (downDirection) {
        	PlayerY += DOT_SIZE;
        }
    }
    
    private void checkCollision() {

        /*for (int z = dots; z > 0; z--) {

            if ((z > 4) && (PlayerX[0] == PlayerX[z]) && (PlayerY[0] == PlayerY[z])) {
                //inGame = false;
            }
        }*/

        if (PlayerY >= B_HEIGHT) {
        	upDirection = true;
        	downDirection = false;
        }

        if (PlayerY < 0) {
        	downDirection = true;
        	upDirection = false;
        }

        if (PlayerX >= B_WIDTH) {
        	rightDirection = true;
            leftDirection = false;
        }

        if (PlayerX < 0) {
        	leftDirection = true;
            rightDirection = false;
        }
        
        if (!inGame) {
            timer.stop();
        }
    }

    private void randomlyPositionWildPokemon(int i) {
    	int r = 0;
    	
        r = (int) (Math.random() * RAND_POS);
        TargetX[i] = ((r * DOT_SIZE) + DOT_SIZE);
        r = (int) (Math.random() * RAND_POS);
        TargetY[i] = ((r * DOT_SIZE) + DOT_SIZE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {
        	checkBattle();
            checkCollision();
        }
        repaint();
    }
    

    public void moveUp() {
    	upDirection = true;
    	downDirection = false;
        rightDirection = false;
        leftDirection = false;
        move();
    }
    public void moveDown() {
    	downDirection = true;
    	upDirection = false;
        rightDirection = false;
        leftDirection = false;
        move();
    }
    public void moveLeft() {
    	leftDirection = true;
        rightDirection = false;
        upDirection = false;
        downDirection = false;
        move();
    }
    public void moveRight() {
    	rightDirection = true;
        leftDirection = false;
        upDirection = false;
        downDirection = false;
        move();
    }
            
}
