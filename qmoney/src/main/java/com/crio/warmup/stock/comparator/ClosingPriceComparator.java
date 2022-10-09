package com.crio.warmup.stock.comparator;

import java.util.Comparator;
import com.crio.warmup.stock.dto.TiingoCandle;

public class ClosingPriceComparator implements Comparator<TiingoCandle>{

    @Override
    public int compare(TiingoCandle arg0, TiingoCandle arg1) {

        if(arg0.getClose() > arg1.getClose()){
            return 1;
        }else if(arg0.getClose() > arg1.getClose()){
            return -1;
        }

        return 0;
    }
    
}
