package me;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import me.entities.AngajatDTO;
import me.services.Service;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class FXMLControllerAdmin implements Initializable, EventHandler {
    public Service service;

    ObservableList<AngajatDTO> modelTabel = FXCollections.observableArrayList();

    @FXML
    Button logOut, inregistrare;

    @FXML
    TableView tabel;

    @FXML
    TableColumn nume, username;

    @FXML
    TextField angajat, usernameAngajat;

    @FXML
    PasswordField parola;

    @Override
    public void handle(Event event) {
        if (event.getSource() == logOut) {
            try {
                FXMLLoader loaderLogare = new FXMLLoader();
                loaderLogare.setLocation(getClass().getResource("/fxml/logareView.fxml"));

                Parent viewLogare = loaderLogare.load();

                Stage windowNote = (Stage) ((Node) event.getSource()).getScene().getWindow();
                windowNote.hide();


                Scene scene = new Scene(viewLogare);
                Stage window = new Stage();
                window.setTitle("Logare");
                window.setScene(scene);

                FXMLControllerLogare ctrl = loaderLogare.getController();
                ctrl.setComponente();
                window.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (event.getSource() == inregistrare) {
            AngajatDTO i = new AngajatDTO(angajat.getText(), usernameAngajat.getText());
            String pass = service.encode(parola.getText());
            service.updateAngajat(i, pass);
            modelTabel.setAll(service.getAllAngajati());
            angajat.setText("");
            usernameAngajat.setText("");
            parola.setText("");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logOut.setOnAction(this);
        inregistrare.setOnAction(this);

        angajat.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                handleFilter(); }
        });

        nume.setCellValueFactory(new PropertyValueFactory<AngajatDTO, String>("nume"));
        username.setCellValueFactory(new PropertyValueFactory<AngajatDTO, String>("username"));
    }

    private void handleFilter() {
        modelTabel.setAll(service.getAllAngajati().stream().filter(x->x.getNume().contains(angajat.getText())).collect(Collectors.toList()));
    }

    public void setComponente() {
        modelTabel.setAll(service.getAllAngajati());
        tabel.setItems(modelTabel);
        TextFields.bindAutoCompletion(angajat, service.getListaNumeAngajati());
    }
}
