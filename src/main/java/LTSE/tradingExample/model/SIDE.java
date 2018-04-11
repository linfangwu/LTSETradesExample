package LTSE.tradingExample.model;

import java.util.HashMap;
import java.util.Map;

public enum SIDE {
   SELL ("Sell"),
   BUY  ("Buy");
   
   private static final Map<String, SIDE> MY_MAP = new HashMap<>();
   static {
	   for (SIDE myEnum : values()) {
		   MY_MAP.put(myEnum.getValue(), myEnum);
	   }
   }
   
   private String value ;
   private SIDE( String value) {
	   this.value = value;
   }
   
   public String getValue() {
	   return this.value ;
   }
   
   public static SIDE getByValue(String str) {
	   return MY_MAP.get(str);
   }
   
  
}
