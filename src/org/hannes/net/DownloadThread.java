package org.hannes.net;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.concurrent.Callable;

import org.hannes.util.Callback;

public class DownloadThread implements Callable<DownloadResult> {

	/**
	 * The download request
	 */
	private final DownloadRequest request;
	
	/**
	 * The callback, should something go wrong
	 */
	private final Callback<DownloadResult> callback;
	
	/**
	 * The target file
	 */
	private final File target;

	/**
	 * 
	 * @param request
	 * @param callback
	 */
	public DownloadThread(DownloadRequest request, Callback<DownloadResult> callback) {
		this (request, null, callback);
	}

	/**
	 * 
	 * @param request
	 * @param target
	 * @param callback
	 */
	public DownloadThread(DownloadRequest request, File target, Callback<DownloadResult> callback) {
		this.request = request;
		this.callback = callback;
		this.target = target;
	}

	@Override
	public DownloadResult call() throws Exception {
		try {
			/*
			 * Open the connection to the video
			 */
			ReadableByteChannel channel = Channels.newChannel(request.getUrl().openStream());
			
			/*
			 * Create the output file
			 */
			File output_file = target == null ? new File(request.getTitle() + ".mp4") : new File(target, request.getTitle() + ".mp4");
			RandomAccessFile raf = new RandomAccessFile(output_file, "rw");
			
			/*
			 * Download the file
			 */
			raf.getChannel().transferFrom(channel, 0, Long.MAX_VALUE);
			
			/*
			 * Close the stream
			 */
			raf.close();
			
			/*
			 * Callback
			 */
			DownloadResult download_result = new DownloadResult(request, output_file);
			callback.call(download_result);
			
			/*
			 * Return the download result
			 */
			return download_result;
		} catch (Throwable throwable) {
			/*
			 * Notify the callback something happened
			 */
			callback.exceptionCaught(throwable);
			
			/*
			 * Re-throw exception
			 */
			throw new Exception(throwable);
		}
	}

}