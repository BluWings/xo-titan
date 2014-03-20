package com.puresoltechnologies.xo.titan.test.crud;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.buschmais.cdo.api.CdoManager;
import com.buschmais.cdo.api.CdoManagerFactory;
import com.buschmais.cdo.api.ResultIterable;
import com.buschmais.cdo.api.ResultIterator;
import com.buschmais.cdo.api.bootstrap.Cdo;
import com.puresoltechnologies.xo.titan.test.AbstractXOTitanTest;
import com.puresoltechnologies.xo.titan.test.bootstrap.TestEntity;

public class StoreAndReadVerticesIT extends AbstractXOTitanTest {

	private static CdoManagerFactory cdoManagerFactory;
	private CdoManager cdoManager;

	@BeforeClass
	public static void initialize() {
		cdoManagerFactory = Cdo.createCdoManagerFactory("Titan");
	}

	@AfterClass
	public static void teardown() {
		if (cdoManagerFactory != null) {
			cdoManagerFactory.close();
		}
	}

	@Before
	public void setup() {
		cdoManager = cdoManagerFactory.createCdoManager();
	}

	@After
	public void destroy() {
		cdoManager.close();
	}

	@Test
	public void test() {
		cdoManager.currentTransaction().begin();
		TestEntity createdA = cdoManager.create(TestEntity.class);
		createdA.setName("Test");
		cdoManager.currentTransaction().commit();

		cdoManager.currentTransaction().begin();
		ResultIterable<TestEntity> aa = cdoManager.find(TestEntity.class,
				"Test");
		assertNotNull(aa);
		ResultIterator<TestEntity> iterator = aa.iterator();
		assertTrue(iterator.hasNext());
		TestEntity readA = iterator.next();
		assertNotNull(readA);
		assertEquals("Test", readA.getName());
		cdoManager.currentTransaction().rollback();
	}

}
