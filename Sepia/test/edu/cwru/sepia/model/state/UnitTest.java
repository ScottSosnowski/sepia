package edu.cwru.sepia.model.state;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class UnitTest {

	@Test
	public void testSingleTileUnitOccupiesPosition() {
		UnitTemplate template = new UnitTemplate(0);
		template.setWidth(1);
		template.setHeight(1);
		Unit unit = new Unit(template, 0);
		unit.setXPosition(1);
		unit.setYPosition(8);
		assertOnBounds(unit);
	}

	@Test
	public void testBigSquareUnitOccupiesPosition() {
		UnitTemplate template = new UnitTemplate(0);
		template.setWidth(3);
		template.setHeight(3);
		Unit unit = new Unit(template, 0);
		unit.setXPosition(4);
		unit.setYPosition(5);
		assertOnBounds(unit);
	}

	@Test
	public void testRectangularUnitOccupiesPosition() {
		UnitTemplate template = new UnitTemplate(0);
		template.setWidth(2);
		template.setHeight(1);
		Unit unit = new Unit(template, 0);
		unit.setXPosition(6);
		unit.setYPosition(6);
		assertOnBounds(unit);
	}
	
	private void assertOnBounds(Unit unit) {
		//assert that method returns true for spaces that the unit occupies
		for(int i = unit.getXPosition(); i < unit.getXPosition() + unit.getTemplate().getWidth(); i++) {
			for(int j = unit.getYPosition(); j < unit.getYPosition() + unit.getTemplate().getHeight(); j++) {
				assertThat(unit.occupiesLocation(i, j), is(true));
			}
		}
		//assert that method returns false for top and bottom
		for(int i = unit.getXPosition() - 1; i < unit.getXPosition() + unit.getTemplate().getWidth() + 1; i++) {
			assertThat(unit.occupiesLocation(i, unit.getYPosition() - 1), is(false));
			assertThat(unit.occupiesLocation(i, unit.getYPosition() + unit.getTemplate().getHeight()), is(false));
		}
		//assert that method returns false for left and right
		for(int j = unit.getYPosition() - 1; j < unit.getYPosition() + unit.getTemplate().getHeight() + 1; j++) {
			assertThat(unit.occupiesLocation(unit.getXPosition() - 1, j), is(false));
			assertThat(unit.occupiesLocation(unit.getXPosition() + unit.getTemplate().getHeight() + 1, j), is(false));
		}
	}
}
