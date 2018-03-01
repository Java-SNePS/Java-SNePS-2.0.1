package sneps.snebr;

import java.util.HashSet;
import java.util.LinkedList;

import sneps.network.CaseFrame;
import sneps.network.Network;
import sneps.network.RCFP;
import sneps.network.Relation;
import sneps.network.cables.DownCable;
import sneps.network.cables.DownCableSet;
import sneps.network.classes.semantic.*;
import sneps.network.classes.syntactic.Pattern;
import sneps.network.nodes.Node;
import sneps.network.nodes.NodeSet;
import sneps.network.nodes.PropositionNode;

public class test {

	@SuppressWarnings("unused")
	public static void main(String[] args) {

		Entity e = new Entity();

		Node x1 = Network.buildVariableNode();
		Node x2 = Network.buildVariableNode();
		Node x3 = Network.buildVariableNode();

		Node b1 = null;
		try {
			b1 = Network.buildBaseNode("0", e);
		} catch (Exception exp) {
			System.out.println(exp.getMessage());
		}

		Relation arg = null;
		try {
			arg = Network.defineRelation("arg", "Entity", "none", 1);
		} catch (Exception exp) {
			System.out.println(exp.getMessage());
		}
		Relation rel2 = null;
		try {
			rel2 = Network.defineRelation("rel2", "Entity", "none", 1);
		} catch (Exception exp) {
			System.out.println(exp.getMessage());
		}
		// System.out.println(arg.isQuantifier());
		Relation min = null;
		try {
			min = Network.defineRelation("min", "Entity", "none", 1);
		} catch (Exception exp) {
			System.out.println(exp.getMessage());
		}

		Relation max = null;
		try {
			max = Network.defineRelation("max", "Entity", "none", 1);
		} catch (Exception exp) {
			System.out.println(exp.getMessage());
		}

		Relation rel = null;
		try {
			rel = Network.defineRelation("rel", "Entity", "none", 1);
		} catch (Exception exp) {
			System.out.println(exp.getMessage());
		}

		RCFP p1 = Network.defineRelationPropertiesForCF(arg, "none", 1);
		RCFP p2 = Network.defineRelationPropertiesForCF(min, "none", 1);
		RCFP p3 = Network.defineRelationPropertiesForCF(max, "none", 1);
		RCFP p4 = Network.defineRelationPropertiesForCF(rel, "none", 1);
		RCFP p5 = Network.defineRelationPropertiesForCF(rel2, "none", 1);
		LinkedList<RCFP> plist2 = new LinkedList<RCFP>();
		plist2.add(p5);
		LinkedList<RCFP> plist = new LinkedList<RCFP>();
		plist.add(p1);
		plist.add(p2);
		plist.add(p3);

		LinkedList<RCFP> plist1 = new LinkedList<RCFP>();
		plist1.add(p4);

		CaseFrame cf = null;
		try {
			cf = Network.defineCaseFrame("Entity", plist);
		} catch (Exception exp) {
			System.out.println(exp.getMessage());
		}

		CaseFrame cf1 = null;
		try {
			cf1 = Network.defineCaseFrame("Entity", plist1);
		} catch (Exception exp) {
			System.out.println(exp.getMessage());
		}

		CaseFrame cf2 = null;
		try {
			cf2 = Network.defineCaseFrame("Proposition", plist2);
		} catch (Exception exp) {
			System.out.println(exp.getMessage());
		}
		NodeSet tst = new NodeSet();
		tst.addNode(x2);
		tst.addNode(x3);
		DownCable dc22 = new DownCable(rel2, tst);
		LinkedList<DownCable> dCables2 = new LinkedList<DownCable>();
		dCables2.add(dc22);
		DownCableSet dcss = new DownCableSet(dCables2, cf2);
		Pattern bat = new Pattern("m3", dcss);
		NodeSet ns = new NodeSet();
		ns.addNode(x1);
		DownCable dc = new DownCable(rel, ns);
		LinkedList<DownCable> dCables = new LinkedList<DownCable>();
		dCables.add(dc);
		DownCableSet dcs = new DownCableSet(dCables, cf1);

		Pattern pat = new Pattern("Penguins can fly", dcs);
		Proposition prop = new Proposition();
		PropositionNode m1 = new PropositionNode(pat, prop);
		Pattern pat4 = new Pattern("m3", dcs);
		Proposition prop2 = new Proposition();
		PropositionNode m4 = new PropositionNode(pat4, prop2);

		NodeSet ns1 = new NodeSet();
		ns1.addNode(b1);
		DownCable dc1 = new DownCable(min, ns1);
		DownCable dc2 = new DownCable(max, ns1);
		NodeSet ns2 = new NodeSet();
		ns2.addNode(m1);
		DownCable dc3 = new DownCable(arg, ns2);
		LinkedList<DownCable> dCables1 = new LinkedList<DownCable>();
		dCables1.add(dc1);
		dCables1.add(dc2);
		dCables1.add(dc3);
		DownCableSet dcs1 = new DownCableSet(dCables1, cf);

		Pattern pat1 = new Pattern("Penguins cannot fly", dcs1);
		Proposition prop1 = new Proposition();
		PropositionNode m2 = new PropositionNode(pat1, prop1);
		Proposition pr1 = new Proposition();
		PropositionNode m3 = new PropositionNode(bat, pr1);
		Context c = new Context((Proposition) m3.getSemantic());
		Support s = new Support(c);
		((Proposition) m1.getSemantic()).getOriginSupport().add(s);
		Context c1 = new Context();
		c1.addName("a");
		ContextSet hash = new ContextSet();
		hash.add(c1);
		SNeBR.setContextSet(hash);
		// SNeBR.setCurrentContext(c1);
		// System.out.println(m1);

		// System.out.println(m2);
		System.out.println("Asserting Penguins can fly in context 'a'");
		Context a = SNeBR.assertProposition(m1, "a");
		Proposition q3 = (Proposition) m1.getSemantic();
		// System.out.println(q3.getOriginSupport());
		System.out
				.println("Assertion of Penguins can fly was successful in context 'a'");
		// System.out.println(a.names + "testing");
		System.out
				.println("Now Context 'a' contains the proposition Penguins can fly");
		System.out.println(" with ID: " + a.hypothesisSet.propositions);
		System.out.println();
		System.out.println("Asserting Penguins cannot fly in context 'a'");
		Context b = SNeBR.assertProposition(m2, "a");
		System.out
				.println("Contradiction Resolved automatically, the system is consistent now");
		// Context q10 = SNeBR.assertProposition(m3, "a");
		// System.out.println(b.names + "testing");
		// System.out.println(b.hypothesisSet.propositions);
		// System.out.println(m1.getIdentifier());
		// System.out.println(m2.getIdentifier());
		Proposition q = (Proposition) m1.getSemantic();
		// Proposition q1 = (Proposition) m1.getSemantic();
		// System.out.println(q.isAsserted(a));
		// System.out.println(q.isAsserted(b));

		// Context a = SNeBR.getContextSet().getContext("a");
		// System.out.println(a.conflicting + "---------------");
		// System.out.println(b.conflicting + "---------------------");
		// System.out.println(SNeBR.minimalNoGoods);
		// Iterator<Contradiction> iterator = a.contradictions.iterator();
		PropositionSet x = new PropositionSet();
		x.propositions.add((Proposition) m2.getSemantic());
		x.propositions.add((Proposition) m1.getSemantic());
		// HashSet<Support>s2=prop1.calcOrigin(x);
		// Iterator<Support> iterator2 = s2.iterator();
		// SNeBR.propositionsToBeDiscarded(x, "", a, iterator.next());
		// Context r=SNeBR.contextSet.getContext("a");
		// System.out
		// .println(iterator2.next().originSet.hypothesisSet.propositions
		// .contains(m1));
		// System.out
		// .println(iterator2.next().originSet.hypothesisSet.propositions
		// .contains(m3));
	}
}
