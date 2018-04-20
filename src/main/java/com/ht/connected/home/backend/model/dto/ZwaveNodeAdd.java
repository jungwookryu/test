package com.ht.connected.home.backend.model.dto;

public class ZwaveNodeAdd {

	private String seqNo = "0";

	private String reserved = "0";

	private int mode = 1;

	public String getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}

	public String getReserved() {
		return reserved;
	}

	public void setReserved(String reserved) {
		this.reserved = reserved;
	}

	public String getMode() {
		return Integer.toString(mode);
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

}
