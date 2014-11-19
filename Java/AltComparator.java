package edu.sdsc.wellderly.rules;

import java.util.Comparator;

public class AltComparator implements Comparator<Object> {

	@Override
	public int compare(Object o1, Object o2) {
		String alt1 = ((VCFData) o1).getAlt();
		String alt2 = ((VCFData) o1).getAlt();
		return alt1.compareTo(alt2);

	};

}