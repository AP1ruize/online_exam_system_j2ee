package statelessBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import bean.JpaPaper;
import bean.JpaQus;
import bean.JpaTest;
import bean.JpaUser;
import bean.JpaUserTest;
import bean.Paper;
import bean.Question;
import bean.Test;

@Stateless
@LocalBean
public class TestManage {
	
	@PersistenceUnit(name = "testsys")
	private EntityManagerFactory emf;
	private EntityManager em;
	
	private int id;
	private String name;
	private String deadline;
	private String subject;
	private String intro;
	private int totalStu;
	private String status;
	private int paperid;
	private ArrayList<Test> quss=new ArrayList<Test>();
	private Set<String> subs=new HashSet<String>();
	private List<Paper> list=new ArrayList<Paper>();
	private List<Test> alltt=new ArrayList<Test>();
	private List<Question> qus=new ArrayList<Question>();
	
	public Set<String> queryAllSub(){
		em = emf.createEntityManager();
		Query q = em.createQuery("select u from JpaPaper u");
		List<?> res = q.getResultList();
		Iterator<?> it = res.iterator();
		while (it.hasNext()) {
			JpaPaper jq = (JpaPaper) it.next();
			subs.add(jq.getSubject());//查询到所有的试卷的科目并且放进去。
		}
		em.close();
		return subs;
	}
	//按科目查找所有的试卷，返回id和name
	public List<Paper> queryPp(String sub){
		list.clear();
		em = emf.createEntityManager();
		Query q = em.createQuery("select u from JpaPaper u where u.subject=:subject");
		q.setParameter("subject",sub);
		List<?> res = q.getResultList();
		Iterator<?> it = res.iterator();
		while (it.hasNext()) {
			JpaPaper jq = (JpaPaper) it.next();
			//其实这里查找到所有试卷的姓名和id就可以了。
			int id=jq.getId();
			String name=jq.getName();
			list.add(new Paper(id,name));//查询到所有的试卷的科目并且放进去。
		}
		return list;
	}
	
	public void add(Test t){//新增考试
		em = emf.createEntityManager();
		JpaTest jt=new JpaTest();
		jt.setName(t.getName());
		jt.setDeadline(t.getDeadline());
		jt.setSubject(t.getSubject());
		jt.setStatus(t.getStatus());
		jt.setIntro(t.getIntro());
		JpaPaper jp=em.find(JpaPaper.class,t.getPaperid());//获取到对象
		jp.setStatus("T");//将试卷状态设置为T，则变为正在使用。
		jt.setJpaper(jp);
		em.persist(jt);	//持久化到数据库中。
		em.close();
	}
	
	public List<Test> queryAllTest(){
		alltt.clear();
		em = emf.createEntityManager();
		Query q = em.createQuery("select u from JpaTest u");
		List<?> res = q.getResultList();
		Iterator<?> it = res.iterator();
		int ct=1;
		while (it.hasNext()) {
			JpaTest jj=(JpaTest)it.next();
			int id=jj.getId();
			
			String name=jj.getName();
			String deadline=jj.getDeadline();
			String subject=jj.getSubject();
			String intro=jj.getIntro();
			String stat=jj.getStatus();
			int totalStu=jj.getTotalStu();//将所有的考试对象放进list中
			int pid=jj.getJpaper().getId();//这里是使用试卷的id
			alltt.add(new Test(ct,id,name,deadline,subject,intro,totalStu,stat,pid));
			ct++;
		}
		em.close();
		return alltt;
	}
	
	public Test queryById(int id){
		Test t=new Test();
		em = emf.createEntityManager();
		JpaTest jp=em.find(JpaTest.class,id);//获取到对象
		t.setId(jp.getId());
		t.setName(jp.getName());
		t.setDeadline(jp.getDeadline());
		t.setSubject(jp.getSubject());
		t.setIntro(jp.getIntro());
		t.setPaperid(jp.getJpaper().getId());//获取到id
		t.setStatus(jp.getStatus());
		em.close();
		return t;
	}
	
	public void delete(Test t){//从数据库中删除考试
		em = emf.createEntityManager();
		JpaTest jp=em.find(JpaTest.class,t.getId());//获取到对象
		em.remove(jp);
		em.close();
	}
	
	public List<Question> queryQus(Test t){
		em = emf.createEntityManager();
		JpaPaper jp=em.find(JpaPaper.class,t.getPaperid());
		List<JpaQus> jqus=jp.getJquss();//获取到。
		int ct=1;
		for(JpaQus jq:jqus){
			int id=jq.getId();
			String cont=jq.getContent();
			String sub=jq.getSubject();
			String ta=jq.getTans();
			String a=jq.getAans();
			String b=jq.getBans();
			String c=jq.getCans();
			qus.add(new Question(ct,id,cont,sub,ta,a,b,c,null));
			ct++;
		}
		em.close();
		return qus;//返回查询到的试题。
	}
	public void edit(Test t){
		//将这个修改的t持久化到数据库中
		em = emf.createEntityManager();
		JpaTest jp=em.find(JpaTest.class,t.getId());
		if(jp==null)
			System.out.println(t.getId()+" 哈哈");
		
		jp.setName(t.getName());//因为jp为空！
		jp.setDeadline(t.getDeadline());
		jp.setSubject(t.getSubject());
		jp.setIntro(t.getIntro());
		JpaPaper jpp=em.find(JpaPaper.class,t.getPaperid());
		jpp.setStatus("T");
		jp.setJpaper(jpp);
		em.merge(jp);
		em.close();//修改持久化到数据库中。
	}
	
	public List<String> submitPaper(List<String> userAns,int userid,int testid){//这里进行判卷操作！。
		//先根据试卷查找到试卷，并且找到其中的试题，并且一一比对。然后在这里对表直接存储分数。
		//直接返回一个如何？直接返回给用户，并且将错题标号出来，并且根据错题标号的长度得出分数！
		em = emf.createEntityManager();
		JpaTest jp=em.find(JpaTest.class,testid);
		JpaUser ju=em.find(JpaUser.class,userid);
		List<String> wrong=new ArrayList<String>();
		List<JpaQus> jq=jp.getJpaper().getJquss();//获取到试卷中的题目
		int ct=0;
		for(JpaQus j:jq){
			if(!j.getTans().equals(userAns.get(ct++))){//这里不能用!=,应该用equals进行字符串的比较！
				wrong.add(ct+"");//那么将它加入进来。
			}
		}
		String w="";
		for(String s:wrong){
			w+=s;//将其转化为string
		}
		//新建一个japusertest对象，将其进行存储。
		JpaUserTest jut=new JpaUserTest();
		Query q = em.createQuery("select u from JpaUserTest u");
		List<?> res = q.getResultList();
		Iterator<?> it = res.iterator();
	
		while (it.hasNext()) {
			jut=(JpaUserTest)it.next();
			if(jut.getJuser().getId()==userid&&jut.getJtest().getId()==testid){
				break;
			}
			
		}
		/*jut.setJuser(ju);
		jut.setJtest(jp);//设置考试。
*/		jut.setWrongno(w);
		jut.setScore(50-wrong.size()*10);	//设置分数
		Date d=new Date();
		java.text.SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");//获取系统时间
        String ctime = sdf.format(d);
		jut.setSubtime(ctime);//设置时间
		
		em.persist(jut);//持久化		
		em.close();//修改持久化到数据库中。
		return wrong;
	}
	
	public String publish(Test t){//发布考试
		em = emf.createEntityManager();
		JpaTest jt=em.find(JpaTest.class,t.getId());
		jt.setStatus("T");//将考试发布。
		em.persist(jt);
		em.close();
		return "success";
	}
	
	public TestManage() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String signup(){//用户报名考试。
		
		return "success";
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
}
