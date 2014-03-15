package com.puresoltechnologies.xo.titan.impl;

import com.buschmais.cdo.api.CdoException;
import com.buschmais.cdo.spi.datastore.DatastoreTransaction;

public class TitanStoreTransaction implements DatastoreTransaction {

	private boolean active = false;

	@Override
	public void begin() {
		if (active) {
			throw new CdoException("There is already an active transaction.");
		}
		active = true;
	}

	@Override
	public void commit() {
		active = false;
	}

	@Override
	public void rollback() {
		active = false;
	}

	@Override
	public boolean isActive() {
		return active;
	}
}
