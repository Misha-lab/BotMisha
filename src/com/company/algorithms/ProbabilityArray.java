package com.company.algorithms;

import java.util.ArrayList;

public class ProbabilityArray {
    private ArrayList<String> names;
    private ArrayList<Double> probabilities;
    private ArrayList<Double> probabilitiesInPercents;
    private ArrayList<Double> highBorders;

    public double sum;

    public ProbabilityArray() {
        names = new ArrayList<>();
        probabilities = new ArrayList<>();
        probabilitiesInPercents = new ArrayList<>();
        highBorders = new ArrayList<>();
        sum = 0;
    }

    public boolean add(String name, double probability) {
        boolean isNew = false;
        int index = names.indexOf(name);
        if (index == -1) {
            names.add(name);
            probabilities.add(probability);
            isNew = true;
        } else {
            probabilities.set(index, probabilities.get(index) + probability);
        }
        sum += probability;
        updateProbabilities();
        return isNew;
    }

    public void updateProbabilities() {
        sum = 0;
        if (probabilities.size() == 0)
            return;
        highBorders.add(0, probabilities.get(0));
        sum += probabilities.get(0);

        for (int i = 1; i < names.size(); i++) {
            highBorders.add(i, highBorders.get(i - 1) + probabilities.get(i));
            sum += probabilities.get(i);
        }
        getProbabilitiesInPercents();
    }

    private boolean isInclude(double value, double left, double right) {
        return (value >= left && value < right);
    }

    public int generateRand() {
        //clearIncorrectValues();
        double rand = (Math.random() * sum);
        int j = 0;
        if (highBorders.size() != 0) {
            while (rand > highBorders.get(j)) {
                j++;
            }
        } else System.out.println("SIZE: 0");
        return j;
    }

    public void getProbabilitiesInPercents() {
        probabilitiesInPercents = new ArrayList<>(probabilities.size());
        for (int i = 0; i < probabilities.size(); i++) {
            probabilitiesInPercents.add(i, probabilities.get(i) * 100 / sum);
        }
    }

    public double getByName(String name) {
        for (int i = 0; i < names.size(); i++) {
            if (names.get(i).equals(name))
                return probabilities.get(i);
        }
        return -1;
    }

    public int indexOf(String name) {
        for (int i = 0; i < names.size(); i++) {
            if (names.get(i).equals(name))
                return i;
        }
        return -1;
    }

    public void printProbabilitiesInPercents() {
        getProbabilitiesInPercents();
        for (int i = 0; i < names.size(); i++) {
            System.out.printf(names.get(i).replace("%", "процентов") + " : %.2f percents\n", probabilitiesInPercents.get(i));
        }
    }

    public void delete(String name) {
        for (int i = 0; i < names.size(); i++) {
            if (names.get(i).equals(name)) {
                names.remove(i);
                probabilities.remove(i);
                break;
            }
        }
    }

    public void delete(int index) {
        names.remove(index);
        probabilities.remove(index);
        updateProbabilities();
    }

    public int[] realTest(int times) {
        int[] freq = new int[probabilities.size()];
        updateProbabilities();

        for (int i = 0; i < times; i++) {
            freq[generateRand()]++;
        }

        for (int i = 0; i < freq.length; i++) {
            System.out.printf(names.get(i) + ": %.2f percents\n", (double) freq[i] * 100 / times);
        }
        return freq;
    }

    public void clear() {
        names.clear();
        probabilities.clear();
        probabilitiesInPercents.clear();
        highBorders.clear();
        sum = 0;
    }

    public void clearIncorrectValues() {
        for (int i = 0; i < names.size(); i++) {
            if (probabilities.get(i) <= 0) {
                names.remove(i);
                probabilities.remove(i);
            }
        }
        updateProbabilities();
    }

    public double getValueAt(int index) {
        return probabilities.get(index);
    }

    public void setValueAt(int index, double value) {
        probabilities.set(index, value);
        updateProbabilities();
    }

    public ArrayList<String> getNames() {
        return names;
    }
}
