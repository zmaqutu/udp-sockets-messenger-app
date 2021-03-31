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
	public static void main(String[] args) {

		System.out.println("Back to Java code");
		//setupGUI();
		//
		DatagramSocket ds = new DatagramSocket(); 
		
		int numberToSend = 8;

		//convert number to send to an array of bytes
		

		//Datagram Packet's constructor takes three agruments
		//data
		byte [] data = (numberToSend + "").getBytes();
		//dataLength
		dataLength = data.length;
		//ipAddress
		InetAddress ia = InetAddress.getLocalHost();
		//portNumber
		DatagramPacket dp = new DatagramPacket(data,dataLength,ia,9999);
		ds.send(dp);

		//After sending, accept response
		byte [] dataToRecieve new byte[1024];
		DatagramPacket dp1 = new DatagramPacket(data,data.length);
		ds.receive(dp1);

		String str = new String(dp1.getData());

		System.out.println("Result is: " + str);

	
	}
}
