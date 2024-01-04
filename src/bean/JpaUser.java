package bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Column;

@Table(name="jpauser")
@Entity
public class JpaUser implements Serializable{
	private static final long serialVersionUID = 1L;
	//实现序列化接口之后，通过rmi(包括ejb)提供远程调用
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="password")
	private String password;
	
	@Column(name="age")
	private String age;
	
	@Column(name="sex")
	private String sex;
	
	@Column(name="answer")
	private String answer;
	
	@ManyToMany
	@JoinTable(name="usercollect",joinColumns={@JoinColumn(name="userid",referencedColumnName="id")},
	inverseJoinColumns={@JoinColumn(name="qusid",referencedColumnName="id")})
	private List<JpaQus> jquss=new ArrayList<JpaQus>();//这个存储用户收藏的试题。
	//joinColumns这些外键列参照当前实体对应表的主键列
	//用于配置连接表中外键列的信息，这些外键列参照当前实体的关联实体对应表的主键列,搞反了吧。。。。
	
	public JpaUser(){}
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public List<JpaQus> getJquss() {
		return jquss;
	}
	public void setJquss(List<JpaQus> jquss) {
		this.jquss = jquss;
	}
	
}
