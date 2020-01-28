package ru.cbr.rrror.service.gateway.config;

import org.junit.Assert;
import org.junit.Test;

import java.net.URI;

public class EntityExtractorFunctionUnitTest {

    @Test
    public void extractOnDeleteOrUpdateTest() {
        ZuulFilterConfig.DbServiceZuulFilterAction.EntityExtractorFunction f = new ZuulFilterConfig.DbServiceZuulFilterAction.EntityExtractorFunction() {};
        String actual = f.get().apply(URI.create("/db-service/api/users/5"));
        Assert.assertEquals("User", actual);
    }

    @Test
    public void extractOnCreateTest() {
        ZuulFilterConfig.DbServiceZuulFilterAction.EntityExtractorFunction f = new ZuulFilterConfig.DbServiceZuulFilterAction.EntityExtractorFunction() {};
        String actual = f.get().apply(URI.create("/db-service/api/users"));
        Assert.assertEquals("User", actual);
    }
}
