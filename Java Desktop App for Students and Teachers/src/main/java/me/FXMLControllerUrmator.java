package me;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import me.boxes.AlertBox;
import me.entities.*;
import me.exceptions.ValidationException;
import me.services.Service;

import java.net.URL;
import java.util.ResourceBundle;

public class FXMLControllerUrmator implements Initializable, EventHandler {
    public Service service;
    private Stage stage;
    private StudentDto studentDto;
    private boolean aUitatProful, seFaceAdd;
    private Integer nrSaptMotivate;
    private NotaDto notaDto;
    String nota, feedback, tema;

    @FXML
    TextField textNota;

    @FXML
    private
    TextArea textFeedback;

    @FXML
    Button adauga;

    @FXML
    Label labNota, labDesc;

    private TextField textField1, textField2;

    public void setStage(Stage stage) {


        //calcule

    }

    public void setComponents(Stage stage, StudentDto studentDto, NotaDto notaDto, String text, String text1, Object comboBoxValue, boolean selectedAuitatProful, Object valueNrSaptMotivate, boolean adaugare, TextField textFieldNota, TextField textFieldFeedback) {
        this.studentDto = studentDto;
        this.nota = text;
        this.feedback = text1;
        this.tema = (String) comboBoxValue;
        this.aUitatProful = selectedAuitatProful;
        this.nrSaptMotivate = (Integer) valueNrSaptMotivate;
        this.seFaceAdd = adaugare;
        this.notaDto = notaDto;
        this.stage = stage;
        this.textField1 = textFieldNota;
        this.textField2 = textFieldFeedback;

        if(seFaceAdd == false) {
            adauga.setText("Modifica Nota");
            labNota.setText("Nota noua");
            labDesc.setText("Feedback nou");
            textNota.setText(notaDto.getNota());
            textFeedback.setText(notaDto.getFeedbackk());
        }

        if(notaDto != null) {
            //update
            textNota.setText(notaDto.getNota());
            textFeedback.setText(notaDto.getFeedbackk());
        } else {
            Tema t = service.getTemabyDescriere(tema);
            Student s = service.getStudentbyID(studentDto.getId());
            String pretendent = service.calculeazaNota(nota, t.getDeadlineWeek(), nrSaptMotivate, aUitatProful);

            textNota.setText(pretendent);
            Integer valNou = Integer.parseInt(pretendent);
            Integer valVeche = Integer.parseInt(nota);
            int dif = valNou-valVeche;
            if(dif<0)
                textFeedback.setText(feedback+ "\nA fost aplicata o penalizare!\n De " + Math.abs(dif) + " puncte!\nDin cauza intarzierii");
            else
                textFeedback.setText(feedback);
        }
    }

    @Override
    public void handle(Event event) {
        if(event.getSource() == adauga) {
            System.out.println("in addd urmator");
            try {
                if (seFaceAdd) {

                    service.addNota(service.getStudentbyID(studentDto.getId()), service.getTemabyDescriere(tema), Integer.parseInt(textNota.getText()), textFeedback.getText(),aUitatProful);
                } else {
                    Nota nota = service.getNota(notaDto.getId());
                    service.modificaNota(nota.getStudent(), nota.getTema(),Integer.parseInt(textNota.getText()), textFeedback.getText());
                }
            }catch (ValidationException e ) {
                AlertBox.display("Eroare", "Eroare la adaugare tema sau modificare!");
            }
            stage.close();
            textField1.setText("");
            textField2.setText("");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        adauga.setOnAction(this);


    }
}
