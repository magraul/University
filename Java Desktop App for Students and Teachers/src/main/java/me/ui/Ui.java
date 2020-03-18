package me.ui;

import me.exceptions.ValidationException;
import me.services.Service;

import java.util.Scanner;

public class Ui {
    private Service service;

    public Ui(Service service) {
        this.service = service;
    }


    public void run() {
        for (; ;) {
            try {
                System.out.println("MENIU:\n1: sterge student\n2: modifica student\n3: adauga tema\n4: sterge tema\n5: modifica tema\n6: sterge profesor\n7: modifica profeosr\n8: da tema unui student\n9: modifica tema unui student\n10: sterge tema unui student\n11: afisare asignari\n12: afisare teme\n13: afisare studenti\n14: afisare profesori\n15: Toti studentii unei grupe\n16: Toti studentii care au predat o anumita tema\n17: Toti studentii care au predat o anumita tema unui profesor\n18:  Toate notele la o anumita tema, dintr-o saptamana data\n");
                Scanner keyboard = new Scanner(System.in);
                System.out.println("alegeti optiunea: ");
                Integer optiune = keyboard.nextInt();
                if(optiune == 19) {
                    System.out.println("dati nume: ");
                    String nume = keyboard.next();
                    System.out.println("dati prenume:" );
                    String prenume = keyboard.next();
                    System.out.println("dati grupa: ");
                    String grupa = keyboard.next();
                    System.out.println("dati mail: ");
                    String mail = keyboard.next();
                    System.out.println("dati prof: ");
                    String profesor = keyboard.next();
                    System.out.println("dati media: ");
                    Float media = keyboard.nextFloat();
                    service.addStudent(nume,prenume,grupa,mail,profesor,media);


                }
                else if (optiune == 1) {
                    System.out.println("dati id ul de sters: ");
                    Long id = keyboard.nextLong();
                    //service_studenti.delete(id);
                    service.deleteStudent(id);
                } else if (optiune == 2) {
                    System.out.println("dati id ul de modificat: ");
                    Long id = keyboard.nextLong();
                    System.out.println("dati noile date ale studentului:");
                    System.out.println("Nume:");
                    String nume = keyboard.next();
                    System.out.println("Prenume:");
                    String prenume = keyboard.next();
                    System.out.println("Grupa: ");
                    String grupa = keyboard.next();
                    System.out.println("email: ");
                    String email = keyboard.next();
                    System.out.println("Cadru didactic: ");
                    String cadru = keyboard.next();
                    System.out.println("Media: ");
                    float media = keyboard.nextFloat();

                    //service_studenti.update(new Student(id, nume, prenume, grupa, email, cadru, media));
                    service.updateStudent(id, nume, prenume, grupa, email, cadru, media);

                } else if (optiune == 3) {
                    System.out.println("dati descrierea: ");
                    String descriere = keyboard.next();
                    System.out.println("dati deadline ul: ");
                    Integer deadline = keyboard.nextInt();

                  //  service.addTema(descriere, deadline, materia);

                } else if (optiune == 4) {
                    System.out.println("dati id ul temei de sters");
                    Long id = keyboard.nextLong();

                    service.deleteTema(id);
                } else if (optiune == 5) {
                    System.out.println("dati id ul temei de modificat: ");
                    Long id = keyboard.nextLong();
                    System.out.println("dati descrierea noua:");
                    String descriere = keyboard.next();
                    System.out.println("dati noul deadlineWeek: ");
                    Integer deadline = keyboard.nextInt();
                    //service_teme.update(new Tema(id, descriere, service_teme.get(id).getStartWeek(), new Saptamana(deadline)));
                    service.updateTema(id, descriere, deadline);

                } else if (optiune == 8) {
                    System.out.println("dati id ul studentului caruia ii dati tema: ");
                    Long id_stud = keyboard.nextLong();
                    System.out.println("dati id ul temei pe care o dati: ");
                    Long id_teme = keyboard.nextLong();

                    System.out.println("dati numele profesorului care da nota: ");
                    String numeProf = keyboard.next();

                    System.out.println("dati data in care a fost predata tema(format yyyy-MM-dd): ");
                    String data = keyboard.next();

                    System.out.println("dati valoare notei: ");
                    Integer nota = keyboard.nextInt();

                    System.out.println("dati un feedback: ");
                    String feedback = keyboard.next();


                //    service.addNota(id_stud, id_teme, numeProf, data, nota, feedback);
                } else if (optiune == 11) {
                    System.out.println(service.findAllNote());
                } else if (optiune == 12) {
                    System.out.println(service.findAllTeme());

                    //System.out.println(service_teme.findAll());
                } else if (optiune == 13) {
                    System.out.println(service.findAllStudenti());
                    //System.out.println(service_studenti.findAll());

                } else if (optiune == 15) {
                    //toti sudentii unei grupe
                    System.out.println("dati grupa: ");
                    String grupa = keyboard.next();
                    System.out.println(service.studentiiGrupei(grupa));
                } else if (optiune == 16) {
                    System.out.println("dati id ul temei: ");
                    Long id = keyboard.nextLong();
                    System.out.println(service.studentiiCareAuPredatTema(id));
                } else if (optiune == 17) {
                    System.out.println("dati id ul temei: ");
                    Long id = keyboard.nextLong();
                    System.out.println("dati numele profesorului: ");
                    String profesor = keyboard.next();
                    System.out.println(service.studentiiCareAuPredatTemaLaUnProfesor(id, profesor));
                } else if (optiune == 18) {
                    System.out.println("dati id ul temei: ");
                    Long id = keyboard.nextLong();
                    Integer saptamana = keyboard.nextInt();
                    System.out.println(service.noteleLaOTemaInOSaptamana(id, saptamana));
                } else if (optiune == 0) {
                    return;
                } else {
                    System.out.println("optiune invalida!\n");
                }
            } catch (ValidationException e) {
                System.out.println(e.getEroare());
            } catch (IllegalArgumentException e) {
                System.out.println(e.toString());
            } catch (Exception e) {
                System.out.println("Eroare!");
            }
        }
    }
}

