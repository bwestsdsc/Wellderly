package edu.sdsc.wellderly.rules;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class VCFComplexGroup implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3406974240440761472L;
	private String chrom;
	private int pos;
	private String alt1;
	private String alt2;
	private String gt;
	private String altList1;
	private String altList2;
	private String vartype;
	private String ref;

	public VCFComplexGroup() {

	}

	public String getChrom() {
		return chrom;
	}

	public void setChrom(String chrom) {
		this.chrom = chrom;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public String getAltList1() {
		return altList1;
	}

	public void setAltList1(String altList1) {
		this.altList1 = altList1;
	}

	public String getAltList2() {
		return altList2;
	}

	public void setAltList2(String altList2) {
		this.altList2 = altList2;
	}

	public String getGt() {
		return gt;
	}

	public String getAlt1() {
		return alt1;
	}

	public void setAlt1(String alt1) {
		this.alt1 = alt1;
	}

	public String getAlt2() {
		return alt2;
	}

	public void setAlt2(String alt2) {
		this.alt2 = alt2;
	}

	public void setGt(String gt) {
		this.gt = gt;
	}

	public String getVartype() {
		return vartype;
	}

	public void setVartype(String vartype) {
		this.vartype = vartype;
	}


	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	@Override
	public int hashCode() {
		return pos;

	}

	@Override
	public boolean equals(Object ob) {
		return false;

	}

	
}
