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
import me.boxes.AlertBox;
import me.entities.AngajatDTO;
import me.entities.CazCaritabilDTO;
import me.events.EvenimentSchimbare;
import me.events.TipEveniment;
import me.observer.Observer;
import me.services.Service;
import org.controlsfx.control.textfield.TextFields;
import sun.security.validator.ValidatorException;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class FXMLControllerAngajat implements Initializable, EventHandler, Observer<EvenimentSchimbare> {
   public Service service;

    ObservableList<CazCaritabilDTO> modelTabel = FXCollections.observableArrayList();

    @FXML
    Button doneaza;

    @FXML
    MenuBar menuBar;

    @FXML
    MenuItem logOut;

    @FXML
    TextField numeDonator, adresa, nrTel, sumaDonata, search;

    @FXML
    TableView tabel;

    @FXML
    TableColumn descriere, sumaAdunata;

    @FXML
    ListView lista;

    @Override
    public void handle(Event event) {
      if (event.getSource() == doneaza) {
            CazCaritabilDTO caz = (CazCaritabilDTO)tabel.getSelectionModel().getSelectedItem();
            if(caz == null) {
                AlertBox.display("eroare", "nu ati selectat un caz caritabil!");
                return;
            }
            Integer cazId = service.getCazId(caz);

            try {
                service.donatie(cazId, numeDonator.getText(), adresa.getText(),nrTel.getText(),Float.parseFloat(sumaDonata.getText()));
            } catch (ValidatorException e) {
                AlertBox.display("eroare", e.getMessage());
            }
        }

      if (event.getSource() == logOut) {
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
      }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        doneaza.setOnAction(this);

        descriere.setCellValueFactory(new PropertyValueFactory<AngajatDTO, String>("descriere"));
        sumaAdunata.setCellValueFactory(new PropertyValueFactory<AngajatDTO, String>("sumaAdunata"));

        search.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                handleFilter();
            }
        });

        lista.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (!lista.getSelectionModel().isEmpty()) {
                    String[] cuvinte = lista.getSelectionModel().getSelectedItems().get(0).toString().split(" {2}");
                    numeDonator.setText(cuvinte[0]);
                    adresa.setText(cuvinte[1]);
                    nrTel.setText(cuvinte[2]);
                }
            }
        });

        logOut.setOnAction(this);
    }

    private void handleFilter() {
        lista.getItems().setAll(service.getRanduriDonatori().stream().filter(x->x.contains(search.getText())).collect(Collectors.toList()));
    }

    public void setComponente() {
        List<CazCaritabilDTO> d = service.getAllCazuriDTO();
        modelTabel.setAll(service.getAllCazuriDTO());
        tabel.setItems(modelTabel);
        service.addObserver(this);
        lista.getItems().setAll(service.getRanduriDonatori());
    }

    @Override
    public void update(EvenimentSchimbare evenimentSchimbare) {
        if (evenimentSchimbare.getTip().compareTo(TipEveniment.DONATIE) == 0) {
            modelTabel.setAll(service.getAllCazuriDTO());
            numeDonator.setText("");
            adresa.setText("");
            nrTel.setText("");
            sumaDonata.setText("");
            search.setText("");
        }
    }
}
