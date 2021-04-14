import java.io.*;
import javax.swing.JFrame;
import java.util.Scanner;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
public class serverDriver {

	public static void main(String [] args) throws Exception{
		Scanner scan = new Scanner(System.in);	

		Server udpServer = new Server();
		udpServer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		DateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
		Date localDate = new Date();
		udpServer.showMessage("[" + dateTimeFormat.format(localDate) + "] " + "Welcome administrator ... click start to run the server");
		
		udpServer.startButton.addActionListener(  
                        new ActionListener(){
                                public void actionPerformed(ActionEvent event){
                                       if(udpServer.startButton.getText().equals("START")){ 
						udpServer.statusLabel.setText("server running");
						udpServer.startButton.setText("Stop");
                                        	udpServer.statusLabel.setBackground(new Color(0,150,0));
						

						udpServer.showMessage("Starting server ...");
						try{
							Thread.sleep(2000);
						}
						catch(InterruptedException e){
							e.printStackTrace();
						}
						udpServer.showMessage("[" + dateTimeFormat.format(localDate) + "] " + "The server is now running and can receive messages");
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
