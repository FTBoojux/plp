package org.example.web.utils.web;

import framework.Best;
import framework.Bassert;
import framework.SimpleTestRunner;

public class RoutePatternTest {
    public static void main(String[] args) {
        RoutePatternTest routeTreeTest = new RoutePatternTest();
        new SimpleTestRunner().runAllTests(routeTreeTest);
    }
    @Best
    public void createStaticRoutePatternSuccessfully(){
        RoutePattern parse = RoutePattern.parse("/healthCheck");
        System.out.println(parse);
        Bassert.fAssert(parse.getPathType() == PathType.STATIC);

    }

    @Best
    public void createDynamicRoutePatternEndWithVariableSuccessfully(){
        RoutePattern parse = RoutePattern.parse("/healthCheck/{pathVariable}");
        System.out.println(parse);
        Bassert.fAssert(parse.getPathType() == PathType.DYNAMIC);
    }
    @Best
    public void createDynamicRoutePatternStartWithVariableSuccessfully2(){
        RoutePattern parse = RoutePattern.parse("/{pathVariable}/healthCheck");
        System.out.println(parse);
        Bassert.fAssert(parse.getPathType() == PathType.DYNAMIC);
    }
    @Best
    public void createDynamicRoutePatternMixedWithVariableSuccessfully3(){
        RoutePattern parse = RoutePattern.parse("/{pathVariable}/{pathVariable2}");
        System.out.println(parse);
        Bassert.fAssert(parse.getPathType() == PathType.DYNAMIC);
    }
}
