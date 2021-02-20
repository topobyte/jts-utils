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

import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.Polygonal;
import org.locationtech.jts.geom.TopologyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PolygonSplitUtil
{

	final static Logger logger = LoggerFactory
			.getLogger(PolygonSplitUtil.class);

	public static List<Geometry> split(Geometry geometry, SplitMode mode)
	{
		if (!(geometry instanceof Polygonal)) {
			throw new IllegalArgumentException(
					"split only accepts Polygonal arguments");
		}
		return reallySplit(geometry, mode);
	}

	// only Polygonal arguments expected here
	private static List<Geometry> reallySplit(Geometry geometry, SplitMode mode)
	{
		RectangleGenerator rg = null;
		switch (mode) {
		default:
		case PCA:
			rg = new RectangleGeneratorPCA(geometry);
			break;
		case HORIZONTAL:
			rg = new RectangleGeneratorAxis(geometry, true);
			break;
		case VERTICAL:
			rg = new RectangleGeneratorAxis(geometry, false);
			break;
		case ALTERNATING:
			rg = new RectangleGeneratorAlternating(geometry);
			break;
		}
		return split(geometry, rg);
	}

	private static List<Geometry> split(Geometry geometry, RectangleGenerator rg)
	{
		while (true) {
			List<Geometry> intersections = new ArrayList<>();
			List<Polygon> rectangles = rg.createRectangles();

			boolean ok = true;
			for (Polygon rect : rectangles) {
				Geometry intersection;
				try {
					intersection = geometry.intersection(rect);
				} catch (TopologyException e) {
					logger.info("TopologyException during split");
					ok = false;
					break;
				}
				if (!(intersection instanceof Polygonal)) {
					logger.info("Non-Polygonal results during split");
					ok = false;
					break;
				}
				intersections.add(intersection);
			}
			if (ok) {
				return intersections;
			}
		}
	}

	public static List<Geometry> split(Geometry geometry, int maxPoints,
			SplitMode mode)
	{
		if (!(geometry instanceof Polygonal)) {
			throw new IllegalArgumentException(
					"split only accepts Polygonal arguments");
		}
		return reallySplit(geometry, maxPoints, mode);
	}

	private static List<Geometry> reallySplit(Geometry geometry, int maxPoints,
			SplitMode mode)
	{
		List<Geometry> results = new ArrayList<>();
		if (geometry.getNumPoints() <= maxPoints) {
			results.add(geometry);
		} else if (geometry instanceof GeometryCollection) {
			GeometryCollection gc = (GeometryCollection) geometry;
			for (int i = 0; i < gc.getNumGeometries(); i++) {
				Geometry part = gc.getGeometryN(i);
				List<Geometry> split = reallySplit(part, maxPoints, mode);
				results.addAll(split);
			}
		} else {
			List<Geometry> parts = split(geometry, mode);
			for (Geometry part : parts) {
				List<Geometry> split = reallySplit(part, maxPoints, mode);
				results.addAll(split);
			}
		}
		return results;
	}

}
