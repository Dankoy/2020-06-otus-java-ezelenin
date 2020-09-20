package ru.dankoy.otus.serialization.guineapig;

import java.util.List;
import java.util.Map;

public class GuineaPig {

    private String name;
    private int age;
    private List<String> someList;
    private List<Integer> someList2;
    private Map<String, Integer> someMap;
    private boolean bool;
    private Boolean bool2;


    public static GuineaPigBuilder newBuilder() {
        return new GuineaPig().new GuineaPigBuilder();
    }

    public class GuineaPigBuilder {

        private GuineaPigBuilder() {
            // private constructor
        }

        public GuineaPigBuilder setName(String name) {
            GuineaPig.this.name = name;
            return this;
        }

        public GuineaPigBuilder setAge(int age) {
            GuineaPig.this.age = age;
            return this;
        }

        public GuineaPigBuilder setSomeList(List<String> list) {
            GuineaPig.this.someList = list;
            return this;
        }

        public GuineaPigBuilder setSomeList2(List<Integer> list) {
            GuineaPig.this.someList2 = list;
            return this;
        }

        public GuineaPigBuilder setSomeMap(Map<String, Integer> map) {
            GuineaPig.this.someMap = map;
            return this;
        }

        public GuineaPigBuilder setBool(boolean bool) {
            GuineaPig.this.bool = bool;
            return this;
        }

        public GuineaPigBuilder setBool2(Boolean bool) {
            GuineaPig.this.bool2 = bool;
            return this;
        }

        public GuineaPig build() {
            return GuineaPig.this;
        }


    }

}
