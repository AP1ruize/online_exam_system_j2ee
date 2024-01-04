package bean;

public class User {//这是一个纯粹的javaBean
	private int id;
	private String name;
	private String password;
	private String sex;
	private String age;
	private String answer;
	public User(){}
	public User(int id,String name,String password,String age,String sex,String answer){
		this.id=id;
		this.name=name;
		this.password=password;
		this.age=age;
		this.sex=sex;
		this.answer=answer;
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
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}	
	
}
