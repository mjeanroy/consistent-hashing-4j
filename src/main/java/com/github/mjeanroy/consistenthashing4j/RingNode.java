package com.github.mjeanroy.consistenthashing4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class RingNode {

	static RingNode of(Node parentNode) {
		return new RingNode(parentNode, Collections.emptyList());
	}

	static RingNode of(Node parentNode, List<VirtualNode> virtualNodes) {
		return new RingNode(parentNode, virtualNodes);
	}

	private final Node node;
	private final List<VirtualNode> virtualNodes;

	private RingNode(Node parentNode, List<VirtualNode> virtualNodes) {
		this.node = parentNode;
		this.virtualNodes = new ArrayList<>(virtualNodes);
	}

	Node getNode() {
		return node;
	}

	int getLastIndex() {
		if (virtualNodes.isEmpty()) {
			return 0;
		}

		return virtualNodes.get(virtualNodes.size() - 1).getIdx();
	}
}
