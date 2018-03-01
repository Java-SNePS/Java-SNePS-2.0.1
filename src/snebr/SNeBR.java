package sneps.snebr;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import com.sun.javafx.collections.MappingChange.Map;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

import sneps.network.classes.semantic.Proposition;
import sneps.network.nodes.PropositionNode;

public class SNeBR {
	private static ContextSet contextSet = new ContextSet();
	private static Context currentContext = new Context();
	private static HashSet<Support> newSupportInAllContexts = new HashSet<Support>();
	private static HashSet<Support> oldSupportInAllContexts = new HashSet<Support>();
	// private static Proposition newContradictingProp = new Proposition();
	// private static PropositionSet oldContradictingPropSet = new
	// PropositionSet();
	private static Contradiction currentContradiction = new Contradiction();

	static HashSet<PropositionSet> minimalNoGoods = new HashSet<PropositionSet>();
	private static boolean automatic = true;
	private static boolean name = false;
	private static int whichMethod = -1;

	private SNeBR() {
	}

	/**
         * 
         */
	public static Support createSupport(PropositionSet propSet) {

		Context c = contextSet.getContext(propSet);
		Support s = new Support(c);
		return s;

	}

	public static Contradiction getCurrentContradiction() {
		return currentContradiction;
	}

	public static void setCurrentContradiction(Contradiction currenContradiction) {
		SNeBR.currentContradiction = currenContradiction;
	}

	/**
	 * sets the current context to the selected context
	 * 
	 * @param context
	 *            the context i want to make the current context
	 */
	public static void setCurrentContext(Context context) {
		currentContext = context;
	}

	public static Context getCurrentContext() {
		return currentContext;
	}

	public static Context getContextByName(String name) {
		return contextSet.getContext(name);
	}

	public static Context getContextByID(int id) {
		return contextSet.getContext(id);
	}

	private static Context multipleRemoveProposition(PropositionSet propSet,
			boolean name, String contextName, Contradiction cont) {
		if (currentContext.contradictions.contains(cont)) {

			Context context = new Context(currentContext);
			if (context.hypothesisSet.propositions
					.containsAll(propSet.propositions)) {
				if (name) {
					context.addName(contextName);
					currentContext.RemoveName(contextName);
				}
				context.hypothesisSet.propositions
						.removeAll(propSet.propositions);
				contextSet.add(context);
				// context = afterRemoved(context);
				context.addContextToProp(context);
				currentContext = context;
				context.contradictions.remove(cont);
				return context;
			}

			else {
				System.out.print("this proposition is not in this context");

				return null;
			}
		} else {
			System.out
					.println("This contradiction does not exist in the current context");
			return null;
		}
	}

	/**
	 * remove a proposition from the context that has this name
	 * 
	 * @param proposition
	 *            the proposition i want to remove
	 * @param name
	 *            the name of the context i want to remove the proposition from
	 */
	private static Context multipleRemoveProposition(PropositionSet propSet,
			String name, Contradiction cont, Context context) {

		// Context context = contextSet.getContext(name);
		if (context == null) {
			System.out
					.print("there is no context with such name to remove the proposition from");
			return null;
		} else {
			if (context == currentContext) {
				return multipleRemoveProposition(propSet, true, name, cont);

			} else {
				if (context.contradictions.contains(cont)) {

					if (context.hypothesisSet.propositions
							.containsAll(propSet.propositions)) {
						context.RemoveName(name);
						Context newContext = new Context(context);
						newContext.hypothesisSet.propositions
								.removeAll(propSet.propositions);
						// newContext.removePropFromRestriction(proposition);
						newContext.addName(name);
						newContext = afterRemoved(newContext, name);
						newContext.addContextToProp(newContext);
						newContext.contradictions.remove(cont);
						return newContext;
					} else {
						System.out
								.print("this proposition is not in this context");
						return null;
					}
				} else {

					System.out
							.println("This contradiction does not exist in the current context");
					return null;
				}
			}
		}

	}

	/**
	 * remove a proposition from the selected context
	 * 
	 * @param proposition
	 *            the proposition i want to remove
	 * @param context
	 *            the context i want to remove the proposition from
	 */
	private static Context multipleRemoveProposition(PropositionSet propSet,
			Context context, Contradiction cont) {

		if (context == currentContext) {
			return multipleRemoveProposition(propSet, false, "", cont);
		} else {
			if (context.contradictions.contains(cont)) {
				if (context.hypothesisSet.propositions
						.containsAll(propSet.propositions)) {
					Context newContext = new Context(context);
					newContext.hypothesisSet.propositions
							.removeAll(propSet.propositions);
					contextSet.add(newContext);
					newContext.addContextToProp(newContext);
					context.contradictions.remove(cont);

					return newContext;
				} else {
					System.out
							.print("This context does not contain all these propsoitions!");
					return null;
				}

			} else {
				return null;
			}
		}
	}

	public static Context removeProposition(Proposition proposition) {
		return removeProposition(proposition, false, "");
	}

	/**
	 * remove a proposition from the current context
	 * 
	 * @param proposition
	 *            the proposition i want to remove from the context
	 */
	private static Context removeProposition(Proposition proposition,
			boolean name, String contextName) {
		Context context = new Context(currentContext);
		if (context.hypothesisSet.propositions.contains(proposition)) {
			if (name) {
				context.addName(contextName);
				currentContext.RemoveName(contextName);
			}
			System.out.println("erorrrrrrrrrrrrrrr");
			context.hypothesisSet.propositions.remove(proposition);
			// context = afterRemoved(context);
			contextSet.add(context);
			context.addContextToProp(context);
			currentContext = context;
			return context;
		}

		else {
			System.out.print("this proposition is not in this context");

			return null;
		}
	}

	/**
	 * remove a proposition from the context that has this name
	 * 
	 * @param proposition
	 *            the proposition i want to remove
	 * @param name
	 *            the name of the context i want to remove the proposition from
	 */
	public static Context removeProposition(Proposition proposition, String name) {
		Context context = contextSet.getContext(name);
		if (context == null) {
			System.out
					.print("there is no context with such name to remove the proposition from");
			return null;
		} else {
			if (context == currentContext) {
				return removeProposition(proposition, true, name);

			} else {
				if (context.hypothesisSet.propositions.contains(proposition)) {
					context.RemoveName(name);
					Context newContext = new Context(context);
					// if ((proposition).getContextSet().contains(context)) {
					newContext.hypothesisSet.propositions.remove(proposition);
					// newContext.removePropFromRestriction(proposition);
					newContext.addName(name);
					newContext = afterRemoved(newContext, name);
					newContext.addContextToProp(newContext);
					return newContext;
					// }

				} else {
					System.out.print("this proposition is not in this context");
					return null;
				}
			}
		}

	}

	/**
	 * remove a proposition from the selected context
	 * 
	 * @param proposition
	 *            the proposition i want to remove
	 * @param context
	 *            the context i want to remove the proposition from
	 */
	public static Context removeProposition(Proposition proposition,
			Context context) {
		if (context == currentContext) {
			return removeProposition(proposition);
		} else {
			if (context.hypothesisSet.propositions.contains(proposition)) {
				Context newContext = new Context(context);
				newContext.hypothesisSet.propositions.remove(proposition);
				// newContext = afterRemoved(newContext);
				contextSet.add(newContext);
				newContext.addContextToProp(newContext);
				return newContext;
			} else {
				System.out.print("this proposition is not in this context");
				return null;
			}

		}
	}

	/**
	 * adding a proposition to a context with a specific name if found added to
	 * it if not creats a new one
	 * 
	 * @param proposition
	 *            proposition you want to add to the context
	 * @param name
	 *            the name of the context
	 */
	public static Context assertProposition(PropositionNode node, String name) {

		Proposition proposition = (Proposition) node.getSemantic();
		Context context = contextSet.getContext(name);
		// System.out.println(context);
		if (context == null) {
			System.out
					.print("there is no context with such name to assert the proposition in it");
			return null;
			// Context newContext = new Context(proposition, name);
			// contextSet.addContext(newContext);
			// (proposition).addContext(newContext);
			// return newContext;
		} else {
			if (context == currentContext) {
				return assertProposition(node, true, name);

			} else {
				// System.out.println(context.names);

				Context newContext = new Context(context);
				// System.out.println("1--------"
				// + newContext.hypothesisSet.propositions);
				newContext.addToContext(proposition);

				// newContext = afterAssert(newContext);
				context.addContextToProp(newContext);
				(proposition).addContext(newContext);
				// System.out.println("2--------"
				// + newContext.hypothesisSet.propositions);
				context.RemoveName(name);
				newContext.addName(name);
				Context c = CheckForContradiction(node, name, newContext);
				if (c != null) {
					contextSet.addContext(c);
					return c;
				} else {
					return null;
				}

			}
		}

	}

	public static Context CheckForContradiction(PropositionNode node,
			String name, Context newContext) {// TODO hal keda monaseb lel
												// inference el parameters?
		// System.out.println(newContext.hypothesisSet.propositions);
		System.out.println("Checking for Contradiction....");
		HashSet<PropositionSet> causingContradictionSet = new HashSet<PropositionSet>();
		node.CheckForNegation(newContext);
		// System.out.println(conradictingPropSet.propositions.size());
		fillMinimalNoGoods();
		causingContradictionSet = Contradiction(newContext);
		// System.out.println("contradicting flag " + newContext.conflicting);
		if (causingContradictionSet.isEmpty()) {
			System.out.println("No Contradiction, the system is consistent");
			return newContext;
		} else {
			System.out
					.println("A contradiction was detected between Penguins can fly");
			System.out.println(" and Penguins cannot fly!");
			// newContext.setConflicting(true);
			whichMethod = 1;
			if (automatic) {
				// Contradiction cont = new Contradiction(
				// (Proposition) node.getSemantic(), conradictingPropSet);
				return AutomaticBeliefRevision(node, name,
						causingContradictionSet, currentContradiction,
						newContext);
			} else {

				System.out.println("Contradictiiooooooooooon!");
				return null;
			}
		}
	}

	/**
	 * adds a proposition to a certain context
	 * 
	 * @param proposition
	 *            is the proposition you want to assert to this context
	 * @param context
	 *            the context you want to add to it
	 */
	public static Context assertProposition(PropositionNode node,
			Context context) {
		Proposition proposition = (Proposition) node.getSemantic();
		if (context == currentContext) {
			return assertProposition(node);

		} else {
			Context newContext = new Context(context);
			newContext.addToContext(proposition);
			context.addContextToProp(newContext);
			(proposition).addContext(newContext);
			HashSet<PropositionSet> causingContradictionSet = new HashSet<PropositionSet>();
			Context c = CheckForContradiction(node, newContext);
			if (c != null) {
				contextSet.addContext(c);
				return c;
			} else {
				return null;
			}
		}

	}

	public static Context CheckForContradiction(PropositionNode node,
			Context newContext) {
		HashSet<PropositionSet> causingContradictionSet = new HashSet<PropositionSet>();
		node.CheckForNegation(newContext);
		// System.out.println(conradictingPropSet.propositions.size());
		fillMinimalNoGoods();
		causingContradictionSet = Contradiction(newContext);
		if (causingContradictionSet.isEmpty()) {
			return newContext;
		} else {
			// newContext.setConflicting(true);
			whichMethod = 0;
			if (automatic) {
				// Contradiction cont = new Contradiction(
				// (Proposition) node.getSemantic(), conradictingPropSet);
				return AutomaticBeliefRevision(node, newContext,
						causingContradictionSet, currentContradiction);
			} else {
				System.out.println("Contradictiiooooooooooon!");
				return null;
			}
		}
	}

	/**
	 * adds a proposition to a new context with no names
	 * 
	 * @param proposition
	 *            the proposition i want to add to the current context
	 */
	public static Context assertProposition(PropositionNode node) {
		return assertProposition(node, false, "");

	}

	private static Context assertProposition(PropositionNode node,
			boolean name, String contextName) {
		// HashSet<PropositionSet> causingContradictionSet = new
		// HashSet<PropositionSet>();
		Proposition proposition = (Proposition) node.getSemantic();
		Context context = new Context(currentContext);
		context.addToContext(proposition);
		currentContext.addContextToProp(context);
		// context.addPropToRestriction(proposition);
		// context = afterAssert(context);
		(proposition).addContext(context);
		if (name) {
			context.addName(contextName);
			currentContext.RemoveName(contextName);
			whichMethod = 3;
		}
		currentContext = context;
		Context c = CheckForContradiction(node, name, contextName,
				currentContext);
		if (c != null) {
			contextSet.addContext(c);
			return c;
		} else {
			return null;
		}
	}

	public static Context CheckForContradiction(PropositionNode node,
			boolean name, String contextName, Context context) {
		HashSet<PropositionSet> causingContradictionSet = new HashSet<PropositionSet>();
		node.CheckForNegation(currentContext);
		fillMinimalNoGoods();
		causingContradictionSet = Contradiction(context);
		if (causingContradictionSet.isEmpty()) {
			return currentContext;
		} else {
			// currentContext.setConflicting(true);
			if (name) {
				whichMethod = 3;
			} else {
				whichMethod = 2;
			}
			if (automatic) {

				return AutomaticBeliefRevision(node, name, contextName,
						causingContradictionSet, currentContradiction);
			} else {

				System.out.println("Contradictiiooooooooooon!");
				return null;
			}
		}
	}

	public static HashSet<PropositionSet> Contradiction(Context c) {
		HashSet<PropositionSet> result = new HashSet<PropositionSet>();
		for (Iterator<PropositionSet> iterator = minimalNoGoods.iterator(); iterator
				.hasNext();) {
			PropositionSet propSet = iterator.next();
			// System.out.println(c.hypothesisSet.propositions);
			if (c.hypothesisSet.propositions.containsAll(propSet.propositions)) {
				result.add(propSet);

			}
		}
		if (!result.isEmpty()) {
			c.setConflicting(true);
			c.addCont(currentContradiction);
		}
		return result;
	}

	public static ContextSet getContextSet() {
		return contextSet;
	}

	public static void setContextSet(ContextSet contextSet) {
		SNeBR.contextSet = contextSet;
	}

	// static Proposition getNewContradictingProp() {
	// return newContradictingProp;
	// }
	//
	// public static void setNewContradictingProp(Proposition
	// newContradictingProp) {
	// SNeBR.newContradictingProp = newContradictingProp;
	// }
	//
	// public static PropositionSet getOldContradictingPropSet() {
	// return oldContradictingPropSet;
	// }
	//
	// public static void setOldContradictingPropSet(
	// PropositionSet oldContradictingPropSet) {
	// SNeBR.oldContradictingPropSet = oldContradictingPropSet;
	// }

	// public static boolean canBeAdded(Proposition prop) {
	// boolean flag = false;
	// HashSet<Support> support = new HashSet<Support>();
	// support = prop.getOriginSupport();
	// for (Iterator<Support> iterator2 = support.iterator(); iterator2
	// .hasNext();) {
	// Support s1 = iterator2.next();
	// if (CheckIfSubset(s1))
	// flag = true;
	//
	// }
	// if (flag == true)
	// return true;
	// else
	// return false;
	// }
	//
	// public static boolean CheckIfSubset(Support s) {
	// for (Iterator<PropositionSet> iterator = minimalNoGoods.iterator();
	// iterator
	// .hasNext();) {
	// PropositionSet propSet = iterator.next();
	// if (propSet.getPropositions().containsAll(
	// s.getOriginSet().getHypothesisSet().getPropositions()))
	// return true;
	//
	// }
	// return false;
	// }

	public static void fillMinimalNoGoods() {

		HashSet<PropositionSet> combination1 = new HashSet<PropositionSet>();

		if (newSupportInAllContexts.isEmpty()
				|| oldSupportInAllContexts.isEmpty()) {
			return;
		}
		Set<Support> combination = new HashSet<Support>();
		combination = Support.combine(newSupportInAllContexts,
				oldSupportInAllContexts);
		for (Iterator<Support> iterator = combination.iterator(); iterator
				.hasNext();) {

			Support s = iterator.next();
			combination1.add(s.originSet.hypothesisSet);

		}

		UpdateMinimalNoGoods(combination1);
		newSupportInAllContexts.clear();
		oldSupportInAllContexts.clear();

	}

	public static void UpdateMinimalNoGoods(HashSet<PropositionSet> c) {
		int flag;

		for (Iterator<PropositionSet> iterator = c.iterator(); iterator
				.hasNext();) {
			flag = -1;
			Set<PropositionSet> temp = new HashSet<PropositionSet>();
			PropositionSet c1 = iterator.next();

			for (Iterator<PropositionSet> iterator1 = minimalNoGoods.iterator(); iterator1
					.hasNext();) {
				if (flag == 1) {
					PropositionSet s = iterator1.next();
				} else {
					PropositionSet s = iterator1.next();
					if (s.propositions.containsAll(c1.propositions)) {
						temp.add(s);
					} else if (c1.propositions.containsAll(s.propositions)) {
						flag = 1;
						// break;
					}
				}
			}
			if (flag != 1) {
				if (!temp.isEmpty()) {
					minimalNoGoods.removeAll(temp);
				}
				minimalNoGoods.add(c1);
			}
		}

	}

	/**
	 * this method is called after assertion to check if the resultant context
	 * already existed or not
	 * 
	 * @param context
	 *            the context i want to check for
	 * @return the chosen context whether it turns out to be an old or new one
	 */
	// public static Context afterAssert(Context context) {
	// // for (Iterator<Context> iterator = contextSet.iterator(); iterator
	// // .hasNext();) {
	// // Context temp = iterator.next();
	// // if (context.hypothesisSet.propositions
	// // .equals(temp.hypothesisSet.propositions)) {
	// // temp.unionRestriction(context);
	// // return temp;
	// // }
	// // }
	// contextSet.addContext(context);
	// return context;
	// }

	/**
	 * this method checks if there is already a context with the same
	 * proposition set as this one so it selects it , if not then it creates a
	 * new context
	 * 
	 * @param context
	 *            the new context i want to add
	 * @param name
	 *            the name of the context to be transfered from one to another
	 * @return the context chosen whether old or new one
	 */
	// must change like the other after removed
	public static Context afterRemoved(Context context, String name) {
		for (Iterator<Context> iterator = contextSet.iterator(); iterator
				.hasNext();) {
			Context cont = iterator.next();
			if (cont.hypothesisSet.propositions
					.equals(context.hypothesisSet.propositions)) {
				cont.addName(name);
				context.RemoveName(name);
				return cont;
			}

		}
		contextSet.addContext(context);
		return context;
	}

	/**
	 * this method checks if there is already a context with the same
	 * proposition set as this one so it selects it , if not then it creates a
	 * new context
	 * 
	 * @param context
	 *            the new context i want to add
	 * @return returns the context chosen whether it's an old or a new one
	 */
	// private static Context afterRemoved(Context context) {// n
	//
	// // for (Iterator<Context> iterator = contextSet.iterator(); iterator
	// // .hasNext();) {
	// // Context cont = iterator.next();
	// // if (cont.hypothesisSet.propositions
	// // .equals(context.hypothesisSet.propositions)) {
	// // context.unionRestriction(cont);
	// // contextSet.remove(cont);
	// // contextSet.add(context);
	// // return context;
	// // }
	// //
	// // }
	// contextSet.add(context);
	// return context;
	// }

	public static void ResolveContradiction(PropositionSet propSet,
			Context context, String name, Contradiction cont) {
		if (whichMethod == 0) {
			multipleRemoveProposition(propSet, context, cont);
			currentContradiction = null;
		} else {
			if (whichMethod == 1) {
				multipleRemoveProposition(propSet, name, cont, context);
				currentContradiction = null;
			} else {
				if (whichMethod == 2) {
					multipleRemoveProposition(propSet, false, "", cont);
					currentContradiction = null;
				} else {
					if (whichMethod == 3) {
						multipleRemoveProposition(propSet, true, name, cont);
						currentContradiction = null;
					}
				}
			}
		}
	}

	public static HashSet<PropositionSet> getMinimalNoGoods() {
		return minimalNoGoods;
	}

	public static void setMinimalNoGoods(HashSet<PropositionSet> minimalNoGood) {
		SNeBR.minimalNoGoods = minimalNoGood;
	}

	public static HashSet<Support> getNewSupportInAllContexts() {
		return newSupportInAllContexts;
	}

	public static void setNewSupportInAllContexts(
			HashSet<Support> newPropositionSupportInAllContexts) {
		SNeBR.newSupportInAllContexts = newPropositionSupportInAllContexts;
	}

	public static HashSet<Support> getOldSupportInAllContexts() {
		return oldSupportInAllContexts;
	}

	public static void setOldSupportInAllContexts(
			HashSet<Support> oldPropositionSupportInAllContexts) {
		SNeBR.oldSupportInAllContexts = oldPropositionSupportInAllContexts;
	}

	public static boolean isAutomatic() {
		return automatic;
	}

	public static void setAutomatic(boolean automatic) {
		SNeBR.automatic = automatic;
	}

	public static Context AutomaticBeliefRevision(PropositionNode node,
			boolean name, String contextName, HashSet<PropositionSet> propSet,
			Contradiction cont) {
		System.out.println("cont " + cont);
		PropositionSet propToBeRemoved = ReducingToSetCover(propSet);
		System.out.println("propTobermve " + propToBeRemoved);
		for (Iterator<Contradiction> iterator = currentContext
				.getContradictions().iterator(); iterator.hasNext();) {
			Contradiction cont1 = iterator.next();
			System.out.println("cont1 " + cont1);
		}
		System.out.println("el gedid " + currentContext.contradictions.size());
		System.out.println(currentContext.hypothesisSet.propositions);
		Context c = multipleRemoveProposition(propToBeRemoved, name,
				contextName, cont);
		System.out.println("el gedid " + c.contradictions.size());
		System.out.println(c.hypothesisSet.propositions);
		return c;

	}

	public static Context AutomaticBeliefRevision(PropositionNode node,
			String name, HashSet<PropositionSet> propSet, Contradiction cont,
			Context context) {

		// System.out.println(context.hypothesisSet.propositions);
		// System.out.println("cont " + cont);
		PropositionSet propToBeRemoved = ReducingToSetCover(propSet);

		// for (Iterator<Contradiction> iterator = context.getContradictions()
		// .iterator(); iterator.hasNext();) {
		// Contradiction cont1 = iterator.next();
		// System.out.println("cont1 " + cont1);
		// }
		System.out.println("Number of contradictions in Context 'a' is "
				+ context.contradictions.size());
		System.out
				.println("Context 'a' contains now these two propositions: 'Penguins can fly'");
		System.out.println(" and 'Penguins cannot fly' with IDs: ");
		for (Iterator<Proposition> iterator = context.hypothesisSet.propositions
				.iterator(); iterator.hasNext();) {

			Proposition q = iterator.next();
			System.out.println(" " + q);
		}

		System.out.println();
		System.out.println("Solving this contradiction automatically!");
		System.out.println("Proposition to be removed is Penguins can fly");
		System.out.println(" with ID: " + propToBeRemoved);
		Context c = multipleRemoveProposition(propToBeRemoved, name, cont,
				context);
		System.out
				.println("Number of contradictions in Context 'a' after removing Penguins can fly! is "
						+ c.contradictions.size());
		System.out
				.println("Context 'a' contains now the proposition 'Penguins cannot fly'");
		System.out.println(" with ID: " + c.hypothesisSet.propositions);

		return c;
	}

	public static Context AutomaticBeliefRevision(PropositionNode node,
			Context context, HashSet<PropositionSet> propSet, Contradiction cont) {

		System.out.println(context.hypothesisSet.propositions);
		System.out.println("cont " + cont);
		PropositionSet propToBeRemoved = ReducingToSetCover(propSet);
		System.out.println("propTobermve " + propToBeRemoved);
		for (Iterator<Contradiction> iterator = context.getContradictions()
				.iterator(); iterator.hasNext();) {
			Contradiction cont1 = iterator.next();
			System.out.println("cont1 " + cont1);
		}
		System.out.println("el gedid " + context.contradictions.size());
		System.out.println(context.hypothesisSet.propositions);
		Context c = multipleRemoveProposition(propToBeRemoved, context, cont);
		System.out.println("el gedid " + c.contradictions.size());
		System.out.println(c.hypothesisSet.propositions);
		return c;

	}

	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	public static void main(String[] args) {
		// HashSet<PropositionSet> a = new HashSet<PropositionSet>();
		// HashSet<PropositionSet> b = new HashSet<PropositionSet>();
		// HashSet<PropositionSet> c = new HashSet<PropositionSet>();
		// for (int j = 0; j < 2; j++) {
		// PropositionSet propSet = new PropositionSet();
		// for (int i = 0; i < 2; i++) {
		// // Proposition p = new Proposition();
		// // propSet.addProposition(p);
		// //
		// // }
		// //
		// // a.add(propSet);
		// //
		// // }
		// //
		// // for (int j = 0; j < 2; j++) {
		// // PropositionSet propSet = new PropositionSet();
		// // for (int i = 0; i < 3; i++) {
		// // Proposition p = new Proposition();
		// // propSet.addProposition(p);
		// //
		// // }
		// //
		// // b.add(propSet);
		// //
		// // }
		// // //c = Combination(a, b);
		// // //System.out.println(c.size());
		//
		// HashSet s1 = new HashSet<Support>();
		// HashSet s2 = new HashSet<Support>();
		// HashSet s3 = new HashSet<Support>();
		// for (int j = 0; j < 3; j++) {
		// PropositionSet propSet = new PropositionSet();
		// for (int i = 0; i < 2; i++) {
		// Proposition p = new Proposition();
		// propSet.addProposition(p);
		//
		// }
		//
		// Context context = new Context(propSet);
		// Support support = new Support(context);
		// s1.add(support);
		// }
		//
		// for (int j = 0; j < 3; j++) {
		// PropositionSet propSet = new PropositionSet();
		// for (int i = 0; i < 2; i++) {
		// Proposition p = new Proposition();
		// propSet.addProposition(p);
		//
		// }
		//
		// Context context = new Context(propSet);
		// Support support = new Support(context);
		// s2.add(support);
		// }
		//
		// newSupportInAllContexts = s1;
		// oldSupportInAllContexts = s2;
		// // %all the comments are testing%
		// // System.out.println(newSupportInAllContexts);
		// // System.out.println("helloooooooooooooooow----------------------");
		// // System.out.println(oldSupportInAllContexts);
		//
		// fillMinimalNoGoodSet();
		// System.out.println(minimalNoGoods);
		HashSet<Integer> intTest = new HashSet<Integer>();
		intTest.add(1);
		intTest.add(2);
		intTest.add(3);
		intTest.add(4);
		intTest.add(5);

		HashSet<Integer> intTest1 = new HashSet<Integer>();
		intTest1.add(1);
		intTest1.add(2);
		intTest1.add(3);

		HashSet<Integer> intTest4 = new HashSet<Integer>();
		intTest4.add(4);
		intTest4.add(5);

		HashSet<Integer> intTest2 = new HashSet<Integer>();

		intTest2.add(2);
		intTest2.add(4);

		HashSet<Integer> intTest0 = new HashSet<Integer>();
		intTest0.add(1);
		intTest0.add(2);
		intTest0.add(3);
		intTest0.add(4);
		intTest0.add(5);

		HashSet<Integer> intTest3 = new HashSet<Integer>();
		intTest3.add(3);
		intTest3.add(4);

		HashSet<HashSet<Integer>> f = new HashSet<HashSet<Integer>>();
		f.add(intTest1);
		f.add(intTest4);
		f.add(intTest3);
		f.add(intTest2);
		// f.add(intTest0);

		// System.out.println("X = " + intTest);
		System.out.println("F = " + f);

		// System.out.println(intTest.retainAll(intTest1));
		// System.out.println(intTest);
		// HashSet<HashSet<Integer>> result = Test1(intTest, f);
		// System.out.println(result.size());
		// HashSet<Integer> result = Test2(f);
		// System.out.println(result);
	}

	public static PropositionSet ReducingToSetCover(HashSet<PropositionSet> prop) {
		HashSet<HashSet<HashSet<Integer>>> collectionOfSubsets = new HashSet<HashSet<HashSet<Integer>>>();
		PropositionSet union = new PropositionSet();
		HashSet<Integer> union1 = new HashSet<Integer>();
		PropositionSet result = new PropositionSet();

		Hashtable<Integer, Proposition> temp = new Hashtable<Integer, Proposition>();
		Hashtable<Proposition, Integer> temp1 = new Hashtable<Proposition, Integer>();
		// getting union of propositions
		for (Iterator<PropositionSet> iterator = prop.iterator(); iterator
				.hasNext();) {

			PropositionSet s = iterator.next();
			for (Iterator<Proposition> iterator1 = s.iterator(); iterator1
					.hasNext();) {
				Proposition c = iterator1.next();
				union.propositions.add(c);
			}

		}

		// mapping propositions to integers in both ways
		int i = 1;
		for (Iterator<Proposition> iterator1 = union.iterator(); iterator1
				.hasNext();) {
			Proposition c = iterator1.next();

			temp.put(i, c);
			temp1.put(c, i);
			union1.add(i);

			i++;
		}

		// getting the new prop parameter with integers
		HashSet<HashSet<Integer>> integerHashSet = new HashSet<HashSet<Integer>>();
		HashSet<Integer> element1 = new HashSet<Integer>();
		for (Iterator<PropositionSet> iterator2 = prop.iterator(); iterator2
				.hasNext();) {

			PropositionSet q = iterator2.next();
			for (Iterator<Proposition> iterator3 = q.iterator(); iterator3
					.hasNext();) {
				Proposition q1 = iterator3.next();
				element1.add(temp1.get(q1));

			}
			integerHashSet.add(element1);
		}
		Hashtable<HashSet<HashSet<Integer>>, Integer> mapping = new Hashtable<HashSet<HashSet<Integer>>, Integer>();

		for (Iterator<Integer> iterator2 = union1.iterator(); iterator2
				.hasNext();) {
			Integer c = iterator2.next();
			HashSet<HashSet<Integer>> element = new HashSet<HashSet<Integer>>();
			for (Iterator<HashSet<Integer>> iterator3 = integerHashSet
					.iterator(); iterator3.hasNext();) {
				HashSet<Integer> q = iterator3.next();
				if (q.contains(c)) {
					element.add(q);

				}
			}
			mapping.put(element, c);
			collectionOfSubsets.add(element);

		}

		HashSet<HashSet<HashSet<Integer>>> partialResult = ResolveSetCover(
				integerHashSet, collectionOfSubsets);// TAMAM
		for (Iterator<HashSet<HashSet<Integer>>> iterator = partialResult
				.iterator(); iterator.hasNext();) {

			HashSet<HashSet<Integer>> q = iterator.next();
			Integer p1 = mapping.get(q);
			Proposition p = temp.get(p1);
			result.propositions.add(p);
		}

		return result;
	}

	// public static HashSet<Integer> Test2(HashSet<HashSet<Integer>> prop) {
	// HashSet<HashSet<HashSet<Integer>>> collectionOfSubsets = new
	// HashSet<HashSet<HashSet<Integer>>>();
	// HashSet<Integer> union = new HashSet<Integer>();
	// HashSet<Integer> result = new HashSet<Integer>();
	// // getting union to get the universe
	// for (Iterator<HashSet<Integer>> iterator = prop.iterator(); iterator
	// .hasNext();) {
	//
	// HashSet<Integer> s = iterator.next();
	// for (Iterator<Integer> iterator1 = s.iterator(); iterator1
	// .hasNext();) {
	// Integer c = iterator1.next();
	// union.add(c);
	// }
	//
	// }
	//
	// Hashtable<HashSet<HashSet<Integer>>, Integer> mapping = new
	// Hashtable<HashSet<HashSet<Integer>>, Integer>();
	//
	// for (Iterator<Integer> iterator2 = union.iterator(); iterator2
	// .hasNext();) {
	// Integer c = iterator2.next();
	// HashSet<HashSet<Integer>> element = new HashSet<HashSet<Integer>>();
	// for (Iterator<HashSet<Integer>> iterator3 = prop.iterator(); iterator3
	// .hasNext();) {
	// HashSet<Integer> q = iterator3.next();
	// if (q.contains(c)) {
	// element.add(q);
	//
	// }
	// }
	// mapping.put(element, c);
	// collectionOfSubsets.add(element);
	//
	// }
	//
	// HashSet<HashSet<HashSet<Integer>>> partialResult = ResolveSetCover(
	// prop, collectionOfSubsets);// TAMAM
	// for (Iterator<HashSet<HashSet<Integer>>> iterator = partialResult
	// .iterator(); iterator.hasNext();) {
	//
	// HashSet<HashSet<Integer>> q = iterator.next();
	// Integer p1 = mapping.get(q);
	// result.add(p1);
	// }
	// System.out.println(result);
	// return result;
	// }

	public static HashSet<HashSet<HashSet<Integer>>> ResolveSetCover(
			HashSet<HashSet<Integer>> universe,
			HashSet<HashSet<HashSet<Integer>>> f) {

		HashSet<HashSet<HashSet<Integer>>> c = new HashSet<HashSet<HashSet<Integer>>>();
		HashSet<HashSet<Integer>> w = new HashSet<HashSet<Integer>>();
		HashSet<HashSet<Integer>> w1 = new HashSet<HashSet<Integer>>();

		w = universe;
		int max = -1;
		while (!w.isEmpty()) {
			max = -1;
			Hashtable<Integer, HashSet<HashSet<Integer>>> hash = new Hashtable<Integer, HashSet<HashSet<Integer>>>();
			f.removeAll(c);
			for (Iterator<HashSet<HashSet<Integer>>> iterator = f.iterator(); iterator
					.hasNext();) {
				w1 = (HashSet<HashSet<Integer>>) w.clone();

				HashSet<HashSet<Integer>> s = iterator.next();
				w1.retainAll(s);
				int max1 = w1.size();
				if (max1 > max) {
					max = max1;

				}
				hash.put(max1, s);
				// System.out.println("hena");
			}

			HashSet<HashSet<Integer>> element = hash.get(max);
			w.removeAll(element);
			c.add(element);

		}
		return c;
	}

	public static boolean isName() {
		return name;
	}

	public static void setName(boolean name) {
		SNeBR.name = name;
	}

	public static int getWhichMethod() {
		return whichMethod;
	}

	public static void setWhichMethod(int whichMethod) {
		SNeBR.whichMethod = whichMethod;
	}
}
