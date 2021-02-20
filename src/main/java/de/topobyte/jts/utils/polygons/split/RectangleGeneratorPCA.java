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
import java.util.Random;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.jts.utils.pca.PCA;
import de.topobyte.jts.utils.pca.PCAUtil;

public class RectangleGeneratorPCA implements RectangleGenerator
{

	final static Logger logger = LoggerFactory
			.getLogger(RectangleGeneratorPCA.class);

	private PCA pca;
	private double split = 0.5;
	private Random random = null;
	private double span = 0.1;
	private double spanIncrease = 1.01;

	private int tries = 0;

	public RectangleGeneratorPCA(Geometry geometry)
	{
		pca = new PCA(geometry);
	}

	@Override
	public List<Polygon> createRectangles()
	{
		if (tries++ > 0) {
			changeValues();
		}
		return PCAUtil.createSplitRectangles(pca, split, 1.1);
	}

	private void changeValues()
	{
		// try a different split position
		if (random == null) {
			random = new Random();
		}
		split = 0.5 + random.nextDouble() * span - span / 2;
		logger.info("Trying with different split position: " + split);
		if (span * spanIncrease < 0.5) {
			span *= spanIncrease;
			logger.info("Increasing span to: " + span);
		}
	}

}
