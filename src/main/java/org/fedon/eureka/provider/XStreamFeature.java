package org.fedon.eureka.provider;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

/**
 * @author Dmytro Fedonin
 *
 */
public class XStreamFeature implements Feature {

    @Override
    public boolean configure(FeatureContext context) {
        context.register(XStreamProvider.class, MessageBodyReader.class, MessageBodyWriter.class);
        return true;
    }
}
