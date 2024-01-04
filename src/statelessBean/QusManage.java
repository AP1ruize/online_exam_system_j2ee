package statelessBean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import bean.JpaQus;
import bean.Question;

@Stateless
@LocalBean
public class QusManage {
	@PersistenceUnit(name="testsys")
	private EntityManagerFactory emf;
	private EntityManager em;
	
	private int id;
	private String content;
	private String subject;
	private String tans;
	private String aans;
	private String bans;
	private String cans;
	private ArrayList<Question> quss=new ArrayList<Question>();
	public QusManage() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String add(Question qus){//添加试题
		em=emf.createEntityManager();
		//em.joinTransaction();
		try{
			JpaQus jqus=new JpaQus();//创建一个实体类进行插入。
			jqus.setContent(qus.getContent());
			jqus.setSubject(qus.getSubject());
			jqus.setTans(qus.getTans());
			jqus.setAans(qus.getAans());
			jqus.setBans(qus.getBans());
			jqus.setCans(qus.getCans());
			jqus.setStatus("F");
			em.persist(jqus);//持久化到数据库中
		}catch(Exception e){
			e.printStackTrace();
		}//关闭事务
		em.close();
		return "success";
	}
	
	
	public String delete(Question q){//删除试题，删除试题时会进行检查，如果已经出现在试卷中，那么就不能删除。
		//同时也从数据库中删除。
		em=emf.createEntityManager();
//		Query qy=em.createQuery("select u from JpaQus u where u.id=:id");//一般jpql中不使用update语句，而是
//		qy.setParameter("id",q.getId());
//		List<?> res=qy.getResultList();
//		Iterator<?> it=res.iterator();//查询到对应的行
//		while(it.hasNext()){
//			
//			
//		}
		JpaQus jq=em.find(JpaQus.class,q.getId());
		//if(jq.getStatus().equals("T"))return "failure";//如果已经被使用则提示不能删除。
		em.remove(jq);//删除操作。
		return "success";
	}
	
	public ArrayList<Question> query(){//按科目查询试题。
		em=emf.createEntityManager();
		Query q=em.createQuery("select u from JpaQus u");
		List<?> res=q.getResultList();
		Iterator<?> it=res.iterator();
		int ct=1;
		while(it.hasNext()){
			JpaQus u=(JpaQus) it.next();
			int id=u.getId();
			String content=u.getContent();
			String subject=u.getSubject();
			String tans=u.getTans();
			String aans=u.getAans();
			String bans=u.getBans();
			String cans=u.getCans();
			String status=u.getStatus();
			quss.add(new Question(ct,id,content,subject,tans,aans,bans,cans,status));
			ct++;
		}
		em.close();
		return quss;
	}
	
	public String saveEdit(ArrayList<Question> editq){
		em=emf.createEntityManager();
		Query q=em.createQuery("select u from JpaQus u where u.id=:id");//一般jpql中不使用update语句，而是
		for(Question no:editq){
			q.setParameter("id",no.getId());
			List<?> res=q.getResultList();
			Iterator<?> it=res.iterator();//查询到对应的行
			while(it.hasNext()){
				JpaQus p=(JpaQus)it.next();
				p.setContent(no.getContent());
				p.setSubject(no.getSubject());
				p.setTans(no.getTans());
				p.setAans(no.getAans());
				p.setBans(no.getBans());
				p.setCans(no.getCans());
				em.merge(p);//将游离状态变为托管状态。
			}
		}
		em.close();//关闭事务。
		return "success";
	}
	/*public ArrayList<Question> queryQus(Paper p){
		String s[]=new String[5];
		for(int i=0;i<5;i++){
			s[i]=p.get
		}
	}*/
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public ArrayList<Question> getQuss() {
		return quss;
	}
	public void setQuss(ArrayList<Question> quss) {
		this.quss = quss;
	}
	
}
