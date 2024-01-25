package ru.inno.tech.products.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class ProdRegCreateRequestBody {
    @NotNull
    Integer instanceId;
    @NotNull
    String registryTypeCode;
    String accountType;
    String currencyCode;
    String branchCode;
    String priorityCode;
    String mdmCode;
    String clientCode;
    String trainRegion;
    String counter;
    String salesCode;
}
