package vidivox.ui;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;

/**
 * This class serves as an extension of the media player component from vlcj
 * 
 * @author Hanzhi Wang
 * @author Ammar Bagasrawala
 *
 */
public class VideoPlayerComponent extends EmbeddedMediaPlayerComponent {

	private String videoPath = null;
	
	/**
	 * Constructor called whenever video player is instantiated
	 */
	public VideoPlayerComponent() {
		// Setting the volume to the maximum possible
		getMediaPlayer().setVolume(100);
	}

	// Method to play video when the only information about it is the path
	public void playVideo(String path) {
		videoPath = path;
		getMediaPlayer().playMedia(path);
	}
	
	public String getVideoPath() {
		return videoPath;
	}
}
