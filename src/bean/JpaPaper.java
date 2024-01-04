package bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.JoinColumn; 

@Table(name="paper")//试卷与试题是多对多的关系。
//一个试卷中包含多个试题，一个试题可以被多个试卷包含
@Entity
public class JpaPaper implements Serializable{
	private static final long serialVersionUID=1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Column(name="subject")
	private String subject;
	
	@Column(name="addTime")
	private String date;
	
	@Column(name="status")
	private String status;
	
	@Column(name="name")
	private String name;
	
	//一个试卷包含多个试题
	@ManyToMany
	@JoinTable(name="paperqus",joinColumns={@JoinColumn(name="paperid",referencedColumnName="id")},
	inverseJoinColumns={@JoinColumn(name="qusid",referencedColumnName="id")})
	private List<JpaQus> jquss=new ArrayList<JpaQus>();
	
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
	public List<JpaQus> getJquss() {
		return jquss;
	}
	public void setJquss(ArrayList<JpaQus> jquss) {
		this.jquss = jquss;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}	
	
}
