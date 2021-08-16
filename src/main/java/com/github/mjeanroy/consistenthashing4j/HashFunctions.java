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

/**
 * Base Hash Functions.
 */
public final class HashFunctions {

	private static final HashFunction JDK_HASH_FUNCTION = new JdkHashFunction();
	private static final HashFunction FNV1_32_HASH_FUNCTION = new Fnv132HashFunction();

	private HashFunctions() {
	}

	/**
	 * Hash functions using native {@link java.util.Objects#hashCode} method.
	 *
	 * @return Hash Function.
	 */
	public static HashFunction jdkHashFunction() {
		return JDK_HASH_FUNCTION;
	}

	/**
	 * Hash functions using <a href="https://en.wikipedia.org/wiki/Fowler%E2%80%93Noll%E2%80%93Vo_hash_function">FNV1_32 algorithm</a>.
	 *
	 * @return Hash Function.
	 */
	public static HashFunction fnv132HashFunction() {
		return FNV1_32_HASH_FUNCTION;
	}

	private static final class JdkHashFunction implements HashFunction {
		@Override
		public int compute(String value) {
			return value.hashCode();
		}
	}

	private static final class Fnv132HashFunction implements HashFunction {
		@Override
		public int compute(String value) {
			final int p = 16777619;

			int hash = (int) 2166136261L;
			for (int i = 0; i < value.length(); i++) {
				hash = (hash ^ value.charAt(i)) * p;
			}

			hash += hash << 13;
			hash ^= hash >> 7;
			hash += hash << 3;
			hash ^= hash >> 17;
			hash += hash << 5;

			return hash;
		}
	}
}
