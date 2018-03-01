package sneps.snebr;

import java.util.HashSet;
import java.util.Iterator;

import sneps.network.classes.semantic.Proposition;

public class PropositionSet implements Iterable<Proposition> {
	public HashSet<Proposition> propositions;

	public PropositionSet() {
		this.propositions = new HashSet<Proposition>();
	}

	public PropositionSet(HashSet<Proposition> p) {
		this.propositions = p;
	}

	public void addPropSet(PropositionSet propSet) {
		if (propSet != null)
			this.propositions.addAll(propSet.propositions);
	}

	public void removePropSet(PropositionSet propSet) {
		this.propositions.removeAll(propSet.propositions);
	}

	public void addProposition(Proposition node) {
		propositions.add(node);
	}

	public String toString() {
		return propositions + "";
	}

	public boolean assertedInContext(Context context) {
		if (context.hypothesisSet.propositions.containsAll(this.propositions))
			return true;
		return false;
	}

	public HashSet<PropositionSet> split() {
		HashSet<PropositionSet> split = new HashSet<PropositionSet>();
		int i = 0;
		PropositionSet newSet1 = new PropositionSet();
		PropositionSet newSet2 = new PropositionSet();
		Proposition prop;
		for (Iterator<Proposition> iterator1 = this.propositions.iterator(); iterator1
				.hasNext();) {
			prop = iterator1.next();
			if (this.propositions.size() / 2 <= i) {
				newSet2.propositions.add(prop);
			} else {
				newSet1.propositions.add(prop);
			}
			i++;
		}
		split.add(newSet1);
		split.add(newSet2);
		return split;
	}

	public HashSet<Proposition> getPropositions() {
		return propositions;
	}

	public void setPropositions(HashSet<Proposition> propositions) {

		this.propositions = propositions;
	}

	@Override
	public Iterator<Proposition> iterator() {

		return propositions.iterator();
	}

}
