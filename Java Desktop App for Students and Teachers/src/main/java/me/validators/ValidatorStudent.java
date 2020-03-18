package me.validators;

import me.entities.Student;
import me.exceptions.ValidationException;

public class ValidatorStudent implements Validator<Student> {
    private String errors;
    @Override
    public void validate(Student entity) throws ValidationException {
        if(entity.getMedia() < 0)
            errors += "medie invalida!\n";
        if(entity.getNume().equals("") || entity.getPrenume().equals("") || entity.getGrupa().equals(""))
            errors += "nume invalid!\n";
        if(errors != null)
            throw new ValidationException(errors);
    }
}
