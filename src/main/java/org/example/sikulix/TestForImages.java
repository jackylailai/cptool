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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TestForImages {

	private static final Screen screen = new Screen(1);
	private static final Map<String, Screen> screens = new ConcurrentHashMap<>();
	private static final Map<String, String> states = new ConcurrentHashMap<>();
	public static void main(String[] args) throws IOException {
		File folder = new File("C:\\testimage\\");
		File[] listOfFiles = folder.listFiles();

		if (listOfFiles != null) {
			ExecutorService executor = Executors.newCachedThreadPool();
			//CountDownLatch latch = new CountDownLatch(listOfFiles.length);

			//Map<String, Screen> screens = new ConcurrentHashMap<>();

			for (File file : listOfFiles) {
				if (file.isFile()) {
					executor.submit(() -> {
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
				System.out.println("停止接收新的任務"+ executor.isShutdown());
				if (!executor.awaitTermination(3, TimeUnit.SECONDS)) {
					executor.shutdownNow();
					System.out.println("強制停止"+executor.isShutdown());
				}
			} catch (InterruptedException e) {
				executor.shutdownNow();
				Thread.currentThread().interrupt();
			}

			//try {
			//	latch.await(3, TimeUnit.SECONDS);
			//} catch (InterruptedException e) {
			//	e.printStackTrace();
			//}

			//executor.shutdownNow();
		} else {
			System.out.println("The directory is empty or does not exist.");
		}
		System.out.println("end sikulix observer");

		try {
			System.out.println("等待觀察者搜尋");
			Thread.sleep(10000); // 等待 60 秒
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// 停止觀察者
		//region1.stopObserver();
		//region2.stopObserver();

		//Screen screen = new Screen(1);
		//System.out.println("new了一個新的screen準備關閉觀察者");
		// 停止所有觀察者
		System.out.println("關閉所有觀察者");
		for (Screen screen : screens.values()) {
			screen.stopObserver();
		}
		System.out.println("states map : "+states);
	}
	private static void processFile(File file) {
		try {
			Screen screen = new Screen(1);
			screens.put(file.getName(), screen);
			states.put(file.getName(), "not found");
			BufferedImage bufferedImage = ImageIO.read(file);
			System.out.println("File input: " + file.getName() + ", size: " + bufferedImage);

			Pattern pattern = new Pattern(file.getPath());
			screen.onAppear(pattern, new ObserverCallBack() {
				@Override
				public void appeared(ObserveEvent event) {
					System.out.println("Image found: " + file.getName());
					states.put(file.getName(), "found");
					//latch.countDown();
				}
			});
			screen.observeInBackground();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
