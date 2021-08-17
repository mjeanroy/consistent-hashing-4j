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

import java.util.Objects;

/**
 * Ring configuration.
 */
public final class RingConfiguration {

	/**
	 * Get new {@link RingConfiguration} builder.
	 *
	 * @return New builder.
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Get default configuration.
	 *
	 * @return Default configuration.
	 */
	public static RingConfiguration defaultConfiguration() {
		return builder().build();
	}

	/**
	 * The hash function to use.
	 */
	private final HashFunction hashFunction;

	private final int nbVirtualNodes;

	private RingConfiguration(HashFunction hashFunction, int nbVirtualNodes) {
		this.hashFunction = PreConditions.notNull(hashFunction, "Hash function must be defined");
		this.nbVirtualNodes = PreConditions.isPositive(nbVirtualNodes, "Number of virtual node must be positive");
	}

	/**
	 * Get {@link #hashFunction}
	 *
	 * @return {@link #hashFunction}
	 */
	public HashFunction getHashFunction() {
		return hashFunction;
	}

	/**
	 * Get {@link #nbVirtualNodes}
	 *
	 * @return {@link #nbVirtualNodes}
	 */
	public int getNbVirtualNodes() {
		return nbVirtualNodes;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (o instanceof RingConfiguration) {
			RingConfiguration rc = (RingConfiguration) o;
			return Objects.equals(hashFunction, rc.hashFunction) && Objects.equals(nbVirtualNodes, rc.nbVirtualNodes);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(hashFunction, nbVirtualNodes);
	}

	/**
	 * Builder for {@link RingConfiguration}
	 */
	public static final class Builder {
		/**
		 * Hash function that will be used to compute hash for each node in the ring.
		 */
		private HashFunction hashFunction;

		/**
		 * Number of virtual nodes to add for each parent node on the ring.
		 */
		private int nbVirtualNodes;

		private Builder() {
			this.hashFunction = HashFunctions.fnv132HashFunction();
			this.nbVirtualNodes = 0;
		}

		/**
		 * Update hash function.
		 *
		 * @param hashFunction New hash function.
		 * @return The builder.
		 */
		public Builder hashFunction(HashFunction hashFunction) {
			this.hashFunction = hashFunction;
			return this;
		}

		/**
		 * Update number of virtual nodes to manage for each parent node.
		 *
		 * @param nbVirtualNodes Number of virtual nodes.
		 * @return The builder.
		 */
		public Builder nbVirtualNodes(int nbVirtualNodes) {
			this.nbVirtualNodes = nbVirtualNodes;
			return this;
		}

		/**
		 * Build configuration.
		 *
		 * @return Configuration.
		 * @throws NullPointerException If {@link #hashFunction} is {@code null}
		 * @throws IllegalArgumentException If {@link #nbVirtualNodes} is strictly less than zero
		 */
		public RingConfiguration build() {
			return new RingConfiguration(
					hashFunction,
					nbVirtualNodes
			);
		}
	}
}
