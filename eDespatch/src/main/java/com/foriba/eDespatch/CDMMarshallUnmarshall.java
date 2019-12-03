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

import com.foriba.edespatch.InvoiceListType;

import oasis.names.specification.ubl.schema.xsd.invoice_2.InvoiceType;

public class CDMMarshallUnmarshall {

	private static JAXBContext ctx = null;

	private static synchronized JAXBContext getContext() throws JAXBException {
		if (ctx == null) {
			ctx = JAXBContext.newInstance(InvoiceListType.class, InvoiceType.class);
		}
		return ctx;
	}

	public static <T> T unmarshallCDM(Class<T> t, String inputXML) throws JAXBException, ClassNotFoundException {
		StringReader sr = null;
		try {
			Unmarshaller um = getContext().createUnmarshaller();
			sr = new StringReader(inputXML);
			Source src = new StreamSource(sr);
			JAXBElement<T> jaxbElement = um.unmarshal(src, t);
			return jaxbElement.getValue();
		} finally {
			UtilsIO.closeStream(sr);
		}
	}

	public static <T> String marshallCDM(T t) throws JAXBException {

		StringWriter sw = null;
		try {
			Marshaller m = getContext().createMarshaller();
			m.setProperty(Marshaller.JAXB_ENCODING, ParamRuntime.UTF8_CHARSET);
			sw = new StringWriter();
			m.marshal(t, sw);

			return sw.toString();
		}
		finally {
			UtilsIO.closeStream(sw);
		}
	}
}
