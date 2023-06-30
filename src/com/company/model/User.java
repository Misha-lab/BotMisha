package com.company.model;

import com.company.io.CategoryLoader;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class User {
    public static final String PATH_BEGIN = "users//";
    public static final String GOODS_BASE = "KnowledgeBase_Goods.xls";
    public static final String BRANDS_BASE = "KnowledgeBase_Brands.xls";
    public static final String TYPES_BASE = "KnowledgeBase_Types.xls";

    public static final String FAVORITE_TYPES_FILE = "FavoriteTypes.txt";
    public static final String FAVORITE_BRANDS_FILE = "FavoriteBrands.txt";

    public static final int NAME_INDEX = 0;
    public static final int COUNT_INDEX = 1;
    public static final int COUNT_LIKED_INDEX = 2;
    public static final int COUNT_NORMAL_INDEX = 3;
    public static final int COUNT_DISLIKED_INDEX = 4;

    public static final int GOODS_ID = 100;
    public static final int BRANDS_ID = 200;
    public static final int TYPES_ID = 300;

    private final String name;
    private Workbook goodsWorkbook;
    private Workbook brandsWorkbook;
    private Workbook typesWorkbook;
    private ArrayList<Favorite> favoriteCategories;
    private ArrayList<ArrayList<Favorite>> favoriteTypes;

    private ArrayList<Favorite> favoriteCategoriesInBrands;
    private ArrayList<ArrayList<Favorite>> favoriteBrands;

    public User(String name) {
        this.name = name;

        createAccount();
        loadWorkbooks();
        loadTextFiles();
    }

    public void createAccount() {
        createFiles();
    }

    public void loadWorkbooks() {
        loadWorkbook(GOODS_ID);
        loadWorkbook(BRANDS_ID);
        loadWorkbook(TYPES_ID);
    }

    public void loadWorkbook(int id) {
        try {
            if (id == GOODS_ID) {
                FileInputStream fis1 = new FileInputStream(PATH_BEGIN + name + "//" + name +
                        GOODS_BASE);
                goodsWorkbook = new HSSFWorkbook(fis1);
            } else if (id == BRANDS_ID) {
                FileInputStream fis2 = new FileInputStream(PATH_BEGIN + name + "//" + name +
                        BRANDS_BASE);
                brandsWorkbook = new HSSFWorkbook(fis2);
            } else if (id == TYPES_ID) {
                FileInputStream fis3 = new FileInputStream(PATH_BEGIN + name + "//" + name +
                        TYPES_BASE);
                typesWorkbook = new HSSFWorkbook(fis3);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void loadTextFiles() {
        loadTextFile(TYPES_ID);
        loadTextFile(BRANDS_ID);
    }

    public void loadTextFile(int id) {
        try {
            if (id == TYPES_ID) {
                CategoryLoader cl = new CategoryLoader();
                ArrayList<String> categories = cl.getCategories();
                ArrayList<ArrayList<String>> types = cl.getTypes();


                favoriteTypes = new ArrayList<>();
                favoriteCategories = new ArrayList<>();
                for (int i = 0; i < categories.size(); i++) {
                    favoriteCategories.add(i, new Favorite(categories.get(i), false));
                    favoriteTypes.add(i, new ArrayList<>());
                    for (int j = 0; j < types.get(i).size(); j++) {
                        favoriteTypes.get(i).add(j, new Favorite(types.get(i).get(j), false));
                    }
                }
                Scanner scanner1 = new Scanner(new File(PATH_BEGIN + name + "//" + name +
                        FAVORITE_TYPES_FILE));
                int idx = -1;
                while (scanner1.hasNextLine()) {
                    String temp = scanner1.nextLine();
                    if(temp.startsWith("+")) {
                        Favorite favorite = new Favorite(temp.substring(1), false);
                        for (int i = 0; i < favoriteCategories.size(); i++) {
                            if(favoriteCategories.get(i).getName().equals(favorite.getName())) {
                                idx = i;
                                break;
                            }
                        }
                        if(idx != -1)
                            favoriteCategories.get(idx).setFavorite(true);
                    }
                    else if(temp.startsWith("-")) {
                        if(idx != -1) {
                            ArrayList<Favorite> typesInCategory = favoriteTypes.get(idx);
                            Favorite favorite = new Favorite(temp.substring(1), false);
                            int idx2 = -1;
                            for (int i = 0; i < typesInCategory.size(); i++) {
                                if(typesInCategory.get(i).getName().equals(favorite.getName())) {
                                    idx2 = i;
                                    break;
                                }
                            }
                            if(idx2 != -1)
                                favoriteTypes.get(idx).get(idx2).setFavorite(true);

                        }
                    }
                }
            } else if (id == BRANDS_ID) {
                CategoryLoader cl = new CategoryLoader();
                ArrayList<String> categories = cl.getCategories();
                ArrayList<ArrayList<String>> brands = cl.getBrands();

                favoriteBrands = new ArrayList<>();
                favoriteCategoriesInBrands = new ArrayList<>();
                for (int i = 0; i < categories.size(); i++) {
                    favoriteCategoriesInBrands.add(i, new Favorite(categories.get(i), false));
                    favoriteBrands.add(i, new ArrayList<>());
                    for (int j = 0; j < brands.get(i).size(); j++) {
                        favoriteBrands.get(i).add(j, new Favorite(brands.get(i).get(j), false));
                    }
                }
                Scanner scanner1 = new Scanner(new File(PATH_BEGIN + name + "//" + name +
                        FAVORITE_BRANDS_FILE));
                int idx = -1;
                while (scanner1.hasNextLine()) {
                    String temp = scanner1.nextLine();
                    if(temp.startsWith("+")) {
                        Favorite favorite = new Favorite(temp.substring(1), false);
                        for (int i = 0; i < favoriteCategoriesInBrands.size(); i++) {
                            if(favoriteCategoriesInBrands.get(i).getName().equals(favorite.getName())) {
                                idx = i;
                                break;
                            }
                        }
                        if(idx != -1)
                            favoriteCategoriesInBrands.get(idx).setFavorite(true);
                    }
                    else if(temp.startsWith("-")) {
                        if(idx != -1) {
                            ArrayList<Favorite> typesInCategory = favoriteBrands.get(idx);
                            Favorite favorite = new Favorite(temp.substring(1), false);
                            int idx2 = -1;
                            for (int i = 0; i < typesInCategory.size(); i++) {
                                if(typesInCategory.get(i).getName().equals(favorite.getName())) {
                                    idx2 = i;
                                    break;
                                }
                            }
                            if(idx2 != -1)
                                favoriteBrands.get(idx).get(idx2).setFavorite(true);

                        }
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void saveWorkbooksToFile() {
        saveWorkbookToFile(GOODS_ID);
        saveWorkbookToFile(BRANDS_ID);
        saveWorkbookToFile(TYPES_ID);
    }

    public void saveWorkbookToFile(int id) {
        try {
            if (id == GOODS_ID) {
                FileOutputStream fos1 = new FileOutputStream(PATH_BEGIN + name + "//" + name +
                        GOODS_BASE);
                goodsWorkbook.write(fos1);
                fos1.close();
                //goodsWorkbook.close();
            } else if (id == BRANDS_ID) {
                FileOutputStream fos2 = new FileOutputStream(PATH_BEGIN + name + "//" + name +
                        BRANDS_BASE);
                brandsWorkbook.write(fos2);
                fos2.close();
                //brandsWorkbook.close();
            } else if (id == TYPES_ID) {
                FileOutputStream fos3 = new FileOutputStream(PATH_BEGIN + name + "//" + name +
                        TYPES_BASE);
                typesWorkbook.write(fos3);
                fos3.close();
                //typesWorkbook.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void saveTextFiles() {
        saveTextFile(TYPES_ID);
        saveTextFile(BRANDS_ID);
    }

    public void saveTextFile(int id) {
        try {
            if (id == TYPES_ID) {
                FileWriter fw = new FileWriter(PATH_BEGIN + name + "//" + name +
                        FAVORITE_TYPES_FILE);
                for (int i = 0; i < favoriteTypes.size(); i++) {
                    if(favoriteCategories.get(i).isFavorite()) {
                        fw.write("+" + favoriteCategories.get(i).getName() + "\r\n");
                        for (int j = 0; j < favoriteTypes.get(i).size(); j++) {
                            if(favoriteTypes.get(i).get(j).isFavorite()) {
                                fw.write("-" + favoriteTypes.get(i).get(j).getName() + "\r\n");
                            }
                        }
                    }
                }
                fw.flush();
                fw.close();
            } else if (id == BRANDS_ID) {
                FileWriter fw = new FileWriter(PATH_BEGIN + name + "//" + name +
                        FAVORITE_BRANDS_FILE);
                for (int i = 0; i < favoriteBrands.size(); i++) {
                    if(favoriteCategoriesInBrands.get(i).isFavorite()) {
                        fw.write("+" + favoriteCategoriesInBrands.get(i).getName() + "\r\n");
                        for (int j = 0; j < favoriteBrands.get(i).size(); j++) {
                            if(favoriteBrands.get(i).get(j).isFavorite()) {
                                fw.write("-" + favoriteBrands.get(i).get(j).getName() + "\r\n");
                            }
                        }
                    }
                }
                fw.flush();
                fw.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void createFiles() {
        File folder = new File(PATH_BEGIN + name);
        if (!folder.exists()) {
            folder.mkdir();
        }
        createExcelFile(GOODS_BASE);
        createExcelFile(BRANDS_BASE);


        createExcelFileOfTypes();
        File favoriteTypesFile = new File(PATH_BEGIN + name + "//" + name +
                FAVORITE_TYPES_FILE);
        File favoriteBrandsFile = new File(PATH_BEGIN + name + "//" + name +
                FAVORITE_BRANDS_FILE);
        try {
            if (!favoriteBrandsFile.exists())
                favoriteBrandsFile.createNewFile();
            if (!favoriteTypesFile.exists())
                favoriteTypesFile.createNewFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void createExcelFileOfTypes() {
        File file = new File(PATH_BEGIN + name + "//" + name + TYPES_BASE);
        if (!file.exists()) {
            try {
                file.createNewFile();
                Workbook wb = new HSSFWorkbook();

                CategoryLoader cl = new CategoryLoader();
                ArrayList<String> categories = cl.getCategories();
                ArrayList<ArrayList<String>> types = cl.getTypes();

                for (int i = 0; i < categories.size(); i++) {
                    Sheet sheet = wb.getSheet(categories.get(i));
                    if (sheet == null)
                        sheet = wb.createSheet(categories.get(i));
                    createHeader(wb, sheet);

                    ArrayList<String> category = types.get(i);
                    for (int j = 0; j < category.size(); j++) {
                        if (findType(sheet, category.get(j)) == -1) {
                            Row row = sheet.createRow(sheet.getPhysicalNumberOfRows());
                            Cell cell = row.createCell(NAME_INDEX);
                            cell.setCellValue(category.get(j));

                            row.createCell(COUNT_INDEX).setCellValue(0);
                            row.createCell(COUNT_LIKED_INDEX).setCellValue(0);
                            row.createCell(COUNT_NORMAL_INDEX).setCellValue(0);
                            row.createCell(COUNT_DISLIKED_INDEX).setCellValue(0);
                        }
                    }
                }

                FileOutputStream fos = new FileOutputStream(file.getPath());
                wb.write(fos);
                fos.flush();
                fos.close();
                wb.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void createExcelFile(String filename) {
        File file = new File(PATH_BEGIN + name + "//" + name + filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
                Workbook wb = new HSSFWorkbook();

                CategoryLoader cl = new CategoryLoader();
                ArrayList<String> categories = cl.getCategories();

                for (int i = 0; i < categories.size(); i++) {
                    Sheet sheet = wb.getSheet(categories.get(i));
                    if (sheet == null)
                        sheet = wb.createSheet(categories.get(i));
                    createHeader(wb, sheet);
                }

                FileOutputStream fos = new FileOutputStream(file.getPath());
                wb.write(fos);
                fos.flush();
                fos.close();
                wb.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void createHeader(Workbook wb, Sheet sheet) {
        String[] titles = new String[]{"Название", "Кол-во купленного товара",
                "Понравилось (в разах)", "Средне (в разах)", "Не понравилось (в разах)"};
        CellStyle style = wb.createCellStyle();
        Font fontHeader = wb.createFont();
        fontHeader.setBold(true);
        style.setFont(fontHeader);

        Row row = sheet.getRow(0);
        if (row == null)
            row = sheet.createRow(0);
        for (int i = 0; i < titles.length; i++) {
            Cell cell = row.getCell(i);
            if (cell == null)
                cell = row.createCell(i);
            cell.setCellValue(titles[i]);
            cell.setCellStyle(style);
        }
    }

    public int findType(Sheet sheet, String name) {
        int result = -1;
        for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                Cell cell = row.getCell(0);
                if (cell != null) {
                    if (cell.getStringCellValue().equals(name))
                        return i;
                }
            }
        }
        return result;
    }

    public void writeProductToFile(Product product, int rate) {
        Sheet typesSheet = typesWorkbook.getSheet(product.getCategory());
        for (int i = 1; i < typesSheet.getPhysicalNumberOfRows(); i++) {
            if(typesSheet.getRow(i).getCell(NAME_INDEX).getStringCellValue().equals(product.getType())) {
                Row row = typesSheet.getRow(i);
                Cell countAll = row.getCell(COUNT_INDEX);
                countAll.setCellValue(row.getCell(COUNT_INDEX).getNumericCellValue() + 1);

                Cell countRated = row.getCell(rate + 1);
                countRated.setCellValue(row.getCell(rate + 1).getNumericCellValue() + 1);

                break;
            }
        }

        Sheet brandsSheet = brandsWorkbook.getSheet(product.getCategory());
        int brandIndex = brandsSheet.getPhysicalNumberOfRows();
        boolean isFoundBrand = false;
        for (int i = 1; i < brandsSheet.getPhysicalNumberOfRows(); i++) {
            if(brandsSheet.getRow(i).getCell(NAME_INDEX).getStringCellValue().equals(product.getBrand())) {
                brandIndex = i;
                isFoundBrand = true;
                break;
            }
        }
        Row brandRow = brandsSheet.getRow(brandIndex);
        if(brandRow == null)
            brandRow = brandsSheet.createRow(brandIndex);
        if(!isFoundBrand) {
            brandRow.createCell(NAME_INDEX).setCellValue(product.getBrand());
            brandRow.createCell(COUNT_INDEX).setCellValue(1);
            for (int i = 2; i < COUNT_DISLIKED_INDEX + 1; i++) {
                if(i == rate + 1) {
                    brandRow.createCell(i).setCellValue(1);
                }
                else {
                    brandRow.createCell(i).setCellValue(0);
                }
            }
        }
        else {
            Cell countAll = brandRow.getCell(COUNT_INDEX);
            countAll.setCellValue(countAll.getNumericCellValue() + 1);

            Cell countRated = brandRow.getCell(rate + 1);
            countRated.setCellValue(countRated.getNumericCellValue() + 1);
        }

        Sheet goodsSheet = goodsWorkbook.getSheet(product.getCategory());
        int productIndex = goodsSheet.getPhysicalNumberOfRows();
        boolean isFoundProduct = false;
        for (int i = 1; i < goodsSheet.getPhysicalNumberOfRows(); i++) {
            if(goodsSheet.getRow(i).getCell(NAME_INDEX).getStringCellValue().equals(product.getName())) {
                productIndex = i;
                isFoundProduct = true;
                break;
            }
        }
        Row productRow = goodsSheet.getRow(productIndex);
        if(productRow == null)
            productRow = goodsSheet.createRow(productIndex);
        if(!isFoundProduct) {
            productRow.createCell(NAME_INDEX).setCellValue(product.getName());
            productRow.createCell(COUNT_INDEX).setCellValue(1);
            for (int i = 2; i < COUNT_DISLIKED_INDEX + 1; i++) {
                if(i == rate + 1) {
                    productRow.createCell(i).setCellValue(1);
                }
                else {
                    productRow.createCell(i).setCellValue(0);
                }
            }
        }
        else {
            Cell countAll = productRow.getCell(COUNT_INDEX);
            countAll.setCellValue(productRow.getCell(COUNT_INDEX).getNumericCellValue() + 1);

            Cell countRated = productRow.getCell(rate + 1);
            countRated.setCellValue(productRow.getCell(rate + 1).getNumericCellValue() + 1);
        }
    }

    public String getName() {
        return name;
    }

    public Workbook getGoodsWorkbook() {
        return goodsWorkbook;
    }

    public Workbook getBrandsWorkbook() {
        return brandsWorkbook;
    }

    public ArrayList<Favorite> getFavoriteCategories() {
        return favoriteCategories;
    }

    public ArrayList<Favorite> getFavoriteCategoriesInBrands() {
        return favoriteCategoriesInBrands;
    }

    public Workbook getTypesWorkbook() {
        return typesWorkbook;
    }

    public ArrayList<ArrayList<Favorite>> getFavoriteTypes() {
        return favoriteTypes;
    }

    public ArrayList<ArrayList<Favorite>> getFavoriteBrands() {
        return favoriteBrands;
    }

    public ArrayList<Favorite> getFavoriteBrandsInCategory(String categoryName) {
        CategoryLoader cl = new CategoryLoader();
        ArrayList<String> categories = cl.getCategories();
        int index = -1;
        for (int i = 0; i < categories.size(); i++) {
            if(categories.get(i).equals(categoryName)) {
                index = i;
                break;
            }
        }
        if(index != -1) {
            return favoriteBrands.get(index);
        }
        return null;
    }
}