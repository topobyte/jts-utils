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

package de.topobyte.jts.utils.pca;

import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;

public class PCAUtil
{

	public static Polygon createRectangleAsPolygon(PCA pca)
	{
		LinearRing ring = createRectangleAsRing(pca);
		return ring.getFactory().createPolygon(ring, null);
	}

	public static LinearRing createRectangleAsRing(PCA pca)
	{
		Coordinate mu = pca.getMu();

		int n = pca.getNumberOfPoints();
		Coordinate e1 = pca.getEigenVector1();
		Coordinate e2 = pca.getEigenVector2();
		double[] p1 = pca.getSortedProjectionsOn(e1);
		double[] p2 = pca.getSortedProjectionsOn(e2);

		// a and b are the extreme points on the major axis
		Coordinate a = new Coordinate(mu.x + p1[0] * e1.x, mu.y + p1[0] * e1.y);
		Coordinate b = new Coordinate(mu.x + p1[n - 1] * e1.x,
				mu.y + p1[n - 1] * e1.y);

		Coordinate[] coords = createRingCoordinates(a, b, e2, p2[0], p2[n - 1]);

		return pca.getGeometry().getFactory().createLinearRing(coords);
	}

	private static Coordinate[] createRingCoordinates(Coordinate a,
			Coordinate b, Coordinate e2, double l1, double l2)
	{
		// c1...c4 are the points of the rectangle
		Coordinate c1 = new Coordinate(a.x + l1 * e2.x, a.y + l1 * e2.y);
		Coordinate c2 = new Coordinate(a.x + l2 * e2.x, a.y + l2 * e2.y);
		Coordinate c3 = new Coordinate(b.x + l2 * e2.x, b.y + l2 * e2.y);
		Coordinate c4 = new Coordinate(b.x + l1 * e2.x, b.y + l1 * e2.y);

		Coordinate[] coords = new Coordinate[] { c1, c2, c3, c4, c1 };
		return coords;
	}

	public static List<Polygon> createSplitRectangles(PCA pca, double pos,
			double safetyFactor)
	{
		Coordinate mu = pca.getMu();

		int n = pca.getNumberOfPoints();
		Coordinate e1 = pca.getEigenVector1();
		Coordinate e2 = pca.getEigenVector2();
		double[] p1 = pca.getSortedProjectionsOn(e1);
		double[] p2 = pca.getSortedProjectionsOn(e2);

		// a and b are the extreme points on the major axis
		Coordinate a = new Coordinate(mu.x + p1[0] * e1.x, mu.y + p1[0] * e1.y);
		Coordinate b = new Coordinate(mu.x + p1[n - 1] * e1.x,
				mu.y + p1[n - 1] * e1.y);

		if (pos < 0 || pos > 1) {
			throw new IllegalArgumentException(
					"'pos' must be in [0,1], currently is: '" + pos + "'");
		}
		if (safetyFactor < 1) {
			throw new IllegalArgumentException(
					"'safetyFactory' must be in >= 1, currently is: '"
							+ safetyFactor + "'");
		}

		// scale linearly in both parts. 0.5 is at the median of the point-set,
		// so we have to distinguish both cases and interpolate within the
		// respective range
		double ppos = pos < 0.5 ? (pos - 0.5) * 2 * Math.abs(p1[0])
				: (pos - 0.5) * 2 * Math.abs(p1[n - 1]);
		Coordinate c = new Coordinate(mu.x + ppos * e1.x, mu.y + ppos * e1.y);

		// make rectangles a bit bigger to make geometric intersection with the
		// original geometry safe
		double span1 = Math.abs(p1[0]) + Math.abs(p1[n - 1]);
		double span2 = Math.abs(p2[0]) + Math.abs(p2[n - 1]);
		double buffer1 = (safetyFactor - 1) * span1;
		double buffer2 = (safetyFactor - 1) * span2;
		a = new Coordinate(a.x - buffer1 * e1.x, a.y - buffer1 * e1.y);
		b = new Coordinate(b.x + buffer1 * e1.x, b.y + buffer1 * e1.y);

		Coordinate[] coords1 = createRingCoordinates(a, c, e2, p2[0] - buffer2,
				p2[n - 1] + buffer2);
		Coordinate[] coords2 = createRingCoordinates(c, b, e2, p2[0] - buffer2,
				p2[n - 1] + buffer2);

		GeometryFactory factory = pca.getGeometry().getFactory();
		LinearRing ring1 = factory.createLinearRing(coords1);
		LinearRing ring2 = factory.createLinearRing(coords2);

		List<Polygon> rects = new ArrayList<>();
		rects.add(factory.createPolygon(ring1, null));
		rects.add(factory.createPolygon(ring2, null));
		return rects;
	}

}
