package edu.sdsc.wellderly.rules;

import java.util.Comparator;

public class ChromComparator implements Comparator<Object> {

	@Override
	public int compare(Object o1, Object o2) {
		String chrom1 = ((VCFData) o1).getChrom();
		String chrom2 = ((VCFData) o2).getChrom();
		return chrom1.compareTo(chrom2);

	};

}
