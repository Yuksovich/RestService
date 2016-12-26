package processors_impl;

import org.springframework.http.client.ClientHttpResponse;
import processors.DataParser;
import util.Request;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.stream.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FacturaDataParser implements DataParser<Map<String, String>> {
    private FacturaDataParser() {
    }

    private static final String UNFORMATTED_REQUEST_TO_FACTURA = "https://demo.faktura.ru/mobws/3.0/json/getBankPOIDescription?objectUiid=%s";
    private static final String NO_DESCRIPTION = "no description";

    private enum Tags {
        BANK_ID("bankBic"),
        UIID("uiid"),
        DESCRIPTION("description"),
        RESPONSE("response"),
        OBJECT("object");

        private final String tagName;

        Tags(final String tagName) {
            this.tagName = tagName;
        }

        @Override
        public String toString() {
            return tagName;
        }
    }

    public static DataParser<Map<String, String>> getInstance() {
        return new FacturaDataParser();
    }

    @Override
    public Map<String, String> parse(final InputStream inputStream, final String bankId) throws Exception {
        if (inputStream == null) {
            return null;
        }
        final JsonParser parser = Json.createParser(inputStream);
        if (isBankPresent(parser, bankId)) {
            final List<String> uiidList = getUiidAraay(parser);
            return getResponse(uiidList);
        } else {
            return null;
        }
    }

    private boolean isBankPresent(final JsonParser parser, final String bankId) {
        JsonParser.Event event;
        while (parser.hasNext()) {
            event = parser.next();
            if (event.equals(JsonParser.Event.KEY_NAME) && Tags.BANK_ID.toString().equals(parser.getString())) {
                event = parser.next();
                if (event.equals(JsonParser.Event.VALUE_STRING) && bankId.equals(parser.getString())) {
                    return true;
                }
            }
        }
        return false;
    }

    private ArrayList<String> getUiidAraay(final JsonParser parser) {
        final ArrayList<String> uiidList = new ArrayList<>();
        JsonParser.Event event;
        while (parser.hasNext()) {
            event = parser.next();
            if (event.equals(JsonParser.Event.END_OBJECT)) {
                return uiidList;
            }
            if (event.equals(JsonParser.Event.START_ARRAY)) {
                while (!event.equals(JsonParser.Event.END_ARRAY)) {
                    event = parser.next();
                    if (event.equals(JsonParser.Event.KEY_NAME) && parser.getString().equals(Tags.UIID.toString())) {
                        if (parser.next().equals(JsonParser.Event.VALUE_STRING)) {
                            uiidList.add(parser.getString());
                        }
                    }
                }
            }
        }
        return uiidList;
    }

    private Map<String, String> getResponse(final List<String> uiidList) throws IOException {


        Map<String, String> map = new HashMap<>();
        ClientHttpResponse response = null;
        InputStream inputStream = null;

        for (String uiid : uiidList) {
            try {
                response = Request.execute(String.format(UNFORMATTED_REQUEST_TO_FACTURA, uiid));
                inputStream = response.getBody();

                map.put(uiid, getDescription(inputStream));
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (response != null) {
                    response.close();
                }
            }
        }

        return map;
    }

    private String getDescription(final InputStream inputStream) {
        final JsonObject jsonObject = Json.createReader(inputStream).readObject()
                .getJsonObject(Tags.RESPONSE.toString()).getJsonObject(Tags.OBJECT.toString());
        if (jsonObject.isNull(Tags.DESCRIPTION.toString())) {
            return NO_DESCRIPTION;
        } else {
            return jsonObject.getString(Tags.DESCRIPTION.toString());
        }
    }

}
