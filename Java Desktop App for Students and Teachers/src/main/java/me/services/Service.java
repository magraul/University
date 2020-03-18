package me.services;

import me.entities.*;
import me.events.EvenimentSchimbare;
import me.events.TipEveniment;
import me.observer.Observable;
import me.observer.Observer;
import me.repositories.NoteFileRepository;
import me.repositories.StudentFileRepository;
import me.repositories.TemeFileRepository;
import me.services.config.ApplicationContext;
import me.structures.AnUniversitar;
import me.validators.Validator;
//import org.json.JSONObject;

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.mindrot.jbcrypt.BCrypt;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class Service implements Observable<EvenimentSchimbare> {

    public AnUniversitar anUniversitar = new AnUniversitar(ApplicationContext.getPROPERTIES().getProperty("data.structure.anUniversitar"));

    private StudentFileRepository repoStudenti;
    private Validator<Student> validatorStudenti;

    private TemeFileRepository repoTeme;
    private Validator<Tema> validatorTema;

    //private InMemoryRepository<IdObject, Nota> repoNote;
    private NoteFileRepository repoNote;
    private Validator<Nota> validatorNota;

    protected Connection con;

    public Service(StudentFileRepository repoStudenti, Validator<Student> validatorStudenti, TemeFileRepository repoTeme, Validator<Tema> validatorTema, NoteFileRepository repoNote, Validator<Nota> validatorNota) {
        this.repoStudenti = repoStudenti;
        this.validatorStudenti = validatorStudenti;
        this.repoTeme = repoTeme;
        this.validatorTema = validatorTema;
        this.repoNote = repoNote;
        this.validatorNota = validatorNota;

//
//        //lista cu studenti
//        List<Student> students = new ArrayList<>();
//        try {
//            Class.forName("org.postgresql.Driver");
//            con = DriverManager.getConnection("jdbc:postgresql://localhost:1234/entitati", "postgres", "magraul");
//            PreparedStatement stmt = con.prepareStatement("select * from entitati.public.studenti");
//            ResultSet Rs = stmt.executeQuery();
//            while (Rs.next()) {
//                students.add(new Student(Rs.getLong(1),Rs.getString(2), Rs.getString(3), Rs.getString(4), Rs.getString(5), Rs.getString(6), Rs.getFloat(7)));
//                //System.out.println(Rs.getInt(1) + " " + Rs.getString(2));
//            }
//        } catch (ClassNotFoundException | SQLException e) {
//            e.printStackTrace();
//        }
//
//        repoStudenti.golesteRepo();
//        students.forEach(repoStudenti::save);
//
//        //lista cu teme
//        List<Tema> teme = new ArrayList<>();
//        try {
//            Class.forName("org.postgresql.Driver");
//            PreparedStatement stmt = con.prepareStatement("select * from entitati.public.teme");
//            ResultSet Rs = stmt.executeQuery();
//            while (Rs.next()) {
//                teme.add(new Tema(Rs.getLong(1), Rs.getString(2), Rs.getInt(3), Rs.getInt(4)));
//            }
//        } catch (ClassNotFoundException | SQLException e) {
//            e.printStackTrace();
//        }
//
//        repoTeme.golesteRepo();
//        teme.forEach(repoTeme::save);
//
//        //lista cu note
//        List<Nota> note = new ArrayList<>();
//        try {
//            Class.forName("org.postgresql.Driver");
//            PreparedStatement stmt = con.prepareStatement("select * from entitati.public.note");
//
//            String sqlStr = "select * from entitati.public.studenti where id = ";
//            String sqlStrTeme = "select * from entitati.public.teme where id = ";
//
//
//            ResultSet Rs = stmt.executeQuery();
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//            while (Rs.next()){
//                String idS = String.valueOf(Rs.getLong(1));
//                PreparedStatement getStudentData = con.prepareStatement(sqlStr + idS);
//                ResultSet RezStud = getStudentData.executeQuery();
//                RezStud.next();
//
//                String idT = String.valueOf(Rs.getLong(2));
//                PreparedStatement getTemaData = con.prepareStatement(sqlStrTeme + idT);
//                ResultSet RezTema = getTemaData.executeQuery();
//                RezTema.next();
//
//                note.add(new Nota(LocalDate.parse(Rs.getString(3),formatter),Rs.getString(4),new Student(RezStud.getLong(1),RezStud.getString(2),RezStud.getString(3),RezStud.getString(4),RezStud.getString(5),RezStud.getString(6),RezStud.getFloat(7)),new Tema(RezTema.getLong(1),RezTema.getString(2),RezTema.getInt(3),RezTema.getInt(4)), Rs.getInt(5)));
//            }
//            con.close();
//        } catch (ClassNotFoundException | SQLException e) {
//            e.printStackTrace();
//        }
//
//        repoNote.golesteRepo();
//        note.forEach(repoNote::save);

    }

    public void addStudent(String nume, String prenume, String grupa, String email, String cadruDidacticIndrumatorLab, float media) {
        List<Long> keys = new ArrayList<>(repoTeme.getMap().keySet());
        Collections.sort(keys);
        int pozitie = 0;
        int pretendent = 1;
        for (; ; ) {
            if (pretendent == keys.size() + 1)
                break;
            if (keys.get(pozitie) != pretendent)
                break;
            pozitie++;
            pretendent++;
        }

        Long id = Long.parseLong(String.valueOf(pretendent));
        Student student = new Student(id, nume, prenume, grupa, email, cadruDidacticIndrumatorLab, media);
        validatorStudenti.validate(student);
        repoStudenti.save(student);
    }

    public void updateStudent(Long id, String nume, String prenume, String grupa, String email, String cadruDidacticIndrumatorLab, float media) {
        Student student = new Student(id, nume, prenume, grupa, email, cadruDidacticIndrumatorLab, media);
        validatorStudenti.validate(student);
        repoStudenti.update(student);
    }

    public void deleteStudent(Long id) {
        repoStudenti.delete(id);
        repoNote.findAll().forEach(x -> {
            if (x.getStudent().getId() == id)
                repoNote.delete(x.getId());
        });

    }

    public void addTema(String descriere, Integer deadLineWeek, String materia) {
        List<Long> keys = new ArrayList<>(repoTeme.getMap().keySet());
        Collections.sort(keys);
        int pozitie = 0;
        int pretendent = 1;
        for (; ; ) {
            if (pretendent == keys.size() + 1)
                break;
            if (keys.get(pozitie) != pretendent)
                break;
            pozitie++;
            pretendent++;
        }

        Long id = Long.parseLong(String.valueOf(pretendent));
        //generam start week ul
        Integer startWeek = anUniversitar.getCurentWeek();
        Tema tema = new Tema(id, descriere, startWeek, deadLineWeek, materia);
        validatorTema.validate(tema);
        Tema t = repoTeme.save(tema);
        if (t == null)
            notifyObservers(new EvenimentSchimbare(TipEveniment.ADD, t));

    }

    public void updateTema(Long id, String descriere, Integer deadLineWeek) {
        Integer startweek = repoTeme.get(id).getStartWeek();
        Tema tema = new Tema(id, descriere, startweek, deadLineWeek);
        validatorTema.validate(tema);
        Tema oldTema = repoTeme.get(id);
        if (oldTema != null) {
            Tema res = repoTeme.update(tema);
            notifyObservers(new EvenimentSchimbare(TipEveniment.UPDATE, tema, oldTema));
        }
    }

    public void deleteTema(Long id) {
        Tema t = repoTeme.delete(id);
        for (Nota n : repoNote.getAll()) {
            if (n.getTema().getId() == id)
                repoNote.delete(n.getId());
        }

        notifyObservers(new EvenimentSchimbare(TipEveniment.DELETE, t));
    }

    public String calculeazaNota(String nota, Integer deadlineWeek, Integer nrSaptMotivate, boolean aUitatProful) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String for_return = null;

        boolean sePotIntroduceNote = false;
        boolean adaugat = true;
        AtomicReference<Integer> nr_zile = new AtomicReference<>(0);

        //verificam daca se mai pot introduce note
        //cazul in care profesorul uita

        Integer saptamanaCurentaAnCalendaristic = Integer.parseInt(new SimpleDateFormat("w").format(new java.util.Date()));
        if (LocalDateTime.now().getYear() == Integer.parseInt(anUniversitar.getAnUniversitar().toString())) {
            //suntem in sem 1
            TemporalField nrSaptamanaAn = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
            int saptamanaInceputSem1 = anUniversitar.getDataInceputSem1().get(nrSaptamanaAn);

            Integer nrSaptamanaDeLaInceput = saptamanaCurentaAnCalendaristic - saptamanaInceputSem1 + 1;
            if (nrSaptamanaDeLaInceput < 17) sePotIntroduceNote = true;
        } else {
            //suntem in sem 2
            TemporalField nrSaptamanaAn = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
            int saptamanaInceputSem2 = anUniversitar.getDataInceputSem2().get(nrSaptamanaAn);

            Integer nrSaptamanaDeLaInceput = saptamanaCurentaAnCalendaristic - saptamanaInceputSem2 + 1;
            if (nrSaptamanaDeLaInceput < 16) sePotIntroduceNote = true;
        }

        if (sePotIntroduceNote) {

            Integer nrSapt = anUniversitar.getCurentWeek();
            Integer diferenta = nrSapt - deadlineWeek;

            //diferenta de saptamani cu care se preda tema

            if (diferenta > 0) {
                if (diferenta >= 0 && diferenta < 3) {
                    Integer aux = Integer.parseInt(nota) - diferenta;
                    for_return = aux.toString();
                } else if (diferenta > 2) {
                    if (aUitatProful) {
                        for_return = nota;
                    } else {
                        if (nrSaptMotivate >= diferenta - 2) {
                            Integer aux = Integer.parseInt(nota) - 2;
                            for_return = aux.toString();
                        } else {
                            for_return = "1";
                        }
                    }
                }
            } else {
                //se preda mai repede
                for_return = nota;
            }
        } else {
            // a trecut perioada in care se pot adauga note
            // studentul va primi automat nota 10
            System.out.println("A trecut perioada in care se pot introduce note! Final de semestru!\n");
            for_return = "10";
        }

        return for_return;
    }

    public void modificaNota(Student s, Tema t, int parseInt, String text) {
        Nota nota = new Nota(LocalDate.now(), s.getCadruDidacticIndrumatorLab(), s, t, parseInt, text);
        validatorNota.validate(nota);
        repoNote.update(nota);
        notifyObservers(new EvenimentSchimbare(TipEveniment.UPDATE, nota));
    }

    public void addNota(Student student, Tema tema, Integer valoare, String feedback, boolean aUitatProful) {
        Nota nota = new Nota(LocalDate.now(), student.getCadruDidacticIndrumatorLab(), student, tema, valoare, feedback);
        nota.setInregistrataCuIntarziereaProfului(aUitatProful);
        boolean adaugat = true;
        validatorNota.validate(nota);

        if (repoNote.save(nota) != null) {
            adaugat = false;
            System.out.println("Studentul are deja nota la aceasta disciplina!\n");
        }
        if (adaugat) {
            String numeFisier = nota.getStudent().getNume() + nota.getStudent().getPrenume() + ".json";

            JSONObject objSecundar = new JSONObject();

            objSecundar.put("Tema", nota.getTema().getDescriere());
            objSecundar.put("Nota", nota.getValoare());
            objSecundar.put("Predata in saptamana", anUniversitar.getCurentWeek());
            objSecundar.put("Deadline", nota.getTema().getDeadlineWeek());
            objSecundar.put("Feedback", feedback);
            String cheiePrincipala = "Tema" + repoNote.size();

            JSONObject objPrincipal = new JSONObject();

            BufferedReader br = null;
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/" + numeFisier, true))) {
                br = new BufferedReader(new FileReader("data/" + numeFisier));
                if (br.readLine() == null) {
                    writer.newLine();
                    writer.write("{}");
                    writer.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            JSONParser parser = new JSONParser();

            try {
                Object obj = parser.parse(new FileReader("data/" + numeFisier));
                objPrincipal = (JSONObject) obj;
                objPrincipal.put(cheiePrincipala, objSecundar);
            } catch (ParseException | FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            try (FileWriter fileWriter = new FileWriter("data/" + numeFisier)) {
                fileWriter.write(objPrincipal.toJSONString());
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        notifyObservers(new EvenimentSchimbare(TipEveniment.ADD, nota));
    }

    public Iterable<Nota> findAllNote() {
        return repoNote.findAll();
    }

    public Iterable<Tema> findAllTeme() {
        return repoTeme.findAll();
    }

    public Iterable<Student> findAllStudenti() {
        return repoStudenti.findAll();
    }

    public Iterable<Student> studentiiGrupei(String grupa) {
        Predicate<Student> p = x -> x.getGrupa().equals(grupa);

        List<Student> rez = (List<Student>) repoStudenti.getMap().values().stream()
                .filter(p)
                .collect(Collectors.toList());
        return rez;
    }

    public Iterable<Student> studentiiCareAuPredatTema(Long id) {
        Predicate<Nota> p = x -> x.getTema().getId() == id;
        List<Nota> list = new ArrayList<>(repoNote.getMap().values());

        List<Student> rez = list.stream()
                .filter(p)
                .map(Nota::getStudent)
                .collect(Collectors.toList());
        return rez;
    }


    public Iterable<Student> studentiiCareAuPredatTemaLaUnProfesor(Long id, String profesor) {

        List<Nota> list = new ArrayList<>(repoNote.getMap().values());
        Predicate<Nota> p1 = x -> x.getTema().getId() == id;
        Predicate<Nota> p2 = x -> x.getProfesor().equals(profesor);
        Predicate<Nota> p = p1.and(p2);

        List<Student> rez = list.stream()
                .filter(p)
                .map(Nota::getStudent)
                .collect(Collectors.toList());
        return rez;
    }

    public Iterable<Nota> noteleLaOTemaInOSaptamana(Long id, Integer saptamana) {
        List<Nota> list = new ArrayList<>(repoNote.getMap().values());

        Predicate<Nota> p1 = x -> x.getTema().getId() == id;

        Predicate<Nota> p2 = x -> anUniversitar.getNrSaptamanaDeStudiuDinSemestruAlDatei(x.getData().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))) == saptamana;

        Predicate<Nota> p = p1.and(p2);

        List<Nota> rez = list.stream()
                .filter(p)
                .collect(Collectors.toList());

        return rez;

    }

    private List<Observer<EvenimentSchimbare>> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer<EvenimentSchimbare> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<EvenimentSchimbare> e) {
    }

    @Override
    public void notifyObservers(EvenimentSchimbare t) {
        observers.stream().forEach(x -> x.update(t));
    }

    public List<Tema> temeFaraNotaAleStudentului(String id) {
        List<Nota> list = new ArrayList<>(repoNote.getMap().values());
        List<Tema> aux = list.stream().filter(nota -> nota.getStudent().getId().toString().equals(id)).map(Nota::getTema).collect(Collectors.toList());

        List<Tema> toate_temele = new ArrayList<>(repoTeme.getMap().values());
        List<Tema> rez = new ArrayList<>();
        return toate_temele.stream().filter(tema -> !aux.contains(tema)).collect(Collectors.toList());

    }

    public List<String> getAllTemeDesc() {
        List<Tema> rez = new ArrayList<>(repoTeme.getMap().values());
        return rez.stream().map(tema -> tema.getDescriere()).collect(Collectors.toList());
    }

    public Tema getTemabyDescriere(String tema) {
        List<Tema> rez = new ArrayList<>(repoTeme.getMap().values());
        for (Tema t : rez)
            if (t.getDescriere().equals(tema))
                return t;
        return null;
    }

    public Student getStudentbyID(String id) {
        for (Student s : findAllStudenti())
            if (s.getId().toString().equals(id))
                return s;
        return null;
    }


    public Nota getNota(IdObject id) {
        List<Nota> note = new ArrayList<>(repoNote.getMap().values());
        for (Nota n : note)
            if (n.getId() == id)
                return n;
        return null;

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

    public String getDescTemaCurentWeek() {
        List<Tema> teme = new ArrayList<>(repoTeme.getMap().values());
        for (Tema t : teme)
            if (t.getDeadlineWeek() == anUniversitar.getCurentWeek())
                return t.getDescriere();
        return "";
    }

    public Map getMapNote() {
        return repoNote.getMap();
    }

    public Map getMapStudenti() {
        return repoStudenti.getMap();
    }

    public Map<Student, Double> getFiltrare1() {
        List<Nota> note = new ArrayList<>(getMapNote().values());
        Map<Student, Double> rez = new HashMap<>();
        int nrSaptamani = 0;
        for (Tema t : repoTeme.getAll())
            nrSaptamani += (t.getDeadlineWeek() - t.getStartWeek());

        for (Student s : repoStudenti.getAll()) {
            double suma = 0;
            for (Nota n : note) {
                if (s.getId().equals(n.getStudent().getId()))
                    suma += n.getValoare() * (repoTeme.get(n.getTema().getId()).getDeadlineWeek() - repoTeme.get(n.getTema().getId()).getStartWeek());
            }
            suma /= nrSaptamani;
            rez.put(s, suma);
        }

        return rez;
    }

    public Map<Tema, Double> getFiltrare2() {
        List<Nota> note = new ArrayList<>(getMapNote().values());
        Map<Tema, Double> rez = new HashMap<>();

        for (Tema t : repoTeme.getAll()) {
            double suma = 0;
            for (Nota n : note) {
                if (t.getId().equals(n.getTema().getId()))
                    suma += n.getValoare();
            }
            suma /= repoStudenti.size();
            rez.put(t, suma);
        }

        return rez;
    }

    private double media(List<Nota> list) {
        Integer suma = 0;
        for (Nota n : list)
            suma += n.getValoare();
        return (double) suma / list.size();
    }

    public Map<Student, Double> getFiltrare3() {

        List<Nota> note = new ArrayList<>(getMapNote().values());
        Map<Student, Double> rez = new HashMap<>();
        int nrSaptamani = 0;
        for (Tema t : repoTeme.getAll())
            nrSaptamani += (t.getDeadlineWeek() - t.getStartWeek());
        for (Student s : repoStudenti.getAll()) {
            double suma = 0;
            for (Nota n : note) {
                if (s.getId().equals(n.getStudent().getId()))
                    suma += n.getValoare() * (repoTeme.get(n.getTema().getId()).getDeadlineWeek() - repoTeme.get(n.getTema().getId()).getStartWeek());
            }
            suma /= nrSaptamani;
            rez.put(s, suma);
        }

        Set<Map.Entry<Student, Double>> set = rez.entrySet();
        Map<Student, Double> for_return = new HashMap<>();
        set = set.stream()
                .filter(x -> x.getValue() >= 4)
                .collect(Collectors.toSet());

        for (Map.Entry<Student, Double> e : set) {
            for_return.put(e.getKey(), e.getValue());
        }
        return for_return;
    }

    int numarNote(Student student) {
        List<Nota> note = new ArrayList<>(getMapNote().values());
        int numar = 0;
        for (Nota nota : note)
            if (nota.getStudent().getId() == student.getId())
                if (anUniversitar.getNrSaptamanaDeStudiuDinSemestruAlDatei(nota.getData().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))) > nota.getTema().getDeadlineWeek()) {
                    if (nota.isInregistrataCuIntarziereaProfului())
                        ++numar;
                } else
                    ++numar;

        return numar;
    }

    public Map<Student, Double> getFiltrare4() {
        Map<Student, Double> medii = getFiltrare1();

        List<Tema> temeCuDeadLinePanaInPrezent = new ArrayList<>();

        for (Tema tema : repoTeme.getAll()) {
            if (tema.getDeadlineWeek() < anUniversitar.getCurentWeek())
                temeCuDeadLinePanaInPrezent.add(tema);
        }
        boolean ok = true;

        Map<Student, Double> rezultat = new HashMap<>();

        for (Student s : repoStudenti.getAll()) {
            if (numarNote(s) >= temeCuDeadLinePanaInPrezent.size()) {
                rezultat.put(s, medii.get(s));
            }
        }

        return rezultat;
    }

    public String getTemaCurenta() {
        for (Tema t : repoTeme.getAll())
            if (anUniversitar.getCurentWeek() == t.getDeadlineWeek())
                return t.getDescriere();
        return "";
    }

    public List<String> getListaNumeStudenti() {
        List<String> ret = new ArrayList<>();
        for (Student s : repoStudenti.getAll())
            ret.add(s.getNume() + " " + s.getPrenume());
        return ret;
    }

    public StudentDto getStudentDtobyName(String text) {
        String[] elems = text.split(" ");
        if (elems.length != 2)
            return null;
        String nume = text.split(" ")[0];
        String prenume = text.split(" ")[1];
        for (Student s : repoStudenti.getAll())
            if (s.getNume().equals(nume) && s.getPrenume().equals(prenume))
                return new StudentDto(s.getId().toString(), text, s.getGrupa());
        return null;
    }


    public List<Tema> getAllTeme() {
        return repoTeme.getAll();
    }

    public List<Tema> getTemeInterval(int fromIndex, int toIndex) {
        return repoTeme.getTemeInterval(fromIndex, toIndex);
    }

    public String getStudentbyUserName(String userName) {
        return repoStudenti.getNumeStudentByUserName(userName);
    }

    public List<NotaDto> getNoteDtoStudentList(String nume) {
        String[] elems = nume.split(" ");
        List<NotaDto> rez = new ArrayList<>();
        for (Nota n : repoNote.getAll()) {
            if (n.getStudent().getNume().equals(elems[0]) && n.getStudent().getPrenume().equals(elems[1]))
                rez.add(new NotaDto(n.getId(), n.getStudent().getNume() + " " + n.getStudent().getPrenume(), n.getTema().getDescriere(), n.getValoare().toString(), n.getFeedback()));
        }
        return rez;
    }

    public Collection<? extends InregistrareDTO> getListaInregistrari() {
        List<InregistrareDTO> rez = new ArrayList<>();
        List<Student> students = repoStudenti.getAll();
        for (Student s : students) {
            InregistrareDTO inregistrareDTO = new InregistrareDTO(s.getNume() + " " + s.getPrenume(), repoStudenti.getUserName(s.getId()));
            rez.add(inregistrareDTO);
        }
        return rez;
    }

    public List<InregistrareDTO> getAllInregistrari() {
        return repoStudenti.getAllInregistrari();
    }

    public void addInregistrare(InregistrareDTO i, String parola) {
        repoStudenti.addInregistrare(i, parola);
    }

    public String encode(String text) {
        return BCrypt.hashpw(text, BCrypt.gensalt());
    }

    public Map<String, String> getInregistrariStudenti() {
        List<InregistrareDTO> toate = repoStudenti.getAllInregistrari();
        Map<String, String> rez = new HashMap<>();
        for (InregistrareDTO i : toate) {
            rez.put(i.getUserName(), repoStudenti.getParola(repoStudenti.getIdStudentByName(i.getStudent())));
        }
        return rez;
    }

    public Map<String, String> getInregistrariProfesori() {
        return repoStudenti.getInregistrariProfesori();

    }

    public Map<String, String> getInregistrariAdmini() {
        return repoStudenti.getInregistrariAdmini();
    }

    public List<String> getListaMaterii() {
        return repoTeme.getMaterii();
    }

    public List<Tema> getTemeLaMateria(String toString) {
        List<Tema> toate = repoTeme.getAll();
        List<Tema> rez = new ArrayList<>();

        for (Tema t : toate)
            if (t.getMaterie().equals(toString))
                rez.add(t);
        return rez;
    }

    public List<NotaDto> getNoteDtoMaterieList(String materia) {
        List<NotaDto> rez = new ArrayList<>();
        List<Nota> toate = repoNote.getAll();
        List<Tema> teme = repoTeme.getAll();
        for (Tema t : teme) {
            for (Nota n : toate)
                if (n.getTema().getId().equals(t.getId()))
                    n.getTema().setMaterie(t.getMaterie());
        }
        for (Nota n : toate) {
            if (n.getTema().getMaterie().equals(materia))
                rez.add(new NotaDto(n.getId(), n.getStudent().getNume() + " " + n.getStudent().getPrenume(), n.getTema().getDescriere(), n.getValoare().toString(), n.getFeedback()));
        }
        return rez;
    }

    public List<NotaDto> getNoteStudentMaterieDtoList(String nume, String materie) {
        String[] elems = nume.split(" ");
        List<NotaDto> rez = new ArrayList<>();
        List<Nota> toate = repoNote.getAll();
        List<Tema> teme = repoTeme.getAll();
        for (Tema t : teme) {
            for (Nota n : toate)
                if (n.getTema().getId().equals(t.getId()))
                    n.getTema().setMaterie(t.getMaterie());
        }
        for (Nota n : toate) {
            if (n.getTema().getMaterie().equals(materie) && n.getStudent().getNume().equals(elems[0]) && n.getStudent().getPrenume().equals(elems[1]))
                rez.add(new NotaDto(n.getId(), n.getStudent().getNume() + " " + n.getStudent().getPrenume(), n.getTema().getDescriere(), n.getValoare().toString(), n.getFeedback()));
        }
        return rez;
    }

    public List<Nota> getAllNote() {
        return repoNote.getAll();
    }

    public String getCriterii(String materia) {
        String rez = "";
        Path path = Paths.get("data/formuleCalcul.txt");
        try {
            List<String> lines = Files.readAllLines(path);

            for (String linie : lines) {
                if (linie != "") {
                    String[] elems = linie.split(" ");
                    if (elems[0].equals(materia))
                        rez += elems[1] + " " + elems[2] + "\n";
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return rez;
    }


    public String getSituatieStudent(String numeStudent, Map<String, String> student_materie__note) {
        String[] elems = numeStudent.split(" ");
        String rez = "Student: " + numeStudent + "\n" + "Materia        Media finala\n";

        for (String materie : repoTeme.getMaterii()) {
            double media = calculeazaMedia(numeStudent, materie, student_materie__note);
            rez += materie + "        " + media + "\n";
        }


        return rez;
    }

    private double calculeazaMedia(String numeStudent, String materie, Map<String, String> student_materie__note) {
        double rez = 0.0F;
        String[] elems = numeStudent.split(" ");
        List<Nota> toate = repoNote.getAll();
        List<Tema> teme = repoTeme.getAll();
        for (Tema t : teme) {
            for (Nota n : toate)
                if (n.getTema().getId().equals(t.getId()))
                    n.getTema().setMaterie(t.getMaterie());
        }

        //calculam media notelor de la laborator
        List<Nota> NoteLaMateriaAsta = toate.stream().filter(nota -> nota.getStudent().getNume().equals(elems[0]) && nota.getStudent().getPrenume().equals(elems[1]) && nota.getTema().getMaterie().equals(materie)).collect(Collectors.toList());

        Integer nrSaptamani = 0;
        for (Tema t : repoTeme.getAll())
            if (t.getMaterie().equals(materie))
                nrSaptamani += (t.getDeadlineWeek() - t.getStartWeek());

        double suma = 0.0;
        for (Nota n : NoteLaMateriaAsta) {
            suma += n.getValoare() * (repoTeme.get(n.getTema().getId()).getDeadlineWeek() - repoTeme.get(n.getTema().getId()).getStartWeek());
        }
        if (nrSaptamani != 0)
            suma /= nrSaptamani;//media la laborator;

        //calculam media totala cu celelalte note care intra in formula

        String[] probe = getFormulaCalcul(materie).split(" ");

        List<Float> note = new ArrayList<>();
        //note.add((float) suma);

        String formula_notele = student_materie__note.get(numeStudent + " " + materie);

        String probele = formula_notele.split("_")[0];
        String notele = formula_notele.split("_")[1];
        String procente = formula_notele.split("_")[2];

        Map<String, String> proba_nota = new HashMap<>();
        Map<String, String> proba_procent = new HashMap<>();

        for (int i = 0; i < probe.length; i++) {
            proba_nota.put(probele.split(" ")[i], notele.split(" ")[i]);
            proba_procent.put(probele.split(" ")[i], procente.split(" ")[i]);
        }

        for (String proba : probe) {
            note.add(Float.parseFloat(proba_nota.get(proba)));
        }


        //in note sunte toate notele de la fiecare proba la aceasta materie inafara de lab


        double sum = note.stream()
                .mapToDouble(Float::doubleValue)
                .sum();

        float sumaa = 0.0F;
        for (int i = 0; i < probe.length; i++) {
            int a1 = Integer.parseInt(proba_procent.get(probe[i]));
            float a2 = Float.parseFloat(proba_nota.get(probe[i]));

            float pr = (float)a1 / 100;

            sumaa += pr * Float.parseFloat(proba_nota.get(probe[i]));
        }

        //media notelor inafara de lab este in sumaa

        //calculam procentul labului

        int suma_procente = 0;
        for (String p : procente.split(" "))
            suma_procente += Integer.parseInt(p);

        int procentLab = 100 - suma_procente;

        rez = (float)((float)procentLab/100) * suma + ((float)suma_procente/100) * sumaa;


        return rez;
    }


    public String getFormulaCalcul(String materia) {
        String rez = "";
        Path path = Paths.get("data/formuleCalcul.txt");
        try {
            List<String> lines = Files.readAllLines(path);

            for (String linie : lines) {
                if (linie != "") {
                    String[] elems = linie.split(" ");
                    if (elems[0].equals(materia) && !elems[1].equals("Lab"))
                        rez += elems[1] + " ";
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return rez;
    }

    public String getProcente(String materia) {
        String rez = "";
        Path path = Paths.get("data/formuleCalcul.txt");
        try {
            List<String> lines = Files.readAllLines(path);
            for (String linie : lines) {
                if (linie != "") {
                    String[] elems = linie.split(" ");
                    if (elems[0].equals(materia) && !elems[1].equals("Lab"))
                        rez += elems[2] + " ";
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return rez;
    }

    public String getSituatieStudent1(String numeStudent, Map<String, String> student_materie__note) {
        String[] elems = numeStudent.split(" ");
        String rez = "Student," + numeStudent + "\n" + "Materia,Media finala\n";

        for (String materie : repoTeme.getMaterii()) {
            double media = calculeazaMedia(numeStudent, materie, student_materie__note);
            rez += materie + "," + media + "\n";
        }


        return rez;
    }
}

