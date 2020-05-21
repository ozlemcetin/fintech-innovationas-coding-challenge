package io.bankbridge.service;

import io.bankbridge.exception.BankModelException;
import io.bankbridge.service.BankCRUDServiceMapImp;
import io.bankbridge.model.BankModel;
import io.bankbridge.service.BankCRUDService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class BankCRUDServiceMapImpTest {

    BankCRUDService service;

    @BeforeEach
    void setUp() {
        service = new BankCRUDServiceMapImp();
    }

    @Test
    void addBankModel() {

        //service
        BankModel bankModel = new BankModel("1234", "Royal Bank of Boredom", "GB", "OAUTH");
        service.addBankModel(bankModel);

    }

    @Test
    void getBankModels() {

        //add
        {
            BankModel bankModel1 = new BankModel("1234", "Royal Bank of Boredom", "GB", "OAUTH");
            service.addBankModel(bankModel1);

            BankModel bankModel2 = new BankModel("5678", "Credit Sweets", "CH", "OpenID");
            service.addBankModel(bankModel2);
        }

        //service
        Collection<BankModel> collection =
                service.getBankModels();

        //assertions
        assertNotNull(collection);
        assertEquals(collection.size(), 2);

    }


    @Test
    void getBankModel() {

        //add
        String bic = "1234";
        {
            BankModel bankModel1 = new BankModel(bic, "Royal Bank of Boredom", "GB", "OAUTH");
            service.addBankModel(bankModel1);
        }

        //service
        BankModel bankModel =
                service.getBankModel(bic);

        //assertions
        assertNotNull(bankModel);
        assertEquals(bankModel.getBic(), bic);
    }

    @Test
    void editBankModel() {

        //add
        BankModel bankModel1 = null;
        {
            bankModel1 = new BankModel("1234", "Royal Bank of Boredom", "GB", "OAUTH");
            service.addBankModel(bankModel1);
        }

        String newTitle = "Royal Bank of Good Omens";
        bankModel1.setName(newTitle);

        //service
        BankModel editedBankModel = null;
        try {
            editedBankModel = service.editBankModel(bankModel1);
        } catch (BankModelException e) {
            e.printStackTrace();
        }

        //assertions
        assertNotNull(editedBankModel);
        assertEquals(editedBankModel.getName(), newTitle);
    }

    @Test
    void deleteBankModel() {

        //add
        String bic = "1234";
        {
            BankModel bankModel1 = new BankModel(bic, "Royal Bank of Boredom", "GB", "OAUTH");
            service.addBankModel(bankModel1);
        }

        //service
        service.deleteBankModel(bic);

        //assertions
        BankModel bankModel = service.getBankModel(bic);
        assertNull(bankModel);
    }

    @Test
    void bankModelExist() {

        //add
        String bic = "1234";
        {
            BankModel bankModel1 = new BankModel(bic, "Royal Bank of Boredom", "GB", "OAUTH");
            service.addBankModel(bankModel1);
        }

        //aservice, ssertions
        assertEquals(service.bankModelExist(bic), true);

    }
}