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
	private InetAddress clientIP;
	private DatagramPacket receivePacket;

	//Constructor
	public Server()throws IOException{
		super("UCT Messenger");
		userText = new JTextField();
		//this.serverSocket = new DatagramSocket();
		userText.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent event){
					//sendMessage(event.getActionCommand());		//using showMessage temporarily
					System.out.println(event.getActionCommand());
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
	public void startRunning()throws IOException{
		serverSocket = new DatagramSocket(9999);
			Scanner scan = new Scanner(System.in);
			while(true){
					byte [] dataReceived = new byte[1024];
					//To use a socket we use DatagramSocket 
					//to send / recieve the data we use DatagramPackets
					receivePacket = new DatagramPacket(dataReceived,dataReceived.length);
					serverSocket.receive(receivePacket);
					String str = new String(receivePacket.getData());
					
					showMessage("Client :" + str);
					

					/*String square = scan.nextLine();

					// now send the data the square back use similar code to client side
					byte [] dataToSendBack = (square + "").getBytes();
					//dataLength					
					int dataLength = dataToSendBack.length;
					//ipAddress
					InetAddress IP = receivePacket.getAddress();
					//portNumber
					int portNo = receivePacket.getPort();
        				DatagramPacket sendPacket = new DatagramPacket(dataToSendBack,dataLength,IP,portNo);
                			serverSocket.send(sendPacket);*/
			}
	}
	//this is a method that receives a message and stores it in a string
/*	public DatagramPacket receiveMessage() throws IOException{
		byte [] dataReceived = new byte[1024];		//the data will be received as an array of bytes
		
		DatagramPacket receivePacket = new DatagramPacket(dataReceived,dataReceived.length);
		
		System.out.println(serverSocket.getLocalPort());

		serverSocket.receive(receivePacket);

                System.out.println(receivePacket.getPort());
		System.out.println("Package received at port" +receivePacket.getPort());
		//String receivedMessage = String(receivePacket.getData());
		//showMessage(receivedMessage1);
		//clientIP = receivePacket.getAddress();
		//System.out.println("Message received from " + clientIP);
		//showMessage("Client: " + new String(receivePacket.getData()));
		return receivePacket; 


	}*/
	//this method sends a message to connected clients
	private void sendMessage(String message){
		//String message = new String(packet.getData());
		
		try{
			byte [] dataToSend = message.getBytes();
			int dataLength = dataToSend.length;
			InetAddress IP = receivePacket.getAddress();
			//InetAddress IP = clientIP;
			
			int portNo = receivePacket.getPort();
			System.out.println("Port to send to is: " + portNo);

			DatagramPacket sendPacket = new DatagramPacket(dataToSend,dataLength,IP,portNo);
			System.out.println("Working till here");
			serverSocket.send(sendPacket);
			showMessage(message);
		}catch(IOException e){
			chatWindow.append("Unable to send to client \n");
		}
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
