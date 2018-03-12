package cz.zonky.loans.marketplace;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import cz.zonky.loans.marketplace.response.Loan;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import java.util.Date;

import static org.junit.Assert.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@RestClientTest(LoanDownloader.class)
public class LoanDownloaderTest {

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private LoanDownloader loanDownloader;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${zonky.market_url}")
    private String endpointUrl;

    private Date date;

    private Loan[] mockResponse = { new Loan(1, "First"), new Loan(2, "Second") };

    @Before
    public void setUp() throws JsonProcessingException {
        this.date = new Date();

        String mockResult = objectMapper.writeValueAsString(mockResponse);

        HttpHeaders mockHeaders = new HttpHeaders();
        mockHeaders.add("X-Total", "2");
        this.server.expect(requestTo(RequestBuilder.build(endpointUrl, date)))
                .andRespond(withSuccess(mockResult, MediaType.APPLICATION_JSON).headers(mockHeaders));
    }

    @Test
    public void testDownloadSuccess() {
        try {
            List<Loan> loans = loanDownloader.downloadLoans(date);

            assertEquals(1, loans.get(0).getId());
            assertEquals("First", loans.get(0).getName());

            assertEquals(2, loans.get(1).getId());
            assertEquals("Second", loans.get(1).getName());
        } catch (MarketplaceConnectionException e) {
            fail();
        }
    }

}