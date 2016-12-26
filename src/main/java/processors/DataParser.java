package processors;

import java.io.InputStream;

public interface DataParser<T> {
	T parse(InputStream inputStream, String bankId) throws Exception;
}
