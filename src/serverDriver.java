import java.io.*;
import javax.swing.JFrame;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
public class serverDriver {

	public static void main(String [] args) throws Exception{
		//socket to accept data at port 9999
		//DatagramSocket serverSocket = new DatagramSocket(9999);
		Scanner scan = new Scanner(System.in);	

		Server udpServer = new Server();
		udpServer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		udpServer.startButton.addActionListener(  
                        new ActionListener(){
                                public void actionPerformed(ActionEvent event){
                                       if(udpServer.startButton.getText().equals("START")){ 
						udpServer.statusLabel.setText("server running");
						udpServer.startButton.setText("Stop");
                                        	udpServer.statusLabel.setBackground(new Color(0,150,0));
				       }
				       else{  
					       udpServer.statusLabel.setText("server offline");
					       udpServer.startButton.setText("START");
					       udpServer.statusLabel.setBackground(new Color(220,20,60));
				       }
                                }
                        } 
                );
		
		udpServer.startRunning();
	}
}
