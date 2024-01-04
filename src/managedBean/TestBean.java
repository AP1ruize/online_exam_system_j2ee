package managedBean;

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

import bean.Paper;
import bean.Question;
import bean.Test;

@ManagedBean(name="testBean")
@SessionScoped
public class TestBean {
	@EJB
	private statelessBean.TestManage tm;
	
	private int id;
	private String name;
	private String deadline;
	private String subject;
	private String intro;
	private int totalStu;
	private String status;
	private int paperid;//选择试卷的id
	private Set<String> subs=new HashSet<String>();//存储所有的科目
	private Map<String,String> itemSub=new LinkedHashMap<String,String>();
	private List<Paper> pp=new ArrayList<Paper>();//按科目查到的试卷
	private Map<String,Integer> itemPp=new LinkedHashMap<String,Integer>();//试卷id与name的映射。
	private List<Test> alltt=new ArrayList<Test>();
	private List<Question> qus=new ArrayList<Question>();
	
	public void queryAllSub(){//查询所有的试卷的科目。
		itemSub.clear();
		subs=tm.queryAllSub();
		for(String s:subs){
			itemSub.put(s,s);//label, value
		}
		return ;
	}
	public void queryPp(ValueChangeEvent e){//根据科目查找对应的试卷。
		itemPp.clear();
		if(e.getNewValue()==null)return ;
		this.subject=e.getNewValue().toString();//获取到对象值
		pp=tm.queryPp(this.subject);
		for(Paper p:pp){
			itemPp.put(p.getName(),p.getId());
		}
		return ;
	}
	
	public String add(){//新增考试
		Test t=new Test();
		t.setName(this.name);
		t.setDeadline(this.deadline);
		t.setSubject(this.subject);
		t.setStatus("F");
		t.setIntro(this.intro);
		t.setPaperid(this.paperid);
		tm.add(t);//添加到数据库中。
		return "success";
	}
	public void queryAllTest(){//查询数据库中所有的考试
		alltt=tm.queryAllTest();		
	}
	
	public String editAction(){
		//在这里需要加载,根据id在数据库中查找，然后给组件中的属性赋值，显示到组件中。
		Test t=tm.queryById(this.id);
		if(t.getStatus().equals("T")){
			return "failureSta";//如果已经发布了考试，那么就不能删除了。
		}
		//this.id=t.getId();
		System.out.println(this.id);//这里打印出要修改的id!
		
		this.name=t.getName();
		this.subject=t.getSubject();
		this.deadline=t.getDeadline();
		this.intro=t.getIntro();
		//this.totalStu=t.getTotalStu();
		this.paperid=t.getPaperid();//获取到试卷的id.
		
		return "success";
	}
	
	public String saveEdit(){
		Test t=new Test();		
		t.setId(this.id);
		t.setName(this.name);//这里都可以获取到值得。
		t.setDeadline(this.deadline);
		t.setSubject(this.subject);
		t.setStatus("F");
		t.setIntro(this.intro);
		t.setPaperid(this.paperid);
		tm.edit(t);//添加到数据库中。
		return "success";
	}
	
	public String delete(Test t){
		if(t.getStatus().equals("T"))
			return "deletePaperError";
		alltt.remove(t);//先将其从列表中删除。
		tm.delete(t);//再将其从数据库中删除。
		return "success";
	}
	
	public String scan(Test t){//浏览考试中的试题。
		//主要是查询考试所用的试卷中的试题。放到一个list里，然后显示就可以了，比较简单。
		this.name=t.getName();
		this.subject=t.getSubject();
		this.deadline=t.getDeadline();
		qus=tm.queryQus(t);		
		return "scan";
	}
	
	public String publish(Test t){//发布这个考试
		tm.publish(t);//就是改变它的状态。		
		return "successPublish";
	}
	public TestBean() {
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

	public Map<String, String> getItemSub() {
		return itemSub;
	}

	public void setItemSub(Map<String, String> itemSub) {
		this.itemSub = itemSub;
	}
	public Map<String, Integer> getItemPp() {
		return itemPp;
	}
	public void setItemPp(Map<String, Integer> itemPp) {
		this.itemPp = itemPp;
	}
	public List<Test> getAlltt() {
		return alltt;
	}
	public void setAlltt(List<Test> alltt) {
		this.alltt = alltt;
	}
	public List<Question> getQus() {
		return qus;
	}
	public void setQus(List<Question> qus) {
		this.qus = qus;
	}
	
	
	
	
}
