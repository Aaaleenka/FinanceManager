package org.example.finance;

import java.util.List;

public class Categories {
    protected String category;
    protected String name;
    protected List<Categories> listCategories;

    public Categories(String name, String category) {
        this.name = name;
        this.category = category;
    }

    public Categories(List<Categories> list) {
        this.listCategories = list;
    }

    public Categories() {

    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public List<Categories> getListCategories() {
        return listCategories;
    }

    @Override
    public String toString() {
        return name + " " + category;
    }

    public void printCategories() {

        for (int i = 0; i < listCategories.size(); i++) {
            System.out.println(listCategories.get(i).toString());
        }
    }

}
