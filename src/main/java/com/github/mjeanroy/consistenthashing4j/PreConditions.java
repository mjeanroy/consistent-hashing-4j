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

import java.util.Collection;
import java.util.Map;

/**
 * Static PreConditions utilities.
 */
final class PreConditions {

	private PreConditions() {
	}

	/**
	 * Check that given value is not null or throw {@link NullPointerException} otherwise.
	 *
	 * @param value Value to check.
	 * @param message Error message.
	 * @param <T> Value type.
	 * @return Given value, if it is not {@code null}
	 * @throws NullPointerException If {@code value} is {@code null}
	 */
	static <T> T notNull(T value, String message) {
		if (value == null) {
			throw new NullPointerException(message);
		}

		return value;
	}

	/**
	 * Check that given value is:
	 *
	 * <ul>
	 *   <li>Not null or throw {@link NullPointerException} otherwise.</li>
	 *   <li>Not empty or throw {@link IllegalArgumentException} otherwise.</li>
	 * </ul>
	 *
	 * @param value Value to check.
	 * @param message Error message.
	 * @return Given value, if it is not {@code null}
	 * @throws NullPointerException If {@code value} is {@code null}
	 * @throws IllegalArgumentException If {@code value} is empty
	 */
	static String notEmpty(String value, String message) {
		notNull(value, message);

		if (value.isEmpty()) {
			throw new IllegalArgumentException(message);
		}

		return value;
	}

	/**
	 * Check that given value is:
	 *
	 * <ul>
	 *   <li>Not null or throw {@link NullPointerException} otherwise.</li>
	 *   <li>Not empty or throw {@link IllegalArgumentException} otherwise.</li>
	 * </ul>
	 *
	 * @param value Value to check.
	 * @param message Error message.
	 * @return Given value, if it is not {@code null}
	 * @throws NullPointerException If {@code value} is {@code null}
	 * @throws IllegalArgumentException If {@code value} is empty
	 */
	static <T> Collection<T> notEmpty(Collection<T> value, String message) {
		notNull(value, message);

		if (value.isEmpty()) {
			throw new IllegalArgumentException(message);
		}

		return value;
	}

	/**
	 * Check that given value is:
	 *
	 * <ul>
	 *   <li>Not null or throw {@link NullPointerException} otherwise.</li>
	 *   <li>Not empty or throw {@link IllegalArgumentException} otherwise.</li>
	 * </ul>
	 *
	 * @param value Value to check.
	 * @param message Error message.
	 * @return Given value, if it is not {@code null}
	 * @throws NullPointerException If {@code value} is {@code null}
	 * @throws IllegalArgumentException If {@code value} is empty
	 */
	static <T, U> Map<T, U> notEmpty(Map<T, U> value, String message) {
		notNull(value, message);

		if (value.isEmpty()) {
			throw new IllegalArgumentException(message);
		}

		return value;
	}

	/**
	 * Check that given value is:
	 *
	 * <ul>
	 *   <li>Not null or throw {@link NullPointerException} otherwise.</li>
	 *   <li>Not empty or throw {@link IllegalArgumentException} otherwise.</li>
	 *   <li>Not blank or throw {@link IllegalArgumentException} otherwise.</li>
	 * </ul>
	 *
	 * @param value Value to check.
	 * @param message Error message.
	 * @return Given value, if it is not {@code null}
	 * @throws NullPointerException If {@code value} is {@code null}
	 * @throws IllegalArgumentException If {@code value} is empty
	 */
	static String notBlank(String value, String message) {
		notEmpty(value, message);
		if (Strings.isBlank(value)) {
			throw new IllegalArgumentException(message);
		}

		return value;
	}

	/**
	 * Check that given value is positive (or equal to zero).
	 *
	 * @param value Value to check.
	 * @param message Error message.
	 * @return Given value.
	 * @throws IllegalArgumentException If {@code value} is strictly less than zero.
	 */
	static int isPositive(int value, String message) {
		if (value < 0) {
			throw new IllegalArgumentException(message);
		}

		return value;
	}
}
