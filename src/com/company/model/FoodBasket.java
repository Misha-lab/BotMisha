package com.company.model;

import com.company.algorithms.CategorySelector;
import com.company.algorithms.GoodsSelector;
import com.company.algorithms.ProbabilityArray;
import com.company.algorithms.TypesSelector;
import com.company.io.CategoryLoader;
import com.company.view.ProductView;

import java.util.ArrayList;

public class FoodBasket {
    private User user;
    private double budget;

    private double currentCost;

    private ArrayList<Integer> lockedCategories;
    private ArrayList<ArrayList<Integer>> lockedTypes;

    public FoodBasket(User user) {
        this.user = user;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public ArrayList<Product> generateBasket(double budget_) {
        CategoryLoader categoryLoader = new CategoryLoader();
        lockedCategories = new ArrayList<>();
        for (int i = 0; i < categoryLoader.getCategories().size(); i++) {
            lockedCategories.add(i, 0);
        }

        lockedTypes = new ArrayList<>();
        for (int i = 0; i < categoryLoader.getCategories().size(); i++) {
            ArrayList<Integer> temp = new ArrayList<>();
            for (int j = 0; j < categoryLoader.getTypes().get(i).size(); j++) {
                temp.add(j, 0);
            }
            lockedTypes.add(i, temp);
        }

        currentCost = 0;
        budget = budget_;
        ArrayList<Product> generatedProducts = new ArrayList<>();

        CategorySelector categorySelector = new CategorySelector(user);
        categorySelector.startAlgorithms();
        ProbabilityArray categoriesArray = categorySelector.getProbabilityArray();
        //categoriesArray.printProbabilitiesInPercents();

        if(categoriesArray.getNames().size() != 0) {
            int notFoundCounter = 0;
            int limit = 3;
            for (int i = 0; currentCost < budget && notFoundCounter < limit; i++) {
                int indexOfCategory = categoriesArray.generateRand();
                String categoryName = categoriesArray.getNames().get(indexOfCategory);
                System.out.println("Сгенерированная категория - " + categoryName + " (index = " + indexOfCategory + ")");

                TypesSelector typesSelector = new TypesSelector(user, categoryName);
                typesSelector.startAlgorithms();
                ProbabilityArray typesArray = typesSelector.getProbabilityArray();

                int indexCat = categoryLoader.indexOfCategory(categoryName);
                if (typesArray.getNames().size() != 0) {
                    int indexOfTypes = typesArray.generateRand();
                    String typeName = typesArray.getNames().get(indexOfTypes);
                    System.out.println(i + " тип: " + typeName);

                    //typesArray.printProbabilitiesInPercents();

                    GoodsSelector gs = new GoodsSelector(user, categoryName, typeName);
                    gs.startAlgorithms();
                    ProbabilityArray goodsArray = gs.getProbabilityArray();

                    boolean isDeleted = false;
                    for (int j = 0; j < goodsArray.getNames().size(); j++) {
                        System.out.println(goodsArray.getNames().get(j) + ": " +
                                (currentCost + gs.getAddedGoods().get(j).getDiscountPrice()));
                        if(currentCost + gs.getAddedGoods().get(j).getDiscountPrice() > budget) {
                            System.out.println("DELETE: " + goodsArray.getNames().get(j));
                            goodsArray.delete(j);
                            gs.getAddedGoods().remove(j);
                            System.out.println(goodsArray.getNames().size());
                            System.out.println(gs.getAddedGoods().size());
                            isDeleted = true;
                            j--;
                        }
                    }
                    if (goodsArray.getNames().size() != 0) {
                        goodsArray.printProbabilitiesInPercents();
                        int indexOfProduct = goodsArray.generateRand();
                        Product product = gs.getAddedGoods().get(indexOfProduct);

                        generatedProducts.add(product);
                        currentCost += product.getDiscountPrice();
                        System.out.print(gs.getAddedGoods().get(indexOfProduct));
                        System.out.print(currentCost);
                        System.out.println();

                        int indexType = categoryLoader.indexOfType(indexCat, typeName);
                        lockedTypes.get(indexCat).set(indexType, lockedTypes.get(indexCat).get(indexType) + 1);
                    }
                    else {
                        if(isDeleted) {
                            notFoundCounter++;
                            System.out.println(notFoundCounter);
                        }
                    }
                }
                lockedCategories.set(indexCat, lockedCategories.get(indexCat) + 1);
                categoriesArray.setValueAt(indexOfCategory, categoriesArray.getValueAt(indexOfCategory)
                        * 0.4);
            }
        }
        return generatedProducts;
    }

    public ArrayList<Product> chooseApprovedProducts(ArrayList<Product> products) {
        ArrayList<Product> approvedProducts = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getRating() == ProductView.GOOD_PRODUCT_SELECTED) {
                approvedProducts.add(products.get(i));
            }
        }
        return approvedProducts;
    }

    public double getCurrentCost() {
        return currentCost;
    }
}
