package jsfValidate;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("jsfValidate.PWValidator")
public class PWValidator implements Validator{
	public void validate(FacesContext context,UIComponent component,Object obj){
		String pass=(String) obj;//强制转换为String类型
		if(pass.length()<=8){
			FacesMessage message=new FacesMessage(FacesMessage.SEVERITY_ERROR,"should not be less than 8 bits.",
					"Password length should not be less than 8 bits.");
			throw new ValidatorException(message);//就在此处抛出异常。
		}
		if(!pass.matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$")){
			FacesMessage message=new FacesMessage(FacesMessage.SEVERITY_ERROR,"must consist of letters and numbers",
					"The password must consist of letters and numbers");
			throw new ValidatorException(message);
		}
		
	}
	
}
