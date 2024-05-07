package org.example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.sikulix.ToolForObserver;
import org.example.sikulix.ToolLocation;
import org.example.sikulix.ToolTest;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Image;

import java.io.File;
import java.io.IOException;

public class ImageUploadApp extends Application {

    private String referenceImagePath;
    private String targetImagePath;
    private String testImagePath;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("SSTP開發小工具");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Reference Image File");
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif");
        fileChooser.getExtensionFilters().add(extFilter);

        Button btnReference = new Button("上傳參考圖片");
        Label referenceStatusLabel = new Label("參考圖片擷取狀態: ");
        btnReference.setOnAction(e -> {
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                referenceImagePath = selectedFile.getAbsolutePath();
                System.out.println("Reference Image Path: " + referenceImagePath);
                referenceStatusLabel.setText("參考圖片上傳成功!" + referenceImagePath);
            } else {
                referenceStatusLabel.setText("參考圖片上傳失敗.");
            }
        });

//        HBox referenceBox = new HBox(btnReference, referenceStatusLabel);
//        referenceBox.setSpacing(10);

        fileChooser.setTitle("Select Target Image File");
        Button btnTarget = new Button("上傳目標表格的圖片");
        Label targetStatusLabel = new Label("目標圖片擷取狀態: ");
        btnTarget.setOnAction(e -> {
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                targetImagePath = selectedFile.getAbsolutePath();
                System.out.println("Target Image Path: " + targetImagePath);
                targetStatusLabel.setText("目標圖片上傳成功!" + targetImagePath);
            } else {
                targetStatusLabel.setText("目標圖片上傳失敗.");
            }
        });


        Label uploadStatusLabel = new Label("查詢情況:");
        Button btnStartSearch = new Button("開始查詢");

        Alert alert = createAlert("擷取中", "擷取正在進行中...");

        btnStartSearch.setOnAction(e -> {
            // 啟動一個新的執行緒處理擷取提醒
            new Thread(() -> {
                Platform.runLater(() -> {
                    // 顯示 Alert
                    alert.show();
                });

                //String uploadSuccess = ToolLocation.testForTwo(referenceImagePath, targetImagePath);

                try {
                    ToolForObserver.startObserver(referenceImagePath);
                } catch (IOException ex) {
                    throw new RuntimeException( ex );
                }
                String uploadSuccess = null;
                Platform.runLater(() -> {
                    alert.close();
                    if (uploadSuccess != null) {
                        uploadStatusLabel.setText("查詢成功圖片存取為:"+uploadSuccess);
                    } else {
                        uploadStatusLabel.setText("查詢跟設定都失敗！");
                    }
                });
            }).start();
        });


        Button btnTest = new Button("測試上面顯示的檔名圖");
        Label testStatusLabel = new Label("測試路徑 Status: ");
        btnTest.setOnAction(e -> {
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                testImagePath = selectedFile.getAbsolutePath();
                System.out.println("Test Image Path: " + testImagePath);
                testStatusLabel.setText("測試圖片路徑!"+testImagePath);
            } else {
                testStatusLabel.setText("測試失敗.");
            }
        });

//        HBox testBox = new HBox(btnTest, testStatusLabel);
//        testBox.setSpacing(10);

        Label testLabel = new Label("測試的狀態 : ");
        Button btnStartTest = new Button("開始測試");
        btnStartTest.setOnAction(e -> {
            // 建立 Alert
            // 啟動一個新的執行緒處理測試提醒
            new Thread(() -> {

                Platform.runLater(() -> {
                    // 顯示 Alert
                    alert.show();
                });

                String testSuccess = null;
                try {
                    // 呼叫測試方法
                    testSuccess = ToolTest.testForFinal(testImagePath);
                } catch (FindFailed ex) {
                    // 處理例外狀況
                    throw new RuntimeException(ex);
                }

                String finalTestSuccess = testSuccess;
                Platform.runLater(() -> {
                    // 關閉 Alert
                    alert.close();

                    if (finalTestSuccess != null) {
                        testLabel.setText("測試成功取到的數值為: " + finalTestSuccess);
                    } else {
                        testLabel.setText("查詢跟設定都失敗！");
                    }
                });
            }).start();
        });

//        HBox startTestBox = new HBox(btnStartTest, testLabel);
//        startTestBox.setSpacing(10);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(btnReference, 0, 0);
        grid.add(referenceStatusLabel, 1, 0);

        grid.add(btnTarget, 0, 3);
        grid.add(targetStatusLabel, 1, 3);

        grid.add(btnStartSearch, 0,6 );
        grid.add(uploadStatusLabel, 1, 6);

        Separator separator2 = new Separator();
        separator2.setMinWidth(300);
        GridPane.setColumnSpan(separator2, 2);
        grid.add(separator2, 0, 8);

        grid.add(btnTest, 0, 15);
        grid.add(testStatusLabel, 1, 15);

        grid.add(btnStartTest, 0, 18);
        grid.add(testLabel, 1, 18);
//        VBox vbox = new javafx.scene.layout.VBox();
//        vbox.getChildren().addAll(btnReference, referenceStatusLabel, btnTarget, targetStatusLabel, btnStartTest, uploadStatusLabel);
//        VBox vbox = new VBox(referenceBox, targetBox, startTestBox, uploadBox, testBox);
//        vbox.setSpacing(20);
        Scene scene = new Scene(grid, 650, 400);
        primaryStage.setScene(scene);
        alert.initOwner(primaryStage);
        primaryStage.show();
    }
    private Alert createAlert(String title, String headerText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        return alert;
    }

}


