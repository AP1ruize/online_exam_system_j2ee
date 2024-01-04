package statefulBean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import bean.JpaQus;
import bean.JpaUser;
import bean.Question;

@Stateful
@LocalBean
public class CollectManage {
	
	@PersistenceUnit(name="testsys")
	private EntityManagerFactory emf;
	private EntityManager em;
	private int id;
	private int userid;
	private int paperid;
	private List<Question> qus=new ArrayList<Question>();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int collect(int userid,ArrayList<Integer> paperids){//用户收藏试卷
		int ct=0;//已经收藏的试卷的个数
		em=emf.createEntityManager();
		JpaUser ju=em.find(JpaUser.class,userid);//获取到用户
		//将用户已经收藏的试题放到一个set里
		List<JpaQus> has=ju.getJquss();//获取到用户原有的这个收藏的试题。
		Set<Integer> set=new HashSet<Integer>();
		for(JpaQus h:has){
			h.setStatus("T");//将状态设置为T，表示不能删除，
			set.add(h.getId());
		}		
		List<JpaQus> jqq=new ArrayList<JpaQus>();
		for(Integer id:paperids){
			if(set.contains(id)){
				ct++;//如果用户已经收藏过本试题，那么就记录一下。
			}else{
				JpaQus jq=em.find(JpaQus.class,id);//找到相应的试题
				jqq.add(jq);
			}
		}		
		has.addAll(jqq);//将所有的再添加进来。
		ju.setJquss(has);//将试题设置为jqq，如果这样的话，那用户原来收藏的试题可就没了。。。
		em.merge(ju);//持久化到数据中
		em.close();
		return ct;
	}
	
	public List<Question> getCollection(int userid){
		qus.clear();
		em=emf.createEntityManager();
		JpaUser ju=em.find(JpaUser.class,userid);//获取到用户
		List<JpaQus> jq=ju.getJquss();
		int ct=1;
		for(JpaQus j:jq){//查询到用户收藏的试题。
			int id=j.getId();
			String con=j.getContent();
			String sub=j.getSubject();
			String a=j.getAans();
			String b=j.getBans();
			String c=j.getCans();
			qus.add(new Question(ct,id,con,sub,null,a,b,c,null));//状态为null，就可以。
			ct++;
		}
		em.close();
		return qus;
	}
	
	public void cancelCollect(int userid,Question q){
		//先查询到这个用户。
		em=emf.createEntityManager();
		JpaUser ju=em.find(JpaUser.class,userid);//获取到用户
		List<JpaQus> jq=ju.getJquss();
		for(JpaQus j:jq){//查询到用户收藏的试题。			
			if(j.getId()==q.getId()){
				jq.remove(j);//从其删除。
				break;
			}			
		}
		ju.setJquss(jq);
		em.merge(ju);
		em.close();
	}
	
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public int getPaperid() {
		return paperid;
	}
	public void setPaperid(int paperid) {
		this.paperid = paperid;
	}
}
