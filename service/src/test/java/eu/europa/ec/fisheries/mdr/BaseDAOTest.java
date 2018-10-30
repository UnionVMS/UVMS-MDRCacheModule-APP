package eu.europa.ec.fisheries.mdr;

import com.ninja_squad.dbsetup.DbSetupTracker;
import org.h2.jdbcx.JdbcConnectionPool;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;

public abstract class BaseDAOTest {

    private static final Logger log = LoggerFactory.getLogger(BaseDAOTest.class);
    protected static DbSetupTracker dbSetupTracker = new DbSetupTracker();
    private final String TEST_DB_USER = "sa";
    private final String TEST_DB_PASSWORD = "";
    private final String TEST_DB_URL = "jdbc:h2:mem:testdb;LOCK_MODE=0;INIT=CREATE SCHEMA IF NOT EXISTS " + this.getSchema() + ";DATABASE_TO_UPPER=false;TRACE_LEVEL_SYSTEM_OUT=0";
    protected DataSource ds;
    protected EntityManager em;

    public BaseDAOTest() {
        this.ds = JdbcConnectionPool.create(this.TEST_DB_URL, "sa", "");
        try {
            log.info("BuildingEntityManager for unit tests");
            EntityManagerFactory emFactory = Persistence.createEntityManagerFactory(this.getPersistenceUnitName());
            this.em = emFactory.createEntityManager();
        } catch (Throwable var2) {
            System.out.println("Error while creating the entity manager {}" + var2);
            log.error("Error while creating spatial or EntityManagerFactory in BaseDAOTest", var2);
        }
    }



    protected abstract String getSchema();

    protected abstract String getPersistenceUnitName();
}