package com.janloong.jlogger;

import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.svg.SVGGlyph;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * @author Janloong
 * @create 2017-12-21 11:00
 **/
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainApp.fxml"));
        JFXDecorator decorator = new JFXDecorator(primaryStage, root);
        decorator.setCustomMaximize(true);
        decorator.setText("日志管理器                By Janloong");
        decorator.setGraphic(new SVGGlyph(""));
        double width = 800;
        double height = 600;
        Rectangle2D bounds = Screen.getScreens().get(0).getBounds();
        width = bounds.getWidth() / 2;
        height = bounds.getHeight() / 1.5;
        Scene scene = new Scene(decorator, width, height);

        final ObservableList<String> stylesheets = scene.getStylesheets();
        stylesheets.addAll(MainApp.class.getResource("/css/jfoenix-fonts.css").toExternalForm(),
                MainApp.class.getResource("/css/jfoenix-design.css").toExternalForm(),
                MainApp.class.getResource("/css/builder-main.css").toExternalForm());

        primaryStage.setTitle("curlbuilder demo");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
