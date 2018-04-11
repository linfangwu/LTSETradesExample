package LTSE.tradingExample;

import java.util.List;

import org.junit.Test;

import LTSE.tradingExample.util.TradingExampleUtil;
import junit.framework.TestCase;

public class TradingExampleUtilTest extends TestCase {
	
	@Test
	public void testGetValidSymbols() {
		List<String> symbols = TradingExampleUtil.getInstance().getValidSymbols() ;
		System.out.println(symbols) ;
	}
	
	@Test
	public void testGetValidFirms() {
		List<String> firms = TradingExampleUtil.getInstance().getValidFirms();
		System.out.println(firms) ;
	}

}
