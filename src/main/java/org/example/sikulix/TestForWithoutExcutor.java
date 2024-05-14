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

public class TestForWithoutExcutor {

	private static final Screen screen = new Screen(1);
	public static void main(String[] args) throws IOException {
		File folder = new File("C:\\testimage\\");
		File[] listOfFiles = folder.listFiles();

		if (listOfFiles != null) {
				for (File file : listOfFiles) {
					if (file.isFile()) {
						try {
							processFile(file);

						} catch (Exception e) {
							e.printStackTrace();
						}
					} else if (file.isDirectory()) {
						System.out.println("Directory " + file.getName());
					}
				}
		} else {
			System.out.println("The directory is empty or does not exist.");
		}
		System.out.println("end sikulix observer");

		try {
			System.out.println("等待觀察者搜尋");
			Thread.sleep(3000); // 等待 60 秒
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
