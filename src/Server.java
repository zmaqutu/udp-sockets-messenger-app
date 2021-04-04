import java.io.*;
import java.util.Scanner;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Server extends JFrame{
	private JTextArea chatWindow;
	private JPanel contentPane;
        private JLabel bannerLabel;
	private JPanel panelSouth;
	private JLabel statusLabel; 
        private JButton startButton;


	private DatagramSocket serverSocket;
	private InetAddress clientIP;
	private DatagramPacket receivePacket;

	//Constructor
	public Server()throws IOException{
		super("UCT Messenger");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 570, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		bannerLabel = new JLabel("CHAT SERVER by Zongo");
		bannerLabel.setForeground(new Color(0,128,0));
		bannerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		bannerLabel.setFont(new Font("Tahoma", Font.PLAIN, 40));
		contentPane.add(bannerLabel, BorderLayout.NORTH);

		//south panel that houses server run status and start button
		panelSouth = new JPanel();
		panelSouth.setBackground(new Color(152,251,152));
		contentPane.add(panelSouth,BorderLayout.SOUTH);


		//the start button
		startButton = new JButton("START");
		startButton.setOpaque(false);
		startButton.setFont(new Font("Tahoma", Font.PLAIN, 25));
		//server run status
		statusLabel = new JLabel("Server offline");
		statusLabel.setBorder(new LineBorder(new Color(0,0,0),2));
		statusLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		statusLabel.setForeground(Color.WHITE);
		
		statusLabel.setBackground(new Color(220,20,60));
		statusLabel.setOpaque(true);
		statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

		GroupLayout gl_panel = new GroupLayout(panelSouth);
		gl_panel.setHorizontalGroup(
				gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(statusLabel,GroupLayout.DEFAULT_SIZE,393,Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(startButton,GroupLayout.PREFERRED_SIZE,127,GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
				);

		gl_panel.setVerticalGroup(
                                gl_panel.createParallelGroup(Alignment.TRAILING)
                                .addGroup(gl_panel.createSequentialGroup()
                                        .addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
                                        .addComponent(statusLabel,Alignment.LEADING,GroupLayout.DEFAULT_SIZE,45,Short.MAX_VALUE)
                                        .addComponent(startButton,Alignment.LEADING,GroupLayout.PREFERRED_SIZE,50,GroupLayout.PREFERRED_SIZE))
                                        .addContainerGap())
                                );

		panelSouth.setLayout(gl_panel);


		//contentPane.add(startButton, BorderLayout.SOUTH);

		//text on the chat window
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);

		chatWindow = new JTextArea();
		chatWindow.setBackground(Color.BLACK);
		chatWindow.setForeground(Color.WHITE);
		chatWindow.setLineWrap(true);
		scrollPane.setViewportView(chatWindow);
		
		
		
		/*userText.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent event){
					//sendMessage(event.getActionCommand());		//using showMessage temporarily
					System.out.println(event.getActionCommand());
					//userText.setText("");
				}
			} 
		);*/


		//add(userText,BorderLayout.NORTH);
		//chatWindow = new JTextArea();
		//add(new JScrollPane(chatWindow));
		//setSize(500,500);
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
