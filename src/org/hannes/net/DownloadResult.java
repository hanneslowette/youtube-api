package org.hannes.net;

import java.io.File;

/**
 * Represents a download result
 * 
 * @author Hannes
 *
 */
public class DownloadResult {

	/**
	 * The request for this download
	 */
	private final DownloadRequest request;

	/**
	 * Path to the file that has been downloaded
	 */
	private final File outputFile;

	public DownloadResult(DownloadRequest request, File outputFile) {
		this.request = request;
		this.outputFile = outputFile;
	}

	public DownloadRequest getRequest() {
		return request;
	}

	public File getOutputFile() {
		return outputFile;
	}

}