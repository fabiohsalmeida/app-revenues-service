package com.fhsa.apprevenues.writer;

import com.fhsa.apprevenues.domain.entity.CompanyEntity;
import com.fhsa.apprevenues.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CompanyItemWriter implements ItemWriter<CompanyEntity> {

    private final CompanyRepository companyRepository;

    @Override
    public void write(Chunk<? extends CompanyEntity> chunk) throws Exception {
        List<CompanyEntity> items = (List<CompanyEntity>) chunk.getItems();

        items.stream().forEach(i -> checkIfItemDoestExistsAndSaveIt(i));
    }

    private void checkIfItemDoestExistsAndSaveIt(CompanyEntity item) {
        if (!isItemIdAlreadyInUse(item)) {
            companyRepository.save(item);
        }
    }

    private boolean isItemIdAlreadyInUse(CompanyEntity item) {
        Optional<CompanyEntity> entity = companyRepository.findById(item.getId());

        return entity.isPresent() ? checkIfDifferentRegisterExists(item, entity.get()) : false;
    }

    private boolean checkIfDifferentRegisterExists(CompanyEntity item, CompanyEntity entity) {
        if (isInputItemDifferentFromStoredEntity(item, entity)) {
            System.out.println("Trying to insert in id %d company (name: %s, country: %s), but already exists a different company stored (name: %s, country: %s");
        }

        return true;
    }

    private boolean isInputItemDifferentFromStoredEntity(CompanyEntity item, CompanyEntity entity) {
        return !item.getName().equals(entity.getName()) || !item.getCountryCode().equals(entity.getCountryCode());
    }
}
