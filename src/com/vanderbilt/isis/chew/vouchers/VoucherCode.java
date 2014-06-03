package com.vanderbilt.isis.chew.vouchers;

public enum VoucherCode {
	
	CV6("CVV6"), CV10("CVV10"), A("A"), A2("A2"), B("B"), B2("B2"),
	L("L"), L2("L2"), G("G"), G2("G2"), T2("T2"), T("T"), K("K"),
	K2("K2"), E("E"), E2("E2"), P("P"), P2("P2");
	
	private String code;
	
	private VoucherCode(String code){		
		this.code = code;
	}
	
	public String getCode(){		
		return this.code;
	}
	
	public String toString(){		
		return this.code;
	}
	
	public static VoucherCode getVoucherCodeFromValue(String value) {
		
		if(value.equals(CV6.getCode())){
			return VoucherCode.CV6;
		}else if (value.equals(CV10.getCode())){
			return VoucherCode.CV10;
		}else{
			return null;
		}
	}
	
	public static boolean isCashCode(String value){
		
		if(value.equals(CV6.getCode()))
			return true;
		else if (value.equals(CV10.getCode()))
			return true;
		else
			return false;
	}
}
