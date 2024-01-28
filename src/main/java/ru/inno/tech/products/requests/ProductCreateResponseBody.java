package ru.inno.tech.products.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
public class ProductCreateResponseBody {
    private Integer instanceId;
    private List<Integer> supplementaryAgreementId = new ArrayList<>();
    private List<Integer> registerId = new ArrayList<>();
    public void addRegisterId(int id){
        registerId.add(id);
    }
    public void addAgreementId(int id){
        supplementaryAgreementId.add(id);
    }
}
