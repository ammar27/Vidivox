package vidivox.worker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import vidivox.audio.AudioOverlay;
import vidivox.ui.AudioOverlaysDialog;
import vidivox.ui.BottomControlsPanel;
import vidivox.ui.ProgressDialog;

/**
 * This class is used to create the ffmpeg process which merges the audio tracks and video file together into one video file
 * @author Ammar Bagasrawala
 *
 */
public class ExportWorker extends SwingWorker<Void, AudioOverlay> {
	/**
	 * Declaring fields to be used within this class
	 */
	private int progress = 0;		// Initial progress
	private String videoPath;		// String containing the path to the video
	private ProgressDialog progressDialog;
	private List<AudioOverlay> overlays;
	private File file;
	private BottomControlsPanel bottomControlsPanel;

	/**
	 * Constructor to initialize fields
	 */
	public ExportWorker (File file, BottomControlsPanel bottomControlsPanel, String videoPath) {
		this.videoPath = videoPath;
		this.bottomControlsPanel = bottomControlsPanel;
		this.file = file;
		this.progressDialog = new ProgressDialog();
		progressDialog.setVisible(true);

		this.overlays = AudioOverlaysDialog.getOverlays();
	}

	/**
	 * The doInBackground method handles time consuming tasks in the background so as to not make the GUI freeze
	 * This method is used to merge the audio and video files together into one video file
	 * where the audio specified by the user overlaps the existing audio
	 */
	@Override
	protected Void doInBackground() throws Exception {

		// Processing all of the audio files
		List<String> audioPaths = new ArrayList<>();
		List<Double> audioOffsets = new ArrayList<>();

		for (AudioOverlay overlay : overlays) {
			// Calling the process method to interact with the GUI
			publish(overlay);
			// Adding the file path of the audio files to the arraylist containing 
			// the paths of all audio files
			audioPaths.add(overlay.getFilePath());
			audioOffsets.add(overlay.getStartTime());
		}

		// Combining all the audio files with the video files using an ffmpeg process
		publish((AudioOverlay) null);

		// Building the command to pass into the process builder
		StringBuilder cmd = new StringBuilder("ffmpeg -y -i \"" + videoPath +"\"");
		
		// Appending the audio path of each audio file to the command. Keep count of the number of audio files added
		int audioTracksAdded=0;

		for (int i = 0; i < audioPaths.size(); i++) {
			// Only add audio files which aren't empty
			if (audioPaths.get(i) != null && !audioPaths.get(i).isEmpty()) {
				if (audioOffsets.get(i) > 0) {
					cmd.append(" -itsoffset " + audioOffsets.get(i));
				}
				cmd.append(" -i \"" + audioPaths.get(i) + "\"");
				audioTracksAdded++;
			}
		}

		for (int i = 0; i < audioTracksAdded + 1; i++) {
			 cmd.append(" -map " + i + ":0");
		}

		// If audio tracks have been added then append the command to mix them
		if (audioTracksAdded > 0) {
			cmd.append(" -c:v copy -async 1 -filter_complex amix=inputs=" + (audioTracksAdded+1));
		}
				
		String fileName = file.getName();
		String path = file.getAbsolutePath();

		if (!fileName.endsWith(".mp4")) {
			path = file.getAbsolutePath();
			path = (path + ".mp4");
		}

		cmd.append(" \"" + path + "\"");		

		// Building process and process builder to run the command then starting it
		Process process = new ProcessBuilder("/bin/bash", "-c", cmd.toString()).start();

		// Causes the current thread to wait if necessary
		process.waitFor();

		// Closing the progress dialog indicating how long merging video and audio will take
		progressDialog.close();
		return null;
	}

	/**
	 * Process method called when the GUI needs to be updated at stages of the doInBackground method
	 */
	@Override
	protected void process(List<AudioOverlay> chunks) {

		// Checking if the size of the list is 0 and returning if so
		if (chunks.size() == 0) {
			return;
		}

		// Adding the size of the audio list to the progress variable in order
		// to inform user how much time is left
		progress += chunks.size();
		//progressDialog.setOverallProgress(progress-1);

		// Informing the user as to which audio overlay is being processed at a given time
		AudioOverlay currentlyProcessedOverlay = chunks.get(chunks.size()-1);
		if (currentlyProcessedOverlay != null) {
			// If there is an audio file being processed at a certain time then informing the user
			// as to which file is being processed
			progressDialog.setTaskProgressNote("Processing " + currentlyProcessedOverlay.getFileName() + "...");
		} else {
			// If there is no audio being processed then this message shows and informs the user that
			// they are in the final stages of exporting where the video and audio is being merged
			progressDialog.setTaskProgressNote("Merging video with audio tracks...");
		}
	}

	/**
	 * done() method which informs the user when the export is completed successfully
	 */
	@Override
	protected void done() {
		// Dialog popup to inform user of successful export
		JOptionPane.showMessageDialog(bottomControlsPanel,"Sucessfully exported project to "
				+ file.getAbsolutePath(), "Success!", JOptionPane.INFORMATION_MESSAGE);
	}
}
