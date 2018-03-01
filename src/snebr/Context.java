package sneps.snebr;

import java.util.HashSet;
import java.util.Iterator;

import sneps.network.classes.semantic.*;

public class Context {
	PropositionSet hypothesisSet;
	HashSet<String> names;
	boolean conflicting;
	HashSet<Contradiction> contradictions;
	int id;
	private static int count = 1;

	public Context() {
		this.names = new HashSet<String>();
		this.conflicting = false;
		this.hypothesisSet = new PropositionSet();
		this.contradictions = new HashSet<Contradiction>();
		this.id = count;
		count++;

	}

	public Context(Proposition proposition) {
		this.names = new HashSet<String>();
		this.conflicting = false;
		this.hypothesisSet = new PropositionSet();
		this.hypothesisSet.propositions.add(proposition);
		this.contradictions = new HashSet<Contradiction>();
		this.id = count;
		count++;
	}

	public Context(PropositionSet propSet) {
		this.hypothesisSet = new PropositionSet();
		hypothesisSet.addPropSet(propSet);
		this.names = new HashSet<String>();
		this.conflicting = false;
		this.contradictions = new HashSet<Contradiction>();
		this.id = count;
		count++;

	}

	public Context(Context context) {
		this.names = new HashSet<String>();
		this.conflicting = context.conflicting;
		this.hypothesisSet = new PropositionSet();
		this.hypothesisSet.addPropSet(context.hypothesisSet);
		this.contradictions = context.contradictions;
		this.id = count;
		count++;

	}

	public Context(Proposition hyp, String name) {
		this.hypothesisSet = new PropositionSet();
		hypothesisSet.propositions.add(hyp);
		this.names = new HashSet<String>();
		names.add(name);
		this.conflicting = false;
		this.contradictions = new HashSet<Contradiction>();
		this.id = count;
		count++;
	}

	public void addCont(Contradiction c) {
		this.contradictions.add(c);
	}

	public void addContextToProp(Context context) {
		for (Iterator<Proposition> iterator = this.hypothesisSet.propositions
				.iterator(); iterator.hasNext();) {
			Proposition prop = iterator.next();
			prop.addContext(context);

		}
	}

	public void addToContext(Proposition proposition) {
		if (!this.hypothesisSet.propositions.contains(proposition)) {
			this.hypothesisSet.propositions.add(proposition);
		}

	}

	public boolean findByName(String name) {
		return this.names.contains(name);
	}

	public HashSet<String> getNames() {
		return names;
	}

	public void addName(String name) {
		this.names.add(name);
	}

	public void addNames(HashSet<String> names) {
		this.names.addAll(names);
	}

	public void clearNames() {
		names.clear();
	}

	public boolean RemoveName(String name) {
		return names.remove(name);
	}

	public PropositionSet getHypothesisSet() {
		return hypothesisSet;
	}

	public void setHypothesisSet(PropositionSet hypothesisSet) {
		this.hypothesisSet = hypothesisSet;
	}

	public boolean getConflicting() {
		return conflicting;
	}

	public void setConflicting(boolean conflicting) {
		this.conflicting = conflicting;
	}

	public HashSet<Contradiction> getContradictions() {
		return contradictions;
	}

	public void setContradictions(HashSet<Contradiction> contradictions) {
		this.contradictions = contradictions;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public static int getCount() {
		return count;
	}

	public static void setCount(int count) {
		Context.count = count;
	}

	public void setNames(HashSet<String> names) {
		this.names = names;
	}

}
