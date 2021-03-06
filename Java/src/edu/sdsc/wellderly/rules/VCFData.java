package edu.sdsc.wellderly.rules;

import java.util.ArrayList;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "gene.illumina_vcf")
public class VCFData {
	@Basic(optional=true)
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String subjectID;
	private String ref;
	private String genotype;
	private int pos;
	private int endPos;
	private String chrom;
	private String alt;
	private String allele1;
	private String allele2;
	private String altList1;
	private String altList2;
	private String refList;
	private String modAlt1;
	private String modAlt2;
	private String modRef1;
	private String modRef2;
	private int modStartPos1;
	private int modStartPos2;
	private String vartype1;
	private String vartype2;
	private String modGT1;
	private String modGT2;
	private String type;
	private int orgPos;
	private String zygosity;
	private ArrayList<String> orgAlts;

	public VCFData() {
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

	public String getAltList2() {
		return altList2;
	}

	public void setAltList2(String altList2) {
		this.altList2 = altList2;
	}

	public String getSubjectID() {
		return subjectID;
	}

	public void setSubjectID(String subjectID) {
		this.subjectID = subjectID;
	}

	public String getRefList() {
		return refList;
	}

	public void setRefList(String refList) {
		this.refList = refList;
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

	public String getModGT1() {
		return modGT1;
	}

	public void setModGT1(String modGT1) {
		this.modGT1 = modGT1;
	}

	public String getModGT2() {
		return modGT2;
	}

	public void setModGT2(String modGT2) {
		this.modGT2 = modGT2;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getOrgPos() {
		return orgPos;
	}

	public void setOrgPos(int pos) {
		this.orgPos = pos;
	}

	public int getEndPos() {
		return endPos;
	}

	public void setEndPos(int endPos) {
		this.endPos = endPos;
	}

	public String getZygosity() {
		return zygosity;
	}

	public void setZygosity(String zygosity) {
		this.zygosity = zygosity;
	}

	public ArrayList<String> getOrgAlts() {
		return orgAlts;
	}

	public void setOrgAlts(ArrayList<String> orgAlts) {
		this.orgAlts = orgAlts;
	}

}
