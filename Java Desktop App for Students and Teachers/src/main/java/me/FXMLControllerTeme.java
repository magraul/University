package me;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import me.boxes.AlertBox;
import me.entities.Tema;
import me.events.EvenimentSchimbare;
import me.observer.Observer;
import me.repositories.NoteFileRepository;
import me.repositories.StudentFileRepository;
import me.repositories.TemeFileRepository;
import me.services.Service;
import me.services.config.ApplicationContext;
import me.validators.ValidatorNota;
import me.validators.ValidatorStudent;
import me.validators.ValidatorTema;

public class FXMLControllerTeme implements Initializable, EventHandler<ActionEvent>, Observer<EvenimentSchimbare> {

    public Service service;// = new Service(new StudentFileRepository(ApplicationContext.getPROPERTIES().getProperty("data.xmls.studenti")), new ValidatorStudent(), new TemeFileRepository(ApplicationContext.getPROPERTIES().getProperty("data.xmls.teme")), new ValidatorTema(), new NoteFileRepository(ApplicationContext.getPROPERTIES().getProperty("data.xmls.note")), new ValidatorNota());

    ObservableList<Tema> modelTeme = FXCollections.observableArrayList();

    @FXML
    private Label label1, label2;
    @FXML
    private Button butonAdauga, butonModifica, butonSterge, butonNote, butonRapoarte;

    @FXML
    private TextField textCautaDescriere, textCautaStartWeek;

    @FXML
    private Pagination pagination;

    @FXML
    private TableView tabelTeme;

    @FXML
    private TableColumn id, descriere, startWeek, deadlineWeek;

    @FXML
    MenuItem logOut;

    @FXML
    private MenuBar menuBar;

    private int rowCount = 5;
    private String optiune, userName;

    @FXML
    private ComboBox materia;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("in functie");
        materia.setOnAction(this);
        butonAdauga.setOnAction(this);
        butonModifica.setOnAction(this);
        butonSterge.setOnAction(this);
        butonNote.setOnAction(this);
        butonRapoarte.setOnAction(this);
        logOut.setOnAction(this);

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        descriere.setCellValueFactory(new PropertyValueFactory<>("descriere"));
        startWeek.setCellValueFactory(new PropertyValueFactory<>("startWeek"));
        deadlineWeek.setCellValueFactory(new PropertyValueFactory<>("deadlineWeek"));

        //modelTeme.setAll(getTemeList());
//        tabelTeme.setItems(modelTeme);

        textCautaDescriere.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (textCautaDescriere.getText().isEmpty())
                    if (materia.getValue() == null)
                        doPagination();
                    else
                        doFilteredPagination(service.getTemeLaMateria(materia.getValue().toString()));
                else
                    handleFilter();
            }
        });


        textCautaStartWeek.textProperty().addListener((x, y, z) -> handleFilter());
        tabelTeme.setItems(modelTeme);
    }

    public void handleFilter() {
        Predicate<Tema> p1 = x -> x.getDescriere().contains(textCautaDescriere.getText());
        Predicate<Tema> p2 = x -> x.getStartWeek().toString().contains(textCautaStartWeek.getText());
        /*modelTeme.setAll(getTemeList().stream()
                .filter(p1.and(p2))
                .collect(Collectors.toList()));*/
        List<Tema> rez = getTemeList().stream()
                .filter(p1.and(p2))
                .collect(Collectors.toList());
        doFilteredPagination(rez);
    }

    private void doFilteredPagination(List<Tema> rez) {
        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
                int pageNr;
                if (rez.size() % rowCount == 0)
                    pageNr = rez.size() / rowCount;
                else
                    pageNr = rez.size() / rowCount + 1;
                pagination.setPageCount(pageNr);

                int fromIndex = pageIndex * rowCount;
                int toIndex = Math.min(fromIndex + rowCount, rez.size());
                List<Tema> elems = new ArrayList<>();

                for (int i = fromIndex; i < toIndex; i++)
                    elems.add(rez.get(i));

                modelTeme.setAll(FXCollections.observableArrayList(elems));
                pagination.currentPageIndexProperty().setValue(pageIndex);

                pagination.setCurrentPageIndex(pageIndex);
                //  p.setLayoutX(110);
                // p.setLayoutY(60);
                return new AnchorPane(tabelTeme);
            }
        });

        pagination.setLayoutX(0);
        pagination.setLayoutY(40);
    }

    public List<Tema> getTemeList() {
        List<Tema> for_return = new ArrayList<>();
        service.findAllTeme().forEach(for_return::add);
        return for_return;
    }


    public void newPopUpAdauga() throws IOException {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loaderAdauga = new FXMLLoader();
        loaderAdauga.setLocation(getClass().getResource("/fxml/viewPopUpAdaugaTema.fxml"));

        Parent root = loaderAdauga.load();
        FXMLControllerAdaugaTema ctrl = loaderAdauga.getController();

        ctrl.service = this.service;
        ctrl.setStage(stage, materia.getValue().toString());

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    private void newPopUpModifica(Tema selected) throws IOException {

        FXMLLoader loaderModifica = new FXMLLoader();
        loaderModifica.setLocation(getClass().getResource("/fxml/viewPopUpModificaTema.fxml"));

        AnchorPane root = loaderModifica.load();


        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);

        Scene scene = new Scene(root);
        stage.setScene(scene);

        FXMLControllerModificaTema ctrl = loaderModifica.getController();
        ctrl.service = service;
        ctrl.setStage(stage);
        ctrl.setTema(selected);
        doPagination();
        stage.show();
    }


    private void newPopUpSterge(Tema tema) throws IOException {
        service.deleteTema(tema.getId());
        doPagination();
    }

    void setComponente() {
        materia.setPromptText("MATERIA");
        materia.getItems().setAll(service.getListaMaterii());
        if (optiune.equals("student")) {
            service.addObserver(this);
            butonAdauga.setVisible(false);
            butonModifica.setVisible(false);
            butonSterge.setVisible(false);
            butonRapoarte.setVisible(false);
        }
        doPagination();
    }

    private void doPagination() {
        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
                int pageNr;
                if (service.getAllTeme().size() % rowCount == 0)
                    pageNr = service.getAllTeme().size() / rowCount;
                else
                    pageNr = service.getAllTeme().size() / rowCount + 1;
                pagination.setPageCount(pageNr);

                int fromIndex = pageIndex * rowCount;
                int toIndex = Math.min(fromIndex + rowCount, service.getAllTeme().size());

                List<Tema> elems = service.getTemeInterval(fromIndex, toIndex);

                modelTeme.setAll(FXCollections.observableArrayList(elems));
                pagination.currentPageIndexProperty().setValue(pageIndex);

                pagination.setCurrentPageIndex(pageIndex);
                //  p.setLayoutX(110);
                // p.setLayoutY(60);
                return new AnchorPane(tabelTeme);
            }
        });

        pagination.setLayoutX(0);
        pagination.setLayoutY(40);
        // pagination.setPrefSize(10,10);
    }

    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == butonAdauga) {
            System.out.println("ai apasat pe ADAUGA");
            if(materia.getValue() == null) {
                AlertBox.display("Eroare", "Nu ati selectat o materie!");
                return;
            }
            try {
                newPopUpAdauga();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (event.getSource() == butonModifica) {
            Tema selected = (Tema) tabelTeme.getSelectionModel().getSelectedItem();
            if (selected != null) {
                try {
                    newPopUpModifica(selected);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else
                AlertBox.display("Eroare", "Nu ati selectat o tema pentru modificare!");
        } else if (event.getSource() == butonSterge) {
            Tema selected = (Tema) tabelTeme.getSelectionModel().getSelectedItem();
            if (selected != null) {
                try {
                    newPopUpSterge(selected);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else
                AlertBox.display("Eroare", "Nu ati selectat o tema pentru stergere!");

        } else if (event.getSource() == butonNote) {
            System.out.println("butin notr");
            try {
                FXMLLoader loaderNote = new FXMLLoader();
                loaderNote.setLocation(getClass().getResource("/fxml/noteView.fxml"));

                Parent viewNote = loaderNote.load();

                Scene scene = new Scene(viewNote);
                Stage windowNote = (Stage) ((Node) event.getSource()).getScene().getWindow();

                FXMLControllerNote ctrl = loaderNote.getController();
                ctrl.service = this.service;
                ctrl.setOption(this.optiune, this.userName);
                ctrl.setComponents();

                windowNote.setScene(scene);
                windowNote.show();
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
        } else if (event.getSource() == materia) {
            System.out.println("materia");
            doFilteredPagination(service.getTemeLaMateria(materia.getValue().toString()));
        }
    }


    @Override
    public void update(EvenimentSchimbare evenimentSchimbare) {
        //modelTeme.setAll(getTemeList());
        doPagination();
    }

    public void setOption(String optiune, String userName) {
        this.optiune = optiune;
        this.userName = userName;
    }
}