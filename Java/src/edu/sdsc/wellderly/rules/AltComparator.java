package edu.sdsc.wellderly.rules;

import java.util.Comparator;

public class AltComparator implements Comparator<Object> {

	@Override
	public int compare(Object o1, Object o2) {
		String alt1 = ((VCFData) o1).getVartype1();
		String alt2 = ((VCFData) o2).getVartype1();
		return alt1.compareTo(alt2);

	};

}