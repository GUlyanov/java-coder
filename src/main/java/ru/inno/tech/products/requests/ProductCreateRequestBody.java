package ru.inno.tech.products.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
public class ProductCreateRequestBody {
    private Integer instanceId;
    @NotNull
    private String productType;
    @NotNull
    private String productCode;
    @NotNull
    private String registerType;
    @NotNull
    private String mdmCode;
    @NotNull
    private String contractNumber;
    @NotNull
    private LocalDate contractDate;
    @NotNull
    private String priority;
    private BigDecimal interestRatePenalty;
    private BigDecimal minimalBalance;
    private BigDecimal thresholdAmount;
    private String accountingDetails;
    private String rateType;
    private BigDecimal taxPercentageRate;
    private BigDecimal technicalOverdraftLimitAmount;
    @NotNull
    private Integer contractId;
    @NotNull
    private String branchCode;
    @NotNull
    private String isoCurrencyCode;
    @NotNull
    private String urgencyCode;
    private Integer referenceCode;
    private List<AdditionalProperty> additionalPropertyList;
    private List<InstanceArrangment> instanceArrangmentList;
}
