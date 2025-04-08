package util;

import java.io.*;

/**
 * Represents a 2D vector with single-precision.
 *
 * @author RGreenlees
 * @author Kai Burjack
 * @author Hans Uhlig
 */
public class Vector2i implements Vector2ic, Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * The x component of the vector.
	 */
	public int x;
	/**
	 * The y component of the vector.
	 */
	public int y;

	public Vector2i() {
	}

	public Vector2i(Vector2ic v) {
		x = v.x();
		y = v.y();
	}

	public int x() {
		return this.x;
	}

	public int y() {
		return this.y;
	}

	/**
	 * Add <code>v</code> to this vector.
	 *
	 * @param v
	 *          the vector to add
	 * @return this
	 */
	public Vector2i add(Vector2ic v) {
		this.x = x + v.x();
		this.y = y + v.y();
		return this;
	}

	public Vector2i add(Vector2ic v, Vector2i dest) {
		dest.x = x + v.x();
		dest.y = y + v.y();
		return dest;
	}

	/**
	 * Increment the components of this vector by the given values.
	 *
	 * @param x
	 *          the x component to add
	 * @param y
	 *          the y component to add
	 * @return this
	 */
	public Vector2i add(int x, int y) {
		this.x = this.x + x;
		this.y = this.y + y;
		return this;
	}

	public Vector2i add(int x, int y, Vector2i dest) {
		dest.x = this.x + x;
		dest.y = this.y + y;
		return dest;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Vector2i other = (Vector2i) obj;
		if (x != other.x) {
			return false;
		}
		return y == other.y;
	}

	public String toString() {
		return "(" + x() + ", " + y() + ")";
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}
