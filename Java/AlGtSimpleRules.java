package edu.sdsc.wellderly.rules;

import java.sql.Connection;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import org.apache.commons.collections4.comparators.ComparatorChain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.sdsc.dao.WellConn;
import edu.sdsc.wellderly.rules.ChromComparator;
import edu.sdsc.wellderly.rules.PosComparator;
import edu.sdsc.wellderly.rules.AltComparator;

public class AlGtSimpleRules {

	static final Set<String> alts1 = new HashSet<String>();
	static Collection<Object> mergedList = new ArrayList<Object>();
	static List<Object> recordList = new ArrayList<Object>();
	static List<Object> groupList = new ArrayList<Object>();
	static final Set<String> alts = new HashSet<String>();
	static final Set<String> refs = new HashSet<String>();

	public AlGtSimpleRules() {
	}

	static AlGtComplexRules vcfComp1 = null;

	public static void main(String[] args) {
		try {

			getData();
			AlGtComplexRules vcfComp = new AlGtComplexRules();
			mergedList = vcfComp.getComplexData();
			mergedList.addAll(recordList);
			sortRecords((ArrayList<Object>) mergedList);
			createAlleleList((ArrayList<Object>) mergedList);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void getData() throws Exception {

		Connection conn = null;
		ResultSet rs = null;

		try {
			conn = WellConn.getConn();
		} catch (Exception e) {
			System.out.println(e.toString());

		}
		// ignore offset and limit. For testing only. They will be removed when
		// in production.
		String query = "select chrom, pos, ref, alt, split_part(file, ':', 1) as GT, subject_id, vartype "
				+ "from gene.illumina_vcf where chrom = 'chr22' and "
				+ "alt not like '%,%' or (alt like '%,%' and length(split_part(alt,',', 1)) = 1 "
				+ "and length(split_part(alt,',', 2)) = 1) "
				+ "order by 1, 2, 4, 7";

		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setFetchSize(60000000);
			rs = ps.executeQuery();

		} catch (Exception e) {
			System.out.println(e.toString());

		}

		try {

			int lastPos = 0;

			VCFGroup vcfGrp = new VCFGroup();
			while (rs.next()) {
				VCFData vcf = new VCFData();
				String chrom = rs.getString(1);
				int pos = rs.getInt(2);
				String genotype = rs.getString(5);
				// Create a new index when chrom/pos change
				if (lastPos != 0 && lastPos != pos) {
					vcfGrp.setAltList1(alts1.toString());
					groupList.add(vcfGrp);
					alts1.clear();
					vcfGrp = new VCFGroup();
				}
				String ref = rs.getString(3);
				String alt = rs.getString(4);
				String subjID = rs.getString(6);
				String varType = rs.getString(7);
				vcf.setChrom(chrom);
				vcfGrp.setChrom(chrom);
				vcf.setPos(pos);
				vcf.setOrgPos(pos);
				vcfGrp.setPos(pos);
				vcf.setRef(ref);
				vcf.setAlt(alt);
				vcfGrp.setAlt(alt);
				vcf.setGenotype(genotype);
				vcfGrp.setGt(genotype);
				vcf.setSubjectID(subjID);
				vcf.setVartype1(varType);
				vcf.setType("s");
				createAlleles(vcf);
				vcfTrim(vcf);
				recordList.add(vcf);
				lastPos = pos;
			}
			// last group record
			vcfGrp.setAltList1(alts1.toString());
			groupList.add(vcfGrp);

			// cycle through the object to assign the distinct alts set to each
			// record

			for (Object group : groupList) {

				String chrom1 = ((VCFGroup) group).getChrom();
				String chrom2 = "";
				int pos1 = ((VCFGroup) group).getPos();
				int pos2 = 0;

				for (Object record : recordList) {

					chrom2 = ((VCFData) record).getChrom();
					pos2 = ((VCFData) record).getPos();
					String altList1 = ((VCFGroup) group).getAltList1(); // assign
																		// discrete
																		// list
																		// of
																		// alts
																		// or
																		// refs
					if (chrom1.equals(chrom2) && pos1 == pos2) {
						((VCFData) record).setAltList1(altList1);
					}

				}
			}

			// Now that you have the distinct list recompute the genotype for
			// each variant
			for (Object record : recordList) {

				String modAlt1 = ((VCFData) record).getModAlt1();
				String modAlt2 = ((VCFData) record).getModAlt2();
				String modRef = ((VCFData) record).getModRef1();
				String gt1 = ((VCFData) record).getGenotype();
				String vartype = ((VCFData) record).getVartype1();
				sortAlleleList(record);
				String modAltList = ((VCFData) record).getAltList1();
				String modGT = createGenotype(gt1, modRef, modAlt1, modAlt2,
						modAltList, vartype);
				((VCFData) record).setModGT1(modGT);
			}

		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.close();
		}

	}

	// This takes the alts and the original genotype and creates the fields
	// allele1 and allele2 (like the CGI data)
	public static void createAlleles(Object vcf) {

		String[] alts = null;
		String gt = ((VCFData) vcf).getGenotype();
		String ref = ((VCFData) vcf).getRef();
		String alt = ((VCFData) vcf).getAlt();
		String[] gts = null;

		gts = gt.split("");
		alts = alt.split(",");

		if (gts[0].equals("0")) {
			((VCFData) vcf).setAllele1(ref);
		} else if (gts[0].equals("1")) {
			((VCFData) vcf).setAllele1(alts[0]);
		} else {
			((VCFData) vcf).setAllele1(alts[1]);
		}
		int arraySize = alts.length;

		if (arraySize > 1) {
			if (gts[1].equals("0")) {
				((VCFData) vcf).setAllele2(ref);
			} else if (gts[1].equals("1")) {
				((VCFData) vcf).setAllele2(alts[0]);
			} else {
				((VCFData) vcf).setAllele2(alts[1]);
			}
		} else {
			if (gts[2].equals("0")) {
				((VCFData) vcf).setAllele2(ref);
			} else {
				((VCFData) vcf).setAllele2(alt);
			}
		}
	}

	// Recompute the genotype using the distinct bases list and each allele
	protected static String createGenotype(String gt, String ref,
			String allele1, String allele2, String altList, String vartype) {

		int a1 = 0;
		String a2 = "";
		int a3 = 0;

		String altList1 = altList.replace("[", "");
		String altList2 = altList1.replace("]", "");
		altList2 = altList2.replace(" ", "");
		String genoType1 = "";
		String[] genoType = gt.split("");

		if (!vartype.equals("del")) {
			a1 = Arrays.asList(altList2.split(",")).indexOf(allele1) + 1;
			a3 = Arrays.asList(altList2.split(",")).indexOf(allele2) + 1;
		} else {
			a1 = Arrays.asList(altList2.split(",")).indexOf(allele2) + 1;
			a3 = Arrays.asList(altList2.split(",")).indexOf(allele1) + 1;
		}
		a2 = genoType[1];
		genoType1 = Integer.toString(a1) + a2 + Integer.toString(a3);

		return genoType1;
	}

	// Method that trims the begin and ends of complex variations
	public static void vcfTrim(Object vcf) {

		try {
			int end = 0;
			int begin1 = ((VCFData) vcf).getPos() - 1;
			int begin2 = begin1;
			String var = ((VCFData) vcf).getAlt();
			String ref = ((VCFData) vcf).getRef();
			String gt = ((VCFData) vcf).getGenotype();
			String[] gts = gt.split("");
			String[] vars = null;
			String varType = null;
			int start1 = 0;
			int start2 = 0;

			if (var.contains(",")) {
				vars = var.split(",");

				for (int i = 0; i < Math.min(vars[0].length(), ref.length()); i++) {
					if (vars[0].substring(vars[0].length() - i - 1,
							vars[0].length() - i).equals(
							ref.substring(ref.length() - i - 1, ref.length()
									- i))) {
						end = end + 1;
					} else {
						break;
					}
				}

				if (!vars[1].isEmpty()) {
					for (int i = 0; i < Math
							.min(vars[1].length(), ref.length()); i++) {
						if (vars[1].substring(vars[1].length() - i - 1,
								vars[1].length() - i).equals(
								ref.substring(ref.length() - i - 1,
										ref.length() - i))) {
							end = end + 1;
						} else {
							break;
						}
					}
				}

				if (end != 0) {
					ref = ref.substring(0, ref.length() - end);
					vars[0] = vars[0].substring(0, vars[0].length() - end);
					if (!vars[1].isEmpty()) {
						vars[0] = vars[0].substring(0, vars[0].length() - end);
					}
				}

				for (int i = 0; i < Math.min(vars[0].length(), ref.length()); i++) {
					if (var.charAt(i) == ref.charAt(i)) {
						start1 = start1 + 1;
					} else {
						break;
					}
				}
				if (!vars[1].isEmpty()) {
					for (int i = 0; i < Math
							.min(vars[1].length(), ref.length()); i++) {
						if (vars[1].charAt(i) == ref.charAt(i)) {
							start2 = start2 + 1;
						} else {
							break;
						}
					}

					// trim the head
					ref = ref.substring(start1);
					vars[0] = vars[0].substring(start1);

					if (!vars[1].isEmpty()) {
						vars[1] = vars[1].substring(start2);
					}
					begin1 = begin1 + start1;
					begin2 = begin2 + start2;

					// is this a snp
					if (ref.length() == 1 && vars[0].length() == 1) {
						varType = "snp";

					} else if (ref.length() == 0 && vars[0].length() > 0) {
						begin1 = begin1 + 1;
						varType = "ins";
						ref = "-";
					}

					else if (ref.length() > 0 && vars[0].length() == 0) {
						varType = "del";
						var = "-";

					}

					else if (ref.length() > 0 && vars[0].length() > 0
							&& !ref.equals(vars[0])) {
						varType = "sub";

					}
				}
				if (!gts[0].equals("0")) {
					((VCFData) vcf).setModAlt1(vars[0]);
					((VCFData) vcf).setModAlt2(vars[1]);
				} else {
					((VCFData) vcf).setModAlt1(ref);
				}
				if (!gts[2].equals("0")) {
					((VCFData) vcf).setModAlt2(vars[1]);
				} else {
					((VCFData) vcf).setModAlt2(ref);
				}

				if (varType.equals("del")) {
					alts1.add(ref);
				} else {
					if (!vars[0].equals(ref)) {
						alts1.add(vars[0]);
					}
					if (!vars[1].equals(ref)) {
						alts1.add(vars[1]);
					}
				}
			} else {
				for (int i = 0; i < Math.min(var.length(), ref.length()); i++) {
					if (var.substring(var.length() - i - 1, var.length() - i)
							.equals(ref.substring(ref.length() - i - 1,
									ref.length() - i))) {
						end = end + 1;
					} else {
						break;
					}
				}

				if (end != 0) {
					ref = ref.substring(0, ref.length() - end);
					var = var.substring(0, var.length() - end);
				}

				start1 = 0;
				for (int i = 0; i < Math.min(var.length(), ref.length()); i++) {
					if (var.charAt(i) == ref.charAt(i)) {
						start1 = start1 + 1;
					} else {
						break;
					}
				}

				// trim the head
				ref = ref.substring(start1);
				var = var.substring(start1);
				begin1 = begin1 + start1;

				// is this a snp
				if (ref.length() == 1 && var.length() == 1) {
					varType = "snp";
				}

				else if (ref.length() == 0 && var.length() > 0) {
					begin1 = begin1 + 1;
					varType = "ins";
					ref = "-";
				}

				else if (ref.length() > 0 && var.length() == 0) {
					varType = "del";
					var = "-";

				}

				else if (ref.length() > 0 && var.length() > 0
						&& !ref.equals(var)) {
					varType = "sub";

				}

				if (varType.equals("del")) {
					alts1.add(ref);
				} else {
					if (!var.equals(ref)) {
						alts1.add(var);
					}
				}
				if (!gts[0].equals("0")) {
					((VCFData) vcf).setModAlt1(var);
				} else {
					((VCFData) vcf).setModAlt1(ref);

				}
				if (!gts[2].equals("0")) {
					((VCFData) vcf).setModAlt2(var);
				} else {
					((VCFData) vcf).setModAlt2(ref);

				}
			}

			((VCFData) vcf).setModStartPos1(begin1);
			((VCFData) vcf).setModStartPos2(begin2);
			((VCFData) vcf).setVartype1(varType);
			((VCFData) vcf).setModRef1(ref);

		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}

	public static void sortAlleleList(Object vcf) {

		String modAltList1 = ((VCFData) vcf).getAltList1();
		List<String> altList4 = new ArrayList<String>();
		String altListc = modAltList1.replace("[", "");
		String altListd = altListc.replace("]", "");

		for (String altElement : altListd.split(", ")) {
			altList4.add(altElement);
		}

		Collections.sort(altList4,
				new edu.sdsc.wellderly.rules.LengthFirstComparator());

		((VCFData) vcf).setAltList1(altList4.toString());
	}

	public static ArrayList<Object> sortRecords(ArrayList<Object> mergedList2) {

		ArrayList<Object> mList = new ArrayList<Object>(mergedList2);

		for (Object mrgRecord : mList) {
			// Only modify records that need to be split into two records
			String modGT = ((VCFData) mrgRecord).getModGT2();
			String type = ((VCFData) mrgRecord).getType();
			// Only modify records that need to be split into two records

			if (type != null && modGT != null) {
				if (type == "c" && modGT.contains("X")) {

					String subjID = ((VCFData) mrgRecord).getSubjectID();
					String chrom = ((VCFData) mrgRecord).getChrom();
					int pos = ((VCFData) mrgRecord).getPos();
					int orgPos = ((VCFData) mrgRecord).getOrgPos();
					String ref = ((VCFData) mrgRecord).getRef();
					String alt = ((VCFData) mrgRecord).getAlt();
					String gt = ((VCFData) mrgRecord).getGenotype();
					int modPos1 = ((VCFData) mrgRecord).getModStartPos1();
					int modPos2 = ((VCFData) mrgRecord).getModStartPos2();
					String modRef1 = ((VCFData) mrgRecord).getModRef1();
					String modRef2 = ((VCFData) mrgRecord).getModRef2();
					String allele1 = ((VCFData) mrgRecord).getAllele1();
					String allele2 = ((VCFData) mrgRecord).getAllele2();
					String modAlt1 = ((VCFData) mrgRecord).getModAlt1();
					String modAlt2 = ((VCFData) mrgRecord).getModAlt2();
					String vartype1 = ((VCFData) mrgRecord).getVartype1();
					String vartype2 = ((VCFData) mrgRecord).getVartype2();

					VCFComplexGroup vcfGrp1 = new VCFComplexGroup();
					VCFComplexGroup vcfGrp2 = new VCFComplexGroup();
					VCFData vcf1 = new VCFData();
					VCFData vcf2 = new VCFData();
					if (pos != modPos1) {
						vcfGrp1.setChrom(chrom);
						vcfGrp1.setPos(modPos1);
						vcf1.setPos(modPos1);
						vcf1.setModStartPos1(modPos1);
						vcf1.setVartype1(vartype1);
						vcf1.setModRef1(modRef1);
						vcf2.setPos(modPos2);
						vcf2.setModStartPos1(modPos2);
						vcf2.setVartype1(vartype2);
						vcf2.setModRef1(modRef2);
					} else {
						vcfGrp2.setChrom(chrom);
						vcfGrp2.setPos(modPos2);
						vcf1.setPos(modPos2);
						vcf1.setModStartPos1(modPos2);
						vcf1.setVartype1(vartype2);
						vcf1.setModRef1(modRef2);
						vcf2.setPos(modPos1);
						vcf2.setModStartPos1(modPos1);
						vcf2.setVartype1(vartype1);
						vcf2.setModRef1(modRef1);
					}

					if (!vartype1.equals(vartype2)) {
						if (!vartype1.equalsIgnoreCase("del")) {
							vcf1.setModAlt1(modAlt1);
							vcf1.setModAlt2("X");
							vcf2.setModAlt1("X");
							vcf2.setModAlt2(modAlt2);
						} else {
							vcf2.setModAlt1(modAlt1);
							vcf2.setModAlt2("X");
							vcf1.setModAlt1("X");
							vcf1.setModAlt2(modAlt2);
						}
					} else {
						vcf1.setModAlt1(modAlt1);
						vcf1.setModAlt2("X");
						vcf2.setModAlt1("X");
						vcf2.setModAlt2(modAlt2);
					}

					// Create a separate object records for this type

					vcf1.setSubjectID(subjID);
					vcf1.setType(type);
					vcf1.setChrom(chrom);
					vcf1.setRef(ref);
					vcf1.setAlt(alt);
					vcf1.setOrgPos(orgPos);
					vcf1.setAllele1(allele1);
					vcf1.setAllele2(allele2);
					vcf1.setGenotype(gt);

					groupList.add(vcfGrp1);
					mergedList2.add(vcf1);

					// Create a separate object records for this type

					vcf2.setSubjectID(subjID);
					vcf2.setType(type);
					vcf2.setChrom(chrom);
					vcf2.setOrgPos(orgPos);
					vcf2.setRef(ref);
					vcf2.setAlt(alt);
					vcf2.setAllele1(allele1);
					vcf2.setAllele2(allele2);
					vcf2.setGenotype(gt);

					groupList.add(vcfGrp2);
					mergedList2.add(vcf2);
					// remove the pre-slpit object from the list
					mergedList2.remove(mrgRecord);
				}
			}
		}

		// Chain comparator takes the merged list with new objects and sorts
		// them by chromosome/pos/alt
		ComparatorChain<Object> chain = new ComparatorChain<Object>();
		Comparator<Object> chromComp = new ChromComparator();
		Comparator<Object> posComp = new PosComparator();
		Comparator<Object> altComp = new AltComparator();
		chain.addComparator(chromComp);
		chain.addComparator(posComp);
		chain.addComparator(altComp);

		Collections.sort(mList, chain);
		return mList;
	}

	public static void createAlleleList(ArrayList<Object> mergedList)
			throws IOException {

		int lastPos = 0;
		ArrayList<Object> groupList1 = new ArrayList<Object>();
		VCFComplexGroup vcfGrp = new VCFComplexGroup();

		for (Object mrgRecord : mergedList) {
			String chrom = ((VCFData) mrgRecord).getChrom();
			int pos = ((VCFData) mrgRecord).getPos();
			if (lastPos != 0 && lastPos != pos) {
				vcfGrp.setAltList1(alts.toString());
				vcfGrp.setAltList2(refs.toString());
				groupList1.add(vcfGrp);
				alts.clear();
				refs.clear();
				vcfGrp = new VCFComplexGroup();
			}
			vcfGrp.setChrom(chrom);
			vcfGrp.setPos(pos);

			String ref1 = ((VCFData) mrgRecord).getModRef1();
			String ref2 = ((VCFData) mrgRecord).getModRef2();
			String var1 = ((VCFData) mrgRecord).getModAlt1();
			String var2 = ((VCFData) mrgRecord).getModAlt2();

			if (ref1 != null && !ref1.equals("-") && !ref1.equals("")
					&& !ref1.equalsIgnoreCase("X")) {
				refs.add(ref1);
			}
			if (ref2 != null && !ref2.equals("-") && !ref2.equals("")
					&& !ref2.equalsIgnoreCase("X")) {
				refs.add(ref2);
			}
			if (var1 != null && !var1.equals("-") && !var1.equals("")
					&& !var1.equalsIgnoreCase("X")) {
				if (!var1.equals(ref1)) {
					alts.add(var1);
				}
			}
			if (var2 != null && !var2.equals("-") && !var2.equals("")
					&& !var2.equalsIgnoreCase("X")) {
				if (!var2.equals(ref2) && !var2.equals(ref1)) {
					alts.add(var2);
				}
			}

			lastPos = pos;

		}
		vcfGrp.setAltList1(alts.toString());
		vcfGrp.setAltList2(refs.toString());
		groupList1.add(vcfGrp);
		alts.clear();
		refs.clear();

		try {
			for (Object group : groupList1) {

				String chrom1 = ((VCFComplexGroup) group).getChrom();
				String chrom2 = "";
				int pos1 = ((VCFComplexGroup) group).getPos();
				int pos2 = 0;

				for (Object record : mergedList) {

					chrom2 = ((VCFData) record).getChrom();
					pos2 = ((VCFData) record).getPos();
					String altList = ((VCFComplexGroup) group).getAltList1();
					String refList = ((VCFComplexGroup) group).getAltList2();

					if (chrom1.equals(chrom2) && pos1 == pos2) {
						((VCFData) record).setAltList1(altList);
						((VCFData) record).setAltList2(refList);
					}
				}
			}

		} catch (Exception e) {
			System.out.println("Grouping " + e.toString());
		}
		int size = 32000000;

		PrintWriter fw = new PrintWriter(new BufferedWriter(new FileWriter("merged_output22.txt"), size));

		String output = null;
		for (Object mrgRecord : mergedList) {
			String subjID = ((VCFData) mrgRecord).getSubjectID();
			String chrom = ((VCFData) mrgRecord).getChrom();
			int pos = ((VCFData) mrgRecord).getOrgPos();
			String ref = ((VCFData) mrgRecord).getRef();
			String alt = ((VCFData) mrgRecord).getAlt();
			String allele1 = ((VCFData) mrgRecord).getAllele1();
			String allele2 = ((VCFData) mrgRecord).getAllele2();
			String vartype1 = ((VCFData) mrgRecord).getVartype1();
			String vartype2 = ((VCFData) mrgRecord).getVartype2();
			String genotype = ((VCFData) mrgRecord).getGenotype();
			int modPos1 = ((VCFData) mrgRecord).getModStartPos1();
			int modPos2 = ((VCFData) mrgRecord).getModStartPos2();
			String modRef1 = ((VCFData) mrgRecord).getModRef1();
			String modRef2 = ((VCFData) mrgRecord).getModRef2();
			String modAlt1 = ((VCFData) mrgRecord).getModAlt1();
			String modAlt2 = ((VCFData) mrgRecord).getModAlt2();
			String modAltList1 = ((VCFData) mrgRecord).getAltList1();
			String modAltList2 = ((VCFData) mrgRecord).getAltList2();
			String type = ((VCFData) mrgRecord).getType();

			List<String> altList3 = new ArrayList<String>();
			String altLista = modAltList1.replace("[", "");
			String altListb = altLista.replace("]", "");

			for (String alt1 : altListb.split(",")) {
				altList3.add(alt1.trim());
			}

			List<String> altList4 = new ArrayList<String>();
			String altListc = modAltList2.replace("[", "");
			String altListd = altListc.replace("]", "");

			for (String alt1 : altListd.split(",")) {
				altList4.add(alt1.trim());
			}

			Collections.sort(altList3, new LengthFirstComparator());
			Collections.sort(altList4, new LengthFirstComparator());
			String altList;

			if (vartype1.equals("del")) {
				altList = altList4.toString();
			} else {
				altList = altList3.toString();
			}
			if (type.equals("s")) {
				String newGT = createGenotype(genotype, modRef1, modAlt1,
						modAlt2, altList, vartype1);

				if (vartype1.equals("del")) {
					output = subjID + "\t" + chrom + "\t" + pos + "\t" + ref
							+ "\t" + alt + "\t" + allele1 + "\t" + allele2
							+ "\t" + genotype + "\t" + vartype1 + "\t"
							+ modPos1 + "\t" + modRef1 + "\t" + modAlt1 + "\t"
							+ modAlt2 + "\t" + altList4 + "\t" + newGT + "\n";

				} else if (!vartype1.equals("del")) {
					output = subjID + "\t" + chrom + "\t" + pos + "\t" + ref
							+ "\t" + alt + "\t" + allele1 + "\t" + allele2
							+ "\t" + genotype + "\t" + vartype1 + "\t"
							+ modPos1 + "\t" + modRef1 + "\t" + modAlt1 + "\t"
							+ modAlt2 + "\t" + altList3 + "\t" + newGT + "\n";

				} else if (vartype2 != null && output == null) {
					if (vartype2.equals("del")) {
						output = subjID + "\t" + chrom + "\t" + pos + "\t"
								+ ref + "\t" + alt + "\t" + allele1 + "\t"
								+ allele2 + "\t" + genotype + "\t" + vartype1
								+ "\t" + modPos2 + "\t" + modRef1 + "\t"
								+ modAlt1 + "\t" + modAlt2 + "\t" + altList3
								+ "\t" + newGT + "\n";

					} else if (!vartype2.equals("del")) {
						output = subjID + "\t" + chrom + "\t" + pos + "\t"
								+ ref + "\t" + alt + "\t" + allele1 + "\t"
								+ allele2 + "\t" + genotype + "\t" + vartype1
								+ "\t" + modPos2 + "\t" + modRef1 + "\t"
								+ modAlt1 + "\t" + modAlt2 + "\t" + altList4
								+ "\t" + newGT + "\n";
					}
				}

			} else if (type.contains("c")) {
				String[] gts = AlGtComplexRules.createGenotype(genotype,
						modRef1, modRef2, modAlt1, modAlt2, altList3, vartype1,
						altList4, vartype2, modPos1, modPos2);

				if (vartype1.equals("del")) {
					output = subjID + "\t" + chrom + "\t" + pos + "\t" + ref
							+ "\t" + alt + "\t" + allele1 + "\t" + allele2
							+ "\t" + genotype + "\t" + vartype1 + "\t"
							+ modPos1 + "\t" + modRef1 + "\t" + modAlt1 + "\t"
							+ modAlt2 + "\t" + altList4 + "\t" + gts[0] + "\n";

				} else if (!vartype1.equals("del")) {
					output = subjID + "\t" + chrom + "\t" + pos + "\t" + ref
							+ "\t" + alt + "\t" + allele1 + "\t" + allele2
							+ "\t" + genotype + "\t" + vartype1 + "\t"
							+ modPos1 + "\t" + modRef1 + "\t" + modAlt1 + "\t"
							+ modAlt2 + "\t" + altList3 + "\t" + gts[0] + "\n";

				} else if (vartype2 != null && output == null) {
					if (vartype2.equals("del")) {
						output = subjID + "\t" + chrom + "\t" + pos + "\t"
								+ ref + "\t" + alt + "\t" + allele1 + "\t"
								+ allele2 + "\t" + genotype + "\t" + vartype1
								+ "\t" + modPos2 + "\t" + modRef1 + "\t"
								+ modAlt1 + "\t" + modAlt2 + "\t" + altList3
								+ "\t" + gts[1] + "\n";

					} else if (!vartype2.equals("del")) {
						output = subjID + "\t" + chrom + "\t" + pos + "\t"
								+ ref + "\t" + alt + "\t" + allele1 + "\t"
								+ allele2 + "\t" + genotype + "\t" + vartype1
								+ "\t" + modPos2 + "\t" + modRef1 + "\t"
								+ modAlt1 + "\t" + modAlt2 + "\t" + altList4
								+ "\t" + gts[1] + "\n";
					}
				}
			}
			fw.write(output);
		}
		fw.close();
	}
}
