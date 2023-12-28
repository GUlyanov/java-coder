package ru.inno.tech.mylogins.validators;

import ru.inno.tech.mylogins.entity.Model;

public interface ModelValidator {
    boolean validate(Model model);

    ModelValidator next();
}
