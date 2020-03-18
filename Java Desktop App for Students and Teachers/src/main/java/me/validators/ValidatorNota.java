package me.validators;

import me.entities.Nota;
import me.exceptions.ValidationException;
import me.services.config.ApplicationContext;
import me.structures.AnUniversitar;

import java.time.format.DateTimeFormatter;

public class ValidatorNota implements Validator<Nota> {
    String errors;

    AnUniversitar anUniversitar = new AnUniversitar(ApplicationContext.getPROPERTIES().getProperty("data.structure.anUniversitar"));

    @Override
    public void validate(Nota entity) throws ValidationException {

        if(entity == null)
            errors += "Nu se poate adauga tema!\n";
        else {
            if (entity.getData().equals(""))
                errors += "Data invalida!\n";
            if (entity.getProfesor().equals(""))
                errors += "Profesor invalid!\n";
            if (entity.getValoare() < 1 || entity.getValoare() > 10)
                errors += "Nota invalida!\n";

            if (anUniversitar.getNrSaptamanaDeStudiuDinSemestruAlDatei(entity.getData().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))) < entity.getTema().getStartWeek())
                errors += "Data de predare nu poate sa fie mai mica decat start week ul!\n";
        }

        if(errors != null)
            throw new ValidationException(errors);

    }
}
