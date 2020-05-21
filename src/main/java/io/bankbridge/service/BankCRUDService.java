package io.bankbridge.service;

import io.bankbridge.exception.BankModelException;
import io.bankbridge.model.BankModel;

import java.util.Collection;

public interface BankCRUDService {

    public void addBankModel(BankModel bankModel);

    public Collection<BankModel> getBankModels();

    public BankModel getBankModel(String id);

    public BankModel editBankModel(BankModel bankModel)
            throws BankModelException;

    public void deleteBankModel(String id);

    public boolean bankModelExist(String id);
}
