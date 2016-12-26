package model;

import java.util.concurrent.Callable;

import processors.DataParser;
import processors.DataStream;

public class Task<T> implements Callable<T> {

	private final DataStream dataStream;
	private final DataParser<T> dataParser;
	private final String bankId;

	public Task(final DataStream dataStream, final DataParser<T> dataParser, final String bankId) {
		if (dataParser == null || dataStream == null) {
			throw new NullPointerException("DataParser: " + dataParser + "\nDataStream: " + dataStream + "\nmust be non null");
		}
		this.dataStream = dataStream;
		this.dataParser = dataParser;
		this.bankId = bankId;
	}

	@Override
	public T call() throws Exception {
		try{
			return dataParser.parse(dataStream.getDataInputStream(), bankId);
		}finally{
			dataStream.close();
		}
	}

}
