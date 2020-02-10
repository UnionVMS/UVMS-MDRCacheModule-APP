package eu.europa.ec.fisheries.mdr.entities.codelists.ers;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleField;
import org.apache.lucene.document.Field;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;

public class CoreDoubleBridge implements FieldBridge {

    @Override
    public void set(String s, Object o, Document document, LuceneOptions luceneOptions) {
            DoubleField df = new DoubleField(s,(Double) o,Field.Store.NO);
            document.add(df);
    }

}
