package ru.inno.tech.mylogins.servicies;

import ru.inno.tech.mylogins.entity.ConnectInfo;
import ru.inno.tech.mylogins.entity.Model;

public interface ModelConsumer {
    void save(ConnectInfo connInfo, Model model);
}
