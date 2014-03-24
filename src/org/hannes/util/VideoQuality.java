package org.hannes.util;

import java.util.HashMap;
import java.util.Map;


public enum VideoQuality {

	p1080, p720, p520, p480, p360, p270, p240, p224, p144;
	
	/**
	 * The map with all the qualities
	 */
	private static final Map<Integer, VideoQuality> qualities = new HashMap<>();

	static {
		qualities.put(120, VideoQuality.p720);
		qualities.put(102, VideoQuality.p720);
		qualities.put(101, VideoQuality.p360);
        qualities.put(100, VideoQuality.p360);
        qualities.put(85, VideoQuality.p520);
        qualities.put(84, VideoQuality.p720);
        qualities.put(83, VideoQuality.p240);
        qualities.put(82, VideoQuality.p360);
        qualities.put(46, VideoQuality.p1080);
        qualities.put(45, VideoQuality.p720);
        qualities.put(44, VideoQuality.p480);
        qualities.put(43, VideoQuality.p360);
        qualities.put(37, VideoQuality.p1080);
        qualities.put(36, VideoQuality.p240);
        qualities.put(35, VideoQuality.p480);
        qualities.put(34, VideoQuality.p360);
        qualities.put(22, VideoQuality.p720);
        qualities.put(18, VideoQuality.p360);
        qualities.put(17, VideoQuality.p144);
        qualities.put(6, VideoQuality.p270);
        qualities.put(5, VideoQuality.p240);
	}

	/**
	 * Get the quality for the given itag
	 * 
	 * @param itag
	 * @return
	 */
	public static VideoQuality getQuality(int itag) {
		return qualities.get(itag);
	}

	/**
	 * Get the quality for the given itag
	 * 
	 * @param itag
	 * @return
	 */
	public static VideoQuality get(String itag) {
		return getQuality(Integer.valueOf(itag));
	}

}