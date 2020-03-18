package me;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import me.boxes.AlertBox;
import me.services.Service;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class FXMLControllerCatalog implements Initializable, EventHandler {
    public Service service;
    private Map<String, String> student_materie__note = new HashMap<>();
    @FXML
    ComboBox studenti, materii;

    @FXML
    Button genereazaSituatie, genereazaCSV, salvare;

    @FXML
    Label label;

    @FXML
    TextField note;

    @FXML
    TextArea area;

    @Override
    public void handle(Event event) {
        if (event.getSource() == materii) {
            label.setText("Introduceti notele studentului la urmatoarele probe: " + service.getFormulaCalcul(materii.getValue().toString()));
        } else if (event.getSource() == salvare) {
            student_materie__note.put(studenti.getValue().toString() + " " + materii.getValue().toString(), service.getFormulaCalcul(materii.getValue().toString()) + "_" + note.getText() + "_" + service.getProcente(materii.getValue().toString()));
            note.setText("");
        } else if (event.getSource() == genereazaSituatie) {
            if (student_materie__note.entrySet().size() != service.getListaMaterii().size()) {
                AlertBox.display("Eroare", "Nu ati introdus notele la toate materiile");
                return;
            }

            area.setText(service.getSituatieStudent(studenti.getValue().toString(), student_materie__note));
        } else if (event.getSource() == genereazaCSV) {
            String numeFisier = studenti.getValue().toString().split(" ")[0] + studenti.getValue().toString().split(" ")[1] + ".csv";

            String textForSave = service.getSituatieStudent1(studenti.getValue().toString(), student_materie__note);

            try (FileWriter fileWriter = new FileWriter("data/" + numeFisier)) {
                fileWriter.write(textForSave);
                fileWriter.close();
                AlertBox.display("Succes", "Fisierul CSV a fost salvat cu succes!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void serComponente() {
        studenti.setPromptText(service.getListaNumeStudenti().get(0));
        studenti.getItems().setAll(service.getListaNumeStudenti());
        materii.setPromptText(service.getListaMaterii().get(0));
        materii.getItems().setAll(service.getListaMaterii());
        student_materie__note.clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        studenti.setOnAction(this);
        genereazaSituatie.setOnAction(this);
        genereazaCSV.setOnAction(this);
        materii.setOnAction(this);
        area.setEditable(false);
        salvare.setOnAction(this);

    }
}
