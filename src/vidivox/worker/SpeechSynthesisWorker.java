package vidivox.worker;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JComboBox;
import javax.swing.SwingWorker;
import vidivox.audio.AudioOverlay;
import vidivox.audio.CommentaryOverlay;

/**
 * This class extends swing worker in order to perform a time consuming task of converting 
 * text into a wav file by running the conversion process in the background
 * 
 * @author Hanzhi Wang
 * @author Ammar Bagasrawala
 *
 */
public class SpeechSynthesisWorker extends SwingWorker<Void,Void> {

	/**
	 * Fields declared to be used within class and package
	 */
	protected String text;					// Text that is to be converted to audio
	protected Process synthesisProcess;		// Process which will convert text file to wav file
	public String filePath;					// Path to the file 
	private String voice;					// String specifying the selected voice type
	private String pitch;					// String specifying the selected pitch
	private JComboBox voiceBox;				// ComboBox for the voice type
	private JComboBox pitchBox;				// ComboBox for the pitch of the voice

	/**
	 * Constructor called when this class is instantiated, allowing the fields declared to be set
	 * 
	 * @param text - Text to be created into audio
	 * @param fileName - Name of the text file
	 */
	public SpeechSynthesisWorker(String text, String fileName, AudioOverlay audioOverlay) {
		this.text = text;
		// Creating file path to the wav file
		filePath = "/tmp/" + fileName;

		// Getting current selection in the combobox for choosing the type of voice
		voiceBox = ((CommentaryOverlay) audioOverlay).getVoiceGenderComboBox();

		// Getting current selection in the combobox for choosing the pitch of the voice
		pitchBox = ((CommentaryOverlay) audioOverlay).getVoicePitchComboBox();
	}

	/**
	 * Method which carries out time consuming task, in this case waiting creating the wav file for the voices and pitch
	 */
	@Override
	protected Void doInBackground() throws Exception {

		// Getting the selected option from the voice check box
		if (voiceBox.getSelectedItem() != null) {
			voice = (String) voiceBox.getSelectedItem();
		} else {
			voice = "Robotic";
		}

		// Getting the selected option from the pitch check box
		if (pitchBox.getSelectedItem() != null) {
			pitch = (String) pitchBox.getSelectedItem();
		} else {
			pitch = "Normal";
		}

		// Changing the voice command depending on user choice
		String voiceCommand = "voice_kal_diphone";
		if (voice.equals("British")) {
			voiceCommand = "voice_rab_diphone";
		} else if (voice.equals("NZ'er")) {
			voiceCommand = "voice_akl_nz_jdt_diphone";
		}

		// Changing the pitch input depending on user choice
		int pitchInt = 100;
		if (pitch.equals("Low")) {
			pitchInt = 50;
		} else if (pitch.equals("High")) {
			pitchInt = 300;
		}

		// Calling method to change the voice and pitch
		try {
			changeVoice(pitchInt, pitchInt, voiceCommand);
		} catch (IOException e) {
			e.printStackTrace();
		}

		synthesisProcess.waitFor();
		return null;
	}

	/**
	 * Method to stop the text to audio conversion process and destroy it
	 */
	public void kill() {
		if (synthesisProcess != null) {
			synthesisProcess.destroy();
		}
		cancel(true);
	}

	/**
	 * This method takes in two arguments, specifying the pitch at the start and end.
	 * It then builds a text file and scm file to pass into the arguments into text2wav
	 * so that the process can be built
	 * 
	 * @param start
	 * @param end
	 * @throws IOException
	 */
	public void changeVoice(int start, int end, String voiceCommand) throws IOException {

		// Building the text file
		File textFile = new File(filePath + ".txt");
		FileWriter fw = new FileWriter(textFile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(text);
		bw.close();

		// Building the string to specify the text in the scm file
		String scmFileText = "(" + voiceCommand + ")";
		if (pitch.equals("Low") || pitch.equals("High")) {
			scmFileText = scmFileText + "(set! duffint_params \'((start " + start + ") (end " + end + ")))\n";
			scmFileText = scmFileText + "(Parameter.set \'Int_Method \'DuffInt)\n";
			scmFileText = scmFileText + "(Parameter.set \'Int_Target_Method Int_Targets_Default)\n";
		}
		
		// Building the scm file
		File scmFile = new File(filePath + "SchemeFile.scm");
		FileWriter scmFileFw = new FileWriter(scmFile.getAbsoluteFile());
		BufferedWriter scmFileBw = new BufferedWriter(scmFileFw);
		scmFileBw.write(scmFileText);
		scmFileBw.close();

		// Building the process which is then started
		String cmd = "text2wave -o " + filePath + " " + textFile + " -eval " + scmFile;
		synthesisProcess = new ProcessBuilder("/bin/bash", "-c", cmd).start();
	}
}
