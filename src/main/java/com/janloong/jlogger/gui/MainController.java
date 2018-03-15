package com.janloong.jlogger.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

/**
 * @author Janloong
 * @create 2017-12-21 11:03
 **/
public class MainController {


    public BorderPane bp_root;

    public void caa(MouseEvent mouseEvent) throws IOException {
        //Parent load = FXMLLoader.load(getClass().getResource("../../../../../resources/fxml/TestContent.fxml"));
        Parent load = FXMLLoader.load(getClass().getResource("/fxml/TestContent.fxml"));

        //Pane pane = new Pane(load);
        //Text text = new Text();
        //text.setText("干");
        bp_root.setCenter(load);
        new TestContent().c();
    }
}
