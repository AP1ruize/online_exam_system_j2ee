package bean;

public class Test {
	private int no;
	private int id;
	private String name;
	private String deadline;
	private String subject;
	private String intro;
	private int totalStu;
	private String status;
	private int paperid;
	
	public Test() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	public Test(int id, String name, String deadline) {
		super();
		this.id = id;
		this.name = name;
		this.deadline = deadline;
	}

	public Test(int no,int id, String name, String deadline, String subject,
			String intro, int totalStu, String status, int paperid) {
		super();
		this.no=no;
		this.id = id;
		this.name = name;
		this.deadline = deadline;
		this.subject = subject;
		this.intro = intro;
		this.totalStu = totalStu;
		this.status = status;
		this.paperid = paperid;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDeadline() {
		return deadline;
	}
	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public int getTotalStu() {
		return totalStu;
	}
	public void setTotalStu(int totalStu) {
		this.totalStu = totalStu;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getPaperid() {
		return paperid;
	}
	public void setPaperid(int paperid) {
		this.paperid = paperid;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}
	
}
