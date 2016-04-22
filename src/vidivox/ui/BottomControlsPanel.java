package vidivox.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import vidivox.audio.AudioOverlay;
import vidivox.worker.ExportWorker;

/**
 * This class is used to create a panel at the bottom of the main frame which allows for the space of buttons
 * such as open video, open project and etc. It extends JPanel as it is a panel which hold the functionality
 * to open other dialogs and panels. 
 * @author Ammar Bagasrawala
 *
 */
public class BottomControlsPanel extends JPanel {

	private JButton openVideoButton;					// Option for opening a video
	private JButton openProjectButton;					// Option for opening a project
	private JButton saveProjectButton;					// Option for saving a project
	private JButton exportButton;						// Option for exporting the audio and video
	private JButton helpButton;							// Option for the help button
	private JButton openAudioButton;					// Option for opening the audio dialog
	private JButton openVideoEffectsButton;				// Option to open video effects panel
	private JPanel bottomPanel;							// Panel to hold buttons
	private AudioOverlaysDialog audioOverlaysDialog;	// Reference to audio dialog	
	private String videoPath;							// String containing the path to the video
	private VideoPlayerComponent videoPlayer;			// Video player object
	private ControlsPanel controlsPanel;				// Reference to ControlsPanel class
	private VideoEffectsPanel videoEffectsPanel;		// Reference to VideoEffectsPanel class
	private MainFrame mainFrame;						// Reference to MainFrame class

	/**
	 * This constructor calls the methods to set up the layout and listeners and initializes fields
	 */
	public BottomControlsPanel(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		this.videoPlayer = mainFrame.getVideoPlayer();
		this.controlsPanel = mainFrame.getControlsPanel();
		this.videoEffectsPanel = mainFrame.getVideoEffectsPanel();
		this.audioOverlaysDialog = mainFrame.getAudioOverlaysDialog();
		setupLayout();
		setupListeners();
	}

	/**
	 * Method called to set up the layout of the buttons in the panel
	 */
	public void setupLayout() {

		bottomPanel = this;
		bottomPanel.setLayout(new GridBagLayout());
		GridBagConstraints botgbc = new GridBagConstraints();
		botgbc.insets = new Insets(5,5,5,5);

		// Creating the open video button and adding it to the panel
		openVideoButton = new JButton("Open Video");
		botgbc.gridx=0;
		botgbc.gridy=0;	
		botgbc.weightx=0.0f;
		botgbc.weighty=0.0f;       
		openVideoButton.setToolTipText("Choose a video to open");
		openVideoButton.setPreferredSize(new Dimension(120, 30));
		openVideoButton.setForeground(Color.blue);
		bottomPanel.add(openVideoButton, botgbc);

		// Creating the open project button and adding it to the panel
		openProjectButton = new JButton("Open Project");
		botgbc.gridx=1;
		botgbc.gridy=0;
		botgbc.weightx=0.0f;
		botgbc.weighty=0.0f;     
		openProjectButton.setToolTipText("A project is the video and audio saved but not combined together");
		openProjectButton.setPreferredSize(new Dimension(120, 30));
		openProjectButton.setForeground(Color.blue);
		bottomPanel.add(openProjectButton, botgbc);

		// Creating the save video project and adding it to the panel
		saveProjectButton = new JButton("Save Project");
		botgbc.gridx=2;
		botgbc.gridy=0;
		botgbc.weightx=0.0f;
		botgbc.weighty=0.0f;       
		saveProjectButton.setToolTipText("A project is the video and audio saved but not combined together");
		saveProjectButton.setPreferredSize(new Dimension(120, 30));
		saveProjectButton.setForeground(Color.blue);
		bottomPanel.add(saveProjectButton, botgbc);

		// Creating the export button and adding it to the panel
		exportButton = new JButton("Export");
		botgbc.gridx=3;
		botgbc.gridy=0;
		botgbc.weightx=0.0f;
		botgbc.weighty=0.0f;  
		exportButton.setToolTipText("Combines the audio and video into one avi file");
		exportButton.setPreferredSize(new Dimension(120, 30));
		exportButton.setForeground(Color.blue);
		bottomPanel.add(exportButton, botgbc);

		// Creating the open audio options button and adding it to the panel
		openAudioButton = new JButton("Audio Options");
		botgbc.gridx=4;
		botgbc.gridy=0;
		botgbc.weightx=0.0f;
		botgbc.weighty=0.0f;    
		openAudioButton.setToolTipText("Opens the options for audio editing");
		openAudioButton.setPreferredSize(new Dimension(120, 30));
		openAudioButton.setForeground(Color.blue);
		bottomPanel.add(openAudioButton, botgbc);

		// Creating the open video effects button and adding it to the panel
		openVideoEffectsButton = new JButton("Video Effects");
		botgbc.gridx=5;
		botgbc.gridy=0;
		botgbc.weightx=0.0f;
		botgbc.weighty=0.0f;       
		openVideoEffectsButton.setToolTipText("Opens the options for adding video effects");
		openVideoEffectsButton.setPreferredSize(new Dimension(120, 30));
		openVideoEffectsButton.setForeground(Color.blue);
		bottomPanel.add(openVideoEffectsButton, botgbc);

		// Creating the help button and adding it to the panel
		helpButton = new JButton("Help");
		botgbc.gridx=6;
		botgbc.gridy=0;
		botgbc.weightx=0.0f;
		botgbc.weighty=0.0f;
		helpButton.setPreferredSize(new Dimension(120, 30));
		helpButton.setForeground(Color.blue);
		bottomPanel.add(helpButton, botgbc);
	}

	/**
	 * Method called to set up the listeners for the buttons in this panel
	 */
	public void setupListeners() {
		// Setting up help button for users
		helpButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {		
				JOptionPane.showMessageDialog(null, "Please hover the cursor over the button or component" +
						"and a message will show its function. \nPlease also look at the manual provided");
			}
		});

		openVideoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();

				// Allowing user to only select mp4 and avi video files to open
				fileChooser.setAcceptAllFileFilterUsed(false);
				FileNameExtensionFilter videoFilter = new FileNameExtensionFilter("MP4 & AVI", "mp4", "avi");
				fileChooser.addChoosableFileFilter(videoFilter);
				int returnValue = fileChooser.showOpenDialog(null);

				// Checking if the return value is equal to the approve option
				if (returnValue == JFileChooser.APPROVE_OPTION) {

					File selectedFile = fileChooser.getSelectedFile();
					videoPath = selectedFile.getPath();
					mainFrame.setVideoName(selectedFile.getName());
					videoPlayer.playVideo(videoPath);
					controlsPanel.startAudioPlayers();
					audioOverlaysDialog.enablePlayAudioVideoButton();
					// Enabling the buttons in the controls panel
					controlsPanel.toggleControlsPanel(true);
					// Creating process to get the duration of the video file
					setDuration();

					// Setting the correct icon for the play/pause button
					JButton pauseButton = controlsPanel.getPauseButton();
					ImageIcon pauseIcon = controlsPanel.getPauseIcon();
					pauseButton.setIcon(pauseIcon);
				}				
			}
		});

		openProjectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				JFileChooser fileChooser = new JFileChooser();

				// Allowing user to only select project files to open
				fileChooser.setAcceptAllFileFilterUsed(false);
				FileNameExtensionFilter projectFilter = new FileNameExtensionFilter("Project files", "vvproject");
				fileChooser.addChoosableFileFilter(projectFilter);

				if (fileChooser.showOpenDialog(BottomControlsPanel.this) == JFileChooser.APPROVE_OPTION) {
					try {
						// Adding warning messages to inform user whether changes to current project (if any) are to be deleted
						String[] options = new String[]{"Yes, Discard changes","No"};
						String message="Opening another project will cause usaved changes to be discarded\n" + "Are you sure you want to do this?";

						// Storing result of the user's choice on whether to discard changes and open new project or not
						int result = JOptionPane.showOptionDialog
								(BottomControlsPanel.this,message,"Warning",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE,null,options,options[1]);

						// If the user does want to open a new project
						if(result==JOptionPane.YES_OPTION) {
							// Opening new project and getting the selected file
							openProject(fileChooser.getSelectedFile());
							// Enabling the components in the controls panel
							controlsPanel.toggleControlsPanel(true);
							// Getting duration of video and setting the appropriate fields
							setDuration();
						}
					} catch (IOException e) {
						e.printStackTrace();
					} catch (Exception e) {
						// Informing user that an invalid project was chosen
						JOptionPane.showMessageDialog(BottomControlsPanel.this,"Invalid or corrupted VIDIVOX project file","Invalid File",JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		saveProjectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {

				// Allowing user to specify where to save the project
				JFileChooser fileChooser = new JFileChooser();

				// Allowing user to only select project files 
				fileChooser.setAcceptAllFileFilterUsed(false);
				FileNameExtensionFilter projectFilter = new FileNameExtensionFilter("Project files", "vvproject");
				fileChooser.addChoosableFileFilter(projectFilter);

				JOptionPane.showMessageDialog(null, "Note: Moving video file after projected is created can result in errors", "Warning", JOptionPane.WARNING_MESSAGE);
				if (fileChooser.showSaveDialog(BottomControlsPanel.this) == JFileChooser.APPROVE_OPTION) {

					// Saving the file to where the user specified and checking if the name ends with .vvproject
					String fileName = fileChooser.getSelectedFile().getName();
					String path = fileChooser.getSelectedFile().getAbsolutePath();
					File saveFile = null;

					// Appending .vvproject to files created without this extension 
					if (!fileName.endsWith(".vvproject")) {
						path = fileChooser.getSelectedFile().getAbsolutePath();
						path = (path + ".vvproject");
						JOptionPane.showMessageDialog(null, "Renaming file to:\t\t\n" + fileName + ".vvproject");
						saveFile = new File(path);
					} else {
						saveFile = fileChooser.getSelectedFile();
					}

					// Adding project information to the file
					try {
						saveProject(saveFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});

		// Export button to allow user to merge video and audio generated from comments into
		// a single video file which can then be played and will contain both video and audio
		exportButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				// Allowing user to select destination of exported file
				JFileChooser fileChooser = new JFileChooser();
				if (fileChooser.showSaveDialog(BottomControlsPanel.this) == JFileChooser.APPROVE_OPTION) {                	
					try {	
						exportProject(fileChooser.getSelectedFile());
					} catch (IOException|InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});

		//Adding listener to the open audio options button
		openAudioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				// Creating an AudioOverlaysDialog that contains the options for adding commentary to the video
				audioOverlaysDialog.setVisible(true);
			}
		});

		// Adding listener to the video effects button
		openVideoEffectsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (videoEffectsPanel.isVisible()) {
					videoEffectsPanel.setVisible(false);
				} else {
					// Checking if the video panel will be clearly shown on the frame
					if (mainFrame.getWidth() < 1300 || mainFrame.getHeight() < 760) {
						mainFrame.setSize(new Dimension(1300, 760));	
					}
					videoEffectsPanel.setVisible(true);
					revalidate();
				}
			}
		});
	}

	/**
	 * This method attempts to save the project by specifying a file to write to and then writing to it the video path as well as 
	 * all the comments that were being edited in that project
	 * @param file - File specified by the user to store the project information in
	 * @throws IOException - Throws an exception if there was an error when dealing with the input/output
	 */
	private void saveProject(File file) throws IOException {
		
		// Creating file to write to
		BufferedWriter saveFile = new BufferedWriter(new FileWriter(file));

		// Allowing option to save the project with no video playing in other words, only audio being saved in the project
		if (videoPath == null) {
			// Writing new line so that it complies with the predetermined file structure
			saveFile.write("\n");
		} else {
			saveFile.write(videoPath + "\n");
		}

		// Looking through all the comments being edited in the AudioOverlayDialog frame and storing
		// them into the file after the video path in order to bring them up in the future
		for(AudioOverlay overlay : AudioOverlaysDialog.getOverlays()) {
			saveFile.write(overlay.toString()+"\n");
		}
		saveFile.close();
	}

	/**
	 * This method is called when the user wants to open an existing project. It does so by opening the file where the video 
	 * file path is saved, reading the file path and the comments saved in the file and opening them into the application
	 * @param file - File specified by the user on project to open up
	 * @throws IOException
	 * @throws FileFormatException
	 */
	private void openProject(File file) throws IOException, Exception {
		
		// Creating file to read from
		BufferedReader fileToOpen = new BufferedReader(new FileReader(file));
		String line = fileToOpen.readLine();
		String videoPath = null;

		// For when there was no video specified and only comments were saved
		if (!line.equals("")) {
			videoPath = line;
			audioOverlaysDialog.enablePlayAudioVideoButton();
		}

		// Adding the comments from the file into a new array list to store for further editing
		List<AudioOverlay> overlays = new ArrayList<>();
		// Adding lines from the file where each line specifies a certain comment
		while((line = fileToOpen.readLine()) != null){
			try {
				overlays.add(AudioOverlay.fromString(line));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		this.videoPath = videoPath;
		if (videoPath != null) {
			videoPlayer.playVideo(videoPath);
		}

		//controlsPanel.startAudioPlayers();

		// Getting the file name
		int index = videoPath.lastIndexOf("/");
		mainFrame.setVideoName(videoPath.substring(index + 1));

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Checking if the video is able to be played
		if (!videoPlayer.getMediaPlayer().isPlayable()) {
			JOptionPane.showMessageDialog(null, "ERROR: Video file cannot be found\nPath to video must have changed due to video being moved!", 
					"Video playback error", JOptionPane.ERROR_MESSAGE);
		}

		// Setting the commentary overlays in the class AudioOverlaysDialog which handles them
		AudioOverlaysDialog.setOverlays(overlays);
		fileToOpen.close();
	}

	/**
	 * This method gets the total time of the video being played and sets the total time label and field to that duration
	 */
	public void setDuration () {
		int totalTime = 0;
		// Process to find the video duration
		try {
			Process ffProbeProcess = new ProcessBuilder("/bin/bash", "-c", "ffprobe -i \"" + videoPath + "\" -show_entries format=duration 2>&1 | grep \"duration=\"").start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(ffProbeProcess.getInputStream()));
			ffProbeProcess.waitFor();
			String durationLine = reader.readLine();
			if (durationLine != null) {
				totalTime = (int) Float.parseFloat(durationLine.split("=")[1])*1000;
			}
		} catch (InterruptedException | IOException e1) {
			e1.printStackTrace();
			System.err.println("Failed to get video duration");
		}
		controlsPanel.setTotalTime(controlsPanel.calculateTime(totalTime),totalTime);
	}

	/**
	 * Merges the video and audio tracks into one file using ffmpeg by overlaying them
	 */
	private void exportProject(final File file) throws InterruptedException, IOException {
		ExportWorker exportWorker = new ExportWorker(file, this, videoPath);
		exportWorker.execute();
	}
}