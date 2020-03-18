package me;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import me.entities.Entity;
import me.entities.Student;
import me.services.Service;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class FXMLControllerPie implements Initializable, EventHandler {
    public Service service;

    @FXML
    Button exit;

    @FXML
    private BarChart<?,?> barChart;

    @FXML
    private CategoryAxis x;

    @FXML
    private NumberAxis y;


    private Stage stage;
    public void setComponents(Stage stage, ObservableList<XYChart.Data> list) {
        this.stage = stage;

       XYChart.Series series = new XYChart.Series<>();

       series.getData().addAll(list);
       barChart.getData().add(series);
    }

    @Override
    public void handle(Event event) {
      if(event.getSource() == exit)
          stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        exit.setOnAction(this);

    }
}
