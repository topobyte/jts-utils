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
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public class PredicateEvaluatorJts extends AbstractPredicateEvaluator
{

	private GeometryFactory factory;
	private Geometry geometry;

	public PredicateEvaluatorJts(Geometry geometry)
	{
		factory = new GeometryFactory();
		this.geometry = geometry;
	}

	@Override
	public boolean covers(Coordinate coordinate)
	{
		return covers(factory.createPoint(coordinate));
	}

	@Override
	public boolean contains(Coordinate coordinate)
	{
		return contains(factory.createPoint(coordinate));
	}

	@Override
	public boolean covers(Point point)
	{
		return geometry.covers(point);
	}

	@Override
	public boolean contains(Point point)
	{
		return geometry.contains(point);
	}

	@Override
	public boolean covers(Envelope envelope)
	{
		return geometry.covers(factory.toGeometry(envelope));
	}

	@Override
	public boolean contains(Envelope envelope)
	{
		return geometry.contains(factory.toGeometry(envelope));
	}

	@Override
	public boolean coversNonCollection(Geometry geometry)
	{
		return this.geometry.covers(geometry);
	}

	@Override
	public boolean containsNonCollection(Geometry geometry)
	{
		return this.geometry.contains(geometry);
	}

	@Override
	public boolean intersects(Envelope envelope)
	{
		return geometry.intersects(factory.toGeometry(envelope));
	}

	@Override
	public boolean intersectsNonCollection(Geometry geometry)
	{
		return geometry.intersects(geometry);
	}

}
