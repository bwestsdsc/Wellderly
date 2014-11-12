package edu.sdsc.wellderly.rules;

import java.sql.Connection;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.sdsc.dao.WellConn;

public class AlGtSimpleRules {

	static final Set<String> alts1 = new HashSet<String>();
	static ArrayList<Object> mergedList = new ArrayList<Object>();
	static ArrayList<Object> recordList = new ArrayList<Object>();
	static ArrayList<Object> groupList = new ArrayList<Object>();

	public AlGtSimpleRules() {
	}

	static AlGtComplexRules vcfComp1 = null;

	public static void main(String[] args) {
		try {
			getData();
			AlGtComplexRules vcfComp = new AlGtComplexRules();
			mergedList = vcfComp.getComplexData();
			mergedList.addAll(recordList);
			createMergedList(mergedList);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void createMergedList(ArrayList<Object> mergedList2) throws IOException, SQLException {

		String output = null;
		
		Connection conn = null;
		
		try {
			conn = WellConn.getConn();
		} catch (Exception e) {
			System.out.println(e.toString());

		}
		
		try {
			
			for (Object mrgRecord : mergedList) {
				String chrom = ((VCFData) mrgRecord).getChrom();
				int pos = ((VCFData) mrgRecord).getPos();
				String ref = ((VCFData) mrgRecord).getRef();
				String alt = ((VCFData) mrgRecord).getAlt();
				String allele1 = ((VCFData) mrgRecord).getAllele1();
				String allele2 = ((VCFData) mrgRecord).getAllele2();
				String genotype = ((VCFData) mrgRecord).getGenotype();
				String modRef1 = ((VCFData) mrgRecord).getModRef1();
				String modRef2 = ((VCFData) mrgRecord).getModRef2();
				String modAlt1 = ((VCFData) mrgRecord).getModAlt1();
				String modAlt2 = ((VCFData) mrgRecord).getModAlt2();
				String modAltList1 = ((VCFData) mrgRecord).getAltList1();
				String modAltList2 = ((VCFData) mrgRecord).getAltList2();
				String modGT1 = ((VCFData) mrgRecord).getModGT1();
				String modGT2 = ((VCFData) mrgRecord).getModGT2();
				String vartype1 = ((VCFData) mrgRecord).getVartype1();
				String vartype2 = ((VCFData) mrgRecord).getVartype2();
				String type = ((VCFData) mrgRecord).getType();
				int modPos1 = ((VCFData) mrgRecord).getModStartPos1();
				int modPos2 = ((VCFData) mrgRecord).getModStartPos2();

				String alleleList1 = null;
				String alleleList2 = null;
				if (vartype1.equalsIgnoreCase("del")) {
					alleleList1 = modAltList2;
				} else {
					alleleList1 = modAltList1;
				}
				if (vartype2 != null){
					if (vartype2.equals("del")) {
						alleleList2 = modAltList2;
					} else {
						alleleList2 = modAltList1;
					}
				}

				
				if (type.equals("s")) {
					if (vartype1.equals("del")) {
						output = "insert into gene.vcf_tmp (chrom, pos, ref, "
								+ "alt, allele1, allele2, org_gt, vartype, mod_pos, "
								+ "mod_ref, mod_allele1, mod_allele2, allele_list, new_gt) values "
								+ "('" + chrom + "','"  + pos + "','" + ref + "','" + alt + "','"
								+ allele1 + "','" + allele2 + "','"
								+ genotype + "','" + vartype1 + "','" + modPos1
								+ "','" + modRef1 + "','" + modAlt1 + "','"
								+ modAlt2 + "','" + modAltList1 + "','" + modGT1
								+ "')";
					} else {

						output = "insert into gene.vcf_tmp (chrom, pos, ref, "
								+ "alt, allele1, allele2, org_gt, vartype, mod_pos, "
								+ "mod_ref, mod_allele1, mod_allele2, allele_list, new_gt) values " 
								+ "('" +chrom + "','" + pos + "','" + ref + "','" + alt
								+ "','" + allele1 + "','" + allele2 + "','"
								+ genotype + "','" + vartype1 + "','" + modPos1
								+ "','" + modRef1 + "','" + modAlt1 + "','"
								+ modAlt2 + "','" + modAltList1 + "','" + modGT1
								+ "');";

					}
				} else if (type.equals("c") && modPos1 == modPos2) {
					output = "insert into gene.vcf_tmp (chrom, pos, ref, "
							+ "alt, allele1, allele2, org_gt, vartype, mod_pos, "
							+ "mod_ref, mod_allele1, mod_allele2, allele_list, new_gt) values "
							+ "('" + chrom + "','" + pos + "','" + ref + "','" + alt
							+ "','" + allele1 + "','" + allele2 + "','" + genotype
							+ "','" + vartype1 + "','" + modPos1 + "','" + modRef1
							+ "','" + modAlt1 + "','" + modAlt2 + "','"
							+ alleleList1 + "','" + modGT1 + "');";

				} else if (type.equals("c") && modPos1 != modPos2) {
					output = "insert into gene.vcf_tmp (chrom, pos, ref, "
							+ "alt, allele1, allele2, org_gt, vartype, mod_pos, "
							+ "mod_ref, mod_allele1, mod_allele2, allele_list, new_gt) values "
							+ "('" + chrom + "','" + pos + "','" + ref + "','" + alt
							+ "','" + allele1 + "','" + allele2 + "','" + genotype
							+ "','" + vartype1 + "','" + modPos1 + "','" + modRef1
							+ "','" + modAlt1 + "','" + "X" + "','" + alleleList1
							+ "','" + modGT1 + "');";

					output += "insert into gene.vcf_tmp (chrom, pos, ref, "
							+ "alt, allele1, allele2, org_gt, vartype, mod_pos, "
							+ "mod_ref, mod_allele1, mod_allele2, allele_list, new_gt) values "
							+ "('" + chrom + "','" + pos + "','" + ref + "','" + alt
							+ "','" + allele1 + "','" + allele2 + "','" + genotype
							+ "','" + vartype2 + "','" + modPos2 + "','" + modRef2
							+ "','" + "X" + "','" + modAlt2 + "','" + alleleList2
							+ "','" + modGT2 + "');";
					
					
				}
				try {
					//System.out.println(output);
					PreparedStatement ps = conn.prepareStatement(output);
					ps.executeQuery();

				} catch (Exception e) {
					System.out.println(e.toString());

				}

			}

		} catch (Exception e) {
			System.out.println("Merge List " + e.toString());
			e.printStackTrace();
		}finally{
			conn.close();
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
				+ "from gene.illumina_vcf where chrom = 'chr1' and "
				+ "alt not like '%,%' or (alt like '%,%' and length(split_part(alt,',', 1)) = 1 "
				+ "and length(split_part(alt,',', 2)) = 1) "
				+ "order by 1, 2, 4, 7 offset 0 limit 5";

		try {
			PreparedStatement ps = conn.prepareStatement(query);
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
					String altList1 = ((VCFGroup) group).getAltList1();
					// assign discrete list of alts or refs
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
				sortAlleleList((VCFData) record);
				String modAltList = ((VCFData) record).getAltList1();
				String modGT = createGenotype(gt1, modRef, modAlt1, modAlt2,
						modAltList, vartype);
				((VCFData) record).setModGT1(modGT);

				/*
				 * // this will eventually be loaded into a delimited file and
				 * bulk // loaded to the db. For now just print to standard out
				 */
				/*
				 * System.out.println(subjID + "\t" + chrom + "\t" + pos + "\t"
				 * + ref + "\t" + alt + "\t" + allele1 + "\t" + allele2 + "\t" +
				 * vartype + "\t" + pos + "\t" + gt1 + "\t" + modStartPos + "\t"
				 * + modRef + "\t" + "\t" + modAlt1 + "\t" + modAlt2 + "\t" +
				 * modAltList + "\t" + modGT);
				 */

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
			((VCFData) vcf).setAllele2(alt);
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
					}

					else if (ref.length() == 0 && vars[0].length() > 0) {
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
					alts1.add(vars[0]);
					alts1.add(vars[1]);
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
					alts1.add(var);
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

	public static void sortAlleleList(VCFData vcf) {
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

}
