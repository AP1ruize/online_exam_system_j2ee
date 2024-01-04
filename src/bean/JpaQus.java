package bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="question")
@Entity
public class JpaQus {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Column(name="content")
	private String content;
	
	@Column(name="subject")
	private String subject;
	
	@Column(name="tans")
	private String tans;
	
	@Column(name="aans")
	private String aans;
	
	@Column(name="bans")
	private String bans;
	
	@Column(name="cans")
	private String cans;
	
	@Column(name="status")
	private String status;//表示是否被使用。若已经使用，那么则不能删除
	public JpaQus() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getId() {
		return id;
	}
	public String query(){//查询试题，试题要使用科目才可以查询。
		
		return "success";
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
