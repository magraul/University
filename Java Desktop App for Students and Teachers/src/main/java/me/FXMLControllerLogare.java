package me;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import me.boxes.AlertBox;
import me.repositories.NoteFileRepository;
import me.repositories.StudentFileRepository;
import me.repositories.TemeFileRepository;
import me.services.Service;
import me.services.config.ApplicationContext;
import me.validators.ValidatorNota;
import me.validators.ValidatorStudent;
import me.validators.ValidatorTema;
import org.controlsfx.control.textfield.TextFields;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FXMLControllerLogare implements Initializable, EventHandler {
    Service service = new Service(new StudentFileRepository(ApplicationContext.getPROPERTIES().getProperty("data.xmls.studenti")), new ValidatorStudent(), new TemeFileRepository(ApplicationContext.getPROPERTIES().getProperty("data.xmls.teme")), new ValidatorTema(), new NoteFileRepository(ApplicationContext.getPROPERTIES().getProperty("data.xmls.note")), new ValidatorNota());

    Map<String, String> adminiMap = new HashMap<>();
    Map<String, String> profiMap = new HashMap<>();
    Map<String, String> studentiMap = new HashMap<>();

    @FXML
    TextField userName;

    @FXML
    PasswordField password;

    @FXML
    Button signIn;


    @Override
    public void handle(Event event) {
        if(userName.getText().equals("") || password.getText().equals("")) {
            AlertBox.display("Eroare", "Nu ati introdus numele sau parola!");
            return;
        }
        if (event.getSource() == signIn) {
            String optiune = "";

            if (studentiMap.get(userName.getText()) != null && BCrypt.checkpw(password.getText(),studentiMap.get(userName.getText())))
                optiune = "student";
            if (adminiMap.get(userName.getText()) != null && adminiMap.get(userName.getText()).equals(password.getText()))
                optiune = "administrator";
            if (profiMap.get(userName.getText()) != null && profiMap.get(userName.getText()).equals(password.getText()))
                optiune = "profesor";

            if (optiune.equals("administrator")) {
                try {
                    FXMLLoader loaderAdmini = new FXMLLoader();
                    loaderAdmini.setLocation(getClass().getResource("/fxml/adminView.fxml"));

                    Parent viewLogare = loaderAdmini.load();

                    Stage windowNote = (Stage) ((Node) signIn).getScene().getWindow();
                    windowNote.hide();


                    Scene scene = new Scene(viewLogare);
                    Stage window = new Stage();
                    window.setTitle("Administrator");
                    window.setScene(scene);

                    FXMLControllerAdmin ctrl = loaderAdmini.getController();
                    ctrl.service = this.service;
                    ctrl.setComponente(userName.getText());
                    window.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (!optiune.equals("")) {
                try {
                    FXMLLoader loaderNote = new FXMLLoader();
                    loaderNote.setLocation(getClass().getResource("/fxml/noteView.fxml"));

                    Parent viewNote = loaderNote.load();

                    Scene scene = new Scene(viewNote);
                    Stage windowNote = (Stage) ((Node) event.getSource()).getScene().getWindow();

                    FXMLControllerNote ctrl = loaderNote.getController();
                    ctrl.service = this.service;
                    ctrl.setOption(optiune, userName.getText());
                    ctrl.setComponents();


                    windowNote.setScene(scene);
                    windowNote.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                AlertBox.display("Eroare", "User Name sau parola incorecte!");
            }
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        signIn.setOnAction(this);
    }


    public void setComponente() {
        incarcaDictionare();
    }

    private void incarcaDictionare() {
        studentiMap = service.getInregistrariStudenti();
        profiMap = service.getInregistrariProfesori();
        adminiMap = service.getInregistrariAdmini();
    }
}
