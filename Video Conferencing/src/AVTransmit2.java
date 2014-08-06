package messengerclient;

import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import javax.media.*;
import javax.media.protocol.*;
import javax.media.protocol.DataSource;
import javax.media.format.*;
import javax.media.control.TrackControl;
import javax.media.control.QualityControl;
import javax.media.rtp.*;
import javax.media.rtp.rtcp.*;
import com.sun.media.rtp.*;

public class AVTransmit2 {

    // Input MediaLocator
    // Can be a file or http or capture source
    private MediaLocator locator;
    private String ipAddress;
    private int portBase;

    private Processor processor = null;
    private RTPManager rtpMgrs[];
    private DataSource dataOutput = null;
    
    public AVTransmit2(MediaLocator locator,
			 String ipAddress,
			 String pb,
			 Format format) {
	
	this.locator = locator;
	this.ipAddress = ipAddress;
	Integer integer = Integer.valueOf(pb);
	if (integer != null)
	    this.portBase = integer.intValue();
    }

    /**
     * Starts the transmission. Returns null if transmission started ok.
     * Otherwise it returns a string with the reason why the setup failed.
     */
    public synchronized String start() {
	String result;

	// Create a processor for the specified media locator
	// and program it to output JPEG/RTP
	result = createProcessor();
	if (result != null)
	    return result;

	// Create an RTP session to transmit the output of the
	// processor to the specified IP address and port no.
	result = createTransmitter();
	if (result != null) {
	    processor.close();
	    processor = null;
	    return result;
	}

	// Start the transmission
	processor.start();
	
	return null;
    }

    /**
     * Stops the transmission if already started
     */
    public void stop() {
	synchronized (this) {
	    if (processor != null) {
		processor.stop();
		processor.close();
		processor = null;
		for (int i = 0; i < rtpMgrs.length; i++) {
		    rtpMgrs[i].removeTargets( "Session ended.");
		    rtpMgrs[i].dispose();
		}
	    }
	}
    }

    private String createProcessor() {
	if (locator == null)
	    return "Locator is null";

	DataSource ds;
	DataSource clone;

	try {
	    ds = javax.media.Manager.createDataSource(locator);
	} catch (Exception e) {
	    return "Couldn't create DataSource";
	}

	// Try to create a processor to handle the input media locator
	try {
	    processor = javax.media.Manager.createProcessor(ds);
	} catch (NoProcessorException npe) {
	    return "Couldn't create processor";
	} catch (IOException ioe) {
	    return "IOException creating processor";
	} 

	// Wait for it to configure
	boolean result = waitForState(processor, Processor.Configured);
	if (result == false)
	    return "Couldn't configure processor";

	// Get the tracks from the processor
	TrackControl [] tracks = processor.getTrackControls();

	// Do we have atleast one track?
	if (tracks == null || tracks.length < 1)
	    return "Couldn't find tracks in processor";

	// Set the output content descriptor to RAW_RTP
	// This will limit the supported formats reported from
	// Track.getSupportedFormats to only valid RTP formats.
	ContentDescriptor cd = new ContentDescriptor(ContentDescriptor.RAW_RTP);
	processor.setContentDescriptor(cd);

	Format supported[];
	Format chosen;
	boolean atLeastOneTrack = false;

	// Program the tracks.
	for (int i = 0; i < tracks.length; i++) {
	    Format format = tracks[i].getFormat();
	    if (tracks[i].isEnabled()) {

		supported = tracks[i].getSupportedFormats();

		// We've set the output content to the RAW_RTP.
		// So all the supported formats should work with RTP.
		// We'll just pick the first one.

		if (supported.length > 0) {
		    if (supported[0] instanceof VideoFormat) {
			// For video formats, we should double check the
			// sizes since not all formats work in all sizes.
			chosen = checkForVideoSizes(tracks[i].getFormat(), 
							supported[0]);
		    } else
			chosen = supported[0];
		    tracks[i].setFormat(chosen);
		    System.err.println("Track " + i + " is set to transmit as:");
		    System.err.println("  " + chosen);
		    atLeastOneTrack = true;
		} else
		    tracks[i].setEnabled(false);
	    } else
		tracks[i].setEnabled(false);
	}

	if (!atLeastOneTrack)
	    return "Couldn't set any of the tracks to a valid RTP format";

	// Realize the processor. This will internally create a flow
	// graph and attempt to create an output datasource for JPEG/RTP
	// audio frames.
	result = waitForState(processor, Controller.Realized);
	if (result == false)
	    return "Couldn't realize processor";

	// Set the JPEG quality to .5.
	setJPEGQuality(processor, 0.5f);

	// Get the output data source of the processor
	dataOutput = processor.getDataOutput();

	return null;
    }


    /**
     * Use the RTPManager API to create sessions for each media 
     * track of the processor.
     */
    private String createTransmitter() {

	// Cheated.  Should have checked the type.
	PushBufferDataSource pbds = (PushBufferDataSource)dataOutput;
	PushBufferStream pbss[] = pbds.getStreams();

	rtpMgrs = new RTPManager[pbss.length];
	SessionAddress localAddr, destAddr;
	InetAddress ipAddr;
	SendStream sendStream;
	int port;
	SourceDescription srcDesList[];

	for (int i = 0; i < pbss.length; i++) {
	    try {
		rtpMgrs[i] = RTPManager.newInstance();	    

		// The local session address will be created on the
		// same port as the the target port. This is necessary
		// if you use AVTransmit2 in conjunction with JMStudio.
		// JMStudio assumes -  in a unicast session - that the
		// transmitter transmits from the same port it is receiving
		// on and sends RTCP Receiver Reports back to this port of
		// the transmitting host.
		
		port = portBase + 2*i;
		ipAddr = InetAddress.getByName(ipAddress);

		localAddr = new SessionAddress( InetAddress.getLocalHost(),
						port);
		
		destAddr = new SessionAddress( ipAddr, port);

		rtpMgrs[i].initialize( localAddr);
		
		rtpMgrs[i].addTarget( destAddr);
		
		System.err.println( "Created RTP session: " + ipAddress + " " + port);
		
		sendStream = rtpMgrs[i].createSendStream(dataOutput, i);		
		sendStream.start();
	    } catch (Exception  e) {
		return e.getMessage();
	    }
	}

	return null;
    }


    /**
     * For JPEG and H263, we know that they only work for particular
     * sizes.  So we'll perform extra checking here to make sure they
     * are of the right sizes.
     */
    Format checkForVideoSizes(Format original, Format supported) {

	int width, height;
	Dimension size = ((VideoFormat)original).getSize();
	Format jpegFmt = new Format(VideoFormat.JPEG_RTP);
	Format h263Fmt = new Format(VideoFormat.H263_RTP);

	if (supported.matches(jpegFmt)) {
	    // For JPEG, make sure width and height are divisible by 8.
	    width = (size.width % 8 == 0 ? size.width :
				(int)(size.width / 8) * 8);
	    height = (size.height % 8 == 0 ? size.height :
				(int)(size.height / 8) * 8);
	} else if (supported.matches(h263Fmt)) {
	    // For H.263, we only support some specific sizes.
	    if (size.width < 128) {
		width = 128;
		height = 96;
	    } else if (size.width < 176) {
		width = 176;
		height = 144;
	    } else {
		width = 352;
		height = 288;
	    }
	} else {
	    // We don't know this particular format.  We'll just
	    // leave it alone then.
	    return supported;
	}

	return (new VideoFormat(null, 
				new Dimension(width, height), 
				Format.NOT_SPECIFIED,
				null,
				Format.NOT_SPECIFIED)).intersects(supported);
    }


    /**
     * Setting the encoding quality to the specified value on the JPEG encoder.
     * 0.5 is a good default.
     */
    void setJPEGQuality(Player p, float val) {

	Control cs[] = p.getControls();
	QualityControl qc = null;
	VideoFormat jpegFmt = new VideoFormat(VideoFormat.JPEG);

	// Loop through the controls to find the Quality control for
 	// the JPEG encoder.
	for (int i = 0; i < cs.length; i++) {

	    if (cs[i] instanceof QualityControl &&
		cs[i] instanceof Owned) {
		Object owner = ((Owned)cs[i]).getOwner();

		// Check to see if the owner is a Codec.
		// Then check for the output format.
		if (owner instanceof Codec) {
		    Format fmts[] = ((Codec)owner).getSupportedOutputFormats(null);
		    for (int j = 0; j < fmts.length; j++) {
			if (fmts[j].matches(jpegFmt)) {
			    qc = (QualityControl)cs[i];
	    		    qc.setQuality(val);
			    System.err.println("- Setting quality to " + 
					val + " on " + qc);
			    break;
			}
		    }
		}
		if (qc != null)
		    break;
	    }
	}
    }


    /****************************************************************
     * Convenience methods to handle processor's state changes.
     ****************************************************************/
    
    private Integer stateLock = new Integer(0);
    private boolean failed = false;
    
    Integer getStateLock() {
	return stateLock;
    }

    void setFailed() {
	failed = true;
    }
    
    private synchronized boolean waitForState(Processor p, int state) {
	p.addControllerListener(new StateListener());
	failed = false;

	// Call the required method on the processor
	if (state == Processor.Configured) {
	    p.configure();
	} else if (state == Processor.Realized) {
	    p.realize();
	}
	
	// Wait until we get an event that confirms the
	// success of the method, or a failure event.
	// See StateListener inner class
	while (p.getState() < state && !failed) {
	    synchronized (getStateLock()) {
		try {
		    getStateLock().wait();
		} catch (InterruptedException ie) {
		    return false;
		}
	    }
	}

	if (failed)
	    return false;
	else
	    return true;
    }

    /****************************************************************
     * Inner Classes
     ****************************************************************/

    class StateListener implements ControllerListener {

	public void controllerUpdate(ControllerEvent ce) {

	    // If there was an error during configure or
	    // realize, the processor will be closed
	    if (ce instanceof ControllerClosedEvent)
		setFailed();

	    // All controller events, send a notification
	    // to the waiting thread in waitForState method.
	    if (ce instanceof ControllerEvent) {
		synchronized (getStateLock()) {
		    getStateLock().notifyAll();
		}
	    }
	}
    }


    /****************************************************************
     * Sample Usage for AVTransmit2 class
     ****************************************************************/
    
   /* public static void main(String [] args) {
	// We need three parameters to do the transmission
	// For example,
	//   java AVTransmit2 file:/C:/media/test.mov  129.130.131.132 42050
	
	if (args.length < 3) {
	    prUsage();
	}

	Format fmt = null;
	int i = 0;

	// Create a audio transmit object with the specified params.
	AVTransmit2 at = new AVTransmit2(new MediaLocator(args[i]),
					     args[i+1], args[i+2], fmt);
	// Start the transmission
	String result = at.start();

	// result will be non-null if there was an error. The return
	// value is a String describing the possible error. Print it.
	if (result != null) {
	    System.err.println("Error : " + result);
	    System.exit(0);
	}
	
	System.err.println("Start transmission for 60 seconds...");

	// Transmit for 60 seconds and then close the processor
	// This is a safeguard when using a capture data source
	// so that the capture device will be properly released
	// before quitting.
	// The right thing to do would be to have a GUI with a
	// "Stop" button that would call stop on AVTransmit2
	try {
	    Thread.currentThread().sleep(60000);
	} catch (InterruptedException ie) {
	}

	// Stop the transmission
	at.stop();
	
	System.err.println("...transmission ended.");

	System.exit(0);
    }


    static void prUsage() {
	System.err.println("Usage: AVTransmit2 <sourceURL> <destIP> <destPortBase>");
	System.err.println("     <sourceURL>: input URL or file name");
	System.err.println("     <destIP>: multicast, broadcast or unicast IP address for the transmission");
	System.err.println("     <destPortBase>: network port numbers for the transmission.");
	System.err.println("                     The first track will use the destPortBase.");
	System.err.println("                     The next track will use destPortBase + 2 and so on.\n");
	System.exit(0);
    }*/
}

