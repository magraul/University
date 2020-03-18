package me;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import me.boxes.AlertBox;
import me.services.Service;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class FXMLControllerConfigurare implements Initializable, EventHandler {

    private String materia;
    public Service service;

    @FXML
    TextField denumire, procentaj;

    @FXML
    Label numeMaterie;

    @FXML
    Button adauga;

    @FXML
    TextArea area;

    public void setComponents(String materia) {
        this.materia = materia;
        numeMaterie.setText(materia);
        area.setText(service.getCriterii(materia));
    }


    @Override
    public void handle(Event event) {
        if (event.getSource() == adauga) {
            if(procentaj.getText().equals("") || denumire.getText().equals("")){
                AlertBox.display("Eroare", "Nu ati completat tot!");
                return;
            }
            try {
                Float.parseFloat(procentaj.getText());
            } catch (NumberFormatException e) {
                AlertBox.display("Eroare", "Nota trebuie sa fie numar!");
                procentaj.setText("");
                return;
            }

            String textToAppend = materia + " " + denumire.getText() + " " + procentaj.getText() + "\n";
            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(
                        new FileWriter("data/formuleCalcul.txt", true)  //Set true for append mode
                );
                writer.write(textToAppend);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            denumire.setText("");
            procentaj.setText("");
            area.setText(service.getCriterii(this.materia));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        adauga.setOnAction(this);
        area.setEditable(false);
    }
}
