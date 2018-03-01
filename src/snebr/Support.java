package sneps.snebr;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import sneps.network.classes.semantic.Proposition;
import sneps.network.nodes.PropositionNode;

public class Support {
	Context originSet;

	public Support(Proposition proposition) {
		this.originSet = new Context(proposition);
	}

	public Support() {
		this.originSet = new Context();
	}

	public Support(Context c) {
		this.originSet = c;
	}

	public boolean assertedInContext(Context context) {
		if (context.hypothesisSet.propositions
				.containsAll(this.originSet.hypothesisSet.propositions))
			return true;
		return false;
	}

	public void addToOriginSet(Proposition proposition) {
		this.originSet.addToContext(proposition);
	}

	public Context getOriginSet() {
		return originSet;
	}

	public void setOriginSet(Context originSet) {
		this.originSet = originSet;
	}

	public Support combine(Support support) {
		Context resContext = new Context();
		for (Proposition pn : originSet.getHypothesisSet())
			resContext.addToContext(pn);
		for (Proposition pn : support.getOriginSet().getHypothesisSet())
			resContext.addToContext(pn);
		return new Support(resContext);
	}

	public static Set<Support> combine(Set<Support> originSupports,
			Set<Support> supports) {

		// long l1 = System.currentTimeMillis();
		if (originSupports == null || originSupports.isEmpty()) {
			// System.out.println("here");
			return supports;
		}

		if (supports == null || supports.isEmpty()) {
			// System.out.println("hello");
			return originSupports;
		}
		Set<Support> res = new HashSet<Support>();

		for (Iterator<Support> iterator3 = originSupports.iterator(); iterator3
				.hasNext();) {
			Support m3 = iterator3.next();
			for (Iterator<Support> iterator4 = supports.iterator(); iterator4
					.hasNext();) {
				Support m4 = iterator4.next();
				res.add(m3.combine(m4));
			}
		}

		// long l2 = System.currentTimeMillis();
		// System.out.println(l2 - l1);
		return res;
	}

	public String toString() {
		return "Names: " + originSet.getNames() + "\nContradictions: "
				+ originSet.getContradictions() + "\nHyps: "
				+ originSet.getHypothesisSet() + "\nCount: "
				+ Context.getCount();
	}

	public static void main(String[] args) {
		Set s1 = new HashSet<Support>();
		Set s2 = new HashSet<Support>();
		Set s3 = new HashSet<Support>();
		for (int j = 0; j < 187; j++) {
			PropositionSet propSet = new PropositionSet();
			for (int i = 0; i < 100; i++) {
				Proposition p = new Proposition();
				propSet.addProposition(p);

			}

			Context context = new Context(propSet);
			Support support = new Support(context);
			s1.add(support);
			s2.add(support);
		}
		s3 = combine(s1, s2);
	}

}
