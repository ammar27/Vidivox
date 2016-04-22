package vidivox.audio;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import vidivox.ui.ControlsPanel;
import vidivox.worker.SpeechSynthesisWorker;

/**
 * This class is a subclass of the AudioOverlay class and is instantiated whenever 
 * the user selects to add a comment to the video. It contains the text the user 
 * entered and executes the synthesis worker class to create a wav file from the text
 * 
 * @author Hanzhi Wang
 * @author Ammar Bagasrawala
 */
public class CommentaryOverlay extends AudioOverlay {

	/**
	 * Fields initialized to be used by methods in this class and other packages
	 */
	private String text;								// String storing the user's comment
	private String filePath;							// File path of created wav file
	private int position;								// Position of the comment in the AudioOverlaysDialog
	private SpeechSynthesisWorker synthesisWorker;		// Reference to the class that creates the wav file
	private JTextField textField;						// Text field where user enters comment
	private JButton saveToFileButton;					// Button used to save commentary as mp3
	private static ControlsPanel controlsPanel;			// Reference to the ControlsPanel class
	protected JComboBox<String> voiceGenderComboBox;	// ComboBox to allow user to select the gender of the voice
	protected JComboBox<String> voicePitchComboBox;		// ComboBox to allow the user to select the emotion of the voice

	/**
	 * Constructor which takes in only the position of the audio and initializes other fields
	 * in this class to a default value
	 * 
	 * @param position
	 */
	public CommentaryOverlay(int position, ControlsPanel controlsPanel) {
		this(position, "", 0, 100, controlsPanel);
	}

	/**
	 * Constructor called when this class is instantiated 
	 * 
	 * @param position - position of the commentary in the dialog
	 * @param text - text entered by the user
	 * @param startTime - start time of audio specified by the user
	 * @param volume - volume of audio
	 */
	public CommentaryOverlay(int position, String text, float startTime,int volume, ControlsPanel controlsPanel){
		// Initializing fields
		this.position = position;
		this.text = text;
		this.startTime = startTime;
		this.volume = volume;
		this.controlsPanel = controlsPanel;
		
		// Checking if the text is empty and if not, allowing the commentary to be
		// converted into a wav file
		if (text != null && !text.isEmpty()) {
			// Instantiating class to convert from text to speech
			new SpeechSynthesisWorker(text, "commentary" + position, getAudioOverlay()) {
				@Override
				protected void done() {
					super.done();
					// Setting the file path of the wav file
					CommentaryOverlay.this.filePath = filePath;
				}
			}.execute(); // Executing worker class
		}
	}

	/**
	 * Getter for the position of the comment in the dialog frame
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * Getter for the text the user typed in
	 */
	public String getText() {
		return text;
	}

	/**
	 * Getter for the file path variable
	 */
	@Override
	public String getFilePath() {
		return filePath;
	}

	/**
	 * Method to set up the layout of the commentary overlay component using the grid 
	 * bag layout
	 * 
	 * @return the content pane which is a JPanel
	 */
	public JPanel getComponentView() {

		// Setting up the content pane by calling AudioOverlay's method
		final JPanel contentPane = super.getComponentView();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5,5,5,5);

		// Adding text field where user will enter their text to turn into audio
		textField = new JTextField();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0f;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTH;
		contentPane.add(textField, gbc);

		// Add button to save the commentary to an mp3 file
		saveToFileButton=new JButton("Save to mp3");
		saveToFileButton.setPreferredSize(new Dimension(120, 28));
		saveToFileButton.setToolTipText("Save commentary as mp3 file");
		gbc.gridx++;
		contentPane.add(saveToFileButton);

		// Creating the botPanel which will contain the options to control the commentary
		JPanel botPanel = new JPanel();
		// Setting up the layout of this panel
		botPanel.setLayout(new GridBagLayout());  
		GridBagConstraints botgbc = new GridBagConstraints();
		botgbc.insets = new Insets(5,5,5,5);

		JLabel genderVoiceLabel = new JLabel("Choose Voice: ");
		botgbc.gridx = 0;
		botgbc.gridy = 0;
		botPanel.add(genderVoiceLabel, botgbc);  

		voiceGenderComboBox = new JComboBox<String>();
		voiceGenderComboBox.addItem("Robotic");
		voiceGenderComboBox.addItem("British");
		voiceGenderComboBox.addItem("NZ'er");
		voiceGenderComboBox.setSelectedItem("Robotic");
		botgbc.gridx = 1;
		botgbc.gridy = 0;
		botPanel.add(voiceGenderComboBox, botgbc);  

		botgbc.gridx = 2;
		botgbc.weightx = 0.1f;
		botPanel.add(new JPanel(), botgbc);
		botgbc.weightx = 0.0f;

		JLabel emotionVoiceLabel = new JLabel("Choose Pitch: ");
		botgbc.gridx = 3;
		botgbc.gridy = 0;
		botPanel.add(emotionVoiceLabel, botgbc); 

		voicePitchComboBox = new JComboBox<String>();
		voicePitchComboBox.addItem("Normal");
		voicePitchComboBox.addItem("Low");
		voicePitchComboBox.addItem("High");
		voicePitchComboBox.setSelectedItem("Normal");
		botgbc.gridx = 4;
		botgbc.gridy = 0;
		botPanel.add(voicePitchComboBox, botgbc); 
		
		botgbc.gridx = 5;
		botgbc.gridy = 0;
		botgbc.weightx = 10.0f;
		botPanel.add(new JPanel(), botgbc); 

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 1.0f;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		contentPane.add(botPanel, gbc);

		// Adding document listener to the text field to determine when the user has
		// typed something in and getting the text from the text field to store
		textField.getDocument().addDocumentListener(new DocumentListener() {

			// Method called whenever text is added
			@Override
			public void insertUpdate(DocumentEvent documentEvent) {
				changedUpdate(documentEvent);
			}

			// Method called whenever text is removed
			@Override
			public void removeUpdate(DocumentEvent documentEvent) {
				changedUpdate(documentEvent);
			}

			// Method called whenever change occurs
			@Override
			public void changedUpdate(DocumentEvent documentEvent) {
				//Storing text
				text = textField.getText();
				
				// Checking if the length of the string is greater than 100 characters
				if (text.length() >= 100) {
					// If it is greater than 100 characters then showing a pop up error message to the user notifying them of limit
					JOptionPane.showMessageDialog(null, "Must specify comment less than or equal 100 characters", "Error", JOptionPane.ERROR_MESSAGE);
				
				} else {
					// Checking if the synthesis worker thread needs to be killed since 
					// text is being updated
					if (synthesisWorker != null && !synthesisWorker.isDone()) {
						synthesisWorker.kill();
					}

					// Creating new synthesis worker to convert the text file into a wav file 
					synthesisWorker = new SpeechSynthesisWorker(text, "commentary" + position, getAudioOverlay()) {
						@Override
						protected void done() {

							super.done();
							// Allowing the play button to be clicked
							playButton.setEnabled(true);
							// Getting the file path of the wav file
							CommentaryOverlay.this.filePath = synthesisWorker.filePath;

							// Getting the duration of the audio and storing it
							try {
								float duration = getDuration(filePath);
								String durationText = (int) (duration / 60) + ":" + (int) (duration % 60) + 
										"." + (int) ((duration - (int) duration)*1000);

								// Setting the text in the label to the duration value
								durationLabel.setText(durationText);
								
								// Checking if the audio is longer than the video
								enableAudioErrorMessage(controlsPanel, duration);


							} catch (IOException | InterruptedException e) {
								// Error handling if there was a problem while getting duration
								durationLabel.setText("???");
							}
						}
					};
					synthesisWorker.execute(); // Executing the process
				}
			}
		});

		// Adding action listener to button used to save a comment as a mp3 file
		saveToFileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {

				// Making sure the commentary is not empty
				if (getFilePath() == null) {
					// Error message disallowing user to save empty comment
					JOptionPane.showMessageDialog(contentPane,"Error: Cannot save empty commentary",
							"Error saving file",JOptionPane.ERROR_MESSAGE);
					return;
				}

				// Allowing user to choose a destination to save to
				JFileChooser fileChooser = new JFileChooser();
				if (fileChooser.showSaveDialog(contentPane) == JFileChooser.APPROVE_OPTION) {

					String fileName = fileChooser.getSelectedFile().getAbsolutePath();

					// Adding .mp3 to the end of the filename if the user didn't
					if (!fileName.endsWith(".mp3")) {
						fileName = fileName + ".mp3";
					}

					// Calling method to save the comment as an mp3 file
					try {
						saveToMp3(fileName);
						JOptionPane.showMessageDialog(contentPane,"Commentary sucessfully saved as " + fileName);
					} catch (IOException | InterruptedException e) {
						JOptionPane.showMessageDialog(contentPane,"Error saving commentary","Error saving file", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		voiceGenderComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {				
				// Creating new synthesis worker to convert the text file into a wav file 
				synthesisWorker = new SpeechSynthesisWorker(text, "commentary" + position, getAudioOverlay()) {
					@Override
					protected void done() {

						super.done();
						// Allowing the play button to be clicked
						playButton.setEnabled(true);
						// Getting the file path of the wav file
						CommentaryOverlay.this.filePath = synthesisWorker.filePath;

						// Getting the duration of the audio and storing it
						try {
							float duration = getDuration(filePath);
							String durationText = (int) (duration / 60) + ":" + (int) (duration % 60) + 
									"." + (int) ((duration - (int) duration)*1000);

							// Setting the text in the label to the duration value
							durationLabel.setText(durationText);
						} catch (IOException | InterruptedException e) {
							// Error handling if there was a problem while getting duration
							durationLabel.setText("???");
						}
					}
				};
				synthesisWorker.execute(); // Executing the process
			}
		});

		voicePitchComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {				
				// Creating new synthesis worker to convert the text file into a wav file 
				synthesisWorker = new SpeechSynthesisWorker(text, "commentary" + position, getAudioOverlay()) {
					@Override
					protected void done() {

						super.done();
						// Allowing the play button to be clicked
						playButton.setEnabled(true);
						// Getting the file path of the wav file
						CommentaryOverlay.this.filePath = synthesisWorker.filePath;

						// Getting the duration of the audio and storing it
						try {
							float duration = getDuration(filePath);
							String durationText = (int) (duration / 60) + ":" + (int) (duration % 60) + 
									"." + (int) ((duration - (int) duration)*1000);

							// Setting the text in the label to the duration value
							durationLabel.setText(durationText);
						} catch (IOException | InterruptedException e) {
							// Error handling if there was a problem while getting duration
							durationLabel.setText("???");
						}
					}
				};
				synthesisWorker.execute(); // Executing the process
			}
		});
		// Checking if the text is not empty and setting the text in the text field
		if (text != null) {
			textField.setText(text);
		}

		return contentPane;
	}

	/**
	 * Saves this commentary to a mp3 file
	 * @param outputFilePath the file to save to
	 */
	private void saveToMp3(final String outputFilePath) throws IOException, InterruptedException {
		//create a  ffmpeg process to process the commentary file to an mp3
		//(note: this is fast enough to run on the main thread without noticeable effect on UI)
		String cmd="ffmpeg -y -i \""+getFilePath()+"\" \""+outputFilePath+"\"";
		new ProcessBuilder("/bin/bash","-c",cmd).start().waitFor();
	}

    /**
     * This method is used to inform the user when the duration of their commentary is too long
     */
    protected void enableAudioErrorMessage(ControlsPanel controlsPanel, float duration) {  	
		if (controlsPanel != null) {
			// Checking if there is a video playing or not
			if (controlsPanel.getVideoPlayer().getMediaPlayer().isPlayable()) {
				// Checking the times
				if ((duration + startTime) > (controlsPanel.getTotalTime()/1000)) {
					JOptionPane.showMessageDialog(null, "Audio is longer than the video", "Warning", JOptionPane.WARNING_MESSAGE);
				}
			}
		}
    }
	
	/**
	 * This method is called whenever this class is needed to be printed out
	 * It allows useful information such as position, text, start time, and volume to be obtained
	 */
	@Override
	public String toString() {
		String s = "C\t" + position + "\t" + text + "\t" + startTime + "\t" + volume;
		return s;
	}

	/**
	 * This method takes in a string 's' and splits it to get the fields position, text, start time and volume and creates a class of type
	 * CommentaryOverlay with those fields
	 */
	public static CommentaryOverlay fromString(String s) {
		int position = Integer.parseInt(s.split("\\t")[1]);									// Getting position
		String text = s.split("\\t")[2];	 												// Getting text	
		float startTime = Float.parseFloat(s.split("\\t")[3]); 								// Getting start time
		int volume=Integer.parseInt(s.split("\\t")[4]);										// Getting volume 	
		return new CommentaryOverlay(position, text, startTime, volume, controlsPanel);		// Creating new object of this class
	}

	public JComboBox getVoiceGenderComboBox() {
		return voiceGenderComboBox;
	}
	
	public JComboBox getVoicePitchComboBox() {
		return voicePitchComboBox;
	}
}