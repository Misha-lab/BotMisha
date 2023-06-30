package com.company.model;

public class Product {

    private String name;
    private boolean isAvailable;
    private double price;
    private double discountPrice;
    private String brand;
    private String type;
    private String category;

    private int rating;

    private int discountInPercents;

    public Product(String name, boolean isAvailable, double price, double discountPrice, String brand, String type, String category) {
        this.name = name;
        this.isAvailable = isAvailable;
        this.price = price;
        this.discountPrice = discountPrice;
        this.brand = brand;
        this.type = type;
        this.category = category;

        discountInPercents = Math.round((float)(1 - (discountPrice/price)) * 100);
    }

    public String getName() {
        return name;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public double getPrice() {
        return price;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public String getBrand() {
        return brand;
    }

    public String getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getRating() {
        return rating;
    }

    public int getDiscountInPercents() {
        return discountInPercents;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Название товара: ").append(name).append("\n");
        sb.append("Наличие: ").append(isAvailable ? "В наличии" : "Нет в наличии").append("\n");
        sb.append("Обычная цена: ").append(price).append("\n");
        sb.append("Цена по скидке: ").append(discountPrice).append("\n");
        sb.append("Размер скидки: ").append(discountInPercents).append("\n");
        sb.append("Торговая марка: ").append(brand).append("\n");
        sb.append("Категория: ").append(category).append("\n");
        sb.append("Тип товара: ").append(type).append("\n");
        sb.append("\n");
        return sb.toString();
    }
}
