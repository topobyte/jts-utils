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
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.jts.utils.JtsHelper;

public class RectangleGeneratorAxis implements RectangleGenerator
{

	final static Logger logger = LoggerFactory
			.getLogger(RectangleGeneratorAxis.class);

	private boolean horizontal;
	private double safetyFactor = 1.1;
	private Envelope envelope;
	private Envelope safeBox;
	private double rmedian;

	private double split;
	private Random random = null;
	private double span = 0.1;
	private double spanIncrease = 1.01;

	private int tries = 0;

	public RectangleGeneratorAxis(Geometry geometry, boolean horizontal)
	{
		this.horizontal = horizontal;
		envelope = geometry.getEnvelopeInternal();
		double width = envelope.getWidth();
		double height = envelope.getHeight();
		double safeH = ((width * safetyFactor) - width) / 2;
		double safeV = ((height * safetyFactor) - height) / 2;
		safeBox = new Envelope(envelope.getMinX() - safeH, envelope.getMaxX()
				+ safeH, envelope.getMinY() - safeV, envelope.getMaxY() + safeV);

		List<Double> values = new ArrayList<>();
		Coordinate[] coordinates = geometry.getCoordinates();
		for (int i = 0; i < coordinates.length; i++) {
			values.add(horizontal ? coordinates[i].x : coordinates[i].y);
		}
		Collections.sort(values);
		double median = values.get(values.size() / 2);
		if (horizontal) {
			rmedian = (median - envelope.getMinX()) / envelope.getWidth();
		} else {
			rmedian = (median - envelope.getMinY()) / envelope.getHeight();
		}
		split = rmedian;
	}

	private List<Polygon> createBoxes()
	{
		Envelope e1, e2;
		if (horizontal) {
			double x = envelope.getMinX() + envelope.getWidth() * split;
			e1 = new Envelope(safeBox.getMinX(), x, safeBox.getMinY(),
					safeBox.getMaxY());
			e2 = new Envelope(x, safeBox.getMaxX(), safeBox.getMinY(),
					safeBox.getMaxY());
		} else {
			double y = envelope.getMinY() + envelope.getHeight() * split;
			e1 = new Envelope(safeBox.getMinX(), safeBox.getMaxX(),
					safeBox.getMinY(), y);
			e2 = new Envelope(safeBox.getMinX(), safeBox.getMaxX(), y,
					safeBox.getMaxY());
		}
		List<Polygon> boxes = new ArrayList<>();
		boxes.add(JtsHelper.toGeometry(e1));
		boxes.add(JtsHelper.toGeometry(e2));
		return boxes;
	}

	@Override
	public List<Polygon> createRectangles()
	{
		if (tries++ > 0) {
			changeValues();
		}
		return createBoxes();
	}

	private void changeValues()
	{
		// try a different split position
		if (random == null) {
			random = new Random();
		}
		split = rmedian + random.nextDouble() * span - span / 2;
		logger.info("Trying with different split position: " + split);
		if (span * spanIncrease < 0.5) {
			span *= spanIncrease;
			logger.info("Increasing span to: " + span);
		}
	}

}
