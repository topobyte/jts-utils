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

import java.util.Iterator;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;

/**
 * An iterator that allows to iterate over the elements of GeometryCollection.
 * Just for convenience since the GeometryCollection does not implements
 * Iterable itself.
 * 
 * @author Sebastian Kuerten (sebastian.kuerten@fu-berlin.de)
 * 
 */
public class GeometryCollectionIterator implements Iterable<Geometry>,
		Iterator<Geometry>
{

	private GeometryCollection gc = null;
	private int index = 0;
	private int n = 0;

	/**
	 * Create a new iterator.
	 * 
	 * @param gc
	 *            the collection to iterate over.
	 */
	public GeometryCollectionIterator(GeometryCollection gc)
	{
		this.gc = gc;
		n = gc.getNumGeometries();
	}

	@Override
	public Iterator<Geometry> iterator()
	{
		return this;
	}

	@Override
	public boolean hasNext()
	{
		return index < n;
	}

	@Override
	public Geometry next()
	{
		Geometry g = gc.getGeometryN(index);
		index += 1;
		return g;
	}

	@Override
	public void remove()
	{
		System.out.println("not implemented");
	}
}
