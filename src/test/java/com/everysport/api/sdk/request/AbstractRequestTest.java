package com.everysport.api.sdk.request;

import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class AbstractRequestTest {

    @Test
    public void properArgumentTest() {

        ConcreteClass cc = new ConcreteClass();
        cc.setPath("/path");
        cc.setProperty("Path");
        cc.addParam("customParam", 1);
        cc.addParam("customListParam", new String[]{"1", "second", "third"});
        cc.setType(ConcreteClass.class);
        cc.limit(100);
        cc.offset(50);
        cc.sort("s1", "s2:desc");
        cc.sport(1,2,3);
        cc.league(10, 20, 30);

        assertEquals("/path", cc.getPath());
        assertEquals("Path", cc.getProperty());
        assertEquals("1", cc.getParam("customParam"));
        assertEquals("1,second,third", cc.getParam("customListParam"));
        assertSame(ConcreteClass.class, cc.getType());
        assertEquals("100", cc.getParam("limit"));
        assertEquals("50", cc.getParam("offset"));
        assertEquals("s1,s2:desc", cc.getParam("sort"));
        assertEquals("1,2,3", cc.getParam("sport"));
        assertEquals("10,20,30", cc.getParam("league"));

    }

    @Test
    public void generateUri() {

        ConcreteClass cc = new ConcreteClass();

        Map<String, String> params = new TreeMap();
        params.put("limit", "10");
        params.put("offset", "5");

        cc.setParams(params);
        cc.setPath("/path");

        assertEquals("/path?limit=10&offset=5", cc.generateUri());

    }

    @Test(expected = IllegalArgumentException.class)
    public void assertEqualOrGreaterThan_shouldThrowException() {
        ConcreteClass cc = new ConcreteClass();
        cc.assertEqualOrGreaterThan(1, "limit", 0);
    }

    @Test
    public void assertEqualOrGreaterThan_shouldSucceed() {
        ConcreteClass cc = new ConcreteClass();
        cc.assertEqualOrGreaterThan(1, "limit", 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void limitAssertEqualOrGreaterThan1() {
        ConcreteClass cc = new ConcreteClass();
        assertSame(cc, cc.limit(1));
        assertSame(cc, cc.limit(50));
        cc.limit(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void offsetAssertEqualOrGreaterThan0() {
        ConcreteClass cc = new ConcreteClass();
        assertSame(cc, cc.offset(0));
        assertSame(cc, cc.offset(100));
        cc.offset(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void sportAssertEqualOrGreaterThan1() {
        ConcreteClass cc = new ConcreteClass();
        assertSame(cc, cc.sport(1));
        assertSame(cc, cc.sport(50));
        cc.sport(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void leagueAssertEqualOrGreaterThan1() {
        ConcreteClass cc = new ConcreteClass();
        assertSame(cc, cc.league(1));
        assertSame(cc, cc.league(50));
        cc.league(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void teamAssertEqualOrGreaterThan1() {
        ConcreteClass cc = new ConcreteClass();
        assertSame(cc, cc.team(1));
        assertSame(cc, cc.team(50));
        cc.team(0);
    }

    /* Only for Tests */
    private class ConcreteClass extends AbstractRequest<ConcreteClass> {}

}
