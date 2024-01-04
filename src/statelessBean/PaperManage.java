package statelessBean;

import java.util.ArrayList;
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
import bean.Paper;
import bean.Question;

@Stateless
@LocalBean
public class PaperManage {
	@PersistenceUnit(name = "testsys")
	private EntityManagerFactory emf;
	private EntityManager em;
	private int id;

	private String subject;
	private String date;
	private int totalScore;
	private String status;
	private ArrayList<Question> qus = new ArrayList<Question>();
	private Set<String> subs = new HashSet<String>();
	private ArrayList<Paper> paper = new ArrayList<Paper>();
	private ArrayList<Paper> allpp = new ArrayList<Paper>();

	public PaperManage() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Set<String> queryAllSub() {// 查询到所有的科目。
		em = emf.createEntityManager();
		Query q = em.createQuery("select u from JpaQus u");
		List<?> res = q.getResultList();
		Iterator<?> it = res.iterator();
		while (it.hasNext()) {
			JpaQus jq = (JpaQus) it.next();
			subs.add(jq.getSubject());
		}
		em.close();
		return subs;
	}

	public ArrayList<Question> queryBySub(String sub) {
		qus.clear();
		em = emf.createEntityManager();
		Query q = em
				.createQuery("select u from JpaQus u where u.subject=:subject");
		q.setParameter("subject", sub);
		List<?> res = q.getResultList();
		Iterator<?> it = res.iterator();
		int ct = 1;
		while (it.hasNext()) {
			JpaQus u = (JpaQus) it.next();
			int id = u.getId();
			String content = u.getContent();
			String subject = u.getSubject();
			String tans = u.getTans();
			String aans = u.getAans();
			String bans = u.getBans();
			String cans = u.getCans();
			String status = u.getStatus();
			qus.add(new Question(ct, id, content, subject, tans, aans, bans,
					cans, status));
			ct++;
		}
		em.close();
		return qus;
	}

	public void add(String[] paperids, Paper pp) {
		qus.clear();
		// 首先根据试题id将试题加载出来。
		JpaPaper jp = new JpaPaper();
		em = emf.createEntityManager();
		
		jp.setJquss(query5Qus(paperids));//调用查询，然后持久化。
		//这里需要将试题的状态设置为T，即不能删除。
		
		jp.setName(pp.getName());
		jp.setDate(pp.getDate());
		jp.setSubject(pp.getSubject());
		jp.setStatus(pp.getStatus());
		em.persist(jp);// 持久化到数据库中，
		em.close();
		return;
	}

	private ArrayList<JpaQus> query5Qus(String[] paperids){
		ArrayList<JpaQus> qa=new ArrayList<JpaQus>();
		/*em = emf.createEntityManager();
		em.joinTransaction();//因为之前正在进行插入或者更新。
*/		Query q = em.createQuery("select u from JpaQus u where u.id=:id");
		for (String id : paperids) {
			q.setParameter("id", Integer.parseInt(id));// 这里要根据string创建大数。
			List<?> res = q.getResultList();
			Iterator<?> it = res.iterator();
			while (it.hasNext()) {
				JpaQus j=(JpaQus)it.next();
				j.setStatus("T");
				qa.add(j);//根据id,查询到所有的试题。
			}
		}
		return qa;
	}
	public ArrayList<Paper> queryAllpp() {//查询所有的试卷。
		em = emf.createEntityManager();
		Query q = em.createQuery("select u from JpaPaper u");
		List<?> res = q.getResultList();
		Iterator<?> it = res.iterator();
		int ct = 1;
		while (it.hasNext()) {
			JpaPaper pp = (JpaPaper) it.next();// 这里必须使用jpa实体类吗？可以使用java
												// bean在这里强制转换，直接加入吗？
			int id = pp.getId();
			String subject = pp.getSubject();
			String date = pp.getDate();
			String status = pp.getStatus();
			String name = pp.getName();
			List<JpaQus> qus=pp.getJquss();//获取到所存储的试题。
			ArrayList<Question> qu=new ArrayList<Question>();
			for(JpaQus jj:qus){
				Question qt=new Question();
				qt.setContent(jj.getContent());
				qt.setSubject(jj.getSubject());
				qt.setAans(jj.getAans());
				qt.setBans(jj.getBans());
				qt.setCans(jj.getCans());
				qu.add(qt);//添加到试卷里。
				//现在每个查询到的试卷中，都存储了对应的试题。
				//但是如果一开始保存的话，后来如果修改了，那和数据库不一致啊，所以还是需要点击链接之后，在
				//
			}
			allpp.add(new Paper(ct, id, subject, date, status, name));// 将所有的放进来。
			ct++;
		}
		em.close();
		return allpp;
	}

	public Paper queryById(int id){
		em=emf.createEntityManager();
		Query q=em.createQuery("select u from JpaPaper u where u.id=:id");
			q.setParameter("id",id);//这里要根据string创建大数。
			List<?> res=q.getResultList();
			Iterator<?> it=res.iterator();
			Paper p=new Paper();
			while(it.hasNext()){
				JpaPaper jq=(JpaPaper)it.next();
				p.setId(jq.getId());
				p.setName(jq.getName());
				p.setSubject(jq.getSubject());
				p.setDate(jq.getDate());
				p.setStatus(jq.getStatus());
				break;
			}
		em.close();
		return p;
	}

	public String edit(String[] paperids, Paper pp) {// 编辑试卷
		em=emf.createEntityManager();
		Query q=em.createQuery("select u from JpaPaper u where u.id=:id");
			q.setParameter("id",pp.getId());//这里要根据string创建大数。
			List<?> res=q.getResultList();
			Iterator<?> it=res.iterator();
			
			while(it.hasNext()){
				JpaPaper jp=(JpaPaper)it.next();
				jp.setName(pp.getName());
				jp.setSubject(pp.getSubject());//将修改的基本信息存储，并且将对应存储的题目修改
				
				jp.setJquss(query5Qus(paperids));//设置新试卷。
				
				em.merge(jp);//到数据库中。
			}
		em.close();
		return "success";
	}

	public String delete(Paper p) {// 删除试卷，如果试卷已经使用或者被收藏，那么就不能删除。
		em=emf.createEntityManager();
		JpaPaper jp=em.find(JpaPaper.class,p.getId());
		//if(jq.getStatus().equals("T"))return "failure";//如果已经被使用则提示不能删除。
		em.remove(jp);//删除操作。
		em.close();
		return "success";
	}
	
	public ArrayList<Question> queryQus(Paper p){
		em=emf.createEntityManager();
		JpaPaper jp=em.find(JpaPaper.class,p.getId());
		ArrayList<Question> qt=new ArrayList<Question>();
		List<JpaQus> jpq=new ArrayList<JpaQus>();//获取其中的试卷。
		jpq=jp.getJquss();//指向
		int ct=1;
		for(JpaQus jj:jpq){
			int id=jj.getId();
			String sub=jj.getSubject();
			String con=jj.getContent();
			String A=jj.getAans();
			String B=jj.getBans();
			String C=jj.getCans();
			qt.add(new Question(ct,id,con,sub,null,A,B,C,null));//将不需要的信息直接赋值为0或者null.
			ct++;
		}
		em.close();
		return qt;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ArrayList<Question> checkPaper() {// 查看某一试卷中的题目。
		return qus;
	}

	public ArrayList<Paper> query() {// 查看所有试卷。
		return paper;
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

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
