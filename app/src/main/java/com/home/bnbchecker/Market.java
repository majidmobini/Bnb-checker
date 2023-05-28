package com.home.bnbchecker;

public class Market {
   // {"base_asset_symbol":"ADA-9F4","list_price":"0.08000000","lot_size":"1.00000000","quote_asset_symbol":"BUSD-BD1","tick_size":"0.00001000"}
    public String base_asset_symbol;
    public String quote_asset_symbol;
    public String list_price;
    public String tick_size;
    public String lot_size;

    public String getBaseAssetSymbol() {
        return base_asset_symbol;
    }

    public void setBaseAssetSymbol(String baseAssetSymbol) {
        this.base_asset_symbol = baseAssetSymbol;
    }


    public String getQuoteAssetSymbol() {
        return quote_asset_symbol;
    }

    public void setQuoteAssetSymbol(String quoteAssetSymbol) {
        this.quote_asset_symbol = quoteAssetSymbol;
    }


    public String getPrice() {
        return list_price;
    }

    public void setPrice(String price) {
        this.list_price = price;
    }


    public String getTickSize() {
        return tick_size;
    }

    public void setTickSize(String tickSize) {
        this.tick_size = tickSize;
    }


    public String getLotSize() {
        return lot_size;
    }

    public void setLotSize(String lotSize) {
        this.lot_size = lotSize;
    }
}
