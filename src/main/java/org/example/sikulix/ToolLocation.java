package org.example.sikulix;

import org.sikuli.script.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import static org.example.sikulix.utils.FileUtil.readFileToBytes;

public class ToolLocation {
    private static long lastCheckedTime = 0;
    private static long lastCheckedTime2 = 0;
    public static String testForTwo(String referenceImagePath, String targetImagePath){
        Screen screen = initializeScreen();

        try {
            File referenceImageFile = new File(referenceImagePath);
            File targetImageFile = new File(targetImagePath);
            FileInputStream inputStream = new FileInputStream(referenceImageFile);

            BufferedImage bufferedImageR = ImageIO.read(new File(String.valueOf(referenceImageFile)));
            Image referrenceimage = new Image(bufferedImageR);
            BufferedImage bufferedImageT = ImageIO.read(new File(String.valueOf(targetImageFile)));
            Image targetimage = new Image(bufferedImageT);
            System.out.println("bufferedImageT"+bufferedImageT);
//            byte[] referenceImageBytes = readFileToBytes(referenceImageFile);
//            byte[] targetImageBytes = readFileToBytes(targetImageFile);

            Pattern referenceImage = new Pattern(referrenceimage);
            Pattern targetImage = new Pattern(targetimage);

//            if (referenceImageFile.lastModified() > getLastCheckedTime()) {
//                System.out.println("~~~~更改時間"+referenceImageFile.lastModified());
//                referenceImage = new Pattern(referenceImagePath);
//                System.out.println("新的pattern : "+referenceImage);
//                updateLastCheckedTime();
//            }
//
//            if (targetImageFile.lastModified() > getLastCheckedTime2()) {
//                targetImage = new Pattern(targetImagePath);
//                updateLastCheckedTime2();
//            }


            // 識別第一張圖片
//            Pattern referenceImage = new Pattern(referenceImagePath);
            int[] referenceCoordinates = getImageCoordinates(screen, referenceImage);
            int referenceX = referenceCoordinates[0];
            int referenceY = referenceCoordinates[1];
            Mouse.move(referenceX, referenceY);
            if (screen.exists(referenceImage.similar(0.7), 6) != null) {
                Region referenceRegion = screen.find(referenceImage);
                referenceRegion.highlight(3);
            }

            // 識別第二張圖片
//            Pattern targetImage = new Pattern(targetImagePath);
            int[] targetCoordinates = getImageCoordinates(screen, targetImage);
            int targetX = targetCoordinates[0];
            int targetY = targetCoordinates[1];
            int targetWidth = getImageWidth(targetImagePath);
            int targetHeight = getImageHeight(targetImagePath);
            Mouse.move(targetX, targetY);
            System.out.println("target x y :"+targetX+"  "+targetY);
            System.out.println("targetWidth  targetHeight :"+targetWidth+"  "+targetHeight);
            if (screen.exists(targetImage.similar(0.7), 6) != null) {
                Region targetRegion = screen.find(targetImage);
                targetRegion.highlight(3);
            }
            // 計算位移量
            int offsetX = targetX - referenceX;
            int offsetY = targetY - referenceY;
            System.out.println("offsetX + offsetY:"+offsetX+offsetY);
            if(offsetX==0&offsetY==0){
                return "失敗!" +"沒有擷取到位移";
            }


            // 將識別到的信息傳遞給其他處理部分
            //存offset XY 存 targetWidth targetHeight
            long timestamp = System.currentTimeMillis();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmssSSS");
            String timestampString = dateFormat.format(new Date(timestamp));
            String newReferenceImageName = String.format("%sX%dY%dW%dH%d",timestampString,offsetX, offsetY, targetWidth, targetHeight);
            System.out.println("產生新檔名 : " + newReferenceImageName);

            // 測試版本：抓到的圖XY值中心 + 位移targetImage先抓xy值 然後+上offset xy 就是我現在要開始抓的位置xy region(x,y,targetWidth ,targetHeight)

            // 將第一張圖的檔名更改為 newReferenceImageName
            File referenceFile = new File(referenceImagePath);
            File destinationFolder = new File("C:\\checkpoint");

            if (!destinationFolder.exists()) {
                destinationFolder.mkdirs();
            }

            File renamedReferenceFile = new File(destinationFolder, newReferenceImageName + ".png");

            try {
                Files.copy(referenceFile.toPath(), renamedReferenceFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Reference Image File copied successfully to: " + renamedReferenceFile.getAbsolutePath());
                return newReferenceImageName;
            } catch (IOException e) {
                e.printStackTrace();
                return "失敗";
            }
        } catch (FindFailed e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "false";
    }

    private static boolean isFileModifiedT(File file) {
        long lastModified = file.lastModified();
        // 比較上次修改時間，這裡可以根據實際需求進行更改
        // 例如，可以保存上次檢查的 lastModified 時間，然後比較兩者是否相同
        return lastModified != getLastCheckedTime2();
    }

    private static int[] getImageCoordinates(Screen screen, Pattern imagePattern) throws FindFailed {
        int[] coordinates = new int[]{0, 0, 0, 0};  // [x, y, width, height]

        if (screen.exists(imagePattern.similar(0.7), 6) != null) {
            Match imageMatch = screen.find(imagePattern);
            coordinates[0] = imageMatch.getX();
            coordinates[1] = imageMatch.getY();
            coordinates[2] = imageMatch.getW();
            coordinates[3] = imageMatch.getH();
        }

        return coordinates;
    }
    private static boolean isFileModifiedR(File file) {
        long lastModified = file.lastModified();
        // 比較上次修改時間，這裡可以根據實際需求進行更改
        // 例如，可以保存上次檢查的 lastModified 時間，然後比較兩者是否相同
        return lastModified != getLastCheckedTime();
    }

    private static int getImageWidth(String imagePath) {
        try {
            File imageFile = new File(imagePath);
            BufferedImage image = ImageIO.read(imageFile);
            return image.getWidth();
        } catch (IOException e) {
            e.printStackTrace();
            return -1; // 錯誤時回傳 -1 或其他適當的值
        }
    }

    private static int getImageHeight(String imagePath) {
        try {
            File imageFile = new File(imagePath);
            BufferedImage image = ImageIO.read(imageFile);
            return image.getHeight();
        } catch (IOException e) {
            e.printStackTrace();
            return -1; // 錯誤時回傳 -1 或其他適當的值
        }
    }
    private static Screen initializeScreen() {
        int numScreens = Screen.getNumberScreens();

        if (numScreens > 1) {
            // 如果有多個螢幕，初始化 SikuliX 的第二個螢幕（索引 1）
            return new Screen(1);
        } else {
            // 如果只有一個螢幕，初始化 SikuliX 的主螢幕（索引 0）
            return new Screen(0);
        }
    }
    private static long getLastCheckedTime() {
        return lastCheckedTime;
    }

    private static long getLastCheckedTime2() {
        return lastCheckedTime2;
    }

    private static void updateLastCheckedTime() {
        lastCheckedTime = System.currentTimeMillis();
    }

    private static void updateLastCheckedTime2() {
        lastCheckedTime2 = System.currentTimeMillis();
    }
}
