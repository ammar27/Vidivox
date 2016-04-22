package vidivox.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import uk.co.caprica.vlcj.binding.LibVlcConst;

/**
 * This class is used to create a panel which shows the list of video effects
 * available for the video. It allows for the changing of brightness, hue, contrast,
 * saturation and gamma through the use of JSlider components.
 * 
 * @author Ammar Bagasrawala
 */
public class VideoEffectsPanel extends JPanel {
	
	/**
	 * Declaring fields to be globally used in this class
	 */
	private VideoPlayerComponent videoPlayer;	// Reference to the videoPlayer component

	private JSlider brightnessSlider;			// Fields for the changing of the brightness 
	private JLabel brightnessLabel;
	private JButton resetBrightness;
	
	private JSlider hueSlider;					// Fields for the changing of the hue 
	private JLabel hueLabel;
	private JButton resetHue;
	
	private JSlider contrastSlider;				// Fields for the changing of the contrast 
	private JLabel contrastLabel;
	private JButton resetContrast;
	
	private JSlider saturationSlider;			// Fields for the changing of the saturation 
	private JLabel saturationLabel;
	private JButton resetSaturation;
	
	private JSlider gammaSlider;				// Fields for the changing of the gamma 
	private JLabel gammaLabel;
	private JButton resetGamma;
		
	/**
	 * Constructor called to set up the layout and listeners for the sliders and reset buttons
	 * @param videoPlayer
	 */
	public VideoEffectsPanel(VideoPlayerComponent videoPlayer) {
		this.videoPlayer = videoPlayer;
		setupLayout();
		setupListeners();
	}

	/**
	 * This method sets up the layout for all of the sliders, labels and reset buttons used
	 * in this panel. It sets up the layout using a GridBagLayout.
	 */
	public void setupLayout() {

		// Setting up a panel within this panel to allow for ease of layout configuration
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5,5,5,5);

		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridBagLayout());
		
		// Label for the brightness
		brightnessLabel = new JLabel("Brightness");
		gbc.gridx=0;
		gbc.gridy=2;
		gbc.weightx=1.0f;
		gbc.weighty=1.0f;       
		buttonsPanel.add(brightnessLabel, gbc);

		// Slider for the brightness of the video
		brightnessSlider = new JSlider();
		brightnessSlider.setValue(100);
		brightnessSlider.setMinimum(Math.round(LibVlcConst.MIN_BRIGHTNESS * 100.0f));
		brightnessSlider.setMaximum(Math.round(LibVlcConst.MAX_BRIGHTNESS * 100.0f));
		brightnessSlider.setMajorTickSpacing(50);
		brightnessSlider.setMinorTickSpacing(5);
		brightnessSlider.setPaintTicks(true);
		brightnessSlider.setPaintLabels(true);
		gbc.gridx=0;
		gbc.gridy=3;
		gbc.weightx=1.0f;
		gbc.weighty=1.0f;        
		buttonsPanel.add(brightnessSlider, gbc);
		
		// Reset button for the brightness of the video
		resetBrightness = new JButton("Reset");
		gbc.gridx=1;
		gbc.gridy=3;
		gbc.weightx=1.0f;
		gbc.weighty=1.0f;        
		buttonsPanel.add(resetBrightness, gbc);
		
		// Adding separator
		gbc.gridy=4;
		gbc.gridx = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		buttonsPanel.add(new JSeparator(JSeparator.HORIZONTAL), gbc);
		gbc.fill = 0;

		// Label for the hue
		hueLabel = new JLabel("Hue");
		gbc.gridx=0;
		gbc.gridy=5;
		gbc.weightx=1.0f;
		gbc.weighty=1.0f;       
		buttonsPanel.add(hueLabel, gbc);

		// Slider for the hue of the video
		hueSlider = new JSlider();
		hueSlider.setValue(100);
		hueSlider.setMinimum(Math.round(LibVlcConst.MIN_HUE));
		hueSlider.setMaximum(Math.round(LibVlcConst.MAX_HUE-60));
		hueSlider.setMajorTickSpacing(100);
		hueSlider.setMinorTickSpacing(10);
		hueSlider.setPaintTicks(true);
		hueSlider.setPaintLabels(true);
		hueSlider.setValue(0);
		gbc.gridx=0;
		gbc.gridy=6;
		gbc.weightx=1.0f;
		gbc.weighty=1.0f;        
		buttonsPanel.add(hueSlider, gbc);

		// Reset button for the hue of the video
		resetHue = new JButton("Reset");
		gbc.gridx=1;
		gbc.gridy=6;
		gbc.weightx=1.0f;
		gbc.weighty=1.0f;        
		buttonsPanel.add(resetHue, gbc);
		
		// Adding separator
		gbc.gridy=7;
		gbc.gridx=0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		buttonsPanel.add(new JSeparator(JSeparator.HORIZONTAL), gbc);
		gbc.fill = 0;

		// Label for the contrast
		contrastLabel = new JLabel("Contrast");
		gbc.gridx=0;
		gbc.gridy=8;
		gbc.weightx=1.0f;
		gbc.weighty=1.0f;       
		buttonsPanel.add(contrastLabel, gbc);

		// Slider for the contrast of the video
		contrastSlider = new JSlider();
		contrastSlider.setValue(100);
		contrastSlider.setMinimum(Math.round(LibVlcConst.MIN_CONTRAST * 100));
		contrastSlider.setMaximum(Math.round(LibVlcConst.MAX_CONTRAST * 100));
		contrastSlider.setMajorTickSpacing(50);
		contrastSlider.setMinorTickSpacing(10);
		contrastSlider.setPaintTicks(true);
		contrastSlider.setPaintLabels(true);
		gbc.gridx=0;
		gbc.gridy=9;
		gbc.weightx=1.0f;
		gbc.weighty=1.0f;        
		buttonsPanel.add(contrastSlider, gbc);
		
		// Reset button for the contrast of the video
		resetContrast = new JButton("Reset");
		gbc.gridx=1;
		gbc.gridy=9;
		gbc.weightx=1.0f;
		gbc.weighty=1.0f;        
		buttonsPanel.add(resetContrast, gbc);
		
		// Adding separator
		gbc.gridy=10;
		gbc.gridx=0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		buttonsPanel.add(new JSeparator(JSeparator.HORIZONTAL), gbc);
		gbc.fill = 0;

		// Label for the saturation
		saturationLabel = new JLabel("Saturation");
		gbc.gridx=0;
		gbc.gridy=11;
		gbc.weightx=1.0f;
		gbc.weighty=1.0f;       
		buttonsPanel.add(saturationLabel, gbc);

		// Slider for the saturation of the video
		saturationSlider = new JSlider();
		saturationSlider.setValue(100);
		saturationSlider.setMinimum(Math.round(LibVlcConst.MIN_SATURATION * 100));
		saturationSlider.setMaximum(Math.round(LibVlcConst.MAX_SATURATION * 100));
		saturationSlider.setMajorTickSpacing(100);
		saturationSlider.setMinorTickSpacing(10);
		saturationSlider.setPaintTicks(true);
		saturationSlider.setPaintLabels(true);
		gbc.gridx=0;
		gbc.gridy=12;
		gbc.weightx=1.0f;
		gbc.weighty=1.0f;        
		buttonsPanel.add(saturationSlider, gbc);
		
		// Reset button for the saturation of the video
		resetSaturation = new JButton("Reset");
		gbc.gridx=1;
		gbc.gridy=12;
		gbc.weightx=1.0f;
		gbc.weighty=1.0f;        
		buttonsPanel.add(resetSaturation, gbc);
		
		// Adding separator
		gbc.gridy=13;
		gbc.gridx=0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		buttonsPanel.add(new JSeparator(JSeparator.HORIZONTAL), gbc);
		gbc.fill = 0;

		// Label for the gamma
		gammaLabel = new JLabel("Gamma");
		gbc.gridx=0;
		gbc.gridy=14;
		gbc.weightx=1.0f;
		gbc.weighty=1.0f;       
		buttonsPanel.add(gammaLabel, gbc);

		// Slider for the hue of the video
		gammaSlider = new JSlider();
		gammaSlider.setValue(100);
		gammaSlider.setMinimum(Math.round(LibVlcConst.MIN_GAMMA * 100 - 1));
		gammaSlider.setMaximum(Math.round(LibVlcConst.MAX_GAMMA * 100 + 1));
		gammaSlider.setMajorTickSpacing(200);
		gammaSlider.setMinorTickSpacing(100);
		gammaSlider.setPaintTicks(true);
		gammaSlider.setPaintLabels(true);
		gbc.gridx=0;
		gbc.gridy=15;
		gbc.weightx=1.0f;
		gbc.weighty=1.0f;        
		buttonsPanel.add(gammaSlider, gbc);

		// Reset button for the gamma of the video
		resetGamma = new JButton("Reset");
		gbc.gridx=1;
		gbc.gridy=15;
		gbc.weightx=1.0f;
		gbc.weighty=1.0f;        
		buttonsPanel.add(resetGamma, gbc);
		
		contentPane.add(buttonsPanel, gbc);
		add(contentPane);
		doLayout();
		revalidate();
	}

	/**
	 * This method is used to set up the listeners for each of the JSlider components
	 * and each of the reset buttons located within this panel class
	 */
	public void setupListeners() {

		// Listener for the brightness of the video
		brightnessSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				// Disabling and then enabling to fix problem where it wouldn't work
				videoPlayer.getMediaPlayer().setAdjustVideo(false);
				videoPlayer.getMediaPlayer().setAdjustVideo(true);

				// Getting the source and setting the brightness to users choice
				JSlider source = (JSlider) e.getSource();
				videoPlayer.getMediaPlayer().setBrightness(source.getValue()/100.0f);				
			}
		});

		// Listener for the hue of the video
		hueSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				// Disabling and then enabling to fix problem where it wouldn't work
				videoPlayer.getMediaPlayer().setAdjustVideo(false);
				videoPlayer.getMediaPlayer().setAdjustVideo(true);

				// Getting the source and setting the brightness to users choice
				JSlider source = (JSlider) e.getSource();
				videoPlayer.getMediaPlayer().setHue(source.getValue());				
			}
		});

		// Listener for the contrast slider of the video
		contrastSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				// Disabling and then enabling to fix problem where it wouldn't work
				videoPlayer.getMediaPlayer().setAdjustVideo(false);
				videoPlayer.getMediaPlayer().setAdjustVideo(true);

				// Getting the source and setting the contrast to users choice
				JSlider source = (JSlider) e.getSource();
				videoPlayer.getMediaPlayer().setContrast(source.getValue()/100);				
			}
		});

		// Listener for the saturation slider of the video
		saturationSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				// Disabling and then enabling to fix problem where it wouldn't work
				videoPlayer.getMediaPlayer().setAdjustVideo(false);
				videoPlayer.getMediaPlayer().setAdjustVideo(true);

				// Getting the source and setting the brightness to users choice
				JSlider source = (JSlider) e.getSource();
				videoPlayer.getMediaPlayer().setSaturation(source.getValue()/100);				
			}
		});

		// Listener for the gamma slider of the video
		gammaSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				// Disabling and then enabling to fix problem where it wouldn't work
				videoPlayer.getMediaPlayer().setAdjustVideo(false);
				videoPlayer.getMediaPlayer().setAdjustVideo(true);

				// Getting the source and setting the brightness to users choice
				JSlider source = (JSlider) e.getSource();
				videoPlayer.getMediaPlayer().setGamma(source.getValue()/100);				
			}
		});
		
		// Reset listener for the brightness of the videosetupListeners
		resetBrightness.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				videoPlayer.getMediaPlayer().setBrightness(1);
				brightnessSlider.setValue(100);
			}
		});
		
		// Reset listener for the hue of the video
		resetHue.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				videoPlayer.getMediaPlayer().setHue(0);
				hueSlider.setValue(0);
			}
		});
		
		// Reset listener for the saturation of the video
		resetSaturation.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				videoPlayer.getMediaPlayer().setSaturation(1);
				saturationSlider.setValue(100);
			}
		});
		
		// Reset listener for the contrast of the video
		resetContrast.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				videoPlayer.getMediaPlayer().setContrast(1);
				contrastSlider.setValue(100);
			}
		});
		
		// Reset listener for the gamma of the video
		resetGamma.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				videoPlayer.getMediaPlayer().setGamma(1);
				gammaSlider.setValue(100);
			}
		});
	}
}
