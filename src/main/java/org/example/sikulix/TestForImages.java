package org.example.sikulix;

import org.sikuli.script.Image;
import org.sikuli.script.ObserveEvent;
import org.sikuli.script.ObserverCallBack;
import org.sikuli.script.Pattern;
import org.sikuli.script.Region;
import org.sikuli.script.Screen;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TestForImages {

	//private static final Screen screen = new Screen(1);
	public static void main(String[] args) throws IOException {
		File folder = new File("C:\\testimage\\");
		File[] listOfFiles = folder.listFiles();
		//System.out.println("start sikulix observer");
		//
		//if (listOfFiles != null) {
		//	for (File file : listOfFiles) {
		//		if (file.isFile()) {
		//			BufferedImage bufferedImageR = ImageIO.read(new File(String.valueOf(file)));
		//			System.out.println("2uu24"+"File input : " + file.getName()+",size : "+bufferedImageR);
		//
		//			// 為每個檔案創建一個觀察者
		//			Screen screen = new Screen(1);
		//			Pattern pattern = new Pattern(file.getPath());
		//			//screen.highlight(0.5);
		//			screen.onAppear(pattern, new ObserverCallBack() {
		//				@Override
		//				public void appeared(ObserveEvent event) {
		//					System.out.println("Image found: " + file.getName());
		//				}
		//			});
		//
		//			screen.observeInBackground();
		//		} else if (file.isDirectory()) {
		//			System.out.println("Directory " + file.getName());
		//		}
		//	}
		//} else {
		//	System.out.println("The directory is empty or does not exist.");
		//}

		//File[] listOfFiles = folder.listFiles();

		if (listOfFiles != null) {
			ExecutorService executor = Executors.newCachedThreadPool();
			for (File file : listOfFiles) {
				if (file.isFile()) {
					executor.execute(() -> {
						try {
							processFile(file);

						} catch (Exception e) {
							e.printStackTrace();
						}
					});
				} else if (file.isDirectory()) {
					System.out.println("Directory " + file.getName());
				}
			}
			try {
				executor.shutdown();
				if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
					executor.shutdownNow();
				}
			} catch (InterruptedException e) {
				executor.shutdownNow();
				Thread.currentThread().interrupt();
			}
		} else {
			System.out.println("The directory is empty or does not exist.");
		}
		System.out.println("end sikulix observer");

		try {
			System.out.println("等待觀察者搜尋");
			Thread.sleep(60000); // 等待 60 秒
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// 停止觀察者
		//region1.stopObserver();
		//region2.stopObserver();

		Screen screen = new Screen(1);
		System.out.println("new了一個新的screen準備關閉觀察者");
		screen.stopObserver();
	}
	private static void processFile(File file) {
		try {
			Screen screen = new Screen(1);
			BufferedImage bufferedImage = ImageIO.read(file);
			System.out.println("File input: " + file.getName() + ", size: " + bufferedImage);

			Pattern pattern = new Pattern(file.getPath());
			screen.onAppear(pattern, new ObserverCallBack() {
				@Override
				public void appeared(ObserveEvent event) {
					System.out.println("Image found: " + file.getName());
				}
			});
			screen.observeInBackground();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
