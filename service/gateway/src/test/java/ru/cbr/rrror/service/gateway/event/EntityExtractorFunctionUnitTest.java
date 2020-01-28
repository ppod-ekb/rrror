package ru.cbr.rrror.service.gateway.event;

import org.junit.Assert;
import org.junit.Test;

import java.net.URI;

public class EntityExtractorFunctionUnitTest {

    @Test
    public void extractOnDeleteOrUpdateTest() {
        DbServiceZuulFilterAction.EntityExtractorFunction f = new DbServiceZuulFilterAction.EntityExtractorFunction() {};
        String actual = f.get().apply(URI.create("/db-service/api/users/5"));
        Assert.assertEquals("User", actual);
    }

    @Test
    public void extractOnCreateTest() {
        DbServiceZuulFilterAction.EntityExtractorFunction f = new DbServiceZuulFilterAction.EntityExtractorFunction() {};
        String actual = f.get().apply(URI.create("/db-service/api/users"));
        Assert.assertEquals("User", actual);
    }
}
