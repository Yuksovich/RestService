package processors;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public interface DataStream extends Closeable, AutoCloseable{
	InputStream getDataInputStream() throws IOException;
}
