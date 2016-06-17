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

package de.topobyte.jts.utils;

import gnu.trove.list.TDoubleList;

import java.util.List;

import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.CoordinateSequenceFactory;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;

/**
 * Various utility methods for JTS.
 * 
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class JtsHelper
{

	/**
	 * Create a LineString from the denoted list of coordinates. The i'th point
	 * of the string is represented by (xs[i], ys[i]).
	 * 
	 * @param xs
	 *            the x values.
	 * @param ys
	 *            the y values.
	 * @return the created LineString instance.
	 */
	public static LineString toLineString(List<Double> xs, List<Double> ys)
	{
		GeometryFactory factory = new GeometryFactory();
		CoordinateSequenceFactory csf = factory.getCoordinateSequenceFactory();
		int len = xs.size();

		CoordinateSequence coords = csf.create(len, 2);
		for (int i = 0; i < xs.size(); i++) {
			coords.setOrdinate(i, 0, xs.get(i));
			coords.setOrdinate(i, 1, ys.get(i));
		}
		LineString string = factory.createLineString(coords);
		return string;
	}

	/**
	 * Create a LineString from the denoted list of coordinates. The i'th point
	 * of the string is represented by (xs[i], ys[i]).
	 * 
	 * @param xs
	 *            the x values.
	 * @param ys
	 *            the y values.
	 * @return the created LineString instance.
	 */
	public static LineString toLineString(TDoubleList xs, TDoubleList ys)
	{
		GeometryFactory factory = new GeometryFactory();
		CoordinateSequenceFactory csf = factory.getCoordinateSequenceFactory();
		int len = xs.size();

		CoordinateSequence coords = csf.create(len, 2);
		for (int i = 0; i < xs.size(); i++) {
			coords.setOrdinate(i, 0, xs.get(i));
			coords.setOrdinate(i, 1, ys.get(i));
		}
		LineString string = factory.createLineString(coords);
		return string;
	}

	/**
	 * Create a LinearRing from two lists of coordinates. The i'th point of the
	 * ring is represented by (xs[i], ys[i]).
	 * 
	 * @param xs
	 *            the list of x-coordinates.
	 * @param ys
	 *            the list of y-coordinates.
	 * @param doublePoint
	 *            whether the first point is explicitly contained in the lists a
	 *            second time as the last point.
	 * @return the constructed ring.
	 */
	public static LinearRing toLinearRing(List<Double> xs, List<Double> ys,
			boolean doublePoint)
	{
		GeometryFactory factory = new GeometryFactory();
		CoordinateSequenceFactory csf = factory.getCoordinateSequenceFactory();
		int len = xs.size();
		if (!doublePoint)
			len += 1;

		if (len > 0 && len < 4) {
			System.out.println("skipping");
			return null;
		}

		CoordinateSequence coords = csf.create(len, 2);
		for (int i = 0; i < xs.size(); i++) {
			coords.setOrdinate(i, 0, xs.get(i));
			coords.setOrdinate(i, 1, ys.get(i));
		}
		if (!doublePoint) {
			coords.setOrdinate(len - 1, 0, xs.get(0));
			coords.setOrdinate(len - 1, 1, ys.get(0));
		}
		LinearRing ring = factory.createLinearRing(coords);
		return ring;
	}

	/**
	 * Create a LinearRing from two lists of coordinates. The i'th point of the
	 * ring is represented by (xs[i], ys[i]).
	 * 
	 * @param xs
	 *            the list of x-coordinates.
	 * @param ys
	 *            the list of y-coordinates.
	 * @param doublePoint
	 *            whether the first point is explicitly contained in the lists a
	 *            second time as the last point.
	 * @return the constructed ring.
	 */
	public static LinearRing toLinearRing(TDoubleList xs, TDoubleList ys,
			boolean doublePoint)
	{
		GeometryFactory factory = new GeometryFactory();
		CoordinateSequenceFactory csf = factory.getCoordinateSequenceFactory();
		int len = xs.size();
		if (!doublePoint)
			len += 1;

		if (len > 0 && len < 4) {
			System.out.println("skipping");
			return null;
		}

		CoordinateSequence coords = csf.create(len, 2);
		for (int i = 0; i < xs.size(); i++) {
			coords.setOrdinate(i, 0, xs.get(i));
			coords.setOrdinate(i, 1, ys.get(i));
		}
		if (!doublePoint) {
			coords.setOrdinate(len - 1, 0, xs.get(0));
			coords.setOrdinate(len - 1, 1, ys.get(0));
		}
		LinearRing ring = factory.createLinearRing(coords);
		return ring;
	}

}
