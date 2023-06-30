package com.company.parsers;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class AuchanParser {
    public void parser(String url, String goodsType) throws IOException {

        FileInputStream fis = new FileInputStream("database//AuchanGoods.xls");
        Workbook wb = new HSSFWorkbook(fis);
        Sheet goods = wb.getSheet(goodsType);
        if(goods == null)
            goods = wb.createSheet(goodsType);

        Document doc = Jsoup.connect(url).get();

        Elements links = doc.select("a[class=productCardPictureLink active css-3d15b0]");

        Elements names = doc.select("div[class=product-card__title]");
        Elements images = doc.select("img[srcset]");
        Elements info = doc.select("a[class=sc-jSgupP bnmesn product-card__link]");

        System.out.println(links.size());

        for (int i = 0; i < links.size(); i++) {
            //links
            Element cur = links.get(i);
            String[] temp_info1 = cur.toString().split("href=");
            String[] temp_info2 = temp_info1[1].split(">");
            String info_url = "https://www.auchan.ru/" + temp_info2[0].substring(1, temp_info2[0].length() - 1);
            System.out.println(info_url);
            // name
            /*Element name = names.get(i);
            Row row = goods.createRow(i);
            row.createCell(0).setCellValue(name.text());

            //detailed information
            Element cur = info.get(i);
            String[] temp_info1 = cur.toString().split("href=");
            String[] temp_info2 = temp_info1[1].split("><span");
            String info_url = "https://www.perekrestok.ru" + temp_info2[0].substring(1, temp_info2[0].length() - 1);
            System.out.println(info_url);

            ChromeOptions c = new ChromeOptions();
            c.addArguments("headless");
            System.setProperty("webdriver.chrome.driver", "C:\\Users\\Misha\\MavenProjects\\tools\\chromedriver.exe");
            WebDriver webDriver = new ChromeDriver();
            webDriver.get(info_url);
            WebElement button = webDriver.findElement(By.xpath("//*[@id=\"app\"]/div/main/div/div[1]/div[4]/div[2]/div/button/span"));
            button.click();

            Document allInfo = Jsoup.connect(info_url).get();
            Elements info_titles = allInfo.select("div[class=product-info-string-title]");
            Elements info_values = allInfo.select("div[class=product-info-string-value]");
            Elements info_smth = allInfo.select("div[class=lazyload-wrapper]");
            System.out.println(info_titles.size() + " ::: " + info_values.size() + " ::: " + info_smth.size());
            //brand

            //price


            // image
            Element image = images.get(i);
            String temp = image.toString();
            String[] temp1 = temp.split("src=");
            String[] temp2 = temp1[1].split(" srcset=");
            String imageUrl = temp2[0].substring(1, temp2[0].length() - 1);
            File folder = new File("AuchanImages//" + goodsType);
            if (!folder.exists()) {
                folder.mkdir();
            }
            ImageDownloader.downloadImage(imageUrl, "AuchanImages//" + goodsType + "//" + i + ".jpg", 1);*/
        }

        FileOutputStream fos = new FileOutputStream("database//AuchanGoods.xls");
        wb.write(fos);
        fos.close();
        wb.close();

    }
}
