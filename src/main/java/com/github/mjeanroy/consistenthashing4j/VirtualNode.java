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
import java.util.UUID;

final class VirtualNode implements Node {

	private final Node parentNode;
	private final String id;

	VirtualNode(Node parentNode) {
		this.parentNode = PreConditions.notNull(parentNode, "Parent node must be defined");
		this.id = UUID.randomUUID().toString();
	}

	@Override
	public String getName() {
		return "@@@" + parentNode.getName() + "@@@" + id + "@@@";
	}

	Node getParentNode() {
		return parentNode;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (o instanceof VirtualNode) {
			VirtualNode vn = (VirtualNode) o;
			return Objects.equals(parentNode, vn.parentNode) && Objects.equals(id, vn.id);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(parentNode, id);
	}
}
