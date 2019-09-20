package eu.europa.ec.fisheries.mdr.repository;

import eu.europa.ec.fisheries.mdr.BuildMdrServiceTestDeployment;
import eu.europa.ec.fisheries.mdr.entities.codelists.ers.FaoSpecies;
import lombok.SneakyThrows;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by georgige on 11/15/2016.
 */
@RunWith(Arquillian.class)
public class MdrLuceneSearchRepositoryBeanTest extends BuildMdrServiceTestDeployment {

    @Inject
    private MdrRepository mdrRepository;

    @Inject
    private MdrLuceneSearchRepository mdrLuceneSearchRepository;

    private static final String CODE = "code";

    @Before
    @SneakyThrows
    public void prepare() {
        List<FaoSpecies> faoSpecies = mockSpecies();
        mdrRepository.insertNewData(faoSpecies);
    }

    @Test
    @SneakyThrows
    public void testLuceneIndexingNoSearchFilters() {
        List<FaoSpecies> species = mockSpecies();
        try {
            mdrLuceneSearchRepository.findCodeListItemsByAcronymAndFilter(species.get(0).getAcronym(), 0, 5, CODE, false, null, null);
            fail("ServiceException was expected but not thrown.");
        } catch (Exception exc) {
            assertTrue(exc.getCause() instanceof IllegalArgumentException);
            assertEquals("No search attributes are provided.", exc.getMessage());
        }
    }

    @Test
    @SneakyThrows
    public void testLuceneSearch() {
        List<FaoSpecies> species = mockSpecies();

        List<FaoSpecies> filterredEntities = (List<FaoSpecies>) mdrLuceneSearchRepository.findCodeListItemsByAcronymAndFilter(
                species.get(0).getAcronym(),
                0,
                5,
                "taxo_code",
                true,
                "*",
                "taxo_code");

        assertEquals(3, filterredEntities.size());
        assertEquals("WHL", filterredEntities.get(0).getTaxoCode());
        assertEquals("COD", filterredEntities.get(1).getTaxoCode());
        assertEquals("CAT", filterredEntities.get(2).getTaxoCode());
    }

    @Test
    @SneakyThrows
    public void testLuceneSearchOnMultipleFields() {
        List<FaoSpecies> species = mockSpecies();

        String[] fields= {"taxo_code", "description"};
        final String filterText = "*whl";
        final String filterText_2 = "c*";

        List<FaoSpecies> filterredEntities = (List<FaoSpecies>) mdrLuceneSearchRepository.findCodeListItemsByAcronymAndFilter(species.get(0).getAcronym(),
                0, 5, "taxo_code", true, filterText, fields);

        List<FaoSpecies> filterredEntities_2 = (List<FaoSpecies>) mdrLuceneSearchRepository.findCodeListItemsByAcronymAndFilter(species.get(0).getAcronym(),
                0, 5, "taxo_code", true, filterText_2, fields);

        assertEquals(1, filterredEntities.size());
        assertEquals("WHL", filterredEntities.get(0).getTaxoCode());

        assertEquals(2, filterredEntities_2.size());
    }

    @Test
    @SneakyThrows
    public void testLuceneSearchCount() {
        List<FaoSpecies> species = mockSpecies();
        int totalCount=  mdrLuceneSearchRepository.countCodeListItemsByAcronymAndFilter(species.get(0).getAcronym(), "c*", "taxo_code");
        assertEquals(2, totalCount);
    }

    private List<FaoSpecies> mockSpecies() {
        List<FaoSpecies> species = new ArrayList<>(2);
        FaoSpecies species1 = new FaoSpecies();
        species1.setTaxoCode("COD");
        species1.setEnName("COD fish");
        FaoSpecies species2 = new FaoSpecies();
        species2.setTaxoCode("CAT");
        species2.setEnName("CAT fish");
        FaoSpecies species3 = new FaoSpecies();
        species3.setTaxoCode("WHL");
        species3.setEnName("Whale");
        species.add(species1);
        species.add(species2);
        species.add(species3);
        return species;
    }

}
