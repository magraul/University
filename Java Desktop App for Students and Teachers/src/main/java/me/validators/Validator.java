package me.validators;

import me.exceptions.ValidationException;

public interface Validator<TIP_ENTITATE> {
    void validate(TIP_ENTITATE entity) throws ValidationException;
}
