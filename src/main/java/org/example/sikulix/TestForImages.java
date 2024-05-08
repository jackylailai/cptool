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

public class TestForImages {


	public static void main(String[] args) {
		File folder = new File("");
		File[] listOfFiles = folder.listFiles();

		if (listOfFiles != null) {
			for (File file : listOfFiles) {
				if (file.isFile()) {
					System.out.println("File " + file.getName());

					// 為每個檔案創建一個觀察者
					Screen screen = new Screen();
					Pattern pattern = new Pattern(file.getPath());

					screen.onAppear(pattern, new ObserverCallBack() {
						@Override
						public void appeared(ObserveEvent event) {
							System.out.println("Image found: " + file.getName());
						}
					});

					screen.observeInBackground();
				} else if (file.isDirectory()) {
					System.out.println("Directory " + file.getName());
				}
			}
		} else {
			System.out.println("The directory is empty or does not exist.");
		}

		System.out.println("start sikulix observer");

		File referenceImageFile = new File(referenceImagePath);
		FileInputStream inputStream = new FileInputStream(referenceImageFile);

		BufferedImage bufferedImageR = ImageIO.read(new File(String.valueOf(referenceImageFile)));
		Image referrenceimage = new Image(bufferedImageR);

		File targetImageFile = new File(targetImagePath);

		BufferedImage bufferedImageT = ImageIO.read(new File(String.valueOf(targetImageFile)));
		Image targetImag = new Image(bufferedImageT);

		Region region1 = new Screen(1);
		//Region region1 = new Region( 0, 0, 200, 200);
		Region region2 = new Screen(1);

		Pattern pattern1 = new Pattern(referrenceimage);
		Pattern pattern2 = new Pattern(targetImag);

		//region1.onAppear(pattern1,
		//		new ObserverCallBack() {
		//			@Override
		//			public void appeared(ObserveEvent event) {
		//				System.out.println("Image 1 found in region 1   :   "+event);
		//			}
		//		}
		//);
		//region1.observe();

		//region1.onChange(50,
		//		new ObserverCallBack() {
		//			@Override
		//			public void changed(ObserveEvent event) {
		//				System.out.println("onChange 事件偵測:   "+event);
		//			}
		//		}
		//);

		region1.onVanish(pattern1,
				new ObserverCallBack() {
					@Override
					public void vanished( ObserveEvent event) {
						System.out.println("Image 1 vanished in region 1   :   "+event);
					}
				}
		);
		region1.observe();


		region2.onAppear(pattern2,
				new ObserverCallBack() {
					@Override
					public void appeared(ObserveEvent event) {
						System.out.println("Image 2 found in region 2   :   "+event);
					}
				}
		);
		//new thread to observe region2
		region2.observeInBackground();
		//region2.observe();

		//region2.onAppear(pattern2, (Match match) -> {
		//	System.out.println("Image 2 found in region 2");
		//	// 執行相應的操作
		//}).observe(60); // observe for 60 seconds

		try {
			System.out.println("等待觀察者搜尋");
			Thread.sleep(60000); // 等待 60 秒
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// 停止觀察者
		region1.stopObserver();
		region2.stopObserver();

		Screen screen = new Screen();
		screen.stopObserver();
	}

}
