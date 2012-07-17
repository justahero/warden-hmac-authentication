package com.asquera.hmac;

import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RequestInfoTest {
    
    private Map<String, Object> options;
    
    @Before
    public void setUp() {
        options = new HashMap<String, Object>();
    }
    
    @Test
    public void constructorThrowsExceptionWhenMapIsNull() throws URISyntaxException {
        try {
            new RequestInfo("http://www.example.com", null);
            Assert.fail("Must throw exception when map is null");
        } catch (IllegalArgumentException e) {
        }
    }
    
    @Test
    public void constructorThrowsExceptionWhenNoUrl() throws URISyntaxException {
        try {
            new RequestInfo(null, options);
            Assert.fail("Must throw exception when url is null");
        } catch (IllegalArgumentException e) {
        }
    }
    
    @Test
    public void pathFromUrlWithoutQuery() throws URISyntaxException {
        RequestInfo info = new RequestInfo("http://www.example.com", options);
        String actualPath = info.path();
        Assert.assertEquals("", actualPath);
    }
    
    @Test
    public void pathFromUrlWithPath() throws URISyntaxException {
        RequestInfo info = new RequestInfo("http://www.example.com/images/test.png", options);
        String actualPath = info.path();
        Assert.assertEquals("/images/test.png", actualPath);
    }
    
    @Test
    public void pathFromUrlWithQuery() throws URISyntaxException {
        RequestInfo info = new RequestInfo("http://www.test.com/blog?key=value&test=true", options);
        String actualPath = info.path();
        Assert.assertEquals("/blog", actualPath);
    }
    
    @Test
    public void dateAsStringGetsFormatted() throws Exception {
        options.put("date", "15 01 2012 16:43:21");
        
        RequestInfo request = new RequestInfo("http://example.com/test", options);
        Assert.assertEquals("SUN, 15 01 2012 16:43:21 GMT", request.date());
    }
    
    @Test
    public void dateAsDateGetsFormatted() throws Exception {
        Calendar calendar = new GregorianCalendar();
        calendar.set(2012, 06, 17, 10, 20, 30);
        Date date = calendar.getTime();
        options.put("date", date);
        
        RequestInfo request = new RequestInfo("http://example.com", options);
        String actualDate = request.date();
        Assert.assertEquals("TUE, 17 07 2012 10:20:30 GMT", actualDate);
    }
    
    @Test
    public void queryStringIsEmptyWhenNoQueryGiven() throws URISyntaxException {
        RequestInfo request = new RequestInfo("http://www.example.com", options);
        Assert.assertEquals("", request.sortedQuery());
    }
    
    @Test
    public void queryStringReturnsSingleEntryFromQuery() throws URISyntaxException {
        RequestInfo request = new RequestInfo("http://www.example.com/test?key=value", options);
        String actualQuery = request.sortedQuery();
        Assert.assertEquals("key=value", actualQuery);
    }
    
    @Test
    public void queryStringsSortsEntries() throws URISyntaxException {
        RequestInfo request = new RequestInfo("http://www.example.com/test?value=1&temp=2", options);
        Assert.assertEquals("temp=2&value=1", request.sortedQuery());
    }
    
    @Test
    public void queryStringsSortsEntriesWithSameKey() throws Exception {
        RequestInfo request = new RequestInfo("http://www.example.com/test?value=def&value=abc", options);
        Assert.assertEquals("value=abc&value=def", request.sortedQuery());
    }
}
