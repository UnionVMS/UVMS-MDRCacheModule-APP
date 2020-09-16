package eu.europa.ec.fisheries.mdr.qualifiers;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Qualifier
@Target(ElementType.TYPE)
@Retention(RUNTIME)
public @interface MDRSync {
    
    String value();

    class MDRSyncImpl extends AnnotationLiteral<MDRSync>
            implements MDRSync {

        public static final String ORACLE_DB = "oracle_db";
        public static final String WEBSERVICE = "webservice";
        
        private final String value;

        public MDRSyncImpl(final String value) {
            super();
            this.value = value;
        }

        @Override
        public final String value() {
            return value;
        }
    }
}
