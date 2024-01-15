import java.awt.Image;
import java.io.Serializable;
import javax.swing.ImageIcon;

class ChatMsg implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String code; 
	/* 100:로그인, 150: 다른 유저 친구 추가, 200:채팅메시지, 300:채팅이미지, 400:채팅이모티콘, 500:채팅방생성,
	600:채팅방목록, 700:채팅방초대, 800:프로필설정, 900:로그아웃*/
	private String data;
	private ImageIcon img;
	private ImageIcon profile;
	private ImageIcon emoticon;
	private String room_id;
	private String userlist;
	private String nowDate;
	private ImageIcon ori_img;

	public ChatMsg(String id, String code, String msg) {
		this.id = id;
		this.code = code;
		this.data = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getData() {
		return data;
	}
	
	public void setData(String data) {
		this.data = data;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setImg(ImageIcon img) {
		this.img = img;
	}
	
	public void setEmoticon(ImageIcon emoticon) {
		this.emoticon = emoticon;
	}
	public ImageIcon getProfile() {
		return profile;
	}
	
	public void setProfile(ImageIcon profile) {
		this.profile = profile;
	}
	
	public void setUserlist(String userlist) {
		this.userlist = userlist;
	}
	
	public void setOri_img(ImageIcon img) {
		this.ori_img = img;
	}
	
	public String getUserlist() {
		return userlist;
	}
	
	public void setRoom_id(String room_id) {
		this.room_id = room_id;
	}
	
	public String getRoom_id() {
		return room_id;
	}
	
	public ImageIcon getEmoticon() {
		return emoticon;
	}
	
	public void setNowDate(String nowDate) {
		this.nowDate = nowDate;
	}
	
	public String getNowDate() {
		return nowDate;
	}
	
	public ImageIcon getOri_img() {
		return ori_img;
	}
	
	public ImageIcon getImg() {
		return img;
	}
}