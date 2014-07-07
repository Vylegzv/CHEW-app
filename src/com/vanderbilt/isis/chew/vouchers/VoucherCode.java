package com.vanderbilt.isis.chew.vouchers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum VoucherCode {
	
	CV6("CVV6"), CV10("CVV10"), A("A"), A2("A2"), B("B"), B2("B2"),
	L("L"), L2("L2"), G("G"), G2("G2"), T2("T2"), T("T"), K("K"),
	K2("K2"), E("E"), E2("E2"), P("P"), P2("P2"), PPW("PPW"),
	PC1("PC1"), PC("PC");
	

	
	private static final Logger logger = LoggerFactory.getLogger(VoucherCode.class);
	//private static final Logger logger = LoggerFactory.getLogger(VoucherCode.class.getName());
	
	private String code;
	
	private VoucherCode(String code){	
		
		this.code = code;
	}
	
	public String getCode(){
		logger.trace("getCode()");
		return this.code;
	}
	
	public String toString(){
		logger.trace("toString()");
		return this.code;
	}
	
	public static VoucherCode getVoucherCodeFromValue(String value) {
		logger.trace("getVoucherCodeFromValue()");
		if(value.equals(A.getCode())){
			return VoucherCode.A;
		}else if(value.equals(A2.getCode())){
			return VoucherCode.A2;
		}else if(value.equals(B.getCode())){
			return VoucherCode.B;
		}else if(value.equals(B2.getCode())){
			return VoucherCode.B2;
		}else if(value.equals(L.getCode())){
			return VoucherCode.L;
		}else if(value.equals(L2.getCode())){
			return VoucherCode.L2;
		}else if(value.equals(G.getCode())){
			return VoucherCode.G;
		}else if(value.equals(G2.getCode())){
			return VoucherCode.G2;
		}else if(value.equals(T.getCode())){
			return VoucherCode.T;
		}else if(value.equals(T2.getCode())){
			return VoucherCode.T2;
		}else if(value.equals(K.getCode())){
			return VoucherCode.K;
		}else if(value.equals(K2.getCode())){
			return VoucherCode.K2;
		}else if(value.equals(E.getCode())){
			return VoucherCode.E;
		}else if(value.equals(E2.getCode())){
			return VoucherCode.E2;
		}else if(value.equals(P.getCode())){
			return VoucherCode.P;
		}else if(value.equals(P2.getCode())){
			return VoucherCode.P2;
		}else if(value.equals(PPW.getCode())){
			return VoucherCode.PPW;
		}else if(value.equals(PC1.getCode())){
			return VoucherCode.PC1;
		}else if(value.equals(PC.getCode())){
			return VoucherCode.PC;
		}else if(value.equals(CV6.getCode())){
			return VoucherCode.CV6;
		}else if (value.equals(CV10.getCode())){
			return VoucherCode.CV10;
		}else{
			return null;
		}
	}
	
	public static boolean isCashCode(String value){
		logger.trace("isCashCode()");
		if(value.equals(CV6.getCode()))
			return true;
		else if (value.equals(CV10.getCode()))
			return true;
		else
			return false;
	}
}
