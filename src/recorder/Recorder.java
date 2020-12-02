package recorder;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.monte.media.Format;
import org.monte.media.FormatKeys;
import static org.monte.media.FormatKeys.EncodingKey;
import static org.monte.media.FormatKeys.FrameRateKey;
import static org.monte.media.FormatKeys.KeyFrameIntervalKey;
import static org.monte.media.FormatKeys.MIME_AVI;
import static org.monte.media.FormatKeys.MediaTypeKey;
import static org.monte.media.FormatKeys.MimeTypeKey;
import static org.monte.media.VideoFormatKeys.CompressorNameKey;
import static org.monte.media.VideoFormatKeys.DepthKey;
import static org.monte.media.VideoFormatKeys.ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE;
import static org.monte.media.VideoFormatKeys.QualityKey;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;

public class Recorder{
	Rectangle screenSize = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
	int DEPTH = 24;
	int FRAMERATE = 30;
	float QUALITY = 0.9f;
	int KEYFRAME_INTERVAL = 30 * 60;
	int FRAME_RATE_KEY = 30;
	ScreenRecorder cap;
	Boolean isRecording = false;
	File OutputFolder;
	GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();

	// DEFAULT VALUES
//	private static final int DEPTH = 24;
//	private static final int FRAMERATE = 15;
//	private static final float QUALITY = 1.0f;
//	private static final int KEYFRAME_INTERVAL = 15 * 60;
//	private static final int FRAME_RATE_KEY = 30;
	
	Format fileFormat = new Format(MediaTypeKey, FormatKeys.MediaType.FILE, MimeTypeKey, MIME_AVI);
	
	Format screenFormat = new Format(MediaTypeKey,
			FormatKeys.MediaType.VIDEO,
			EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
			CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
			DepthKey, DEPTH,
			FrameRateKey, Rational.valueOf(FRAMERATE),
			QualityKey, QUALITY,
			KeyFrameIntervalKey, KEYFRAME_INTERVAL);
	
	Format mouseFormat = new Format(MediaTypeKey,
			FormatKeys.MediaType.VIDEO,
			EncodingKey, "black",
			FrameRateKey, Rational.valueOf(FRAME_RATE_KEY));
	
	Recorder(String OutputFolder) throws Exception{
		if(createDir(OutputFolder)) {
			this.OutputFolder = new File(OutputFolder);
			initRecorder();
		}
		else{
			throw new IOException("Something Went Wrong with I/O Operation");
		}
	}
	
	Boolean reloadGraphicSettings() {
		Boolean status = true;
		try {
			this.gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
			this.initRecorder();
		}
		catch(Exception e) {
			status = false;
			e.printStackTrace();
		}
		return status;	
	}
	
	Boolean initFileFormat() {
		Boolean status = true;
		try {
			this.fileFormat = new Format(MediaTypeKey, FormatKeys.MediaType.FILE, MimeTypeKey, MIME_AVI);
			this.initRecorder();
		}
		catch(Exception e) {
			status = false;
			e.printStackTrace();
		}
		return status;
	}
	
	Boolean initScreenFormat() {
		Boolean status = true;
		try {
			this.screenFormat = new Format(MediaTypeKey,
				FormatKeys.MediaType.VIDEO,
				EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
				CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
				DepthKey, this.DEPTH,
				FrameRateKey, Rational.valueOf(this.FRAMERATE),
				QualityKey, this.QUALITY,
				KeyFrameIntervalKey, this.KEYFRAME_INTERVAL);
                        
			this.initRecorder();
			}
		catch(Exception e) {
			status = false;
			e.printStackTrace();
		}
		return status;
	}
	
	Boolean initMouseFormat() {
		Boolean status = true;
		try {
			this.mouseFormat = new Format(MediaTypeKey,
				FormatKeys.MediaType.VIDEO,
				EncodingKey, "black",
				FrameRateKey, Rational.valueOf(FRAME_RATE_KEY));

			this.initRecorder();
			
			}
		catch(Exception e) {
			status = false;
			e.printStackTrace();
		}
		
		return status;
	}
		
	Boolean change_FRAMERATE(int val) {
		Boolean status = true;
		try {
			this.FRAMERATE = val;
                        change_KEYFRAME_INTERVAL(this.FRAMERATE);
			this.initScreenFormat();
                        print("Changed FPS to "+val);
		}
		catch(Exception e) {
			status = false;
			e.printStackTrace();
		}
		return status;
	}	

	Boolean change_DEPTH(int val) {
		Boolean status = true;
		try {
			this.DEPTH = val;
			this.initScreenFormat();
                        print("Changed Depth to "+val);

			}
		catch(Exception e) {
			status = false;
			e.printStackTrace();
		}
		return status;
	}
	
	Boolean change_QUALITY(float val) {
		Boolean status = true;
		try {
                    
			this.QUALITY = val;
                        print("Changed Quality to "+val*100+"%");

			this.initScreenFormat();
                        
			}
		catch(Exception e) {
			status = false;
			e.printStackTrace();
		}
		return status;
	}

	Boolean change_KEYFRAME_INTERVAL(int val) {
		Boolean status = true;
		try {
			this.KEYFRAME_INTERVAL = 60 * val;
			this.initMouseFormat();
                        print("Changed KeyFrameRate Interval to "+val);
			}
		catch(Exception e) {
			status = false;
			e.printStackTrace();
		}
		return status;
	}

	Boolean change_FRAME_RATE_KEY(int val) {
		Boolean status = true;
		try {
			this.FRAME_RATE_KEY = val;
                        initMouseFormat();
			this.initScreenFormat();
                        print("Changed FrameRate Key to "+val);
		}
		catch(Exception e) {
			status = false;
			e.printStackTrace();
		}
		return status;
	}
	
	Boolean reInitSettings() {
		Boolean status = true;
		System.gc();
		try {
			this.reloadGraphicSettings();
			this.initFileFormat();
			this.initMouseFormat();
			this.initScreenFormat();
		}
		catch(Exception e) {
			status = false;
			e.printStackTrace();
		}
		return status;
	}
	
	Boolean createDir(String dir) {
		Boolean status = true;
		
		Path optPath = Paths.get(dir);
		if(!Files.isDirectory(optPath))
			try {
				Files.createDirectory(optPath);
			} catch (Exception e) {
				status = false;
				e.printStackTrace();
			}
		
		return status;
	}
	
	void print(Object o) {
		System.out.println(o);
	}
	
	Boolean changeOutputFolder(String FOLDER_PATH) {
		Boolean status = true;
		try {
			this.OutputFolder = new File(FOLDER_PATH);
			this.initRecorder();
		}
		catch(Exception e) {status = false;e.printStackTrace();}
		return status;
	}
	
	Boolean initRecorder() {
		Boolean status = true;
		System.gc();
		try {
			this.cap = new ScreenRecorder(gc,this.screenSize,fileFormat,screenFormat,mouseFormat,null,OutputFolder);
                        print("Initialize Record Object...");
		} catch (final Exception ex) {status=false;ex.printStackTrace();}
		
		return status;
	}
	
	Boolean startRecording() {
		Boolean status = true;
		print("Started....");
		try {
			this.isRecording = true;
			this.cap.start();
		} catch (Exception e) {
			e.printStackTrace();
			status = false;
		}
		return status;
	}
	
	Boolean stopRecording() {
		Boolean status = true;
		try {
			this.cap.stop();

		} catch (Exception e) {
			e.printStackTrace();
			status = false;
		}
		this.isRecording = false;
		print("Stopped....");
		return status;
	}
        
        Boolean pauseRecording(){
            Boolean status = true;
            
            try {
                this.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Recorder.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            return status;
        }
        
        Boolean resumeRecording(){
            Boolean status = true;
            
            this.notify();
            
            return status;
        }
        
        
        
}
