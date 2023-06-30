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

public class TypesSelector {
    private User user;
    private ProbabilityArray probabilityArray;
    private ArrayList<String> categories;
    private ArrayList<String> typesInCategories;
    private String categoryName;
    private  CategoryLoader cl;
    private ArrayList<Boolean> badTypes;

    public static final int ALG1_CONST = 5;
    public static final int ALG4_CONST = 10;

    public TypesSelector(User user, String categoryName) {
        this.user = user;
        probabilityArray = new ProbabilityArray();

        cl = new CategoryLoader();
        categories = cl.getCategories();

        int index = cl.indexOfCategory(categoryName);
        if(index != -1) {
            typesInCategories = cl.getTypes().get(index);
        }
        badTypes = new ArrayList<>();
        for (int i = 0; i < typesInCategories.size(); i++) {
            badTypes.add(i, false);
        }

        this.categoryName = categoryName;
    }

    public void algorithm1() {
        int index = -1;
        for (int i = 0; i < categories.size(); i++) {
            if(categoryName.equals(categories.get(i))) {
                index = i;
                break;
            }
        }
        if (index != - 1) {
            ArrayList<Favorite> favoriteTypesInCategory = user.getFavoriteTypes().get(index);
            for (int i = 0; i < favoriteTypesInCategory.size(); i++) {
                if(favoriteTypesInCategory.get(i).isFavorite())
                    probabilityArray.add(favoriteTypesInCategory.get(i).getName(), ALG1_CONST);
            }
        }
    }

    public void algorithm2() {
        Workbook wb = user.getTypesWorkbook();
        Sheet sheet = wb.getSheet(categoryName);
        int liked;
        int normal;
        int disliked;
        for (int j = 1; j < sheet.getPhysicalNumberOfRows(); j++) {
            liked = (int) sheet.getRow(j).getCell(User.COUNT_LIKED_INDEX).getNumericCellValue();
            normal = (int) sheet.getRow(j).getCell(User.COUNT_NORMAL_INDEX).getNumericCellValue();
            disliked = (int) sheet.getRow(j).getCell(User.COUNT_DISLIKED_INDEX).getNumericCellValue();

            String type = sheet.getRow(j).getCell(User.NAME_INDEX).getStringCellValue();
            //System.out.println("--- " + type + " ---");
            double rating = MathPart.func(liked, normal, disliked);
            //System.out.printf("Liked: %d\nDisliked: %d\nNormal: %d\nResult: %f\n\n", liked,disliked,normal,rating);
            if (rating > 0) {
                probabilityArray.add(type, rating);
                //System.out.println(rating);
            }
            else if (liked + normal + disliked != 0) {
                badTypes.set(j - 1, true);
            }
        }
    }

    public void algorithm4() {
        try {
            FileInputStream fis = new FileInputStream(GoodsDatabase.TYPES_COMPATIBILITY_PATH);
            Workbook typeComp = new HSSFWorkbook(fis);
            Sheet sheet = typeComp.getSheet(categoryName);
            if(sheet != null) {
                for (int i = 0; i < probabilityArray.getNames().size(); i++) {
                    int index = cl.indexOfType(categoryName, probabilityArray.getNames().get(i));
                    if (index != -1) {
                        for (int j = 1; j < index + 1; j++) {
                            if(!badTypes.get(j - 1)) {
                                Cell cell = sheet.getRow(j).getCell(index + 1);
                                if (cell != null) {
                                    //System.out.println(cell.getStringCellValue());
                                    double value = Double.parseDouble(cell.getStringCellValue());
                                    if (value >= 0.0 && value <= 1.0) {
                                        if(Math.random() < value) {
                                            probabilityArray.add(typesInCategories.get(j - 1), ALG4_CONST);
                                            //System.out.println("ADDED: " + typesInCategories.get(j - 1));
                                        }
                                    }
                                }
                            }
                        }
                        Row row = sheet.getRow(index + 1);
                        for (int j = index + 2; j <= typesInCategories.size(); j++) {
                            if(!badTypes.get(j - 1)) {
                                Cell cell = row.getCell(j);
                                if (cell != null) {
                                    //System.out.println(cell.getStringCellValue());
                                    double value = Double.parseDouble(cell.getStringCellValue());
                                    if (value >= 0.0 && value <= 1.0) {
                                        if(Math.random() < value) {
                                            probabilityArray.add(typesInCategories.get(j - 1), ALG4_CONST);
                                            //System.out.println("ADDED: " + typesInCategories.get(j - 1));
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