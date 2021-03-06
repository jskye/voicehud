package julius.sky.voicehud.core.hud;

import java.beans.PropertyVetoException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.speech.AudioException;
import javax.speech.EngineException;
import javax.speech.EngineStateError;

import com.jme3.app.SimpleApplication;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.events.ElementShowEvent;
import de.lessvoid.nifty.elements.events.NiftyMousePrimaryClickedEvent;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import julius.sky.voicehud.core.voice.tts.TTS;
import  javax.speech.synthesis.SpeakableAdapter;

public class ClockController extends SpeakableAdapter implements ScreenController{
	

	private HUDGUIState hudGUI;
	private Nifty nifty;
	private Screen screen;
    private String currentTime;

	private TTS timeSpeakable;
	private Thread alertThread;
	private int clockElementShownCount;



	/**
	 * Creates a new controller instance for nifty-gui.
	 */

	/**
	 * Creates a new controller instance for nifty-gui.
	 */
	public ClockController(HUDGUIState hudGUI) 
	{
//		this.sim = sim;
		this.hudGUI = hudGUI;
		this.nifty = hudGUI.getNifty();
		this.screen = nifty.getCurrentScreen();
		try {
			this.timeSpeakable = new TTS();
		} catch (EngineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AudioException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EngineStateError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PropertyVetoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.alertThread = null;
	}

	public void bind(Nifty arg0, Screen arg1) {
		// TODO Auto-generated method stub
		
	}

	public void onEndScreen() {
		// TODO Auto-generated method stub
		
	}

	public void onStartScreen() {
		// TODO Auto-generated method stub
		updateTime();
		speakTime();
		try {
			Thread.sleep(3);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		disappear();
	}
	
	public void disappear(){
		this.screen.getLayerElements().clear();

	}
	
	@NiftyEventSubscriber(id="CLOCK")
	public int onElementShow(final String id, ElementShowEvent showevent ) {
		updateTime();
		System.out.println("speaking time");
		speakTime();
		clockElementShownCount++;
		try {
			Thread.sleep(3);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		disappear();		
		return clockElementShownCount;

	}
	
	// needs to be called when command is recognised and layer shown.
	public void updateTime(){
       Calendar cal = Calendar.getInstance();
       SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
       setCurrentTime(sdf.format(cal.getTime()));
       System.out.println(sdf.format(cal.getTime()));
       setTextToElement("clockLabel1", getCurrentTime().toString());
       this.timeSpeakable.setTextToSpeak(getCurrentTime());
       this.alertThread = new Thread(this.timeSpeakable);

	}
	
	public void speakTime(){
			alertThread.start();
	}
	
	public String getCurrentTime() {
		return currentTime;
	}
	
	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
	}
	
	
	
	/**
	 * Sets given text to the given element (e.g. label).
	 * 
	 * @param elementID
	 * 			Element's ID to assign a text to.
	 * 
	 * @param text
	 * 			Text to set.
	 */
    private void setTextToElement(String elementID, String text) 
    {
    	getElementByID(elementID).getRenderer(TextRenderer.class).setText(text);
    }
    
    /**
     * Looks up an element by ID.
     * 
     * @param elementID
     * 			ID to  look up.
     * 
     * @return
     * 			Element with given ID.
     */
    private Element getElementByID(String elementID)
    {
    	return nifty.getCurrentScreen().findElementByName(elementID);
    }
    
    private int getElementShownCount(){
    	return this.clockElementShownCount;
    }


}
