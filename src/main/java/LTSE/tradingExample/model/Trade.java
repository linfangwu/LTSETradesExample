package LTSE.tradingExample.model;

import java.util.Date;

public class Trade {
	private Date date ;
	private String brokerName ;
	private Integer sequenceId;
	private String type ;
	private String symbol;
	private Long quantities; 
	private Double price ;
    private SIDE side ;
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getBrokerName() {
		return brokerName;
	}
	public void setBrokerName(String brokerName) {
		this.brokerName = brokerName;
	}
	public Integer getSequenceId() {
		return sequenceId;
	}
	public void setSequenceId(Integer sequenceId) {
		this.sequenceId = sequenceId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public Long getQuantities() {
		return quantities;
	}
	public void setQuantities(Long quantities) {
		this.quantities = quantities;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public SIDE getSide() {
		return side;
	}
	public void setSide(SIDE side) {
		this.side = side;
	}
    
    
}
