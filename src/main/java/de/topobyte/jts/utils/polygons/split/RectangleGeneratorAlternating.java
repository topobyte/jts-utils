// Copyright 2021 Sebastian Kuerten
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

package de.topobyte.jts.utils.polygons.split;

import java.util.List;

import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;

public class RectangleGeneratorAlternating implements RectangleGenerator
{

	private RectangleGenerator delegate;

	public RectangleGeneratorAlternating(Geometry geometry)
	{
		Envelope envelope = geometry.getEnvelopeInternal();
		boolean horizontal = envelope.getWidth() > envelope.getHeight();
		delegate = new RectangleGeneratorAxis(geometry, horizontal);
	}

	@Override
	public List<Polygon> createRectangles()
	{
		return delegate.createRectangles();
	}

}
