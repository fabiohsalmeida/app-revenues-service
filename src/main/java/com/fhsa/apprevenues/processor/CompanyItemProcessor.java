package com.fhsa.apprevenues.processor;

import com.fhsa.apprevenues.domain.entity.CompanyEntity;
import com.fhsa.apprevenues.domain.item.CompanyItem;
import com.fhsa.apprevenues.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.batch.item.ItemProcessor;

import java.util.Optional;

import static com.fhsa.apprevenues.util.ConstantMessages.ERROR_DIFFERENT_COMPANY_SAVED_WITH_SAME_ID;

@RequiredArgsConstructor
public class CompanyItemProcessor implements ItemProcessor<CompanyItem, CompanyEntity> {

    private final CompanyRepository repository;

    @Override
    @SneakyThrows
    public CompanyEntity process(CompanyItem companyItem) {
        return isItemIdAlreadyInUse(companyItem) ? null : new CompanyEntity(
            companyItem.getId(),
            companyItem.getName(),
            companyItem.getCountryCode()
        );
    }

    private boolean isItemIdAlreadyInUse(CompanyItem item) {
        Optional<CompanyEntity> entity = repository.findById(item.getId());

        return entity.isPresent() ? checkIfDifferentRegisterExists(item, entity.get()) : false;
    }

    private boolean checkIfDifferentRegisterExists(CompanyItem item, CompanyEntity entity) {
        if (isInputItemDifferentFromStoredEntity(item, entity)) {
            System.out.println(
                String.format(
                    ERROR_DIFFERENT_COMPANY_SAVED_WITH_SAME_ID,
                    item.getId(),
                    item.getName(),
                    item.getCountryCode(),
                    entity.getName(),
                    entity.getCountryCode()
                )
            );
        }

        return true;
    }

    private boolean isInputItemDifferentFromStoredEntity(CompanyItem item, CompanyEntity entity) {
        return !item.getName().equals(entity.getName()) || !item.getCountryCode().equals(entity.getCountryCode());
    }
}
