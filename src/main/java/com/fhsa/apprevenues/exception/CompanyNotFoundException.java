package com.fhsa.apprevenues.exception;

import static com.fhsa.apprevenues.util.ConstantMessages.ERROR_COMPANY_NOT_FOUND;

public class CompanyNotFoundException extends RuntimeException {

    public CompanyNotFoundException(Integer companyId) {
        super(String.format(ERROR_COMPANY_NOT_FOUND, companyId));
    }
}
