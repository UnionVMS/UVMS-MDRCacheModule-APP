package eu.europa.ec.fisheries.mdr;

import org.junit.After;
import org.junit.Before;

import javax.inject.Inject;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

public class TransactionalTests extends BuildMdrServiceTestDeployment {

    @Inject
    private UserTransaction userTransaction;

    @Before
    public void before() throws SystemException, NotSupportedException {
        userTransaction.begin();
    }

    @After
    public void after() throws SystemException {
        userTransaction.rollback();
    }
}
