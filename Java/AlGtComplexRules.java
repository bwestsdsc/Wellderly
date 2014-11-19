package edu.sdsc.wellderly.rules;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import edu.sdsc.dao.WellConn;

public class AlGtComplexRules extends AlGtSimpleRules {

	static final Set<String> alts = new HashSet<String>();
	static final Set<String> refs = new HashSet<String>();
	static CopyOnWriteArrayList<Object> recordList = new CopyOnWriteArrayList<Object>();
	static ArrayList<Object> groupList = new ArrayList<Object>();

	public AlGtComplexRules() {

	}

	public CopyOnWriteArrayList<Object> getComplexData() throws Exception {

		Connection conn = null;
		ResultSet rs = null;

		try {
			conn = WellConn.getConn();
		} catch (Exception e) {
			System.out.println(e.toString());

		}

		String query = "select subject_id, chrom, pos, ref, split_part(alt, ',', 1) as allele1, "
				+ "split_part(alt, ',', 2) as allele2, "
				+ "split_part(file, ':', 1) as GT, alt "
				+ "from gene.illumina_vcf where alt like '%,%' "
				+ "and (length(split_part(alt,',', 1)) > 1 or length(split_part(alt,',', 2)) > 1) "
				+ "order by  2, 3, 5, 6, 7";

		try {

			PreparedStatement ps = conn.prepareStatement(query);
			rs = ps.executeQuery();

		} catch (Exception e) {
			System.out.println(e.toString());

		}

		try {

			int lastPos = 0;

			VCFComplexGroup vcfGrp = new VCFComplexGroup();
			while (rs.next()) {
				VCFData vcf = new VCFData();
				String chrom = rs.getString(2);
				int pos = rs.getInt(3);
				if (lastPos != 0 && lastPos != pos) {
					vcfGrp.setAltList1(alts.toString());
					vcfGrp.setAltList2(refs.toString());
					groupList.add(vcfGrp);
					alts.clear();
					refs.clear();
					vcfGrp = new VCFComplexGroup();
				}
				String ref = rs.getString(4);
				String allele1 = rs.getString(5);
				String allele2 = rs.getString(6);
				String genotype = rs.getString(7);
				String alt = rs.getString(8);
				String subjID = rs.getString(1);
				vcf.setChrom(chrom);
				vcf.setPos(pos);
				vcf.setAlt(alt);
				vcfGrp.setChrom(chrom);
				vcfGrp.setPos(pos);
				vcf.setOrgPos(pos);
				vcf.setRef(ref);
				vcf.setAllele1(allele1);
				vcf.setAllele2(allele2);
				vcfGrp.setAlt1(allele1);
				vcfGrp.setAlt2(allele2);
				vcf.setGenotype(genotype);
				vcfGrp.setGt(genotype);
				vcf.setSubjectID(subjID);
				vcf.setType("c");
				vcfTrim(vcf);
				recordList.add(vcf);
				lastPos = pos;
			}
			vcfGrp.setAltList1(alts.toString());
			vcfGrp.setAltList2(refs.toString());
			groupList.add(vcfGrp);

			try {
				// cycle through the object to assign the distinct alts set to
				// each
				// record
				for (Object group : groupList) {

					String chrom1 = ((VCFComplexGroup) group).getChrom();
					String chrom2 = "";
					int pos1 = ((VCFComplexGroup) group).getPos();
					int pos2 = 0;

					for (Object record : recordList) {

						chrom2 = ((VCFData) record).getChrom();
						pos2 = ((VCFData) record).getPos();
						String altList = ((VCFComplexGroup) group)
								.getAltList1();
						String refList = ((VCFComplexGroup) group)
								.getAltList2();

						if (chrom1.equals(chrom2) && pos1 == pos2) {
							((VCFData) record).setAltList1(altList);
							((VCFData) record).setAltList2(refList);
						}
					}
				}

			} catch (Exception e) {
				System.out.println("Grouping " + e.toString()
						+ e.getStackTrace().toString());
			}

			try {
				for (Object record : recordList) {

					String modAlt1 = ((VCFData) record).getModAlt1();
					String modAlt2 = ((VCFData) record).getModAlt2();
					String modRef1 = ((VCFData) record).getModRef1();
					String modRef2 = ((VCFData) record).getModRef2();
					int modStartPos1 = ((VCFData) record).getModStartPos1();
					int modStartPos2 = ((VCFData) record).getModStartPos2();
					String gt = ((VCFData) record).getGenotype();
					String altList = ((VCFData) record).getAltList1();
					String refList = ((VCFData) record).getAltList2();
					String varType1 = ((VCFData) record).getVartype1();
					String varType2 = ((VCFData) record).getVartype2();

					List<String> altList3 = new ArrayList<String>();
					String altLista = altList.replace("[", "");
					String altListb = altLista.replace("]", "");

					for (String alt : altListb.split(",")) {
						altList3.add(alt.trim());
					}

					List<String> altList4 = new ArrayList<String>();
					String altListc = refList.replace("[", "");
					String altListd = altListc.replace("]", "");

					for (String alt : altListd.split(",")) {
						altList4.add(alt.trim());
					}

					Collections.sort(altList3, new LengthFirstComparator());
					Collections.sort(altList4, new LengthFirstComparator());

					String[] gt2 = createGenotype(gt, modRef1, modRef2,
							modAlt1, modAlt2, altList3, varType1, altList4,
							varType2, modStartPos1, modStartPos2);

					((VCFData) record).setAltList1(altList3.toString());
					((VCFData) record).setAltList2(altList4.toString());
					((VCFData) record).setModGT1(gt2[0]);
					((VCFData) record).setModGT2(gt2[1]);

				}
			} catch (Exception e) {
				System.out.println("Display " + e.toString());
				e.printStackTrace();
			}

		} catch (Exception e) {
			System.out.println("Get Data" + e.toString());
		} finally {
			if (conn != null)
				conn.close();
		}
		return recordList;

	}

	// Recompute the genotype using the distinct list and each allele
	protected static String[] createGenotype(String gt, String ref1,
			String ref2, String allele1, String allele2, List<String> altList3,
			String vartype1, List<String> altList4, String vartype2,
			int modPos1, int modPos2) {

		int a1 = 0;
		String a2 = "";
		int a3 = 0;

		int a5 = 0;

		String genoType1 = "";
		String genoType2 = "";
		String[] genoType = gt.split("");

		try {

			// set the basis for the calculation. dels have a base of ref and
			// all others have a basis of the alts
			// System.out.println(x);
			if (modPos1 == modPos2) {
				if (!vartype1.equals("del")) {
					a1 = altList3.indexOf(allele1) + 1;
				} else {
					a1 = altList4.indexOf(ref1) + 1;
				}
				if (!vartype2.equals("del")) {
					a3 = altList3.indexOf(allele2) + 1;
				} else {
					a3 = altList4.indexOf(ref2) + 1;
				}
				a2 = genoType[1];
				genoType1 = Integer.toString(a1) + a2 + Integer.toString(a3);
			} else {
				if (vartype1.equals("del")) {
					a1 = altList4.indexOf(ref1) + 1;
				} else {
					a1 = altList3.indexOf(allele1) + 1;
				}
				if (vartype2 != null && vartype2.equals("del")) {
					a5 = altList4.indexOf(ref2) + 1;
				} else {
					a5 = altList3.indexOf(allele2) + 1;
				}
				a2 = genoType[1];
				int modGT = a1 != 0 ? a1 : a5;
				if (!allele1.equals("X")) {
					genoType1 = Integer.toString(modGT) + a2 + "X";
				} else {
					genoType1 = "X" + a2 + Integer.toString(modGT);
				}

				genoType2 = "X" + a2 + Integer.toString(a5);

			}

		} catch (Exception e) {
			System.out.println("Genotyping " + e.toString());
			e.printStackTrace();
		}
		return new String[] { genoType1, genoType2 };
	}

	// Method that trims the begin and ends of complex variations
	public static void vcfTrim(Object vcf) {

		try {
			int end1 = 0;
			int end2 = 0;
			int begin1 = ((VCFData) vcf).getPos() - 1;
			int begin2 = ((VCFData) vcf).getPos() - 1;
			String var1 = ((VCFData) vcf).getAllele1();
			String var2 = ((VCFData) vcf).getAllele2();
			String ref = ((VCFData) vcf).getRef();
			String ref1 = ((VCFData) vcf).getRef();

			for (int i = 0; i < Math.min(var1.length(), ref.length()); i++) {
				if (var1.substring(var1.length() - i - 1, var1.length() - i)
						.equals(ref.substring(ref.length() - i - 1,
								ref.length() - i))) {
					end1 = end1 + 1;
				} else {
					break;
				}
			}

			for (int i = 0; i < Math.min(var2.length(), ref1.length()); i++) {
				if (var2.substring(var2.length() - i - 1, var2.length() - i)
						.equals(ref1.substring(ref1.length() - i - 1,
								ref1.length() - i))) {
					end2 = end2 + 1;
				} else {
					break;
				}
			}

			if (end1 != 0) {
				ref = ref.substring(0, ref.length() - end1);
				var1 = var1.substring(0, var1.length() - end1);
			}

			if (end2 != 0) {
				ref1 = ref1.substring(0, ref1.length() - end2);
				var2 = var2.substring(0, var2.length() - end2);
			}

			String varType1 = null;
			String varType2 = null;

			int start1 = 0;
			for (int i = 0; i < Math.min(var1.length(), ref.length()); i++) {
				if (var1.charAt(i) == ref.charAt(i)) {
					start1 = start1 + 1;
				} else {
					break;
				}
			}

			int start2 = 0;
			for (int i = 0; i < Math.min(var2.length(), ref1.length()); i++) {
				if (var2.charAt(i) == ref1.charAt(i)) {
					start2 = start2 + 1;
				} else {
					break;
				}
			}

			// trim the head
			ref = ref.substring(start1);
			ref1 = ref1.substring(start2);
			var1 = var1.substring(start1);
			var2 = var2.substring(start2);
			begin1 = begin1 + start1;
			begin2 = begin2 + start2;

			// is this a snp
			if (ref.length() == 1 && var1.length() == 1) {
				varType1 = "snp";

			}

			else if (ref.length() == 0 && var1.length() > 0) {
				begin1 = begin1 + 1;
				varType1 = "ins";
				ref = "-";
			}

			else if (ref.length() > 0 && var1.length() == 0) {
				varType1 = "del";
				var1 = "-";

			}

			else if (ref.length() > 0 && var1.length() > 0 && !ref.equals(var1)) {
				varType1 = "sub";
				var1 = "-";

			}

			// is this a snp
			if (ref1.length() == 1 && var2.length() == 1) {
				varType2 = "snp";

			}

			else if (ref1.length() == 0 && var2.length() > 0) {
				begin2 = begin2 + 1;
				varType2 = "ins";
				ref1 = "-";
			}

			else if (ref1.length() > 0 && var2.length() == 0) {
				varType2 = "del";
				var2 = "-";

			}

			else if (ref1.length() > 0 && var2.length() > 0
					&& !ref1.equals(var2)) {
				varType2 = "sub";
			}

			((VCFData) vcf).setModAlt1(var1);
			((VCFData) vcf).setModAlt2(var2);

			if (!ref.equals("-") && !ref.equals("") && ref != null) {
				refs.add(ref);
			}
			if (!ref1.equals("-") && !ref1.equals("") && ref1 != null) {
				refs.add(ref1);
			}
			if (!var1.equals("-") && !var1.equals("") && var1 != null) {
				if (!var1.equals(ref)) {
					alts.add(var1);
				}
			}
			if (!var2.equals("-") && !var2.equals("") && var2 != null) {
				if (!var2.equals(ref1)) {
					alts.add(var2);
				}
			}

			((VCFData) vcf).setModStartPos1(begin1);
			((VCFData) vcf).setModStartPos2(begin2);
			((VCFData) vcf).setModAlt1(var1);
			((VCFData) vcf).setModAlt2(var2);
			((VCFData) vcf).setVartype1(varType1);
			((VCFData) vcf).setVartype2(varType2);
			((VCFData) vcf).setModRef1(ref);
			((VCFData) vcf).setModRef2(ref1);

		} catch (Exception e) {
			System.out.println("Trimmming ");
			e.printStackTrace();
		}
	}
}
