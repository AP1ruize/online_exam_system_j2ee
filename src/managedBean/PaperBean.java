package managedBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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


@ManagedBean(name="paperBean")
@SessionScoped
public class PaperBean{
	@EJB
	private statelessBean.PaperManage pm;
	
	private int id;
	private String name;
	private String subject;
	private String date;
	private String status;
	private Map<String,String> itemSubject=new LinkedHashMap<String,String>();
	private Set<String> subs=new HashSet<String>();
	private ArrayList<Question> qus=new ArrayList<Question>();//存储特定科目的试题。
	private Map<String,String> itemQus=new LinkedHashMap<String,String>();
	private List<Paper> allpp=new ArrayList<Paper>();
	private Paper editp=new Paper();//先存储需要更改的对象，
	
	private String[] paperids;//存储选中题目的id.
	
	public PaperBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public void queryBySub(ValueChangeEvent e){//按科目查询试题！,将返回类型改为了void
		qus.clear();
		itemQus.clear();//清空
		if(e.getNewValue()==null)return ;
		this.subject=e.getNewValue().toString();//加上这一句才可以获取到组件的值。
		qus=pm.queryBySub(this.subject);//现在qus里已经保存了特定科目的试题了。
		System.out.println(this.subject+"按科目查询！");
		for(Question q:qus){
			itemQus.put(q.getContent(),q.getId()+"");//为什么不显示content呢？
			//label,value
		}
		return ;
	}
	
	public void queryAllSub(ValueChangeEvent e){//查找所有的科目！
		itemSubject.clear();//清空
		subs=pm.queryAllSub();	
		//将所有查询到的科目放到item中进行显示。
		for(String s:subs){
			itemSubject.put(s,s);//都是科目。
		}
		return ;
	}
	public String add(){
		//首先应该检查是否是选了5道题
		if(paperids.length==5){
			//将整个创建为一个对象。
			Paper pp=new Paper();
			pp.setSubject(this.subject);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
			pp.setDate(df.format(new Date()));
			pp.setStatus("F");//这时将所有的添加的试题状态设置为T
			pp.setName(this.name);
			pm.add(paperids,pp);
			return "success";
		}else
			return "failure";
	}
	
	public void queryAllpp(){//查询所有的试卷。
		allpp.clear();
		allpp=pm.queryAllpp();		
		return ;
	}
	
	public String editAction(){//将目前更改的对象先获取，便于更改保存到数据库中。
		editp=pm.queryById(this.id);		
		if(editp.getStatus().equals("T"))//如果试卷已经被考试正在使用，那么就不能更改了。
			return "failureStatus";
		this.name=editp.getName();
		this.subject=editp.getSubject();
		this.date=editp.getDate();
		this.status=editp.getStatus();
		return "success";
	}
	public String edit(){
		if(paperids.length==5){
			//将整个创建为一个对象。
			Paper pp=new Paper();
			pp.setSubject(this.subject);
			pp.setName(this.name);
			pp.setId(this.id);
			pm.edit(paperids,pp);
			return "success";
		}else
			return "failure";
	}
	public String scan(Paper p){
		//查找试卷内所有的试题。
		this.name=p.getName();
		qus.clear();
		qus=pm.queryQus(p);//现在qus里存好了本试卷的试题了。	
		return "scansuc";
	}
	
	public String delete(Paper p){//点击超链接删除试卷。
		if(p.getStatus().equals("T"))
			return "inuse";
		allpp.remove(p);//首先从表格里删除。
		pm.delete(p);	
		return "nouse";
	}
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, String> getItemSubject() {
		return itemSubject;
	}

	public void setItemSubject(LinkedHashMap<String, String> itemSubject) {
		this.itemSubject = itemSubject;
	}


	public ArrayList<Question> getQus() {
		return qus;
	}


	public void setQus(ArrayList<Question> qus) {
		this.qus = qus;
	}


	public String[] getPaperids() {
		return paperids;
	}


	public void setPaperids(String[] paperids) {
		this.paperids = paperids;
	}


	public Map<String, String> getItemQus() {
		return itemQus;
	}


	public void setItemQus(LinkedHashMap<String, String> itemQus) {
		this.itemQus = itemQus;
	}

	public List<Paper> getAllpp() {
		return allpp;
	}

	public void setAllpp(List<Paper> allpp) {
		this.allpp = allpp;
	}

	public Paper getEditp() {
		return editp;
	}

	public void setEditp(Paper editp) {
		this.editp = editp;
	}
	
}
