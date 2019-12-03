package com.foriba.eDespatch;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;



public class GenericMarshallUnmarshall {

	public static <T> T unmarshall(Class<T> t, String inputXML) throws JAXBException, ClassNotFoundException {
		StringReader sr = null;
		try {
			JAXBContext jc = JAXBContext.newInstance(Class.forName(t.getName()));
			Unmarshaller um = jc.createUnmarshaller();
			sr = new StringReader(inputXML);
			Source src = new StreamSource(sr);
			JAXBElement<T> jaxbElement = um.unmarshal(src, t);
			return jaxbElement.getValue();
		} finally {
			UtilsIO.closeStream(sr);
		}
	}

	public static <T> String marshall(T t) throws JAXBException {
		StringWriter sw = null;
		try {
			JAXBContext jc = JAXBContext.newInstance(t.getClass());
			Marshaller m = jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_ENCODING, ParamRuntime.UTF8_CHARSET);
			sw = new StringWriter();
			m.marshal(t, sw);
			return sw.toString();
		} finally {
			UtilsIO.closeStream(sw);
		}
	}
}
