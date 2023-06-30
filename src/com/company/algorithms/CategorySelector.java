package com.company.algorithms;

import com.company.io.CategoryLoader;
import com.company.model.Favorite;
import com.company.model.GoodsDatabase;
import com.company.model.User;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class CategorySelector {
    private User user;
    private ProbabilityArray probabilityArray;
    private ArrayList<String> categories;
    private ArrayList<Boolean> badCategories;

    private CategoryLoader cl;

    public static final int ALG1_CONST = 5;
    public static final int ALG4_CONST = 10;

    public CategorySelector(User user) {
        this.user = user;
        probabilityArray = new ProbabilityArray();

        cl = new CategoryLoader();
        categories = cl.getCategories();

        badCategories = new ArrayList<>();
        for (int i = 0; i < categories.size(); i++) {
            badCategories.add(i, false);
        }
    }

    public void algorithm1() {
        ArrayList<Favorite> favoriteCategories = user.getFavoriteCategories();
        ArrayList<ArrayList<Favorite>> favoriteTypes = user.getFavoriteTypes();
        for (int i = 0; i < favoriteTypes.size(); i++) {
            double rating = 0;
            for (int j = 0; j < favoriteTypes.get(i).size(); j++) {
                if(favoriteTypes.get(i).get(j).isFavorite()) {
                    rating += ALG1_CONST;
                    probabilityArray.add(favoriteCategories.get(i).getName(), rating);
                }
            }
        }
    }

    public void algorithm2() {
        Workbook wb = user.getTypesWorkbook();
        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            String categoryName = categories.get(i);
            Sheet sheet = wb.getSheet(categoryName);
            int liked = 0;
            int normal = 0;
            int disliked = 0;
            for (int j = 1; j < sheet.getPhysicalNumberOfRows(); j++) {
                liked += sheet.getRow(j).getCell(User.COUNT_LIKED_INDEX).getNumericCellValue();
                normal += sheet.getRow(j).getCell(User.COUNT_NORMAL_INDEX).getNumericCellValue();
                disliked += sheet.getRow(j).getCell(User.COUNT_DISLIKED_INDEX).getNumericCellValue();
            }
            //System.out.println("--- " + sheet.getSheetName() + " ---");
            double rating = MathPart.func(liked, normal, disliked);
            //System.out.printf("Liked: %d\nDisliked: %d\nNormal: %d\nResult: %f\n\n", liked,disliked,normal,rating);
            if(rating > 0) {
                probabilityArray.add(categoryName, rating);
            }
            else if (liked+normal+disliked != 0){
                badCategories.set(i, true);
            }
        }
    }

    public void algorithm4() {
        try {
            FileInputStream fis = new FileInputStream(GoodsDatabase.CATEGORY_COMPATIBILITY_PATH);
            Workbook categoryComp = new HSSFWorkbook(fis);
            Sheet sheet = categoryComp.getSheet(GoodsDatabase.CATEGORY_COMPATIBILITY_SHEET_NAME);
            if(sheet != null) {
                for (int i = 0; i < probabilityArray.getNames().size(); i++) {
                    int index = cl.indexOfCategory(probabilityArray.getNames().get(i));
                    if (index != -1) {
                        for (int j = 1; j < index + 1; j++) {
                            if(!badCategories.get(j - 1)) {
                                Cell cell = sheet.getRow(j).getCell(index + 1);
                                if (cell != null) {
                                    //System.out.println(cell.getStringCellValue());
                                    double value = Double.parseDouble(cell.getStringCellValue());
                                    if (value >= 0.0 && value <= 1.0) {
                                        if(Math.random() < value) {
                                            probabilityArray.add(categories.get(j - 1), ALG4_CONST);
                                            //System.out.println("ADDED: " + categories.get(j - 1));
                                        }
                                    }
                                }
                            }
                        }
                        Row row = sheet.getRow(index + 1);
                        for (int j = index + 2; j <= categories.size(); j++) {
                            if(!badCategories.get(j - 1)) {
                                Cell cell = row.getCell(j);
                                if (cell != null) {
                                    //System.out.println(cell.getStringCellValue());
                                    double value = Double.parseDouble(cell.getStringCellValue());
                                    if (value >= 0.0 && value <= 1.0) {
                                        if(Math.random() < value) {
                                            probabilityArray.add(categories.get(j - 1), ALG4_CONST);
                                            //System.out.println("ADDED: " + categories.get(j - 1));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    public void startAlgorithms() {
        algorithm1();
        algorithm2();
        algorithm4();
    }

    public ProbabilityArray getProbabilityArray() {
        return probabilityArray;
    }
}
