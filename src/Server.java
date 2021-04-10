import java.io.*;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
	public JLabel statusLabel; 
        public JButton startButton;
	private HashMap<String,clientHandler> connected = new HashMap<>();
	private HashMap<String,clientHandler> groupChatUsers = new HashMap<>();


	private DatagramSocket serverSocket;
	private DatagramSocket loginSocket;
	private DatagramSocket sendSocket;
	//private DatagramSocket groupSocket;
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
		startButton.addActionListener(
                        new ActionListener(){
                                public void actionPerformed(ActionEvent event){
                                }
                        } 
                );
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



		//text on the chat window
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);

		chatWindow = new JTextArea();
		chatWindow.setBackground(Color.BLACK);
		chatWindow.setForeground(Color.WHITE);
		chatWindow.setLineWrap(true);
		scrollPane.setViewportView(chatWindow);

		setVisible(true);
	}

	//setup and run the server, this will be called after GUI is setup
	public void startRunning()throws IOException{
		serverSocket = new DatagramSocket(9999);
		loginSocket = new DatagramSocket(1776);
		sendSocket = new DatagramSocket();

		while(true){
			byte [] loginData = new byte[1024];

			DatagramPacket loginPacket = new DatagramPacket(loginData,loginData.length);	
			loginSocket.receive(loginPacket);
	

			String [] chatInfo = new String(loginPacket.getData()).split(" ");
			String userName = chatInfo[0].trim();
			String recipientName = chatInfo[1].trim();
			InetAddress ip = loginPacket.getAddress();
			int portNo = Integer.valueOf(chatInfo[2].trim());


			if(recipientName.equals("GROUP")){
				clientHandler groupUser = new clientHandler(userName,"GROUP",serverSocket,ip,portNo);
				groupChatUsers.put(userName,groupUser);

				if(groupChatUsers.size() > 1){
					String gcWelcome = userName + " has joined the group chat";
					broadcastMessage(gcWelcome,"Server");				//notify groupchat member has joined
				}
				new Thread(groupUser).start();
			}
			else{
				clientHandler userProfile = new clientHandler(userName,recipientName,serverSocket,ip,portNo);
                        
                        
				connected.put(userName,userProfile);

				//if the person you want to chat with in connected/online send them a message that you are online
				//else send yourself message telling you they arent online
				if(connected.containsKey(recipientName)){
					String joinNotification = userName + " has just joined the chat";
					sendMessage(joinNotification,"Server",recipientName);
				}
				else{
					String offlineMessage = recipientName + " is unable to receive your messages at the moment (offline)"; 
					sendMessage(offlineMessage,"Server",userName);
				}
			
				new Thread(userProfile).start();
			}
			//sendMessage(userProfile.getUserName(),userProfile.getRecipient());
		}
	}
	//this method sends a message to connected clients
	private void sendMessage(String message, String sender,String recipient){
		
		try{
			
			String messageInfo = message + "\n" + sender + "\n" + recipient;
			byte [] dataToSend = messageInfo.getBytes();
			int dataLength = dataToSend.length;
				
			InetAddress IP = connected.get(recipient).getIP();
			//int portNo = 1996;
			int portNo = connected.get(recipient).getPortNo();
			DatagramPacket sendPacket = new DatagramPacket(dataToSend,dataLength,IP,portNo);
			
			sendSocket.send(sendPacket);
	}catch(IOException e){
			chatWindow.append("Unable to send to client \n");
		}
	}
	private void broadcastMessage(String message, String sender){
		String messageInfo = message + "\n" + sender;
		byte [] dataToSend = messageInfo.getBytes();
		int dataLength = dataToSend.length;

		for(String userName : groupChatUsers.keySet()){
			try{
				InetAddress IP = groupChatUsers.get(userName).getIP();
				int portNo = groupChatUsers.get(userName).getPortNo();

				DatagramPacket sendPacket = new DatagramPacket(dataToSend,dataLength,IP,portNo);

				sendSocket.send(sendPacket);
			}catch(IOException e){
				chatWindow.append("Unable to send messages to the group chat. Please try again");
			}

		}
	}
	//this method broadcasts a message to everyone connected to the server
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
	public class clientHandler implements Runnable{
        	private DatagramSocket clientSocket;
        	public String userName;
		private String recipient;
		public InetAddress userIP;
		int portNo;
        	//constructor
        	public clientHandler(String name,String sendingTo, DatagramSocket socket,InetAddress IP,int userPortNo) throws IOException{
                	//super();
                	this.clientSocket = socket;             //this is my socket that will be receiving messages
                	this.userName = name;
			this.recipient = sendingTo;
			this.userIP = IP;
			this.portNo = userPortNo;

			DateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
                        //DateFormat forTime = new SimpleDateFormat("hh:mm:ss");

                        Date localDate = new Date();
			System.out.println(userName + " has just joined the chat");
			//String joinNotification = name + " has just joined the chat";
			//sendMessage(joinNotification,"Server",sendingTo);
			if(sendingTo.equals("GROUP")){
				showMessage("["+ dateTimeFormat.format(localDate) + "] " + "[Server]: "  + userName + " has just joined the group chat");
			}
			else{
				showMessage("["+ dateTimeFormat.format(localDate) + "] " + "[Server]: "  + userName + " has just joined the chat");
			}
        	}
		/*public clientHandler(String name, DatagramSocket socket,InetAddress IP, int userPortNo) throws IOException{
			this.clientSocket = socket;
			this.userName = name;
			this.userIP = IP;
			this.portNo = userPortNo;

			DateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
			Date localDate = new Date();
			showMessage("["+ dateTimeFormat.format(localDate) + "] " + "[Server]: "  + userName + " has just joined the group chat");
		}*/
		//getters
		public String getUserName(){
			return this.userName;
		}
		public String getRecipient(){
			return this.recipient;
		}
		public InetAddress getIP(){
			return userIP;
		}
		public int getPortNo(){
			return portNo;
		}
        	@Override
        	public void run(){
                	try{
                        	while(true){
                                	byte [] dataToReceive = new byte[2048];
                                	DatagramPacket receivePacket = new DatagramPacket(dataToReceive,dataToReceive.length);
                                	clientSocket.receive(receivePacket);
                                
					String [] packetData = new String(receivePacket.getData()).split("\n");
                                        String message = packetData[0];
                                        String sender = packetData[1];

					//TODO write a method that writes the message to a file in a sub directory for later reteival
					//arguments message sender recipient 
					
					message.trim();

					DateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
                        
					Date localDate = new Date();

					if(recipient.equals("GROUP")){
						broadcastMessage(message,sender);
						showMessage("[" + dateTimeFormat.format(localDate) + "] " + " [" + sender + " to GROUP]: " + message);
						//call the  method in here
					}
					else{
						sendMessage(message,sender,recipient);
						sendMessage(message,sender,sender);
						showMessage("[" + dateTimeFormat.format(localDate) + "] " + " [" + sender + "]: " + message);
						//and in here
					}
                        	}
                	}catch(Exception e){
                        	e.printStackTrace();
                	}
        	}
	}
	
	
}
