package com.company;

import com.company.algorithms.CategorySelector;
import com.company.algorithms.GoodsSelector;
import com.company.algorithms.TypesSelector;
import com.company.model.FoodBasket;
import com.company.model.GoodsDatabase;
import com.company.model.User;
import com.company.parsers.PerekrestokParser;
import com.company.view.Client;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
//        PerekrestokParser p = new PerekrestokParser();
//        p.parseAll();

        GoodsDatabase gs = new GoodsDatabase();
        gs.allInfoToFile(GoodsDatabase.BRAND_INDEX);
        gs.allInfoToFile(GoodsDatabase.TYPE_INDEX);

        new Client();

        /*TypesSelector typesSelector = new TypesSelector(new User("Миша"), "Сырки");
        typesSelector.algorithm1();
        typesSelector.algorithm2();
        typesSelector.getProbabilityArray().printProbabilitiesInPercents();
        System.out.println();
        typesSelector.algorithm4();
        typesSelector.getProbabilityArray().printProbabilitiesInPercents();*/

        /*CategorySelector categorySelector = new CategorySelector(new User("Миша"));
        categorySelector.algorithm1();
        categorySelector.algorithm2();
        categorySelector.getProbabilityArray().printProbabilitiesInPercents();
        System.out.println();
        categorySelector.algorithm4();
        categorySelector.getProbabilityArray().printProbabilitiesInPercents();*/

        //PerekrestokParser p = new PerekrestokParser();
        //p.parseAll();
        /*try {
            p.parser("https://www.perekrestok.ru/cat/c/197/pecene-krekery-vafli-praniki","Печенье, вафли, пряники");
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        //User me = new User("Misha");

        /*User user = new User("Admin");

        FoodBasket foodBasket = new FoodBasket(user);
        foodBasket.generateBasket(3000);*/
    }
}
