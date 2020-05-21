package io.bankbridge.service;

import io.bankbridge.exception.BankModelException;
import io.bankbridge.model.BankModel;
import io.bankbridge.service.BankCRUDService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BankCRUDServiceMapImp implements BankCRUDService {

    private Map<String, BankModel> map;

    public BankCRUDServiceMapImp() {
        this.map = new HashMap<>();
    }

    @Override
    public void addBankModel(BankModel bankModel) {
        map.put(bankModel.getBic(), bankModel);
    }

    @Override
    public Collection<BankModel> getBankModels() {
        return map.values();
    }

    @Override
    public BankModel getBankModel(String id) {
        return map.get(id);
    }

    @Override
    public BankModel editBankModel(BankModel bankModel) throws BankModelException {

        try {

            if (bankModel == null || bankModel.getBic() == null)
                throw new BankModelException("Bank/Bank Id is null, cannot be edited!");

            BankModel bank = map.get(bankModel.getBic());

            if (bank == null)
                throw new BankModelException("Bank cannot be found!");

            if (bank.getName() != null) {
                bank.setName(bank.getName());
            }

            if (bank.getCountryCode() != null) {
                bank.setCountryCode(bank.getCountryCode());
            }

            if (bank.getAuth() != null) {
                bank.setAuth(bank.getAuth());
            }

            return bank;

        } catch (Exception ex) {
            throw new BankModelException(ex.getMessage());
        }
    }

    @Override
    public void deleteBankModel(String id) {
        map.remove(id);
    }

    @Override
    public boolean bankModelExist(String id) {
        return map.containsKey(id);
    }
}
