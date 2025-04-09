package org.whania.util;


import net.minecraft.util.Pair;

public class MyPair<A, B> extends Pair<A, B> {
	public MyPair(A left, B right) {
		super(left, right);
	}
	public String toString() {
		return "(%s, %s)".formatted(getLeft().toString(), getRight().toString());
	}
}
