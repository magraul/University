package me.repositories;

import me.entities.InregistrareDTO;
import me.entities.Student;
import me.entities.Tema;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentFileRepository extends AbstractFileRepository<Long, Student> {


    public StudentFileRepository(String fileName) {
        super(fileName);
    }

    @Override
    protected void loadPostGres() {
        try {
            Class.forName("org.postgresql.Driver");
            PreparedStatement stmt = con.prepareStatement("select * from entitati.public.studenti");
            ResultSet Rs = stmt.executeQuery();
            entities.clear();
            while (Rs.next()) {
                Student s = new Student(Rs.getLong(1), Rs.getString(2), Rs.getString(3), Rs.getString(4), Rs.getString(5), Rs.getString(6), Rs.getFloat(7));
                entities.put(s.getId(), s);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Student createEntityFromXML(Element elem) {
        return new Student(Long.parseLong(elem.getAttribute("id")), elem.getAttribute("nume"), elem.getAttribute("prenume"), elem.getAttribute("grupa"), elem.getAttribute("email"), elem.getAttribute("profesor"), Float.parseFloat(elem.getAttribute("media")));
    }

    @Override
    protected String getNumeEntitate() {
        return "student";
    }

    @Override
    protected String getNumeEntitati() {
        return "studenti";
    }

    @Override
    protected Node createEntityforSaving(Document doc, Student student) {
        Element stud = doc.createElement("student");
        stud.setAttribute("id", student.getId().toString());
        stud.setAttribute("nume", student.getNume());
        stud.setAttribute("prenume", student.getPrenume());
        stud.setAttribute("grupa", student.getGrupa());
        stud.setAttribute("email", student.getEmail());
        stud.setAttribute("profesor", student.getCadruDidacticIndrumatorLab());
        stud.setAttribute("media", String.valueOf(student.getMedia()));

        return stud;
    }

    @Override
    protected void javaObjectToXML(Student student) {

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("student");
            doc.appendChild(rootElement);

            Element entity = doc.createElement("student");
            rootElement.appendChild(entity);

            Attr attr = doc.createAttribute("id");
            attr.setValue(student.getId().toString());
            entity.setAttributeNode(attr);

            Attr attr1 = doc.createAttribute("Nume");
            attr1.setValue(student.getNume());
            entity.setAttributeNode(attr1);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(String.valueOf(Paths.get("data/studenti.xml"))));

        } catch (ParserConfigurationException | TransformerConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Student createEntity(String linie) {
        String[] elems = linie.split(" ");
        return new Student(Long.parseLong(elems[0]), elems[1], elems[2], elems[3], elems[4], elems[5], Float.parseFloat(elems[6]));
    }

    @Override
    protected String getSQLSave(Student elem) {
        return "insert into entitati.public.studenti(id, nume, prenume, grupa, email, cadrudidacticindrumatorlab, media)  values  (" + elem.getId() + ", '" + elem.getNume() + "' ,'" + elem.getPrenume() + "', '" + elem.getGrupa() + "', '" + elem.getEmail() + "', '" + elem.getCadruDidacticIndrumatorLab() + "', " + elem.getMedia() + ");";
    }

    @Override
    protected String getSQLUpdate(Student elem) {
        return "delete from entitati.public.studenti where id = " + elem.getId() + ";" +
                "insert into entitati.public.studenti(id, nume, prenume, grupa, email, cadrudidacticindrumatorlab, media)  values  (" + elem.getId() + ", '" + elem.getNume() + "' ,'" + elem.getPrenume() + "', '" + elem.getGrupa() + "', '" + elem.getEmail() + "', '" + elem.getCadruDidacticIndrumatorLab() + "', " + elem.getMedia() + ");";
    }

    @Override
    protected String getSQLDelete(Long aLong) {
        return "delete from entitati.public.studenti where id = " + aLong + "; ";
    }

    @Override
    public List<Tema> getTemeInterval(int fromIndex, int toIndex) {
        return null;
    }

    @Override
    public String getUserName(Long id) {
        try {
            Class.forName("org.postgresql.Driver");
            PreparedStatement stmt = con.prepareStatement("select username from entitati.public.inregistrare where id_student = " + id);
            ResultSet Rs = stmt.executeQuery();

            if(Rs.next())
               return Rs.getString(1);
        } catch (ClassNotFoundException | SQLException e) {
            // e.printStackTrace();
            return "";
        }
        return "";
    }

    @Override
    public List<InregistrareDTO> getAllInregistrari() {
        List<InregistrareDTO> rez = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
            PreparedStatement stmt = con.prepareStatement("select * from entitati.public.studenti");
            ResultSet Rs = stmt.executeQuery();
            while (Rs.next()) {
                Long id = Rs.getLong(1);

                PreparedStatement statement = con.prepareStatement("select username from entitati.public.inregistrare where id_student = " + id);
                ResultSet resultSet = statement.executeQuery();
                String user = "";
                if (resultSet.next())
                    user = resultSet.getString(1);

                InregistrareDTO i = new InregistrareDTO(Rs.getString(2) + " " + Rs.getString(3), user);
                rez.add(i);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return rez;
    }

    @Override
    public void addInregistrare(InregistrareDTO i, String parola) {
        try {
            Class.forName("org.postgresql.Driver");
            String sql = "do $$ begin if exists (select * from entitati.public.inregistrare where id_student = " + getIdStudentByName(i.getStudent()) + ") " + "then update entitati.public.inregistrare set username = '" + i.getUserName() + "', parola = '" + parola + "' where id_student = " + getIdStudentByName(i.getStudent()) + ";else insert into inregistrare(id_student, nume_student, username, parola) VALUES (" + getIdStudentByName(i.getStudent()) + ", '" + i.getStudent() + "', '" + i.getUserName() + "', '" + parola +"');end if; end; $$;";
            Statement stmt = con.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            con.commit();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getParola(String idStudent) {
        try {
            Class.forName("org.postgresql.Driver");
            PreparedStatement stmt = con.prepareStatement("select parola from entitati.public.inregistrare where id_student = " + Integer.parseInt(idStudent));
            ResultSet Rs = stmt.executeQuery();

            if(Rs.next())
                return Rs.getString(1);
        } catch (ClassNotFoundException | SQLException e) {
            // e.printStackTrace();
            return "";
        }
        return "";
    }

    @Override
    public Map<String, String> getInregistrariProfesori() {
        Map<String, String> rez = new HashMap<>();
        try {
            Class.forName("org.postgresql.Driver");
            PreparedStatement stmt = con.prepareStatement("select username, parola from entitati.public.inregistrariprofesori");
            ResultSet Rs = stmt.executeQuery();

            while (Rs.next()) {
                rez.put(Rs.getString(1), Rs.getString(2));
            }
        } catch (ClassNotFoundException | SQLException e) {
             e.printStackTrace();
        }
        return rez;
    }

    @Override
    public Map<String, String> getInregistrariAdmini() {
        Map<String, String> rez = new HashMap<>();
        try {
            Class.forName("org.postgresql.Driver");
            PreparedStatement stmt = con.prepareStatement("select username, parola from entitati.public.inregistrariadmini");
            ResultSet Rs = stmt.executeQuery();

            while (Rs.next()) {
                rez.put(Rs.getString(1), Rs.getString(2));
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return rez;
    }

    @Override
    public String getNumeStudentByUserName(String userName) {
        try {
            Class.forName("org.postgresql.Driver");
            PreparedStatement stmt = con.prepareStatement("select nume_student from entitati.public.inregistrare where username = '" + userName + "'");
            ResultSet Rs = stmt.executeQuery();

            if(Rs.next())
                return Rs.getString(1);
        } catch (ClassNotFoundException | SQLException e) {
            // e.printStackTrace();
            return "";
        }
        return "";
    }

    @Override
    public List<String> getMaterii() {
        return null;
    }

    public String getIdStudentByName(String nume) {
        String[] elems = nume.split(" ");
        for(Student s : getAll()) {
            if(s.getNume().equals(elems[0]) && s.getPrenume().equals(elems[1]))
                return s.getId().toString();
        }
        return null;
    }


}