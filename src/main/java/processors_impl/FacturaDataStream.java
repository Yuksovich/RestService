package processors_impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipInputStream;

import org.springframework.http.client.ClientHttpResponse;

import processors.DataStream;
import util.Request;

public final class FacturaDataStream implements DataStream {

	private final int id;
	private final int versionOfBD;
	private InputStream inputStream;
	private final static String UNFORMATTED_REQUEST =
			"https://demo.faktura.ru/mobws/3.0/json/objectsLocationUpdate?bankId=%d&versionOfBD=%d&sure=true";

	private FacturaDataStream(final int id, final int versionOfBD) {
		this.id = id;
		this.versionOfBD = versionOfBD;
	}

	public static DataStream getInstance(final int id, final int versionOfBD) {
		return new FacturaDataStream(id, versionOfBD);
	}

	@Override
	public void close() throws IOException {
		if (inputStream != null) {
			inputStream.close();
		}
	}

	@Override
	public InputStream getDataInputStream() throws IOException {
		
		final ClientHttpResponse response = Request.execute(String.format(UNFORMATTED_REQUEST, id, versionOfBD));
		inputStream = response.getBody();
		final ZipInputStream zipInputStream = new ZipInputStream(inputStream);
		zipInputStream.getNextEntry();

		return zipInputStream;
	}

}
