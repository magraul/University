package me;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import me.boxes.AlertBox;
import me.exceptions.ValidationException;
import me.services.Service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class FXMLControllerAdaugaTema implements Initializable, EventHandler {
    public Service service;
    private Stage stage;
    private String materia;

    public void setStage(Stage stage, String materia) {
        this.stage = stage;
        this.materia = materia;
    }

    @FXML
    Button adauga, close;

    @FXML
    TextField textDescriere, textDeadLine;


    @Override
    public void handle(Event event) {
        if (event.getSource() == adauga) {
            if (textDeadLine.getText().equals(""))
                AlertBox.display("Eroare Adaugare tema", "Nu ati introdus deadline ul!");
            else {
                try {
                    service.addTema(textDescriere.getText(), Integer.parseInt(textDeadLine.getText()), materia);
                    textDeadLine.setText("");
                    textDescriere.setText("");
                } catch (ValidationException e) {
                    AlertBox.display("Eroare la adaugare", e.getEroare());
                }
            }
        } else if (event.getSource() == close) {
            System.out.println("ai apasatpe close in pop up");
            stage.close();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        adauga.setOnAction(this);
        close.setOnAction(this);
    }
}
