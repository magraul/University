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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import me.boxes.AlertBox;
import me.repositories.AngajatiDBRepository;
import me.repositories.CazuriDBRepository;
import me.repositories.DonatiiDBRepository;
import me.repositories.DonatoriDBRepository;
import me.services.Service;
import me.validators.ValidatorCazuri;
import me.validators.ValidatorDonatii;
import me.validators.ValidatorDonatori;
import org.mindrot.jbcrypt.BCrypt;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

public class FXMLControllerLogare implements Initializable, EventHandler {
    private Properties serverProps = null;

    {
        this.serverProps = new Properties();
        try {
            serverProps.load(new FileReader("bd.config"));
            serverProps.list(System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Service service = new Service(new AngajatiDBRepository(serverProps), new CazuriDBRepository(serverProps), new DonatiiDBRepository(serverProps), new DonatoriDBRepository(serverProps), new ValidatorCazuri(), new ValidatorDonatii(), new ValidatorDonatori());
    Map<String, String> angajatiMap = new HashMap<>();

    @FXML
    Button logIn;

    @FXML
    TextField username, parola;


    @Override
    public void handle(Event event) {
        if (event.getSource() == logIn) {
            if (username.getText().length() == 0 || parola.getText().length() ==0)
            {
                AlertBox.display("eroare", "toate fieldurile sunt obligatorii");
                return;
            }
            if (username.getText().equals("admin")) {
                //view admin
                try {
                    FXMLLoader loaderAdmin = new FXMLLoader();
                    loaderAdmin.setLocation(getClass().getResource("/fxml/adminView.fxml"));

                    Parent viewLogare = loaderAdmin.load();

                    Stage windowNote = (Stage) ((Node) logIn).getScene().getWindow();
                    windowNote.hide();

                    Scene scene = new Scene(viewLogare);
                    Stage window = new Stage();
                    window.setTitle("Administrator");
                    window.setScene(scene);

                    FXMLControllerAdmin ctrl = loaderAdmin.getController();
                    ctrl.service = this.service;
                    ctrl.setComponente();
                    window.show();
                }catch (Exception e) {
                    AlertBox.display("eroare", "eroare");
                }

            } else {
                //logare ca angajat
                if (angajatiMap.get(username.getText()) != null && BCrypt.checkpw(parola.getText(), angajatiMap.get(username.getText()))) {
                    //logare cu succes acgajat
                    try {
                        FXMLLoader loaderAngajat = new FXMLLoader();
                        loaderAngajat.setLocation(getClass().getResource("/fxml/noteView.fxml"));

                        Parent viewAngajat = loaderAngajat.load();

                        Stage windowLogare = (Stage) ((Node) logIn).getScene().getWindow();
                        windowLogare.hide();

                        Scene scene = new Scene(viewAngajat);
                        Stage window = new Stage();
                        window.setTitle("Angajat");
                        window.setScene(scene);

                        FXMLControllerAngajat ctrl = loaderAngajat.getController();
                        ctrl.service = this.service;
                        ctrl.setComponente();
                        window.show();

                }catch (Exception e) {
                    AlertBox.display("eroare", "eroare");
                }

                } else {
                    AlertBox.display("Eroare", "User Name sau parola incorecte!");
                }
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logIn.setOnAction(this);
    }

    private void incarcaDictionare() {
        angajatiMap = service.getDataAutentificareAngajati();
    }

    public void setComponente() {
        incarcaDictionare();
    }
}
