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

package de.topobyte.jts.utils.transform;

import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.CoordinateSequenceFactory;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.util.GeometryTransformer;

import de.topobyte.jgs.transform.CoordinateTransformer;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class CoordinateGeometryTransformer extends GeometryTransformer
{

	private CoordinateTransformer ct;

	/**
	 * @param ct
	 *            the coordinate transformation to use.
	 */
	public CoordinateGeometryTransformer(CoordinateTransformer ct)
	{
		this.ct = ct;
	}

	@Override
	protected CoordinateSequence transformCoordinates(
			CoordinateSequence coords, Geometry parent)
	{
		CoordinateSequenceFactory csf = factory.getCoordinateSequenceFactory();
		CoordinateSequence cs = csf
				.create(coords.size(), coords.getDimension());
		for (int i = 0; i < coords.size(); i++) {
			cs.setOrdinate(i, 0, ct.getX(coords.getX(i)));
			cs.setOrdinate(i, 1, ct.getY(coords.getY(i)));
		}
		return cs;
	}

}
