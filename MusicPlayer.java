import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/*
 * No credits for this file, downloaded from Internet and in some ways changed.
 */
public class MusicPlayer implements Runnable {

	static final int BUF_SIZE = 16384;
	private AudioInputStream audioInputStream;
	private AudioFormat format;
	SourceDataLine line;
	static Thread thread = new Thread();
	private final File linkedFile;
	private final float volume;
	private static float standardVolume = 50f;

	public MusicPlayer(AudioInputStream audioInputStream) {
		this.audioInputStream = audioInputStream;
		format = audioInputStream.getFormat();
		linkedFile = null;
		volume = standardVolume;
	}

	public MusicPlayer(File wavFile) {
		this(wavFile, standardVolume);
	}
	
	public MusicPlayer(File wavFile, float volume) {
		try {
			this.audioInputStream = AudioSystem.getAudioInputStream(wavFile);
			format = audioInputStream.getFormat();
		} catch (Exception e) {
		}
		linkedFile = wavFile;
		this.volume = volume;
	}

	public void start() {
		thread.setName("Playback");
			thread.start();
	}

	public void stop() {
		thread = null;
	}

	public void shutDown(final String message) {
		if (thread != null) {
			thread = null;
		}
		System.out.println(message);
	}

	@Override
	public void run() {
		// make sure we have something to play
				if (audioInputStream == null) {
					shutDown("No loaded audio to play back");
					return;
				}

				final AudioInputStream playbackInputStream = AudioSystem
						.getAudioInputStream(format, audioInputStream);
				if (playbackInputStream == null) {
					shutDown("Unable to convert stream of format " + audioInputStream
							+ " to format " + format);
					return;
				}
				line = getSourceDataLineForPlayback();

				FloatControl volume_control = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
				volume_control.setValue((float) (Math.log(volume / 100.0f) / Math.log(10.0f) * 20.0f));
				
				final int frameSizeInBytes = format.getFrameSize();
				final int bufferLengthInFrames = line.getBufferSize() / 8;
				final int bufferLengthInBytes = bufferLengthInFrames * frameSizeInBytes;
				final byte[] audioBuffer = new byte[bufferLengthInBytes];
				int numBytesRead = 0;

				line.start();
				while (thread != null) {
					try {
						if ((numBytesRead = playbackInputStream.read(audioBuffer)) == -1) {
							break;
						}
						int numBytesRemaining = numBytesRead;
						while (numBytesRemaining > 0) {
							numBytesRemaining -= line.write(audioBuffer, 0,
									numBytesRemaining);
						}
					} catch (final Exception e) {
						shutDown("Error during playback: " + e);
						break;
					}
				}

				if (thread != null) {
					line.drain();
				}
				line.stop();
				line.close();
				line = null;
				thread = null;
	}

	private SourceDataLine getSourceDataLineForPlayback() {
		SourceDataLine line;
		final DataLine.Info info = new DataLine.Info(SourceDataLine.class,
				format);
		if (!AudioSystem.isLineSupported(info)) {
			return null;
		}

		try {
			line = (SourceDataLine) AudioSystem.getLine(info);
			line.open(format, BUF_SIZE);
		} catch (final LineUnavailableException ex) {
			return null;
		}
		return line;
	}
	
	public File getLinkedFile() {
		return linkedFile;
	}

	public static float getStandardVolume() {
		return standardVolume;
	}

	public static void setStandardVolume(float standardVolume) {
		MusicPlayer.standardVolume = standardVolume;
	}

	public float getVolume() {
		return volume;
	}
}
