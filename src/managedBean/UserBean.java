package managedBean;

//import javax.enterprise.context.SessionScoped;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;

import bean.Question;
import bean.Test;
import bean.User;
import bean.UserTest;

@ManagedBean(name="userBean")
@SessionScoped
public class UserBean{
	
	@EJB
	private statelessBean.UserManage um;
	
	@EJB
	private statefulBean.CollectManage cm;
	
	@EJB
	private statelessBean.TestManage tm;
	
	
	private int id;//提交表单时已经赋值。
	private String name;
	private String password;
	private String age;
	private String sex;
	private String answer;
	private String trueAns;
	private String message;
	private String subject;
	private Set<String> subs=new HashSet<String>();//存储所有的科目
	private Map<String,String> itemSub=new LinkedHashMap<String,String>();
	private List<Test> tests=new ArrayList<Test>();
	private Map<String,Integer> itemTest=new LinkedHashMap<String,Integer>();
	private List<Question> qus=new ArrayList<Question>();
	private int testid;
	private String csubject;//这个变量用来在用户查询试卷时进行
	private String collectNo;
	private int num;
	private int totalNum;
	private String testName;
	private List<String> userAns=new ArrayList<String>();
	private int score;
	private List<String> wrong=new ArrayList<String>();
	private List<UserTest> usertest=new ArrayList<UserTest>();
	
	private User user;
	public String login(){//用户登录。
		ArrayList<User> users=um.queryAuser(this.name);//查找用户是否存在
		if(users.size()==0){
			message="Sorry! The user doesn't exist! Please register first!";
			return "failure";
		}
		String ep=users.get(0).getPassword();
		this.id=users.get(0).getId();//这里存储了用户的id，以便进行以后的操作。
		this.age=users.get(0).getAge();
		this.sex=users.get(0).getSex();//获取到性别。
		this.answer="";
		this.trueAns=users.get(0).getAnswer();
		String ep2=um.encrypt(this.password);
		if(!ep.equals(ep2)){
			System.out.println(ep);
			System.out.println(ep2);
			message="Incorrect password! Please try again!";
			return "failure";
		}
		return "success";
	}

	public String validateAnswer(){
		this.password="";//清空之前输入的密码。
		if(answer.equals(this.trueAns))//判断是否相等。
			return "validateSuc";
		else
			return "validateFailure";
	}
	
	public String changePass(){
		um.changePass(this.id,password);		
		return "success";
	}
	
	public String addUser(){
//		ArrayList<User> users=um.query();
//		Iterator<?> it=users.iterator();
//		while(it.hasNext()){
//			user=(User)it.next();
//			if(this.name.equals(user.getName())){
//				message="Sorry! Your registraion is failed! The user name already exsits!";
//				return "failure";
//			}
//		}
		System.out.println(this.name+"哈哈");
		ArrayList<User> users=um.queryAuser(this.name);//查找用户是否存在
		if(users.size()!=0){
			message="Sorry! Your registraion is failed! The user name already exsits!";
			return "failure";
		}
		User user=new User();
		user.setName(name);
		user.setPassword(password);
		user.setAge(age);
		user.setSex(sex);
		user.setAnswer(answer);
		um.insert(user);		
		return "success";
	}
	public void querySub(){//查询所有考试中的科目。
		subs=um.querySub();
		for(String s:subs){
			itemSub.put(s,s);
		}
	}
	
	public void queryTest(ValueChangeEvent e){//按照科目查询考试
		if(e.getNewValue()==null)return ;
		itemTest.clear();
		this.subject=e.getNewValue().toString();//加上这一句才可以获取到组件的值。
		tests=um.queryTest(this.subject);
		for(Test t:tests){
			itemTest.put(t.getName()+"(ddl:"+t.getDeadline()+")",t.getId());
		}
	}
	
	public String registerExam(){//注册考试
		//根据获取考试的id和用户的id.
		return um.registerExam(this.id,this.testid);
	}
	
	public void queryMyTestName(){//查询用户报名考试的名字。
		tests=um.queryMyTestName(this.id);
		itemTest.clear();
		for(Test t:tests){//放进item当中进行显示
			itemTest.put(t.getName(),t.getId());//label,value
			//key value
		}
	}

	public void queryTestCon(ValueChangeEvent e){//查询用户报名考试所含的题目。
		if(e.getNewValue()==null)return ;
		this.testid=Integer.parseInt(e.getNewValue().toString());//加上这一句才可以获取到组件的值。
		for(Map.Entry<String,Integer> mapEntry:itemTest.entrySet()){
			if(mapEntry.getValue()==this.testid){
				this.testName=mapEntry.getKey();break;//获取到考试的名字。
			}	
		}
	//	System.out.println(this.testid+"报名");
		qus=um.queryTestCon(this.testid);		
	}
	
	public String collect(){//收藏试卷。
		String[] sp=collectNo.split(",");//获取收藏试卷的编号。
		totalNum=sp.length;
		//首先是查找到所有的试卷。
		//但是这里序号为1，在list里也就是第0个元素。这样获取到对象？这里只要获取到试题的id就可以了。
		ArrayList<Integer> ids=new ArrayList<Integer>();
		for(String s:sp){
			ids.add(qus.get(Integer.parseInt(s)-1).getId());//获取到试题的id
		}
		num=cm.collect(this.id,ids);//这里主要是返回，如果用户收藏了，多个返回，num是重复收藏的个数，那么sp.length-num就是收藏了几个试题。
		return "success";
	}
	
	public String isBegin(){//这里根据考试id，确定当前考试是否已经开始，如果开始了，那么就跳转到考试页面，没开始则提示当前考试没开始。
		String status=um.isBegin(this.testid);
		//首先判断用户是否已经参加过考试，如果已经参加过，那么就不能再开始了。
		String is=um.hasDone(this.id,this.testid);
		if(is.equals("hasdone"))
			return "hasdone";//此处表示已经完成了本考试。
		if(!status.equals("F"))//考试已经开始那么可以做了。
			return "yes";
		else
			return "no";		
		//这里其实还应该判断，当前的时间是否是已经过了截止期。
	}
	
	public String submitPaper(){		
		for(Question q:qus){
			userAns.add(q.getTans());	//这里可以获取到选项的。		
		}
		wrong=tm.submitPaper(userAns,this.id,this.testid);//获得错题。
		score=50-wrong.size()*10;//这就是得分。
		return "success";
	}
	
	public void getCollection(){//获取到用户收藏的试题。
		qus=cm.getCollection(this.id);	
	}
	
	public void checkScore(){//这里可以获取到userid
		//查询用户参与的所有的考试
		usertest=um.checkScore(this.id);
	}
	
	public String saveEdit(){
		User u=new User();
		u.setId(this.id);
		u.setAge(this.age);
		u.setSex(this.sex);
		um.saveEdit(u);
		return "success";
	}
	public void cancelCollect(Question q){//取消收藏。
		qus.remove(q);//首先从list中删除。
		cm.cancelCollect(this.id,q);		
	}
	
	public UserBean() {}//托管bean必须得有一个无参的构造函数
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, String> getItemSub() {
		return itemSub;
	}

	public void setItemSub(Map<String, String> itemSub) {
		this.itemSub = itemSub;
	}

	public List<Test> getTests() {
		return tests;
	}

	public void setTests(List<Test> tests) {
		this.tests = tests;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Map<String, Integer> getItemTest() {
		return itemTest;
	}

	public void setItemTest(Map<String, Integer> itemTest) {
		this.itemTest = itemTest;
	}

	public int getTestid() {
		return testid;
	}

	public void setTestid(int testid) {
		this.testid = testid;
	}

	public String getCsubject() {
		return csubject;
	}

	public void setCsubject(String csubject) {
		this.csubject = csubject;
	}

	public List<Question> getQus() {
		return qus;
	}

	public void setQus(List<Question> qus) {
		this.qus = qus;
	}

	public String getCollectNo() {
		return collectNo;
	}

	public void setCollectNo(String collectNo) {
		this.collectNo = collectNo;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	

	public List<String> getUserAns() {
		return userAns;
	}

	public void setUserAns(List<String> userAns) {
		this.userAns = userAns;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public List<String> getWrong() {
		return wrong;
	}

	public void setWrong(List<String> wrong) {
		this.wrong = wrong;
	}

	public List<UserTest> getUsertest() {
		return usertest;
	}

	public void setUsertest(List<UserTest> usertest) {
		this.usertest = usertest;
	}

	public String getTrueAns() {
		return trueAns;
	}

	public void setTrueAns(String trueAns) {
		this.trueAns = trueAns;
	}
	
	
}
