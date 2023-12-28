package ru.inno.tech.mylogins.servicies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.inno.tech.mylogins.entity.ConnectInfo;
import ru.inno.tech.mylogins.entity.Model;
import ru.inno.tech.mylogins.validators.ModelValidator;

@Component
public class Controller {
    private ModelSupplier supplier;
    private ModelValidator firstValidator;
    private ModelConsumer consumer;
    private ModelErrorWriter errorWriter;
    private ConnectInfo connInfo;
    private String folderName;
    private Model model;

    public Controller(ModelSupplier modelSupplier,
                      ModelConsumer modelConsumer,
                      ModelErrorWriter errorWriter){
        this.supplier = modelSupplier;
        this.consumer = modelConsumer;
        this.errorWriter = errorWriter;
    }

    public ModelSupplier getSupplier() {
        return supplier;
    }

    private void setSupplier(ModelSupplier supplier) {
        this.supplier = supplier;
    }

    public ModelConsumer getConsumer() {
        return consumer;
    }

    private void setConsumer(ModelConsumer consumer) {
        this.consumer = consumer;
    }

    public ConnectInfo getConnInfo() {
        return connInfo;
    }

    public void setConnInfo(ConnectInfo connInfo) {
        this.connInfo = connInfo;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    @Autowired
    @Qualifier("fioValidator")
    public void setFirstValidator(ModelValidator firstValidator) {
        this.firstValidator = firstValidator;
    }

    public void make(){
        model = supplier.read(folderName);

        ModelValidator validator = firstValidator;
        while (validator!=null){
            // проверить порцию данных
            boolean rez = validator.validate(model);
            if (rez==false) break;
            validator = validator.next();
        }
        errorWriter.writeErrors(folderName, model);

        String connStr = "jdbc:postgresql://localhost:5430/postgres?currentSchema=mylogin";
        ConnectInfo connectInfo = new ConnectInfo(connStr, "postgres", "postgres");
        consumer.save(connectInfo, model);

    }


}
