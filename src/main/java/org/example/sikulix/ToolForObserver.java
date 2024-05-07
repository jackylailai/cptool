package org.example.sikulix;

import org.sikuli.script.Image;
import org.sikuli.script.Match;
import org.sikuli.script.ObserveEvent;
import org.sikuli.script.ObserverCallBack;
import org.sikuli.script.Pattern;
import org.sikuli.script.Region;
import org.sikuli.script.Screen;
import org.sikuli.script.support.Observer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.sikuli.basics.Settings.FOREVER;

public class ToolForObserver {
	public static void startObserver(String referenceImagePath) throws IOException {

		System.out.println("start sikulix observer");
		File referenceImageFile = new File(referenceImagePath);
		FileInputStream inputStream = new FileInputStream(referenceImageFile);

		BufferedImage bufferedImageR = ImageIO.read(new File(String.valueOf(referenceImageFile)));
		Image referrenceimage = new Image(bufferedImageR);
		//String imagePath2 = "path/to/image2.png";

		Region region1 = new Screen(1);
		Region region2 = new Screen();

		Pattern pattern1 = new Pattern(referrenceimage);
		//Pattern pattern2 = new Pattern(imagePath2);
		//region1.onAppear(pattern1.exact(),stop);
		//region1.observe(FOREVER);
		//region1.onAppear(pattern1, new ObserverCallBack() {
		//	@Override
		//	public void appeared(ObserveEvent e) {
		//		System.out.println("Image 1 found in region 1   :   "+e);
		//	}
		//});
		region1.onAppear(pattern1,
				new ObserverCallBack() {
					@Override
					public void appeared(ObserveEvent event) {
						System.out.println("Image 1 found in region 1   :   "+event);
					}
				}
		);
		// run observation in foreground for 10 seconds
		region1.observe(50);
		//region1.onAppear(pattern1, new IObserve() {
		//	@Override
		//	public void changed( ObserveEvent event) {
		//		System.out.println("Image 1 found in region 1");
		//	}
		//}); // observe for 60 seconds
		//region1.observe(600);

		//region2.onAppear(pattern2, (Match match) -> {
		//	System.out.println("Image 2 found in region 2");
		//	// 執行相應的操作
		//}).observe(60); // observe for 60 seconds

		// 等待一段時間，讓觀察者運行
		try {
			Thread.sleep(60000); // 等待 60 秒
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// 停止觀察者
		region1.stopObserver();
		region2.stopObserver();
	}
}

