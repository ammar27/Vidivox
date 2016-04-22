package vidivox.ui;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;

/**
 * This is the main frame of the vidivox application
 * Contains the components to allow for: viewing a video
 * 										 opening a video
 * 										 opening a project
 * 										 saving a project
 * 										 opening the commentary frame
 * 										 opening the video effects panel 
 * 
 * @author Hanzhi Wang
 * @author Ammar Bagasrawala
 *
 */

public class MainFrame extends JFrame{

	/**
	 * Fields instantiated globally to be used by all methods in this class
	 */
	private VideoPlayerComponent videoPlayer;		// Video player object
	private ControlsPanel controlsPanel;			// Reference to ControlsPanel class
	private AudioOverlaysDialog audioOverlaysDialog;
	private MainFrame mainFrame = this;
	private VideoEffectsPanel videoEffectsPanel;
	private BottomControlsPanel bottomControlsPanel;
	private JLabel currentVideoLabel;
	private String videoPath;

	/**
	 * Constructor used to call methods to set up the layout and listeners for 
	 * the menu bar options when this object is instantiated
	 */
	public MainFrame() {

		// Naming the application VIDIVOX at the top of the frame
		super("VIDIVOX");
		
		// Setting up the layout of the application
		setupLayout(); 
		setupListeners();
		// Setting up the minimum size to disallow distortion of the frame
		setMinimumSize(new Dimension(750,500));
		pack();
	}
	
	/**
	 * Setting up the listeners for this class
	 */
	public void setupListeners() {
		// Setting up the listener to stop all audio playing when the window is closed
		mainFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				controlsPanel.stopAudioPlayers();
				System.exit(0);
			}
		});
	}
	
	/**
	 * Getter for the instance of controlsPanel of type ControlsPanel
	 * 
	 * @return controlsPanel - returns type ControlsPanel
	 */
	public ControlsPanel getControlsPanel() {
		return controlsPanel;
	}

	/**
	 * Setter for the instance of controlsPanel of type ControlsPanel
	 * 
	 * @param controlsPanel
	 */
	public void setControlsPanel(ControlsPanel controlsPanel) {
		this.controlsPanel = controlsPanel;
	}

	/**
	 * Sets up the frame layout
	 */
	private void setupLayout(){

		// Getting the content pane to add components to it
		Container contentPane = getContentPane();
		// Setting the layout to that of GridBagLayout
		contentPane.setLayout(new GridBagLayout());
		// Adding constraints to the grid bag layout to shape the panels in a neat format
		GridBagConstraints gbc = new GridBagConstraints();
		
		// Setting up the video player frame by adding a video player component to it
		videoPlayer = new VideoPlayerComponent();
		// Setting up size of the player
		videoPlayer.setPreferredSize(new Dimension(600, 400));
		// Formatting the grid bag layout for the video player and adding it to the top of the frame
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0f;
		gbc.weighty = 1.0f;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.fill = GridBagConstraints.BOTH;       
		// Adding the video player to the content pane
		contentPane.add(videoPlayer, gbc);

		// Setting up the controls panel which will contain all the buttons for playing the video, pausing etc
		setControlsPanel(new ControlsPanel(videoPlayer, audioOverlaysDialog));
		// Formatting the grid bag layout for the content pane and adding it to the bottom of the frame
		gbc.gridx=0;
		gbc.gridy=1;
		gbc.weightx=1.0f;
		gbc.weighty=0.0f;
		gbc.anchor= GridBagConstraints.CENTER;
		gbc.fill= GridBagConstraints.HORIZONTAL;
		controlsPanel.toggleControlsPanel(false);
		contentPane.add(getControlsPanel(), gbc);  
		
		// Creating the hide-able video effects panel and adding it to the content pane
		videoEffectsPanel = new VideoEffectsPanel(videoPlayer);
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx=1.0f;
		gbc.weighty=1.0f;
		gbc.anchor= GridBagConstraints.EAST;
		gbc.fill= GridBagConstraints.BOTH; 
		videoEffectsPanel.setVisible(false);
		contentPane.add(videoEffectsPanel, gbc);  

		// Creating the label which enables user to see the current video playing
		currentVideoLabel = new JLabel("Video Currently Playing: ");
		gbc.gridx=0;
		gbc.gridy=2;
		gbc.weightx=1.0f;
		gbc.weighty=0.0f;
		gbc.insets = new Insets(0,15,0,5);
		gbc.anchor= GridBagConstraints.CENTER;
		gbc.fill= GridBagConstraints.HORIZONTAL;
		contentPane.add(currentVideoLabel, gbc);
		
		gbc.gridx=0;
		gbc.gridy=3;
		gbc.weighty=0.0f;
		gbc.weightx=1.0f;
		gbc.fill= GridBagConstraints.HORIZONTAL;  
		contentPane.add(new JSeparator(), gbc);
		
		audioOverlaysDialog = new AudioOverlaysDialog(controlsPanel);
		audioOverlaysDialog.setVisible(false);
		
		// Creating a panel to hold all the options for opening/saving etc
		bottomControlsPanel = new BottomControlsPanel(this);
		gbc.gridx=0;
		gbc.gridy=4;
		gbc.weightx=0.0f;
		gbc.weighty=0.0f;
		gbc.fill= GridBagConstraints.HORIZONTAL;  
		contentPane.add(bottomControlsPanel, gbc);  
	}
	
	/**
	 * Establishing getters for the fields in this class
	 * @return
	 */
	public VideoPlayerComponent getVideoPlayer() {
		return videoPlayer;
	}

	public VideoEffectsPanel getVideoEffectsPanel() {
		return videoEffectsPanel;
	}

	public AudioOverlaysDialog getAudioOverlaysDialog() {
		return audioOverlaysDialog;
	}
	
	/**
	 * Establishing a setter or the label which shows the current video being played
	 * @param name
	 */
	public void setVideoName(String name) {
		currentVideoLabel.setText("Video Currently Playing: " + name);
	}
}