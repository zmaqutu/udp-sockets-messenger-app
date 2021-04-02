import java.io.*;
import java.util.Scanner;
import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Server extends JFrame{
	private JTextField userText;
	private JTextArea chatWindow;
	private DatagramSocket serverSocket;

	//Constructor
	public Server(){
		super("UCT Messenger");
		this.userText = new JTextField();
		userText.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent event){
					showMessage("Server: " + event.getActionCommand());		//using showMessage temporarily
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
					//close();
				}
			}
		}catch (IOException e){
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
		showMessage("Client: " + new String(receivePacket.getData()));
		return receivePacket; 


	}
	//this method sends a message to connected clients
	private void sendMessage(String message, DatagramPacket packet) throws IOException{
		//String message = new String(packet.getData());
		
		byte [] dataToSend = packet.getData();
		int dataLength = dataToSend.length;
		InetAddress IP = packet.getAddress();
		int portNo = packet.getPort();

		DatagramPacket sendPacket = new DatagramPacket(dataToSend,dataLength,IP,portNo);
		serverSocket.send(sendPacket);
		showMessage(message);
	}
	//this method creates a new thread that appends a message to the gui
	public void showMessage(final String message){
		SwingUtilities.invokeLater(
				new Runnable(){
					public void run(){
						chatWindow.append(message + "\n");
					}
				}
		);
	}
	
	
}
