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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import me.boxes.AlertBox;
import me.entities.*;
import me.events.EvenimentSchimbare;
import me.observer.Observer;
import me.services.Service;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class FXMLControllerNote implements Initializable, EventHandler, Observer<EvenimentSchimbare> {

    Service service;
    private String optiune, userName;

    ObservableList<StudentDto> modelStudenti = FXCollections.observableArrayList();
    ObservableList<NotaDto> modelNote = FXCollections.observableArrayList();

    @FXML
    private TableView tabelNote;

    @FXML
    Button urmator, butonTeme, butonNoteCompl, butonRapoarte, configurare, catalog;

    @FXML
    TableColumn coloanaStudent, coloanaTema, coloanaNota;

    @FXML
    TextField textFieldCauta, textFieldNota, textFieldFeedback;

    @FXML
    ComboBox comboBox, comboBoxNrSaptamaniMotivate, materii;

    @FXML
    CheckBox checkBox, checkBoxAUitatProful;

    @FXML
    Label titluTema, titluFedback, titluNota, titluNumarSaptamani, logatCaSi;

    @FXML
    MenuItem logOut;

    @FXML
    MenuBar menuBar;


    private List<String> getListaTemeFaraNota(String id, String materia) {
        List<Tema> teme = service.temeFaraNotaAleStudentului(id);
        return teme.stream().filter(x->x.getMaterie().equals(materia)).map(Tema::getDescriere).collect(Collectors.toList());
    }

    @Override
    public void handle(Event event) {
        StudentDto selected = service.getStudentDtobyName(textFieldCauta.getText());
        NotaDto selectedNota = (NotaDto) tabelNote.getSelectionModel().getSelectedItem();
        if (event.getSource() == tabelNote) {
            if (checkBox.isSelected())
                comboBox.getItems().setAll(service.getAllTemeDesc());
            else {
                comboBox.getItems().setAll(getListaTemeFaraNota(selected.getId(), materii.getValue().toString()));
                Tema t = service.getTemabyDescriere(service.getDescTemaCurentWeek());
                boolean gasit = false;
                for (Nota n : service.findAllNote()) {
                    if (n.getTema().getId() == t.getId() && Long.parseLong(selected.getId()) == n.getStudent().getId()) {
                        comboBox.setPromptText(getListaTemeFaraNota(selected.getId(), materii.getValue().toString()).get(0));
                        gasit = true;
                        break;
                    }
                }
                if (gasit == false)
                    comboBox.setPromptText(service.getDescTemaCurentWeek());
            }

        } else if (event.getSource() == checkBox) {
            comboBox.getItems().setAll(service.getAllTemeDesc());
        } else if (event.getSource() == urmator) {
            StudentDto selectedd = service.getStudentDtobyName(textFieldCauta.getText());
            NotaDto selectednotaa = (NotaDto) tabelNote.getSelectionModel().getSelectedItem();

            if (selectedNota != null && checkBox.isSelected()) {
                try {
                    newPopUpUrmator(selected, selectedNota);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
            try {
                Integer.parseInt(textFieldNota.getText());
            } catch (NumberFormatException e) {
                AlertBox.display("Eroare", "Nota trebuie sa fie numar intreg");
                return;
            }
            if (checkBox.isSelected()) {
//                //se va face update
//                if (selectednotaa != null) {
//                    try {
//                        newPopUpUrmator(selected, selectedNota);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    AlertBox.display("Eroare", "Nu ati selectat o nota din tabel!");
//                }

            } else {
                //add
                if (selectedd != null) {
                    try {
                        if (textFieldNota.getText().equals(""))
                            AlertBox.display("Eroare", "Nu ati introdus nota!");
                        else
                            newPopUpUrmator(selected, selectedNota);
                    } catch (IOException | NumberFormatException e) {
                        e.printStackTrace();
                        AlertBox.display("Eroare", e.getMessage());
                    }

                } else
                    AlertBox.display("Eroare", "Nu ati selectat un student sau tema!");
            }

        } else if (event.getSource() == butonNoteCompl) {
            try {
                newPopUpNoteComplete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (event.getSource() == butonTeme) {
            try {
                FXMLLoader loaderTeme = new FXMLLoader();
                loaderTeme.setLocation(getClass().getResource("/fxml/temeView.fxml"));
                Parent viewTeme = loaderTeme.load();

                Scene scene = new Scene(viewTeme);
                Stage windowTeme = (Stage) ((Node) event.getSource()).getScene().getWindow();

                FXMLControllerTeme ctrl = loaderTeme.getController();
                ctrl.service = this.service;
                ctrl.setOption(this.optiune, this.userName);
                ctrl.setComponente();

                windowTeme.setScene(scene);
                windowTeme.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (event.getSource() == butonRapoarte) {
            try {
                FXMLLoader loaderRapoarte = new FXMLLoader();
                loaderRapoarte.setLocation(getClass().getResource("/fxml/rapoarteView.fxml"));

                Parent viewRapoarte = loaderRapoarte.load();

                Scene scene = new Scene(viewRapoarte);
                Stage windowRapoarte = (Stage) ((Node) event.getSource()).getScene().getWindow();

                FXMLControllerRapoarte ctrl = loaderRapoarte.getController();
                ctrl.service = this.service;
                ctrl.setOption("profesor", this.userName);
                ctrl.setComponents();

                windowRapoarte.setScene(scene);
                windowRapoarte.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (event.getSource() == logOut) {
            System.out.println("log out");

            try {
                FXMLLoader loaderLogare = new FXMLLoader();
                loaderLogare.setLocation(getClass().getResource("/fxml/logareView.fxml"));

                Parent viewLogare = loaderLogare.load();

                Stage windowNote = (Stage) ((Node) menuBar).getScene().getWindow();
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
        } else if(event.getSource() == materii) {
            if(textFieldCauta.getText().equals("")) {
                modelNote.setAll(service.getNoteDtoMaterieList(materii.getValue().toString()));

            }
            else {
                modelNote.setAll(service.getNoteStudentMaterieDtoList(textFieldCauta.getText(), materii.getValue().toString()));
                comboBox.getItems().setAll(getListaTemeFaraNota(selected.getId(), materii.getValue().toString()));
            }
        } else if(event.getSource() == configurare) {
            if(materii.getValue() == null){
                AlertBox.display("Eroare", "Nu ati ales o materie!");
                return;
            }
            try {
                newPopUpConfigurare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if(event.getSource() == catalog) {
            try {
                newPopUpCatalog();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void newPopUpCatalog() throws IOException {
        FXMLLoader loaderCatalog = new FXMLLoader();
        loaderCatalog.setLocation(getClass().getResource("/fxml/catalogView.fxml"));

        Parent root = loaderCatalog.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        Scene scene = new Scene(root);
        stage.setScene(scene);

        FXMLControllerCatalog ctrl = loaderCatalog.getController();
        ctrl.service = this.service;
        ctrl.serComponente();
        stage.show();
    }

    private void newPopUpConfigurare() throws IOException {
        FXMLLoader loaderConfigurare = new FXMLLoader();
        loaderConfigurare.setLocation(getClass().getResource("/fxml/configurareView.fxml"));

        Parent root = loaderConfigurare.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        Scene scene = new Scene(root);
        stage.setScene(scene);

        FXMLControllerConfigurare ctrl = loaderConfigurare.getController();
        ctrl.service = this.service;
        ctrl.setComponents(materii.getValue().toString());
        stage.show();
    }

    private void newPopUpNoteComplete() throws IOException {
        FXMLLoader loaderNoteComplete = new FXMLLoader();
        loaderNoteComplete.setLocation(getClass().getResource("/fxml/viewPopUpNoteComplete.fxml"));

        Parent root = loaderNoteComplete.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        Scene scene = new Scene(root);
        stage.setScene(scene);

        FXMLControllerNoteComplete ctrl = loaderNoteComplete.getController();
        ctrl.service = this.service;

        ctrl.setStage(stage);
        stage.show();

    }

    private void newPopUpUrmator(StudentDto studentDto, NotaDto notaDto) throws IOException {
        FXMLLoader loaderUrmator = new FXMLLoader();
        loaderUrmator.setLocation(getClass().getResource("/fxml/viewPopUpUrmator.fxml"));

        AnchorPane root = loaderUrmator.load();

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        Scene scene = new Scene(root);
        stage.setScene(scene);

        FXMLControllerUrmator ctrl = loaderUrmator.getController();
        ctrl.service = this.service;
        boolean adaugare = true;
        if (checkBox.isSelected())
            adaugare = false;
        String desc = service.getDescTemaCurentWeek();
        if (desc != "" && comboBox.getValue() == null)
            ctrl.setComponents(stage, studentDto, notaDto, textFieldNota.getText(), textFieldFeedback.getText(), desc, checkBoxAUitatProful.isSelected(), comboBoxNrSaptamaniMotivate.getValue(), adaugare, textFieldNota, textFieldFeedback);
        else {
            if (comboBox.getValue() == null) {
                if (!checkBox.isSelected()) {
                    AlertBox.display("Eroare", "Nu exista o tema cu predare in saptamana aceasta!\nTrebuie sa selectati o tema!");
                    return;
                } else {
                    ctrl.setComponents(stage, studentDto, notaDto, textFieldNota.getText(), textFieldFeedback.getText(), notaDto.getTema(), checkBoxAUitatProful.isSelected(), comboBoxNrSaptamaniMotivate.getValue(), adaugare, textFieldNota, textFieldFeedback);
                }
            } else
                ctrl.setComponents(stage, studentDto, notaDto, textFieldNota.getText(), textFieldFeedback.getText(), comboBox.getValue(), checkBoxAUitatProful.isSelected(), comboBoxNrSaptamaniMotivate.getValue(), adaugare, textFieldNota, textFieldFeedback);

        }
//        textFieldNota.setText("");
//        textFieldFeedback.setText("");
        stage.show();
    }

    private List<StudentDto> getStudentsDtoList() {
        List<Student> for_return = new ArrayList<>();
        service.findAllStudenti().forEach(for_return::add);
        return for_return.stream()
                .map(s -> new StudentDto(s.getId().toString(), s.getNume() + " " + s.getPrenume(), s.getGrupa()))
                .collect(Collectors.toList());
    }

    private List<NotaDto> getNoteDtoList() {
        List<Nota> for_return = new ArrayList<>();
        service.findAllNote().forEach(for_return::add);
        return for_return.stream()
                .map(n -> new NotaDto(n.getId(), n.getStudent().getNume() + " " + n.getStudent().getPrenume(), n.getTema().getDescriere(), n.getValoare().toString(), n.getFeedback()))
                .collect(Collectors.toList());
    }

    void setComponents() {
        logatCaSi.setText("Logat ca si " + this.userName);
        materii.getItems().setAll(service.getListaMaterii());
        if(optiune.equals("student")) {
            configurare.setVisible(false);
            catalog.setVisible(false);
            System.out.println("student");
            textFieldCauta.setText(service.getStudentbyUserName(this.userName));
            textFieldCauta.setEditable(false);
            titluTema.setVisible(false);
            comboBox.setVisible(false);
            titluFedback.setVisible(false);
            titluNota.setVisible(false);
            comboBoxNrSaptamaniMotivate.setVisible(false);
            checkBox.setVisible(false);
            checkBoxAUitatProful.setVisible(false);
            textFieldNota.setVisible(false);
            textFieldFeedback.setVisible(false);
            titluNumarSaptamani.setVisible(false);
            urmator.setVisible(false);
            butonNoteCompl.setVisible(false);
            modelNote.setAll(service.getNoteDtoStudentList(service.getStudentbyUserName(this.userName)));
            tabelNote.setItems(modelNote);
            butonRapoarte.setVisible(false);
        }else {
            comboBox.setPromptText(service.getDescTemaCurentWeek());

            List<String> listNames = service.getListaNumeStudenti();
            TextFields.bindAutoCompletion(textFieldCauta, listNames);
            modelNote.setAll(getNoteDtoList());
            tabelNote.setItems(modelNote);

        }
        service.addObserver(this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        catalog.setOnAction(this);
        materii.setOnAction(this);
        configurare.setOnAction(this);
        // coloanaNume.setCellValueFactory(new PropertyValueFactory<StudentDto, String>("numeComplet"));
        // coloanaGrupa.setCellValueFactory(new PropertyValueFactory<StudentDto, String>("grupa"));

        coloanaStudent.setCellValueFactory(new PropertyValueFactory<NotaDto, String>("student"));
        coloanaTema.setCellValueFactory(new PropertyValueFactory<NotaDto, String>("tema"));
        coloanaNota.setCellValueFactory(new PropertyValueFactory<NotaDto, String>("nota"));

        textFieldCauta.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String[] elems = textFieldCauta.getText().split(" ");
                if (elems.length == 2) {
                    StudentDto studentDto = service.getStudentDtobyName(textFieldCauta.getText());
                    if (studentDto != null) {
                        if(materii.getValue()!=null) {
                            comboBox.setPromptText(getListaTemeFaraNota(studentDto.getId(), materii.getValue().toString()).get(0));
                            comboBox.getItems().setAll(getListaTemeFaraNota(studentDto.getId(), materii.getValue().toString()));
                        }
                    }
                } else if (textFieldCauta.getText().equals("")) {
                    comboBox.setPromptText(service.getDescTemaCurentWeek());
                    comboBox.getItems().setAll(new ArrayList());
                }
                handleFilter();
            }
        });
        urmator.setOnAction(this);
        //  tabelStudenti.setOnMouseClicked(this);
        checkBox.setOnAction(this);
        comboBox.setOnAction(this);
        butonNoteCompl.setOnAction(this);
        butonRapoarte.setOnAction(this);
        logOut.setOnAction(this);

        comboBoxNrSaptamaniMotivate.getItems().setAll(0, 1, 2, 3, 4, 5, 6, 7, 8);
        comboBoxNrSaptamaniMotivate.getSelectionModel().selectFirst();
        butonTeme.setOnAction(this);
    }

    private void handleFilter() {
        //  modelStudenti.setAll(getStudentsDtoList().stream().filter(x -> x.getNumeComplet().contains(textFieldCauta.getText())).collect(Collectors.toList()));
       // modelNote.setAll(getNoteDtoList().stream().filter(x -> x.getStudent().contains(textFieldCauta.getText())).collect(Collectors.toList()));
        List<NotaDto> forSet = new ArrayList<>();
        if(textFieldCauta.getText().equals("")){
            if(materii.getValue() != null)
                forSet = service.getNoteDtoMaterieList(materii.getValue().toString());
        }else{
            if(materii.getValue() != null) {
                for (Nota n : service.getAllNote()) {
                    if ((n.getStudent().getNume() + " " + n.getStudent().getPrenume()).contains(textFieldCauta.getText()) && n.getTema().getMaterie().equals(materii.getValue().toString()))
                        forSet.add(new NotaDto(n.getId(), n.getStudent().getNume() + " " + n.getStudent().getPrenume(), n.getTema().getDescriere(), n.getValoare().toString(), n.getFeedback()));
                }
            } else forSet = getNoteDtoList().stream().filter(x -> x.getStudent().contains(textFieldCauta.getText())).collect(Collectors.toList());
        }
                modelNote.setAll(forSet);
    }

    @Override
    public void update(EvenimentSchimbare evenimentSchimbare) {
        modelNote.setAll(getNoteDtoList());
    }

    public void setOption(String optiune, String text) {
        this.userName = text;
        this.optiune = optiune;
    }
}
