package college.assignment.VaadinSurvey;

import java.util.ArrayList;
import java.util.List;

public class Surveys {
	List<SurveyQuestion> surveys = new ArrayList<>();
	
	public Surveys(){
		//Default Survey
		//The question
		SurveyQuestion defaultSurvey= new SurveyQuestion();
		
		defaultSurvey.setQuestion("How do you like to die?");
		//Adding options
		defaultSurvey.getSelections().add("While in Sleep");
		defaultSurvey.getSelections().add("Hanging");
		defaultSurvey.getSelections().add("Fire");
		defaultSurvey.getSelections().add("Water");
		defaultSurvey.getSelections().add("Gun");
		defaultSurvey.getSelections().add("Other");
		//Initialising count
		defaultSurvey.getSelectionCount().add(0);
		defaultSurvey.getSelectionCount().add(0);
		defaultSurvey.getSelectionCount().add(0);
		defaultSurvey.getSelectionCount().add(0);
		defaultSurvey.getSelectionCount().add(0);
		defaultSurvey.getSelectionCount().add(0);
		//add to the survey list
		surveys.add(defaultSurvey);
	}
	
	public List<SurveyQuestion> getSurveys(){
		return this.surveys;
	}
}
