package ru.inno.tech.products.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;
import ru.inno.tech.products.entities.AccountPool;

import java.util.Optional;

public interface AccountPoolRepository extends JpaRepository<AccountPool,Integer> {
    @Query("select p from AccountPool p where p.branchCode=:branchCode and p.currencyCode=:currencyCode and " +
            " p.mdmCode=:mdmCode and p.priority=:priority and p.registryTypeCode = :registryTypeCode")
    public Optional<AccountPool> getAccountPool(@Param("branchCode") String branchCode,
                                                @Param("currencyCode") String currencyCode,
                                                @Param("mdmCode") String mdmCode,
                                                @Param("priority") String priority,
                                                @Param("registryTypeCode") String registryTypeCode);
}
