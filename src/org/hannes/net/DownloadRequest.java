package org.hannes.net;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

import org.hannes.util.VideoQuality;

/**
 * Youtube download stuff
 * 
 * @author Hannes
 *
 */
public class DownloadRequest {

	/**
	 * The video itag
	 */
	private final VideoQuality quality;
	
	/**
	 * The video location
	 */
	private final String location;
	
	/**
	 * The video signature
	 */
	private final String signature;

	/**
	 * Title of the video
	 */
	private final String title;

	public DownloadRequest(VideoQuality quality, String location, String signature, String title) {
		this.quality = quality;
		this.location = location;
		this.signature = signature;
		this.title = title;
	}

	public URL getUrl() throws MalformedURLException, UnsupportedEncodingException {
		return new URL(URLDecoder.decode(location + "&signature=" + signature, "UTF-8"));
	}

	public VideoQuality getQuality() {
		return quality;
	}

	public String getLocation() {
		return location;
	}

	public String getSignature() {
		return signature;
	}

	public String getTitle() {
		return title;
	}

}