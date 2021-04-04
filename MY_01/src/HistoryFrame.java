import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class HistoryFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	public static final int HEIGHT = 300, WIDTH = 300, AREA_HEIGHT = 80, AREA_WIDTH = 80;
	
	private JLabel title;
	private JTextArea showarea;
	//private JScrollPane scroll;
	
	private String server = "jdbc:mysql://140.119.19.73:9306/";
	private String database = "TG16";
	private String url = server+database;
	private String username = "TG16";
	private String password = "hiec7n";
	
	public HistoryFrame() {
		
		
		add(this.createTitlePanel(),BorderLayout.NORTH);
		add(new JScrollPane(this.createShowPanel()),BorderLayout.CENTER);
		
		this.setSize(WIDTH,HEIGHT);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
	}

	private JPanel createShowPanel() {
		// TODO Auto-generated method stub
		showarea = new JTextArea(AREA_HEIGHT,AREA_WIDTH);
		showarea.append("Name      Time      Map");
		JPanel panel = new JPanel();
		try {
			
			Connection conn = DriverManager.getConnection(url,username,password);
			String query = "SELECT * FROM History_Score";
			Statement stat = conn.createStatement();
			
			ResultSet result = stat.executeQuery(query);
			
			while(result.next()) {
				String player = result.getString("PlayerName");
				int time = result.getInt("Time");
				String map = result.getString("Map");
				
				String show = String.format("\n%-10s%-10d%-10s",player,time,map);
				
				showarea.append(show);
				
				System.out.println(show);
			}
			
		}catch(SQLException e) {
			
			e.printStackTrace();
		}
		
		panel.add(showarea);
		
		return panel;
	}

	private JPanel createTitlePanel() {
		// TODO Auto-generated method stub
		title = new JLabel("History Score");
		JPanel panel = new JPanel();
		panel.add(title);
		
		return panel;
	}
	
	
}