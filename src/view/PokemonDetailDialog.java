package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import api_control.PokeAPI;
import api_model.Ability;
import api_model.Pokemon;
import javax.swing.border.BevelBorder;
import java.awt.Button;
import javax.swing.JButton;
import java.awt.Rectangle;

public class PokemonDetailDialog extends JDialog {
	
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTable tblStatsTable;
	private JTable tblMovesTable;
	private JTable tblWeaknesses;
	
	public PokemonDetailDialog(String name) {

		PokeAPI api = new PokeAPI();
		Pokemon p = api.getDataOfPokemon(name);
		
		setTitle(name.toUpperCase().replace('-', ' ') + " DETAILS");
		setModal(true);
		setResizable(false);
		setBounds(100, 100, 800, 660);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		// Sprite
		JLabel pokemonImg = new JLabel();
		pokemonImg.setBounds(12, 10, 254, 254);
		pokemonImg.setIcon(resize(new ImageIcon("sprites/pokemon/" + p.getId() + ".png"), 250, 250));
		pokemonImg.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		contentPanel.add(pokemonImg);

		// Name
		JLabel lblName = new JLabel();
		lblName.setBounds(276, 10, 224, 37);
		lblName.setText(p.getName().toUpperCase().replace('-', ' '));
		lblName.setForeground(Color.ORANGE);
		lblName.setHorizontalAlignment(SwingConstants.LEFT);
		lblName.setFont(new Font("Tahoma", Font.BOLD, 30));
		contentPanel.add(lblName);
		
		// Number
		JLabel lblNumberLabel = new JLabel("National #:");
		lblNumberLabel.setBounds(276, 58, 91, 20);
		lblNumberLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		JLabel lblNumber = new JLabel();
		lblNumber.setBounds(377, 58, 123, 20);
		lblNumber.setText(Integer.toString(p.getId()));
		lblNumber.setForeground(Color.LIGHT_GRAY);
		lblNumber.setFont(new Font("Tahoma", Font.BOLD, 16));
		contentPanel.add(lblNumberLabel);
		contentPanel.add(lblNumber);
		
		// Type(s)
		JLabel lblTypeLabel = new JLabel("Type:");
		lblTypeLabel.setBounds(276, 89, 67, 20);
		lblTypeLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		JLabel lblType = new JLabel();
		lblType.setForeground(Color.LIGHT_GRAY);
		lblType.setHorizontalAlignment(SwingConstants.LEFT);
		lblType.setBounds(328, 89, 172, 20);
		lblType.setFont(new Font("Tahoma", Font.BOLD, 16));
		String typesBuilder = "";
		int typesSize = p.getTypes().size();
		for (int i = 0; i < typesSize; i++) {
			api_model.Type t = p.getTypes().get(i).getType();
			typesBuilder += t.getName().toUpperCase();
			if (i != (typesSize - 1)) typesBuilder += ", ";
		}
		lblType.setText(typesBuilder);
		contentPanel.add(lblTypeLabel);
		contentPanel.add(lblType);
		
		// Height
		JLabel lblHeightLabel = new JLabel("Height:");
		lblHeightLabel.setBounds(276, 120, 59, 20);
		lblHeightLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		JLabel lblHeight = new JLabel();
		lblHeight.setBounds(340, 120, 160, 20);
		lblHeight.setText(Integer.toString(p.getHeight()));
		lblHeight.setForeground(Color.LIGHT_GRAY);
		lblHeight.setFont(new Font("Tahoma", Font.BOLD, 16));
		contentPanel.add(lblHeightLabel);
		contentPanel.add(lblHeight);
		
		// Weight
		JLabel lblWeightLabel = new JLabel("Weight:");
		lblWeightLabel.setBounds(276, 151, 63, 20);
		lblWeightLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		JLabel lblWeight = new JLabel();
		lblWeight.setBounds(345, 151, 155, 20);
		lblWeight.setText(Integer.toString(p.getWeight()));
		lblWeight.setForeground(Color.LIGHT_GRAY);
		lblWeight.setFont(new Font("Tahoma", Font.BOLD, 16));
		contentPanel.add(lblWeightLabel);
		contentPanel.add(lblWeight);
		
		// Abilities
		JLabel lblAbilitesLabel = new JLabel("Abilites:");
		lblAbilitesLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblAbilitesLabel.setBounds(276, 182, 67, 20);
		JLabel lblAbilities = new JLabel();
		lblAbilities.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		lblAbilities.setForeground(Color.LIGHT_GRAY);
		lblAbilities.setVerticalAlignment(SwingConstants.TOP);
		lblAbilities.setHorizontalAlignment(SwingConstants.LEFT);
		lblAbilities.setText("");
		lblAbilities.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblAbilities.setBounds(276, 203, 205, 105);
		String abilitiesBuilder = "<html>";
		int abilitiesSize = p.getAbilities().size();
		for (int i = 0; i < abilitiesSize; i++) {
			Ability a = p.getAbilities().get(i).getAbility();
			abilitiesBuilder += a.getName().toUpperCase() + "<br>";
		}
		abilitiesBuilder += "</html>";
		lblAbilities.setText(abilitiesBuilder);
		contentPanel.add(lblAbilitesLabel);
		contentPanel.add(lblAbilities);
		
		DefaultTableCellRenderer defaultCellRenderer = new DefaultTableCellRenderer();
		defaultCellRenderer.setHorizontalAlignment( JLabel.CENTER );
		
		// Stats
		@SuppressWarnings("serial")
		DefaultTableModel modelStats = new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
						"Stat Name", "EV", "Base"
				}
				) {
			@SuppressWarnings("rawtypes")
			Class[] columnTypes = new Class[] {
					String.class, Integer.class, Integer.class
			};
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
					false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		};
		for (int i = 0; i < p.getStats().size(); i++) {
			modelStats.addRow(new Object[]{capitalizeAndFormat(p.getStats().get(i).getStat().getName()), p.getStats().get(i).getEffort(), p.getStats().get(i).getBase_stat()});
		}
		
		JScrollPane spStatsTable = new JScrollPane();
		spStatsTable.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		spStatsTable.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		spStatsTable.setEnabled(false);
		spStatsTable.setBounds(35, 275, 205, 150);
		contentPanel.add(spStatsTable);
		tblStatsTable = new JTable();
		spStatsTable.setViewportView(tblStatsTable);
		tblStatsTable.setModel(modelStats);
		tblStatsTable.getColumnModel().getColumn(0).setResizable(false);
		tblStatsTable.getColumnModel().getColumn(0).setPreferredWidth(100);
		tblStatsTable.getColumnModel().getColumn(0).setMinWidth(80);
		tblStatsTable.getColumnModel().getColumn(0).setMaxWidth(200);
		tblStatsTable.getColumnModel().getColumn(1).setResizable(false);
		tblStatsTable.getColumnModel().getColumn(1).setPreferredWidth(30);
		tblStatsTable.getColumnModel().getColumn(1).setMinWidth(10);
		tblStatsTable.getColumnModel().getColumn(1).setMaxWidth(60);
		tblStatsTable.getColumnModel().getColumn(1).setCellRenderer( defaultCellRenderer );
		tblStatsTable.getColumnModel().getColumn(2).setResizable(false);
		tblStatsTable.getColumnModel().getColumn(2).setPreferredWidth(30);
		tblStatsTable.getColumnModel().getColumn(2).setMinWidth(10);
		tblStatsTable.getColumnModel().getColumn(2).setMaxWidth(60);
		tblStatsTable.getColumnModel().getColumn(2).setCellRenderer( defaultCellRenderer );
		
		// Moves
		@SuppressWarnings("serial")
		DefaultTableModel modelMoves = new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
						"Move Name", "Typ", "Pwr"
				}
				) {
			@SuppressWarnings("rawtypes")
			Class[] columnTypes = new Class[] {
					String.class, Integer.class, Integer.class
			};
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
					false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		};
		for (int i = 0; i < p.getStats().size(); i++) {
			modelMoves.addRow(new Object[]{capitalizeAndFormat(p.getMoves().get(i).getMove().getName()), "N/A", "N/A"}); // modelMoves.addRow(new Object[]{p.getMoves().get(i).getMove().getName(), p.getMoves().get(i).getMove().getType, p.getMoves().get(i).getPower()});
		}
		JLabel lblMovesAreaLabel = new JLabel("Moves:");
		lblMovesAreaLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblMovesAreaLabel.setBounds(520, 30, 115, 15);
		contentPanel.add(lblMovesAreaLabel);
		JScrollPane spMovesTable = new JScrollPane();
		spMovesTable.setBounds(510, 55, 260, 368);
		contentPanel.add(spMovesTable);
		tblMovesTable = new JTable();
		spMovesTable.setViewportView(tblMovesTable);
		tblMovesTable.setModel(modelMoves);
		tblMovesTable.getColumnModel().getColumn(0).setResizable(false);
		tblMovesTable.getColumnModel().getColumn(0).setPreferredWidth(160);
		tblMovesTable.getColumnModel().getColumn(0).setMinWidth(10);
		tblMovesTable.getColumnModel().getColumn(0).setMaxWidth(200);
		tblMovesTable.getColumnModel().getColumn(1).setResizable(false);
		tblMovesTable.getColumnModel().getColumn(1).setPreferredWidth(30);
		tblMovesTable.getColumnModel().getColumn(1).setMinWidth(10);
		tblMovesTable.getColumnModel().getColumn(1).setMaxWidth(60);
		tblMovesTable.getColumnModel().getColumn(1).setCellRenderer( defaultCellRenderer );
		tblMovesTable.getColumnModel().getColumn(2).setResizable(false);
		tblMovesTable.getColumnModel().getColumn(2).setPreferredWidth(30);
		tblMovesTable.getColumnModel().getColumn(2).setMinWidth(10);
		tblMovesTable.getColumnModel().getColumn(2).setMaxWidth(60);
		tblMovesTable.getColumnModel().getColumn(2).setCellRenderer( defaultCellRenderer );
		
		// Weaknesses
		JLabel lblWeaknessesTableLabel = new JLabel("Weaknesses:");
		lblWeaknessesTableLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblWeaknessesTableLabel.setBounds(269, 319, 115, 15);
		contentPanel.add(lblWeaknessesTableLabel);
		@SuppressWarnings("serial")
		DefaultTableModel modelWeaknesses = new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
						"A", "B", "C", "D", "E", "F", "G", "H", "I"
				}
				) {
			@SuppressWarnings("rawtypes")
			Class[] columnTypes = new Class[] {
					String.class, Integer.class, Integer.class
			};
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
					false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		};
		modelWeaknesses.addRow(new Object[]{"NOR", "FIR", "WAT", "ELE", "GRA", "ICE", "FIG", "POI", "GRO"});
		modelWeaknesses.addRow(new Object[]{"n/a", "n/a", "n/a", "n/a", "n/a", "n/a", "n/a", "n/a", "n/a"});
		modelWeaknesses.addRow(new Object[]{"FLY", "PSY", "BUG", "ROC", "GHO", "DRA", "DAR", "STE", "FAI"});
		modelWeaknesses.addRow(new Object[]{"n/a", "n/a", "n/a", "n/a", "n/a", "n/a", "n/a", "n/a", "n/a"});
		tblWeaknesses = new JTable();
		tblWeaknesses.setBounds(260, 345, 225, 80);
		contentPanel.add(tblWeaknesses);
		tblWeaknesses.setModel(modelWeaknesses);
		for (int i = 0; i < 9; i++) {
		tblWeaknesses.getColumnModel().getColumn(i).setResizable(false);
		tblWeaknesses.getColumnModel().getColumn(i).setPreferredWidth(25);
		tblWeaknesses.getColumnModel().getColumn(i).setMinWidth(20);
		tblWeaknesses.getColumnModel().getColumn(i).setMaxWidth(30);
		tblWeaknesses.setFont(new Font("Tahoma", Font.BOLD, 8));
		tblWeaknesses.getColumnModel().getColumn(i).setCellRenderer( defaultCellRenderer );
		}
		
		// ----- Bottom Panel
		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setBounds(10, 435, 775, 185);
		contentPanel.add(panel);
		panel.setLayout(null);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnAdd.setForeground(Color.GREEN);
		btnAdd.setBackground(Color.DARK_GRAY);
		btnAdd.setBounds(10, 10, 120, 80);
		panel.add(btnAdd);
		
		JButton btnRemove = new JButton("Remove");
		btnRemove.setForeground(Color.RED);
		btnRemove.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnRemove.setBackground(Color.DARK_GRAY);
		btnRemove.setBounds(10, 95, 120, 80);
		panel.add(btnRemove);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(134, 10, 630, 165);
		panel.add(panel_1);
		panel_1.setLayout(null);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.DARK_GRAY);
		panel_2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_2.setBounds(5, 5, 100, 155);
		panel_1.add(panel_2);
		
		JPanel panel_2_1 = new JPanel();
		panel_2_1.setBackground(Color.DARK_GRAY);
		panel_2_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_2_1.setBounds(109, 5, 100, 155);
		panel_1.add(panel_2_1);
		
		JPanel panel_2_2 = new JPanel();
		panel_2_2.setBackground(Color.DARK_GRAY);
		panel_2_2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_2_2.setBounds(213, 5, 100, 155);
		panel_1.add(panel_2_2);
		
		JPanel panel_2_3 = new JPanel();
		panel_2_3.setBackground(Color.DARK_GRAY);
		panel_2_3.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_2_3.setBounds(317, 5, 100, 155);
		panel_1.add(panel_2_3);
		
		JPanel panel_2_4 = new JPanel();
		panel_2_4.setBackground(Color.DARK_GRAY);
		panel_2_4.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_2_4.setBounds(421, 5, 100, 155);
		panel_1.add(panel_2_4);
		
		JPanel panel_2_5 = new JPanel();
		panel_2_5.setBackground(Color.DARK_GRAY);
		panel_2_5.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_2_5.setBounds(525, 5, 100, 155);
		panel_1.add(panel_2_5);
		
	}
	
	public static ImageIcon resize(ImageIcon image, int width, int height) {
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
        Graphics2D g2d = (Graphics2D) bi.createGraphics();
        g2d.addRenderingHints(//  ww  w  . jav  a2 s. c o m
                new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        g2d.drawImage(image.getImage(), 0, 0, width, height, null);
        g2d.dispose();
        return new ImageIcon(bi);
    }
	
	private static String capitalizeAndFormat(String inString) {
        String outString = "";
        String words[] = inString.split("\\W"); 
	    for(String w:words){  
	        String first = w.substring(0,1);  
	        String afterfirst = w.substring(1);  
	        outString += first.toUpperCase() + afterfirst + " ";
	    }
        return outString.trim();
    }
}
