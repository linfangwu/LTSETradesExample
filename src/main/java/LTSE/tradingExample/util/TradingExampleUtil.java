package LTSE.tradingExample.util;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 
 * @author lwu
 *
 */
public class TradingExampleUtil {

   // public static SimpleDateFormat dateFormatAmerica = new SimpleDateFormat("MM/dd/yyyy ',' h:mm" );
	public static SimpleDateFormat dateFormatAmerica = new SimpleDateFormat("MM/dd/yyyy H:mm" );
	private static TradingExampleUtil instance ;
	private TradingExampleUtil() {

	}
	public static TradingExampleUtil getInstance() {
		if (instance == null) {
			instance = new TradingExampleUtil() ;
		}
		return instance ;
	}
	public List<String> getValidFirms() {
        return getValidStringList("firms.txt") ;
	}
	
	public List<String> getValidSymbols() {
		return getValidStringList("symbols.txt") ;
	}


	private  List<String>  getValidStringList(String fileName) {
		List<String> symbolList = new ArrayList<>() ;
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());

		try (Scanner scanner = new Scanner(file)) {

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				symbolList.add(line.trim()) ;
			}

			scanner.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return symbolList ;
	}


}
