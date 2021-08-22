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
import java.util.SortedMap;
import java.util.TreeMap;

import static java.util.Collections.unmodifiableList;

/**
 * Implementation of cluster.
 *
 * This class is not thread safe, adding node should be synchronized if needed!
 */
public final class Cluster {

	/**
	 * Create new cluster with given nodes, each node being placed on a ring based on the given hash function.
	 *
	 * @param configuration The cluster configuration.
	 * @param nodes The nodes.
	 * @return The cluster.
	 */
	public static Cluster of(ClusterConfiguration configuration, Collection<Node> nodes) {
		Cluster cluster = new Cluster(configuration);
		for (Node node: nodes) {
			cluster.addNode(node);
		}

		return cluster;
	}

	/**
	 * Create new cluster with given nodes, each node being placed on a ring based on a default hash function.
	 *
	 * @param nodes The nodes.
	 * @return The cluster.
	 */
	public static Cluster of(Collection<Node> nodes) {
		return Cluster.of(ClusterConfiguration.defaultConfiguration(), nodes);
	}

	/**
	 * Create new empty cluster with given hash function.
	 *
	 * @param configuration Cluster configuration.
	 * @return The cluster.
	 * @throws NullPointerException If {@code configuration} is {@code null}
	 */
	public static Cluster of(ClusterConfiguration configuration) {
		return Cluster.of(configuration, Collections.emptyList());
	}

	/**
	 * Create new empty cluster.
	 *
	 * @return The cluster.
	 */
	public static Cluster emptyCluster() {
		return Cluster.of(ClusterConfiguration.defaultConfiguration(), Collections.emptyList());
	}

	/**
	 * The ring.
	 */
	private final SortedMap<Integer, ClusterNode> ring;

	/**
	 * Cluster configuration.
	 */
	private final ClusterConfiguration configuration;

	private Cluster(ClusterConfiguration configuration) {
		this.ring = new TreeMap<>();
		this.configuration = PreConditions.notNull(configuration, "Cluster configuration must be defined");
	}

	/**
	 * Get cluster size.
	 *
	 * @return Cluster size.
	 */
	public int size() {
		return ring.size();
	}

	/**
	 * Check if given cluster is empty.
	 *
	 * @return {@code true} if cluster is empty, {@code false} otherwise.
	 */
	public boolean isEmpty() {
		return ring.isEmpty();
	}

	/**
	 * Add new node into the cluster.
	 *
	 * @param name Node name.
	 */
	public void addNode(String name) {
		addNode(Nodes.of(name));
	}

	/**
	 * Add new node into the cluster.
	 *
	 * @param name Node name.
	 */
	public void addNode(String name, int nbVirtualNodes) {
		addNode(Nodes.of(name), nbVirtualNodes);
	}

	/**
	 * Add new node into the cluster.
	 *
	 * @param node New node to add.
	 */
	public void addNode(Node node) {
		internalAddNode(node, configuration.getNbVirtualNodes());
	}

	/**
	 * Add new node into the cluster.
	 *
	 * @param node New node to add.
	 */
	public void addNode(Node node, int nbVirtualNodes) {
		PreConditions.isPositive(nbVirtualNodes, "Number of virtual nodes must be positive");
		internalAddNode(node, nbVirtualNodes);
	}

	/**
	 *
	 * Remove given node from the cluster.
	 *
	 * @param node Node to remove.
	 */
	public void removeNode(Node node) {
		ClusterNode removedNode = ring.remove(computeHash(node));

		if (removedNode != null) {
			for (VirtualNode virtualNode : removedNode.getVirtualNodes()) {
				ring.remove(computeHash(virtualNode));
			}
		}
	}

	/**
	 *
	 * Remove given node name from the cluster.
	 *
	 * @param name Node name to remove.
	 */
	public void removeNode(String name) {
		removeNode(Nodes.of(name));
	}


	/**
	 * Add new node into the cluster.
	 *
	 * @param node New node to add.
	 * @param nbVirtualNodes The number of virtual nodes to create.
	 */
	private void internalAddNode(Node node, int nbVirtualNodes) {
		PreConditions.notNull(node, "Node cannot be null");

		int hash = computeHash(node);
		if (ring.containsKey(hash)) {
			throw new IllegalArgumentException("Node with hash <" + hash + "> already exists!");
		}

		List<VirtualNode> virtualNodes = createVirtualNodes(node, nbVirtualNodes);

		// Add main node on the ring.
		this.ring.put(hash, ClusterNode.of(node, virtualNodes));

		// Add virtual node on the ring.
		if (virtualNodes.size() > 0) {
			for (VirtualNode virtualNode : virtualNodes) {
				this.ring.put(computeHash(virtualNode), ClusterNode.of(virtualNode));
			}
		}
	}

	/**
	 * Find appropriate node based on given value.
	 *
	 * @param value The value.
	 * @return The node.
	 */
	public Node findNode(String value) {
		PreConditions.notEmpty(ring, "Cannot find node from an empty cluster");
		PreConditions.notNull(value, "Cannot find node for null value");

		int hash = computeHash(value);
		if (ring.containsKey(hash)) {
			return ring.get(hash).rootNode();
		}

		// Get next node on the ring.
		SortedMap<Integer, ClusterNode> tailMap = ring.tailMap(hash);
		if (tailMap.isEmpty()) {
			// If we don't have a "next" node, get the first one.
			tailMap = ring;
		}

		return tailMap.get(tailMap.firstKey()).rootNode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (o instanceof Cluster) {
			Cluster r = (Cluster) o;
			return Objects.equals(ring, r.ring) && Objects.equals(configuration, r.configuration);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(ring, configuration);
	}

	@Override
	public String toString() {
		return ToStringBuilder.of(getClass())
				.append("ring", ring)
				.append("configuration", configuration)
				.build();
	}

	private int computeHash(String value) {
		return Math.abs(hashFunction().compute(value));
	}

	private int computeHash(Node node) {
		return computeHash(node.getName());
	}

	private List<VirtualNode> createVirtualNodes(Node parentNode, int nbVirtualNodes) {
		if (nbVirtualNodes <= 0) {
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
}
