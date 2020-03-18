package me.repositories;

import me.entities.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class NoteFileRepository extends AbstractFileRepository<IdObject, Nota> {
    public NoteFileRepository(String fileName) {
        super(fileName);
    }

    @Override
    protected void loadPostGres() {
        try {
            Class.forName("org.postgresql.Driver");
            PreparedStatement stmt = con.prepareStatement("select * from entitati.public.note");

            String sqlStr = "select * from entitati.public.studenti where id = ";
            String sqlStrTeme = "select * from entitati.public.teme where id = ";


            ResultSet Rs = stmt.executeQuery();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            while (Rs.next()){
                String idT = String.valueOf(Rs.getLong(2));
                PreparedStatement getTemaData = con.prepareStatement(sqlStrTeme + idT);
                ResultSet RezTema = getTemaData.executeQuery();


                String idS = String.valueOf(Rs.getLong(1));
                PreparedStatement getStudentData = con.prepareStatement(sqlStr + idS);
                ResultSet RezStud = getStudentData.executeQuery();

                if(RezStud.next() && RezTema.next()) {
                    Nota n = new Nota(LocalDate.parse(Rs.getString(3), formatter), Rs.getString(4), new Student(RezStud.getLong(1), RezStud.getString(2), RezStud.getString(3), RezStud.getString(4), RezStud.getString(5), RezStud.getString(6), RezStud.getFloat(7)), new Tema(RezTema.getLong(1), RezTema.getString(2), RezTema.getInt(3), RezTema.getInt(4)), Rs.getInt(5), Rs.getString(6));
                    entities.put(n.getId(), n);
                }

            }
            //con.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Nota createEntityFromXML(Element elem) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return new Nota(LocalDate.parse(elem.getAttribute("dataPredare"),formatter),elem.getAttribute("profesor"),new Student(Long.parseLong(elem.getAttribute("idStudent")),elem.getAttribute("numeStudent"),elem.getAttribute("prenumeStudent"),elem.getAttribute("grupa"),elem.getAttribute("email"),elem.getAttribute("cadruDidactic"),Float.parseFloat(elem.getAttribute("media"))),new Tema(Long.parseLong(elem.getAttribute("idTema")),elem.getAttribute("descriereTema"),Integer.parseInt(elem.getAttribute("startWeek")),Integer.parseInt(elem.getAttribute("deadlineWeek"))),Integer.parseInt(elem.getAttribute("valoareNota")),elem.getAttribute("feedback"));
    }

    @Override
    protected String getNumeEntitate() {
        return "nota";
    }

    @Override
    protected String getNumeEntitati() {
        return "note";
    }

    @Override
    protected Node createEntityforSaving(Document doc, Nota nota) {
        Element notaa = doc.createElement("nota");
        notaa.setAttribute("idStudent",nota.getStudent().getId().toString());
        notaa.setAttribute("numeStudent",nota.getStudent().getNume());
        notaa.setAttribute("prenumeStudent",nota.getStudent().getPrenume());
        notaa.setAttribute("grupa",nota.getStudent().getGrupa());
        notaa.setAttribute("email",nota.getStudent().getEmail());
        notaa.setAttribute("cadruDidactic",nota.getStudent().getCadruDidacticIndrumatorLab());
        notaa.setAttribute("media",String.valueOf(nota.getStudent().getMedia()));

        notaa.setAttribute("idTema",nota.getTema().getId().toString());
        notaa.setAttribute("descriereTema",nota.getTema().getDescriere());
        notaa.setAttribute("startWeek",nota.getTema().getStartWeek().toString());
        notaa.setAttribute("deadlineWeek",nota.getTema().getDeadlineWeek().toString());
        notaa.setAttribute("valoareNota",nota.getValoare().toString());


        notaa.setAttribute("dataPredare",nota.getData().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        notaa.setAttribute("profesor",nota.getProfesor());
        notaa.setAttribute("feedback",nota.getFeedback());
        return notaa;
    }

    @Override
    protected void javaObjectToXML(Nota nota) {

    }

    @Override
    protected Nota createEntity(String linie) {
        return null;
    }

    @Override
    protected String getSQLSave(Nota elem) {
        return "insert into entitati.public.note(idStudent, idTema, data, profesor, valoare, feedback) VALUES (" + elem.getStudent().getId()+ ", " + elem.getTema().getId()+", '"+ elem.getData().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))+"' ,'"+elem.getProfesor()+"', "+elem.getValoare()+ ", '" +elem.getFeedback()+"');";
    }

    @Override
    protected String getSQLUpdate(Nota elem) {
        return "delete from entitati.public.note where idStudent = " + elem.getStudent().getId() + " and idTema = " + elem.getTema().getId() + ";" +
                "insert into entitati.public.note(idStudent, idTema, data, profesor, valoare, feedback) VALUES (" + elem.getStudent().getId()+ ", " + elem.getTema().getId()+", '"+ elem.getData().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))+"' ,'"+elem.getProfesor()+"', "+elem.getValoare()+", '" +elem.getFeedback()+"');";
    }

    @Override
    protected String getSQLDelete(IdObject idObject) {
        return "delete from entitati.public.note where idStudent = " + idObject.getId1() + "and idTema = " + idObject.getId2() + ";";
    }

    @Override
    public List<Tema> getTemeInterval(int fromIndex, int toIndex) {
        return null;
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
        return null;
    }
}
