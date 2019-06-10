package server;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.event.*;

import javax.swing.*;

public class Server extends JFrame {

	JTextArea textShow;
	JButton start;
	Vector socketsss = new Vector();//这里用到了变长对象数组，用来存储来自客户端的socket对象
	ServerSocket server = null;
	Socket clients;

	Server() { // 服务器的构造函数，并且初始化
		init();
		setVisible(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(450, 150, 340, 455);
		setTitle("123chat server");
		setResizable(false);
	}

	void init() { // 设置布局和事件监视器
		setLayout(new FlowLayout());
		getContentPane().setBackground(Color.yellow);
		textShow = new JTextArea(21, 29);
		textShow.setBackground(Color.white );
		start = new JButton(" Begin the server ");
		start.setBackground(Color.red);
		add(start);
		add(new JScrollPane(textShow));
		textShow.setEditable(false);
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {   //在这里启动监听的线程
				Listen listen = new Listen();
				Thread go = new Thread(listen);
				go.start();
			}
		});

		addWindowListener(new WindowAdapter() { // 响应关闭按钮功能
			public void windowClosing(WindowEvent e) {
				int option = JOptionPane
						.showConfirmDialog(null, "leave?",
								" Hi", JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE);
				if (option == JOptionPane.YES_OPTION)
					System.exit(0);

			}
		});

	} // init()结束

	class ServerThread extends Thread { // 服务器消息处理的线程
		Socket socket;
		DataOutputStream out = null;
		DataInputStream in = null;
		String s = null;
		Vector sockets = new Vector();
		int j = 0;

		ServerThread(Socket t, Vector socketss) {
			socket = t;
			sockets = socketss;
			try { 
				in = new DataInputStream(socket.getInputStream());
			} catch (IOException e) {
			}
		}

		public void run() {
			while (true) {
				try {
					String r = in.readUTF();// 堵塞状态，除非读取到信息
					for (int j = 0; j < sockets.size(); j++) {
						out = new DataOutputStream(
								((Socket) sockets.get(j)).getOutputStream()); // 对于每个数组内的socket对象都建立输出流
						out.writeUTF(r);
					}
				} catch (IOException e) {
					textShow.append("someone leave\n");
					return;
				}
			}
		}
	}

	class Listen implements Runnable { // 服务器监听线程
		ServerSocket server;
		Socket clients;

		public void run() {
			while (true) {
				try {
					server = new ServerSocket(8888);
					textShow.append(new java.text.SimpleDateFormat(
							"yy-MM-dd HH:mm:ss").format(new Date())
							+ "服务器已开启\n");
				} catch (IOException e1) {
					textShow.append("正在监听\n"); // ServerSocket对象不能重复创建
				}
				try {
					textShow.append(new java.text.SimpleDateFormat(
							"yy-MM-dd HH:mm:ss").format(new Date())
							+ " 等待用户连接......\n");

					clients = server.accept();
					socketsss.add(clients);
					ServerThread handlers = new ServerThread(clients, socketsss);
					handlers.start(); // 为每个用户创建单独的消息处理线程
					textShow.append(new java.text.SimpleDateFormat(
							"yy-MM-dd HH:mm:ss").format(new Date())
							+ "有用户连接,用户的地址:" + clients.getInetAddress() + "\n");
				} catch (IOException e1) {
					textShow.append(new java.text.SimpleDateFormat(
							"yy-MM-dd HH:mm:ss").format(new Date())
							+ "正在等待逗比来临......\n");
				}
			}
		}
	}

	public static void main(String args[]) {
		Server server = new Server();
	}
}