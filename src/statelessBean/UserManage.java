package statelessBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import bean.JpaPaper;
import bean.JpaQus;
import bean.JpaTest;
import bean.JpaUser;
import bean.JpaUserTest;
import bean.Question;
import bean.Test;
import bean.User;
import bean.UserTest;

@Stateless
@LocalBean
public class UserManage {//通过无状态会话bean实现对用户的增、删、改、查
	//首先初始化一个持久化单元作为实体管理器工厂
	@PersistenceUnit(name="testsys")
	private EntityManagerFactory emf;
	private EntityManager em;
	
	private User user;
	private ArrayList<User> list=new ArrayList<User>();//初始化，不然就是空了。
	private Set<String> subs=new HashSet<String>();//存储所有的科目
	private List<Test> tests=new ArrayList<Test>();
	private List<Question> qus=new ArrayList<Question>();
	
	public UserManage(){}
	
	public String insert(User user){//向数据库中添加用户。
		em=emf.createEntityManager();
		try{
			JpaUser juser=new JpaUser();//创建一个实体类进行插入。
			juser.setName(user.getName());
			juser.setPassword(encrypt(user.getPassword()));
			juser.setAge(user.getAge());
			juser.setSex(user.getSex());
			juser.setAnswer(user.getAnswer());
			em.persist(juser);//持久化到数据库中
		}catch(Exception e){
			e.printStackTrace();
		}	
		em.close();
		return "success";//在success页面注册成功，自动跳转到登陆页面。
	}

	public ArrayList<User>  queryAuser(String loginName){//查找是否有当前姓名的用户
		list.clear();
		em=emf.createEntityManager();
		Query q=em.createQuery("select u from JpaUser u where u.name=:name");
		q.setParameter("name",loginName);
		List<?> res=q.getResultList();
		Iterator<?> it=res.iterator();
		while(it.hasNext()){
			JpaUser u=(JpaUser) it.next();
			int id=u.getId();
			String name=u.getName();
			String password=u.getPassword();
			String age=u.getAge();
			String sex=u.getSex();
			String answer=u.getAnswer();
			list.add(new User(id,name,password,age,sex,answer));
		}
		return list;
	}
	
	public ArrayList<User> query(){//查找所有用户。

		em=emf.createEntityManager();
		Query q=em.createQuery("select u from JpaUser u");
		List<?> res=q.getResultList();
		Iterator<?> it=res.iterator();
		while(it.hasNext()){
			JpaUser u=(JpaUser) it.next();
			int id=u.getId();
			String name=u.getName();
			String password=u.getPassword();;
			String age=u.getAge();
			String sex=u.getSex();
			String answer=u.getAnswer();
			list.add(new User(id,name,password,age,sex,answer));
		}
		em.close();
		return list;
	}
	
	public Set<String> querySub(){//查询所有考试的科目。
		em=emf.createEntityManager();
		Query q=em.createQuery("select u from JpaPaper u");
		List<?> res=q.getResultList();
		Iterator<?> it=res.iterator();
		while(it.hasNext()){
			JpaPaper jp=(JpaPaper)it.next();
			subs.add(jp.getSubject());			
		}
		em.close();
		return subs;
	}
	
	public List<Test> queryTest(String sub){
		tests.clear();
		em=emf.createEntityManager();
		Query q=em.createQuery("select u from JpaTest u where u.subject=:subject");
		q.setParameter("subject",sub);
		List<?> res=q.getResultList();
		Iterator<?> it=res.iterator();
		while(it.hasNext()){
			JpaTest jt=(JpaTest)it.next();
			int id=jt.getId();
			String name=jt.getName();
			//获取系统当前的时间，
			String status=jt.getStatus();
			String ddl=jt.getDeadline();
			//应该判断一下，获取当前考试的状态，
			//发布考试之后学生才可以报名，然后等管理员手动地开始考试，才向用户发送消息说开始考试了。
			if(!status.equals("F"))	
				tests.add(new Test(id,name,ddl));
		}
		em.close();
		return tests;
	}
	
	public String  registerExam(int userid,int testid){
		//先查询到user。
		em=emf.createEntityManager();
		JpaUser ju=em.find(JpaUser.class,userid);
		JpaTest jt=em.find(JpaTest.class,testid);
		//在这里首先应该查找一下，用户是否已经注册过了这个考试。如果已经注册过了那么就不让它再注册了。
		Query q=em.createQuery("select u from JpaUserTest u");
		List<?> res=q.getResultList();
		Iterator<?> it=res.iterator();
		while(it.hasNext()){
			JpaUserTest f=(JpaUserTest)it.next();
			if(f.getJtest().getId()==testid&&f.getJuser().getId()==userid){//如果已经报名了这个考试
				return "failure";
			}
		}
		jt.setTotalStu(jt.getTotalStu()+1);
		JpaUserTest jut=new JpaUserTest();
		jut.setJtest(jt);
		jut.setJuser(ju);
		jut.setScore(0);//目前还没有错题，wrongno就不赋值了。
		jut.setWrongno("");
		jut.setSubtime("");
		em.merge(jt);//持久化到数据库中
		em.persist(jut);
		em.close();
		return "success";
	}
	
	public List<Test> queryMyTestName(int userid){//查询所有本用户报名的考试的名字和id.
		tests.clear();
		em=emf.createEntityManager();
		Query q=em.createQuery("select u from JpaUserTest u");
		//不能通过这样来查询。
		//q.setParameter("userid",userid);
		List<?> res=q.getResultList();
		Iterator<?> it=res.iterator();
		while(it.hasNext()){
			JpaUserTest f=(JpaUserTest)it.next();
			if(f.getJuser().getId()==userid){//如果是当前报名的，
				JpaTest jt=f.getJtest();
				int id=jt.getId();
				String name=jt.getName();
				tests.add(new Test(id,name,null));
			}
		}
		em.close();
		return tests;
	}
	
	public List<Question> queryTestCon(int testid){
		qus.clear();
		em=emf.createEntityManager();
		JpaTest jt=em.find(JpaTest.class,testid);
		
		List<JpaQus> jq=jt.getJpaper().getJquss();//获取到试卷中的题目
		int ct=1;
		for(JpaQus j:jq){
			
			int id=j.getId();
			String content=j.getContent();
			String a=j.getAans();
			String b=j.getBans();
			String c=j.getCans();
			qus.add(new Question(ct,id,content,null,null,a,b,c,null));
			ct++;
		}
		em.close();
		return qus;
	}
	
	public int collect(int userid,ArrayList<Integer> paperids){
		int ct=0;//已经收藏的试卷的个数
		em=emf.createEntityManager();
		JpaUser ju=em.find(JpaUser.class,userid);//获取到用户
		//将用户已经收藏的试题放到一个set里
		List<JpaQus> has=ju.getJquss();//获取到用户原有的这个收藏的试题。
		Set<Integer> set=new HashSet<Integer>();
		for(JpaQus h:has){
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
	
	public String isBegin(int testid){//获取考试的状态，确定当前考试是否开始。
		em=emf.createEntityManager();
		JpaTest ju=em.find(JpaTest.class,testid);//获取到用户		
		em.close();
		return ju.getStatus();
	}
	
	public List<UserTest> checkScore(int userid){//查看分数。
		List<UserTest> ut=new ArrayList<UserTest>();
		em=emf.createEntityManager();
		Query q=em.createQuery("select u from JpaUserTest u");
		ut.clear();
		//不能通过这样来查询。
		//q.setParameter("userid",userid);
		List<?> res=q.getResultList();
		Iterator<?> it=res.iterator();
		//在这里查到了所有的用户之后进行一个排名。相同分数的排名相同。
		List<JpaUserTest> list=new ArrayList<JpaUserTest>();	
		while(it.hasNext()){
			list.add((JpaUserTest)it.next());//获取到下一个对象。
		}
		res=q.getResultList();//再次查询遍历。
		it=res.iterator();
		while(it.hasNext()){
			JpaUserTest jut=(JpaUserTest)it.next();
			if(jut.getJuser().getId()==userid){		//这里查询到的是用户参加的考试。
				int id=jut.getId();
				int testid=jut.getJtest().getId();
				String name=jut.getJtest().getName();
				//在这里获取到test的id.进行查询。
				int rank=0;
				if(!jut.getSubtime().equals("")){//如果用户参加了考试，那么才对它进行排序。
					List<JpaUserTest> lt=new ArrayList<JpaUserTest>();
					for(JpaUserTest jj:list){
						if(jj.getJtest().getId()==testid){
							lt.add(jj);
						}
					}
					Collections.sort(lt,new Comparator<JpaUserTest>(){
						@Override
						public int compare(JpaUserTest j1,JpaUserTest j2){
							int s1=j1.getScore();
							int s2=j2.getScore();
							if(s1<s2) return 1;//从大到小排序。
							else return -1;
						}
					});
					//再对lt中的进行判断，分数相同的rank应该相同
					int ct=1,rk=1;
					rank=rk;
					for(int i=1;i<lt.size();i++){
						if(lt.get(i).getScore()==lt.get(i-1).getScore()){//如果这两个的分数相同，那么
							rank=rk;ct++;
						}
						else{
							ct++;
							rk=ct;
							rank=rk;
						}
						if(lt.get(i).getJuser().getId()==userid)break;
					}
				}
				
				int score=jut.getScore();
				int total=jut.getJtest().getTotalStu();
				String subtime=jut.getSubtime();
				String wrongno=jut.getWrongno();
				ut.add(new UserTest(id,name,score,rank,total,subtime,wrongno));
			}
		}		
		em.close();
		return ut;
	}
	public String hasDone(int userid,int testid){
		em=emf.createEntityManager();
		Query q=em.createQuery("select u from JpaUserTest u");
		List<?> res=q.getResultList();
		Iterator<?> it=res.iterator();
		String status="notyet";
		while(it.hasNext()){
			JpaUserTest jut=(JpaUserTest)it.next();
			if(jut.getJuser().getId()==userid&&jut.getJtest().getId()==testid){		
				if(!jut.getSubtime().equals("")){//如果提交时间
					status="hasdone";break;
				}
			}
		}
		em.close();
		return status;
	}
	
	public void changePass(int userid,String password){
		em=emf.createEntityManager();
		JpaUser ju=em.find(JpaUser.class,userid);
		ju.setPassword(encrypt(password));
		em.merge(ju);
		em.close();
	}
	
	public void saveEdit(User u){
		em=emf.createEntityManager();
		JpaUser ju=em.find(JpaUser.class,u.getId());
		ju.setAge(u.getAge());
		ju.setSex(u.getSex());
		em.merge(ju);
		em.close();
		
	}
	public String encrypt(String pass){//密码加密
		 MessageDigest md5 = null;
	        try {
	            md5 = MessageDigest.getInstance("MD5");
	        } catch (Exception e) {
	            System.out.println(e.toString());
	            e.printStackTrace();
	            return "";
	        }
	        StringBuffer hexValue= new StringBuffer();
	        try{
	        	byte[] byteArray = pass.getBytes("UTF-8");
		        byte[] md5Bytes = md5.digest(byteArray);
		        for (int i = 0; i < md5Bytes.length; i++) {
		            int val = ((int) md5Bytes[i]) & 0xff;
		            if (val < 16) {
		                hexValue.append("0");
		            }
		            hexValue.append(Integer.toHexString(val));
		        }
	        }catch(UnsupportedEncodingException e){
	        	System.out.println(e.toString());
	        	e.printStackTrace();
	        }    
	        return hexValue.toString();
	}
	public String editInfo(){//修改个人信息，包括年龄和性别。
		return "success";
	}
	public String changePass(){//用于修改密码或者找回密码时调用
		return "success";
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
}
