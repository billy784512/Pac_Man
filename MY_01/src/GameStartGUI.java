import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


 public class GameStartGUI extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	public static final int FRAME_WIDTH = 300;
	public static final int FRAME_HEIGHT = 200;
	public static final int FIELD_WIDTH = 10;
	
	
	private JLabel characterLabel,mapLabel,nameLabel;  
	private JButton startBtn;
	private JComboBox<String> characterCombo,mapCombo;
	private JMenuBar menubar;
	JTextField nameField;
	
	private String server = "jdbc:mysql://140.119.19.73:9306/";
	private String database = "TG16";
	private String url = server+database;
	private String username = "TG16";
	private String password = "hiec7n"; 
	
	private String name;
	private int character;
	
	public GameStartGUI() {
		this.characterLabel = new JLabel("Character: ");
		this.mapLabel = new JLabel("Map: ");
		this.nameLabel = new JLabel("Your Name: ");
		
		this.nameField = new JTextField(FIELD_WIDTH);
		
		this.mapCombo = new JComboBox<String>();
		mapCombo.addItem("map1");
		mapCombo.addItem("map2");
		
		this.characterCombo = new JComboBox<String>();
		characterCombo.addItem("character1");
		characterCombo.addItem("character2");
		
		this.menubar = new JMenuBar();
		setJMenuBar(menubar);
		//menubar.add(createSettingMenu());
		menubar.add(createHistoryMenu());
		
		
		
		this.createPanel();
		setSize(FRAME_WIDTH,FRAME_HEIGHT);
	}
	
	public JPanel createChoosePanel() {
		
		JPanel panel = new JPanel();
		
		panel.add(this.mapLabel);
		panel.add(this.mapCombo);
		panel.add(this.characterLabel);
		panel.add(this.characterCombo);
		panel.add(this.nameLabel);
		panel.add(this.nameField);
		
		return panel;
		
	}
	
	public JPanel createStartBtn() {
		
		JPanel panel = new JPanel();
		this.startBtn = new JButton("Game Start!");
		
		class StartActionListener implements ActionListener{
			
			public void actionPerformed(ActionEvent event) {
				name = nameField.getText();
				if (characterCombo.getSelectedItem().toString().equals("character1")) {
					character = 1;
				} else {
					character = 2;
				}
				String map = mapCombo.getSelectedItem().toString();
				// Insert the map and name information into database
				try {
					
					Connection conn = DriverManager.getConnection(url,username, password);
					String query = "INSERT INTO History_Score VALUES(?,0,?)";
					PreparedStatement stat = conn.prepareStatement(query);
					stat.setString(1, name);
					stat.setString(2, map);
					
					stat.execute();
					conn.close();
				
				}catch(SQLException e) {
					JFrame frame = new JFrame("訊息");
					frame.setSize(300, 100);
					JOptionPane.showMessageDialog(frame,"Name Invalid");
				}
				
				
				
				if(map.equals("map1")) {
					Game.theReallyStartingPoint(1, character, name);
				}
				
				else if(map.equals("map2")) {
					Game.theReallyStartingPoint(2, character, name);
				}
			}
		}
		
		startBtn.addActionListener( new StartActionListener());
		panel.add(this.startBtn);
		
		return panel;
		
	}
	
	public void createPanel() {
		
		JPanel choosepanel = this.createChoosePanel();
		JPanel startbtn = this.createStartBtn();
		
		add(choosepanel,BorderLayout.CENTER);
		add(startbtn, BorderLayout.SOUTH);
		
	}
	
	//public JMenu createSettingMenu() {
		
		//JMenu settingmenu = new JMenu("Setting");
		//settingmenu.add(createTimeLimitItem());
		//return settingmenu;
		
	//}
	
	//public JMenuItem createTimeLimitItem() {
		
		//JMenuItem timeLim = new JMenuItem("Time Limit");
		//return timeLim;
		
	//}
	
	public JMenu createHistoryMenu() {
		
		JMenu history = new JMenu("History");
		history.add(createLocalHistoryMenuItem());
		return history;
		
	}
	
	public JMenuItem createLocalHistoryMenuItem() {
		
		JMenuItem localhistory = new JMenuItem("Local History");
		
		class MenuItemListener implements ActionListener {
			
			public void actionPerformed(ActionEvent event){
				HistoryFrame frame = new HistoryFrame();
				frame.setVisible(true);
			}
		}
		
		ActionListener menuitemlistener = new MenuItemListener();
		
		localhistory.addActionListener(menuitemlistener);
		
		return localhistory;
		
	}
	

	
	
	public static void main(String[] args) {
		// TODO Auto-generated method
		JFrame entrance = new GameStartGUI();
		entrance.setLocationRelativeTo(null);
		entrance.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		entrance.setTitle("Welcome to Pacman");
		entrance.setVisible(true);
		
		
	}

}
	
	
	
