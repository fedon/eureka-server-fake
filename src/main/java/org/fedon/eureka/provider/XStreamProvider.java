package org.fedon.eureka.provider;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.converters.Converters;
import com.netflix.discovery.shared.Application;
import com.netflix.discovery.shared.Applications;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

/**
 * @author Dmytro Fedonin
 * 
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class XStreamProvider<T> implements MessageBodyReader<T>, MessageBodyWriter<T> {
    private final Log log = LogFactory.getLog(getClass());

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotas, MediaType mediaType) {
        boolean isAnnotated = false;
        log.debug(type.getCanonicalName());
        Annotation[] annots = type.getAnnotations();
        for (int i = 0; i < annots.length; i++) {
            if (annots[i] instanceof XStreamAlias) {
                isAnnotated = true;
                break;
            }
        }
        if (isAnnotated && MediaType.APPLICATION_JSON_TYPE.equals(mediaType)) {
            return true;
        }
        return false;
    }

    @Override
    public long getSize(T t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(T t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        XStream xstream = new XStream(new JettisonMappedXmlDriver());
        String alias = "";
        Annotation[] annots = type.getAnnotations();
        for (int i = 0; i < annots.length; i++) {
            if (annots[i] instanceof XStreamAlias) {
                alias = ((XStreamAlias) annots[i]).value();
                xstream.alias(alias, type);
                break;
            }
        }
        xstream.registerConverter(new Converters.ApplicationConverter());
        xstream.registerConverter(new Converters.ApplicationsConverter());
        xstream.registerConverter(new Converters.DataCenterInfoConverter());
        xstream.registerConverter(new Converters.InstanceInfoConverter());
        xstream.registerConverter(new Converters.LeaseInfoConverter());
        xstream.registerConverter(new Converters.MetadataConverter());
        xstream.setMode(XStream.NO_REFERENCES);
        xstream.processAnnotations(new Class[] { InstanceInfo.class, Application.class, Applications.class });
        entityStream.write(xstream.toXML(t).getBytes());
    }

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        boolean isAnnotated = false;
        log.debug(type.getCanonicalName());
        Annotation[] annots = type.getAnnotations();
        for (int i = 0; i < annots.length; i++) {
            if (annots[i] instanceof XStreamAlias) {
                isAnnotated = true;
                break;
            }
        }
        if (isAnnotated && MediaType.APPLICATION_JSON_TYPE.equals(mediaType)) {
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T readFrom(Class<T> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders,
            InputStream entityStream) throws IOException, WebApplicationException {
        XStream xstream = new XStream(new JettisonMappedXmlDriver());
        String alias = "";
        Annotation[] annots = type.getAnnotations();
        for (int i = 0; i < annots.length; i++) {
            if (annots[i] instanceof XStreamAlias) {
                alias = ((XStreamAlias) annots[i]).value();
                xstream.alias(alias, type);
                break;
            }
        }
        xstream.registerConverter(new Converters.ApplicationConverter());
        xstream.registerConverter(new Converters.ApplicationsConverter());
        xstream.registerConverter(new Converters.DataCenterInfoConverter());
        xstream.registerConverter(new Converters.InstanceInfoConverter());
        xstream.registerConverter(new Converters.LeaseInfoConverter());
        xstream.registerConverter(new Converters.MetadataConverter());
        xstream.setMode(XStream.NO_REFERENCES);
        xstream.processAnnotations(new Class[] { InstanceInfo.class, Application.class, Applications.class });
        // InputStreamReader sr = new InputStreamReader(entityStream);
        // StringBuilder sb = new StringBuilder();
        // while (sr.ready()) {
        // int val = sr.read();
        // if (val < 0) {
        // break;
        // }
        // sb.append((char) val);
        // }
        // log.info("-------------------\ninput: " + sb.toString());
        // return (T) xstream.fromXML(new ByteArrayInputStream(sb.toString().getBytes()));
        return (T) xstream.fromXML(entityStream);
    }
}
