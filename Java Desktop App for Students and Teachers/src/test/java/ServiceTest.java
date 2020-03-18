import me.entities.Nota;
import me.entities.Student;
import me.entities.Tema;
import me.exceptions.ValidationException;
import me.repositories.NoteFileRepository;
import me.repositories.StudentFileRepository;
import me.repositories.TemeFileRepository;
import me.services.Service;
import me.services.config.ApplicationContext;
import me.validators.Validator;
import me.validators.ValidatorNota;
import me.validators.ValidatorStudent;
import me.validators.ValidatorTema;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class ServiceTest {

    Validator<Student> studentValidator = new ValidatorStudent();
    Validator<Tema> temaValidator = new ValidatorTema();
    StudentFileRepository repoStudenti = new StudentFileRepository(ApplicationContext.getPROPERTIES().getProperty("data.xmls.test.studenti"));
    TemeFileRepository repoTeme = new TemeFileRepository(ApplicationContext.getPROPERTIES().getProperty("data.xmls.test.teme"));
    NoteFileRepository repoNote = new NoteFileRepository(ApplicationContext.getPROPERTIES().getProperty("data.xmls.test.note"));
    Validator<Nota> validatorNota = new ValidatorNota();
    Service service = new Service(repoStudenti, studentValidator, repoTeme, temaValidator, repoNote,validatorNota);

    @Test
    void addStudent() {
        repoStudenti.clearFile();

        assert repoStudenti.size() == 0;
        repoStudenti.save(new Student(1L,"nume1", "prenume1", "grupa1", "mail1", "cadru1", 10));
        assert repoStudenti.size() == 1;

        Student a = repoStudenti.save(new Student(1L,"nume1", "prenume1", "grupa1", "mail1", "cadru1", 10));
        assert a!=null;
        assert repoStudenti.size() == 1;

        try {
            service.addStudent("", "prenume1", "grupa1", "mail1", "cadru1", 10);
            assert false;
        } catch (ValidationException e) {
            assert true;
        }

        repoStudenti.clearFile();


    }

    @Test
    void updateStudent() {
        repoStudenti.clearFile();
        repoStudenti.save(new Student(1L,"nume1", "prenume1", "grupa1", "mail1", "cadru1", 10));
        service.updateStudent(1L, "nume2", "prenume2", "cadru", "mail", "cadru", 2);
        assert repoStudenti.get(1L).getNume().equals("nume2");

        try {
            service.updateStudent(2L, "fd", "fd", "fd", "fd", "fd", 1);
            assert false;
        } catch (IllegalArgumentException e) {
            assert true;
        }
        repoStudenti.clearFile();
    }

    @Test
    void deleteStudent() {
        repoStudenti.clearFile();
        repoStudenti.save(new Student(1L,"nume1", "prenume1", "grupa1", "mail1", "cadru1", 10));
        assert repoStudenti.size() == 1;
        try {
            service.deleteStudent(2L);
            assert false;
        } catch (IllegalArgumentException e) {
            assert true;
        }

        assert repoStudenti.size() == 1;
        service.deleteStudent(1L);
        assert repoStudenti.size() == 0;
    }

    @Test
    void addTema() {
        if(service.anUniversitar.getCurentWeek()<9) {
            try {
                service.addTema("map", 9, materia);
                assert true;
            } catch (ValidationException e) {
                assert false;
            }
        } else {
            try {
                service.addTema("map", 9, materia);
                assert false;
            } catch (ValidationException e) {
                assert true;
            }
        }

        if(service.anUniversitar.getCurentWeek() < 14) {
            try {
                service.addTema("desc", service.anUniversitar.getCurentWeek() + 1, materia);
                assert true;
            }catch (ValidationException e) {
                assert  false;
            }
        } else {
            try {
                service.addTema("desc", service.anUniversitar.getCurentWeek() + 1, materia);
                assert false;
            }catch (ValidationException e) {
                assert  true;
            }
        }
        repoTeme.clearFile();

    }

    @Test
    void updateTema() {

        if(service.anUniversitar.getCurentWeek() <13) {
            service.addTema("map", 13, materia);
            try {
                service.updateTema(1L, "plf", 14);
                assert true;
            } catch (ValidationException e) {
                assert false;
            }

            try {
                service.updateTema(1L, "bade de date", service.anUniversitar.getCurentWeek() - 1);
                assert false;
            } catch (ValidationException e) {
                assert true;
            }
        }
        repoTeme.clearFile();
    }

    @Test
    void deleteTema() {
        if(service.anUniversitar.getCurentWeek() < 14) {
            service.addTema("map", 14, materia);
            try{
                service.deleteTema(2L);
                assert false;
            } catch (IllegalArgumentException e) {
                assert true;
            }

            try {
                service.deleteTema(1L);
                assert true;
            } catch (ValidationException e) {
                assert false;
            }

        }
        repoTeme.clearFile();
    }

    @Test
    void addNota() {
        if(service.anUniversitar.getCurentWeek() < 11) {
            Student s = new Student(1L, "Mag", "Raul", "224", "mail1", "cadru1", 10);
            repoStudenti.save(s);
            Tema t = new Tema(1L, "map Lab4", 2, 11);
            Tema t2 = new Tema(2L, "map Lab5", 3, 11);
            repoTeme.save(t);
            repoTeme.save(t2);
            service.addNota(1L, 1L, "prof", "2019-11-02", 10, "ft bine");
            service.addNota(1L, 2L, "prof2", "2019-11-02", 8, "ft bine");
            assert repoNote.size() == 2;

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate ld = LocalDate.parse("2019-11-01", formatter);

            Nota nota = new Nota(ld,"PROF",s , t,3, feedback);
            Nota n = repoNote.save(nota);
            assert n != null;

            //sa nu putem adauga dupa doua saptamani de la deadline
            repoStudenti.save(new Student(2L, "nume2", "prenume2", "grupa1", "mail1", "cadru1", 10));
            try {
                service.addNota(2L, 1L, "prof2", "2020-01-18", 9, "ft rau");
                assert false;
            } catch (ValidationException e) {
                assert true;
            }

        }

        repoStudenti.delete(2L);
        repoStudenti.delete(1L);
        assert repoStudenti.size() == 0;

        repoTeme.clearFile();
        repoStudenti.clearFile();
    }
}