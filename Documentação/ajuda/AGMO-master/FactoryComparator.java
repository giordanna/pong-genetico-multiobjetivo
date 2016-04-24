package comparators;

import ga.Chromosome;

import java.util.Comparator;

public class FactoryComparator {
	
	public static Comparator<Chromosome> getComparator(int objective) {
		switch(objective){
		case 0:
			return new SimplicityComparator();			
		case 1:
			return new SenspeComparator();
		case 2:
			return new SensibilityComparator();
		case 3:
			return new SpecificityComparator();
		default:
			return null;
		}
	}
}
