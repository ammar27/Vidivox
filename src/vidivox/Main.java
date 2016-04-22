package vidivox;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.UIManager;
import vidivox.ui.MainFrame;

/**
 * This is main class containing the main method to run the vidivox application
 * It is also used to call the UpdateRunnable class which updates the JSlider used
 * to inform the user the time at which the video is currently playing
 * 
 * @author Hanzhi Wang (partner)
 * @author Ammar Bagasrawala
 *
 */
public class Main {
	
	/** 
	 * Creating a ScheduledExecutorService to call a specific update class at given intervals
	 */
	private static ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
	
	/**
	 * Main method for the vidivox application
	 */
    public static void main(String[] args){
 
    	// Changing the GUI look and feel to one that is more 'pretty'
    	try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			UIManager.put("Slider.paintValue", false);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	// Creating the main frame which contains functionality for opening a video, playing the video etc.
    	MainFrame mainFrame = new MainFrame();
   		mainFrame.setVisible(true);
		
		// Instantiating a UpdateRunnable class to update specific components every 500 milliseconds
		// Source: https://github.com/caprica/vlcj/blob/master/src/test/java/uk/co/caprica/vlcj/test/basic/PlayerControlsPanel.java
		executorService.scheduleAtFixedRate(new UpdateRunnable(mainFrame.getControlsPanel()), 0L, 500L, TimeUnit.MILLISECONDS);
    }
}
