package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import com.jtattoo.plaf.hifi.HiFiLookAndFeel;

import api_control.PokeAPI;
import control.CommonData;
import control.WrapLayout;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final int scrollBarVerticalUnitIncrement = 16;
	private JPanel contentPane;
	
	public PokeAPI api;

	public MainFrame(PokeAPI api) {
		this.api = api;
		try {
			Properties props = new Properties();
			props.put("logoString", "Pokedex");
			HiFiLookAndFeel.setCurrentTheme(props);
			UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		setTitle("PLANET POKÉMON");
		setIconImage(new ImageIcon("img/program_icon.png").getImage());
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		setMinimumSize(new Dimension(550, 300));
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 754, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		// World Map *************************************************
		JScrollPane mapSP = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		tabbedPane.addTab("WORLD MAP", new ImageIcon("img/program_icon.png"), mapSP, "World Map");
		
		MapPanel mapPanel = new MapPanel(api);
		mapPanel.setLayout(new BorderLayout());
		mapSP.setViewportView(mapPanel);
		EventQueue.invokeLater(() -> {
            pack();
            mapSP.requestFocusInWindow();
        });
		
		setupKeyBindings(mapSP, mapPanel);;
        
		
		// Pokedex
		JScrollPane pokemonsSP = new JScrollPane();
		pokemonsSP.getVerticalScrollBar().setUnitIncrement(scrollBarVerticalUnitIncrement);
		tabbedPane.addTab("POKÉDEX", new ImageIcon("img/pokemon_panel_icon.png"), pokemonsSP, "List of all pokémon");
		
		JPanel pokemonsPanel = new JPanel();
		pokemonsPanel.setLayout(new WrapLayout(FlowLayout.LEADING));
		pokemonsSP.setViewportView(pokemonsPanel);
		
		for (int i = 0; i < 1118; i++) {
			pokemonsPanel.add(new PokemonPanel(CommonData.pokemonCatalogue.get(i).getName(), i+1));
		}
		
		
		
		// Berries
		JScrollPane berriesSP = new JScrollPane();
		berriesSP.getVerticalScrollBar().setUnitIncrement(scrollBarVerticalUnitIncrement);
		tabbedPane.addTab("BERRIES", new ImageIcon("img/berry_panel_icon.png"), berriesSP, "List of all berrys");
		
		JPanel berriesPanel = new JPanel();
		berriesPanel.setLayout(new WrapLayout(FlowLayout.LEADING));
		berriesSP.setViewportView(berriesPanel);
		
		for (int i = 0; i < 64; i++) {
			berriesPanel.add(new BerryPanel(CommonData.berryCatalogue.get(i).getName()));
		}
		
		// Items
		JScrollPane itemsSP = new JScrollPane();
		itemsSP.getVerticalScrollBar().setUnitIncrement(scrollBarVerticalUnitIncrement);
		tabbedPane.addTab("ITEMS", new ImageIcon("img/items_panel_icon.png"), itemsSP, "List of all items");
		
		JPanel itemsPanel = new JPanel();
		itemsPanel.setLayout(new WrapLayout(FlowLayout.LEADING));
		itemsSP.setViewportView(itemsPanel);
		
		for (int i = 0; i < 867; i++) {
			itemsPanel.add(new ItemPanel(CommonData.itemCatalogue.get(i).getName(), i+1));
		}
		
		contentPane.setLayout(gl_contentPane);
	    
		
	}
	
	private void setupKeyBindings(JScrollPane p, MapPanel m)
    {
		ActionMap actionMap;        
        InputMap  inputMap;

        actionMap = p.getActionMap();
        inputMap  = p.getInputMap();
        
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), "up");
        actionMap.put("up", new AbstractAction() {
        	public void actionPerformed(ActionEvent ae) {
            	m.moveUp();
            }
        });

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "down");
        actionMap.put("down", new AbstractAction() {
        	public void actionPerformed(ActionEvent ae) {
        		m.moveDown();
        	}
        });

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "left");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0), "left");
        actionMap.put("left", new AbstractAction() {
        	public void actionPerformed(ActionEvent ae) {
        		m.moveLeft();
        	}
    	});

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "right");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0), "right");
        actionMap.put("right", new AbstractAction() {
        	public void actionPerformed(ActionEvent ae) {
        		m.moveRight();
        	}
    	});
    }
	
	public void mouseEntered(MouseEvent e) { e.getComponent().requestFocus(); }
}