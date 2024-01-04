package bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Table(name="test")
@Entity
public class JpaTest {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="deadline")
	private String deadline;
	
	@Column(name="subject")
	private String subject;
	
	@Column(name="intro")
	private String intro;
	
	@Column(name="totalStu")
	private int totalStu;
	
	@Column(name="status")
	private String status;
	
	//这个地方涉及到表之间的关联。
	//这里是一对一单向的关系
	
	
	//private int paperid;
	@OneToOne
	@JoinColumn(name = "paperid")
	private JpaPaper jpaper;//这里必须是一个实体类。对应的jpapaper类中什么都不用加。
	
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
	/*public int getPaperid() {
		return paperid;
	}
	public void setPaperid(int paperid) {
		this.paperid = paperid;
	}*/
	public JpaPaper getJpaper() {
		return jpaper;
	}
	public void setJpaper(JpaPaper jpaper) {
		this.jpaper = jpaper;
	}
}
