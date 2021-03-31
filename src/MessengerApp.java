import javax.swing.*;

//import java.awt.Dimension;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.BorderLayout;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;



public class MessengerApp {

	public static void setupGUI(){
		JFrame frame = new JFrame();		//creates a new frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500,500);
		frame.setTitle("Welcome to UCT Messenger");
		frame.setVisible(true);			//set the frame as visible

	}
	public static void main(String[] args) throws Exception{

		System.out.println("Back to Java code");
		//setupGUI();
		//
		DatagramSocket clientSocket = new DatagramSocket(); 
		
		int numberToSend = 8;

		//convert number to send to an array of bytes
		

		//Datagram Packet's constructor takes three agruments
		//data
		byte [] data = String.valueOf(numberToSend).getBytes();
		//dataLength
		int dataLength = data.length;
		//ipAddress
		InetAddress IP = InetAddress.getLocalHost();
		//portNumber
		DatagramPacket sendPacket = new DatagramPacket(data,dataLength,IP,9999);
		clientSocket.send(sendPacket);

		//After sending, accept response
		byte [] dataToReceive = new byte[1024];
		DatagramPacket receivePacket = new DatagramPacket(dataToReceive,dataToReceive.length);
		clientSocket.receive(receivePacket);

		String str = new String(receivePacket.getData());

		System.out.println("Result is: " + str);

	
	}
}
