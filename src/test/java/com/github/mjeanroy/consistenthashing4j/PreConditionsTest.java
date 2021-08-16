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

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PreConditionsTest {

	@Nested
	class NotNull {
		@Test
		void it_should_throw_null_pointer_exception() {
			String message = "should not be null";
			String value = null;
			assertThatThrownBy(() -> PreConditions.notNull(value, message))
					.isExactlyInstanceOf(NullPointerException.class)
					.hasMessage(message);
		}

		@Test
		void it_should_not_throw_null_pointer_exception_and_return_value() {
			String message = "should not be null";
			String value = "value";
			String result = PreConditions.notNull(value, message);
			assertThat(result).isSameAs(value);
		}
	}

	@Nested
	class StringNotEmpty {
		@Test
		void it_should_throw_null_pointer_exception_if_value_is_null() {
			String message = "should not be empty";
			String value = null;
			assertThatThrownBy(() -> PreConditions.notEmpty(value, message))
					.isExactlyInstanceOf(NullPointerException.class)
					.hasMessage(message);
		}

		@Test
		void it_should_throw_illegal_argument_exception_if_value_is_empty() {
			String message = "should not be empty";
			String value = "";
			assertThatThrownBy(() -> PreConditions.notEmpty(value, message))
					.isExactlyInstanceOf(IllegalArgumentException.class)
					.hasMessage(message);
		}

		@Test
		void it_should_not_throw_exception_and_return_value_if_value_is_not_empty() {
			String message = "should not be empty";
			String value = "value";
			String result = PreConditions.notEmpty(value, message);
			assertThat(result).isSameAs(value);
		}
	}

	@Nested
	class NotBlank {
		@Test
		void it_should_throw_null_pointer_exception_if_value_is_null() {
			String message = "should not be blank";
			String value = null;
			assertThatThrownBy(() -> PreConditions.notBlank(value, message))
					.isExactlyInstanceOf(NullPointerException.class)
					.hasMessage(message);
		}

		@Test
		void it_should_throw_illegal_argument_exception_if_value_is_empty() {
			String message = "should not be blank";
			String value = "";
			assertThatThrownBy(() -> PreConditions.notBlank(value, message))
					.isExactlyInstanceOf(IllegalArgumentException.class)
					.hasMessage(message);
		}

		@Test
		void it_should_throw_illegal_argument_exception_if_value_is_blank() {
			String message = "should not be blank";
			String value = "   ";
			assertThatThrownBy(() -> PreConditions.notBlank(value, message))
					.isExactlyInstanceOf(IllegalArgumentException.class)
					.hasMessage(message);
		}

		@Test
		void it_should_not_throw_exception_and_return_value_if_value_is_not_blank() {
			String message = "should not be blank";
			String value = "value";
			String result = PreConditions.notBlank(value, message);
			assertThat(result).isSameAs(value);
		}
	}

	@Nested
	class CollectionNotEmpty {
		@Test
		void it_should_throw_null_pointer_exception_if_value_is_null() {
			String message = "should not be empty";
			Collection<String> values = null;
			assertThatThrownBy(() -> PreConditions.notEmpty(values, message))
					.isExactlyInstanceOf(NullPointerException.class)
					.hasMessage(message);
		}

		@Test
		void it_should_throw_illegal_argument_exception_if_value_is_empty() {
			String message = "should not be empty";
			Collection<String> values = emptyList();
			assertThatThrownBy(() -> PreConditions.notEmpty(values, message))
					.isExactlyInstanceOf(IllegalArgumentException.class)
					.hasMessage(message);
		}

		@Test
		void it_should_not_throw_exception_and_return_value_if_value_is_not_blank() {
			String message = "should not be null";
			Collection<String> values = singleton("value");
			Collection<String> result = PreConditions.notEmpty(values, message);
			assertThat(result).isSameAs(values);
		}
	}

	@Nested
	class MapNotEmpty {
		@Test
		void it_should_throw_null_pointer_exception_if_value_is_null() {
			String message = "should not be empty";
			Map<String, String> values = null;
			assertThatThrownBy(() -> PreConditions.notEmpty(values, message))
					.isExactlyInstanceOf(NullPointerException.class)
					.hasMessage(message);
		}

		@Test
		void it_should_throw_illegal_argument_exception_if_value_is_empty() {
			String message = "should not be empty";
			Map<String, String> values = emptyMap();
			assertThatThrownBy(() -> PreConditions.notEmpty(values, message))
					.isExactlyInstanceOf(IllegalArgumentException.class)
					.hasMessage(message);
		}

		@Test
		void it_should_not_throw_exception_and_return_value_if_value_is_not_blank() {
			String message = "should not be null";
			Map<String, String> values = singletonMap("key", "value");
			Map<String, String> result = PreConditions.notEmpty(values, message);
			assertThat(result).isSameAs(values);
		}
	}
}
