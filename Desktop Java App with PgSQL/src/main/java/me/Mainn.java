package me;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.entities.Angajat;
import me.entities.CazCaritabil;
import me.entities.Donatie;
import me.entities.Donator;
import me.repositories.AngajatiDBRepository;
import me.repositories.CazuriDBRepository;
import me.repositories.DonatiiDBRepository;
import me.repositories.DonatoriDBRepository;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class Mainn extends Application {
    public static void main(String[] args) {



        launch(args);
    }

    @Override
    public void start(Stage window) throws Exception {
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

