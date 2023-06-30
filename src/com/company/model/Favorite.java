package com.company.model;

public class Favorite {
    private String name;
    private boolean isFavorite;

    public Favorite(String name, boolean isFavorite) {
        this.name = name;
        this.isFavorite = isFavorite;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String toString() {
        return name + " ::: " + isFavorite;
    }
}
