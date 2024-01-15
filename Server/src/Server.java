//JavaObjServer.java ObjectStream 기반 채팅 Server

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import java.awt.Image;

public class Server extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	JTextArea textArea;
	private JTextField txtPortNumber;

	private ServerSocket socket; // 서버소켓
	private Socket client_socket; // accept() 에서 생성된 client 소켓
	private Vector UserVec = new Vector(); // 연결된 사용자를 저장할 벡터
	private Vector RoomVec = new Vector();
	private Vector RoomIdVec = new Vector();
	private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
	public int RoomNumber = 0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Server frame = new Server();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Server() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 338, 440);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 10, 300, 298);
		contentPane.add(scrollPane);

		textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);

		JLabel lblNewLabel = new JLabel("Port Number");
		lblNewLabel.setBounds(13, 318, 87, 26);
		contentPane.add(lblNewLabel);

		txtPortNumber = new JTextField();
		txtPortNumber.setHorizontalAlignment(SwingConstants.CENTER);
		txtPortNumber.setText("30000");
		txtPortNumber.setBounds(112, 318, 199, 26);
		contentPane.add(txtPortNumber);
		txtPortNumber.setColumns(10);

		JButton btnServerStart = new JButton("Server Start");
		btnServerStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					socket = new ServerSocket(Integer.parseInt(txtPortNumber.getText()));
				} catch (NumberFormatException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				AppendText("Chat Server Running..");
				btnServerStart.setText("Chat Server Running..");
				btnServerStart.setEnabled(false); // 서버를 더이상 실행시키지 못 하게 막는다
				txtPortNumber.setEnabled(false); // 더이상 포트번호 수정못 하게 막는다
				AcceptServer accept_server = new AcceptServer();
				accept_server.start();
			}
		});
		btnServerStart.setBounds(12, 356, 300, 35);
		contentPane.add(btnServerStart);
	}

	// 새로운 참가자 accept() 하고 user thread를 새로 생성한다.
	class AcceptServer extends Thread {
		@SuppressWarnings("unchecked")
		public void run() {
			while (true) { // 사용자 접속을 계속해서 받기 위해 while문
				try {
					AppendText("Waiting new clients ...");
					client_socket = socket.accept(); // accept가 일어나기 전까지는 무한 대기중
					AppendText("새로운 참가자 from " + client_socket);
					// User 당 하나씩 Thread 생성
					UserService new_user = new UserService(client_socket);
					UserVec.add(new_user); // 새로운 참가자 배열에 추가
					new_user.start(); // 만든 객체의 스레드 실행
					AppendText("현재 참가자 수 " + UserVec.size());
				} catch (IOException e) {
					AppendText("accept() error");
					// System.exit(0);
				}
			}
		}
	}

	public void AppendText(String str) {
		// textArea.append("사용자로부터 들어온 메세지 : " + str+"\n");
		textArea.append(str + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}

	public void AppendObject(ChatMsg msg) {
		// textArea.append("사용자로부터 들어온 object : " + str+"\n");
		textArea.append("code = " + msg.getCode() + "\n");
		textArea.append("id = " + msg.getId() + "\n");
		textArea.append("data = " + msg.getData() + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}

	// User 당 생성되는 Thread
	// Read One 에서 대기 -> Write All
	class UserService extends Thread {

		private ObjectInputStream ois;
		private ObjectOutputStream oos;

		private Socket client_socket;
		private Vector user_vc;
		public String  UserName= "";
		public String NowDate = "";
		public ImageIcon UserProfile;
		public String UserList;
		public String[] inviteUserlist;
		private String room_id;
		public ImageIcon Emoticon;
		public ImageIcon Img;

		public UserService(Socket client_socket) {
			// TODO Auto-generated constructor stub
			// 매개변수로 넘어온 자료 저장
			this.client_socket = client_socket;
			this.user_vc = UserVec;
			try {
				oos = new ObjectOutputStream(client_socket.getOutputStream());
				oos.flush();
				ois = new ObjectInputStream(client_socket.getInputStream());

			} catch (Exception e) {
				AppendText("userService error");
			}
		}

		public void Login() {
			AppendText("새로운 참가자 " + UserName + " 입장.");
			ChatMsg ob = new ChatMsg(UserName, "150", "NewUser");
			ob.setProfile(UserProfile);
			WriteOthersObject(ob);
			
			for(int i=0; i<user_vc.size()-1; i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				ChatMsg ob2 = new ChatMsg(user.UserName, "100", "OriginUser");
				ob2.setProfile(user.UserProfile);
				WriteOneObject(ob2);
			}
		}

		public void Logout() {
			ChatMsg ob = new ChatMsg(UserName, "900", "Logout");
			ob.setProfile(UserProfile);
			UserVec.removeElement(this); // Logout한 현재 객체를 벡터에서 지운다
			WriteOthersObject(ob); // 나를 제외한 다른 User들에게 전송
			AppendText("사용자 " + "[" + UserName + "] 퇴장. 현재 참가자 수 " + UserVec.size());
		}
		
		public void ChangeProfile() {
			AppendText("[" + UserName + "] 프로필 변경.");
			String msg = "프로필 변경";
			ChatMsg ob = new ChatMsg(UserName, "800", msg);
			ob.setProfile(UserProfile);
			WriteOthersObject(ob);
		}
		
		public void CreateChatRoom() {
			RoomNumber++;
			AppendText("채팅방 생성. 방 번호 : " + RoomNumber +", 참가자 : " + UserList);
			ChatRoom new_room = new ChatRoom(String.valueOf(RoomNumber), UserList);
			RoomVec.add(new_room);
			RoomIdVec.add(String.valueOf(RoomNumber));

			for(int i=0; i<user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				for(int j=0; j<inviteUserlist.length; j++) {
					if(user.UserName.matches(inviteUserlist[j])) {
						ChatMsg ob = new ChatMsg("Server", "500", "채팅방 생성");
						ob.setUserlist(UserList);
						ob.setRoom_id(String.valueOf(RoomNumber));
						user.WriteOneObject(ob);
					}
				}
			}
		}
		
		public void ClickChatRoom() {
			AppendText("채팅방 클릭. 방 번호 : " + RoomNumber);
			for(int i=0; i<RoomVec.size(); i++) {
				if(((String) RoomIdVec.get(i)).matches(room_id)) {
					ChatMsg ob = new ChatMsg("Server", "550", "채팅방 클릭");
					ob.setRoom_id(room_id);
					ob.setUserlist(UserList);
					WriteOneObject(ob);
				}
			}
		}
		
		public void SendChat(String msg) { // 메세지 전송
			AppendText("채팅 메세지 전송. 방 번호 : " + RoomNumber + "채팅 메세지 : " + msg);
			for(int i=0; i<RoomVec.size(); i++) {
				if(((String) RoomIdVec.get(i)).matches(room_id)) {
					ChatMsg ob = new ChatMsg(UserName, "200", msg); //다른 사람들에게 전송
					ob.setRoom_id(room_id);
					ob.setProfile(UserProfile);
					ob.setNowDate(NowDate);
					ob.setUserlist(UserList);
					WriteAllObject(ob);
				}
			}
		}
		
		public void SendImage(String msg) { // 이미지 전송
			int width, height;
			double ratio;
			
			width = Img.getIconWidth();
			height = Img.getIconHeight();
			Image ori_img = Img.getImage();
			
			if (width > 200 || height > 200) {
				if (width > height) { // 가로 사진
					ratio = (double) height / width;
					width = 200;
					height = (int) (width * ratio);
				} else { // 세로 사진
					ratio = (double) width / height;
					height = 200;
					width = (int) (height * ratio);
				}
			}
			Image new_img = ori_img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			ImageIcon new_icon = new ImageIcon(new_img);
			ImageIcon ori_icon = new ImageIcon(ori_img);
			
			AppendText("이미지 전송. 방 번호 : " + RoomNumber);
			for(int i=0; i<RoomVec.size(); i++) {
				if(((String) RoomIdVec.get(i)).matches(room_id)) {
					ChatMsg ob = new ChatMsg(UserName, "300", msg); 
					ob.setRoom_id(room_id);
					ob.setNowDate(NowDate);
					ob.setProfile(UserProfile);
					ob.setUserlist(UserList);
					ob.setEmoticon(Emoticon);
					ob.setImg(new_icon);
					ob.setOri_img(ori_icon);
					WriteAllObject(ob);
				}
			}
		}
		
		public void SendEmoticon(String msg) { // 이모티콘 전송
			AppendText("이모티콘 전송. 방 번호 : " + RoomNumber + " 이모티콘 : " + msg);
			for(int i=0; i<RoomVec.size(); i++) {
				if(((String) RoomIdVec.get(i)).matches(room_id)) {
					ChatMsg ob = new ChatMsg(UserName, "400", msg); 
					ob.setRoom_id(room_id);
					ob.setProfile(UserProfile);
					ob.setUserlist(UserList);
					ob.setEmoticon(Emoticon);
					ob.setNowDate(NowDate);
					WriteAllObject(ob);
				}
			}
		}
		
		public void WriteAllObject(Object ob) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				user.WriteOneObject(ob);
			}
		}
		
		public void WriteOthersObject(Object ob) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				if (user != this)
					user.WriteOneObject(ob);
			}
		}

		public void WriteOneObject(Object ob) {
			try {
			    oos.writeObject(ob);
			} 
			catch (IOException e) {
				AppendText("oos.writeObject(ob) error");		
				try {
					ois.close();
					oos.close();
					client_socket.close();
					client_socket = null;
					ois = null;
					oos = null;				
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Logout();
			}
		}
		
		public void run() {
			while (true) { // 사용자 접속을 계속해서 받기 위해 while문
				try {
					Object obcm = null;
					String msg = null;
					ChatMsg cm = null;
					if (socket == null)
						break;
					try {
						obcm = ois.readObject();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return;
					}
					if (obcm == null)
						break;
					if (obcm instanceof ChatMsg) {
						cm = (ChatMsg) obcm;
						AppendObject(cm);
					} else
						continue;
					if (cm.getCode().matches("100")) {
						UserName = cm.getId();
						UserProfile = cm.getProfile();
						Login();
					}
					if (cm.getCode().matches("200")) {
						UserList = cm.getUserlist();
						UserName = cm.getId();
						UserProfile = cm.getProfile();
						NowDate = cm.getNowDate();
						room_id=cm.getRoom_id();
						msg = cm.getData();
						SendChat(msg);
					}
					
					else if (cm.getCode().matches("300")) { // 이미지
						UserList = cm.getUserlist();
						UserName = cm.getId();
						UserProfile = cm.getProfile();
						NowDate = cm.getNowDate();
						room_id=cm.getRoom_id();
						Img=cm.getImg();
						msg = cm.getData();
						SendImage(msg);
					}
					else if (cm.getCode().matches("400")) { // 이모티콘
						UserList = cm.getUserlist();
						UserName = cm.getId();
						UserProfile = cm.getProfile();
						room_id=cm.getRoom_id();
						Emoticon = cm.getEmoticon();
						NowDate = cm.getNowDate();
						msg = cm.getData();
						SendEmoticon(msg);
						
					}
					
					else if (cm.getCode().matches("500")) { // 채팅방 생성
						UserList = cm.getUserlist();
						inviteUserlist = UserList.split(", ");
						CreateChatRoom();
					}
					else if (cm.getCode().matches("550")) { // 채팅방 클릭
						room_id = cm.getRoom_id();
						UserList = cm.getUserlist();
						ClickChatRoom();
					}
					else if (cm.getCode().matches("800")) { // 프로필 변경
						UserProfile = cm.getProfile();
						ChangeProfile();
					}
					else if (cm.getCode().matches("900")) { // logout message 처리
						Logout();
						break;
					}
				}
				catch (IOException e) {
				AppendText("ois.readObject() error");
				try {
						ois.close();
						oos.close();
						client_socket.close();
						Logout(); // 에러가난 현재 객체를 벡터에서 지운다
						break;
					} catch (Exception ee) {
						break;
					} // catch문 끝
				} // 바깥 catch문끝
			} // while
		} // run
	}
}