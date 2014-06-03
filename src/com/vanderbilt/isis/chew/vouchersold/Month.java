package com.vanderbilt.isis.chew.vouchersold;

public enum Month {

	January(0),
	February(1),
	March(2),
	April(3),
	May(4),
	June(5),
	July(6),
	August(7),
	September(8),
	October(9),
	November(10),
	December(11);
	
	private int value;
	
	private Month(int value){
		
		this.value = value;
	}
	
	public int getValue(){
		
		return this.value;
	}
	
	public boolean equals(Month m){
		
		return this.value == m.value;
	}
	
	public String toString(){
		
		switch(value){
		
		case 0:
			return "January";
		case 1:
			return "February";
		case 2:
			return "March";
		case 3:
			return "April";
		case 4:
			return "May";
		case 5:
			return "June";
		case 6:
			return "July";
		case 7:
			return "August";
		case 8:
			return "September";
		case 9:
			return "October";
		case 10:
			return "November";
		case 11:
			return "December";
		default:
			return "";
		}
	}
}
