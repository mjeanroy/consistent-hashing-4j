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

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RingConfigurationTest {

	@Test
	void it_should_create_default_configuration() {
		RingConfiguration configuration = RingConfiguration.defaultConfiguration();
		assertThat(configuration).isNotNull();
		assertThat(configuration.getHashFunction()).isNotNull();
	}

	@Test
	void it_should_create_custom_configuration() {
		HashFunction hashFunction = HashFunctions.jdkHashFunction();
		RingConfiguration configuration = RingConfiguration.builder()
				.hashFunction(hashFunction)
				.build();

		assertThat(configuration).isNotNull();
		assertThat(configuration.getHashFunction()).isSameAs(hashFunction);
	}

	@Test
	void it_should_implement_equals_hash_code() {
		EqualsVerifier.forClass(RingConfiguration.class).verify();
	}

	@Test
	void it_should_implement_to_string() {
		HashFunction hashFunction = HashFunctions.jdkHashFunction();
		RingConfiguration configuration = RingConfiguration.builder()
				.hashFunction(hashFunction)
				.nbVirtualNodes(2)
				.build();

		assertThat(configuration).hasToString(
				"RingConfiguration{" +
						"hashFunction: " + hashFunction.toString() + ", " +
						"nbVirtualNodes: 2" +
				"}"
		);
	}
}
