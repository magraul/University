package me.repositories;

import me.entities.InregistrareDTO;
import me.entities.Student;
import me.entities.Tema;
import me.services.config.ApplicationContext;
import me.structures.AnUniversitar;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TemeFileRepository extends AbstractFileRepository<Long, Tema> {
    public AnUniversitar anUniversitar = new AnUniversitar(ApplicationContext.getPROPERTIES().getProperty("data.structure.anUniversitar"));
    public TemeFileRepository(String fileName) {
        super(fileName);
    }

    @Override
    protected void loadPostGres() {
        try {
            Class.forName("org.postgresql.Driver");
            PreparedStatement stmt = con.prepareStatement("select * from entitati.public.teme");
            ResultSet Rs = stmt.executeQuery();
            entities.clear();
            while (Rs.next()) {
                Tema t = new Tema(Rs.getLong(1), Rs.getString(2), Rs.getInt(3), Rs.getInt(4), Rs.getString(5));
                entities.put(t.getId(),t);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Tema createEntityFromXML(Element elem) {
        return new Tema(Long.parseLong(elem.getAttribute("id")), elem.getAttribute("descriere"), Integer.parseInt(elem.getAttribute("startWeek")), Integer.parseInt(elem.getAttribute("deadlineWeek")));
    }

    @Override
    protected String getNumeEntitate() {
        return "tema";
    }

    @Override
    protected String getNumeEntitati() {
        return "teme";
    }

    @Override
    protected Node createEntityforSaving(Document doc, Tema tema) {
        Element tema1 = doc.createElement("tema");
        tema1.setAttribute("id", tema.getId().toString());
        tema1.setAttribute("descriere", tema.getDescriere());
        tema1.setAttribute("startWeek", tema.getStartWeek().toString());
        tema1.setAttribute("deadlineWeek", tema.getDeadlineWeek().toString());
        return tema1;
    }

    @Override
    protected void javaObjectToXML(Tema tema) {

    }



    @Override
    protected Tema createEntity(String linie) {
        String[] elems = linie.split(" ");
        return new Tema(Long.parseLong(elems[0]), elems[1], Integer.parseInt(elems[2]), Integer.parseInt(elems[3]));
    }

    @Override
    protected String getSQLSave(Tema elem) {
        return "insert into entitati.public.teme(id, descriere, startWeek, deadlineWeek)  values (" + elem.getId() + ", '" + elem.getDescriere() + "'," + elem.getStartWeek() + ", " + elem.getDeadlineWeek()+");";
    }

    @Override
    protected String getSQLUpdate(Tema elem) {
        return "delete from entitati.public.teme where id = "+elem.getId()+ ";"+
                "insert into entitati.public.teme(id, descriere, startWeek, deadlineWeek)  values (" + elem.getId() + ", '" + elem.getDescriere() + "'," + elem.getStartWeek() + ", " + elem.getDeadlineWeek()+");";
    }

    @Override
    protected String getSQLDelete(Long aLong) {
        return "delete from entitati.public.teme where id = " + aLong + ";";
    }

    @Override
    public List<Tema> getTemeInterval(int fromIndex, int toIndex) {
        List<Tema> rez = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
            String sql = "select * from entitati.public.teme order by id limit " + (toIndex - fromIndex) + " offset " + fromIndex;
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet Rs= stmt.executeQuery();
            while (Rs.next()) {
                Tema t = new Tema(Rs.getLong(1), Rs.getString(2), Rs.getInt(3), Rs.getInt(4));
                rez.add(t);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return rez;
    }

    @Override
    public String getUserName(Long id) {
        return null;
    }

    @Override
    public List<InregistrareDTO> getAllInregistrari() {
        return null;
    }

    @Override
    public void addInregistrare(InregistrareDTO i, String parola) {

    }

    @Override
    public String getParola(String idStudentByName) {
        return null;
    }

    @Override
    public Map<String, String> getInregistrariProfesori() {
        return null;
    }

    @Override
    public Map<String, String> getInregistrariAdmini() {
        return null;
    }

    @Override
    public String getNumeStudentByUserName(String userName) {
        return null;
    }

    @Override
    public List<String> getMaterii() {
        List<String> rez = new ArrayList<>();
        Path path = Paths.get("data/materii.txt");
        try {
            List<String> lines = Files.readAllLines(path);

            for(String linie : lines) {
                if (linie != "") {
                    String[] elems = linie.split(" ");
                    if (anUniversitar.suntemInSem1() && elems[1].equals("sem1"))
                        rez.add(elems[0]);
                    else if (!anUniversitar.suntemInSem1() && elems[1].equals("sem2"))
                        rez.add(elems[0]);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }



        return rez;
    }
}
