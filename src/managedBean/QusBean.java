package managedBean;

import java.util.ArrayList;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import bean.Question;

@ManagedBean(name="qusBean")
@SessionScoped
public class QusBean {
	@EJB
	private statelessBean.QusManage qm;
	
	private String content;
	private String subject;
	private String tans;
	private String aans;
	private String bans;
	private String cans;
	private Question qus=new Question();
	private ArrayList<Question> quss=new ArrayList<Question>();
	private ArrayList<Question> editq=new ArrayList<Question>();
	public QusBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String add(){//新增试题
		qus.setContent(this.content);
		qus.setSubject(this.subject);
		qus.setTans(this.tans);
		qus.setAans(this.aans);
		qus.setBans(this.bans);
		qus.setCans(this.cans);
		qm.add(qus);
		return "success";
	}
	public String editAction(Question q){
		q.setEditable(true);//使当前试题可以编辑。
		//editNo.add(q.getId());//这些都是修改过的。应该保存整个对象的。
		editq.add(q);
		return null;
	}
	public String saveEdit(){
		for(Question q:quss){
			q.setEditable(false);
		}	
		qm.saveEdit(editq);
		return "success";
	}
	
	public String deleteAction(Question q){//删除试题
		if(q.getStatus().equals("T"))return "failure";
		quss.remove(q);
		qm.delete(q);
		return "success";//如果返回failure，那么就是不能删除。
	}
	
	public ArrayList<Question> query(){//查询所有试题，并显示在表格中，
		quss.clear();
		quss=qm.query();
		return quss;
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
	public ArrayList<Question> getQuss() {
		return quss;
	}
	public void setQuss(ArrayList<Question> quss) {
		this.quss = quss;
	}
	
}
