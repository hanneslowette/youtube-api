package org.hannes.net;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.hannes.util.VideoQuality;

/**
 * Class for creating download requests
 * 
 * @author Hannes
 *
 */
public class DownloadRequests {
	
	/**
	 * The youtube id pattern
	 */
	private static final Pattern ID_PATTERN = Pattern.compile("(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*");
	
	/**
	 * the link pattern
	 */
	private static final Pattern LINK_PATTERN = Pattern.compile("([^&,]*)[&,]");
	
	/**
	 * The itag pattern (itag is the ID for the quality of the video)
	 */
	private static final Pattern ITAG_PATTERN = Pattern.compile("itag=(\\d+)");
	
	/**
	 * The signature pattern
	 */
	private static final Pattern SIGNATURE_PATTERN = Pattern.compile("&signature=([^&,]*)|sig=([^&,]*)|[&,]s=([^&,]*)");

	/**
	 * 
	 * @param url
	 * @param quality
	 * @return
	 * @throws Exception
	 */
	public static DownloadRequest request(URL url, VideoQuality quality) throws Exception {
		return request(url).get(quality);
	}

	/**
	 * 
	 * @param url
	 * @param quality
	 * @return
	 * @throws Exception
	 */
	public static Map<VideoQuality, DownloadRequest> request(URL url) throws Exception {
		String video_id = getVideoId(url);
		URL download_url = new URL("http://www.youtube.com/get_video_info?authuser=0&video_id=" + video_id + "&el=embedded");
		String html = readUrlStream(download_url);
		Map<String, String> properties = getVideoProperties(html);
		return createDownloadRequests(properties);
	}

	/**
	 * Extract the video locations from the given input
	 * 
	 * @param input
	 * @throws Exception
	 */
	private static Map<VideoQuality, DownloadRequest> createDownloadRequests(Map<String, String> properties) throws Exception {
		Map<VideoQuality, DownloadRequest> requests = new HashMap<>();
		String fmt_stream_map = URLDecoder.decode(properties.get("url_encoded_fmt_stream_map"), "UTF-8");
		
		String[] split = fmt_stream_map.split("url=");
		for (String token : split) {
			token = StringEscapeUtils.unescapeJava(token);
			
			/*
			 * Create the regex matchers
			 */
			Matcher url_matcher = LINK_PATTERN.matcher(token);
			Matcher itag_matcher = ITAG_PATTERN.matcher(URLDecoder.decode(token, "UTF-8"));
			Matcher signature_matcher = SIGNATURE_PATTERN.matcher(URLDecoder.decode(token, "UTF-8"));
			
			/*
			 * Only if all 3 elements have been found, it is valid
			 */
			if (url_matcher.find() && itag_matcher.find() && signature_matcher.find()) {
				DownloadRequest request = new DownloadRequest(VideoQuality.get(itag_matcher.group(1)), url_matcher.group(1), signature_matcher.group(1), URLDecoder.decode(properties.get("title"), "UTF-8"));
				requests.put(request.getQuality(), request);
			}
		}
		return requests;
	}

	/**
	 * 
	 * @param html
	 * @return
	 * @throws Exception
	 */
	private static final Map<String, String> getVideoProperties(String html) throws Exception {
		List<NameValuePair> list = URLEncodedUtils.parse(new URI(null, null, null, -1, null, html, null), "UTF-8");
		Map<String, String> map = new HashMap<>();
		for (Iterator<NameValuePair> iterator = list.iterator(); iterator.hasNext(); ) {
			NameValuePair pair = iterator.next();
			map.put(pair.getName(), pair.getValue());
		}
		return map;
	}

	/**
	 * Read the input from a stream to a string
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	private static String readUrlStream(URL url) throws Exception {
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		ReadableByteChannel channel = Channels.newChannel(connection.getInputStream());
		ByteBuffer data = ByteBuffer.allocate(64);
		for (ByteBuffer buffer = ByteBuffer.allocate(64); channel.read(buffer) > 0; ) {
			data = (ByteBuffer) expand(data, buffer.flip().capacity()).put(data).put(buffer).flip();
			buffer = ByteBuffer.allocate(64);
		}
		return new String(data.array(), Charset.forName("UTF-8"));
	}

	/**
	 * Expands the ByteBuffer by n
	 * 
	 * @param buffer
	 * @param n
	 * @return
	 */
	private static final ByteBuffer expand(ByteBuffer buffer, int n) {
		ByteBuffer out = ByteBuffer.allocate(buffer.capacity() + n);
		return out.put(buffer);
	}

	/**
	 * Gets the ID of the video for a given url
	 * 
	 * @param video_location
	 * @return
	 * @throws MalformedURLException
	 */
	public static String getVideoId(String video_location) throws MalformedURLException {
		Matcher matcher = ID_PATTERN.matcher(video_location);
		if (matcher.find()) {
			return matcher.group();
		}
		throw new MalformedURLException("Could not get video ID for " + video_location);
	}

	/**
	 * Gets the ID of the video for a given url
	 * 
	 * @param video_location
	 * @return
	 * @throws MalformedURLException
	 */
	public static String getVideoId(URL url) throws MalformedURLException {
		return getVideoId(url.toString());
	}

}