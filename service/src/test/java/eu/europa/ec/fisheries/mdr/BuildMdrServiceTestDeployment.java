package eu.europa.ec.fisheries.mdr;

import org.eu.ingwar.tools.arquillian.extension.suite.annotations.ArquillianSuiteDeployment;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

import java.io.File;

@ArquillianSuiteDeployment
public class BuildMdrServiceTestDeployment {

    @Deployment(name = "mdr")
    public static Archive<?> createDeployment() {
        WebArchive testWar = ShrinkWrap.create(WebArchive.class, "mdr.war");

        File[] files = Maven
                .resolver()
                .loadPomFromFile("pom.xml")
                .importRuntimeAndTestDependencies()
                .resolve()
                .withTransitivity()
                .asFile();

        testWar.addAsLibraries(files);

        testWar.addPackages(true, "eu.europa.ec.fisheries.mdr", "eu.europa.ec.fisheries.uvms.mdr.message");

        testWar.addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml");
        testWar.addAsResource("beans.xml", "META-INF/beans.xml");

        return testWar;
    }
}
