import java.io.*;
import javax.swing.JFrame;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class clientDriver {

	public static void main(String[] args) throws Exception{

                Client udpClient = new Client("localhost");
                udpClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
		udpClient.loginButton.addActionListener(
                        new ActionListener(){
                                public void actionPerformed(ActionEvent event){
                                       if(udpClient.loginButton.getText().equals("chat with")){
                                                udpClient.userText.setEditable(true);
						udpClient.userName.setEditable(false);
						udpClient.recipientName.setEditable(false);
						udpClient.loginButton.setText("Welcome");
						udpClient.loginButton.setEnabled(false);
						udpClient.login(udpClient.userName.getText(),udpClient.recipientName.getText());
                                       }
                                }
                        }
                );
		udpClient.startRunning();
	}
}
