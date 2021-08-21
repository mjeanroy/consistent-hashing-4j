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
import java.util.SortedMap;
import java.util.TreeMap;

import static java.util.Collections.unmodifiableList;

/**
 * Implementation of ring.
 *
 * This class is not thread safe, adding node should be synchronized if needed!
 */
public final class Ring {

	/**
	 * Create new ring with given nodes, each node being placed on the ring based on the given hash function.
	 *
	 * @param configuration The ring configuration.
	 * @param nodes The nodes.
	 * @return The ring.
	 */
	public static Ring of(RingConfiguration configuration, Collection<Node> nodes) {
		Ring ring = new Ring(configuration);
		for (Node node: nodes) {
			ring.addNode(node);
		}

		return ring;
	}

	/**
	 * Create new ring with given nodes, each node being placed on the ring based on a default hash function.
	 *
	 * @param nodes The nodes.
	 * @return The ring.
	 */
	public static Ring of(Collection<Node> nodes) {
		return Ring.of(RingConfiguration.defaultConfiguration(), nodes);
	}

	/**
	 * Create new empty ring with given hash function.
	 *
	 * @param configuration Ring configuration.
	 * @return The ring.
	 * @throws NullPointerException If {@code configuration} is {@code null}
	 */
	public static Ring of(RingConfiguration configuration) {
		return Ring.of(configuration, Collections.emptyList());
	}

	/**
	 * Create new empty ring.
	 *
	 * @return The ring.
	 */
	public static Ring of() {
		return Ring.of(RingConfiguration.defaultConfiguration(), Collections.emptyList());
	}

	/**
	 * List of nodes in the ring.
	 */
	private final SortedMap<Integer, Node> nodes;

	/**
	 * Ring configuration.
	 */
	private final RingConfiguration configuration;

	private Ring(RingConfiguration configuration) {
		this.nodes = new TreeMap<>();
		this.configuration = PreConditions.notNull(configuration, "Ring configuration must be defined");
	}

	/**
	 * Get ring size.
	 *
	 * @return Ring size.
	 */
	public int size() {
		return nodes.size();
	}

	/**
	 * Check if given ring is empty.
	 *
	 * @return {@code true} if ring is empty, {@code false} otherwise.
	 */
	public boolean isEmpty() {
		return nodes.isEmpty();
	}

	/**
	 * Add new node into the ring.
	 *
	 * @param node New node to add.
	 */
	public void addNode(Node node) {
		PreConditions.notNull(node, "Node cannot be null");

		int hash = computeHash(node);
		if (nodes.containsKey(hash)) {
			throw new IllegalArgumentException("Node with hash <" + hash + "> already exists!");
		}

		List<VirtualNode> virtualNodes = createVirtualNodes(node);

		// Add main node on the ring.
		this.nodes.put(hash, node);

		// Add virtual node on the ring.
		for (VirtualNode virtualNode : virtualNodes) {
			this.nodes.put(computeHash(virtualNode), virtualNode);
		}
	}

	/**
	 * Add new node into the ring.
	 *
	 * @param name Node name.
	 */
	public void addNode(String name) {
		addNode(Nodes.of(name));
	}

	/**
	 *
	 * Remove given node from the ring.
	 *
	 * @param node Node to remove.
	 */
	public void removeNode(Node node) {
		nodes.remove(computeHash(node));
	}

	/**
	 *
	 * Remove given node name from the ring.
	 *
	 * @param name Node name to remove.
	 */
	public void removeNode(String name) {
		removeNode(Nodes.of(name));
	}

	/**
	 * Find appropriate node based on given value.
	 *
	 * @param value The value.
	 * @return The node.
	 */
	public Node findNode(String value) {
		PreConditions.notEmpty(nodes, "Cannot find node from an empty ring");
		PreConditions.notNull(value, "Cannot find node for null value");

		int hash = computeHash(value);
		if (nodes.containsKey(hash)) {
			return getMainNode(nodes.get(hash));
		}

		// Get next node on the ring.
		SortedMap<Integer, Node> tailMap = nodes.tailMap(hash);
		if (tailMap.isEmpty()) {
			// If we don't have a "next" node, get the first one.
			tailMap = nodes;
		}

		Node matchedNode = tailMap.get(tailMap.firstKey());
		return getMainNode(matchedNode);
	}

	private Node getMainNode(Node node) {
		return node instanceof VirtualNode ? ((VirtualNode) node).getParentNode() : node;
	}

	private int computeHash(String value) {
		// To keep it simple, force positive values.
		return Math.abs(hashFunction().compute(value));
	}

	private int computeHash(Node node) {
		return computeHash(node.getName());
	}

	private List<VirtualNode> createVirtualNodes(Node parentNode) {
		int nbVirtualNodes = nbVirtualNodes();
		if (nbVirtualNodes == 0) {
			return Collections.emptyList();
		}

		List<VirtualNode> virtualNodes = new ArrayList<>(nbVirtualNodes);
		for (int i = 0; i < nbVirtualNodes; ++i) {
			VirtualNode virtualNode = new VirtualNode(parentNode);
			virtualNodes.add(virtualNode);
		}

		return unmodifiableList(virtualNodes);
	}

	private HashFunction hashFunction() {
		return configuration.getHashFunction();
	}

	private int nbVirtualNodes() {
		return configuration.getNbVirtualNodes();
	}
}
