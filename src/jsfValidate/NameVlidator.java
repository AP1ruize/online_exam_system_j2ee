package jsfValidate;

import java.util.ArrayList;
import java.util.Iterator;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import bean.User;

//@FacesValidator("jsfValidate.NameValidator")
public class NameVlidator implements Validator{
	@EJB
	private statelessBean.UserManage um;
	
	private User u;
	public void validate(FacesContext context,UIComponent component,Object obj){
		String name=(String) obj;//强制转换为String类型
		ArrayList<User> list=um.query();
		Iterator<?> it=list.iterator();
		while(it.hasNext()){
			u=(User)it.next();
			if(name==u.getName()){
				FacesMessage message=new FacesMessage(FacesMessage.SEVERITY_ERROR,"This name already exists",
						"This name already exists");
				throw new ValidatorException(message);
			}
		}
	}
}
