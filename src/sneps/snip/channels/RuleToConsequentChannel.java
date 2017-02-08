package sneps.snip.channels;

import sneps.network.nodes.Node;
import sneps.snip.matching.Substitutions;

public class RuleToConsequentChannel extends Channel {

	public RuleToConsequentChannel(Substitutions switchSubstitution, Substitutions filterSubstitutions, int contextID,
			Node requester, Node reporter, boolean v) {
		super(switchSubstitution, filterSubstitutions, contextID, requester, reporter, v);
	}

}
