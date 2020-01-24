// Copyright 2015 Sebastian Kuerten
//
// This file is part of jts-utils.
//
// jts-utils is free software: you can redistribute it and/or modify
// it under the terms of the GNU Lesser General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// jts-utils is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public License
// along with jts-utils. If not, see <http://www.gnu.org/licenses/>.

package de.topobyte.jts.utils.predicate;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;

public interface PredicateEvaluator
{

	/**
	 * Test for coverage. Includes the boundary of geometric objects.
	 */
	public boolean covers(Coordinate coordinate);

	/**
	 * Test for containment. Excludes the boundary of geometric objects.
	 */
	public boolean contains(Coordinate coordinate);

	/**
	 * Test for coverage. Includes the boundary of geometric objects.
	 */
	public boolean covers(Point point);

	/**
	 * Test for containment. Excludes the boundary of geometric objects.
	 */
	public boolean contains(Point point);

	/**
	 * Test for coverage. Includes the boundary of geometric objects.
	 */
	public boolean covers(Envelope envelope);

	/**
	 * Test for containment. Excludes the boundary of geometric objects.
	 */
	public boolean contains(Envelope envelope);

	/**
	 * Test for coverage. Includes the boundary of geometric objects.
	 */
	public boolean covers(Geometry geometry);

	/**
	 * Test for containment. Excludes the boundary of geometric objects.
	 */
	public boolean contains(Geometry geometry);

	/**
	 * Test for intersection.
	 */
	public boolean intersects(Geometry geometry);

	/**
	 * Test for intersection.
	 */
	public boolean intersects(Envelope envelope);

}
