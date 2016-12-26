package controller;

import javax.json.JsonObject;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import model.Task;
import processors_impl.FacturaDataParser;
import processors_impl.FacturaDataStream;
import util.ThreadPool;

@RestController
public class ServiceController {

	@RequestMapping("/process")
	public JsonObject process(@RequestParam(value = "id", defaultValue = "0") final String id,
			@RequestParam(value = "versionOfBD", defaultValue = "1") final int versionOfBD) {

		Task<JsonObject> task = new Task<>(FacturaDataStream.getInstance(0, versionOfBD),
				FacturaDataParser.getInstance(), id);
		try {
			return ThreadPool.INSTANCE.submit(task).get();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
}
