package com.bwf.yibao.framwork.tools;

import com.bwf.yibao.Yibao.entities.Goods;

import java.util.Comparator;

/**
 * Created by nicholas on 2016/9/13.
 */
public class GoodsComparator implements Comparator<Goods> {
    public boolean price ;
    public boolean vote ;
    public boolean time ;
    public boolean saleState ;

    public int result = 0;
    public GoodsComparator(){

    }
    public GoodsComparator(boolean price, boolean vote, boolean time, boolean saleState) {
        this.price = price;
        this.vote = vote;
        this.time = time;
        this.saleState = saleState;
    }

    @Override
    public int compare(Goods goods1, Goods goods2) {
        if(goods1 == goods2)
            return 0;
        if(goods1 != null && goods2 == null)
            return 1;
        if (goods1 == null && goods2 != null)
            return -1;
        if (price)
            return Math.round(goods1.getPrice() - goods2.getPrice());
        if(vote)
            return goods1.getState() - goods2.getState();
        if (time)
            return -goods1.getTime().compareToIgnoreCase(goods2.getTime());
        if (saleState)
            return goods1.vote - goods2.vote;
        return result;
    }
}
