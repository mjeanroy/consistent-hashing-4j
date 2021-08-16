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

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Implementation of ring.
 *
 * This class is not thread safe, adding node should be synchronized if needed!
 */
public final class Ring {

	/**
	 * Create new ring with given nodes, each node being placed on the ring based on the given hash function.
	 *
	 * @param hashFunction The hash function.
	 * @param nodes The nodes.
	 * @return The ring.
	 */
	public static Ring newRing(HashFunction hashFunction, List<Node> nodes) {
		PreConditions.notNull(hashFunction, "Cannot create ring without hash function");

		Ring ring = new Ring(hashFunction);
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
	public static Ring newRing(List<Node> nodes) {
		return newRing(HashFunctions.fnv132HashFunction(), nodes);
	}

	/**
	 * List of nodes in the ring.
	 */
	private final SortedMap<Integer, Node> nodes;

	/**
	 * Hash function.
	 */
	private final HashFunction hashFunction;

	private Ring(HashFunction hashFunction) {
		this.nodes = new TreeMap<>();
		this.hashFunction = PreConditions.notNull(hashFunction, "Hash function must be defined");
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

		int hash = computeHash(node.getName());
		if (nodes.containsKey(hash)) {
			throw new IllegalArgumentException("Node with hash <" + hash + "> already exists!");
		}

		this.nodes.put(hash, node);
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
		SortedMap<Integer, Node> tailMap = nodes.tailMap(hash);
		if (tailMap.isEmpty()) {
			tailMap = nodes;
		}

		return tailMap.get(tailMap.firstKey());
	}

	private int computeHash(String value) {
		return Math.abs(hashFunction.compute(value));
	}
}
