//import java.awt.Color;
//import java.awt.event.ActionListener;
//import java.util.Vector;
//
//import javax.swing.JButton;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//import javax.swing.SwingConstants;
//
///*public class RoomPanel extends JPanel {
//		private JButton btnRoomName;
//		private JLabel lbFriendsProfile;
//		private String RoomId;
//		private JLabel lbLastMsg;
//		private JLabel lbTime;
//
//		public RoomPanel(String UserName, String RoomId, String RoomName, Vector<ImageIcon> friendsProfileList) {
//
//			this.RoomId = RoomId;
//			setBackground(new Color(255, 255, 255));
//			setLayout(null);
//			
//			btnRoomName = new JButton(RoomName);
//			btnRoomName.setBounds(65, 7, 150, 30);
//			btnRoomName.setContentAreaFilled(false);
//			btnRoomName.setFocusPainted(false);
//			btnRoomName.setBorderPainted(false);
//			//btnRoomName.setHorizontalAlignment(SwingConstants.LEFT);
//			add(btnRoomName);
//			
//			lbFriendsProfile = new JLabel(friendsProfileList.get(0));
//			lbFriendsProfile.setHorizontalAlignment(JLabel.CENTER);
//			lbFriendsProfile.setBounds(6, 5, 65, 65);
//			add(lbFriendsProfile);
//			
//			btnRoomName.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					ChatMsg obcm = new ChatMsg(UserName ,"550", "채팅방 생성");
//					obcm.setRoom_id(RoomId);
//					obcm.setUserlist(RoomName);
//					SendObject(obcm);
//				}
//			});
//			
//			lbLastMsg = new JLabel();
//			lbLastMsg.setBounds(83, 30, 226, 30);
//			add(lbLastMsg);
//			
//			lbTime = new JLabel();
//			lbTime.setBounds(250, 7, 100, 20);
//			add(lbTime);
//		}
//		public void setLastMsg(String msg) {
//			lbLastMsg.setForeground(Color.GRAY);
//			//lbLastMsg.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
//			lbLastMsg.setText(msg);
//			revalidate();
//		}
//		public void setLastTime(String time) {
//			lbTime.setForeground(Color.GRAY);
//			//lbLastMsg.setFont(new Font("맑은 고딕", Font.PLAIN, 9));
//			lbLastMsg.setText(time);
//			revalidate();
//		}
//	}*/