package college.assignment.VaadinSurvey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.swing.plaf.basic.BasicBorders.RadioButtonBorder;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.renderers.ButtonRenderer;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
@Push
public class MyUI extends UI implements Broadcaster.BroadcastListener {

	enum UIState{HomePage, SurveyShowPage, Default};
	UIState mCurrentStage=UIState.Default;
	static Surveys mSurveys = new Surveys();
	boolean mIssubmitted=false;
	static int	mCurrentIndex;
	
	
    
	//Components
	//final VerticalLayout mLayout = new VerticalLayout();    
    //final Label mTitle = new Label();
    //final OptionGroup mOptionsGroup = new OptionGroup(mSQ.getQuestion(), mSQ.getSelections());
    //final Button mBSubmit = new Button("Submit");
    //final Grid mGrid = new Grid();
    //final Label mQuestion = new Label();
    
    @Override
    protected void init(VaadinRequest vaadinRequest) {
    	// Register to receive broadcasts
        Broadcaster.register(this);
    	layoutHome();
    }
    
    private void layoutHome(){
    	mCurrentStage=UIState.HomePage;
    	Label lTitle = new Label();    	
    	lTitle.setCaption("Please Select Any Survey");
    	
    	Grid gSurveys = new Grid();
    	gSurveys.addColumn("Survey No", Integer.class);
    	gSurveys.addColumn("Survey Question", String.class);
    	gSurveys.addColumn("submitted", String.class);
    	gSurveys.addColumn("Show", String.class);
    	List<Integer> lSubmitted = (List<Integer>) getSession().getAttribute("SubmittedList");
    	for (int i = 0; i < mSurveys.getSurveys().size(); i++) {
    		final int k=i;
			String sSubmitted="No";
			if(lSubmitted!=null){
				for (int j = 0; j < lSubmitted.size(); j++) {
					if(lSubmitted.get(j)==i){
						sSubmitted="Yes";
						break;
					}
				}
			}
			//Show button
			Button bShow = new Button();
			bShow.setCaption("Show");
			bShow.addClickListener( e -> {
				//Showing that particular Survey
				layoutShowSurvey(k);
			});
			
			//Add the row			
			gSurveys.addRow(i+1,mSurveys.getSurveys().get(i).getQuestion(),sSubmitted,"Show");
		}
    
    	gSurveys.getColumn("Show").setRenderer(new ButtonRenderer(e -> {
    		int index = Integer.parseInt(e.getItemId().toString());
    		if(index>0){
    			layoutShowSurvey(--index);
    		}
    	}));
    	
    	Button bAddSurvey = new Button();
    	bAddSurvey.setCaption("Add Survey");
    	bAddSurvey.addClickListener( e -> {
    		layoutLogin();
    	});
    	
    	VerticalLayout vLayout = new VerticalLayout();
    	
    	vLayout.addComponents(lTitle,gSurveys,bAddSurvey);
        vLayout.setMargin(true);
        vLayout.setSpacing(true);
        
        setContent(vLayout);
    	
    	
    }

    private void layoutLogin(){
    	mCurrentStage=UIState.Default;
    	//Home
    	Button bHome = new Button();
    	bHome.setCaption("Home");
    	bHome.addClickListener( e -> {
    		layoutHome();
    	});
    	
    	TextField tUsername = new TextField("Username");    	
    	PasswordField pPassword = new PasswordField(); 
    	Button bLogin = new Button();
    	bLogin.setCaption("Login");
    	bLogin.addClickListener( e -> {
    		if(tUsername.getValue().equalsIgnoreCase("admin") && pPassword.getValue().equalsIgnoreCase("admin")){
    			layoutAddSurvey();
    		}else{
    			Notification n = new Notification("Login Erro");
    			n.show("Username or Password is incorrect!");
    		}
    	});
    	
    	VerticalLayout vLayout = new VerticalLayout();
    	
    	vLayout.addComponents(bHome, tUsername, pPassword, bLogin);
        vLayout.setMargin(true);
        vLayout.setSpacing(true);
        
        setContent(vLayout);
    	
    }
    private void layoutAddSurvey(){
    	mCurrentStage=UIState.Default;
    	//Home
    	Button bHome = new Button();
    	bHome.setCaption("Home");
    	bHome.addClickListener( e -> {
    		layoutHome();
    	});
    	
    	TextField tQuestion = new TextField("Please Type Your Question Here");
    	String sQuestion=(String) getSession().getAttribute("Question");
    	if(sQuestion!=null){
    		tQuestion.setValue(sQuestion);
    	}
    	
    	Grid gAnswers = new Grid();
    	gAnswers.addColumn("No", Integer.class);
    	gAnswers.addColumn("Answer Selection", String.class);
    	gAnswers.addColumn("Remove", String.class);
    	TextField tAnswer = new TextField("Type Answer Here");
    	final List<String> lsAnswers=(List<String>) getSession().getAttribute("SelectionList");
    	if(lsAnswers!=null){
	    	for (int i = 0; i < lsAnswers.size(); i++) {
	    		gAnswers.addRow(i+1, lsAnswers.get(i), "Remove");	    		
			}
    	}    	
    	gAnswers.getColumn("Remove").setRenderer(new ButtonRenderer(e -> {
    		int index = Integer.parseInt(e.getItemId().toString());
    		if(index>0){
    			if(lsAnswers!=null){  
    				lsAnswers.remove(--index);    			
    			}
    			
    			layoutAddSurvey();
    		}
    	}));
    	
    	Button bAddAnswer = new Button();
    	bAddAnswer.setCaption("Add Answer");
    	bAddAnswer.addClickListener( e -> {
    		if(tAnswer.getValue()!=null && !tAnswer.getValue().trim().equals("")){
    			if(lsAnswers!=null){
	    			lsAnswers.add(tAnswer.getValue());	    			
	    			getSession().setAttribute("SelectionList", lsAnswers);
    			}else{
    				List<String> svAns = new ArrayList<>();
    				svAns.add(tAnswer.getValue());
    				getSession().setAttribute("SelectionList", svAns);
    			}
    			
    			if(tQuestion.getValue()!=null){
    				getSession().setAttribute("Question", tQuestion.getValue().trim());    				
    			}
    			layoutAddSurvey();
    		}
    		
    	});
    	
    	
    	Button bCreateSurvey = new Button();
    	bCreateSurvey.setCaption("Create Survey");
    	bCreateSurvey.addClickListener( e -> {
    		if(lsAnswers.size()>0 && tQuestion.getValue()!=null && !tQuestion.getValue().trim().equals("")){    			
    			SurveyQuestion sq = new SurveyQuestion();
    			sq.setQuestion(tQuestion.getValue().trim());    			
    			for (int i = 0; i < lsAnswers.size(); i++) {
					sq.getSelections().add(lsAnswers.get(i));
					sq.getSelectionCount().add(0);
				}    	
    			//Add to all survey
    			mSurveys.getSurveys().add(sq);
    			//Empty Question and Answers
    			getSession().setAttribute("Question", "");  
    			getSession().setAttribute("SelectionList", null);
    			layoutHome();
    			
    			//Broadcast send
    			Broadcaster.broadcast("");
    		}else{
    			Notification n = new Notification("");
    			n.show("Please Add atleast one Question and Answer.");
    		}
    		
    	});
    	
    	VerticalLayout vLayout = new VerticalLayout();
    	
    	vLayout.addComponents(bHome, tQuestion, gAnswers, tAnswer, bAddAnswer, bCreateSurvey);
        vLayout.setMargin(true);
        vLayout.setSpacing(true);
        
        setContent(vLayout);
    }
    


    private void layoutShowSurvey(int index){
    	mCurrentIndex=index;
    	//Home
    	Button bHome = new Button();
    	bHome.setCaption("Home");
    	bHome.addClickListener( e -> {
    		layoutHome();
    	});
    	//Checks if the user has already submitted the survey
    	List<Integer> lSubmitted = (List<Integer>) getSession().getAttribute("SubmittedList");
    	boolean found =false;    	
    	if(lSubmitted!=null){
	    	for (int i = 0; i < lSubmitted.size(); i++) {
				if(lSubmitted.get(i)==index){
					found=true;
					break;
				}
			}
    	}
    	
    	if(!found){
    		mCurrentStage=UIState.Default;
    		Label lTitle = new Label();    	
    		lTitle.setCaption("Please Select you Option.");
    		OptionGroup ogAnswers = new OptionGroup(mSurveys.getSurveys().get(index).getQuestion(), mSurveys.getSurveys().get(index).getSelections());
    		TextField tAnswer = new TextField("Your Customer Answer");
    		Button bSubmit = new Button();
    		bSubmit.setCaption("Submit");
    		bSubmit.addClickListener( e -> {
    			if(ogAnswers.getValue()!=null){
    				for (int i = 0; i < mSurveys.getSurveys().get(index).getSelections().size(); i++) {
						if( mSurveys.getSurveys().get(index).getSelections().get(i).equalsIgnoreCase(ogAnswers.getValue().toString())){
							mSurveys.getSurveys().get(index).getSelectionCount().set(i,mSurveys.getSurveys().get(index).getSelectionCount().get(i)+1);
							if(lSubmitted!=null){
								lSubmitted.add(index);
								getSession().setAttribute("SubmittedList", lSubmitted);
							}else{
								List<Integer> ls = new ArrayList<>();
								ls.add(index);
								getSession().setAttribute("SubmittedList", ls);
							}
							
						}
					}
    				
    				Map<Integer,String> custAnswers=(Map<Integer,String>) getSession().getAttribute("CustomAnswers");
    				if(custAnswers!=null){
    					custAnswers.put(index, tAnswer.getValue());
    					getSession().setAttribute("CustomAnswers", custAnswers);
    				}else{
    					custAnswers = new HashMap<Integer,String>();
    					custAnswers.put(index, tAnswer.getValue());
    					getSession().setAttribute("CustomAnswers", custAnswers);
    				}
    					
    				layoutShowSurvey(index);
    				//Broadcast send
        			Broadcaster.broadcast("");
    			}
        	});
    		
    		VerticalLayout vLayout = new VerticalLayout();
        	
        	vLayout.addComponents(bHome, lTitle, ogAnswers, tAnswer, bSubmit);
            vLayout.setMargin(true);
            vLayout.setSpacing(true);
            
            setContent(vLayout);
            
    	}else{
    		mCurrentStage=UIState.SurveyShowPage;
    		Label lTitle = new Label();
    		lTitle.setCaption("Survey No "+(index+1)+" Result.");
    		Label lQuestion = new Label();
    		lQuestion.setCaption(mSurveys.getSurveys().get(index).getQuestion());
    		Grid gAnswers = new Grid();
    		gAnswers.addColumn("Serial No", Integer.class);
    		gAnswers.addColumn("Selections", String.class);
    		gAnswers.addColumn("Choice Count", Integer.class);
    		for (int i = 0; i <mSurveys.getSurveys().get(index).getSelections().size(); i++) {
    			gAnswers.addRow(i,mSurveys.getSurveys().get(index).getSelections().get(i),mSurveys.getSurveys().get(index).getSelectionCount().get(i));
    		}
    		
    		Label lCustomAnswer = new Label();
    		Map<Integer,String> custAnswers=(Map<Integer,String>) getSession().getAttribute("CustomAnswers");
			if(custAnswers!=null){
				String customA=custAnswers.get(index);
				if(customA!=null){
					lCustomAnswer.setCaption("Your Custom Answer is : "+customA);
				}								
			}
			
    		
    		
    		VerticalLayout vLayout = new VerticalLayout();
        	
        	vLayout.addComponents(bHome, lTitle, lQuestion, gAnswers, lCustomAnswer);
            vLayout.setMargin(true);
            vLayout.setSpacing(true);
            
            setContent(vLayout);
    	}
    }
    

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }


    //Unregister when the UI expires
    @Override
    public void detach() {
        Broadcaster.unregister(this);
        super.detach();
    }
    
	@Override
	public void receiveBroadcast(String message) {		
		if(mCurrentStage==UIState.SurveyShowPage){			
			this.getUI().getSession().lock();
			try {
				layoutShowSurvey(mCurrentIndex);
			} finally {
			   this.getUI().getSession().unlock();
			}
						
		}
		if(mCurrentStage==UIState.HomePage){
			this.getUI().getSession().lock();
			try {
				layoutHome();
			} finally {
			   this.getUI().getSession().unlock();
			}								
		} 
		
	}
}
