// Copyright 2016 Sebastian Kuerten
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

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;

public abstract class AbstractPredicateEvaluator implements PredicateEvaluator
{

	@Override
	public boolean covers(Geometry geometry)
	{
		if (geometry instanceof GeometryCollection) {
			return coversCollection(geometry);
		}
		return coversNonCollection(geometry);
	}

	@Override
	public boolean contains(Geometry geometry)
	{
		if (geometry instanceof GeometryCollection) {
			return containsCollection(geometry);
		}
		return containsNonCollection(geometry);
	}

	@Override
	public boolean intersects(Geometry geometry)
	{
		if (geometry instanceof GeometryCollection) {
			return intersectsCollection(geometry);
		}
		return intersectsNonCollection(geometry);
	}

	public abstract boolean coversNonCollection(Geometry geometry);

	public boolean coversCollection(Geometry b)
	{
		for (int i = 0; i < b.getNumGeometries(); i++) {
			Geometry g = b.getGeometryN(i);
			if (!covers(g)) {
				return false;
			}
		}
		return true;
	}

	public abstract boolean containsNonCollection(Geometry geometry);

	public boolean containsCollection(Geometry b)
	{
		for (int i = 0; i < b.getNumGeometries(); i++) {
			Geometry g = b.getGeometryN(i);
			if (!contains(g)) {
				return false;
			}
		}
		return true;
	}

	public abstract boolean intersectsNonCollection(Geometry geometry);

	public boolean intersectsCollection(Geometry b)
	{
		for (int i = 0; i < b.getNumGeometries(); i++) {
			Geometry g = b.getGeometryN(i);
			if (intersects(g)) {
				return true;
			}
		}
		return false;
	}

}
