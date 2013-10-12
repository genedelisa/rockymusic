package com.rockhoppertech.music.modifiers;

import java.math.BigDecimal;

public abstract class AbstractModifier implements Modifier {
    
  

    @Override
    public String getDescription() {
        return "None";
    }

    @Override
    public String getName() {
        return "Untitled";
    }

    public static double quantize(final double value, final double q) {
        double result = 0d;
        int decimalPlaces = 3;
        BigDecimal ratio = null;
     
        if (value == q) {
//          System.err.printf("value %f == %f no change\n",
//                            value,
//                            q);
          return value;
      }
        
        if (value < q) {
            double v = value;
            // make it q at a minimum
            while(v < q)
                v += q;
            ratio = new BigDecimal(v);
            ratio = ratio.setScale(decimalPlaces,BigDecimal.ROUND_HALF_DOWN);
            ratio = ratio.divide(new BigDecimal(q));
            //System.err.println(ratio.doubleValue());
            result = Math.floor(ratio.doubleValue()) * q;
            
            // can't have a duration of 0 for example
            if(result == 0d) {
                result = value < q ? value : q;
            }
          //  System.err.println("<");
            return result;
        }
        
        ratio = new BigDecimal(value );
        ratio = ratio.setScale(decimalPlaces,BigDecimal.ROUND_HALF_UP);
        ratio = ratio.divide(new BigDecimal(q));
       
        //System.err.println(ratio.doubleValue());
       // System.err.println(">");
        result = Math.floor(ratio.doubleValue()) * q;
//        System.err.printf("value is %f quant %f is %f\n",
//                          value,
//                          q,
//                          result);
        return result;
    }
}
