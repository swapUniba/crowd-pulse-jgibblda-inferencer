package com.github.frapontillo.pulse.crowd.LDA;


import jgibblda.Inferencer;
import jgibblda.LDACmdOption;
import jgibblda.Model;
import rx.Observable;
import rx.Subscriber;

import java.util.List;

import com.github.frapontillo.pulse.crowd.data.entity.Message;
import com.github.frapontillo.pulse.crowd.data.entity.Token;
import com.github.frapontillo.pulse.rx.PulseSubscriber;
import com.github.frapontillo.pulse.spi.IPlugin;


public class CrowdLDAInferencer extends IPlugin<Message, Message, CrowdLDAInferencerConfig> {
	
	public final static String PLUGIN_NAME = "inferencer-LDA";
    private Inferencer inferencer;

    
    @Override public String getName() {
        return PLUGIN_NAME;
    }

    @Override public CrowdLDAInferencerConfig getNewParameter() {
        return new CrowdLDAInferencerConfig();
    }
	
    @Override
    protected Observable.Operator<Message, Message> getOperator(CrowdLDAInferencerConfig parameters) {
    	return new Observable.Operator<Message, Message>() {
        	
            @Override public Subscriber<? super Message> call(Subscriber<? super Message> subscriber) {
                
            	
            	
            	return new PulseSubscriber<Message>(subscriber) {
                    
                	
                	@Override public void onStart() 
                	{
                        
                        String modelName=parameters.getModel();
                        
                        LDACmdOption ldaOption = new LDACmdOption(); 
                		ldaOption.inf = true; 
                		ldaOption.dir = "/opt/crowd-pulse/build/install/crowd-pulse/lib/" + modelName; 
                		ldaOption.modelName = "model-final"; 
                		ldaOption.niters = 100;
                		
                		inferencer = new Inferencer(); 
                		inferencer.init(ldaOption);
                		
                		super.onStart();
                    }

                    @Override public void onNext(Message message) 
                    {
                        reportElementAsStarted(message.getId());
                        String s = "";
                        
                        if(!(message.getTokens() == null || message.getTokens().size() == 0)){
	                		List<Token> t = message.getTokens();
	                		for(int i=0; i<t.size();i++){
	                			if(!t.get(i).isStopWord())
	                				s += t.get(i).getText() + " ";
	                		}
                        }else{
                        	s=message.getText();
                        }
                        
                		String[] temp = {s};
                		Model newModel = inferencer.inference(temp);
                		
                		try{
                			message.setCluster(newModel.z[0].get(0));
                		}catch(Exception e){
                			
                			message.setCluster(-1);
                			e.printStackTrace();
                		}
                		
                        reportElementAsEnded(message.getId());
                        subscriber.onNext(message);
                    }

                    @Override public void onCompleted() {
                    	reportPluginAsCompleted();
                        super.onCompleted();
                    }

                    @Override public void onError(Throwable e) {
                        reportPluginAsErrored();
                        super.onError(e);
                    }
                };
                
                
                
            }
        };
    }
    
}
