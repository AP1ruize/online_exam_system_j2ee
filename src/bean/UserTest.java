package bean;


public class UserTest {
	private int id;
	private String name;//考试名称
	private int score;//得分
	private int rank;
	private int total;
	private String subTime;
	private String wrongno;	
	
	
	
	
	public UserTest(int id, String name, int score,  int rank,int total,String subTime, String wrongno) {
		super();
		this.id = id;
		this.name = name;
		this.score = score;
		this.rank=rank;
		this.total = total;
		this.subTime = subTime;
		this.wrongno = wrongno;
	}
	public UserTest() {
		super();
		// TODO Auto-generated constructor stub
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
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public String getSubTime() {
		return subTime;
	}
	public void setSubTime(String subTime) {
		this.subTime = subTime;
	}
	public String getWrongno() {
		return wrongno;
	}
	public void setWrongno(String wrongno) {
		this.wrongno = wrongno;
	}
	
}
