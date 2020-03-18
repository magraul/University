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
import me.entities.InregistrareDTO;
import me.entities.NotaDto;
import me.services.Service;
import org.controlsfx.control.textfield.TextFields;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class FXMLControllerAdmin implements Initializable, EventHandler {
    public Service service;

    ObservableList<InregistrareDTO> modelTabel = FXCollections.observableArrayList();

    @FXML
    TextField textFieldStudent, textFieldUserName, textFieldParola, numeMaterie;

    @FXML
    Button inregistrare, logOut, adaugaMaterie;

    @FXML
    TableView tabel;

    @FXML
    Label logatCaSi;

    @FXML
    TableColumn studentInregistrare, userNameInregistrare;

    @FXML
    TextArea area;

    @Override
    public void handle(Event event) {
        if (event.getSource() == logOut) {
            System.out.println("log out");

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
        } else if (event.getSource() == inregistrare) {
            InregistrareDTO i = new InregistrareDTO(textFieldStudent.getText(), textFieldUserName.getText());
            String parola = service.encode(textFieldParola.getText());
            service.addInregistrare(i, parola);
            modelTabel.setAll(service.getListaInregistrari());
            textFieldUserName.setText("");
            textFieldStudent.setText("");
            textFieldParola.setText("");
        } else if (event.getSource() == adaugaMaterie) {
            String textToAppend;
            if (service.anUniversitar.getDataInceputSem2().isAfter(LocalDate.now()))
                textToAppend = numeMaterie.getText() + " sem1" + "\n";
            else
                textToAppend = numeMaterie.getText() + " sem2" + "\n";

            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(
                        new FileWriter("data/materii.txt", true)  //Set true for append mode
                );
                writer.write(textToAppend);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            numeMaterie.setText("");
            area.setText(populeazaAreaMaterii());
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logOut.setOnAction(this);
        area.setEditable(false);
        adaugaMaterie.setOnAction(this);
        inregistrare.setOnAction(this);

        textFieldStudent.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                handleFilter();
            }
        });

        studentInregistrare.setCellValueFactory(new PropertyValueFactory<InregistrareDTO, String>("student"));
        userNameInregistrare.setCellValueFactory(new PropertyValueFactory<InregistrareDTO, String>("userName"));
    }

    private void handleFilter() {
        modelTabel.setAll(service.getAllInregistrari().stream().filter(x -> x.getStudent().contains(textFieldStudent.getText())).collect(Collectors.toList()));
    }

    public void setComponente(String userName) {
        area.setText(populeazaAreaMaterii());
        logatCaSi.setText("Logat ca si " + userName);
        modelTabel.setAll(service.getListaInregistrari());
        tabel.setItems(modelTabel);
        TextFields.bindAutoCompletion(textFieldStudent, service.getListaNumeStudenti());
    }

    private String populeazaAreaMaterii() {
        List<String> materii = service.getListaMaterii();
        String rez = "Materii an universitar " + service.anUniversitar.getAnUniversitar().toString() + " - " + (Integer.parseInt(service.anUniversitar.getAnUniversitar().toString())+1) + "\n\n";

        Path path = Paths.get("data/materii.txt");
        try {
            List<String> lines = Files.readAllLines(path);
            List<String> inSem1 = new ArrayList<>();
            List<String> inSem2 = new ArrayList<>();

            for(String linie : lines) {
                if (linie != "") {
                    String[] elems = linie.split(" ");
                    if (elems[1].equals("sem1"))
                        inSem1.add(elems[0]);
                    else inSem2.add(elems[0]);
                }
            }

            rez += "Semestrul 1\n";
            for(String m : inSem1)
                rez += m + "\n";
            rez += "\nSemestrul 2\n";
            for(String m : inSem2)
                rez += m + "\n";

        } catch (IOException e) {
            e.printStackTrace();
        }
        return rez;
    }
}
