package jsfValidate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;


@FacesValidator("jsfValidate.AgeValidator")
public class AgeValidator implements Validator{
	public void validate(FacesContext context,UIComponent component,Object obj){
		String age=(String)obj;
		Pattern p=Pattern.compile("[0-9]*");
		Matcher m=p.matcher(age);
		if(!m.matches()){
			FacesMessage message=new FacesMessage(FacesMessage.SEVERITY_ERROR,"Only digits are allowed",
					"Only digits are allowed");
			throw new ValidatorException(message);
		}
		Integer g=new Integer(age);
		if(g<=0||g>150){
			FacesMessage message=new FacesMessage(FacesMessage.SEVERITY_ERROR,"must be between 0-150",
					"must be between 0-150");
			throw new ValidatorException(message);
		}
		
	}
}
