package edu.sdsc.wellderly.rules;

import java.util.Comparator;

public class RefComparator implements Comparator<Object> {

	@Override
	public int compare(Object o1, Object o2) {
		String ref1 = ((VCFData) o1).getModRef1();
		String ref2 = ((VCFData) o2).getModRef1();
		return ref1.compareTo(ref2);

	};
}
