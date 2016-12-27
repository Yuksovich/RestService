package controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import processors_impl.FacturaDataParser;
import processors_impl.FacturaDataStream;
import util.ThreadPool;

import java.util.Map;

@RestController
final class ServiceController {

    @RequestMapping("/process")
    Map<String, String> process(@RequestParam(value = "id", defaultValue = "0") final String id,
                                @RequestParam(value = "versionOfBD", defaultValue = "1") final int versionOfBD) {

        Task<Map<String, String>> task = new Task<>(
                FacturaDataStream.getInstance(0, versionOfBD),
                FacturaDataParser.getInstance(), id);
        try {
            return ThreadPool.INSTANCE.submit(task).get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
