package me;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import me.boxes.AlertBox;
import me.entities.Tema;
import me.exceptions.ValidationException;
import me.services.Service;

import java.awt.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class FXMLControllerModificaTema implements Initializable, EventHandler {
    public Service service;
    private Stage stage;

    private Tema tema;

    public void setTema(Tema tema) {
        this.tema = tema;
        this.textDescriere.setText(tema.getDescriere());
        this.textDeadLine.setText(tema.getDeadlineWeek().toString());
    }

    @FXML
    Button modifica, close;

    @FXML
    TextField textDescriere, textDeadLine;


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void handle(Event event) {
        if (event.getSource() == modifica) {

            try {
                service.updateTema(tema.getId(), textDescriere.getText(), Integer.parseInt(textDeadLine.getText()));
                textDeadLine.setText("");
                textDescriere.setText("");
            } catch (ValidationException e) {
                AlertBox.display("Eroare la update", e.getEroare());
            }

        } else if (event.getSource() == close) {
            stage.close();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        modifica.setOnAction(this);
        close.setOnAction(this);
    }
}
