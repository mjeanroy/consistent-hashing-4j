/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2021 Mickael Jeanroy
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.mjeanroy.consistenthashing4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

final class RingNode {

	static RingNode of(Node node, Collection<VirtualNode> virtualNodes) {
		return new RingNode(node, virtualNodes);
	}

	static RingNode of(Node node) {
		return new RingNode(node, Collections.emptyList());
	}

	private final Node node;
	private final List<VirtualNode> virtualNodes;

	private RingNode(Node node, Collection<VirtualNode> virtualNodes) {
		this.node = PreConditions.notNull(node, "Node must be defined");
		this.virtualNodes = Collections.unmodifiableList(new ArrayList<>(
				PreConditions.notNull(virtualNodes, "Virtual nodes must be defined")
		));
	}

	Node getNode() {
		return node;
	}

	Collection<VirtualNode> getVirtualNodes() {
		return virtualNodes;
	}

	Node rootNode() {
		Node rootNode = node.getRootNode();
		return rootNode != null ? rootNode : node;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (o instanceof RingNode) {
			RingNode n = (RingNode) o;
			return Objects.equals(node, n.node) && Objects.equals(virtualNodes, n.virtualNodes);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(node, virtualNodes);
	}

	@Override
	public String toString() {
		return ToStringBuilder.of(getClass())
				.append("node", node)
				.append("virtualNodes", virtualNodes)
				.build();
	}
}
