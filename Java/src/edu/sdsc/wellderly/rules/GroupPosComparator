package edu.sdsc.wellderly.rules;

import java.util.Comparator;

public class GroupPosComparator implements Comparator<Object> {

	@Override
	public int compare(Object o1, Object o2) {
		int pos1 = ((VCFGroup) o1).getPos();
		int pos2 = ((VCFGroup) o2).getPos();
		return pos1 - pos2;

	};
}