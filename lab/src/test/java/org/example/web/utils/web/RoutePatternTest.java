package org.example.web.utils.web;

import framework.FTest;
import framework.SimpleTestRunner;

public class RoutePatternTest {
    public static void main(String[] args) {
        RoutePatternTest routeTreeTest = new RoutePatternTest();
        new SimpleTestRunner().runAllTests(routeTreeTest);
    }
    @FTest
    public void createStaticRoutePatternSuccessfully(){
        RoutePattern parse = RoutePattern.parse("/healthCheck");
        System.out.println(parse);
        assert parse.getPathType() == PathType.STATIC;

    }

    @FTest
    public void createDynamicRoutePatternEndWithVariableSuccessfully(){
        RoutePattern parse = RoutePattern.parse("/healthCheck/{pathVariable}");
        System.out.println(parse);
        assert parse.getPathType() == PathType.DYNAMIC;
    }
    @FTest
    public void createDynamicRoutePatternStartWithVariableSuccessfully2(){
        RoutePattern parse = RoutePattern.parse("/{pathVariable}/healthCheck");
        System.out.println(parse);
        assert parse.getPathType() == PathType.DYNAMIC;
    }
    @FTest
    public void createDynamicRoutePatternMixedWithVariableSuccessfully3(){
        RoutePattern parse = RoutePattern.parse("/{pathVariable}/{pathVariable2}");
        System.out.println(parse);
        assert parse.getPathType() == PathType.DYNAMIC;
    }
}
