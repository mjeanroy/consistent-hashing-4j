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

	private RingConfiguration(HashFunction hashFunction) {
		this.hashFunction = PreConditions.notNull(hashFunction, "Hash function must be defined");
	}

	/**
	 * Get {@link #hashFunction}
	 *
	 * @return {@link #hashFunction}
	 */
	public HashFunction getHashFunction() {
		return hashFunction;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (o instanceof RingConfiguration) {
			RingConfiguration rc = (RingConfiguration) o;
			return Objects.equals(hashFunction, rc.hashFunction);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(hashFunction);
	}

	/**
	 * Builder for {@link RingConfiguration}
	 */
	public static final class Builder {
		/**
		 * Hash function that will be used to compute hash for each node in the ring.
		 */
		private HashFunction hashFunction;

		private Builder() {
			this.hashFunction = HashFunctions.fnv132HashFunction();
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
		 * Build configuration.
		 *
		 * @return Configuration.
		 * @throws NullPointerException If {@link #hashFunction} is {@code null}
		 */
		public RingConfiguration build() {
			return new RingConfiguration(hashFunction);
		}
	}
}
