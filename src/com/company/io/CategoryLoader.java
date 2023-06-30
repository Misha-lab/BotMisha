package com.company.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class CategoryLoader {
    private ArrayList<String> categories;
    private ArrayList<ArrayList<String>> types;
    private ArrayList<ArrayList<String>> brands;

    public static final String DATA_TEXTS_PATH = "data_texts//";
    public static final String TYPES_IN_CATEGORIES = "typesInCategories//";
    public static final String ALL_CATEGORIES_PATH = "data_texts//allCategories.txt";
    public static final String ALL_BRANDS_PATH = "data_texts//allBrands.txt";
    public static final String ALL_TYPES_PATH = "data_texts//allTypes.txt";

    public CategoryLoader() {
        loadCategories();
        loadBrands();
    }

    public void loadCategories() {
        categories = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File(ALL_CATEGORIES_PATH), "Cp1251");
            while (scanner.hasNextLine()) {
                categories.add(scanner.nextLine());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        loadTypes();
    }

    public void loadTypes() {
        types = new ArrayList<>(categories.size());
        for (int i = 0; i < categories.size(); i++) {
            types.add(new ArrayList<>());
        }
        try {
            Scanner scanner = new Scanner(new File(ALL_TYPES_PATH), "Cp1251");
            int curIndex = -1;
            while (scanner.hasNextLine()) {
                String temp = scanner.nextLine();
                if (temp.startsWith(":::")) {
                    curIndex++;
                } else {
                    types.get(curIndex).add(temp);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void loadBrands() {
        brands = new ArrayList<>(categories.size());
        for (int i = 0; i < categories.size(); i++) {
            brands.add(new ArrayList<>());
        }
        try {
            Scanner scanner = new Scanner(new File(ALL_BRANDS_PATH), "Cp1251");
            int curIndex = -1;
            while (scanner.hasNextLine()) {
                String temp = scanner.nextLine();
                if (temp.startsWith(":::")) {
                    curIndex++;
                } else {
                    brands.get(curIndex).add(temp);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public int indexOfCategory(String categoryName) {
        for (int i = 0; i < categories.size(); i++) {
            if(categories.get(i).equals(categoryName))
                return i;
        }
        return -1;
    }

    public int indexOfType(String categoryName, String typeName) {
        int indexOfCategory = indexOfCategory(categoryName);
        ArrayList<String> typesInCategory = types.get(indexOfCategory);
        for (int i = 0; i < typesInCategory.size(); i++) {
            if(typesInCategory.get(i).equals(typeName))
                return i;
        }
        return -1;
    }

    public int indexOfType(int indexOfCategory, String typeName) {
        ArrayList<String> typesInCategory = types.get(indexOfCategory);
        for (int i = 0; i < typesInCategory.size(); i++) {
            if(typesInCategory.get(i).equals(typeName))
                return i;
        }
        return -1;
    }

    public ArrayList<ArrayList<String>> getTypes() {
        return types;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public ArrayList<ArrayList<String>> getBrands() {
        return brands;
    }
}
