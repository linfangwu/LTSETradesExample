package LTSE.tradingExample;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import LTSE.tradingExample.model.SIDE;
import LTSE.tradingExample.model.Trade;
import LTSE.tradingExample.util.Constants;
import LTSE.tradingExample.util.ReportMill;
import LTSE.tradingExample.util.TradingExampleUtil;
/**
 * 
 * @author lwu
 *
 */
public class App 
{
	public List<Trade> validTrades = new ArrayList<>();
	public List<Trade> invalidTrades = new ArrayList<>() ;
	private List<String> validSymbols = TradingExampleUtil.getInstance().getValidSymbols() ;
	private List<String> validFirms = TradingExampleUtil.getInstance().getValidFirms() ;

	private static final char DEFAULT_SEPARATOR = ',';
	private static final char DEFAULT_QUOTE = '"';

	Map<Date, Map<String, List<Integer>>> timeBrokerNameIdsMap = new HashMap<>();
	
	final String csvFile = "trades.csv" ;
	
	private ObjectMapper mapper = new ObjectMapper();
	
	public void getResult() throws IOException {

		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(csvFile).getFile());
		
		Scanner scanner = new Scanner(file);
		if(scanner.hasNext()) scanner.nextLine() ;
		
		while (scanner.hasNext()) {
			
            List<String> line = parseLine(scanner.nextLine());
            loadTrades(line) ;
		}
		scanner.close();
	}
	
	public void loadTrades(List<String> line) {
		Trade trade = new Trade() ;
		boolean isValid = true ;
		String str= line.get(Constants.TIMESTAMP_POS).trim();
		Date timeStamp = null;
		try {
			timeStamp = TradingExampleUtil.dateFormatAmerica.parse(str) ;
		} catch (ParseException e) {
			isValid = false ;
		}
		trade.setDate( timeStamp) ;

		str = line.get(Constants.BROKER_POS).trim();
		trade.setBrokerName(str);
		if (!validFirms.contains(str)) {
			isValid = false ;
		}

		str = line.get(Constants.SEQUENCE_ID);
		Integer id = null;
		try {
			id = Integer.parseInt(str) ;
			if (id < 1) isValid = false ;
		} catch (NumberFormatException e ) {
			isValid = false ;
		}
		trade.setSequenceId(id);

		str = line.get(Constants.TYPE_POS) ;
		if (str == null || str.isEmpty()) {
			isValid = false ;
		}
		trade.setType(str);

		str = line.get(Constants.SYMBOL_POS);
		if (str == null || str.isEmpty() || !validSymbols.contains(str)){
			isValid = false ;
		}
		trade.setSymbol(str);

		str = line.get(Constants.QUANTITY_POS);
		Long quantity = null;
		try {
			quantity = Long.parseLong(str) ;
			if (quantity < 1) isValid = false ;
		} catch (NumberFormatException e ) {
			isValid = false ;
		}
		trade.setQuantities(quantity);	

		str = line.get(Constants.PRICE_POS);
		Double price = null ;
		try {
			price = Double.parseDouble(str) ;
			if (price <=0) isValid = false;
		} catch (NumberFormatException e ) {
			isValid = false ;
		}
		trade.setPrice(price);


		str = line.get(Constants.SIDE_POS);
		SIDE side = null ;
		try {
			side = SIDE.getByValue(str) ;
			if (side == null) isValid = false ;
		} catch (IllegalArgumentException e) {
			isValid = false ;
		}
		trade.setSide(side);

		if (isValid) {
			Map<String, List<Integer>> brokerMap = timeBrokerNameIdsMap.get(timeStamp) ;
			if ( brokerMap == null) {
				brokerMap = new HashMap<>();
				timeBrokerNameIdsMap.put(timeStamp,brokerMap);
			}
			List<Integer> idList = brokerMap.get(trade.getBrokerName());
			if (idList == null) {
				idList = new ArrayList<>();
			}
			if (idList.size() >= 3){
				isValid = false ;
			} else {
				if (idList.contains(trade.getSequenceId())) isValid = false ;
				else {
					idList.add(trade.getSequenceId());
				}
			}
		}

		if (isValid) {
			validTrades.add(trade) ;
		} else {
			invalidTrades.add(trade) ;

		}

	}
    public void writeToCSV() throws Exception {
    	writeToCSVBrokers("output", "validBrokers.csv", "VALID TRADES", validTrades);
    	writeToCSVBrokers("output", "invalidBrokers.csv", "INVALID TRADES", invalidTrades);
    	writeTradesToCSV("output", "allValidTrades.csv", "VALID TRADES", validTrades) ;
    	writeTradesToCSV("output", "allInValidTrades.csv", "INVALID TRADES", invalidTrades) ;
    }
    
    public void convertToJson() throws JsonProcessingException {
    	String jsonInString = mapper.writeValueAsString(validTrades);
    	System.out.println("Print valid trades in Json") ;
		System.out.println(jsonInString);
		
		jsonInString = mapper.writeValueAsString(invalidTrades) ;
		System.out.println("Print invalid trades in Json") ;
		System.out.println(jsonInString);
    }
    
    
    private void writeToCSVBrokers(String outputDir, String fileName, String title, List<Trade> trades) throws Exception {
    	ReportMill report = new ReportMill(outputDir);
    	report.open (fileName);
    	report.write(title);
    	report.write( "broker", "sequence id");
    	for (Trade trade : trades) {
    		report.write(trade.getBrokerName(),
    				trade.getSequenceId().toString()
    				);
    	
    	}
    	
    	File resultFile = report.getOutputFile();
    	System.out.println(resultFile.getAbsolutePath()) ;
    	report.close();
    }
    
    private void writeTradesToCSV(String outputDir, String fileName, String title, List<Trade> trades) throws Exception {
    	ReportMill report = new ReportMill(outputDir);
    	report.open (fileName);
    	report.write(title);
    	report.write("Time stamp", "broker", "sequence id", "type" , "Symbol", "Quantity", "Price", "Side");
    	for (Trade trade : trades) {
    		report.write(TradingExampleUtil.dateFormatAmerica.format(trade.getDate()),
    				trade.getBrokerName(),
    				trade.getSequenceId().toString(),
    				trade.getType(),
    				trade.getSymbol(),
    				trade.getQuantities().toString(),
    				trade.getPrice().toString(),
    				trade.getSide() == null ? "" : trade.getSide().getValue()
    				);
    	
    	}
    	
    	File resultFile = report.getOutputFile();
    	System.out.println(resultFile.getAbsolutePath()) ;
    	report.close();
    }
	
	
	public static List<String> parseLine(String cvsLine) {
        return parseLine(cvsLine, DEFAULT_SEPARATOR, DEFAULT_QUOTE);
    }

    public static List<String> parseLine(String cvsLine, char separators) {
        return parseLine(cvsLine, separators, DEFAULT_QUOTE);
    }
	
	 public static List<String> parseLine(String cvsLine, char separators, char customQuote) {
		
	        List<String> result = new ArrayList<>();

	        //if empty, return!
	        if (cvsLine == null || cvsLine.isEmpty()) {
	            return result;
	        }

	        if (customQuote == ' ') {
	            customQuote = DEFAULT_QUOTE;
	        }

	        if (separators == ' ') {
	            separators = DEFAULT_SEPARATOR;
	        }

	        StringBuffer curVal = new StringBuffer();
	        boolean inQuotes = false;
	        boolean startCollectChar = false;
	        boolean doubleQuotesInColumn = false;

	        char[] chars = cvsLine.toCharArray();

	        for (char ch : chars) {

	            if (inQuotes) {
	                startCollectChar = true;
	                if (ch == customQuote) {
	                    inQuotes = false;
	                    doubleQuotesInColumn = false;
	                } else {

	                    //Fixed : allow "" in custom quote enclosed
	                    if (ch == '\"') {
	                        if (!doubleQuotesInColumn) {
	                            curVal.append(ch);
	                            doubleQuotesInColumn = true;
	                        }
	                    } else {
	                        curVal.append(ch);
	                    }

	                }
	            } else {
	                if (ch == customQuote) {

	                    inQuotes = true;

	                    //Fixed : allow "" in empty quote enclosed
	                    if (chars[0] != '"' && customQuote == '\"') {
	                        curVal.append('"');
	                    }

	                    //double quotes in column will hit this!
	                    if (startCollectChar) {
	                        curVal.append('"');
	                    }

	                } else if (ch == separators) {

	                    result.add(curVal.toString());

	                    curVal = new StringBuffer();
	                    startCollectChar = false;

	                } else if (ch == '\r') {
	                    //ignore LF characters
	                    continue;
	                } else if (ch == '\n') {
	                    //the end, break!
	                    break;
	                } else {
	                    curVal.append(ch);
	                }
	            }

	        }

	        result.add(curVal.toString());

	        return result;
	    }

	
	
    public static void main( String[] args ) throws Exception
    {
    	App app = new App();
    	app.getResult();
    	app.writeToCSV();
    	app.convertToJson();
    	
    }
}
