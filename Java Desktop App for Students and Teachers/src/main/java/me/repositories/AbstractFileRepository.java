package me.repositories;

import me.entities.Entity;
import me.entities.InregistrareDTO;
import me.entities.Student;
import me.entities.Tema;
import me.services.config.ApplicationContext;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractFileRepository<TIP_ID, TIP_ENTITATE extends Entity<TIP_ID>> extends InMemoryRepository<TIP_ID, TIP_ENTITATE> {
    private String fileName;
    protected Connection con;

    String fisier = ApplicationContext.getPROPERTIES().getProperty("data.database.credentiale");
    Path path = Paths.get(fisier);
    List<String> linii;

    String url, user, password;

    {
        try {
            linii = Files.readAllLines(path);
            url = linii.get(0).split("->")[1];
            user = linii.get(1).split("->")[1];
            password = linii.get(2).split("->")[1];

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            //con = DriverManager.getConnection("jdbc:postgresql://localhost:1234/entitati", "postgres", "magraul");
            con = DriverManager.getConnection(url, user, password);
            con.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    AbstractFileRepository(String fileName) {
        this.fileName = fileName;

        //loadData();
        loadPostGres();
        saveData();
        //loadXMLData();


    }

    protected abstract void loadPostGres();

    public String getFileName() {
        return fileName;
    }

    protected void loadData() {

        Path path = Paths.get(fileName);
        try {
            List<String> lines = Files.readAllLines(path);
            lines.forEach(linie -> {
                if (linie != "") {
                    TIP_ENTITATE entitate = createEntity(linie);
                    super.save(entitate);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void loadXMLData() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        File xmlFile = new File(fileName);
        try {
            DocumentBuilder dBuilder = factory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            doc.getDocumentElement().normalize();

            //   System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getElementsByTagName(getNumeEntitate());

            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element elem = (Element) nNode;
                    TIP_ENTITATE entitate = createEntityFromXML(elem);
                    super.save(entitate);
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }


    }

    protected abstract TIP_ENTITATE createEntityFromXML(Element elem);

    protected abstract String getNumeEntitate();


    protected void saveData() {

        clearFile();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element principal = doc.createElement(getNumeEntitati());
            doc.appendChild(principal);

            super.findAll().forEach(entitate -> {
                principal.appendChild(createEntityforSaving(doc, entitate));
            });

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "6");

            DOMSource source = new DOMSource(doc);

            File file = new File(fileName);

            StreamResult rez = new StreamResult(file);

            transformer.transform(source, rez);


        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }


    }

    protected abstract String getNumeEntitati();

    protected abstract Node createEntityforSaving(Document doc, TIP_ENTITATE entitate);

    protected abstract void javaObjectToXML(TIP_ENTITATE entitate);

    protected abstract TIP_ENTITATE createEntity(String linie);

    public void clearFile() {
        FileOutputStream writer = null;
        try {
            writer = new FileOutputStream(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            writer.write(("").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sincronizeazaBazaDeDate(String operatiune) {
        try {
            Class.forName("org.postgresql.Driver");
            Statement stmt = con.createStatement();
            stmt.executeUpdate(operatiune);
            stmt.close();
            con.commit();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public TIP_ENTITATE save(TIP_ENTITATE elem) {
        TIP_ENTITATE a = super.save(elem);
        if (a == null) {
            saveData();
            sincronizeazaBazaDeDate(getSQLSave(elem));
        }
        return a;
    }

    protected abstract String getSQLSave(TIP_ENTITATE elem);

    @Override
    public TIP_ENTITATE update(TIP_ENTITATE elem) {
        TIP_ENTITATE e = super.update(elem);
        if (e == null) {
            saveData();
            sincronizeazaBazaDeDate(getSQLUpdate(elem));
        }
        return e;
    }

    protected abstract String getSQLUpdate(TIP_ENTITATE elem);

    @Override
    public TIP_ENTITATE delete(TIP_ID tip_id) throws RuntimeException {
        TIP_ENTITATE e = super.delete(tip_id);
        if (e != null) {
            saveData();
            sincronizeazaBazaDeDate(getSQLDelete(tip_id));
        }
        return e;
    }

    protected abstract String getSQLDelete(TIP_ID tip_id);

    public void golesteRepo() {
        clearFile();
        super.deleteAll();
    }

    public abstract List<Tema> getTemeInterval(int fromIndex, int toIndex);

    public abstract String getUserName(Long id);

    public abstract List<InregistrareDTO> getAllInregistrari();

    public abstract void addInregistrare(InregistrareDTO i, String parola);

    public abstract String getParola(String idStudentByName);

    public abstract Map<String, String> getInregistrariProfesori();

    public abstract Map<String, String> getInregistrariAdmini();

    public abstract String getNumeStudentByUserName(String userName);

    public abstract List<String> getMaterii();
}