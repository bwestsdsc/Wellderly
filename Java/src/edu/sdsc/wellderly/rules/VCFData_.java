package edu.sdsc.wellderly.rules;

import java.util.ArrayList;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2015-01-04T10:09:08.667-0800")
@StaticMetamodel(VCFData.class)
public class VCFData_ {
	public static volatile SingularAttribute<VCFData, Long> id;
	public static volatile SingularAttribute<VCFData, String> subjectID;
	public static volatile SingularAttribute<VCFData, String> ref;
	public static volatile SingularAttribute<VCFData, String> genotype;
	public static volatile SingularAttribute<VCFData, Integer> pos;
	public static volatile SingularAttribute<VCFData, Integer> endPos;
	public static volatile SingularAttribute<VCFData, String> chrom;
	public static volatile SingularAttribute<VCFData, String> alt;
	public static volatile SingularAttribute<VCFData, String> allele1;
	public static volatile SingularAttribute<VCFData, String> allele2;
	public static volatile SingularAttribute<VCFData, String> altList1;
	public static volatile SingularAttribute<VCFData, String> altList2;
	public static volatile SingularAttribute<VCFData, String> refList;
	public static volatile SingularAttribute<VCFData, String> modAlt1;
	public static volatile SingularAttribute<VCFData, String> modAlt2;
	public static volatile SingularAttribute<VCFData, String> modRef1;
	public static volatile SingularAttribute<VCFData, String> modRef2;
	public static volatile SingularAttribute<VCFData, Integer> modStartPos1;
	public static volatile SingularAttribute<VCFData, Integer> modStartPos2;
	public static volatile SingularAttribute<VCFData, String> vartype1;
	public static volatile SingularAttribute<VCFData, String> vartype2;
	public static volatile SingularAttribute<VCFData, String> modGT1;
	public static volatile SingularAttribute<VCFData, String> modGT2;
	public static volatile SingularAttribute<VCFData, String> type;
	public static volatile SingularAttribute<VCFData, Integer> orgPos;
	public static volatile SingularAttribute<VCFData, String> zygosity;
	public static volatile SingularAttribute<VCFData, ArrayList> orgAlts;
}
