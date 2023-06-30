package com.company.parsers;

import com.company.algorithms.MathPart;
import org.apache.poi.ss.usermodel.*;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class PerekrestokParser {

    public static final int NAME_INDEX = 0;
    public static final int AVAIL_INDEX = 1;
    public static final int USUAL_PRICE_INDEX = 2;
    public static final int DISCOUNT_PRICE_INDEX = 3;
    public static final int BRAND_INDEX = 4;
    public static final int TYPE_INDEX = 5;

    public static final String URL_CATEGORIES = "data_texts//URL_categories1.txt";

    public static int counter;

    public PerekrestokParser() {
        try {
            FileInputStream fis = new FileInputStream("database//goods.xls");
            Workbook wb = new HSSFWorkbook(fis);
            for (int i = 0; i < wb.getNumberOfSheets(); i++) {
                Sheet sheet = wb.getSheetAt(i);
                clearAvailability(sheet);
                createHeader(wb, sheet);
            }
            FileOutputStream fos = new FileOutputStream("database//goods.xls");
            wb.write(fos);
            fos.close();
            wb.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void parseAll() {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(URL_CATEGORIES), "Cp1251");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (scanner != null) {
            while (scanner.hasNextLine()) {
                String[] data = scanner.nextLine().split(": ");
                if (!data[0].startsWith("...")) {
                    try {
                        parser(data[1], data[0]);
                    } catch (IOException ex) {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException intex) {
                            intex.printStackTrace();
                        }
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    public void parser(String url, String goodsType) throws IOException {

        FileInputStream fis = new FileInputStream("database//goods.xls");
        Workbook wb = new HSSFWorkbook(fis);
        Sheet goods = wb.getSheet(goodsType);
        if (goods == null)
            goods = wb.createSheet(goodsType);
        //System.out.println(goods.getPhysicalNumberOfRows());

        Document doc = Jsoup.connect(url).get();

        ArrayList<String> list = new ArrayList<>();
        String path = "data_texts//typesInCategories//" + goodsType + ".txt";
        Scanner scanner = new Scanner(new File(path), "Cp1251");
        while (scanner.hasNextLine()) {
            list.add(scanner.nextLine());
        }
        System.out.println(list);

        Elements names = doc.select("div[class=product-card__title]");
        //Elements images = doc.select("img[srcset]");
        Elements images = doc.select("div[class=product-card__image-wrapper]");
        Elements info = doc.select("a[class=sc-bkzZxe fAwjHM product-card__link]");
        Elements productsAvailability = doc.select("div[class=sc-iWFSnp gHjKcW product-card__balance-badge]");
        Elements pricesInfo = doc.select("div[class=product-card__price]");

        Elements titles = doc.select("img[title]");
        System.out.println("Количество товаров: " + names.size());
        System.out.println(images.size());

        for (int i = 0; i < names.size(); i++) {

            counter++;

            // name
            Element nameElem = names.get(i);
            String name = nameElem.text();
            System.out.println(name);

            //price
            String discountPrice = "-";
            String usualPrice;
            Element price = pricesInfo.get(i);
            String[] prices = price.text().split(" ₽");
            if (prices.length > 1) {
                discountPrice = prices[0].replace(",", ".").replace(" ", "");
                usualPrice = prices[1].replace(",", ".").replace(" ", "").trim();
            } else {
                usualPrice = prices[0].replace(",", ".").replace(" ", "");
            }


            //availability
            String isAvailability = "+";
            //String isAvailability = "-";
            /*if(!badTagInAvailability) {
                String availabilityStr = productsAvailability.get(i).text();
                if (availabilityStr.startsWith("В наличии")) {
                    isAvailability = "+";
                }
                else {
                    System.out.println("WWWWOOOOOOOOOOOOOOOOOOOOOOWWWW!!!!!!");
                }
                //System.out.println("LALALA");
            }*/

            Element cur = info.get(i);
            String[] temp_info1 = cur.toString().split("href=");
            String[] temp_info2 = temp_info1[1].split("><span");
            String info_url = "https://www.perekrestok.ru" + temp_info2[0].substring(1, temp_info2[0].length() - 1);

            int productIndex = MathPart.productIndex(goods, name);
            Row row;
            if (productIndex == -1) {
                //row = goods.getRow(goods.getPhysicalNumberOfRows());
                row = goods.createRow(goods.getPhysicalNumberOfRows());
            } else {
                row = goods.getRow(productIndex);
                updateData(row, isAvailability, usualPrice, discountPrice);
            }

            // IMAGE
            File folder = new File("images//" + goodsType);
            if (!folder.exists()) {
                folder.mkdir();
            }
            File file = new File("images//" + goodsType + "//" + name + ".jpg");
            if (!file.exists()) {
                Element image = images.get(i);
                String imageStr = image.toString();
                //System.out.println(imageStr);
                String[] temp1 = imageStr.split("src=");
                if (temp1.length > 1) {
                    String[] temp2 = temp1[1].split(" srcset=");
                    if (temp2.length > 1) {
                        String imageUrl = temp2[0].substring(1, temp2[0].length() - 1);
                        System.out.println(imageUrl);
                        ImageDownloader.downloadImage(imageUrl, "images//" + goodsType + "//" + name + ".jpg", 1);
                    }
                }
            }

            if (productIndex == -1) {
                Document allInfo = null;
                try {
                    allInfo = allInfoDocument(info_url);
                } catch (HttpStatusException ex) {
                    FileOutputStream fos = new FileOutputStream("database//goods.xls");
                    goods.removeRow(row);
                    wb.write(fos);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException inter) {
                        inter.printStackTrace();
                    }
                    ex.printStackTrace();
                }

                if (allInfo != null) {

                    // BRAND
                    String brand;
                    brand = getBrand(allInfo);

                    // TYPE
                    String type = "ОСТАЛЬНОЕ";
                    for (int j = 0; j < list.size(); j++) {
                        String[] temp = list.get(j).split(":");
                        if (temp.length > 1) {
                            String[] filling = temp[0].split(">");
                            if(filling.length > 1) {
                                if (name.toUpperCase().matches("(.*)" + filling[0] + "(.*)")) {
                                    boolean isMatches = true;
                                    for (int k = 1; k < filling.length; k++) {
                                        if (!name.toUpperCase().matches("(.*)" + filling[k] + "(.*)")) {
                                            isMatches = false;
                                        }
                                    }
                                    if (isMatches) {
                                        type = temp[1];
                                        break;
                                    }
                                }
                            } else {
                                if (name.toUpperCase().matches("(.*)" + filling[0] + "(.*)")) {
                                    type = temp[1];
                                    break;
                                }
                            }
                        }
                        else {
                            if (name.toUpperCase().matches("(.*)" + temp[0] + "(.*)")) {
                                type = temp[0];
                                break;
                            }
                        }
                    }
                    writeToDatabase(row, name, isAvailability, usualPrice, discountPrice, brand, type);
                }
            }
        }

        FileOutputStream fos = new FileOutputStream("database//goods.xls");
        wb.write(fos);
        fos.close();
        wb.close();
    }

    public Document allInfoDocument(String info_url) throws IOException {
        Document allInfo;
        allInfo = Jsoup.connect(info_url).get();
        return allInfo;
    }

    public String getBrand(Document allInfo) {
        Element brandElt = allInfo.select("h3[class=product-brand__title]").first();
        String brand = "Без торговой марки";
        if (brandElt != null) {
            brand = brandElt.text();
        }

        return brand;
    }


    public void writeToDatabase(Row row, String name, String isAvailability, String usualPrice, String discountPrice, String brand, String type) {
        if (!name.equals("")) {
            row.createCell(NAME_INDEX).setCellValue(name);
            row.createCell(AVAIL_INDEX).setCellValue(isAvailability);
            row.createCell(USUAL_PRICE_INDEX).setCellValue(usualPrice);
            row.createCell(DISCOUNT_PRICE_INDEX).setCellValue(discountPrice);
            row.createCell(BRAND_INDEX).setCellValue(brand);
            row.createCell(TYPE_INDEX).setCellValue(type);
        }
    }

    public boolean isHaveBrand(Row row) {
        return row.getCell(BRAND_INDEX) != null;
    }

    public boolean isHaveType(Row row) {
        return row.getCell(TYPE_INDEX) != null;
    }

    public void updateData(Row row, String isAvailability, String usualPrice, String discountPrice) {
        row.createCell(AVAIL_INDEX).setCellValue(isAvailability);
        row.createCell(USUAL_PRICE_INDEX).setCellValue(usualPrice);
        row.createCell(DISCOUNT_PRICE_INDEX).setCellValue(discountPrice);
    }

    public void clearAvailability(Sheet sheet) {
        for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
            Row row = sheet.getRow(i);
            if (row == null)
                row = sheet.createRow(i);
            row.createCell(1).setCellValue("-");
        }
    }

    public void createHeader(Workbook wb, Sheet sheet) {
        String[] titles = {"Название", "Наличие", "Обычная цена", "Цена по скидке", "Торговая марка",
                "Вид товара"};
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
}