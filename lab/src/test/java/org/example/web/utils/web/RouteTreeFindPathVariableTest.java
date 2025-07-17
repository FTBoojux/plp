package org.example.web.utils.web;

import framework.FTest;
import framework.SimpleTestRunner;
import org.example.web.handlers.HealthCheckHandler;

public class RouteTreeFindPathVariableTest {
    public static void main(String[] args) {
        RouteTreeFindPathVariableTest test = new RouteTreeFindPathVariableTest();
        new SimpleTestRunner().runAllTests(test);
    }
    @FTest
    public void bindPathWithPureVariableToHealthCheckSuccessfully(){
        RoutePattern parse = RoutePattern.parse("/{pathVariable}/{pathVariable2}");
        HealthCheckHandler healthCheckHandler = new HealthCheckHandler();
        parse.setRequestHandler(healthCheckHandler);
        RouteTree routeTree = new RouteTree("");
        routeTree.addRoute(parse);
        MatchResult matchResult = routeTree.findWithVariable("/1/2");
        System.out.println(matchResult);
        assert matchResult.requestHandler == healthCheckHandler;
    }
    @FTest
    public void testAllFourCase(){
        RouteTree routeTree = new RouteTree("");

        RoutePattern parse1 = RoutePattern.parse("/{pathVariable}/{pathVariable2}/path");
        HealthCheckHandler healthCheckHandler1 = new HealthCheckHandler();
        parse1.setRequestHandler(healthCheckHandler1);
        routeTree.addRoute(parse1);

        RoutePattern parse2 = RoutePattern.parse("/{pathVariable}/path");
        HealthCheckHandler healthCheckHandler2 = new HealthCheckHandler();
        parse2.setRequestHandler(healthCheckHandler2);
        routeTree.addRoute(parse2);

        RoutePattern parse3 = RoutePattern.parse("/path/{pathVariable}");
        HealthCheckHandler healthCheckHandler3 = new HealthCheckHandler();
        parse3.setRequestHandler(healthCheckHandler3);
        routeTree.addRoute(parse3);

        RoutePattern parse4 = RoutePattern.parse("/{pathVariable}/path/{pathVariable2}/path");
        HealthCheckHandler healthCheckHandler4 = new HealthCheckHandler();
        parse4.setRequestHandler(healthCheckHandler4);
        routeTree.addRoute(parse4);

        MatchResult matchResult1 = routeTree.findWithVariable("/1/2/path");
        System.out.println(matchResult1);
        assert matchResult1.requestHandler == healthCheckHandler1;

        MatchResult matchResult2 = routeTree.findWithVariable("/1/path");
        System.out.println(matchResult2);
        assert matchResult2.requestHandler == healthCheckHandler2;

        MatchResult matchResult3 = routeTree.findWithVariable("/path/1");
        System.out.println(matchResult3);
        assert matchResult3.requestHandler == healthCheckHandler3;

        MatchResult matchResult4 = routeTree.findWithVariable("/path/path/path/path");
        System.out.println(matchResult4);
        assert matchResult4.requestHandler == healthCheckHandler4;
    }
}
