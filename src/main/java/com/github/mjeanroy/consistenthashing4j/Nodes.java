package com.github.mjeanroy.consistenthashing4j;

public class Nodes {

	/**
	 * Create new node.
	 * @param name Node name.
	 * @return The node.
	 * @throws NullPointerException If {@code name} is {@code null}.
	 * @throws IllegalArgumentException If {@code name} is empty or blank.
	 */
	public static Node of(String name) {
		return new DefaultNode(name);
	}
}
