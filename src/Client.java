import java.io.*;
import java.util.Scanner;
import java.util.Date;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client extends JFrame{
        public JTextField userText;
        private JTextArea chatWindow;
        private JPanel contentPane;
	private JPanel headerBanner;
	private JLabel bannerLabel;
	private JPanel inputHeader;
	private JLabel portLabel;
	private JLabel nameLabel;
	private JPanel panelSouth;
	private JButton sendButton;
	public JButton loginButton;
	public JTextField userName;

	private DatagramSocket clientSocket;
	private DatagramSocket loginSocket;
	private String serverIP;
	private int i = 8;

        //Constructor
        public Client(String host){
                super("UCT Messenger Client");
		this.serverIP = host;
                //this.userText = new JTextField();
                
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 570, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(new Color(200,200,0));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		
		headerBanner = new JPanel();
		contentPane.add(headerBanner, BorderLayout.NORTH);
		headerBanner.setLayout(new BorderLayout(0, 0));
		headerBanner.setBackground(new Color(200,200,0));

		bannerLabel = new JLabel("CHAT CLIENT");
		bannerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		bannerLabel.setFont(new Font("Tahoma", Font.PLAIN, 40));
		headerBanner.add(bannerLabel, BorderLayout.NORTH);

		inputHeader = new JPanel();
		headerBanner.add(inputHeader, BorderLayout.SOUTH);
		inputHeader.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		nameLabel = new JLabel("username");
		inputHeader.add(nameLabel);
		inputHeader.setBackground(new Color(135,135,135));

		userName = new JTextField();
		userName.setColumns(10);
		inputHeader.add(userName);

		loginButton = new JButton("Login");
		inputHeader.add(loginButton);
		loginButton.setFont(new Font("Tahoma", Font.PLAIN, 12));


                JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane,BorderLayout.CENTER);

		chatWindow = new JTextArea();
		chatWindow.setBackground(Color.BLACK);
		chatWindow.setForeground(Color.WHITE);
		chatWindow.setLineWrap(true);
		scrollPane.setViewportView(chatWindow);

		panelSouth = new JPanel();
		panelSouth.setBackground(new Color(135,135,135));
		FlowLayout fl_panelSouth = (FlowLayout) panelSouth.getLayout();
		fl_panelSouth.setAlignment(FlowLayout.RIGHT);
		contentPane.add(panelSouth, BorderLayout.SOUTH);


		userText = new JTextField();
		panelSouth.add(userText);
		userText.setColumns(35);
		userText.setEditable(false);

		sendButton = new JButton("SEND");
		sendButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panelSouth.add(sendButton);

		
		
		
		userText.addActionListener(
                        new ActionListener(){
                                public void actionPerformed(ActionEvent event){
					sendMessage(event.getActionCommand());	
                                        userText.setText("");
                                }
                        } 
                );
                setVisible(true);
        }
	//setup and run the server, this will be called after GUI is setup
        public void startRunning()throws IOException{
                
		clientSocket = new DatagramSocket();
		loginSocket = new DatagramSocket();
		Scanner scan = new Scanner(System.in);
                while(true){
                }
        }
	//this method sends a message to connected clients
        public void sendMessage(String message){
		try
		{
			byte [] dataToSend = message.getBytes();
			int dataLength = dataToSend.length;
			InetAddress IP = InetAddress.getLocalHost();
			int portNo = 9999;
		
			DatagramPacket sendPacket = new DatagramPacket(dataToSend,dataLength,IP,portNo);
		
			clientSocket.send(sendPacket);
			showMessage(userName.getText() + " : " + message);
		}catch(IOException e){
			chatWindow.append("There was a problem sending the message");
		}
        }
	public void login(String name){
		String loginStr = name;			//userName + port number
		try{
			byte [] loginData = loginStr.getBytes();
			int dataLength = loginData.length;
			InetAddress IP = InetAddress.getLocalHost();
			int portNo = 1776;

			DatagramPacket loginPacket = new DatagramPacket(loginData,dataLength,IP,portNo);
			loginSocket.send(loginPacket);
			showMessage("Welcome to the chat " + name);
		}catch(IOException e){
			e.printStackTrace();
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


