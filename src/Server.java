import java.io.*;
import java.util.Scanner;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Server Extends JFrame{
	private JTextField userInput;
	private JTextArea chatWindow;
	private DatagramSocket serverSocket = new DatagramSocket(9999);

	//Constructor
	public Server(){
		super("UCT Messenger");
		this.userText = new JTextField();
		userText.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent event){
					sendMessage(event.getActionCommand());
					userText.setText("");
				}
			} 
		);
		add(userText,BorderLayout.NORTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow));
		setSize(500,500);
		setVisible(true);
	}

	//setup and run the server, this will be called after GUI is setup
	public void startRunning(){
		try{
		}catch (IOExceotion e){
			e.printStackTrace();
		}
	}
	
	
}