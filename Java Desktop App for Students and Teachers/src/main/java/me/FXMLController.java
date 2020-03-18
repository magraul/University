package me;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;
import me.boxes.AlertBox;
import me.entities.Tema;
import me.exceptions.ValidationException;
import me.repositories.NoteFileRepository;
import me.repositories.StudentFileRepository;
import me.repositories.TemeFileRepository;
import me.services.Service;
import me.services.config.ApplicationContext;
import me.validators.ValidatorNota;
import me.validators.ValidatorStudent;
import me.validators.ValidatorTema;

public class FXMLController implements Initializable, EventHandler<ActionEvent> {

    Service service = new Service(new StudentFileRepository(ApplicationContext.getPROPERTIES().getProperty("data.xmls.studenti")), new ValidatorStudent(), new TemeFileRepository(ApplicationContext.getPROPERTIES().getProperty("data.xmls.teme")), new ValidatorTema(), new NoteFileRepository(ApplicationContext.getPROPERTIES().getProperty("data.xmls.note")), new ValidatorNota());

    @FXML
    private Label label1, label2;
    @FXML
    private Button butonCreate, butonRead, butonUpdate, butonDelete;

    @FXML
    private TextField textDescriere, textDeadline, textId;

    @FXML
    private TableView teme;
    @FXML
    private TableColumn id, descriere, startWeek, deadlineWeek;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("in functie");
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        label1.setText("This is JavaFX " + javafxVersion + "\nRunning on Java " + javaVersion + ".");
        label1.setStyle("-fx-text-fill: white");
        label2.setText("               Interfata Teme");
        Font f = new Font("Segoe Script", 40);
        label2.setStyle("-fx-text-fill: rgba(11,0,255,0.9)");
        label2.setFont(f);

        butonUpdate.setOnAction(this);
        butonRead.setOnAction(this);
        butonDelete.setOnAction(this);
        butonCreate.setOnAction(this);

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        descriere.setCellValueFactory(new PropertyValueFactory<>("descriere"));
        startWeek.setCellValueFactory(new PropertyValueFactory<>("startWeek"));
        deadlineWeek.setCellValueFactory(new PropertyValueFactory<>("deadlineWeek"));

        textDeadline.setStyle("-fx-background-color: rgba(107,144,178,0.36);-fx-font-weight: bold");
        textDescriere.setStyle("-fx-background-color: rgba(107,144,178,0.36);-fx-font-weight: bold");
        textId.setStyle("-fx-background-color: rgba(107,144,178,0.36);-fx-font-weight: bold");

    }

    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == butonRead) {
            System.out.println("ai apasat pe read!");
            teme.getItems().clear();
            Iterable<Tema> toate = service.findAllTeme();
            toate.forEach(tema -> {
                teme.getItems().add(tema);
            });
        } else if (event.getSource() == butonCreate) {
            System.out.println("ai apasat pe create");
            if (textDeadline.getText().equals(""))
                AlertBox.display("Eroare Adaugare", "Nu ati introdus deadline!");
            else {
                try {
                   // service.addTema(textDescriere.getText(), Integer.parseInt(textDeadline.getText()), materia);
                    textDescriere.setText("");
                    textDeadline.setText("");

                    teme.getItems().clear();
                    Iterable<Tema> toate = service.findAllTeme();
                    toate.forEach(tema -> {
                        teme.getItems().add(tema);
                    });
                } catch (ValidationException e) {
                    String msg = e.getEroare();
                    AlertBox.display("Eroare Adaugare", msg);
                }
            }

        } else if (event.getSource() == butonUpdate) {
            System.out.println("ai apasat pe update");
            if (textId.getText().equals(""))
                AlertBox.display("Eroare Update", "Nu ati introdus id ul!");
            else if (textDeadline.getText().equals(""))
                AlertBox.display("Eroare Update", "Nu ati introdus deadline!");
            else {
                try {
                    service.updateTema(Long.parseLong(textId.getText()), textDescriere.getText(), Integer.parseInt(textDeadline.getText()));
                    textDeadline.setText("");
                    textId.setText("");
                    textDescriere.setText("");

                    teme.getItems().clear();
                    Iterable<Tema> toate = service.findAllTeme();
                    toate.forEach(tema -> {
                        teme.getItems().add(tema);
                    });
                } catch (ValidationException e) {
                    String msg = e.getEroare();
                    AlertBox.display("Eroare Update", msg);
                } catch (IllegalArgumentException e) {
                    AlertBox.display("Eroare Update", e.getMessage());
                }
            }
        } else if (event.getSource() == butonDelete) {
            System.out.println("ai apasat delete");
            if (textId.getText().equals(""))
                AlertBox.display("Eroare delete", "Nu ati dat id ul!");
            else {
                try {
                    service.deleteTema(Long.parseLong(textId.getText()));
                    textId.setText("");

                    teme.getItems().clear();
                    Iterable<Tema> toate = service.findAllTeme();
                    toate.forEach(tema -> {
                        teme.getItems().add(tema);
                    });
                } catch (IllegalArgumentException e) {
                    AlertBox.display("Eroare Delete", e.getMessage());
                }
            }
        }
    }
}