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

import java.util.Iterator;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

class RingNodeTest {

	@Test
	void it_should_create_node() {
		Node rootNode = Nodes.of("192.168.1.1");
		VirtualNode vn1 = new VirtualNode(rootNode);
		VirtualNode vn2 = new VirtualNode(rootNode);
		RingNode node = RingNode.of(rootNode, asList(vn1, vn2));

		assertThat(node).isNotNull();
		assertThat(node.getNode()).isSameAs(rootNode);
		assertThat(node.getVirtualNodes()).isEqualTo(asList(vn1, vn2));
		assertThat(node.rootNode()).isSameAs(rootNode);
	}

	@Test
	void it_should_create_node_from_virtual_node() {
		Node rootNode = Nodes.of("192.168.1.1");
		VirtualNode vn1 = new VirtualNode(rootNode);
		RingNode node = RingNode.of(vn1, emptyList());

		assertThat(node).isNotNull();
		assertThat(node.getNode()).isSameAs(vn1);
		assertThat(node.getVirtualNodes()).isNotNull().isEmpty();
		assertThat(node.rootNode()).isSameAs(rootNode);
	}

	@Test
	void it_should_implement_equals_hash_code() {
		EqualsVerifier.forClass(RingNode.class).verify();
	}

	@Test
	void it_should_implement_to_string() {
		Node rootNode = Nodes.of("192.168.1.1");
		RingNode node = RingNode.of(rootNode, asList(
				new VirtualNode(rootNode),
				new VirtualNode(rootNode)
		));

		Iterator<VirtualNode> it = node.getVirtualNodes().iterator();
		VirtualNode vn1 = it.next();
		VirtualNode vn2 = it.next();

		assertThat(node).hasToString(
				"RingNode{" +
						"node: DefaultNode{name: \"192.168.1.1\"}, " +
						"virtualNodes: [" +
							"VirtualNode{parentNode: DefaultNode{name: \"192.168.1.1\"}, id: \"" + vn1.getId() + "\"}, " +
							"VirtualNode{parentNode: DefaultNode{name: \"192.168.1.1\"}, id: \"" + vn2.getId() + "\"}" +
						"]" +
				"}"
		);
	}
}
