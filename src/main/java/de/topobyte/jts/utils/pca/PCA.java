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

import java.util.Arrays;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;

public class PCA
{

	private Coordinate mu;
	private Coordinate e1, e2;
	private double l1, l2;

	private Geometry geometry;
	private Coordinate[] coordinates;
	private int n;
	private Coordinate[] deltas;

	public PCA(Geometry geometry)
	{
		this.geometry = geometry;

		// mu
		mu = geometry.getCentroid().getCoordinate();
		coordinates = geometry.getCoordinates();
		n = coordinates.length;

		// Subtract mu
		deltas = new Coordinate[n];
		for (int i = 0; i < n; i++) {
			Coordinate c = coordinates[i];
			double dx = c.x - mu.x;
			double dy = c.y - mu.y;
			deltas[i] = new Coordinate(dx, dy);
		}

		// Covariance matrix
		// (k11 k12)
		// (k21 k22)
		double k11 = 0, k12 = 0, k21 = 0, k22 = 0;
		for (int i = 0; i < n; i++) {
			Coordinate d = deltas[i];
			k11 += d.x * d.x;
			k22 += d.y * d.y;
			k12 += d.x * d.y;
			k21 += d.x * d.y;
		}
		k11 /= n;
		k12 /= n;
		k21 /= n;
		k22 /= n;

		// Characteristic polynom for eigenvalues l1, l2
		// l^2 - (k11 + k22) * l + k11k22 - k12k21 = 0
		// l^2 - 2a * l + b = 0
		double a = (k11 + k22) / 2;
		double b = k11 * k22 - k12 * k21;
		double root = Math.sqrt(a * a - b);
		l1 = a + root;
		l2 = a - root;

		// / Sort eigenvalues on their absolute value
		if (Math.abs(l2) > Math.abs(l1)) {
			double tmp = l1;
			l1 = l2;
			l2 = tmp;
		}

		// Eigenvectors (x1,y1) and (x2, y2)
		e1 = new Coordinate(1, (l1 - k11) / k12);
		double f1 = Math.sqrt(e1.x * e1.x + e1.y * e1.y);
		e1.x /= f1;
		e1.y /= f1;

		e2 = new Coordinate(1, (l2 - k11) / k12);
		double f2 = Math.sqrt(e2.x * e2.x + e2.y * e2.y);
		e2.x /= f2;
		e2.y /= f2;
	}

	public int getNumberOfPoints()
	{
		return n;
	}

	public Geometry getGeometry()
	{
		return geometry;
	}

	public Coordinate getMu()
	{
		return mu;
	}

	public Coordinate getEigenVector1()
	{
		return e1;
	}

	public Coordinate getEigenVector2()
	{
		return e2;
	}

	public double getEigenValue1()
	{
		return l1;
	}

	public double getEigenValue2()
	{
		return l2;
	}

	public double[] getProjectionsOn(Coordinate e)
	{
		// Project on eigenvectors
		double[] p = new double[n];
		for (int i = 0; i < n; i++) {
			Coordinate d = deltas[i];
			p[i] = e.x * d.x + e.y * d.y;
		}
		return p;
	}

	public double[] getSortedProjectionsOn(Coordinate e)
	{
		double[] p = getProjectionsOn(e);
		Arrays.sort(p);
		return p;
	}

}
