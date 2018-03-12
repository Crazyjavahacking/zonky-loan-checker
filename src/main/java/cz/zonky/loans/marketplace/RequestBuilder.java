package cz.zonky.loans.marketplace;

import org.springframework.web.util.UriComponentsBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RequestBuilder {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    public static String build(String endpointUrl, Date fromDate) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(endpointUrl)
                .queryParam("fields", "id,datePublished,name")
                .queryParam("datePublished__gt", dateFormat.format(fromDate));

        return builder.toUriString();
    }
}
