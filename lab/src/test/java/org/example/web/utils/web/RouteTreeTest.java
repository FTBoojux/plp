package org.example.web.utils.web;

import framework.FTest;
import framework.SimpleTestRunner;
import org.example.web.handlers.HealthCheckHandler;

public class RouteTreeTest {
    public static void main(String[] args) {
        RouteTreeTest routeTreeTest = new RouteTreeTest();
        new SimpleTestRunner().runAllTests(routeTreeTest);
    }
    @FTest
    public void bindPathWithPureVariableToHealthCheckSuccessfully(){
        RoutePattern parse = RoutePattern.parse("/{pathVariable}/{pathVariable2}");
        HealthCheckHandler healthCheckHandler = new HealthCheckHandler();
        parse.setRequestHandler(healthCheckHandler);
        RouteTree routeTree = new RouteTree("");
        routeTree.addRoute(parse);
        RouteTree routeTree1 = routeTree.find("/1/2");
        System.out.println(routeTree1);
        assert routeTree1.getRequestHandler() == healthCheckHandler;
    }
    @FTest
    public void bindPathStartWithVariableToHealthCheckSuccessfully(){
        RoutePattern parse = RoutePattern.parse("/{pathVariable}/path");
        HealthCheckHandler healthCheckHandler = new HealthCheckHandler();
        parse.setRequestHandler(healthCheckHandler);
        RouteTree routeTree = new RouteTree("");
        routeTree.addRoute(parse);
        RouteTree routeTree1 = routeTree.find("/1/path");
        System.out.println(routeTree1);
        assert routeTree1.getRequestHandler() == healthCheckHandler;
    }
    @FTest
    public void bindPathEndWithVariableToHealthCheckSuccessfully(){
        RoutePattern parse = RoutePattern.parse("/path/{pathVariable}");
        HealthCheckHandler healthCheckHandler = new HealthCheckHandler();
        parse.setRequestHandler(healthCheckHandler);
        RouteTree routeTree = new RouteTree("");
        routeTree.addRoute(parse);
        RouteTree routeTree1 = routeTree.find("/path/1");
        System.out.println(routeTree1);
        assert routeTree1.getRequestHandler() == healthCheckHandler;
    }
    @FTest
    public void bindPathWithComplexVariableToHealthCheckSuccessfully(){
        RoutePattern parse = RoutePattern.parse("/{pathVariable}/path/{pathVariable2}/path");
        HealthCheckHandler healthCheckHandler = new HealthCheckHandler();
        parse.setRequestHandler(healthCheckHandler);
        RouteTree routeTree = new RouteTree("");
        routeTree.addRoute(parse);
        RouteTree routeTree1 = routeTree.find("/path/path/path/path");
        System.out.println(routeTree1);
        assert routeTree1.getRequestHandler() == healthCheckHandler;
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

        RouteTree routeTree1 = routeTree.find("/1/2/path");
        System.out.println(routeTree1);
        assert routeTree1.getRequestHandler() == healthCheckHandler1;

        RouteTree routeTree2 = routeTree.find("/1/path");
        System.out.println(routeTree2);
        assert routeTree2.getRequestHandler() == healthCheckHandler2;

        RouteTree routeTree3 = routeTree.find("/path/1");
        System.out.println(routeTree3);
        assert routeTree3.getRequestHandler() == healthCheckHandler3;

        RouteTree routeTree4 = routeTree.find("/path/path/path/path");
        System.out.println(routeTree4);
        assert routeTree4.getRequestHandler() == healthCheckHandler4;
    }
}
