package cz.zonky.loans.marketplace;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.zonky.loans.marketplace.response.Loan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Service
public class LoanDownloader {
    @Value("${zonky.market_url}")
    private String endpointUrl;

    private RestTemplate client;

    private final Integer limit = 20;
    private Integer page = 0;

    private List<Loan> loans = new ArrayList<Loan>();

    @Autowired
    public LoanDownloader(RestTemplateBuilder restTemplateBuilder) {
        this.client = restTemplateBuilder.build();
    }

    public List<Loan> downloadLoans(Date fromDate) throws MarketplaceConnectionException {
        getLoans(fromDate);

        return loans;
    }

    private void getLoans(Date fromDate) {
        try {
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.add("X-Size", limit.toString());
            requestHeaders.add("X-Page", page.toString());

            ResponseEntity<Loan[]> response = client.exchange(RequestBuilder.build(endpointUrl, fromDate), HttpMethod.GET, new HttpEntity<Void>(requestHeaders), Loan[].class);

            loans.addAll(Arrays.asList(response.getBody()));

            if (response.getStatusCode().value() == 200) {

                HttpHeaders headers = response.getHeaders();

                Integer total = new Integer(headers.get("X-Total").get(0));

                // check for more pages
                if (total > (limit * (page + 1))) {
                    page++;
                    downloadLoans(fromDate);
                }
            } else {
                throw new MarketplaceConnectionException(String.format("Unable to download data from Zonky marketplace. Code %d returned", response.getStatusCodeValue()));
            }
        } catch (RestClientException e) {
            throw new MarketplaceConnectionException(String.format("Unable to call Zonky marketplace: %s", e.getMessage()));
        }
    }
}
