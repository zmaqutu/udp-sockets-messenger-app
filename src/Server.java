import java.io.*;
import java.util.Scanner;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Server Extends JFrame{
	private JTextField userInput;
	private JTextArea chatWindow;
	private DatagramSocket serverSocket;

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
			serverSocket = new DatagramSocket(9999);
			while(true){
				try{
					DatagramPacket packet = receiveMessage();
				}catch(EOFException e){
					showMessage("\n Server Ended the Connection");
				}finally{
					close();
				}
			}
		}catch (IOExceotion e){
			e.printStackTrace();
		}
	}
	//this is a method that receives a message and stores it in a string
	public DatagramPacket receiveMessage() throws IOException{
		byte [] dataReceived = new byte[1024];		//the data will be received as an array of bytes
		
		DatagramPacket receivePacket = new DatagramPacket(dataReceived,dataReceived.length);
                serverSocket.receive(receivePacket);

		//String receivedMessage = String(receivePacket.getData());
		//showMessage(receivedMessage1);
		return receivePacket;


	}
	
	
}
