package eu.europa.ec.fisheries.mdr.qualifiers;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Qualifier
@Target(ElementType.TYPE)
@Retention(RUNTIME)
public @interface MDRMapper {
    
    String value();

    class MDRMapperImpl extends AnnotationLiteral<MDRMapper>
            implements MDRMapper {
        
        private final String value;

        public MDRMapperImpl(final String value) {
            super();
            this.value = value;
        }

        @Override
        public final String value() {
            return value;
        }
    }
}
