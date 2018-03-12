package cz.zonky.loans.marketplace;

import org.junit.Assert;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RequestBuilderTest {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    @Test
    public void testBuild() {
        Date date = new Date();
        String request = RequestBuilder.build("http://localhost/", date);

        Assert.assertEquals("http://localhost/?fields=id,datePublished,name&datePublished__gt=" + dateFormat.format(date), request);
    }
}