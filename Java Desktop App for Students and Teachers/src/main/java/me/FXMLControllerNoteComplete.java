package me;

import com.google.gson.internal.$Gson$Preconditions;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import me.entities.Nota;
import me.entities.NotaDto;
import me.services.Service;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class FXMLControllerNoteComplete implements Initializable, EventHandler {
    public Service service;
    private Stage stage;

    @FXML
    private TableView tabelNote;

    @FXML
    private TableColumn student, tema, nota, dataColumn, profesor, feedback;

    @FXML
     private TextField cauta;

    @FXML
    private Button exit;


    ObservableList<NotaDto> modelNote = FXCollections.observableArrayList();


    private List<NotaDto> getNoteDtoList() {
        List<Nota> for_return = new ArrayList<>();
        service.findAllNote().forEach(for_return::add);
        return for_return.stream()
                .map(n -> new NotaDto(n.getStudent().getNume() + " " + n.getStudent().getPrenume(), n.getTema().getDescriere(), n.getValoare().toString(), n.getData().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),n.getProfesor(),n.getFeedback()))
                .collect(Collectors.toList());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        modelNote.setAll(getNoteDtoList());
        tabelNote.setItems(modelNote);
    }


    @Override
    public void handle(Event event) {
        if(event.getSource() == exit)
            stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        student.setCellValueFactory(new PropertyValueFactory<NotaDto, String>("student"));
        tema.setCellValueFactory(new PropertyValueFactory<NotaDto, String>("tema"));
        nota.setCellValueFactory(new PropertyValueFactory<NotaDto, String>("nota"));
        dataColumn.setCellValueFactory(new PropertyValueFactory<NotaDto, String>("data"));
        profesor.setCellValueFactory(new PropertyValueFactory<NotaDto, String>("profesor"));
        feedback.setCellValueFactory(new PropertyValueFactory<NotaDto, String>("feedbackk"));

        cauta.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                handleFilter();
            }
        });

        exit.setOnAction(this);
    }

    private void handleFilter() {
        modelNote.setAll(getNoteDtoList().stream().filter(x->x.getStudent().contains(cauta.getText())).collect(Collectors.toList()));
    }
}
