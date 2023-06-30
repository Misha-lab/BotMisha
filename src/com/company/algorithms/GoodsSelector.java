package com.company.algorithms;

import com.company.model.Favorite;
import com.company.model.GoodsDatabase;
import com.company.model.Product;
import com.company.model.User;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.ArrayList;

public class GoodsSelector {
    private User user;
    private ProbabilityArray probabilityArray;
    private ArrayList<Product> addedGoods;
    private String categoryName;
    private String typeName;
    private ArrayList<Product> goods;

    public static final int ALG1_CONST = 5;
    public static final double ALG1_COEFF = 0.2;
    public static final int ALG3_CONST = 10;

    public GoodsSelector(User user, String categoryName, String typeName) {
        this.user = user;
        probabilityArray = new ProbabilityArray();

        this.categoryName = categoryName;
        this.typeName = typeName;

        GoodsDatabase goodsDatabase = new GoodsDatabase();
        goods = goodsDatabase.loadGoodsFromTypeAndCategory(this.categoryName, this.typeName);
        addedGoods = new ArrayList<>();
    }

    public void algorithm1() {
        ArrayList<Favorite> favoriteBrands = user.getFavoriteBrandsInCategory(categoryName);

        for (int i = 0; i < goods.size(); i++) {
            boolean isFound = false;
            for (int j = 0; j < favoriteBrands.size() && !isFound; j++) {
                if (goods.get(i).getBrand().equals(favoriteBrands.get(j).getName())) {
                    if(favoriteBrands.get(j).isFavorite()) {
                        double value = probabilityArray.getByName(goods.get(i).getName());
                        boolean isNew = probabilityArray.add(goods.get(i).getName(), ALG1_CONST +
                                ALG1_COEFF * value);
                        if (isNew) {
                            addedGoods.add(goods.get(i));
                        }
                    }
                    isFound = true;

                }
            }
        }
    }

    public void algorithm2() {
        Workbook goodsWorkbook = user.getGoodsWorkbook();
        Sheet sheet = goodsWorkbook.getSheet(categoryName);
        int liked;
        int normal;
        int disliked;
        for (int j = 1; j < sheet.getPhysicalNumberOfRows(); j++) {
            String name = sheet.getRow(j).getCell(User.NAME_INDEX).getStringCellValue();
            Product product = findProductInGoods(name);

            if(product != null) {
                liked = (int) sheet.getRow(j).getCell(User.COUNT_LIKED_INDEX).getNumericCellValue();
                normal = (int) sheet.getRow(j).getCell(User.COUNT_NORMAL_INDEX).getNumericCellValue();
                disliked = (int) sheet.getRow(j).getCell(User.COUNT_DISLIKED_INDEX).getNumericCellValue();

                double rating = MathPart.func(liked, normal, disliked);
                //System.out.printf("Liked: %d\nDisliked: %d\nNormal: %d\nResult: %f\n\n", liked,disliked,normal,rating);
                if (rating > 0) {
                    boolean isNew = probabilityArray.add(name, rating);
                    if (isNew) {
                        addedGoods.add(product);
                    }
                    //System.out.println(rating);
                }
            }
        }
    }

    public void algorithm3() {
        for (int i = 0; i < goods.size(); i++) {
            if(goods.get(i).getDiscountInPercents() > 0) {
                int index = probabilityArray.indexOf(goods.get(i).getName());
                if (index == -1) {
                    probabilityArray.add(goods.get(i).getName(), goods.get(i).getDiscountInPercents());
                    addedGoods.add(goods.get(i));
                }
                else {
                    probabilityArray.setValueAt(index, probabilityArray.getValueAt(index) +
                            addedGoods.get(index).getDiscountInPercents() * probabilityArray.getValueAt(index)/100);
                }
            }
        }
    }

    public void startAlgorithms() {
        algorithm2();
        algorithm1();
        algorithm3();
    }

    public ProbabilityArray getProbabilityArray() {
        return probabilityArray;
    }

    public ArrayList<Product> getGoods() {
        return goods;
    }

    public ArrayList<Product> getAddedGoods() {
        return addedGoods;
    }

    public Product findProductInGoods(String name) {
        for (int i = 0; i < goods.size(); i++) {
            if(goods.get(i).getName().equals(name))
                return goods.get(i);
        }
        return null;
    }
}
