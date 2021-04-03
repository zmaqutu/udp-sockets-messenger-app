import java.io.*;
import java.util.Scanner;
import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client extends JFrame{
        private JTextField userText;
        private JTextArea chatWindow;
        //private static DatagramSocket clientSocket;
	private String serverIP;
	private int i = 8;

        //Constructor
        public Client(String host){
                super("UCT Messenger Client");
		this.serverIP = host;
                this.userText = new JTextField();
		//this.clientSocket = new DatagramSocket();
                userText.addActionListener(
                        new ActionListener(){
                                public void actionPerformed(ActionEvent event){
					//sendMessage(event.getActionCommand());	
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
                
		DatagramSocket clientSocket = new DatagramSocket();
                while(true){
			int numberToSend = 8;
                        //Datagram Packet's constructor takes three agruments data, dataLength,IP address, PortNumber
			byte [] data = String.valueOf(numberToSend).getBytes();
                        int dataLength = data.length;
                        InetAddress IP = InetAddress.getLocalHost();
                        DatagramPacket sendPacket = new DatagramPacket(data,dataLength,IP,9999);
                        clientSocket.send(sendPacket);

                        //After sending, accept response
                	byte [] dataToReceive = new byte[1024];
                        DatagramPacket receivePacket = new DatagramPacket(dataToReceive,dataToReceive.length);
                        clientSocket.receive(receivePacket);

                        String str = new String(receivePacket.getData());

                        System.out.println("Server: " + str);
                }
        }
        //this is a method that receives a message and stores it in a string
        /*public DatagramPacket receiveMessage() throws IOException{
		byte [] dataReceived = new byte[1024];          //the data will be received as an array of bytes
                
		DatagramPacket receivePacket = new DatagramPacket(dataReceived,dataReceived.length);
		clientSocket.receive(receivePacket);

		//String receivedMessage = String(receivePacket.getData());
		//showMessage(receivedMessage1);
		showMessage("Server: " + new String(receivePacket.getData()) + "\n");
		return receivePacket; 
	}
	//this method sends a message to connected clients
        private void sendMessage(String message){
                //String message = new String(packet.getData());
                
		try
		{
			byte [] dataToSend = message.getBytes();
			byte [] data = String.valueOf(i).getBytes();
			int dataLength = dataToSend.length;
			//InetAddress IP = InetAddress.getByName(serverIP);
			InetAddress IP = InetAddress.getLocalHost();
			int portNo = 17;
		
			DatagramPacket sendPacket = new DatagramPacket(dataToSend,dataLength,IP,portNo);
		
			clientSocket.send(sendPacket);
			System.out.println("Sent to " + new String (sendPacket.getData()));
			showMessage("Client: " + message);
		}catch(IOException e){
			//chatWindow.append("There was a problem sending the message");
			System.out.println("Whats wrong with my code g");
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
	}*/
                
}


