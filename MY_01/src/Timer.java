import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Timer extends JFrame implements Runnable {
	
	private static final long serialVersionUID = 1L;
	private boolean isRunning = false;
	
	public static final int WIDTH = 30,HEIGHT = 100;
	
	
	private JButton submitgrade;
	private JLabel showcount;
	private int count;
	
	private Thread thread;
	
	private String name;
	
	private String server = "jdbc:mysql://140.119.19.73:9306/";
	private String database = "TG16";
	private String url = server+database;
	private String username = "TG16";
	private String password = "hiec7n";

	
	public Timer(String name) {
		
		this.name = name;
		JPanel countpanel = this.createcountPanel();
		JPanel btnpanel = this.createSubmitBtnPanel();
		add(countpanel, BorderLayout.NORTH);
		add(btnpanel, BorderLayout.SOUTH);
		
		this.setSize(WIDTH,HEIGHT);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	
	public JPanel createSubmitBtnPanel() {
		
		JPanel panel = new JPanel();
		this.submitgrade = new JButton("Submit Grade");
		
		class submitBtnActionListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent event) {
				// TODO Auto-generated method stub
				try {
					
					Connection conn = DriverManager.getConnection(url,username,password);
					String command = "UPDATE History_Score "
							+ "SET History_Score.Time = ? "
							+ "WHERE History_Score.PlayerName = ? ";
					PreparedStatement stat5 = conn.prepareStatement(command);
					stat5.setInt(1, count);
					stat5.setString(2, name);
					stat5.execute();
					stat5.close();
				}catch(SQLException e) {
					
					e.printStackTrace();
				}
				
			}
			
		}
		
		ActionListener submitbtnlistener = new submitBtnActionListener();
		submitgrade.addActionListener(submitbtnlistener);
		
		
		
		panel.add(submitgrade);
		return panel;
	}
	
	public JPanel createcountPanel() {
		
		showcount = new JLabel("Start !");
		JPanel panel = new JPanel();
		panel.add(showcount);
		return panel;
	}
	
	
	public synchronized void start() {
		if(isRunning)return;
		isRunning = true;
		thread = new Thread(this);
		thread.start();
	}
	
	
	public synchronized void stop() {
		//At here add in the data base insert operation 
		if(!isRunning)return;
		isRunning = false;
	}
	
	public int getCount() {
		return this.count;
	}
	
	
	public void run() {
		
		double timer = System.currentTimeMillis();
		
		while(isRunning) {
			
			if(System.currentTimeMillis() - timer >= 1000) {
				
				count++;
				showcount.setText(Integer.toString(count));

				timer+=1000;
			}	
		}
		
		stop();
	}
	
}