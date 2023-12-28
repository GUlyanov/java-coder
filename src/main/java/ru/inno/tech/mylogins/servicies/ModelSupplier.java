package ru.inno.tech.mylogins.servicies;

import ru.inno.tech.mylogins.entity.Model;

public interface ModelSupplier {
    Model read(String folderName);
}
