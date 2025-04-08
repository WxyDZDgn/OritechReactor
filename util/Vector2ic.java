/*
 * The MIT License
 *
 * Copyright (c) 2016-2022 JOML
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package util;


/**
 * Interface to a read-only view of a 2-dimensional vector of integers.
 *
 * @author Kai Burjack
 */
public interface Vector2ic {

	/**
	 * @return the value of the x component
	 */
	int x();

	/**
	 * @return the value of the y component
	 */
	int y();

	/**
	 * Add the supplied vector to this one and store the result in
	 * <code>dest</code>.
	 *
	 * @param v
	 *          the vector to add
	 * @param dest
	 *          will hold the result
	 * @return dest
	 */
	Vector2i add(Vector2ic v, Vector2i dest);

	/**
	 * Increment the components of this vector by the given values and store the
	 * result in <code>dest</code>.
	 *
	 * @param x
	 *          the x component to add
	 * @param y
	 *          the y component to add
	 * @param dest
	 *          will hold the result
	 * @return dest
	 */
	Vector2i add(int x, int y, Vector2i dest);

}
