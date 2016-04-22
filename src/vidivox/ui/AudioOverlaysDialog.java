package vidivox.ui;
import javax.swing.*;

import vidivox.audio.AudioOverlay;
import vidivox.audio.CommentaryOverlay;
import vidivox.audio.FileOverlay;
import vidivox.worker.SkipVideoWorker;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the class for the Dialog which allows users to add and edit the audio tracks they wish to overlay onto their
 * video. It consists of container components generated from AudioOverlay objects.
 *
 * @author Hanzhi Wang
 * @author Ammar Bagasrawala
 *
 */
public class AudioOverlaysDialog extends JDialog {
	private static List<AudioOverlay> overlays = new ArrayList<>();                   // List of audio overlays added
	public static List<CommentaryOverlay> commentaryOverlays = new ArrayList<>();   // List of overlays which are commentary
	private List<JButton> deleteButtons;        // List of button for deleting each audio overlay track
	private JButton addCommentaryButton;        // A button for adding a commentary overlay
	private JButton addAudioButton;             // A button for adding an audio file (eg mp3) overlay
	private JButton playAudioVideoButton;				// Button to play the audio and the video
	private ControlsPanel controlsPanel;
	private VideoPlayerComponent videoPlayer;
	private static AudioOverlaysDialog audioDialog;

	/**
	 * Initializes the Dialog with layout and listeners
	 */
	public AudioOverlaysDialog(ControlsPanel controlsPanel) {
		super((Dialog)null);
		this.audioDialog = this;
		this.controlsPanel = controlsPanel;
		setupLayout();
		setupListeners();
		setMinimumSize(new Dimension(900, 400));
		pack();
	}

	/**
	 * Gets the list of audio overlays currently being added to the video
	 * @return the list of overlays
	 */
	public static List<AudioOverlay> getOverlays() {
		return overlays;
	}

	/**
	 * Sets the list of audio overlays currently being added to the video
	 * @param overlays the list to set to
	 */
	public static void setOverlays(List<AudioOverlay> overlays){
		AudioOverlaysDialog.overlays=overlays;
		audioDialog.setupLayout();
		audioDialog.setupListeners();
	}

	/**
	 * Sets up the listeners to respond to user input
	 */
	public void setupListeners() {
		videoPlayer = controlsPanel.getVideoPlayer();
		
		// Add commentary button creates a new commentary track and adds it to the dialog
		addCommentaryButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				CommentaryOverlay overlay = new CommentaryOverlay(overlays.size(), controlsPanel);
				overlays.add(overlay);
				commentaryOverlays.add(overlay);
				setupLayout();
				setupListeners();
			}
		});
		// Add audio button creates a new audio track and adds it to the dialog
		addAudioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				FileOverlay overlay=new FileOverlay();
				overlays.add(overlay);
				setupLayout();
				setupListeners();
			}
		});

		playAudioVideoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent actionEvent) {      
				SkipVideoWorker skipVid = controlsPanel.getSkipVid();

				JButton pauseButton = controlsPanel.getPauseButton();
				ImageIcon pauseIcon = controlsPanel.getPauseIcon();
				pauseButton.setIcon(pauseIcon);

				// Un-muting the video if it is muted
				videoPlayer.getMediaPlayer().mute(false);

				// Checking if the video is currently forwarding/rewinding and if it is then stopping it from skipping
				if (skipVid != null) {
					skipVid.setIsSkipping(false);
					skipVid.cancel(true);
					skipVid = null;
				}

				// Check through the commentaries to make sure they are all under 100 characters
				List<CommentaryOverlay> commentaryOverlays = AudioOverlaysDialog.commentaryOverlays;
				for(CommentaryOverlay overlay: commentaryOverlays){
					// Looking through the commentary overlays to check if the text is less than 100 characters
					if (overlay.getText().length() >= 100) {

						// Showing error message to user informing them character limit is 100
						JOptionPane.showMessageDialog(null, "Must specify comment less than or equal 100 characters", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					} 
				}
				controlsPanel.stopAudioPlayers();
				videoPlayer.getMediaPlayer().stop();
				// Playing the video
				videoPlayer.getMediaPlayer().play();
				// Starting the audio players
				controlsPanel.startAudioPlayers();
			}
		});

		// Set the delete button next to each track to delete that track when clicked
		for (int i = 0; i < deleteButtons.size(); i++) {
			final int position = i;
			deleteButtons.get(i).addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent actionEvent) {
					if (overlays.get(position).getAudioOverlay().getAudioWorker() != null) {
						overlays.get(position).getAudioOverlay().getAudioWorker().kill();
					}
					overlays.remove(position);
					setupLayout();
					setupListeners();
				}
			});
		}
	}

	/**
	 * Set up the layout of the dialog using a GridBagLayout
	 */
	public void setupLayout(){
		getContentPane().removeAll();
		JScrollPane scrollPane = new JScrollPane();
		JPanel contentPane = new JPanel();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setViewportView(contentPane);
		getContentPane().add(scrollPane);

		contentPane.setLayout(new GridBagLayout());
		GridBagConstraints gbc=new GridBagConstraints();
		gbc.insets=new Insets(5,5,5,5);

		//create a panel for holding the buttons at the top of the dialog
		JPanel buttonsPanel=new JPanel();
		FlowLayout buttonsLayout = new FlowLayout();
		buttonsPanel.setLayout(buttonsLayout);
		buttonsLayout.setHgap(10);
		addCommentaryButton=new JButton("Add Commentary");
		addCommentaryButton.setPreferredSize(new Dimension(180, 30));
		addCommentaryButton.setToolTipText("Add commentary to be converted to audio");
		buttonsPanel.add(addCommentaryButton);

		addAudioButton=new JButton("Add Audio File");
		addAudioButton.setToolTipText("Add mp3 file");
		addAudioButton.setPreferredSize(new Dimension(180, 30));
		buttonsPanel.add(addAudioButton);

		playAudioVideoButton = new JButton("Play Audio & Video");
		playAudioVideoButton.setToolTipText("Preview the audio with the video");
		playAudioVideoButton.setPreferredSize(new Dimension(180, 30));
		// Checking if there is a current video added
		if (videoPlayer == null) {
			playAudioVideoButton.setEnabled(false);
		}
		buttonsPanel.add(playAudioVideoButton);

		gbc.gridwidth=3;
		gbc.gridy=0;
		gbc.fill= GridBagConstraints.HORIZONTAL;
		gbc.anchor=GridBagConstraints.NORTH;
		contentPane.add(buttonsPanel,gbc);

		deleteButtons = new ArrayList<>();

		gbc.gridwidth = 1;

		//create a panel for holding the components to display the audio overlay tracks
		for (AudioOverlay overlay:overlays) {
			Component overlayDisplay=overlay.getComponentView();

			//add a divider between each overlay
			gbc.gridy++;
			gbc.gridx=0;
			gbc.gridwidth=2;
			contentPane.add(new JSeparator(JSeparator.HORIZONTAL),gbc);

			//set up the layout for the overlay controls
			gbc.gridy++;
			gbc.gridx=0;
			gbc.gridwidth=1;
			gbc.weightx=1.0f;
			gbc.anchor=GridBagConstraints.CENTER;
			contentPane.add(overlayDisplay,gbc);

			//add a delete button for removing the track
			gbc.gridx=1;
			gbc.weightx=0.0f;
			gbc.anchor=GridBagConstraints.CENTER;
			JButton deleteButton=new JButton("X");
			deleteButton.setForeground(Color.red);
			deleteButton.setPreferredSize(new Dimension(50, 36));
			deleteButton.setToolTipText("Delete the commentary from this project");
			deleteButtons.add(deleteButton);
			contentPane.add(deleteButton,gbc);
		}

		gbc.gridy++;
		gbc.weighty=1.0f;
		contentPane.add(new JPanel(),gbc);

		doLayout();
		revalidate();
	}

	public void enablePlayAudioVideoButton () {
		playAudioVideoButton.setEnabled(true);
	}
}
