package ru.inno.tech.mylogins.servicies;

import ru.inno.tech.mylogins.entity.Model;

public interface ModelErrorWriter {
    public void writeErrors(String folderName, Model model);
}
