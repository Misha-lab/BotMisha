package com.company.model;

import com.company.algorithms.ProbabilityArray;
import com.company.io.CategoryLoader;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.*;
import java.util.ArrayList;

public class GoodsDatabase {
    public static final int NAME_INDEX = 0;
    public static final int AVAIL_INDEX = 1;
    public static final int USUAL_PRICE_INDEX = 2;
    public static final int DISCOUNT_PRICE_INDEX = 3;
    public static final int BRAND_INDEX = 4;
    public static final int TYPE_INDEX = 5;

    public static final String DATABASE_PATH = "database/goods.xls";
    public static final String ALL_BRANDS_PATH = "data_texts/allBrands.txt";
    public static final String ALL_TYPES_PATH = "data_texts/allTypes.txt";
    public static final String CATEGORY_COMPATIBILITY_PATH = "database/categoryCompatibility.xls";
    public static final String CATEGORY_COMPATIBILITY_SHEET_NAME = "Совместимость категорий";
    public static final String TYPES_COMPATIBILITY_PATH = "database/typesCompatibility.xls";

    private FileInputStream fis;
    private Workbook wb;
    private ArrayList<String> categories;
    private CategoryLoader categoryLoader;

    public GoodsDatabase() {
        try {
            fis = new FileInputStream(DATABASE_PATH);
            wb = new HSSFWorkbook(fis);
            categoryLoader = new CategoryLoader();
            categories = categoryLoader.getCategories();
            createCategoryCompatibilityFile(false);
            createTypesCompatibilityFile(false);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void createCategoryCompatibilityFile(boolean needUpdate) {
        try {
            File file = new File(CATEGORY_COMPATIBILITY_PATH);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileInputStream fis = new FileInputStream(CATEGORY_COMPATIBILITY_PATH);
            Workbook categoryComp = new HSSFWorkbook(fis);
            Sheet sheet = categoryComp.getSheet(CATEGORY_COMPATIBILITY_SHEET_NAME);
            if (sheet == null || needUpdate) {
                sheet = categoryComp.createSheet(CATEGORY_COMPATIBILITY_SHEET_NAME);
                Row row0 = sheet.getRow(0);
                if (row0 == null)
                    row0 = sheet.createRow(0);
                for (int i = 0; i < categories.size(); i++) {
                    row0.createCell(i + 1).setCellValue(categories.get(i));

                    Row rowI = sheet.getRow(i + 1);
                    if (rowI == null)
                        rowI = sheet.createRow(i + 1);
                    rowI.createCell(0).setCellValue(categories.get(i));
                }
                FileOutputStream fos = new FileOutputStream(CATEGORY_COMPATIBILITY_PATH);
                categoryComp.write(fos);
                fos.flush();
                fos.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void createTypesCompatibilityFile(boolean needUpdate) {
        try {
            FileInputStream fis;
            Workbook typesComp;

            File file = new File(TYPES_COMPATIBILITY_PATH);
            if (!file.exists()) {
                typesComp = new HSSFWorkbook();
            }
            else {
                fis = new FileInputStream(TYPES_COMPATIBILITY_PATH);
                typesComp = new HSSFWorkbook(fis);
            }

            for (int i = 0; i < categories.size(); i++) {
                Sheet sheet = typesComp.getSheet(categories.get(i));
                if (sheet == null || needUpdate) {
                    sheet = typesComp.createSheet(categories.get(i));
                    ArrayList<String> typesInCategory = categoryLoader.getTypes().get(i);
                    Row row0 = sheet.getRow(0);
                    if (row0 == null)
                        row0 = sheet.createRow(0);
                    for (int j = 0; j < typesInCategory.size(); j++) {
                        row0.createCell(j + 1).setCellValue(typesInCategory.get(j));

                        Row rowI = sheet.getRow(j + 1);
                        if (rowI == null)
                            rowI = sheet.createRow(j + 1);
                        rowI.createCell(0).setCellValue(typesInCategory.get(j));
                    }
                }
            }
            FileOutputStream fos = new FileOutputStream(TYPES_COMPATIBILITY_PATH);
            typesComp.write(fos);
            fos.flush();
            fos.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public ArrayList<Product> loadGoodsFromTypeAndCategory(String categoryName, String typeName) {
        ArrayList<Product> goods = new ArrayList<>();
        Sheet sheet = wb.getSheet(categoryName);
        for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
            Row row = sheet.getRow(i);
            if (row.getCell(TYPE_INDEX).getStringCellValue().equals(typeName)) {
                String name = row.getCell(NAME_INDEX).getStringCellValue();
                boolean isAvailability = row.getCell(AVAIL_INDEX).getStringCellValue().equals("+");
                if (isAvailability) {

                    String price1 = row.getCell(USUAL_PRICE_INDEX).getStringCellValue();
                    double usualPrice = Double.parseDouble(price1);

                    String price2 = row.getCell(DISCOUNT_PRICE_INDEX).getStringCellValue();
                    if (price2.equals("-")) {
                        price2 = price1;
                    }
                    double discountPrice = Double.parseDouble(price2);

                    String brand = row.getCell(BRAND_INDEX).getStringCellValue();

                    goods.add(new Product(name, true, usualPrice, discountPrice,
                            brand, typeName, categoryName));
                }
            }
        }
        return goods;
    }

    public Product generateRandProduct() {
        ProbabilityArray probabilityArray = new ProbabilityArray();
        for (int i = 0; i < categories.size(); i++) {
            String categoryName = categories.get(i);
            probabilityArray.add(categoryName, wb.getSheet(categoryName).getPhysicalNumberOfRows() - 1);
        }
        do {
            int randCategory = probabilityArray.generateRand();
            String categoryName = categories.get(randCategory);
            Sheet sheet = wb.getSheet(categoryName);
            if (sheet != null) {
                int randProduct = (int) (Math.random() * (sheet.getPhysicalNumberOfRows() - 1) + 1);
                Row row = sheet.getRow(randProduct);

                String name = row.getCell(NAME_INDEX).getStringCellValue();
                boolean isAvailability = row.getCell(AVAIL_INDEX).getStringCellValue().equals("+");

                if (isAvailability) {
                    String price1 = row.getCell(USUAL_PRICE_INDEX).getStringCellValue().replace(",", ".").replace(" ", "");
                    double usualPrice = Double.parseDouble(price1);

                    String price2 = row.getCell(DISCOUNT_PRICE_INDEX).getStringCellValue().replace(",", ".").replace(" ", "");
                    if (price2.equals("-")) {
                        price2 = price1;
                    }
                    double discountPrice = Double.parseDouble(price2);
                    String brand = row.getCell(BRAND_INDEX).getStringCellValue();
                    String typeName = row.getCell(TYPE_INDEX).getStringCellValue();

                    return new Product(name, isAvailability, usualPrice, discountPrice,
                            brand, typeName, categoryName);
                }
            }
        } while (true);
    }

    public int goodsWithDiscountCount(ArrayList<Product> products) {
        int counter = 0;
        for (int i = 0; i < products.size(); i++) {
            if(products.get(i).getDiscountInPercents() != 0)
                counter++;
        }
        return counter;
    }

    public void allInfoToFile(int index) {
        String filename = "";
        if (index == BRAND_INDEX)
            filename = ALL_BRANDS_PATH;
        else if (index == TYPE_INDEX) {
            filename = ALL_TYPES_PATH;
        }
        try {
            File file = new File(filename);
            if (!file.exists())
                file.createNewFile();
            PrintWriter pw = new PrintWriter(file, "Cp1251");

            for (int i = 0; i < categories.size(); i++) {
                Sheet sheet = wb.getSheet(categories.get(i));
                if (sheet != null) {
                    ArrayList<String> brands = new ArrayList<>();
                    for (int j = 1; j < sheet.getPhysicalNumberOfRows(); j++) {
                        String brand = sheet.getRow(j).getCell(index).getStringCellValue();
                        boolean isFound = false;
                        for (String temp : brands) {
                            if (temp.equals(brand)) {
                                isFound = true;
                                break;
                            }
                        }
                        if (!isFound) {
                            brands.add(brand);
                        }
                    }
                    pw.write(":::" + sheet.getSheetName() + "\r\n");
                    for (String temp : brands)
                        pw.write(temp + "\r\n");
                }
            }
            pw.flush();
            pw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}