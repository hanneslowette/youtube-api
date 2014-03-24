package org.hannes;

import java.io.File;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.hannes.net.DownloadRequest;
import org.hannes.net.DownloadRequests;
import org.hannes.net.DownloadResult;
import org.hannes.net.DownloadThread;
import org.hannes.util.Callback;
import org.hannes.util.VideoQuality;

public class Example {
	
	public static void main(String[] args) throws Exception {
		DownloadRequest request = DownloadRequests.request(new URL("https://www.youtube.com/watch?v=Wi_6K_SwG4Q"), VideoQuality.p240);
		
		final ExecutorService service = Executors.newSingleThreadExecutor();
		service.submit(new DownloadThread(request, new File(System.getProperty("user.home") + "/downloads/"), new Callback<DownloadResult>() {

			@Override
			public void call(DownloadResult object) throws Exception {
				System.out.println("file downloaded to " + object.getOutputFile().getPath());
				service.shutdown();
			}

			@Override
			public void exceptionCaught(Throwable t) {
				t.printStackTrace();
			}
			
		}));
	}

}