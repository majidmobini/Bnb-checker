package com.home.bnbchecker;

public class Trade {
    //{"tradeId":"152262720-0","blockHeight":152262720,"symbol":"BOLT-4C6_BNB","price":"0.00004575","quantity":"39300.00000000","buyerOrderId":"F620EAC51762100DA71ABA0851B2E0A0B7924BB6-1","sellerOrderId":"75AF4445464DC3F82DE33C7F9974C993569D7F70-135038","buyerSource":1,"sellerSource":0,"buyerId":"bnb17csw43ghvggqmfc6hgy9rvhq5zmeyjakxkfls8","sellerId":"bnb1wkh5g32xfhplst0r83lejaxfjdtf6lmsmdy5mj","buyFee":"BNB:0.00071919;","sellFee":"BNB:0.00071919;","baseAsset":"BOLT-4C6","quoteAsset":"BNB","buySingleFee":"BNB:0.00071919;","sellSingleFee":"BNB:0.00071919;","tickType":"BuyTaker","time":1616587354475
    public String baseAsset;
    public Long blockHeight;
    public String buyFee;
    public String buyerId;
    public String buyerOrderId;
    public String price;
    public String quantity;
    public String quoteAsset;
    public String sellFee;
    public String sellerId;
    public String sellerOrderId;
    public String symbol;
    public Long time;
    public String tradeId;
    public String buySingleFee;
    public String sellSingleFee;
    public String tickType;
    public int count;

    public String getBaseAsset() {
        return baseAsset;
    }

    public void setBaseAsset(String baseAsset) {
        this.baseAsset = baseAsset;
    }

    public Long getBlockHeight() {
        return blockHeight;
    }

    public void setBlockHeight(Long blockHeight) {
        this.blockHeight = blockHeight;
    }

    public String getBuyFee() {
        return buyFee;
    }

    public void setBuyFee(String buyFee) {
        this.buyFee = buyFee;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getBuyerOrderId() {
        return buyerOrderId;
    }

    public void setBuyerOrderId(String buyerOrderId) {
        this.buyerOrderId = buyerOrderId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getQuoteAsset() {
        return quoteAsset;
    }

    public void setQuoteAsset(String quoteAsset) {
        this.quoteAsset = quoteAsset;
    }

    public String getSellFee() {
        return sellFee;
    }

    public void setSellFee(String sellFee) {
        this.sellFee = sellFee;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerOrderId() {
        return sellerOrderId;
    }

    public void setSellerOrderId(String sellerOrderId) {
        this.sellerOrderId = sellerOrderId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

}


