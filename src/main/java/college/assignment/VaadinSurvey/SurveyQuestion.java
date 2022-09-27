package college.assignment.VaadinSurvey;

import java.util.ArrayList;
import java.util.List;

public class SurveyQuestion {
	String question;
	List<String> selections= new ArrayList<>();
	List<Integer> selectionCount= new ArrayList<>();
	
	public SurveyQuestion(){
		
	}
	
	public String getQuestion(){
		return this.question;
	}
	
	public void setQuestion(String q){
		this.question=q;
	}
	
	public List<String> getSelections(){
		return this.selections;
	}
	
	public List<Integer> getSelectionCount(){
		return this.selectionCount;
	}
	
}
