package ru.inno.tech.products.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @AllArgsConstructor
public class InstanceArrangment {
    private String generalAgreementId;
    private String supplementaryAgreementId;
    private String arrangementType;
    private Integer shedulerJobId;
    @NotNull
    private String number;
    @NotNull
    private LocalDateTime openingDate;
    private LocalDateTime closingDate;
    private LocalDate cancelDate;
    private String status;
    private Integer validityDuration;
    private String cancellationReason;
    private LocalDate interestCalculationDate;
    private BigDecimal interestRate;
    private BigDecimal coefficient;
    private String coefficientAction;
    private BigDecimal minimumInterestRate;
    private String minimumInterestRateCoefficient;
    private String minimumInterestRateCoefficientAction;
    private BigDecimal maximumInterestRate;
    private String maximumInterestRateCoefficient;
    private String maximumInterestRateCoefficientAction;
}
