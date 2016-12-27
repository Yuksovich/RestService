package controller;

import java.util.concurrent.Callable;

import processors.DataParser;
import processors.DataStream;
import processors.CacheAdapter;
import util.Cache;

final class Task<T> implements Callable<T> {

	private final DataStream dataStream;
	private final DataParser<T> dataParser;
	private final String bankId;

	Task(final DataStream dataStream, final DataParser<T> dataParser, final String bankId) {
		if (dataParser == null || dataStream == null) {
			throw new NullPointerException("DataParser: " + dataParser + "\nDataStream: " + dataStream + "\nmust be non null");
		}
		this.dataStream = dataStream;
		this.dataParser = dataParser;
		this.bankId = bankId;
	}

	@Override
	@SuppressWarnings("unchecked")
	public T call() throws Exception {
		try{
			final CacheAdapter cache = Cache.getInstance();
			T result = (T)cache.get(bankId);
			if(result!=null){
				return result;
			}
			result = dataParser.parse(dataStream.getDataInputStream(), bankId);
			cache.put(bankId, result);
			return result;
		}
		finally{
			dataStream.close();
		}
	}

}
