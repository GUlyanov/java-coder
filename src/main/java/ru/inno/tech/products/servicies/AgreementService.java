package ru.inno.tech.products.servicies;

import org.springframework.stereotype.Service;
import ru.inno.tech.products.entities.Agreement;
import ru.inno.tech.products.entities.ProdType;
import ru.inno.tech.products.entities.Product;
import ru.inno.tech.products.exceptions.AgreementByNumberExException;
import ru.inno.tech.products.repositories.AgreementRepository;
import ru.inno.tech.products.requests.InstanceArrangment;
import ru.inno.tech.products.requests.ProductCreateRequestBody;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AgreementService implements AgreementServiceInt {
    AgreementRepository agrRep;

    public AgreementService(AgreementRepository agrRep) {
        this.agrRep = agrRep;
    }

    // Формирование списка допсоглашений из запроса на создание договора
    public Set<Agreement> formAgreements(ProductCreateRequestBody reqBody){
        Set<Agreement> setAgr = new HashSet<>();
        List<InstanceArrangment> agrLst = reqBody.getInstanceArrangmentList();
        if (agrLst==null) return null;
        for (InstanceArrangment insArr: reqBody.getInstanceArrangmentList()) {
            Agreement agr = new Agreement();
            agr.setType(ProdType.valueOf(insArr.getArrangementType()));
            agr.setNumber(insArr.getNumber());
            agr.setStartDateTime(insArr.getOpeningDate());
            agr.setState(insArr.getStatus());
            //--
            agr.setEndDateTime(insArr.getClosingDate());
            agr.setDays(insArr.getValidityDuration());
            agr.setReasonClose(insArr.getCancellationReason());
            setAgr.add(agr);
        }
        return setAgr;
    }

    // Сохранить допсоглашение
    public void saveAgreement(Agreement agreement, Product product){
        product.addAgreement(agreement);
        agrRep.save(agreement);
    }

    public Agreement findAgreementByProductAndNumber(Product product, String number, boolean doEx){
        Optional<Agreement> agrDb = agrRep.findAgreementByProductAndNumber(product, number);
        if (agrDb.isPresent()) {
            if (doEx) throw new AgreementByNumberExException(null, product, number);
            else return agrDb.get();
        }
        return null;
    }


}
