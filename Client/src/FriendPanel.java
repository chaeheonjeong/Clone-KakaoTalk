import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;

public class FriendPanel extends JPanel {
	private JLabel lblFriendProfile;
	private JLabel lblFriendName;

	public FriendPanel(String username, ImageIcon profile) {

		setBackground(new Color(255, 255, 255));
		setLayout(null);
		
		lblFriendName = new JLabel(username);
		lblFriendName.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		lblFriendName.setBounds(83, 0, 226, 75);
		add(lblFriendName);
		
		lblFriendProfile = new JLabel(profile);
		lblFriendProfile.setBounds(6, 5, 65, 65);
		add(lblFriendProfile);
	}
	
	public void setProfile(ImageIcon profile) {
		lblFriendProfile.setIcon(profile);
	}
	
	public void setName(String username) {
		lblFriendName.setText(username);
	}
	
}
