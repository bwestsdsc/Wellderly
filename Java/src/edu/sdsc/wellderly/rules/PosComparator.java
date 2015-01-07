package edu.sdsc.wellderly.rules;

import java.util.Comparator;

public class PosComparator implements Comparator<Object> {

	@Override
	public int compare(Object o1, Object o2) {
		int pos1 = ((VCFData) o1).getModStartPos1();
		int pos2 = ((VCFData) o2).getModStartPos1();
		return pos1 - pos2;

	};
}
