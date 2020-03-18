package me;

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
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import me.boxes.AlertBox;
import me.entities.Nota;
import me.entities.Student;
import me.entities.Tema;
import me.services.Service;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class FXMLControllerRapoarte implements Initializable, EventHandler {
    public Service service;

    @FXML
    Button butonTeme, butonNote, filtrare1, filtrare2, filtrare3, filtrare4, savePDF1, savePDF2, savePDF3, savePDF4, pieChart1, pieChart2, pieChart3, pieChart4;

    @FXML
    TextArea rezultat;
    private String userName;

    public void setComponents() {
    }
    @FXML
    MenuItem logOut;

    @FXML
    private MenuBar menuBar;

    @Override
    public void handle(Event event) {
        if (event.getSource() == butonTeme) {
            try {
                FXMLLoader loaderTeme = new FXMLLoader();
                loaderTeme.setLocation(getClass().getResource("/fxml/temeView.fxml"));

                Parent viewTeme = loaderTeme.load();

                Scene scene = new Scene(viewTeme);
                Stage windowTeme = (Stage) ((Node) event.getSource()).getScene().getWindow();

                FXMLControllerTeme ctrl = loaderTeme.getController();
                ctrl.service = this.service;
                ctrl.setOption("profesor", this.userName);
                ctrl.setComponente();

                windowTeme.setScene(scene);
                windowTeme.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
                ctrl.setOption("profesor", this.userName);
                ctrl.setComponents();

                windowNote.setScene(scene);
                windowNote.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (event.getSource() == filtrare1) {
            doFiltrare1();
        } else if (event.getSource() == filtrare2)
            doFiltrare2();
        else if (event.getSource() == filtrare3)
            doFiltrare3();
        else if (event.getSource() == filtrare4)
            doFiltrare4();
        else if (event.getSource() == savePDF1) {
            savePDF_PROC1(event);
        } else if (event.getSource() == savePDF2) {
            savePDF_PROC2(event);
        } else if (event.getSource() == savePDF3)
            savePDF_PROC3(event);
        else if (event.getSource() == savePDF4)
            savePDF_PROC4(event);
        else if (event.getSource() == pieChart1) {
            List<Nota> note = new ArrayList<>(service.getMapNote().values());
            List<Student> students = new ArrayList<>(service.getMapStudenti().values());
            Map<Student, Double> grouped = service.getFiltrare1();

            ObservableList<XYChart.Data> list = FXCollections.observableArrayList();
            for (Map.Entry<Student, Double> p : grouped.entrySet()) {
                list.add(new XYChart.Data(p.getKey().getNume() + p.getKey().getPrenume(), p.getValue()));
            }
            try {
                newPopUpChart(list);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (event.getSource() == pieChart2) {
            List<Nota> note = new ArrayList<>(service.getMapNote().values());
            //Optional<Map.Entry<Tema, Double>> minEntr = service.getFiltrare2();

            ObservableList<XYChart.Data> list = FXCollections.observableArrayList();

            Map<Tema, Double> rez = service.getFiltrare2();
            for (Map.Entry<Tema, Double> p : rez.entrySet()) {
                list.add(new XYChart.Data(p.getKey().getDescriere(), p.getValue()));
            }

            try {
                newPopUpChart(list);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (event.getSource() == pieChart3) {
            List<Nota> note = new ArrayList<>(service.getMapNote().values());
            List<Student> students = new ArrayList<>(service.getMapStudenti().values());

            Map<Student, Double> grouped = service.getFiltrare3();
            ObservableList<XYChart.Data> list = FXCollections.observableArrayList();

            for (Map.Entry<Student, Double> p : grouped.entrySet()) {
                    list.add(new XYChart.Data(p.getKey().getNume() + p.getKey().getPrenume(), p.getValue()));
            }

            try {
                newPopUpChart(list);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (event.getSource() == pieChart4) {
            List<Nota> note = new ArrayList<>(service.getMapNote().values());

            Map<Student, Double> predateLaTimp = service.getFiltrare4();
            ObservableList<XYChart.Data> list = FXCollections.observableArrayList();

            for (Map.Entry<Student,Double> e : predateLaTimp.entrySet()) {
                list.add(new XYChart.Data(e.getKey().getNume() + " " + e.getKey().getPrenume(), e.getValue()));
            }

            try {
                newPopUpChart(list);
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
        }
    }

    private void newPopUpChart(ObservableList<XYChart.Data> grouped) throws IOException {
        FXMLLoader loaderPie = new FXMLLoader();
        loaderPie.setLocation(getClass().getResource("/fxml/viewPopUpPieChart.fxml"));

        Parent root = loaderPie.load();
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);

        Scene scene = new Scene(root);
        stage.setScene(scene);

        FXMLControllerPie ctrl = loaderPie.getController();
        ctrl.service = this.service;
        ctrl.setComponents(stage, grouped);
        stage.show();

    }

    private void savePDF_PROC4(Event event) {
        List<Nota> note = new ArrayList<>(service.getMapNote().values());
        Set<Student> predateLaTimp = note.stream()
                .filter(nota -> service.anUniversitar.getNrSaptamanaDeStudiuDinSemestruAlDatei(nota.getData().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))) == nota.getTema().getDeadlineWeek())
                .map(nota -> nota.getStudent())
                .collect(Collectors.toSet());

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);

        Stage windowNote = (Stage) ((Node) event.getSource()).getScene().getWindow();

        File selected = fileChooser.showSaveDialog(windowNote);
        if (selected != null) {
            try {
                PDDocument document = new PDDocument();
                PDPage page = new PDPage();
                document.addPage(page);

                PDPageContentStream contentStream = new PDPageContentStream(document, page);

                contentStream.setFont(PDType1Font.COURIER, 12);

                contentStream.beginText();
                contentStream.setLeading(14.5f);

                contentStream.newLineAtOffset(25, 700);
                contentStream.showText("Studentii care predat toate temele la timp:");
                contentStream.newLine();

                for (Student s : predateLaTimp) {
                    contentStream.showText(s.getNume() + " " + s.getPrenume());
                    contentStream.newLine();
                }

                contentStream.newLine();
                contentStream.endText();
                contentStream.close();

                document.save(selected);
                document.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            AlertBox.display("Salvare", "Raportul a fost salvat cu succes!");


        }
    }

    private void savePDF_PROC3(Event event) {
        String forWrite = "";

        List<Nota> note = new ArrayList<>(service.getMapNote().values());
        List<Student> students = new ArrayList<>(service.getMapStudenti().values());
        Map<Student, Double> grouped = note.stream()
                .collect(Collectors.groupingBy(x -> x.getStudent()))
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        lista -> (double) getNumarator(lista.getValue()) / getNumitor(lista.getValue())
                ));

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);

        Stage windowNote = (Stage) ((Node) event.getSource()).getScene().getWindow();

        File selected = fileChooser.showSaveDialog(windowNote);
        if (selected != null) {
            try {
                PDDocument document = new PDDocument();
                PDPage page = new PDPage();
                document.addPage(page);

                PDPageContentStream contentStream = new PDPageContentStream(document, page);

                contentStream.setFont(PDType1Font.COURIER, 12);

                contentStream.beginText();
                contentStream.setLeading(14.5f);

                contentStream.newLineAtOffset(25, 700);
                contentStream.showText("Studentii care pot intra in examen:");
                contentStream.newLine();


                for (Map.Entry<Student, Double> p : grouped.entrySet()) {
                    if (p.getValue() >= 4.0) {
                        contentStream.showText(p.getKey().getNume() + " " + p.getKey().getPrenume() + "            Media:  " + p.getValue());
                        contentStream.newLine();
                    }
                }


                contentStream.newLine();
                contentStream.endText();
                contentStream.close();

                document.save(selected);
                document.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            AlertBox.display("Salvare", "Raportul a fost salvat cu succes!");
        }
    }

    private void savePDF_PROC2(Event event) {
        String forWrite = "";

        List<Nota> note = new ArrayList<>(service.getMapNote().values());
        Optional<Map.Entry<Tema, Double>> minEntry = note.stream().collect(Collectors.groupingBy(x -> x.getTema()))
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> media(e.getValue())
                )).entrySet().stream()
                .min(Comparator.comparingDouble(Map.Entry::getValue));

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);

        Stage windowNote = (Stage) ((Node) event.getSource()).getScene().getWindow();

        File selected = fileChooser.showSaveDialog(windowNote);
        if (selected != null) {
            try {
                PDDocument document = new PDDocument();
                PDPage page = new PDPage();
                document.addPage(page);

                PDPageContentStream contentStream = new PDPageContentStream(document, page);

                contentStream.setFont(PDType1Font.COURIER, 12);

                contentStream.beginText();
                contentStream.setLeading(14.5f);

                contentStream.newLineAtOffset(25, 700);
                contentStream.showText("Cea mai grea tema este  :");

                contentStream.newLine();
                contentStream.showText(minEntry.get().getKey().getDescriere());

                contentStream.newLine();
                contentStream.showText("Cu media notelor " + minEntry.get().getValue());

                contentStream.newLine();

                contentStream.endText();
                contentStream.close();

                document.save(selected);
                document.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            AlertBox.display("Salvare", "Raportul a fost salvat cu succes!");
        }
    }

    private void savePDF_PROC1(Event event) {

        String forWrite = "";

        List<Nota> note = new ArrayList<>(service.getMapNote().values());
        List<Student> students = new ArrayList<>(service.getMapStudenti().values());
        Map<Student, Double> grouped = note.stream()
                .collect(Collectors.groupingBy(x -> x.getStudent()))
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        lista -> (double) getNumarator(lista.getValue()) / getNumitor(lista.getValue())
                ));

        String forWriteAUX = "";
        for (Map.Entry<Student, Double> p : grouped.entrySet()) {
            forWriteAUX += "\n" + p.getKey().getNume() + " " + p.getKey().getPrenume() + " --> " + p.getValue() + "\n";
        }

        String rez = "Media ponderata a notelor fiecarui student:\n" + forWrite;

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);

        Stage windowNote = (Stage) ((Node) event.getSource()).getScene().getWindow();

        File selected = fileChooser.showSaveDialog(windowNote);
        if (selected != null) {
            try {
                PDDocument document = new PDDocument();
                PDPage page = new PDPage();
                document.addPage(page);

                PDPageContentStream contentStream = new PDPageContentStream(document, page);

                contentStream.setFont(PDType1Font.COURIER, 12);

                contentStream.beginText();
                contentStream.setLeading(14.5f);

                contentStream.newLineAtOffset(25, 700);
                contentStream.showText("Media ponderata a notelor fiecarui student:");

                contentStream.newLine();

                for (Map.Entry<Student, Double> p : grouped.entrySet()) {
                    forWrite = p.getKey().getNume() + " " + p.getKey().getPrenume() + " --> " + p.getValue();
                    contentStream.showText(forWrite);
                    contentStream.newLine();
                }

                for (Student s : students) {
                    if (!forWriteAUX.contains(s.getNume())) {
                        forWrite = s.getNume() + " " + s.getPrenume() + " --> " + 0;
                        contentStream.showText(forWrite);
                        contentStream.newLine();
                    }

                }
                contentStream.endText();
                contentStream.close();

                document.save(selected);
                document.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            AlertBox.display("Salvare", "Raportul a fost salvat cu succes!");
        }
    }

    private void doFiltrare4() {
        List<Nota> note = new ArrayList<>(service.getMapNote().values());
        String forWrite = "";

        Map<Student,Double> predateLaTimp = service.getFiltrare4();
        for (Map.Entry<Student,Double> e: predateLaTimp.entrySet())
            forWrite += e.getKey().getNume() + " " + e.getKey().getPrenume() + " " + " --> " + e.getValue() + "\n";
        rezultat.setText("Studentii care predat toate temele la timp:\n" + forWrite);
    }

    private void doFiltrare3() {
        String forWrite = "";

        List<Nota> note = new ArrayList<>(service.getMapNote().values());
        Map<Student, Double> grouped = service.getFiltrare3();

        for (Map.Entry<Student, Double> p : grouped.entrySet()) {
            if (p.getValue() >= 4.0)
                forWrite += "\n" + p.getKey().getNume() + " " + p.getKey().getPrenume() + "            Media:  " + p.getValue() + "\n";
        }
        rezultat.setText("Studentii care pot intra in examen:\n" + forWrite);
    }

    private void doFiltrare2() {
        String s = "Temele cu mediile aferente\n";

        List<Nota> note = new ArrayList<>(service.getMapNote().values());

        Map<Tema, Double> minEntry = service.getFiltrare2();



        for(Map.Entry<Tema, Double> e : minEntry.entrySet()) {
                s += e.getKey().getDescriere() + " --> " + e.getValue() + "\n";
        }

        rezultat.setText(s);
    }

    private void doFiltrare1() {
        String forWrite = "";

        List<Nota> note = new ArrayList<>(service.getMapNote().values());
        List<Student> students = new ArrayList<>(service.getMapStudenti().values());
        Map<Student, Double> grouped = service.getFiltrare1();

        for (Map.Entry<Student, Double> p : grouped.entrySet()) {
            forWrite += "\n" + p.getKey().getNume() + " " + p.getKey().getPrenume() + " --> " + p.getValue() + "\n";
        }

        for (Student s : students) {
            if (!forWrite.contains(s.getNume()))
                forWrite = forWrite + "\n" + s.getNume() + " " + s.getPrenume() + " --> " + 0;
        }

        rezultat.setText("Media ponderata a notelor fiecarui student:\n" + forWrite);
    }

    private double media(List<Nota> list) {
        Integer suma = 0;
        for (Nota n : list)
            suma += n.getValoare();
        return (double) suma / list.size();
    }

    private Integer getNumitor(List<Nota> l) {
        Integer rez = 0;
        for (Nota nota : l)
            rez += (nota.getTema().getDeadlineWeek() - nota.getTema().getStartWeek());

        return rez;
    }

    private Integer getNumarator(List<Nota> l) {
        Integer rez = 0;
        for (Nota nota : l)
            rez += nota.getValoare() * (nota.getTema().getDeadlineWeek() - nota.getTema().getStartWeek());
        return rez;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        butonTeme.setOnAction(this);
        butonNote.setOnAction(this);
        filtrare3.setOnAction(this);
        filtrare4.setOnAction(this);
        filtrare2.setOnAction(this);
        filtrare1.setOnAction(this);
        savePDF1.setOnAction(this);
        savePDF2.setOnAction(this);
        savePDF3.setOnAction(this);
        savePDF4.setOnAction(this);
        pieChart1.setOnAction(this);
        pieChart2.setOnAction(this);
        pieChart3.setOnAction(this);
        pieChart4.setOnAction(this);
        logOut.setOnAction(this);
    }

    public void setOption(String profesor, String userName) {
        this.userName = userName;
    }
}
