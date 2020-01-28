package ru.cbr.rrror.service.gateway.config;

import org.junit.Assert;
import org.junit.Test;

import static ru.cbr.rrror.service.gateway.config.ZuulFilterConfig.DbServiceZuulFilterAction.HalHrefModifier;

public class DbServiceZuulFilterActionUnitTest {

    static class Logger {
        public static void debug(Object o) {
            System.out.println(">>> " + o);
        }
    }

    @Test
    public void testOfTest() {
        Logger.debug("run test: testOfTest" );
        Assert.assertTrue(true);
    }

    @Test
    public void halLinkModifierTest() {
        String expected = "{\n" +
                "  \"login\" : \"test1\",\n" +
                "  \"firstName\" : \"Андрей Воробей\",\n" +
                "  \"lastName\" : \"test3\",\n" +
                "  \"description\" : \"tet4\",\n" +
                "  \"_links\" : {\n" +
                "    \"self\" : {\n" +
                "      \"href\" : \"http://localhost:8080/db-service/api/users/12\"\n" +
                "    },\n" +
                "    \"user\" : {\n" +
                "      \"href\" : \"http://localhost:8080/db-service/api/users/12\"\n" +
                "    }\n" +
                "  }\n" +
                "}";

        HalHrefModifier m = new HalHrefModifier(
                "{\n" +
                        "  \"login\" : \"test1\",\n" +
                        "  \"firstName\" : \"Андрей Воробей\",\n" +
                        "  \"lastName\" : \"test3\",\n" +
                        "  \"description\" : \"tet4\",\n" +
                        "  \"_links\" : {\n" +
                        "    \"self\" : {\n" +
                        "      \"href\" : \"http://localhost:8081/api/users/12\"\n" +
                        "    },\n" +
                        "    \"user\" : {\n" +
                        "      \"href\" : \"http://localhost:8081/api/users/12\"\n" +
                        "    }\n" +
                        "  }\n" +
                        "}",

                "http://localhost:8080/db-service"
        ){};

        String actual = m.modify();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void halLinkModifierReplaceAllTest() {
        HalHrefModifier m = new HalHrefModifier(response, "http://localhost:8080/db-service");
        String actual = m.modify();

        Assert.assertEquals(expected, actual);

    }

    private static final String response = "{\n" +
            "  \"_embedded\" : {\n" +
            "    \"users\" : [ {\n" +
            "      \"login\" : \"frodo\",\n" +
            "      \"firstName\" : \"Frodo\",\n" +
            "      \"lastName\" : \"Baggins\",\n" +
            "      \"description\" : \"ring bearer\",\n" +
            "      \"_links\" : {\n" +
            "        \"self\" : {\n" +
            "          \"href\" : \"http://172.17.0.3:8080/api/users/1\"\n" +
            "        },\n" +
            "        \"user\" : {\n" +
            "          \"href\" : \"http://172.17.0.3:8080/api/users/1\"\n" +
            "        }\n" +
            "      }\n" +
            "    }, {\n" +
            "      \"login\" : \"bilbo\",\n" +
            "      \"firstName\" : \"Bilbo\",\n" +
            "      \"lastName\" : \"Baggins\",\n" +
            "      \"description\" : \"burglar\",\n" +
            "      \"_links\" : {\n" +
            "        \"self\" : {\n" +
            "          \"href\" : \"http://172.17.0.3:8080/api/users/2\"\n" +
            "        },\n" +
            "        \"user\" : {\n" +
            "          \"href\" : \"http://172.17.0.3:8080/api/users/2\"\n" +
            "        }\n" +
            "      }\n" +
            "    }, {\n" +
            "      \"login\" : \"gandalf\",\n" +
            "      \"firstName\" : \"Gandalf\",\n" +
            "      \"lastName\" : \"the Grey\",\n" +
            "      \"description\" : \"wizard\",\n" +
            "      \"_links\" : {\n" +
            "        \"self\" : {\n" +
            "          \"href\" : \"http://172.17.0.3:8080/api/users/3\"\n" +
            "        },\n" +
            "        \"user\" : {\n" +
            "          \"href\" : \"http://172.17.0.3:8080/api/users/3\"\n" +
            "        }\n" +
            "      }\n" +
            "    }, {\n" +
            "      \"login\" : \"samwise\",\n" +
            "      \"firstName\" : \"Samwise\",\n" +
            "      \"lastName\" : \"Gamgee\",\n" +
            "      \"description\" : \"gardener\",\n" +
            "      \"_links\" : {\n" +
            "        \"self\" : {\n" +
            "          \"href\" : \"http://172.17.0.3:8080/api/users/4\"\n" +
            "        },\n" +
            "        \"user\" : {\n" +
            "          \"href\" : \"http://172.17.0.3:8080/api/users/4\"\n" +
            "        }\n" +
            "      }\n" +
            "    }, {\n" +
            "      \"login\" : \"meriadoc\",\n" +
            "      \"firstName\" : \"Meriadoc\",\n" +
            "      \"lastName\" : \"Brandybuck\",\n" +
            "      \"description\" : \"pony rider\",\n" +
            "      \"_links\" : {\n" +
            "        \"self\" : {\n" +
            "          \"href\" : \"http://172.17.0.3:8080/api/users/5\"\n" +
            "        },\n" +
            "        \"user\" : {\n" +
            "          \"href\" : \"http://172.17.0.3:8080/api/users/5\"\n" +
            "        }\n" +
            "      }\n" +
            "    }, {\n" +
            "      \"login\" : \"peregrin\",\n" +
            "      \"firstName\" : \"Peregrin\",\n" +
            "      \"lastName\" : \"Took\",\n" +
            "      \"description\" : \"pipe smoker\",\n" +
            "      \"_links\" : {\n" +
            "        \"self\" : {\n" +
            "          \"href\" : \"http://172.17.0.3:8080/api/users/6\"\n" +
            "        },\n" +
            "        \"user\" : {\n" +
            "          \"href\" : \"http://172.17.0.3:8080/api/users/6\"\n" +
            "        }\n" +
            "      }\n" +
            "    }, {\n" +
            "      \"login\" : \"ppod-ekb\",\n" +
            "      \"firstName\" : \"Pppod-ekb\",\n" +
            "      \"lastName\" : \"Pppod-ekb\",\n" +
            "      \"description\" : \"Pppod-ekb developer\",\n" +
            "      \"_links\" : {\n" +
            "        \"self\" : {\n" +
            "          \"href\" : \"http://172.17.0.3:8080/api/users/7\"\n" +
            "        },\n" +
            "        \"user\" : {\n" +
            "          \"href\" : \"http://172.17.0.3:8080/api/users/7\"\n" +
            "        }\n" +
            "      }\n" +
            "    } ]\n" +
            "  },\n" +
            "  \"_links\" : {\n" +
            "    \"self\" : {\n" +
            "      \"href\" : \"http://172.17.0.3:8080/api/users{?page,size,sort}\",\n" +
            "      \"templated\" : true\n" +
            "    },\n" +
            "    \"profile\" : {\n" +
            "      \"href\" : \"http://172.17.0.3:8080/api/profile/users\"\n" +
            "    },\n" +
            "    \"search\" : {\n" +
            "      \"href\" : \"http://172.17.0.3:8080/api/users/search\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"page\" : {\n" +
            "    \"size\" : 20,\n" +
            "    \"totalElements\" : 7,\n" +
            "    \"totalPages\" : 1,\n" +
            "    \"number\" : 0\n" +
            "  }\n" +
            "}";

    private static final String expected = "{\n" +
            "  \"_embedded\" : {\n" +
            "    \"users\" : [ {\n" +
            "      \"login\" : \"frodo\",\n" +
            "      \"firstName\" : \"Frodo\",\n" +
            "      \"lastName\" : \"Baggins\",\n" +
            "      \"description\" : \"ring bearer\",\n" +
            "      \"_links\" : {\n" +
            "        \"self\" : {\n" +
            "          \"href\" : \"http://localhost:8080/db-service/api/users/1\"\n" +
            "        },\n" +
            "        \"user\" : {\n" +
            "          \"href\" : \"http://localhost:8080/db-service/api/users/1\"\n" +
            "        }\n" +
            "      }\n" +
            "    }, {\n" +
            "      \"login\" : \"bilbo\",\n" +
            "      \"firstName\" : \"Bilbo\",\n" +
            "      \"lastName\" : \"Baggins\",\n" +
            "      \"description\" : \"burglar\",\n" +
            "      \"_links\" : {\n" +
            "        \"self\" : {\n" +
            "          \"href\" : \"http://localhost:8080/db-service/api/users/2\"\n" +
            "        },\n" +
            "        \"user\" : {\n" +
            "          \"href\" : \"http://localhost:8080/db-service/api/users/2\"\n" +
            "        }\n" +
            "      }\n" +
            "    }, {\n" +
            "      \"login\" : \"gandalf\",\n" +
            "      \"firstName\" : \"Gandalf\",\n" +
            "      \"lastName\" : \"the Grey\",\n" +
            "      \"description\" : \"wizard\",\n" +
            "      \"_links\" : {\n" +
            "        \"self\" : {\n" +
            "          \"href\" : \"http://localhost:8080/db-service/api/users/3\"\n" +
            "        },\n" +
            "        \"user\" : {\n" +
            "          \"href\" : \"http://localhost:8080/db-service/api/users/3\"\n" +
            "        }\n" +
            "      }\n" +
            "    }, {\n" +
            "      \"login\" : \"samwise\",\n" +
            "      \"firstName\" : \"Samwise\",\n" +
            "      \"lastName\" : \"Gamgee\",\n" +
            "      \"description\" : \"gardener\",\n" +
            "      \"_links\" : {\n" +
            "        \"self\" : {\n" +
            "          \"href\" : \"http://localhost:8080/db-service/api/users/4\"\n" +
            "        },\n" +
            "        \"user\" : {\n" +
            "          \"href\" : \"http://localhost:8080/db-service/api/users/4\"\n" +
            "        }\n" +
            "      }\n" +
            "    }, {\n" +
            "      \"login\" : \"meriadoc\",\n" +
            "      \"firstName\" : \"Meriadoc\",\n" +
            "      \"lastName\" : \"Brandybuck\",\n" +
            "      \"description\" : \"pony rider\",\n" +
            "      \"_links\" : {\n" +
            "        \"self\" : {\n" +
            "          \"href\" : \"http://localhost:8080/db-service/api/users/5\"\n" +
            "        },\n" +
            "        \"user\" : {\n" +
            "          \"href\" : \"http://localhost:8080/db-service/api/users/5\"\n" +
            "        }\n" +
            "      }\n" +
            "    }, {\n" +
            "      \"login\" : \"peregrin\",\n" +
            "      \"firstName\" : \"Peregrin\",\n" +
            "      \"lastName\" : \"Took\",\n" +
            "      \"description\" : \"pipe smoker\",\n" +
            "      \"_links\" : {\n" +
            "        \"self\" : {\n" +
            "          \"href\" : \"http://localhost:8080/db-service/api/users/6\"\n" +
            "        },\n" +
            "        \"user\" : {\n" +
            "          \"href\" : \"http://localhost:8080/db-service/api/users/6\"\n" +
            "        }\n" +
            "      }\n" +
            "    }, {\n" +
            "      \"login\" : \"ppod-ekb\",\n" +
            "      \"firstName\" : \"Pppod-ekb\",\n" +
            "      \"lastName\" : \"Pppod-ekb\",\n" +
            "      \"description\" : \"Pppod-ekb developer\",\n" +
            "      \"_links\" : {\n" +
            "        \"self\" : {\n" +
            "          \"href\" : \"http://localhost:8080/db-service/api/users/7\"\n" +
            "        },\n" +
            "        \"user\" : {\n" +
            "          \"href\" : \"http://localhost:8080/db-service/api/users/7\"\n" +
            "        }\n" +
            "      }\n" +
            "    } ]\n" +
            "  },\n" +
            "  \"_links\" : {\n" +
            "    \"self\" : {\n" +
            "      \"href\" : \"http://localhost:8080/db-service/api/users{?page,size,sort}\",\n" +
            "      \"templated\" : true\n" +
            "    },\n" +
            "    \"profile\" : {\n" +
            "      \"href\" : \"http://localhost:8080/db-service/api/profile/users\"\n" +
            "    },\n" +
            "    \"search\" : {\n" +
            "      \"href\" : \"http://localhost:8080/db-service/api/users/search\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"page\" : {\n" +
            "    \"size\" : 20,\n" +
            "    \"totalElements\" : 7,\n" +
            "    \"totalPages\" : 1,\n" +
            "    \"number\" : 0\n" +
            "  }\n" +
            "}";
}
