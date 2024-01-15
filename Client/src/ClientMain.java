import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.KeyStore.Entry;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import javax.swing.ScrollPaneConstants;

public class ClientMain extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panel;
	private JButton btnUserList;
	private JButton btnChatList;
	private JButton btnAddChat;
	private JPanel titlePanel;
	private JLabel titleLabel;
	private JPanel clientPanel;
	private JLabel lblClientName;
	private JScrollPane scrollPaneFriendList;
	private JTextPane textPaneFriendList;
	private JScrollPane scrollPaneChatList;
	private JTextPane textPaneChatList;
	private JButton btnCleintProfile;
	
	private ImageIcon friendIcon = new ImageIcon("./icon/friend.png");
	private ImageIcon chatIcon = new ImageIcon("./icon/chat.png");
	private ImageIcon addChatIcon = new ImageIcon("./icon/addChat_icon.png");
	
	private String UserName;
	private ImageIcon Profile;
	private Frame frame;
	private FileDialog fd;
	
	private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
	private Socket socket; // 연결소켓

	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	ArrayList<FriendPanel> friends = new ArrayList<>();
	ArrayList<String> friendsName = new ArrayList<>(); // 프로필 변경, 로그아웃을 위해서 user들의 name을 저장해둠
	ArrayList<ImageIcon> friendsProfile = new ArrayList<>();
	private Vector ChatRoomVec = new Vector();
	private Vector RoomVec = new Vector();
	private Vector RoomIdVec = new Vector();
	public TitledBorder oneTb = new TitledBorder(new LineBorder(new Color(230,230,230))); //테두리
	
	private ImageIcon 신남 = new ImageIcon("./emoticon/신남.png");
	private ImageIcon 안녕 = new ImageIcon("./emoticon/안녕.png");
	private ImageIcon 안돼 = new ImageIcon("./emoticon/안돼.png");
	private ImageIcon 엄지척 = new ImageIcon("./emoticon/엄지척.png");
	private ImageIcon 집중 = new ImageIcon("./emoticon/집중.png");
	private ImageIcon 축하 = new ImageIcon("./emoticon/축하.png");
	private ImageIcon 하트뿅 = new ImageIcon("./emoticon/하트뿅.png");
	private ImageIcon 하하 = new ImageIcon("./emoticon/하하.png");
	private ImageIcon 히히 = new ImageIcon("./emoticon/히히.png");
	
	private HashMap<String, ImageIcon> emoticonHash = new HashMap<String, ImageIcon>(){{
		put("신남", 신남);
		put("안녕", 안녕);
		put("안돼", 안돼);
		put("엄지척", 엄지척);
		put("집중", 집중);
		put("축하", 축하);
		put("하트뿅", 하트뿅);
		put("하하", 하하);
		put("히히", 히히);
	}};
	
	public ClientMain(String username, String ip_addr, String port_no, ImageIcon profile) {
		
		//윈도우창의 상단메뉴 해제
		//setUndecorated(true);
		UserName = username;
		Profile = profile;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setResizable(false);
		
		setBounds(100, 100, 390, 568);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		panel = new JPanel();
		panel.setBounds(0, 0, 65, 529);
		panel.setLayout(null);
		contentPane.add(panel);
		
		btnChatList = new JButton();
		btnChatList.setIcon(chatIcon);
		btnChatList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				titleLabel.setText("채팅");
				btnAddChat.setVisible(true);
				clientPanel.setVisible(false);
				scrollPaneFriendList.setVisible(false);
				scrollPaneChatList.setVisible(true);
			}
		});
		btnChatList.setBounds(0, 106, 65, 65);
		btnChatList.setContentAreaFilled(false);
		btnChatList.setFocusPainted(false);
		btnChatList.setBorderPainted(false);
		panel.add(btnChatList);
			
		btnUserList = new JButton();
		btnUserList.setIcon(friendIcon);
		btnUserList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				titleLabel.setText("친구");
				clientPanel.setVisible(true);
				scrollPaneFriendList.setVisible(true);
				scrollPaneChatList.setVisible(false);
				btnAddChat.setVisible(false);
			}
		});
		btnUserList.setBounds(0, 28, 65, 65);
		btnUserList.setContentAreaFilled(false);
		btnUserList.setFocusPainted(false);
		btnUserList.setBorderPainted(false);
		panel.add(btnUserList);
		
		titlePanel = new JPanel();
		titlePanel.setBackground(new Color(255, 255, 255));
		titlePanel.setBounds(65, 0, 309, 63);
		contentPane.add(titlePanel);
		titlePanel.setLayout(null);
		
		btnAddChat = new JButton(addChatIcon); // 채팅 추가 버튼
		btnAddChat.setVisible(false);
		btnAddChat.setContentAreaFilled(false);
		btnAddChat.setFocusPainted(false);
		btnAddChat.setBorderPainted(false);
		btnAddChat.setBackground(new Color(255, 255, 255));
		btnAddChat.setBounds(257, 20, 40, 40);
		titlePanel.add(btnAddChat);
		btnAddChat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new InviteFriend(friendsName);
			}
		});
		
		titleLabel = new JLabel("친구");
		titleLabel.setBounds(12, 20, 295, 43);
		titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		titlePanel.add(titleLabel);
		
		clientPanel = new JPanel();
		clientPanel.setBackground(new Color(255, 255, 255));
		clientPanel.setBounds(63, 62, 312, 75);
		contentPane.add(clientPanel);
		clientPanel.setLayout(null);
		clientPanel.setBorder(oneTb);
		
		lblClientName = new JLabel(UserName);
		lblClientName.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		lblClientName.setBackground(new Color(240, 240, 240));
		lblClientName.setBounds(83, 0, 226, 75);
		clientPanel.add(lblClientName);
		
		btnCleintProfile = new JButton(Profile);
		btnCleintProfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = lblClientName.getText();
				 frame = new Frame("프로필 변경");
					fd = new FileDialog(frame, "이미지선택", FileDialog.LOAD);
					fd.setVisible(true);
					
					ImageIcon ori_icon = new ImageIcon(fd.getDirectory() + fd.getFile());
					Image ori_img = ori_icon.getImage();
					
					int width,height;
					width = ori_icon.getIconWidth();
					height = ori_icon.getIconHeight();
					
					if(width > 65 || height > 65) { // 내 창에서 보일 프로필 사진
						width = 65;
						height = 65;
					}
					Image new_img = ori_img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
					ImageIcon new_profile = new ImageIcon(new_img);
					btnCleintProfile.setIcon(new_profile);
					Profile = new_profile;
					ChatMsg obcm = new ChatMsg(UserName,"800", "Profile_IMG");
					obcm.setProfile(new_profile);
					SendObject(obcm);
			}
		});
		btnCleintProfile.setContentAreaFilled(false);
		btnCleintProfile.setFocusPainted(false);
		btnCleintProfile.setBorderPainted(false);
		btnCleintProfile.setBounds(6, 5, 65, 65);
		clientPanel.add(btnCleintProfile);
		
		scrollPaneFriendList = new JScrollPane();
		scrollPaneFriendList.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
		scrollPaneFriendList.setBorder(null);

		scrollPaneFriendList.setBounds(65, 137, 309, 392);
		contentPane.add(scrollPaneFriendList);
		
		textPaneFriendList = new JTextPane();
		textPaneFriendList.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		textPaneFriendList.setEditable(false);
		textPaneFriendList.setBackground(new Color(255, 255, 255));
		scrollPaneFriendList.setViewportView(textPaneFriendList);
		
		scrollPaneChatList = new JScrollPane();
		scrollPaneChatList.setBorder(null);
	
		scrollPaneChatList.setBounds(65, 63, 309, 467);

		contentPane.add(scrollPaneChatList);
		
		textPaneChatList = new JTextPane();
		textPaneChatList.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		textPaneChatList.setEditable(false);
		textPaneChatList.setBounds(0, 0, 10, 10);
		scrollPaneChatList.setViewportView(textPaneChatList);
		
		
		try {
			socket = new Socket(ip_addr, Integer.parseInt(port_no));
			
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(socket.getInputStream());
			
			ChatMsg obcm = new ChatMsg(UserName, "100", "Login");
			obcm.setProfile(Profile);
			SendObject(obcm);
			
			ListenNetwork net = new ListenNetwork();
			net.start();
			
		} catch(NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	
	// 채팅초대창
	class InviteFriend extends JFrame { 
		private JPanel InvitecontentPane;
		private JTextPane InviteFriendList;
		private JScrollPane InviteScrollPane;
		private JButton btn;
		private JCheckBox[] checkbox = new JCheckBox[friendsName.size()];
			
		MyItemListener listener = new MyItemListener();
		ArrayList<String> inviteList = new ArrayList<>();
			
		public InviteFriend(ArrayList<String> friendsName) {
			inviteList.add(UserName);
				
			setVisible(true);
			setResizable(false);
				
			setBounds(200,200, 311, 350);
			InvitecontentPane = new JPanel();
			InvitecontentPane.setBackground(new Color(255, 255, 255));
			setContentPane(InvitecontentPane);
			InvitecontentPane.setLayout(null);
				
			InviteScrollPane = new JScrollPane();
			InviteScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
			InviteScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			InviteScrollPane.setBounds(0, 0, 295, 263);
			InviteScrollPane.setBorder(null);
			InvitecontentPane.add(InviteScrollPane);
				
			InviteFriendList = new JTextPane();
			InviteFriendList.setEditable(false);
			InviteScrollPane.setViewportView(InviteFriendList);
				
			btn = new JButton("확인");
			btn.setFont(new Font("맑은 고딕", Font.BOLD, 12));
			btn.setForeground(new Color(255, 255, 255));
			btn.setBorderPainted(false);
			btn.setBackground(new Color(58, 29, 29));
			btn.setBounds(118, 273, 58, 23);
			InvitecontentPane.add(btn);
			btn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// 서버한테 채팅 참기 리스트 보내기
					String userlist = String.join(", ",inviteList);
					ChatMsg obcm = new ChatMsg(UserName,"500", "채팅방 생성");
					obcm.setUserlist(userlist);
					SendObject(obcm);
					setVisible(false);
				}
			});
				
			for(int i = 0; i < checkbox.length; i++) {
				checkbox[i] = new JCheckBox(friendsName.get(i));
				checkbox[i].setBackground(null);
				checkbox[i].setFont(new Font("맑은 고딕", Font.BOLD, 14));
				JPanel cbPanel = new JPanel();
				cbPanel.setBorder(oneTb);
				cbPanel.setBackground(null);
				cbPanel.add(checkbox[i]);
				cbPanel.setPreferredSize(new Dimension(300,40));
				//checkbox[i].setPreferredSize(new Dimension(265,15));
				InviteFriendList.insertComponent(cbPanel);
				checkbox[i].addItemListener(listener);
			}
				
		}
			
		class MyItemListener implements ItemListener {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					// 체크되면 arraylist에 저장
					for(int i = 0; i < checkbox.length; i++) {
						if(e.getItem() == checkbox[i])
							inviteList.add(checkbox[i].getText());
					}
					//System.out.println("체크 : " + inviteList);
				} else {
					// 체크 해제되면 arraylist에서 삭제
					for(int i = 0; i < checkbox.length; i++) {
						if(e.getItem() == checkbox[i])
							inviteList.remove(inviteList.indexOf(checkbox[i].getText()));
					}
					//System.out.println("체크해제 : " + inviteList);
				}
			}
		}
			
	}
	
	// 채팅방
	class ChatRoom extends JFrame {
		private JPanel contentPane;
		private JTextField textFieldMsg;
		public JButton btnSendMsg;
		private JButton btnEmoticon;
		private JTextPane textPaneMsg;
		private JPanel panelUserlist;
		private JLabel lblUserList;
		private JScrollPane scrollPaneMsg;
		
		private ImageIcon happy = new ImageIcon("./icon/happy.png");
		private ImageIcon attachments = new ImageIcon("./icon/attachment.png");
		private JButton btnImage;
		
		// 채팅방 참가 유저리스트, 채팅방 번호
		public ChatRoom(String room_id, String userlist) {
			setResizable(false);
			setVisible(false);
			setBounds(100, 100, 390, 644);
			contentPane = new JPanel();
			contentPane.setBackground(new Color(255, 255, 255));
			setContentPane(contentPane);
			contentPane.setLayout(null);
			
			scrollPaneMsg = new JScrollPane();
			scrollPaneMsg.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			scrollPaneMsg.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
			scrollPaneMsg.setBounds(0, 64, 374, 462);
			scrollPaneMsg.setBorder(null);
			contentPane.add(scrollPaneMsg);
			
			textPaneMsg = new JTextPane();
			textPaneMsg.setBackground(new Color(155, 187, 212));
			textPaneMsg.setEditable(false);
			scrollPaneMsg.setViewportView(textPaneMsg);
			
			textFieldMsg = new JTextField();
			textFieldMsg.setHorizontalAlignment(SwingConstants.LEFT);
			textFieldMsg.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			textFieldMsg.setBounds(10, 525, 374, 42);
			textFieldMsg.setBorder(null);
			contentPane.add(textFieldMsg);
			textFieldMsg.setColumns(10);
			
			btnEmoticon = new JButton(happy);
			btnEmoticon.setContentAreaFilled(false);
			btnEmoticon.setFocusPainted(false);
			btnEmoticon.setBorderPainted(false);
			btnEmoticon.setBounds(0, 570, 35, 35);
			contentPane.add(btnEmoticon);
			btnEmoticon.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					new Emoticon(room_id, userlist);
				}
			});
			
			btnSendMsg = new JButton("전송");
			btnSendMsg.setFont(new Font("맑은 고딕", Font.BOLD, 11));
			btnSendMsg.setForeground(new Color(0, 0, 0));
			btnSendMsg.setBorderPainted(false);
			btnSendMsg.setBackground(new Color(254, 247, 27));
			btnSendMsg.setBounds(305, 566, 60, 30);
			contentPane.add(btnSendMsg);
			btnSendMsg.addActionListener(new ActionListener() { // 메세지 전송 버튼
				public void actionPerformed(ActionEvent e) {
					if(e.getSource() == btnSendMsg || e.getSource() == textFieldMsg) {
						Date date = new Date();
						SimpleDateFormat formatType = new SimpleDateFormat("a HH:mm");
						String nowDate = formatType.format(date).toString();
						
						String msg = null;
						msg = textFieldMsg.getText();
						ChatMsg obcm = new ChatMsg(UserName ,"200", msg);
						obcm.setNowDate(nowDate);
						obcm.setRoom_id(room_id);
						obcm.setProfile(Profile);
						obcm.setUserlist(userlist);
						SendObject(obcm);
						textFieldMsg.setText("");
						textFieldMsg.requestFocus();
					}
				}
			});
			
			panelUserlist = new JPanel();
			panelUserlist.setBackground(new Color(147, 177, 205));
			panelUserlist.setBounds(0, 0, 374, 65);
			contentPane.add(panelUserlist);
			panelUserlist.setLayout(null);
			
			lblUserList = new JLabel();
			System.out.println(userlist);
			lblUserList.setText(userlist);
			lblUserList.setBounds(12, 0, 362, 65);
			lblUserList.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
			panelUserlist.add(lblUserList);
			
			btnImage = new JButton(attachments);
			btnImage.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					frame = new Frame("이미지 전송");
					fd = new FileDialog(frame, "이미지선택", FileDialog.LOAD);
					fd.setVisible(true);
					
					ImageIcon ori_img = new ImageIcon(fd.getDirectory() + fd.getFile());
					
					Date date = new Date();
					SimpleDateFormat formatType = new SimpleDateFormat("a HH:mm");
					String nowDate = formatType.format(date).toString();
					
					ChatMsg obcm = new ChatMsg(UserName,"300", "(사진)");
					obcm.setNowDate(nowDate);
					obcm.setRoom_id(room_id);
					obcm.setProfile(Profile);
					obcm.setUserlist(userlist);
					obcm.setImg(ori_img);
					SendObject(obcm);
				}
			});
			btnImage.setFocusPainted(false);
			btnImage.setContentAreaFilled(false);
			btnImage.setBorderPainted(false);
			btnImage.setBounds(35, 570, 35, 35);
			contentPane.add(btnImage);
		}
		
		public void getChatRoom() {
			setVisible(true);
			setBounds(100, 100, 390, 644);
		}
		public JTextPane getTextPane() {
			return textPaneMsg;
		}
	}
	
	//채팅목록에 뜨는 채팅방
	public class RoomPanel extends JPanel {
		private JButton btnRoomName;
		private JLabel lbFriendsProfile;
		private String RoomId;
		private JLabel lbLastMsg;
		private JLabel lbTime;

		public RoomPanel(String UserName, String RoomId, String RoomName, Vector<ImageIcon> friendsProfileList) {

			this.RoomId = RoomId;
			setBackground(new Color(255, 255, 255));
			setLayout(null);
			
			btnRoomName = new JButton(RoomName);
			btnRoomName.setFont(new Font("맑은 고딕", Font.BOLD, 12));
			btnRoomName.setBounds(65, 7, 170, 30);
			btnRoomName.setContentAreaFilled(false);
			btnRoomName.setFocusPainted(false);
			btnRoomName.setBorderPainted(false);
			btnRoomName.setHorizontalAlignment(SwingConstants.LEFT);
			add(btnRoomName);
			
			lbFriendsProfile = new JLabel(friendsProfileList.get(0));
			lbFriendsProfile.setHorizontalAlignment(JLabel.CENTER);
			lbFriendsProfile.setBounds(6, 5, 65, 65);
			add(lbFriendsProfile);
			
			btnRoomName.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ChatMsg obcm = new ChatMsg(UserName ,"550", "채팅방 생성");
					obcm.setRoom_id(RoomId);
					obcm.setUserlist(RoomName);
					SendObject(obcm);
				}
			});
			
			lbLastMsg = new JLabel();
			lbLastMsg.setBounds(83, 30, 226, 30);
			add(lbLastMsg);
			
			lbTime = new JLabel();
			lbTime.setBounds(238, 9, 100, 30);
			add(lbTime);
		}
		public void setLastMsg(String msg, String time) {
			lbLastMsg.setForeground(Color.GRAY);
			lbLastMsg.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			lbLastMsg.setText(msg);
			lbTime.setForeground(Color.lightGray);
			lbTime.setFont(new Font("맑은 고딕", Font.PLAIN, 10));
			lbTime.setText(time);
			revalidate();
		}
	}
	
	public class RoomPanel2 extends JPanel {
		private JButton btnRoomName;
		private JLabel lbFriendsProfile;
		private JLabel lbFriendsProfile2;
		private String RoomId;
		private JLabel lbLastMsg;
		private JLabel lbTime;

		public RoomPanel2(String UserName, String RoomId, String RoomName, Vector<ImageIcon> friendsProfileList) {

			this.RoomId = RoomId;
			setBackground(new Color(255, 255, 255));
			setLayout(null);
			
			btnRoomName = new JButton(RoomName);
			btnRoomName.setBounds(65, 7, 170, 30);
			btnRoomName.setContentAreaFilled(false);
			btnRoomName.setFocusPainted(false);
			btnRoomName.setBorderPainted(false);
			btnRoomName.setHorizontalAlignment(SwingConstants.LEFT);
			add(btnRoomName);
			
			Image img = friendsProfileList.get(0).getImage();
			Image update = img.getScaledInstance(37, 37, Image.SCALE_SMOOTH);
			ImageIcon profile1 = new ImageIcon(update);
			
			lbFriendsProfile = new JLabel(profile1);
			lbFriendsProfile.setBackground(null);
			lbFriendsProfile.setHorizontalAlignment(JLabel.CENTER);
			lbFriendsProfile.setBounds(0, 5, 37, 37);
			add(lbFriendsProfile);
			
			Image img2 = friendsProfileList.get(1).getImage();
			Image update2 = img2.getScaledInstance(37, 37, Image.SCALE_SMOOTH);
			ImageIcon profile2 = new ImageIcon(update2);
			
			lbFriendsProfile2 = new JLabel(profile2);
			lbFriendsProfile2.setBackground(null);
			lbFriendsProfile2.setHorizontalAlignment(JLabel.CENTER);
			lbFriendsProfile2.setBounds(32, 32, 37, 37);
			add(lbFriendsProfile2);
			
			btnRoomName.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ChatMsg obcm = new ChatMsg(UserName ,"550", "채팅방 생성");
					obcm.setRoom_id(RoomId);
					obcm.setUserlist(RoomName);
					SendObject(obcm);
				}
			});
			lbLastMsg = new JLabel();
			lbLastMsg.setBounds(83, 30, 226, 30);
			add(lbLastMsg);
			
			lbTime = new JLabel();
			lbTime.setBounds(238, 9, 100, 30);
			add(lbTime);
		}
		public void setLastMsg(String msg, String time) {
			lbLastMsg.setForeground(Color.GRAY);
			lbLastMsg.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			lbLastMsg.setText(msg);
			lbTime.setForeground(Color.lightGray);
			lbTime.setFont(new Font("맑은 고딕", Font.PLAIN, 10));
			lbTime.setText(time);
			revalidate();
		}
	}
	
	public class RoomPanel3 extends JPanel {
		private JButton btnRoomName;
		private JLabel lbFriendsProfile;
		private JLabel lbFriendsProfile2;
		private JLabel lbFriendsProfile3;
		private String RoomId;
		private JLabel lbLastMsg;
		private JLabel lbTime;

		public RoomPanel3(String UserName, String RoomId, String RoomName, Vector<ImageIcon> friendsProfileList) {

			this.RoomId = RoomId;
			setBackground(new Color(255, 255, 255));
			setLayout(null);
			
			btnRoomName = new JButton(RoomName);
			btnRoomName.setBounds(65, 7, 170, 30);
			btnRoomName.setContentAreaFilled(false);
			btnRoomName.setFocusPainted(false);
			btnRoomName.setBorderPainted(false);
			btnRoomName.setHorizontalAlignment(SwingConstants.LEFT);
			add(btnRoomName);
			
			Image img = friendsProfileList.get(0).getImage();
			Image update = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
			ImageIcon profile1 = new ImageIcon(update);
			
			lbFriendsProfile = new JLabel(profile1);
			lbFriendsProfile.setHorizontalAlignment(JLabel.CENTER);
			lbFriendsProfile.setBounds(20, 5, 30, 30);
			add(lbFriendsProfile);
			
			Image img2 = friendsProfileList.get(1).getImage();
			Image update2 = img2.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
			ImageIcon profile2 = new ImageIcon(update2);
			
			lbFriendsProfile2 = new JLabel(profile2);
			lbFriendsProfile2.setHorizontalAlignment(JLabel.CENTER);
			lbFriendsProfile2.setBounds(6, 35, 30, 30);
			add(lbFriendsProfile2);
			
			Image img3 = friendsProfileList.get(2).getImage();
			Image update3 = img3.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
			ImageIcon profile3 = new ImageIcon(update3);
			
			lbFriendsProfile3 = new JLabel(profile3);
			lbFriendsProfile3.setHorizontalAlignment(JLabel.CENTER);
			lbFriendsProfile3.setBounds(34, 35, 30, 30);
			add(lbFriendsProfile3);
			
			btnRoomName.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ChatMsg obcm = new ChatMsg(UserName ,"550", "채팅방 생성");
					obcm.setRoom_id(RoomId);
					obcm.setUserlist(RoomName);
					SendObject(obcm);
				}
			});
			lbLastMsg = new JLabel();
			lbLastMsg.setBounds(83, 30, 226, 30);
			add(lbLastMsg);
			
			lbTime = new JLabel();
			lbTime.setBounds(238, 9, 100, 30);
			add(lbTime);
		}
		public void setLastMsg(String msg, String time) {
			lbLastMsg.setForeground(Color.GRAY);
			lbLastMsg.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			lbLastMsg.setText(msg);
			lbTime.setForeground(Color.lightGray);
			lbTime.setFont(new Font("맑은 고딕", Font.PLAIN, 10));
			lbTime.setText(time);
			revalidate();
		}
	}
	
	public class RoomPanel4 extends JPanel {
		private JButton btnRoomName;
		private JLabel lbFriendsProfile;
		private JLabel lbFriendsProfile2;
		private JLabel lbFriendsProfile3;
		private JLabel lbFriendsProfile4;
		private String RoomId;
		private JLabel lbLastMsg;
		private JLabel lbTime;

		public RoomPanel4(String UserName, String RoomId, String RoomName, Vector<ImageIcon> friendsProfileList) {

			this.RoomId = RoomId;
			setBackground(new Color(255, 255, 255));
			setLayout(null);
			
			btnRoomName = new JButton(RoomName);
			btnRoomName.setBounds(65, 7, 170, 30);
			btnRoomName.setContentAreaFilled(false);
			btnRoomName.setFocusPainted(false);
			btnRoomName.setBorderPainted(false);
			btnRoomName.setHorizontalAlignment(SwingConstants.LEFT);
			add(btnRoomName);
			
			Image img = friendsProfileList.get(0).getImage();
			Image update = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
			ImageIcon profile1 = new ImageIcon(update);
			
			lbFriendsProfile = new JLabel(profile1);
			lbFriendsProfile.setHorizontalAlignment(JLabel.CENTER);
			lbFriendsProfile.setBounds(6, 5, 30, 30);
			add(lbFriendsProfile);
			
			Image img2 = friendsProfileList.get(1).getImage();
			Image update2 = img2.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
			ImageIcon profile2 = new ImageIcon(update2);
			
			lbFriendsProfile2 = new JLabel(profile2);
			lbFriendsProfile2.setHorizontalAlignment(JLabel.CENTER);
			lbFriendsProfile2.setBounds(34, 5, 30, 30);
			add(lbFriendsProfile2);
			
			Image img3 = friendsProfileList.get(2).getImage();
			Image update3 = img3.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
			ImageIcon profile3 = new ImageIcon(update3);
			
			lbFriendsProfile3 = new JLabel(profile3);
			lbFriendsProfile3.setHorizontalAlignment(JLabel.CENTER);
			lbFriendsProfile3.setBounds(6, 35, 30, 30);
			add(lbFriendsProfile3);
			
			Image img4 = friendsProfileList.get(3).getImage();
			Image update4 = img4.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
			ImageIcon profile4 = new ImageIcon(update4);
			
			lbFriendsProfile4 = new JLabel(profile4);
			lbFriendsProfile4.setHorizontalAlignment(JLabel.CENTER);
			lbFriendsProfile4.setBounds(34, 35, 30, 30);
			add(lbFriendsProfile4);
			
			btnRoomName.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ChatMsg obcm = new ChatMsg(UserName ,"550", "채팅방 생성");
					obcm.setRoom_id(RoomId);
					obcm.setUserlist(RoomName);
					SendObject(obcm);
				}
			});
			lbLastMsg = new JLabel();
			lbLastMsg.setBounds(83, 30, 226, 30);
			add(lbLastMsg);
			lbTime = new JLabel();
			lbTime.setBounds(238, 9, 100, 30);
			add(lbTime);
		}
		public void setLastMsg(String msg, String time) {
			lbLastMsg.setForeground(Color.GRAY);
			lbLastMsg.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			lbLastMsg.setText(msg);
			lbTime.setForeground(Color.lightGray);
			lbTime.setFont(new Font("맑은 고딕", Font.PLAIN, 10));
			lbTime.setText(time);
			revalidate();
			revalidate();
		}
	}
	
	// 이모티콘
	class Emoticon extends JFrame{
		private JButton btnemoji1;
		private JButton btnemoji2;
		private JButton btnemoji3;
		private JButton btnemoji4;
		private JButton btnemoji5;
		private JButton btnemoji6;
		private JButton btnemoji7;
		private JButton btnemoji8;
		private JButton btnemoji9;
		private JPanel contentPane;
		
		
		public Emoticon(String room_id, String userlist) {
			setResizable(false);
			setVisible(true);
			setBounds(200, 200, 400, 400);
			contentPane = new JPanel();
			contentPane.setBackground(new Color(255, 255, 255));
			setContentPane(contentPane);
			contentPane.setLayout(new GridLayout(3, 3, 0, 0));
			
			Date date = new Date();
			SimpleDateFormat formatType = new SimpleDateFormat("a HH:mm");
			String nowDate = formatType.format(date).toString();
			
			btnemoji1 = new JButton(신남);
			btnemoji1.setFocusPainted(false);
			btnemoji1.setContentAreaFilled(false);
			btnemoji1.setBorderPainted(false);
			contentPane.add(btnemoji1);
			btnemoji1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setVisible(false);
					ChatMsg obcm = new ChatMsg(UserName ,"400", "(신남)");
					obcm.setEmoticon(신남);
					obcm.setRoom_id(room_id);
					obcm.setProfile(Profile);
					obcm.setUserlist(userlist);
					obcm.setNowDate(nowDate);
					SendObject(obcm);
					
				}
			});
			
			btnemoji2 = new JButton(안녕);
			btnemoji2.setFocusPainted(false);
			btnemoji2.setContentAreaFilled(false);
			btnemoji2.setBorderPainted(false);
			contentPane.add(btnemoji2);
			btnemoji2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setVisible(false);
					ChatMsg obcm = new ChatMsg(UserName ,"400", "(안녕)");
					obcm.setEmoticon(안녕);
					obcm.setRoom_id(room_id);
					obcm.setProfile(Profile);
					obcm.setUserlist(userlist);
					obcm.setNowDate(nowDate);
					SendObject(obcm);
					
				}
			});
			
			btnemoji3 = new JButton(안돼);
			btnemoji3.setFocusPainted(false);
			btnemoji3.setContentAreaFilled(false);
			btnemoji3.setBorderPainted(false);
			contentPane.add(btnemoji3);
			btnemoji3.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setVisible(false);
					ChatMsg obcm = new ChatMsg(UserName ,"400", "(안돼)");
					obcm.setEmoticon(안돼);
					obcm.setRoom_id(room_id);
					obcm.setProfile(Profile);
					obcm.setUserlist(userlist);
					obcm.setNowDate(nowDate);
					SendObject(obcm);
					
				}
			});
			
			btnemoji4 = new JButton(엄지척);
			btnemoji4.setFocusPainted(false);
			btnemoji4.setContentAreaFilled(false);
			btnemoji4.setBorderPainted(false);
			contentPane.add(btnemoji4);
			btnemoji4.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setVisible(false);
					ChatMsg obcm = new ChatMsg(UserName ,"400", "(엄지척)");
					obcm.setEmoticon(엄지척);
					obcm.setRoom_id(room_id);
					obcm.setProfile(Profile);
					obcm.setUserlist(userlist);
					obcm.setNowDate(nowDate);
					SendObject(obcm);
					
				}
			});
			
			btnemoji5 = new JButton(집중);
			btnemoji5.setFocusPainted(false);
			btnemoji5.setContentAreaFilled(false);
			btnemoji5.setBorderPainted(false);
			contentPane.add(btnemoji5);
			btnemoji5.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setVisible(false);
					ChatMsg obcm = new ChatMsg(UserName ,"400", "(집중)");
					obcm.setEmoticon(집중);
					obcm.setRoom_id(room_id);
					obcm.setProfile(Profile);
					obcm.setUserlist(userlist);
					obcm.setNowDate(nowDate);
					SendObject(obcm);
					
				}
			});
			
			btnemoji6 = new JButton(축하);
			btnemoji6.setFocusPainted(false);
			btnemoji6.setContentAreaFilled(false);
			btnemoji6.setBorderPainted(false);
			contentPane.add(btnemoji6);
			btnemoji6.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setVisible(false);
					ChatMsg obcm = new ChatMsg(UserName ,"400", "(축하)");
					obcm.setEmoticon(축하);
					obcm.setRoom_id(room_id);
					obcm.setProfile(Profile);
					obcm.setUserlist(userlist);
					obcm.setNowDate(nowDate);
					SendObject(obcm);
					
				}
			});
			
			btnemoji7 = new JButton(하트뿅);
			btnemoji7.setFocusPainted(false);
			btnemoji7.setContentAreaFilled(false);
			btnemoji7.setBorderPainted(false);
			contentPane.add(btnemoji7);
			btnemoji7.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setVisible(false);
					ChatMsg obcm = new ChatMsg(UserName ,"400", "(하트뿅)");
					obcm.setEmoticon(하트뿅);
					obcm.setRoom_id(room_id);
					obcm.setProfile(Profile);
					obcm.setUserlist(userlist);
					obcm.setNowDate(nowDate);
					SendObject(obcm);
					
				}
			});
			
			btnemoji8 = new JButton(하하);
			btnemoji8.setFocusPainted(false);
			btnemoji8.setContentAreaFilled(false);
			btnemoji8.setBorderPainted(false);
			contentPane.add(btnemoji8);
			btnemoji8.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setVisible(false);
					ChatMsg obcm = new ChatMsg(UserName ,"400", "(하하)");
					obcm.setEmoticon(엄지척);
					obcm.setRoom_id(room_id);
					obcm.setProfile(Profile);
					obcm.setUserlist(userlist);
					obcm.setNowDate(nowDate);
					SendObject(obcm);
					
				}
			});
			
			btnemoji9 = new JButton(히히);
			btnemoji9.setFocusPainted(false);
			btnemoji9.setContentAreaFilled(false);
			btnemoji9.setBorderPainted(false);
			contentPane.add(btnemoji9);
			btnemoji9.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setVisible(false);
					ChatMsg obcm = new ChatMsg(UserName ,"400", "(히히)");
					obcm.setEmoticon(히히);
					obcm.setRoom_id(room_id);
					obcm.setProfile(Profile);
					obcm.setUserlist(userlist);
					obcm.setNowDate(nowDate);
					SendObject(obcm);
					
				}
			});
		}
	}
	
	
	class ListenNetwork extends Thread {
		
		public void run() {
			while(true) {
				try {
					Object obcm = null;
					String msg = null;
					ImageIcon profile = null;
					String userName = null;
					String room_id = null;
					String userlist = null;
					ChatMsg cm;
					ImageIcon emoticon = null;
					String nowDate = null;
					String[] chat_arrUserList = null;
					try {
						obcm = ois.readObject();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
						break;
					}
					if (obcm == null)
						break;
					if (obcm instanceof ChatMsg) {
						cm = (ChatMsg) obcm;
						msg = cm.getData();
						profile = cm.getProfile();
						userName = cm.getId();
						userlist = cm.getUserlist();
						room_id = cm.getRoom_id();
						nowDate = cm.getNowDate();
						emoticon = cm.getEmoticon();
					} else 
						continue;
					switch(cm.getCode()) {
					case "100": //내가 로그인
						FriendPanel n_friends = new FriendPanel(userName, profile);
						n_friends.setPreferredSize(new Dimension(309,75)); //label 사이즈 조절
						textPaneFriendList.insertComponent(n_friends);
						friends.add(n_friends);
						friendsName.add(userName);
						friendsProfile.add(profile);
						break;
					case "150": //남이 로그인, 친구 추가 개념
						n_friends = new FriendPanel(userName, profile);
						n_friends.setPreferredSize(new Dimension(309, 75));
						textPaneFriendList.insertComponent(n_friends);
						friends.add(n_friends);
						friendsName.add(userName);
						friendsProfile.add(profile);
						break;
					case "200":
						chat_arrUserList = userlist.split(", ");
						
						if(UserName.matches(userName)) { //내가 보낸 메세지
							int msgLength = msg.length();
							ClientMsgPanel c_msg = new ClientMsgPanel(userName, profile, msg, nowDate);
							
							for(int i=0; i<RoomVec.size(); i++) {
								if(((String) RoomIdVec.get(i)).matches(room_id)) {
									ChatRoom tempRoom = (ChatRoom) RoomVec.get(i);
									JTextPane tempTextPane = tempRoom.getTextPane();
									c_msg.setPreferredSize(new Dimension(350, 55));

									int len = tempTextPane.getDocument().getLength();
						            tempTextPane.setCaretPosition(len);
									tempTextPane.insertComponent(c_msg);
									//tempTextPane.repaint();
									
									
									if(chat_arrUserList.length == 2) {
										RoomPanel tempRoomPanel = (RoomPanel) ChatRoomVec.get(i); //마지막 메세지
										tempRoomPanel.setLastMsg(msg, nowDate);
										//tempRoomPanel.setLastTime(nowDate);
									}
									else if(chat_arrUserList.length == 3) {
										RoomPanel2 tempRoomPanel = (RoomPanel2) ChatRoomVec.get(i); //마지막 메세지
										tempRoomPanel.setLastMsg(msg, nowDate);
									}
									else if(chat_arrUserList.length == 4) {
										RoomPanel3 tempRoomPanel = (RoomPanel3) ChatRoomVec.get(i); //마지막 메세지
										tempRoomPanel.setLastMsg(msg, nowDate);
									}
									else if(chat_arrUserList.length >= 5) {
										RoomPanel4 tempRoomPanel = (RoomPanel4) ChatRoomVec.get(i); //마지막 메세지
										tempRoomPanel.setLastMsg(msg, nowDate);
									}
								}
							}
						}
						
						else { //친구가 보낸 메세지
							FriendMsgPanel f_msg = new FriendMsgPanel(userName, profile, msg, nowDate);
							f_msg.setPreferredSize(new Dimension(350, 70));
							
								for(int i=0; i<RoomVec.size(); i++) {
									if(((String) RoomIdVec.get(i)).matches(room_id)) {
										ChatRoom tempRoom = (ChatRoom) RoomVec.get(i);
										JTextPane tempTextPane = tempRoom.getTextPane();
										int len = tempTextPane.getDocument().getLength();
										tempTextPane.setCaretPosition(len);
										tempTextPane.insertComponent(f_msg);
									
									if(chat_arrUserList.length == 2) {
										RoomPanel tempRoomPanel = (RoomPanel) ChatRoomVec.get(i); //마지막 메세지
										tempRoomPanel.setLastMsg(msg, nowDate);
									}
									else if(chat_arrUserList.length == 3) {
										RoomPanel2 tempRoomPanel = (RoomPanel2) ChatRoomVec.get(i); //마지막 메세지
										tempRoomPanel.setLastMsg(msg, nowDate);
									}
									else if(chat_arrUserList.length == 4) {
										RoomPanel3 tempRoomPanel = (RoomPanel3) ChatRoomVec.get(i); //마지막 메세지
										tempRoomPanel.setLastMsg(msg, nowDate);
									}
									else if(chat_arrUserList.length >= 5) {
										RoomPanel4 tempRoomPanel = (RoomPanel4) ChatRoomVec.get(i); //마지막 메세지
										tempRoomPanel.setLastMsg(msg, nowDate);
									}
								}
						
							}
						}
						break;
					case "300": // 이미지
						ImageIcon ori_img = cm.getOri_img();
						ImageIcon img = cm.getImg();
						chat_arrUserList = userlist.split(", ");
						JPanel ClientImgpanel = null;
						JPanel FriendImgpanel = null;
						
						if(UserName.matches(userName)) { //내가 보낸 메세지
							ClientImgpanel = new ClientEmoPanel(nowDate, null, img, ori_img);
							
							int size = ((ClientEmoPanel) ClientImgpanel).get_size();
							ClientImgpanel.setPreferredSize(new Dimension(350, size));

							for(int i=0; i<RoomVec.size(); i++) {
								if(((String) RoomIdVec.get(i)).matches(room_id)) {
									ChatRoom tempRoom = (ChatRoom) RoomVec.get(i);
									JTextPane tempTextPane = tempRoom.getTextPane();

									int len = tempTextPane.getDocument().getLength();
									tempTextPane.setCaretPosition(len);
									tempTextPane.insertComponent(ClientImgpanel);
								
									if(chat_arrUserList.length == 2) {
										RoomPanel tempRoomPanel = (RoomPanel) ChatRoomVec.get(i); //마지막 메세지
										tempRoomPanel.setLastMsg(msg, nowDate);
									}
									else if(chat_arrUserList.length == 3) {
										RoomPanel2 tempRoomPanel = (RoomPanel2) ChatRoomVec.get(i); //마지막 메세지
										tempRoomPanel.setLastMsg(msg, nowDate);
									}
									else if(chat_arrUserList.length == 4) {
										RoomPanel3 tempRoomPanel = (RoomPanel3) ChatRoomVec.get(i); //마지막 메세지
										tempRoomPanel.setLastMsg(msg, nowDate);
									}
									else if(chat_arrUserList.length >= 5) {
										RoomPanel4 tempRoomPanel = (RoomPanel4) ChatRoomVec.get(i); //마지막 메세지
										tempRoomPanel.setLastMsg(msg, nowDate);
									}
								}
							}
							
						}
						else { //친구가 보낸 메세지
							FriendImgpanel = new FriendEmoPanel(userName, profile, nowDate, null , img, ori_img);
							System.out.println(ori_img);
							int size = ((FriendEmoPanel) FriendImgpanel).get_size();
							FriendImgpanel.setPreferredSize(new Dimension(350, size));
							for(int i=0; i<RoomVec.size(); i++) {
								if(((String) RoomIdVec.get(i)).matches(room_id)) {
									ChatRoom tempRoom = (ChatRoom) RoomVec.get(i);
									JTextPane tempTextPane = tempRoom.getTextPane();
									int len = tempTextPane.getDocument().getLength();
									tempTextPane.setCaretPosition(len);
									tempTextPane.insertComponent(FriendImgpanel);
								
								
									if(chat_arrUserList.length == 2) {
										RoomPanel tempRoomPanel = (RoomPanel) ChatRoomVec.get(i); //마지막 메세지
										tempRoomPanel.setLastMsg(msg, nowDate);
									}
									else if(chat_arrUserList.length == 3) {
										RoomPanel2 tempRoomPanel = (RoomPanel2) ChatRoomVec.get(i); //마지막 메세지
										tempRoomPanel.setLastMsg(msg, nowDate);
									}
									else if(chat_arrUserList.length == 4) {
										RoomPanel3 tempRoomPanel = (RoomPanel3) ChatRoomVec.get(i); //마지막 메세지
										tempRoomPanel.setLastMsg(msg, nowDate);
									}
									else if(chat_arrUserList.length >= 5) {
										RoomPanel4 tempRoomPanel = (RoomPanel4) ChatRoomVec.get(i); //마지막 메세지
										tempRoomPanel.setLastMsg(msg, nowDate);
									}
								}
							}
						}
						break;
					case "400": // 이모티콘
						chat_arrUserList = userlist.split(", ");
						JPanel ClientEmopanel = null;
						JPanel FriendEmopanel = null;
						msg = msg.replace("(", "").replace(")", "");
						
						if(UserName.matches(userName)) { //내가 보낸 메세지
							for(String name : emoticonHash.keySet()) {
								if(name.equals(msg)) {
									ClientEmopanel = new ClientEmoPanel(nowDate, emoticonHash.get(name), null, null);
								}
							}
							int size = ((ClientEmoPanel) ClientEmopanel).get_size();
							ClientEmopanel.setPreferredSize(new Dimension(350, size));

							for(int i=0; i<RoomVec.size(); i++) {
								if(((String) RoomIdVec.get(i)).matches(room_id)) {
									ChatRoom tempRoom = (ChatRoom) RoomVec.get(i);
									JTextPane tempTextPane = tempRoom.getTextPane();

									int len = tempTextPane.getDocument().getLength();
									tempTextPane.setCaretPosition(len);
									tempTextPane.insertComponent(ClientEmopanel);
								
									String tempMsg = "(이모티콘)";
									if(chat_arrUserList.length == 2) {
										RoomPanel tempRoomPanel = (RoomPanel) ChatRoomVec.get(i); //마지막 메세지
										tempRoomPanel.setLastMsg(tempMsg, nowDate);
										//tempRoomPanel.setLastTime(nowDate);
									}
									else if(chat_arrUserList.length == 3) {
										RoomPanel2 tempRoomPanel = (RoomPanel2) ChatRoomVec.get(i); //마지막 메세지
										tempRoomPanel.setLastMsg(tempMsg, nowDate);
									}
									else if(chat_arrUserList.length == 4) {
										RoomPanel3 tempRoomPanel = (RoomPanel3) ChatRoomVec.get(i); //마지막 메세지
										tempRoomPanel.setLastMsg(tempMsg, nowDate);
									}
									else if(chat_arrUserList.length >= 5) {
										RoomPanel4 tempRoomPanel = (RoomPanel4) ChatRoomVec.get(i); //마지막 메세지
										tempRoomPanel.setLastMsg(tempMsg, nowDate);
									}
								}
							}
							
						}
						else { //친구가 보낸 메세지
							for(String name : emoticonHash.keySet()) {
								if(name.equals(msg)) {
									FriendEmopanel = new FriendEmoPanel(userName, profile, nowDate, emoticonHash.get(name), null, null);
								}
							}
							int size = ((FriendEmoPanel) FriendEmopanel).get_size();
							FriendEmopanel.setPreferredSize(new Dimension(350, size));
							for(int i=0; i<RoomVec.size(); i++) {
								if(((String) RoomIdVec.get(i)).matches(room_id)) {
									ChatRoom tempRoom = (ChatRoom) RoomVec.get(i);
									JTextPane tempTextPane = tempRoom.getTextPane();
									int len = tempTextPane.getDocument().getLength();
									tempTextPane.setCaretPosition(len);
									tempTextPane.insertComponent(FriendEmopanel);
								
								
									String tempMsg="(이모티콘)";
									if(chat_arrUserList.length == 2) {
										RoomPanel tempRoomPanel = (RoomPanel) ChatRoomVec.get(i); //마지막 메세지
										tempRoomPanel.setLastMsg(tempMsg, nowDate);
									}
									else if(chat_arrUserList.length == 3) {
										RoomPanel2 tempRoomPanel = (RoomPanel2) ChatRoomVec.get(i); //마지막 메세지
										tempRoomPanel.setLastMsg(tempMsg, nowDate);
									}
									else if(chat_arrUserList.length == 4) {
										RoomPanel3 tempRoomPanel = (RoomPanel3) ChatRoomVec.get(i); //마지막 메세지
										tempRoomPanel.setLastMsg(tempMsg, nowDate);
									}
									else if(chat_arrUserList.length >= 5) {
										RoomPanel4 tempRoomPanel = (RoomPanel4) ChatRoomVec.get(i); //마지막 메세지
										tempRoomPanel.setLastMsg(tempMsg, nowDate);
									}
								}
							}
						}
						break;
					case "500": // 채팅방 생성
						String[] arrUserList = userlist.split(", ");
						ChatRoom new_room = new ChatRoom(room_id, userlist);
						RoomVec.add(new_room);
						RoomIdVec.add(room_id);
						
						Vector profileList = new Vector();
						for(int i=0; i<arrUserList.length; i++) {
							for(int j=0; j<friendsName.size(); j++) {
								if(friendsName.get(j).matches(UserName)) continue;
								if(arrUserList[i].matches(friendsName.get(j))){
									profileList.add(friendsProfile.get(j));
								}
							}
						}
						
						if(arrUserList.length == 2) { //채팅방 인원: 2명
							RoomPanel roomPanel = new RoomPanel(UserName, room_id, userlist, profileList);
							roomPanel.setPreferredSize(new Dimension(309, 75));
							textPaneChatList.insertComponent(roomPanel);
							ChatRoomVec.add(roomPanel);
						}
						else if(arrUserList.length == 3) { //채팅방 인원: 3명
							RoomPanel2 roomPanel = new RoomPanel2(UserName, room_id, userlist, profileList);
							roomPanel.setPreferredSize(new Dimension(309, 75));
							textPaneChatList.insertComponent(roomPanel);
							ChatRoomVec.add(roomPanel);
						}
						else if(arrUserList.length == 4) { //채팅방 인원: 4명
							RoomPanel3 roomPanel = new RoomPanel3(UserName, room_id, userlist, profileList);
							roomPanel.setPreferredSize(new Dimension(309, 75));
							textPaneChatList.insertComponent(roomPanel);
							ChatRoomVec.add(roomPanel);
						}
						else if(arrUserList.length >= 5) { //채팅방 인원: 5명 이상
							RoomPanel4 roomPanel = new RoomPanel4(UserName, room_id, userlist, profileList);
							roomPanel.setPreferredSize(new Dimension(309, 75));
							textPaneChatList.insertComponent(roomPanel);
							ChatRoomVec.add(roomPanel);
						}
						
						titleLabel.setText("채팅"); // 채팅 생성할 때 친구목록과 겹치는 오류가 나서 채팅 생성시 일괄적으로 채팅목록이 뜨도록 수정하였음
						btnAddChat.setVisible(true);
						clientPanel.setVisible(false);
						scrollPaneFriendList.setVisible(false);
						scrollPaneChatList.setVisible(true);
						break;
					case "550": //채팅방 클릭
						ChatRoom tempRoom;
						for(int i=0; i<RoomVec.size(); i++) {
							if(((String) RoomIdVec.get(i)).matches(room_id)) {
								tempRoom = (ChatRoom) RoomVec.get(i);
								tempRoom.getChatRoom();
							}
						}
						break;
					case "600":
						break;
					case "700":
						break;
					case "800":  // 800:프로필설정
						for(int i = 0; i < friends.size(); i++) {
							if(friendsName.get(i).equals(userName)) {
								friends.get(i).setProfile(profile);
								friendsProfile.remove(i);
								friendsProfile.add(i, profile);
							}
						}
						break;
					case "900":  //900: 로그아웃
						for(int i = 0; i < friends.size(); i++) {
							if(friendsName.get(i).equals(userName)) {
								friends.remove(i);
								friendsName.remove(i);
								break;
							}
						}
						textPaneFriendList.setText(null);
						for(int i=0; i<friends.size(); i++) {
							friends.get(i).setPreferredSize(new Dimension(309, 75));
							textPaneFriendList.insertComponent(friends.get(i));
						}
						break;
					}
				} catch (IOException e) {
					try {
						ois.close();
						oos.close();
						socket.close();
					
						break;
					} catch (Exception ee) {
						break;
					}
				}
			}
		}
	}
	public void AppendIcon(ImageIcon icon, JLabel jlabel) {
		int len = jlabel.getParent().getWidth();
		// 끝으로 이동
		jlabel.setVerticalTextPosition(len);
		jlabel.setIcon(icon);
	}

	// 화면에 출력
	public void AppendText(String msg, ImageIcon icon, JLabel jlabel) {
		AppendIcon(icon, jlabel);
		msg = msg.trim(); // 앞뒤 blank와 \n을 제거한다.
		int len = jlabel.getParent().getWidth();
		// 끝으로 이동
		jlabel.setVerticalTextPosition(len);
		jlabel.setText(msg + "\n");
	}
	
	public void SendObject(Object ob) { // 서버로 메세지를 보내는 메소드
		try {
			oos.writeObject(ob);
		} catch (IOException e) {
			System.out.print("SendObject Error");
		}
	}
	
}