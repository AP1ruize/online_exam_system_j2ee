package bean;

public class Question {
	private int id;
	
	private int no;//表示题目的序号，并不是id.
	private String content;
	private String subject;
	private String tans;
	private String aans;
	private String bans;
	private String cans;
	private boolean editable;
	private String status;
	public Question() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Question(int no,int id, String content, String subject, String tans,
			String aans, String bans, String cans,String status) {
		this.no=no;
		this.id = id;
		this.content = content;
		this.subject = subject;
		this.tans = tans;
		this.aans = aans;
		this.bans = bans;
		this.cans = cans;
		this.status=status;
	}

	
	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getTans() {
		return tans;
	}
	public void setTans(String tans) {
		this.tans = tans;
	}
	public String getAans() {
		return aans;
	}
	public void setAans(String aans) {
		this.aans = aans;
	}
	public String getBans() {
		return bans;
	}
	public void setBans(String bans) {
		this.bans = bans;
	}
	public String getCans() {
		return cans;
	}
	public void setCans(String cans) {
		this.cans = cans;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}
	
}
