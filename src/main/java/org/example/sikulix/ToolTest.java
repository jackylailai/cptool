package org.example.sikulix;

import org.sikuli.script.FindFailed;
import org.sikuli.script.Region;
import org.sikuli.script.Screen;
import org.sikuli.script.Pattern;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;

public class ToolTest {
    // 測試版本：抓到的圖XY值中心 + 位移targetImage先抓xy值 然後+上offset xy 就是我現在要開始抓的位置xy region(x,y,targetWidth ,targetHeight )
    public static String testForFinal(String testImagePath) throws FindFailed {
        // Example decoding
        try {

            // Example decoding
            DecodedValues decodedValues = decodeReferenceImageName(testImagePath);
            System.out.println("取得測試路徑準備進行測試 : "+testImagePath);
            // Accessing the decoded values
            if(decodedValues!=null) {
                System.out.println("decoded OffsetX: " + decodedValues.offsetX);
                System.out.println("decoded OffsetY: " + decodedValues.offsetY);
                System.out.println("decoded TargetWidth: " + decodedValues.targetWidth);
                System.out.println("decoded TargetHeight: " + decodedValues.targetHeight);
//                System.out.println("decoded TimestampString: " + decodedValues.timestampString);
            }else{
                System.out.println("decodedValues為null，無法根據檔名解析為參數");
            }

            String finalText = testForSikulix(decodedValues.offsetX, decodedValues.offsetY, decodedValues.targetWidth, decodedValues.targetHeight, testImagePath);
            return finalText;
        } catch (NullPointerException e) {
            // Handle the case where decodedValues is null
            System.err.println("Error: decodedValues is null.");
            e.printStackTrace();  // This line prints the stack trace for debugging purposes.
            return "Error: decodedValues is null.";
        } catch (FindFailed e) {
            // Handle the FindFailed exception
            System.err.println("Error in Sikulix operation.");
            e.printStackTrace();  // This line prints the stack trace for debugging purposes.
            return "Error in Sikulix operation.";
        }
    }

    private static String testForSikulix(int offsetX, int offsetY, int targetWidth, int targetHeight, String testImagePath) throws FindFailed {
        Screen screen = initializeScreen();
        org.sikuli.script.Pattern referenceImage = new org.sikuli.script.Pattern(testImagePath);
        if (screen.exists(referenceImage.similar(0.7), 6) != null) {
            Region referenceImageRegion = screen.find(referenceImage);
            System.out.println("顯示參考的圖片區域!");
            referenceImageRegion.highlight(3);
            int referX = referenceImageRegion.getX();
            int referY = referenceImageRegion.getY();
            int targetX=referX+offsetX;
            int targetY=referY+offsetY;
            Region newRegion = new Region(targetX,targetY,targetWidth,targetHeight);
            newRegion.highlight(3);
            String extractedText = newRegion.text();
            System.out.println("擷取內容 : "+extractedText);
            return extractedText;
        }
//        Region newRegion = new Region(offsetX,offsetY,targetWidth,targetHeight);
//        newRegion.highlight(3);
//        String extractedText = newRegion.text();
//        System.out.println("擷取內容 : "+extractedText);
//        return extractedText;
        return "sikulix擷取時有拿到參數，但是擷取失敗";
    }



//    private static DecodedValues decodeReferenceImageName(String encodedString) {
//        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\((.*?)\\)");
//        Matcher matcher = pattern.matcher(encodedString);
//
//        DecodedValues values = new DecodedValues();
//
//        if (matcher.find()) {
//            String contentInsideBrackets = matcher.group(1);
//            System.out.println(contentInsideBrackets);
//            String[] parts = contentInsideBrackets.split("_");
//            System.out.println(Arrays.toString(parts));
//            if (parts.length == 6) {
//                values.offsetX = Integer.parseInt(parts[0]);
//                values.offsetY = Integer.parseInt(parts[1]);
//                values.targetWidth = Integer.parseInt(parts[2]);
//                values.targetHeight = Integer.parseInt(parts[3]);
//                values.timestampString = parts[5];
//            }
//        }
////        String withoutParentheses = encodedString.replaceAll("[()]", "");
////        String[] parts = withoutParentheses.split("_");
////        System.out.println(withoutParentheses);
////        System.out.println(Arrays.toString(parts));
//        return values;
//
//    }
    private static DecodedValues decodeReferenceImageName(String encodedString) {
        // Remove the file extension (.png)
        File file = new File(encodedString);
        String fileName = file.getName();
        System.out.println(fileName);
//        String[] values = fileName.split("(?<=\\D)(?=-?\\d)|(?<=\\d)(?=-?\\D)");
//        String[] valuess = fileName.split("\\D+");
        String[] parts = fileName.split("[^-\\d]+");
//        String[] values = fileName.split("(?<=\\D)(?=-|\\d)|(?<=\\d)(?=-|\\D)");
//        String timestampString = fileName.substring(0, fileName.indexOf('-'));
        System.out.println("parts : "+Arrays.toString(parts));
//        System.out.println("valuess : "+Arrays.toString(valuess));
        if (parts.length == 6) {
            int offsetX = Integer.parseInt(parts[2]);
            int offsetY = Integer.parseInt(parts[3]);
            int targetWidth = Integer.parseInt(parts[4]);
            int targetHeight = Integer.parseInt(parts[5]);

            System.out.println("OffsetX: " + offsetX);
            System.out.println("OffsetY: " + offsetY);
            System.out.println("TargetWidth: " + targetWidth);
            System.out.println("TargetHeight: " + targetHeight);

            DecodedValues decodedValues = new DecodedValues();
            decodedValues.offsetX = Integer.parseInt(parts[2]);
            decodedValues.offsetY = Integer.parseInt(parts[3]);
            decodedValues.targetWidth = Integer.parseInt(parts[4]);
            decodedValues.targetHeight = Integer.parseInt(parts[5]);
//            decodedValues.timestampString = timestampString;
            return decodedValues;
        } else {
            System.out.println("Error: Invalid numeric values in the string.");
        }
//        String[] parts = encodedString.replace(".png", "").split("-");
//        System.out.println("parts :　"+Arrays.toString(parts));
        //不能是-的
        //檔名使用英文 不要用path
//        if (parts.length == 2) {
//            String timestampString = parts[0];
//            String[] values = parts[1].split("_");
//            System.out.println("解碼後values : "+Arrays.toString(values));
//            if (values.length == 4) {
//                DecodedValues decodedValues = new DecodedValues();
//                decodedValues.offsetX = Integer.parseInt(values[0]);
//                decodedValues.offsetY = Integer.parseInt(values[1]);
//                decodedValues.targetWidth = Integer.parseInt(values[2]);
//                decodedValues.targetHeight = Integer.parseInt(values[3]);
//                decodedValues.timestampString = timestampString;
//                return decodedValues;
//            }
//        }

        return null;
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
    static class DecodedValues {
        int offsetX;
        int offsetY;
        int targetWidth;
        int targetHeight;
        String timestampString;
    }
}
