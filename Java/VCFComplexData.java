package edu.sdsc.wellderly.rules;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "gene.illumina_vcf")
public class VCFComplexData {

	private String subjectID;
	private String ref;
	private String genotype;
	private int pos;
	private String chrom;
	private String alt;
	private String allele1;
	private String allele2;
	private String altList1;
	private String altList2;
	private String modAlt1;
	private String modAlt2;
	private String modRef1;
	private String modRef2;
	private int modStartPos1;
	private int modStartPos2;
	private String vartype1;
	private String vartype2;

	public VCFComplexData() {

	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public String getGenotype() {
		return genotype;
	}

	public void setGenotype(String genotype) {
		this.genotype = genotype;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public String getChrom() {
		return chrom;
	}

	public void setChrom(String chrom) {
		this.chrom = chrom;
	}

	public String getAlt() {
		return alt;
	}

	public void setAlt(String alt) {
		this.alt = alt;
	}

	public String getAllele1() {
		return allele1;
	}

	public void setAllele1(String allele1) {
		this.allele1 = allele1;
	}

	public String getAllele2() {
		return allele2;
	}

	public void setAllele2(String allele2) {
		this.allele2 = allele2;
	}

	public String getAltList1() {
		return altList1;
	}

	public void setAltList1(String altList1) {
		this.altList1 = altList1;
	}

	public String getSubjectID() {
		return subjectID;
	}

	public void setSubjectID(String subjectID) {
		this.subjectID = subjectID;
	}

	public String getAltList2() {
		return altList2;
	}

	public void setAltList2(String altList2) {
		this.altList2 = altList2;
	}

	public String getModAlt1() {
		return modAlt1;
	}

	public void setModAlt1(String modAlt1) {
		this.modAlt1 = modAlt1;
	}

	public String getModAlt2() {
		return modAlt2;
	}

	public void setModAlt2(String modAlt2) {
		this.modAlt2 = modAlt2;
	}

	public String getModRef1() {
		return modRef1;
	}

	public void setModRef1(String modRef1) {
		this.modRef1 = modRef1;
	}

	public String getModRef2() {
		return modRef2;
	}

	public void setModRef2(String modRef2) {
		this.modRef2 = modRef2;
	}

	public int getModStartPos1() {
		return modStartPos1;
	}

	public void setModStartPos1(int modStartPos1) {
		this.modStartPos1 = modStartPos1;
	}

	public int getModStartPos2() {
		return modStartPos2;
	}

	public void setModStartPos2(int modStartPos2) {
		this.modStartPos2 = modStartPos2;
	}

	public String getVartype1() {
		return vartype1;
	}

	public void setVartype1(String vartype1) {
		this.vartype1 = vartype1;
	}

	public String getVartype2() {
		return vartype2;
	}

	public void setVartype2(String vartype2) {
		this.vartype2 = vartype2;
	}

}
