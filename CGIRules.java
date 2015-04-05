package edu.sdsc.wellderly.rules;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections4.comparators.ComparatorChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import edu.sdsc.dao.WellConn;

public class CGIRules {

	static final Set<String> alts = new HashSet<String>();
	static final Set<String> orgAlts = new HashSet<String>();
	static final Set<String> refs = new HashSet<String>();
	static ArrayList<Object> recordList = new ArrayList<Object>();
	static ArrayList<Object> groupList = new ArrayList<Object>();
	static Logger logger = LoggerFactory.getLogger(AlGtSimpleRules.class);
	static String inChrom = null;
	static String offset = null;
	static String limit = null;
	static String file = null;

	public CGIRules() {
	}

	public static void main(String[] args) {

		file = args[0];
		inChrom = args[1];
		offset = args[2];
		limit = args[3];

		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		StatusPrinter.print(lc);

		System.out.println("Entering application." + file + " Chromosome "
				+ inChrom + " limit " + limit + " offset " + offset);
		logger.info("Entering application." + file + " Chromosome " + inChrom
				+ " limit "+ " offset " + offset);

		ResultSet rs = getCGIData();
		try {
			parseData(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		createDownload(recordList);
		computeGenotype();
		createFile();

		logger.info("Exiting application.");

	}

	public static ResultSet getCGIData() {

		Connection conn = null;
		ResultSet rs = null;

		try {
			conn = WellConn.getConn();
		} catch (Exception e) {
			System.out.println(e.toString());

		}
		// ignore offset and limit. For testing only. They will be removed when
		// in production.
		String query = "select chromosome, begin_pos, end_pos, zygosity, vartype,  patient_id,"
				+ "case when reference is null then '-' else reference end, "
				+ "case when allele1Seq like '%?%' then 'N' when allele1Seq is null then '-' else allele1Seq end, "
				+ "case when allele2Seq like '%?%' then 'N' when allele2Seq is null then '-' else allele2Seq end "
				+ "from gene.cgi_data where chromosome = ? and reference <> '=' and vartype not in ('ins', 'ref')"
				+ " and zygosity != 'no-call' "
				+ "Union "
				+ "select  chromosome, begin_pos, end_pos, zygosity, vartype, patient_id,"
				+ "case when reference is null then '-' else reference end, "
				+ "case when allele1Seq like '%?%' then 'N' when allele1Seq is null then '-' else allele1Seq end, "
				+ "case when allele2Seq like '%?%' then 'N' when allele2Seq is null then '-' else allele2Seq end "
				+ "from gene.cgi_data where chromosome = ? and vartype = 'ins'"
				+ " and zygosity != 'no-call' "
				+ "order by patient_id, chromosome, begin_pos, allele1Seq, vartype limit ? offset ?";

		try {
			//System.out.println(query);
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, inChrom);
		    ps.setString(2, inChrom);
			ps.setInt(4, Integer.parseInt(offset));
			ps.setInt(3, Integer.parseInt(limit));
			rs = ps.executeQuery();

		} catch (Exception e) {
			logger.debug(e.toString());
			System.out.println(e.toString());

		}
		return rs;

	}

	private static void parseData(ResultSet rs) throws SQLException {

		while (rs.next()) {
			VCFData vcf = new VCFData();
			String subjID = rs.getString(6);
			String chrom = rs.getString(1);
			int beginPos = rs.getInt(2);
			int endPos = rs.getInt(3);
			String zygosity = rs.getString(4);
			String vartype = rs.getString(5);
			String ref = rs.getString(7);
			String allele1 = rs.getString(8);
			String allele2 = rs.getString(9);
			if (vartype.equals("del")) {
				// alts.add(ref);
				orgAlts.add(ref);
			} else {
				if (!allele1.equals(ref)) {
					// alts.add(allele1);
					orgAlts.add(allele1);
				}
				if (!allele2.equals(ref)) {
					// alts.add(allele2);
					orgAlts.add(allele2);
				}
			}
			vcf.setSubjectID(subjID);
			vcf.setChrom(chrom);
			vcf.setPos(beginPos);
			vcf.setEndPos(endPos);
			vcf.setRef(ref);
			vcf.setZygosity(zygosity);
			vcf.setAlt(allele1);
			vcf.setAllele1(allele1);
			vcf.setAllele2(allele2);
			String genotype = setGT(zygosity, vartype, ref, ref, allele1,
					allele2, orgAlts, ref, allele1, allele2, false, false);
			vcf.setGenotype(genotype);
			vcf.setVartype1(vartype);
			vcf.setChrom(chrom);
			vcf.setPos(beginPos);
			recordList.add(vcf);
			orgAlts.clear(); // distinct allele list for each record
		}
		// last group record
		alts.clear();
		rs.close();

	}

	private static void createDownload(ArrayList<Object> vcfList) {

		ComparatorChain<Object> chain = new ComparatorChain<Object>();
		Comparator<Object> chromComp = new ChromComparator();
		Comparator<Object> posComp = new PosComparator();
		Comparator<Object> altComp = new AltComparator();
		chain.addComparator(chromComp);
		chain.addComparator(posComp);
		chain.addComparator(altComp);

		Collections.sort(vcfList, chain);

		for (Object vcf : vcfList) {
			vcfTrim(vcf);
		}

		try {
			vcfList = splitRecords(vcfList);

			ComparatorChain<Object> chain1 = new ComparatorChain<Object>();
			Comparator<Object> chromComp1 = new ChromComparator();
			Comparator<Object> posComp1 = new PosComparator();
			Comparator<Object> altComp1 = new AltComparator();
			Comparator<Object> refComp1 = new RefComparator();
			chain1.addComparator(chromComp1);
			chain1.addComparator(posComp1);
			chain1.addComparator(altComp1);
			chain1.addComparator(refComp1);

			Collections.sort(vcfList, chain1);
			createAlleleList(vcfList);
		} catch (IOException e) {
			logger.debug(e.toString());
			e.printStackTrace();
		}
	}

	private static String setGT(String zygosity, String vartype, String ref,
			String ref1, String allele1, String allele2, Set<String> altList,
			String orgRef, String alt1, String alt2, boolean pre1, boolean pre2) {
		String genotype;
		String gt1 = null;
		String gt2 = "/";
		String gt3 = null;

		ArrayList<String> sortedList = sortAlts(altList);

		if (!vartype.equals("del")) {
			if (!allele1.equals(null) || allele1 != "") {
				if (allele1.equals(ref)) {
					gt1 = "0";
				} else if (allele1.equals("-")) {
					gt1 = String.valueOf(sortedList.indexOf(alt1) + 1);
				} else if (allele1.equals("N")) {
					gt1 = "N";
				} else if (allele1.equals("X")) {
					gt1 = "X";
				} else if (!allele1.equals(ref)) {
					gt1 = String.valueOf(sortedList.indexOf(allele1) + 1);
				}
			}

			if (!allele2.equals(null)) {
				if (allele2.equals(ref)) {
					gt3 = "0";
				} else if (allele2.equals("-")) {
					gt3 = String.valueOf(sortedList.indexOf(alt2) + 1);
				} else if (allele2.equals("N")) {
					gt3 = "N";
				} else if (allele2.equals("X")) {
					gt3 = "X";
				} else if (!allele2.equals(ref)) {
					gt3 = String.valueOf(sortedList.indexOf(allele2) + 1);
				}
			}
		} else {
			if (!allele1.equals(null)) {
				if (pre1 && !allele1.equals("X")) {
					gt1 = String.valueOf(sortedList.indexOf(orgRef) + 1);
				} else if (allele1.equals("-") && !allele1.equals("X")) {
					gt1 = String.valueOf(sortedList.indexOf(ref1) + 1);
				} else if (allele1.equals("N")) {
					gt1 = "N";
				} else if (allele1.equals("X")) {
					gt1 = "X";
				} else if (allele1.equals(ref)) {
					gt1 = "0";
				} else {
					gt1 = "0";
				}
			}
			if (!allele2.equals(null)) {
				if (pre2 && !allele2.equals("X")) {
					gt3 = String.valueOf(sortedList.indexOf(orgRef) + 1);
				} else if (allele2.equals("-") && !allele2.equals("X") && !pre2) {
					gt3 = String.valueOf(sortedList.indexOf(alt2) + 1);
				} else if (allele2.equals("N")) {
					gt3 = "N";
				} else if (allele2.equals("X")) {
					gt3 = "X";
				} else if (allele2.equals(ref)) {
					gt3 = "0";
				} else {
					gt3 = "0";
				}
			}

		}

		genotype = gt1 + gt2 + gt3;

		return genotype;

	}

	// Method that trims the begin and ends of complex variations
	public static void vcfTrim(Object vcf) {

		try {
			int end1 = 0;
			int end2 = 0;
			int begin1 = ((VCFData) vcf).getPos(); // not required for CGI data
													// - 1;
			int begin2 = ((VCFData) vcf).getPos(); // not required for CGI data.
													// it is already 0 based -
													// 1;
			String var1 = ((VCFData) vcf).getAllele1();
			String var2 = ((VCFData) vcf).getAllele2();
			String ref = ((VCFData) vcf).getRef();
			String ref1 = ((VCFData) vcf).getRef();
			int start1 = 0;
			String[] gts = ((VCFData) vcf).getGenotype().split("");
			String vartype1 = ((VCFData) vcf).getVartype1();
			String vartype2 = ((VCFData) vcf).getVartype1();

			if ((vartype1.equals("del") || vartype1.equals("complex"))
					&& ((var1.equals("-") && var2.equals("-")))) {
				alts.add(ref);
			}

			// trimming logic is slightly different from other vartypes
			if (!vartype1.equals("sub") && !vartype2.equals("sub")) {
				// get the end index of the substring for allele1
				if (!ref.equals(var1) && !var1.equals("-") && !var1.equals("N")) {
					for (int i = 0; i < Math.min(var1.length(), ref.length()); i++) {
						if (var1.substring(var1.length() - i - 1,
								var1.length() - i).equals(
								ref.substring(ref.length() - i - 1,
										ref.length() - i))) {
							end1 = end1 + 1;
						} else {
							break;
						}
					}

					if (end1 != 0) {
						ref = ref.substring(0, ref.length() - end1);
						var1 = var1.substring(0, var1.length() - end1);
					}

					// get the end index of the substring for allele1 start

					for (int i = 0; i < Math.min(var1.length(), ref.length()); i++) {
						if (var1.charAt(i) == ref.charAt(i)) {
							start1++;
						} else {
							break;
						}
					}
					// trim the head

					ref = ref.substring(start1);
					var1 = var1.substring(start1);
					begin1 = begin1 + start1;
				}

				// get the end position index for allele2 substring
				if (!ref1.equals(var2) && !var2.equals("-")
						&& !var2.equals("N")) {
					for (int i = 0; i < Math.min(var2.length(), ref1.length()); i++) {
						if (var2.substring(var2.length() - i - 1,
								var2.length() - i).equals(
								ref1.substring(ref1.length() - i - 1,
										ref1.length() - i))) {
							end2 = end2 + 1;
						} else {
							break;
						}
					}

					if (vartype1.equals(vartype2) && vartype1.equals("sub")) {
						end2 = end1;

					}

					if (end2 != 0) {
						ref1 = ref1.substring(0, ref1.length() - end2);
						var2 = var2.substring(0, var2.length() - end2);
					}

					// start pos index for allele2
					int start2 = 0;
					for (int i = 0; i < Math.min(var2.length(), ref1.length()); i++) {
						if (var2.charAt(i) == ref1.charAt(i)) {
							start2++;
						} else {
							break;
						}
					}

					// trim the head

					ref1 = ref1.substring(start2);
					var2 = var2.substring(start2);
					begin2 = begin2 + start2;
				}
			} else {
				// get the end index of the substring for allele1
				if (!var1.equals("-") && !var1.equals("N")) {
					for (int i = 0; i < Math.min(var1.length(), ref.length()); i++) {
						if (var1.substring(var1.length() - i - 1,
								var1.length() - i).equals(
								ref.substring(ref.length() - i - 1,
										ref.length() - i))) {
							end1 = end1 + 1;
						} else {
							break;
						}
					}

					if (end1 != 0) {
						ref = ref.substring(0, ref.length() - end1);
						var1 = var1.substring(0, var1.length() - end1);
					}

					// get the end index of the substring for allele1 start

					for (int i = 0; i < Math.min(var1.length(), ref.length()); i++) {
						if (var1.charAt(i) == ref.charAt(i)) {
							start1++;
						} else {
							break;
						}
					}
					// trim the head

					ref = ref.substring(start1);
					var1 = var1.substring(start1);
					begin1 = begin1 + start1;
				}

				// get the end position index for allele2 substring
				if (!var2.equals("-") && !var2.equals("N")) {
					for (int i = 0; i < Math.min(var2.length(), ref1.length()); i++) {
						if (var2.substring(var2.length() - i - 1,
								var2.length() - i).equals(
								ref1.substring(ref1.length() - i - 1,
										ref1.length() - i))) {
							end2 = end2 + 1;
						} else {
							break;
						}
					}

					if (vartype1.equals(vartype2) && vartype1.equals("sub")) {
						end2 = 0;

					}

					if (end2 != 0) {
						ref1 = ref1.substring(0, ref1.length() - end2);
						var2 = var2.substring(0, var2.length() - end2);
					}

					// start pos index for allele2
					int start2 = 0;
					if (ref1.equals(var2)) {
						start2 = start1;
					} else {
						for (int i = 0; i < Math.min(var2.length(),
								ref1.length()); i++) {
							if (var2.charAt(i) == ref1.charAt(i)) {
								start2++;
							} else {
								break;
							}
						}
					}

					// trim the head

					ref1 = ref1.substring(start2);
					var2 = var2.substring(start2);
					begin2 = begin2 + start2;
				}
			}

			// is this a snp
			if (ref.length() == 1 && !ref1.equals("-") && var1.length() == 1
					&& !var1.equals("-") && !var1.equals(ref)) {
				vartype1 = "snp";
			}

			else if (ref.length() == 0 && var1.length() > 0
					&& !var1.equals('-') || ref.equals("-")) {
				// begin1 = begin1 + 1; not needed for CGI data
				vartype1 = "ins";
				ref = "-";
			}

			else if (ref.length() == 1
					&& (var1.length() == 0 || var1.equals("-"))) {
				vartype1 = "del";
				var1 = "-";
			}

			else if (ref.length() > 0
					&& (var1.length() == 0 || var1.equals("-"))) {
				vartype1 = "del";
				var1 = "-";

			}

			else if (ref.length() > 0 && var1.length() > 0 && !ref.equals(var1)) {
				vartype1 = "sub";

			}

			// is this a snp
			if (ref1.length() == 1 && !ref1.equals("-") && var2.length() == 1
					&& !var2.equals("-") && !var2.equals(ref1)) {
				vartype2 = "snp";

			}

			else if (ref1.length() == 0 && var2.length() > 0
					&& !var2.equals('-') || ref1.equals("-")) {
				// begin2 = begin2 + 1;
				vartype2 = "ins";
				ref1 = "-";
			}

			else if (ref1.length() == 1
					&& (var2.length() == 0 || var2.equals("-"))) {
				vartype2 = "del";
				var2 = "-";
			}

			else if (ref1.length() > 0
					&& (var2.length() == 0 || var2.equals("-"))) {
				vartype2 = "del";
				var2 = "-";

			}

			else if (ref1.length() > 0 && var2.length() > 0
					&& !ref1.equals(var2)) {
				vartype2 = "sub";
			}

			((VCFData) vcf).setModAlt1(var1);
			((VCFData) vcf).setModAlt2(var2);

			if (vartype1.equals("del")) {
				if (!ref.equals("-") && !ref.equals("") && !ref.equals("N")
						&& ref != null) {
					alts.add(ref);
				}
				if (!ref1.equals("-") && !ref1.equals("") && !ref1.equals("N")
						&& ref1 != null) {
					alts.add(ref1);
				}
			} else {
				if (!var1.equals("-") && !var1.equals("") && var1 != null
						&& !var1.equals("N")) {
					if (!var1.equals(ref)) {
						alts.add(var1);
					}
				}
				if (!var2.equals("-") && !var2.equals("") && var2 != null
						&& !var2.equals("N")) {
					if (!var2.equals(ref1)) {
						alts.add(var2);
					}
				}
			}
			if (!gts[0].equals("0")) {
				((VCFData) vcf).setModAlt1(var1);
			} else {
				((VCFData) vcf).setModAlt1(ref);

			}
			if (!gts[2].equals("0")) {
				((VCFData) vcf).setModAlt2(var2);
			} else {
				((VCFData) vcf).setModAlt2(ref1);

			}

			((VCFData) vcf).setModStartPos1(begin1);
			((VCFData) vcf).setModStartPos2(begin2);
			((VCFData) vcf).setModAlt1(var1);
			((VCFData) vcf).setModAlt2(var2);
			((VCFData) vcf).setVartype1(vartype1);
			((VCFData) vcf).setVartype2(vartype2);
			((VCFData) vcf).setModRef1(ref);
			((VCFData) vcf).setModRef2(ref1);

		} catch (Exception e) {
			System.out.println("Trimmming ");
			logger.warn(e.toString());
			e.printStackTrace();
		}
	}

	private static void computeGenotype() {

		ComparatorChain<Object> chain = new ComparatorChain<Object>();
		Comparator<Object> chromComp = new ChromComparator();
		Comparator<Object> posComp = new PosComparator();
		chain.addComparator(chromComp);
		chain.addComparator(posComp);

		Collections.sort(recordList, chain);

		try {
			for (Object vcf : recordList) {

				String ref = ((VCFData) vcf).getRef();
				String allele1 = ((VCFData) vcf).getAllele1();
				String allele2 = ((VCFData) vcf).getAllele2();
				String modRef1 = ((VCFData) vcf).getModRef1();
				String modAlt1 = ((VCFData) vcf).getModAlt1();
				String modAlt2 = ((VCFData) vcf).getModAlt2();
				String modAltList = ((VCFData) vcf).getAltList1();
				String vartype1 = ((VCFData) vcf).getVartype1();
				Boolean pre1 = false;
				if (allele1.equals("-")) {
					pre1 = true;
				}
				Boolean pre2 = false;
				if (allele2.equals("-")) {
					pre2 = true;
				}

				Set<String> altList = new HashSet<String>();
				String genotype = null;
				ArrayList<String> altList1 = new ArrayList<String>();
				if (modAltList != null && !modAltList.equals("")) {
					for (String alt : modAltList.split(",")) {
						alt = alt.replace("[", "");
						alt = alt.replace("]", "");
						altList.add(alt);
					}

					altList1 = sortAlts(altList);
					((VCFData) vcf).setSortedList(altList1.toString());

					Set<String> altList2 = new HashSet<String>();
					for (String alts : altList1) {
						altList2.add(alts);
					}

					genotype = setGT("null", vartype1, modRef1, modRef1,
							modAlt1, modAlt2, altList2, ref, allele1, allele2,
							pre1, pre2);

					((VCFData) vcf).setModGT1(genotype);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn(e.toString());
		}
	}

	public static void createAlleleList(ArrayList<Object> mergedList)
			throws IOException {

		int lastPos = 0;
		String lastVartype = "";
		String lastRef = "";
		ArrayList<Object> groupList1 = new ArrayList<Object>();
		VCFComplexGroup vcfGrp = new VCFComplexGroup();
		alts.clear();
		for (Object mrgRecord : mergedList) {
			String chrom = ((VCFData) mrgRecord).getChrom();
			int pos = ((VCFData) mrgRecord).getModStartPos1();
			String vartype = ((VCFData) mrgRecord).getVartype1();
			String ref = ((VCFData) mrgRecord).getRef();
			String ref1 = ((VCFData) mrgRecord).getModRef1();
			if ((lastPos != 0 && lastPos != pos)
					|| (!lastVartype.equals("") && !vartype.equals(lastVartype))
					|| (!lastRef.equals("") && vartype.equals("sub") && ref1
							.length() != lastRef.length())) {
				vcfGrp.setAltList1(alts.toString());
				groupList1.add(vcfGrp);
				alts.clear();
				vcfGrp = new VCFComplexGroup();
			}
			vcfGrp.setChrom(chrom);
			vcfGrp.setPos(pos);
			vcfGrp.setVartype(vartype);
			vcfGrp.setRef(ref1);

			String ref2 = ((VCFData) mrgRecord).getModRef2();
			String var1 = ((VCFData) mrgRecord).getModAlt1();
			String var2 = ((VCFData) mrgRecord).getModAlt2();
			String orgVar1 = ((VCFData) mrgRecord).getAllele1();
			String orgVar2 = ((VCFData) mrgRecord).getAllele2();

			if (vartype.equals("del")) {
				if (ref1 != null && !ref1.equals("-") && !ref1.equals("")
						&& !ref1.equals("X") && !ref1.equals("N")
						&& !var1.equals("N")) {
					alts.add(ref1);
					if (var1.equals("-")) {
						alts.add(ref1);
						/*
						 * if (!orgVar1.equals("-")) alts.add(orgVar1);
						 */
						if (orgVar1.equals("-")) {
							alts.add(ref);
						}
					}
				}
				if (ref2 != null && !ref2.equals("-") && !ref2.equals("")
						&& !ref2.equals("X") && !ref1.equals("N")
						&& !var2.equals("N")) {
					if (var2.equals("-")) {
						/*
						 * if (!orgVar2.equals("-")) alts.add(orgVar2);
						 */
						if (orgVar2.equals("-")) {
							alts.add(ref);
						}
					}

				}
			} else {
				if (var1 != null && !var1.equals("-") && !var1.equals("")
						&& !var1.equals("X") && !var1.equals("N")) {
					if (!var1.equals(ref1)) {
						alts.add(var1);
					}
				}
				if (var2 != null && !var2.equals("-") && !var2.equals("")
						&& !var2.equals("X") && !var2.equals("N")) {
					if (!var2.equals(ref1)) {
						alts.add(var2);
					}
				}
			}
			lastPos = pos;
			lastVartype = vartype;
			lastRef = ref1;
		}
		vcfGrp.setAltList1(alts.toString());
		groupList1.add(vcfGrp);
		alts.clear();

		try {
			for (Object group : groupList1) {

				String chrom1 = ((VCFComplexGroup) group).getChrom();
				String chrom2 = "";
				int pos1 = ((VCFComplexGroup) group).getPos();
				int pos2 = 0;
				String vartype1 = ((VCFComplexGroup) group).getVartype();
				String vartype2 = "";
				String ref1 = ((VCFComplexGroup) group).getRef();
				String ref2 = "";

				for (Object record : mergedList) {

					chrom2 = ((VCFData) record).getChrom();
					pos2 = ((VCFData) record).getModStartPos1();
					vartype2 = ((VCFData) record).getVartype1();
					ref2 = ((VCFData) record).getModRef1();
					String altList = ((VCFComplexGroup) group).getAltList1();
					String refList = ((VCFComplexGroup) group).getAltList2();

					if (!vartype1.equals("sub")) {
						if (chrom1.equals(chrom2) && pos1 == pos2
								&& vartype1.equals(vartype2)) {
							((VCFData) record).setAltList1(altList);
							((VCFData) record).setAltList2(refList);
						}
					} else {
						if (chrom1.equals(chrom2) && pos1 == pos2
								&& vartype1.equals(vartype2)
								&& ref1.length() == ref2.length()) {
							((VCFData) record).setAltList1(altList);
							((VCFData) record).setAltList2(refList);
						}
					}
				}
			}

		} catch (Exception e) {
			System.out.println("Grouping " + e.toString());
			logger.warn(e.toString());
		}
	}

	private static ArrayList<String> sortAlts(Set<String> altList) {

		// sort the arrayList by length (shortest to longest) then
		// alphabetically
		ArrayList<String> altList1 = new ArrayList<String>();

		for (String alt : altList) {
			if (alt != null) {
				altList1.add(alt.trim());
			}
		}
		Collections.sort(altList1, new LengthFirstComparator());

		return altList1;
	}

	private static ArrayList<Object> splitRecords(ArrayList<Object> vcfList) {

		ArrayList<Object> mList = new ArrayList<Object>(vcfList);

		for (Object record : mList) {
			int pos = ((VCFData) record).getPos();
			String subjID = ((VCFData) record).getSubjectID();
			String chrom = ((VCFData) record).getChrom();
			String ref = ((VCFData) record).getRef();
			int modPos1 = ((VCFData) record).getModStartPos1();
			int modPos2 = ((VCFData) record).getModStartPos2();
			String modRef1 = ((VCFData) record).getModRef1();
			String modRef2 = ((VCFData) record).getModRef2();
			String allele1 = ((VCFData) record).getAllele1();
			String allele2 = ((VCFData) record).getAllele2();
			String modAlt1 = ((VCFData) record).getModAlt1();
			String modAlt2 = ((VCFData) record).getModAlt2();
			String vartype1 = ((VCFData) record).getVartype1();
			String vartype2 = ((VCFData) record).getVartype2();
			// if the position changed during the trimming process split this
			// record into two and
			// remove the original record
			if (pos != modPos1 || pos != modPos2
					&& (!modRef1.equals(modAlt1) && !modRef2.equals(modAlt2))
					|| !vartype1.equals(vartype2)) {
				if ((!modAlt1.equals("N") && !modAlt2.equals("N"))) {
					if (!vartype1.equals("sub") || !vartype2.equals("sub")) {

						VCFData vcf1 = new VCFData();
						VCFData vcf2 = new VCFData();

						// populate the first object
						vcf1.setChrom(chrom);
						vcf1.setPos(modPos1);
						vcf1.setRef(ref);
						vcf1.setAllele1(allele1);
						vcf1.setAllele2(allele2);
						vcf1.setModStartPos1(modPos1);
						vcf1.setModStartPos2(pos);
						vcf1.setModRef1(modRef1);
						vcf1.setModRef2(modRef1);
						vcf1.setModAlt1(modAlt1);
						vcf1.setModAlt2("X");
						vcf1.setVartype1(vartype1);
						vcf1.setVartype2(vartype1);
						vcf1.setSubjectID(subjID);

						vcfList.add(vcf1);

						// populate the second object
						vcf2.setChrom(chrom);
						vcf2.setPos(modPos2);
						vcf2.setRef(ref);
						vcf2.setAllele1(allele1);
						vcf2.setAllele2(allele2);
						vcf2.setModStartPos1(modPos2);
						vcf2.setModStartPos2(pos);
						vcf2.setModRef1(modRef2);
						vcf2.setModRef2(modRef2);
						vcf2.setModAlt1("X");
						vcf2.setModAlt2(modAlt2);
						vcf2.setVartype1(vartype2);
						vcf2.setVartype2(vartype2);
						vcf2.setSubjectID(subjID);

						vcfList.add(vcf2);
						vcfList.remove(record);
					}
				}
			}
		}
		return vcfList;
	}

	public static void createFile() {

		int size = 160000000;

		try {
			PrintWriter fw = new PrintWriter(new BufferedWriter(new FileWriter(
					file, true), size));
			
			for(Object record : recordList){
				String chrom = ((VCFData) record).getChrom();
				int pos = ((VCFData) record).getPos();
				String subjID = ((VCFData) record).getSubjectID();
				String ref = ((VCFData) record).getRef();
				int modPos1 = ((VCFData) record).getModStartPos1();
				String modRef1 = ((VCFData) record).getModRef1();
				String allele1 = ((VCFData) record).getAllele1();
				String allele2 = ((VCFData) record).getAllele2();
				String modAlt1 = ((VCFData) record).getModAlt1();
				String modAlt2 = ((VCFData) record).getModAlt2();
				String vartype1 = ((VCFData) record).getVartype1();
				String gt = ((VCFData) record).getGenotype();
				String alleleList = ((VCFData) record).getSortedList();
				String modGT = ((VCFData) record).getModGT1();
				
				fw.write(subjID + "\t" +chrom + "\t" + pos + "\t" + ref + "\t" + allele1 + "\t" +
						allele2 + "\t" + gt + "\t" + vartype1 + "\t" + modPos1 + "\t" +
						modRef1 + "\t" + modAlt1 + "\t" + modAlt2 + "\t" + alleleList + "\t" +
						modGT + "\n");
			}

			/*recordList.parallelStream().forEachOrdered(
							e -> fw.write(((VCFData) e).getSubjectID() + "\t"
							+ ((VCFData) e).getChrom() + "\t"
							+ ((VCFData) e).getPos() + "\t"
							+ ((VCFData) e).getRef() + "\t"
							+ ((VCFData) e).getAllele1() + "\t"
							+ ((VCFData) e).getAllele2() + "\t"
							+ ((VCFData) e).getGenotype() + "\t"
							+ ((VCFData) e).getVartype1() + "\t"
							+ ((VCFData) e).getModStartPos1() + "\t"
							+ ((VCFData) e).getModRef1() + "\t"
							+ ((VCFData) e).getModAlt1() + "\t"
							+ ((VCFData) e).getModAlt2() + "\t"
							+ ((VCFData) e).getSortedList() + "\t"
							+ ((VCFData) e).getModGT1() + "\n"));
*/
			fw.close();

		} catch (IOException e) {

			e.printStackTrace();
			logger.warn(e.toString());
		}
	}
}