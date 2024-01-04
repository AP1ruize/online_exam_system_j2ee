package bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Column;


@Table(name="usertest")
@Entity
public class JpaUserTest {
	//管理员是可以根据考试来查询所有的考生得分情况的。
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	//这个存储user,在一对多中，
	@ManyToOne
	@JoinColumn(name="userid")//多对一
	private JpaUser juser;
	
	@ManyToOne
	@JoinColumn(name="testid")//多对一
	private JpaTest jtest;
	
	@Column(name="score")
	private int score;
	
	@Column(name="wrongno")
	private String wrongno;	
	
	@Column(name="subtime")
	private String subtime;	
	
	public JpaUserTest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public JpaUser getJuser() {
		return juser;
	}

	public void setJuser(JpaUser juser) {
		this.juser = juser;
	}

	public JpaTest getJtest() {
		return jtest;
	}

	public void setJtest(JpaTest jtest) {
		this.jtest = jtest;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getWrongno() {
		return wrongno;
	}

	public void setWrongno(String wrongno) {
		this.wrongno = wrongno;
	}

	public String getSubtime() {
		return subtime;
	}

	public void setSubtime(String subtime) {
		this.subtime = subtime;
	}	
	
}
