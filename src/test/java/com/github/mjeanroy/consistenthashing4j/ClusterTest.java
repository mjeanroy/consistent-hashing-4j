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

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

class ClusterTest {

	@Test
	void it_should_create_ring() {
		List<Node> nodes = asList(
				Nodes.of("192.168.1.1"),
				Nodes.of("192.168.1.2"),
				Nodes.of("192.168.1.3"),
				Nodes.of("192.168.1.4")
		);

		Cluster cluster = Cluster.of(
				nodes
		);

		assertThat(cluster).isNotNull();
		assertThat(cluster.isEmpty()).isFalse();
		assertThat(cluster.size()).isEqualTo(4);
	}

	@Test
	void it_should_create_ring_and_add_node() {
		Cluster cluster = Cluster.emptyCluster();
		cluster.addNode(Nodes.of("192.168.1.1"));
		cluster.addNode(Nodes.of("192.168.1.2"));
		cluster.addNode(Nodes.of("192.168.1.3"));
		cluster.addNode(Nodes.of("192.168.1.4"));

		assertThat(cluster).isNotNull();
		assertThat(cluster.isEmpty()).isFalse();
		assertThat(cluster.size()).isEqualTo(4);
	}

	@Test
	void it_should_add_node_names() {
		Cluster cluster = Cluster.emptyCluster();
		cluster.addNode("192.168.1.1");
		cluster.addNode("192.168.1.2");
		cluster.addNode("192.168.1.3");
		cluster.addNode("192.168.1.4");

		assertThat(cluster).isNotNull();
		assertThat(cluster.isEmpty()).isFalse();
		assertThat(cluster.size()).isEqualTo(4);
	}

	@Test
	void it_should_get_node_for_given_value() {
		ClusterConfiguration configuration = ClusterConfiguration.builder()
				.hashFunction(new FakeHashFunction())
				.build();

		Cluster cluster = Cluster.of(configuration);
		cluster.addNode("2");
		cluster.addNode("4");
		cluster.addNode("8");

		assertThat(cluster.findNode("0").getName()).isEqualTo("2");
		assertThat(cluster.findNode("1").getName()).isEqualTo("2");
		assertThat(cluster.findNode("2").getName()).isEqualTo("2");
		assertThat(cluster.findNode("3").getName()).isEqualTo("4");
		assertThat(cluster.findNode("4").getName()).isEqualTo("4");
		assertThat(cluster.findNode("5").getName()).isEqualTo("8");
		assertThat(cluster.findNode("6").getName()).isEqualTo("8");
		assertThat(cluster.findNode("7").getName()).isEqualTo("8");
		assertThat(cluster.findNode("8").getName()).isEqualTo("8");
		assertThat(cluster.findNode("9").getName()).isEqualTo("2");
		assertThat(cluster.findNode("10").getName()).isEqualTo("2");
	}

	@Test
	void it_should_get_node_for_given_value_and_remove_node() {
		ClusterConfiguration configuration = ClusterConfiguration.builder()
				.hashFunction(new FakeHashFunction())
				.build();

		Cluster cluster = Cluster.of(configuration);
		cluster.addNode("2");
		cluster.addNode("4");
		cluster.addNode("8");

		assertThat(cluster.findNode("0").getName()).isEqualTo("2");
		assertThat(cluster.findNode("1").getName()).isEqualTo("2");
		assertThat(cluster.findNode("2").getName()).isEqualTo("2");
		assertThat(cluster.findNode("3").getName()).isEqualTo("4");
		assertThat(cluster.findNode("4").getName()).isEqualTo("4");
		assertThat(cluster.findNode("5").getName()).isEqualTo("8");
		assertThat(cluster.findNode("6").getName()).isEqualTo("8");
		assertThat(cluster.findNode("7").getName()).isEqualTo("8");
		assertThat(cluster.findNode("8").getName()).isEqualTo("8");
		assertThat(cluster.findNode("9").getName()).isEqualTo("2");
		assertThat(cluster.findNode("10").getName()).isEqualTo("2");

		cluster.removeNode("4");

		assertThat(cluster.findNode("0").getName()).isEqualTo("2");
		assertThat(cluster.findNode("1").getName()).isEqualTo("2");
		assertThat(cluster.findNode("2").getName()).isEqualTo("2");
		assertThat(cluster.findNode("3").getName()).isEqualTo("8");
		assertThat(cluster.findNode("4").getName()).isEqualTo("8");
		assertThat(cluster.findNode("5").getName()).isEqualTo("8");
		assertThat(cluster.findNode("6").getName()).isEqualTo("8");
		assertThat(cluster.findNode("7").getName()).isEqualTo("8");
		assertThat(cluster.findNode("8").getName()).isEqualTo("8");
		assertThat(cluster.findNode("9").getName()).isEqualTo("2");
		assertThat(cluster.findNode("10").getName()).isEqualTo("2");

		cluster.removeNode(Nodes.of("8"));

		assertThat(cluster.findNode("0").getName()).isEqualTo("2");
		assertThat(cluster.findNode("1").getName()).isEqualTo("2");
		assertThat(cluster.findNode("2").getName()).isEqualTo("2");
		assertThat(cluster.findNode("3").getName()).isEqualTo("2");
		assertThat(cluster.findNode("4").getName()).isEqualTo("2");
		assertThat(cluster.findNode("5").getName()).isEqualTo("2");
		assertThat(cluster.findNode("6").getName()).isEqualTo("2");
		assertThat(cluster.findNode("7").getName()).isEqualTo("2");
		assertThat(cluster.findNode("8").getName()).isEqualTo("2");
		assertThat(cluster.findNode("9").getName()).isEqualTo("2");
		assertThat(cluster.findNode("10").getName()).isEqualTo("2");
	}

	@Test
	void it_should_create_cluster_with_virtual_node() {
		ClusterConfiguration configuration = ClusterConfiguration.builder()
				.nbVirtualNodes(2)
				.build();

		Cluster cluster = Cluster.of(configuration);
		cluster.addNode("192.168.1.1");
		cluster.addNode("192.168.1.2");
		cluster.addNode("192.168.1.3");

		assertThat(cluster.size()).isEqualTo(9);
	}

	@Test
	void it_should_add_node_with_additional_virtual_nodes() {
		ClusterConfiguration configuration = ClusterConfiguration.builder()
				.nbVirtualNodes(2)
				.build();

		Cluster cluster = Cluster.of(configuration);
		cluster.addNode("192.168.1.1", 3);
		cluster.addNode(Nodes.of("192.168.1.2"), 4);
		cluster.addNode("192.168.1.3", 5);

		assertThat(cluster.size()).isEqualTo(15);
	}

	@Test
	void it_should_remove_node_and_all_its_virtual_nodes() {
		ClusterConfiguration configuration = ClusterConfiguration.defaultConfiguration();

		Cluster cluster = Cluster.of(configuration);
		cluster.addNode("192.168.1.1", 3);
		cluster.addNode("192.168.1.2", 4);
		cluster.addNode("192.168.1.3", 5);
		assertThat(cluster.size()).isEqualTo(15);

		cluster.removeNode("192.168.1.2");
		assertThat(cluster.size()).isEqualTo(10);

		cluster.removeNode("192.168.1.3");
		assertThat(cluster.size()).isEqualTo(4);

		cluster.removeNode("192.168.1.1");
		assertThat(cluster.size()).isEqualTo(0);
	}

	@Test
	void it_should_implement_equals_hash_code() {
		EqualsVerifier.forClass(Cluster.class).verify();
	}

	@Test
	void it_should_implement_to_string() {
		ClusterConfiguration configuration = ClusterConfiguration.builder()
				.hashFunction(new FakeHashFunction())
				.build();

		Cluster cluster = Cluster.of(configuration);
		cluster.addNode("1");
		cluster.addNode("2");
		cluster.addNode("3");

		assertThat(cluster).hasToString(
				"Cluster{" +
						"ring: {" +
							"1=ClusterNode{node: DefaultNode{name: \"1\"}, virtualNodes: []}, " +
							"2=ClusterNode{node: DefaultNode{name: \"2\"}, virtualNodes: []}, " +
							"3=ClusterNode{node: DefaultNode{name: \"3\"}, virtualNodes: []}" +
						"}, " +
						"configuration: ClusterConfiguration{" +
							"hashFunction: FakeHashFunction, " +
							"nbVirtualNodes: 0" +
						"}" +
				"}"
		);
	}

	private static class FakeHashFunction implements HashFunction {
		@Override
		public int compute(String value) {
			return Integer.parseInt(value);
		}

		@Override
		public String toString() {
			return getClass().getSimpleName();
		}
	}
}
