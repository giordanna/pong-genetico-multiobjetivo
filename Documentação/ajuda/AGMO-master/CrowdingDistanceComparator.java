package comparators;

import ga.Chromosome;

import java.util.Comparator;

public class CrowdingDistanceComparator implements Comparator<Chromosome> {

	/**
	 * This method is used to sort chromosomes by crowding distance.
	 * The method sorts array of chromosomes in DESCENDANT order.
	 * 
	 */
	@Override
	public int compare(Chromosome c1, Chromosome c2) {
		if( c1.getCrowdingDistance() > c2.getCrowdingDistance() )
			return -1;
		else if( c1.getCrowdingDistance() < c2.getCrowdingDistance() )
			return 1;
		return 0;
		
	}

}
