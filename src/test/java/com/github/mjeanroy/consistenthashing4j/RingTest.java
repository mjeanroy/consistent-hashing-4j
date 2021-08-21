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

import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

class RingTest {

	@Test
	void it_should_create_ring() {
		List<Node> nodes = asList(
				Nodes.newNode("192.168.1.1"),
				Nodes.newNode("192.168.1.2"),
				Nodes.newNode("192.168.1.3"),
				Nodes.newNode("192.168.1.4")
		);

		Ring ring = Ring.newRing(
				nodes
		);

		assertThat(ring).isNotNull();
		assertThat(ring.isEmpty()).isFalse();
		assertThat(ring.size()).isEqualTo(4);
	}

	@Test
	void it_should_create_ring_and_add_node() {
		Ring ring = Ring.newRing();
		ring.addNode(Nodes.newNode("192.168.1.1"));
		ring.addNode(Nodes.newNode("192.168.1.2"));
		ring.addNode(Nodes.newNode("192.168.1.3"));
		ring.addNode(Nodes.newNode("192.168.1.4"));

		assertThat(ring).isNotNull();
		assertThat(ring.isEmpty()).isFalse();
		assertThat(ring.size()).isEqualTo(4);
	}

	@Test
	void it_should_add_node_names() {
		Ring ring = Ring.newRing();
		ring.addNode("192.168.1.1");
		ring.addNode("192.168.1.2");
		ring.addNode("192.168.1.3");
		ring.addNode("192.168.1.4");

		assertThat(ring).isNotNull();
		assertThat(ring.isEmpty()).isFalse();
		assertThat(ring.size()).isEqualTo(4);
	}

	@Test
	void it_should_get_node_for_given_value() {
		RingConfiguration configuration = RingConfiguration.builder()
				.hashFunction(new FakeHashFunction())
				.build();

		Ring ring = Ring.newRing(configuration);
		ring.addNode("2");
		ring.addNode("4");
		ring.addNode("8");

		assertThat(ring.findNode("0").getName()).isEqualTo("2");
		assertThat(ring.findNode("1").getName()).isEqualTo("2");
		assertThat(ring.findNode("2").getName()).isEqualTo("2");
		assertThat(ring.findNode("3").getName()).isEqualTo("4");
		assertThat(ring.findNode("4").getName()).isEqualTo("4");
		assertThat(ring.findNode("5").getName()).isEqualTo("8");
		assertThat(ring.findNode("6").getName()).isEqualTo("8");
		assertThat(ring.findNode("7").getName()).isEqualTo("8");
		assertThat(ring.findNode("8").getName()).isEqualTo("8");
		assertThat(ring.findNode("9").getName()).isEqualTo("2");
		assertThat(ring.findNode("10").getName()).isEqualTo("2");
	}

	@Test
	void it_should_get_node_for_given_value_and_remove_node() {
		RingConfiguration configuration = RingConfiguration.builder()
				.hashFunction(new FakeHashFunction())
				.build();

		Ring ring = Ring.newRing(configuration);
		ring.addNode("2");
		ring.addNode("4");
		ring.addNode("8");

		assertThat(ring.findNode("0").getName()).isEqualTo("2");
		assertThat(ring.findNode("1").getName()).isEqualTo("2");
		assertThat(ring.findNode("2").getName()).isEqualTo("2");
		assertThat(ring.findNode("3").getName()).isEqualTo("4");
		assertThat(ring.findNode("4").getName()).isEqualTo("4");
		assertThat(ring.findNode("5").getName()).isEqualTo("8");
		assertThat(ring.findNode("6").getName()).isEqualTo("8");
		assertThat(ring.findNode("7").getName()).isEqualTo("8");
		assertThat(ring.findNode("8").getName()).isEqualTo("8");
		assertThat(ring.findNode("9").getName()).isEqualTo("2");
		assertThat(ring.findNode("10").getName()).isEqualTo("2");

		ring.removeNode("4");

		assertThat(ring.findNode("0").getName()).isEqualTo("2");
		assertThat(ring.findNode("1").getName()).isEqualTo("2");
		assertThat(ring.findNode("2").getName()).isEqualTo("2");
		assertThat(ring.findNode("3").getName()).isEqualTo("8");
		assertThat(ring.findNode("4").getName()).isEqualTo("8");
		assertThat(ring.findNode("5").getName()).isEqualTo("8");
		assertThat(ring.findNode("6").getName()).isEqualTo("8");
		assertThat(ring.findNode("7").getName()).isEqualTo("8");
		assertThat(ring.findNode("8").getName()).isEqualTo("8");
		assertThat(ring.findNode("9").getName()).isEqualTo("2");
		assertThat(ring.findNode("10").getName()).isEqualTo("2");

		ring.removeNode(Nodes.newNode("8"));

		assertThat(ring.findNode("0").getName()).isEqualTo("2");
		assertThat(ring.findNode("1").getName()).isEqualTo("2");
		assertThat(ring.findNode("2").getName()).isEqualTo("2");
		assertThat(ring.findNode("3").getName()).isEqualTo("2");
		assertThat(ring.findNode("4").getName()).isEqualTo("2");
		assertThat(ring.findNode("5").getName()).isEqualTo("2");
		assertThat(ring.findNode("6").getName()).isEqualTo("2");
		assertThat(ring.findNode("7").getName()).isEqualTo("2");
		assertThat(ring.findNode("8").getName()).isEqualTo("2");
		assertThat(ring.findNode("9").getName()).isEqualTo("2");
		assertThat(ring.findNode("10").getName()).isEqualTo("2");
	}

	@Test
	void it_should_create_ring_with_virtual_node() {
		RingConfiguration configuration = RingConfiguration.builder()
				.nbVirtualNodes(2)
				.build();

		Ring ring = Ring.newRing(configuration);
		ring.addNode("192.168.1.1");
		ring.addNode("192.168.1.2");
		ring.addNode("192.168.1.3");

		assertThat(ring.size()).isEqualTo(9);
	}

	private static class FakeHashFunction implements HashFunction {
		@Override
		public int compute(String value) {
			return Integer.parseInt(value);
		}
	}
}
