package me;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.w3c.dom.Document;

import javax.swing.*;
import javax.swing.text.ParagraphView;
import java.io.File;
import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        System.out.println("hi");


         launch(args);
    }

    @Override
    public void start(Stage window) throws Exception {
       /* FXMLLoader loaderTeme = new FXMLLoader();
        loaderTeme.setLocation(getClass().getResource("/fxml/temeView.fxml"));

        Parent root = loaderTeme.load();

        Scene scene = new Scene(root);
        // scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

        window.setTitle("JavaFX and Gradle");
        window.setScene(scene);
        FXMLControllerTeme ctrl = loaderTeme.getController();
        ctrl.setComponente();
        window.show();*/

       FXMLLoader loaderLogare = new FXMLLoader();
       loaderLogare.setLocation(getClass().getResource("/fxml/logareView.fxml"));
       Parent root = loaderLogare.load();

       Scene scene = new Scene(root);

       window.setTitle("Logare");
       window.setScene(scene);
       FXMLControllerLogare ctrl = loaderLogare.getController();
       ctrl.setComponente();
       window.show();
    }
}
