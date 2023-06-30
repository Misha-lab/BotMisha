package com.company.algorithms;

import com.company.model.Product;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;

public class MathPart {
    private static final int NAME_INDEX = 0;

    public static double func(int liked, int normal, int disliked) {
        double res;

        int num = liked + disliked + normal;
        if (num == 0) {
            return 0;
        }
        res = (20 * Math.pow(liked,2) - 77 * Math.pow(disliked,2) + 9 * Math.pow(normal,2))/
                Math.pow(num,2) * Math.sqrt(num);
        if(res < 0)
            res = 0;
        return res;
    }

    public static int productIndex(Sheet sheet, String name) {
        int result = -1;
        for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                Cell cell = row.getCell(NAME_INDEX);
                if (cell != null) {
                    if (cell.getStringCellValue().equals(name))
                        return i;
                }
            }
        }
        return result;
    }

    public static ArrayList<Product> unionArrays(ArrayList<Product> first, ArrayList<Product> second) {
        if(first != null) {
            if(second != null) {
                first.addAll(second);
            }
            return first;
        }
        else {
            return second;
        }
    }
}
