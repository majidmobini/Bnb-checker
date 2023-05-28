package com.home.bnbchecker;

public class Statistic {

    private static final long serialVersionUID = 1L;

    private String symbol;
    private int tradeSize;
    private String maxPrice;
    private String minPrice;
    private double delta;
    public String priceChangePercent;
    String lastPrice="";
    String lastQuantity="";
    String highPrice="";
    String lowPrice="";
    String bidPrice="";
    String bidQuantity="";
    String askPrice="";
    String askQuantity="";
    String quoteVolume="";
    String profitPercent = "";
    String weightedAvgPrice = "";
    long count;

    public Statistic() {
    }

    public Statistic(String symbol, int tradeSize, String maxPrice, String minPrice, double delta) {
        this.symbol = symbol;
        this.tradeSize = tradeSize;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
        this.delta = delta;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getTradeSize() {
        return tradeSize;
    }

    public void setTradeSize(int tradeSize) {
        this.tradeSize = tradeSize;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public double getDelta() {
        return delta;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }

}
