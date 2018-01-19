/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.mfi.futures.tuto.functionnal;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Examples of code to demonstrate how to write lambdas and method references.
 *
 * @author fguerry
 */
public class LambdaSugaringSample {

    public static String method() {
        return "text";
    }

    public void staticSugaring() {
        Supplier<String> full = new Supplier<String>() {
            @Override
            public String get() {
                return LambdaSugaringSample.method();
            }
        };
        Supplier<String> basic = () -> LambdaSugaringSample.method();
        Supplier<String> sugar = LambdaSugaringSample::method;
    }

    public void constructorSugaring() {
        Supplier<String> full = new Supplier<String>() {
            @Override
            public String get() {
                return new String();
            }
        };
        Supplier<String> basic = () -> new String();
        Supplier<String> sugar = String::new;
    }

    public void instanceSugaring() {
        String x = "test";
        Supplier<String> full = new Supplier<String>() {
            @Override
            public String get() {
                return x.toLowerCase();
            }
        };
        Supplier<String> basic = () -> x.toLowerCase();
        Supplier<String> sugar = x::toLowerCase;
    }

    public void classSugaring() {
        Function<String, String> full = new Function<String, String>() {
            @Override
            public String apply(String s) {
                return s.toLowerCase();
            }
        };
        Function<String, String> basic = s -> s.toLowerCase();
        Function<String, String> sugar = String::toLowerCase;
    }

    public void classSugaring2() {
        BiFunction<String, Integer, String> full = new BiFunction<String, Integer, String>() {
            @Override
            public String apply(String s, Integer i) {
                return s.substring(i);
            }
        };
        BiFunction<String, Integer, String> basic = (s, i) -> s.substring(i);
        BiFunction<String, Integer, String> sugar = String::substring;
    }

}
