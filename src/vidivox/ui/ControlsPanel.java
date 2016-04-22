package vidivox.ui;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSliderUI;

import vidivox.Main;
import vidivox.audio.AudioOverlay;
import vidivox.worker.AudioPlayWorker;
import vidivox.worker.SkipVideoWorker;

/**
 * This class contains the components which allow the user to perform the basic tasks
 * associated with video players such as play, pause, stop, skip forward/back, fast forward,
 * rewind and adjust the volume. The user can also drag the slider defined in this class
 * which shows the progress of the video to a specific time. 
 * 
 * @author Hanzhi Wang
 * @author Ammar Bagasrawala
 *
 */
public class ControlsPanel extends JPanel {

	/**
	 * Declaring fields to be used within this class and the project
	 */
	private ArrayList<AudioPlayWorker> audioPlayWorkers = new ArrayList<AudioPlayWorker>(); // List of audio workers
	private ControlsPanel controlsPanel = this;		// Reference to the only instance of this class
	private VideoPlayerComponent videoPlayer;		// Reference to the video player component 
	private SkipVideoWorker skipVid = null;			// Reference to instance of a worker class to forward/rewind
	private JLabel currentTimeLabel;				// Label showing the time the video is currently at
	private JLabel totalTimeLabel;					// Label showing the total time of the video
	private JSlider seekSlider;						// Slider showing progress of video and allowing user to set
	private JButton pauseButton;					// Button to pause and play the video
	private JButton stopButton;						// Button to stop the video
	private JButton rewindButton;					// Button to rewind the video
	private JButton fastForwardButton;				// Button to fast forward the video
	private JButton skipBackButton;					// Button to skip backwards
	private JButton skipForwardButton;				// Button to skip forwards
	private JSlider volumeSlider;					// Slider used to control volume
	private JLabel volumeLevelLabel;				// Label showing user current volume level
	private int volume = 100;						// Integer representing volume level
	private float totalTime = 0;					// Total time of the video
	private boolean sliderCanMove = false;			// Boolean indicating whether the slider is movable
	private ImageIcon playIcon;						// Image icon for the play button
	private ImageIcon pauseIcon;					// Image icon for the pause button
	private ImageIcon stopIcon;						// Image icon for the stop button
	private ImageIcon fastForwardIcon;				// Image icon for the fast forward button
	private ImageIcon rewindIcon;					// Image icon for the rewind button
	private ImageIcon skipForwardIcon;				// Image icon for the skip forward button
	private ImageIcon skipBackwardIcon;				// Image icon for the skip back button
	private AudioOverlaysDialog audioOverlaysDialog;// Reference to the AudioOverlaysDialog (UI control class)
	
	/**
	 * Constructor called when this class is instantiated to set up the layout, listeners
	 * and initialize the video player component
	 * @param videoPlayer - the media play component which allows the video to played
	 */
	public ControlsPanel(VideoPlayerComponent videoPlayer, AudioOverlaysDialog dialog) {
		this.audioOverlaysDialog = dialog;
		this.videoPlayer = videoPlayer;
		setupLayout();
		setupListeners();
	}

	/**
	 * Sets up the layout for the sliders, labels and other buttons through the use of the grid bag layout
	 */
	private void setupLayout(){

		// Setting the layout to that of a grid bag layout
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5,5,5,5);

		// Set up the slider panel which contains the slider and labels for the time
		JPanel sliderPanel = new JPanel();
		sliderPanel.setLayout(new GridBagLayout());

		// Creating and adding current time label to the slider panel
		currentTimeLabel = new JLabel("00:00:00");
		gbc.gridx=0;
		gbc.gridy=0;
		gbc.weightx=0.0f;
		gbc.weighty=1.0f;       
		sliderPanel.add(currentTimeLabel,gbc);

		// Creating and adding the seek slider to the slider panel
		seekSlider = new JSlider();
		gbc.gridx=1;
		gbc.gridy=0;
		gbc.weightx=1.0f;
		gbc.weighty=1.0f;
		gbc.fill=GridBagConstraints.HORIZONTAL;
		seekSlider.setMaximum(100);
		seekSlider.setMinimum(0);
		seekSlider.setMajorTickSpacing(5);
		seekSlider.setMinorTickSpacing(1);
		seekSlider.setValue(0);
		sliderPanel.add(seekSlider,gbc);    

		// Creating and adding the total video time label to the slider panel
		totalTimeLabel=new JLabel("00:00:00");
		gbc.gridx=2;
		gbc.gridy=0;
		gbc.weightx=0.0f;
		gbc.weighty=1.0f;
		sliderPanel.add(totalTimeLabel,gbc);
		gbc.gridx=0;
		gbc.gridy=0;
		gbc.weightx=1.0;
		gbc.weighty=0.0;
		add(sliderPanel,gbc);

		// Creating a buttons panel to contain all the controls for the video
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridBagLayout());

		// Getting image icons for each of the buttons and setting their layout
		URL url = Main.class.getResource("/resources/play.png");
		playIcon = new ImageIcon(url);
		Image img = playIcon.getImage();
		Image newimg = img.getScaledInstance(40, 24, java.awt.Image.SCALE_SMOOTH);
		playIcon = new ImageIcon(newimg);

		url = Main.class.getResource("/resources/pause.png");      
		pauseIcon = new ImageIcon(url);
		img = pauseIcon.getImage();
		newimg = img.getScaledInstance(40, 24, java.awt.Image.SCALE_SMOOTH);
		pauseIcon = new ImageIcon(newimg);

		url = Main.class.getResource("/resources/stop.png"); 
		stopIcon = new ImageIcon(url);
		img = stopIcon.getImage();
		newimg = img.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
		stopIcon = new ImageIcon(newimg);

		url = Main.class.getResource("/resources/fastforward.png"); 
		fastForwardIcon = new ImageIcon(url);
		img = fastForwardIcon.getImage();
		newimg = img.getScaledInstance(45, 24, java.awt.Image.SCALE_SMOOTH);
		fastForwardIcon = new ImageIcon(newimg);

		url = Main.class.getResource("/resources/rewind.png"); 
		rewindIcon = new ImageIcon(url);
		img = rewindIcon.getImage();
		newimg = img.getScaledInstance(45, 24, java.awt.Image.SCALE_SMOOTH);
		rewindIcon = new ImageIcon(newimg);

		url = Main.class.getResource("/resources/skipforward.png"); 
		skipForwardIcon = new ImageIcon(url);
		img = skipForwardIcon.getImage();
		newimg = img.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
		skipForwardIcon = new ImageIcon(newimg);

		url = Main.class.getResource("/resources/skipbackward.png"); 
		skipBackwardIcon = new ImageIcon(url);
		img = skipBackwardIcon.getImage();
		newimg = img.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
		skipBackwardIcon = new ImageIcon(newimg);

		// Creating pause button which will also serve as the play button and adding it to the layout
		pauseButton = new JButton();
		pauseButton.setIcon(playIcon);
		pauseButton.setMargin(new Insets(0,0,0,0));
		gbc.gridx=3;
		gbc.gridy=0;
		gbc.weightx=0.0f;
		gbc.weighty=1.0f;
		pauseButton.setToolTipText("Plays or pauses the video");
		pauseButton.setPreferredSize(new Dimension(52, 36));
		buttonsPanel.add(pauseButton,gbc);

		// Creating stop button and adding it to the layout
		stopButton=new JButton();
		stopButton.setIcon(stopIcon);
		gbc.gridx=4;
		gbc.gridy=0;
		gbc.weightx=0.0f;
		gbc.weighty=1.0f;
		stopButton.setToolTipText("Stops the video");
		stopButton.setPreferredSize(new Dimension(52, 36));
		buttonsPanel.add(stopButton,gbc);

		// Creating rewind button and adding it to the layout
		rewindButton=new JButton();
		rewindButton.setIcon(rewindIcon);
		rewindButton.setMargin(new Insets(0,0,0,0));
		gbc.gridx=1;
		gbc.gridy=0;
		gbc.weightx=0.0f;
		gbc.weighty=1.0f;
		rewindButton.setToolTipText("Rewinds the video");
		rewindButton.setPreferredSize(new Dimension(52, 36));
		buttonsPanel.add(rewindButton,gbc);

		// Creating forward button and adding it to the layout
		fastForwardButton=new JButton();
		fastForwardButton.setIcon(fastForwardIcon);
		fastForwardButton.setMargin(new Insets(0,0,0,0));
		gbc.gridx=6;
		gbc.gridy=0;
		gbc.weightx=0.0f;
		gbc.weighty=1.0f;
		fastForwardButton.setToolTipText("Forwards the video");
		fastForwardButton.setPreferredSize(new Dimension(52, 36));
		buttonsPanel.add(fastForwardButton,gbc);

		// Creating skip forward button and adding it to the layout
		skipForwardButton = new JButton();
		skipForwardButton.setIcon(skipForwardIcon);
		gbc.gridx = 5;
		gbc.gridy = 0;
		gbc.weightx=0.0f;
		gbc.weighty=3.0f;
		skipForwardButton.setToolTipText("Skip 10 secs forward");
		skipForwardButton.setPreferredSize(new Dimension(52, 36));
		buttonsPanel.add(skipForwardButton, gbc);

		// Creating skip back button and adding it to the layout
		skipBackButton = new JButton();
		skipBackButton.setIcon(skipBackwardIcon);
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.weightx=0.0f;
		gbc.weighty=3.0f;
		skipBackButton.setToolTipText("Skip 10 secs backward");
		skipBackButton.setPreferredSize(new Dimension(52, 36));
		buttonsPanel.add(skipBackButton, gbc);

		// Adding space between GUI components
		gbc.gridx=8;
		gbc.gridy=0;
		gbc.weightx=0.5f;
		gbc.weighty=1.0f;
		buttonsPanel.add(new JPanel(),gbc);

		// Adding volume label to the layout
		gbc.gridx=8;
		gbc.gridy=0;
		gbc.weightx=0.0f;
		gbc.weighty=1.0f;
		buttonsPanel.add(new JLabel("Volume"));

		// Adding the volume slider to the layout
		volumeSlider=new JSlider();
		volumeSlider.setValue(100);
		volumeSlider.setPreferredSize(new Dimension(100,25));
		gbc.gridx=8;
		gbc.gridy=0;
		gbc.weightx=1.0f;
		gbc.weighty=1.0f;
		buttonsPanel.add(volumeSlider);

		// Adding the volume level label to the layout
		volumeLevelLabel=new JLabel(volume + "%");
		gbc.gridx=9;
		gbc.gridy=0;
		gbc.weightx=0.0f;
		gbc.weighty=1.0f;
		buttonsPanel.add(volumeLevelLabel);
		
		// Adding the button panels to the layout
		gbc.gridx=0;
		gbc.gridy=1;
		gbc.weightx=1.0;
		gbc.weighty=0.0;
		add(buttonsPanel,gbc);
	}

	/**
	 * Sets up the listeners for the controls
	 */
	private void setupListeners(){

		// Setting up listener for when the user clicks on the video, it allows them to pause or play the video
		videoPlayer.getVideoSurface().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// Un-muting the audio from the video
				videoPlayer.getMediaPlayer().mute(false);
				// Checking if the video is currently forwarding/rewinding and
				// stopping this process if it is
				if (skipVid != null) {
					skipVid.setIsSkipping(false);
					skipVid.cancel(true);
					skipVid = null;
				}

				// Checking current state of video
				if (videoPlayer.getMediaPlayer().isPlaying()) {
					// Pausing the video player
					videoPlayer.getMediaPlayer().pause();
					// Changing the label of the button to play
					pauseButton.setIcon(playIcon);
					stopAudioPlayers();
				} else {
					videoPlayer.getMediaPlayer().play();
					// Changing the label of the button to pause
					pauseButton.setIcon(pauseIcon);
					startAudioPlayers();
				}
			}
		});

		// Seek slider change event to make video player to play at set time
		seekSlider.addChangeListener(new ChangeListener()  {
			// For when user drags the seek bar
			@Override
			public void stateChanged(ChangeEvent e) {

				// Updating the video to play from where the user dragged the 
				// slider to and updating the current time label accordingly
				if (sliderCanMove) {
					// Updating current time
					long currentTime = videoPlayer.getMediaPlayer().getTime();
					setCurrentTime(calculateTime(currentTime), currentTime);

					// Setting the position of the video to that of the seek slider
					videoPlayer.getMediaPlayer().setTime(seekSlider.getValue());
				}
			}
		});

		// Adding a mouse listener to the slider so that the boolean sliderCanMove
		// can be changed to true or false accordingly
		seekSlider.addMouseListener(new MouseAdapter() {

			// Setting the sliderCanMove field to true when the user clicks
			// on the slider so that they can drag the slider and update the video
			@Override
			public void mousePressed(MouseEvent arg0) {
				sliderCanMove = true;
				// Mute the audio when the slider is being moved
				videoPlayer.getMediaPlayer().mute(true);
				stopAudioPlayers();
			}

			// Setting the sliderCanMove field to false when the user releases the click from the slider so that the state 
			// changed listener doesn't cause frame lags
			@Override
			public void mouseReleased(MouseEvent e) {

				// Getting mouse click location and setting seek bar to that position
				// so that video forwards according to where the user clicked
				final BasicSliderUI ui = (BasicSliderUI) seekSlider.getUI();
				seekSlider.setValue(ui.valueForXPosition(e.getX()));
				sliderCanMove = false;

				// Unmute the audio when the seek slider is released
				videoPlayer.getMediaPlayer().mute(false);
				if(videoPlayer.getMediaPlayer().isPlaying()){
					startAudioPlayers();
				}
			}
		});

		// Adding action listener for the pause/play button
		pauseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				videoPlayer.getMediaPlayer().mute(false);

				// Checking if the video is currently forwarding/rewinding and stopping this process if it is
				if (skipVid != null) {
					skipVid.setIsSkipping(false);
					skipVid.cancel(true);
					skipVid = null;
				}

				// Stopping the audio players
				stopAudioPlayers();

				// Checking current state of video
				if (videoPlayer.getMediaPlayer().isPlaying()) {
					// Pausing the video player
					videoPlayer.getMediaPlayer().pause();
					// Changing the label of the button to play
					pauseButton.setIcon(playIcon);
				} else {
					videoPlayer.getMediaPlayer().play();
					// Changing the label of the button to pause
					pauseButton.setIcon(pauseIcon);
					startAudioPlayers();
				}
			}
		});

		// Adding action listener to the stop button
		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Un-muting the video
				videoPlayer.getMediaPlayer().mute(false);

				// Setting the text of the pause/play button to pause
				pauseButton.setIcon(playIcon);

				// Checking if the video is currently forwarding/rewinding and
				// stopping this process if it is
				if (skipVid != null) {
					skipVid.setIsSkipping(false);
					skipVid.cancel(true);
					skipVid = null;
				}

				// Stopping the audio players
				stopAudioPlayers();

				// Stopping the video
				videoPlayer.getMediaPlayer().stop();

				// Resetting the slider and the time label to their initial states
				seekSlider.setValue(0);
				setCurrentTime("00:00:00", 0);
			}
		});

		// Adding action listener to the fast forward button to allow the user to
		// fast forward the video when the button is clicked on
		fastForwardButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// Checking if there is no already running skipping function
				if (skipVid == null) {
					stopAudioPlayers();
					videoPlayer.getMediaPlayer().mute(true);

					// Instantiating new SkipVideoWorker to forward the video
					// and executing the thread to start the skipping
					skipVid =  new SkipVideoWorker(videoPlayer, controlsPanel);
					skipVid.setSkipValue(1000);
					skipVid.setIsSkipping(true);
					skipVid.execute();

				} else {
					// If there is an already running skipping function then stopping it
					if(videoPlayer.getMediaPlayer().isPlaying()){
						startAudioPlayers();
					}
					videoPlayer.getMediaPlayer().mute(false);
					skipVid.setIsSkipping(false);
					skipVid.cancel(true);
					skipVid = null;
				}
			}
		});

		// Adding listener to the rewind button to allow the user to rewind the video
		rewindButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// Checking if the video is already forwarding or rewinding
				if (skipVid == null) {
					// If the video is not currently forwarding/rewinding then muting
					// the audio of the video and starting process to rewind the video
					stopAudioPlayers();
					videoPlayer.getMediaPlayer().mute(true);
					skipVid = new SkipVideoWorker(videoPlayer, controlsPanel);
					skipVid.setSkipValue(-1000);
					skipVid.setIsSkipping(true);
					skipVid.execute();

				} else {
					// If the video is skipping then the following will un-mute the video
					// and stop it from skipping
					if(videoPlayer.getMediaPlayer().isPlaying()){
						startAudioPlayers();
					}
					videoPlayer.getMediaPlayer().mute(false);
					skipVid.setIsSkipping(false);
					skipVid.cancel(true);
					skipVid = null;
				}
			}
		});

		// Adding action listener for the skip ahead one large frame at a time button aka skip forward
		skipForwardButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// Making sure the video is not muted
				videoPlayer.getMediaPlayer().mute(false);

				// Checking if the video is being forwarded or is rewinding and stopping that process if it is
				if (skipVid != null) {
					skipVid.setIsSkipping(false);
					skipVid.cancel(true);
					skipVid = null;
				}

				// Skipping the video ahead and updating the audio
				videoPlayer.getMediaPlayer().skip(10000);
				seekSlider.setValue(seekSlider.getValue()+10000);
				updateAudioPlayers();
			}
		});

		// Adding action listener for the skip backward one large frame at a time button aka skip back
		skipBackButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Making sure the video is not mute
				videoPlayer.getMediaPlayer().mute(false);

				// Checking if the video is currently forwarding/rewinding and stopping the process if it is
				if (skipVid != null) {
					skipVid.setIsSkipping(false);
					skipVid.cancel(true);
					skipVid = null;
				}
				// RSkipping backwards by a set amount and updating the audio players
				videoPlayer.getMediaPlayer().skip(-10000);
				seekSlider.setValue(seekSlider.getValue()-10000);
				updateAudioPlayers();
			}
		});

		// Adding a listener to the volume slider in order to change the volume of the video accordingly
		volumeSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				// Getting current volume user has set
				volume = volumeSlider.getValue();
				// Updating the audio in the video player to the selected volume
				videoPlayer.getMediaPlayer().setVolume(volume);
				// Updating the label for the volume to show current volume level
				volumeLevelLabel.setText(volume + "%");
			}
		});
	}

	/**
	 * This method calculates the hours, minutes and seconds of a given time and formats
	 * it into a string to be shown on the GUI
	 * 
	 * @param newTime - the time to convert into seconds, minutes and hours
	 * @return timeString - returns the formatted string
	 */
	public String calculateTime(float newTime) {

		long second = (long) ((newTime / 1000) % 60);
		long minute = (long) ((newTime/ 60000) % 60);
		long hour = (long) ((newTime/ 3600000) % 24);

		// Storing the formatted string
		String timeString = String.format("%02d:%02d:%02d", hour, minute, second);
		return timeString;
	}

	/**
	 * Plays all of the overlaid audio tracks
	 */
	public void startAudioPlayers(){

		stopAudioPlayers();

		for(AudioOverlay overlay: AudioOverlaysDialog.getOverlays()){
			// Only play the overlay tracks with "preview" ticked
			if(overlay.isShowingPreview()) {
				long currentPositionMillis = videoPlayer.getMediaPlayer().getTime();
				double currentPositionSeconds = ((double) currentPositionMillis) / 1000;
				AudioPlayWorker player = new AudioPlayWorker(overlay, currentPositionSeconds);
				player.execute();
				audioPlayWorkers.add(player);
			}
		}
	}

	/**
	 * Stops all the overlaid audio tracks currently playing
	 */
	public void stopAudioPlayers() {
		// Stopping those being previewed by the play button in the audio frame
		List<AudioOverlay> audioOverlays = audioOverlaysDialog.getOverlays();
		for (int i = 0; i < audioOverlays.size(); i++) {
			if (audioOverlays.get(i).getAudioWorker() != null) {
				audioOverlays.get(i).getAudioWorker().kill();
			}
		}
		
		// Stopping all other audio playing
		for(AudioPlayWorker player : audioPlayWorkers){
			player.kill();
		}
		audioPlayWorkers = new ArrayList<>();
	}
	
	public void toggleControlsPanel(boolean bool) {
		pauseButton.setEnabled(bool);
		stopButton.setEnabled(bool);
		rewindButton.setEnabled(bool);
		fastForwardButton.setEnabled(bool);
		skipForwardButton.setEnabled(bool);
		skipBackButton.setEnabled(bool);
		seekSlider.setEnabled(bool);
	}

	/**
	 * Updates the currently playing audio tracks to synchronize them with the video
	 */
	public void updateAudioPlayers() {
		stopAudioPlayers();
		if(videoPlayer.getMediaPlayer().isPlaying()){
			startAudioPlayers();
		}
	}

	/**
	 * Method which sets the total time variable as well as the label in the GUI
	 */
	public void setTotalTime(String timeString, long time) {
		totalTimeLabel.setText(timeString);
		totalTime = time;
		seekSlider.setMaximum((int)time);
	}

	/**
	 * Method which sets the label in the GUI
	 */
	public void setCurrentTime(String timeString, long time) {
		currentTimeLabel.setText(timeString);
	}
	
	/**
	 * Defining getters for use of variables in other classes
	 */
	public SkipVideoWorker getSkipVid() {
		return skipVid;
	}

	public JButton getPauseButton() {
		return pauseButton;
	}

	public ImageIcon getPlayIcon() {
		return playIcon;
	}

	public ImageIcon getPauseIcon() {
		return pauseIcon;
	}
	
	public AudioOverlaysDialog getAudioOverlaysDialog() {
		return audioOverlaysDialog;
	}

	public VideoPlayerComponent getVideoPlayer() {
		return videoPlayer;
	}

	public JSlider getSeekSlider() {
		return seekSlider;
	}

	public float getTotalTime() {
		return totalTime;
	}

	public boolean getSliderStatus() {
		return sliderCanMove;
	}
}