package util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

public final class Request {
	private Request(){
		throw new UnsupportedOperationException();
	}

	public static ClientHttpResponse execute(final String request) throws IOException {
		if (request == null) {
			throw new NullPointerException("Request is null");
		}
		final SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		ClientHttpRequest httpRequest;
		try {
			httpRequest = requestFactory.createRequest(new URI(request), HttpMethod.POST);
		} catch (URISyntaxException e) {
			throw new IOException("Incorrect request: " + request);
		}
		return httpRequest.execute();
	}

}
