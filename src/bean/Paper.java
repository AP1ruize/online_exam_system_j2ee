package bean;

import java.util.ArrayList;

public class Paper {
	private int id;
	
	private String subject;
	private String date;
	private String status;
	private String name;
	private int no;
	private ArrayList<Question> qus=new ArrayList<Question>();
	
	public Paper() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Paper(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Paper(int no,int id, String subject, String date, String status, String name) {
		super();
		this.no=no;
		this.id = id;
		this.subject = subject;
		this.date = date;
		this.status = status;
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public ArrayList<Question> getQus() {
		return qus;
	}
	public void setQus(ArrayList<Question> qus) {
		this.qus = qus;
	}
	
}
