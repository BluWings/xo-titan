package com.puresoltechnologies.xo.titan.impl;

import java.util.Collection;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;

import com.buschmais.cdo.api.CdoException;
import com.buschmais.cdo.spi.datastore.Datastore;
import com.buschmais.cdo.spi.datastore.DatastoreMetadataFactory;
import com.buschmais.cdo.spi.metadata.type.TypeMetadata;
import com.puresoltechnologies.xo.titan.impl.metadata.TitanNodeMetadata;
import com.puresoltechnologies.xo.titan.impl.metadata.TitanRelationMetadata;
import com.thinkaurelius.titan.core.TitanFactory;
import com.thinkaurelius.titan.core.TitanGraph;

/**
 * https://github.com/thinkaurelius/titan/wiki/Using-Cassandra
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public class TitanCassandraStore
		implements
		Datastore<TitanStoreSession, TitanNodeMetadata, String, TitanRelationMetadata, String> {

	private TitanGraph titanGraph = null;

	private final String host;
	private final int port;
	private final String keyspace;

	public TitanCassandraStore(String host, int port, String path) {
		this.host = host;
		this.port = port;
		while ((path != null) && (path.startsWith("/"))) {
			path = path.substring(1);
		}
		if ((path == null) || (path.isEmpty())) {
			// Titan will use its default keyspace.
			keyspace = null;
		} else {
			String[] list = path.split("/");
			if (list.length > 1) {
				throw new CdoException(
						"The path part of the URI contains more than one element.");
			}
			keyspace = list[0];
		}
	}

	public final TitanGraph getTitanGraph() {
		return titanGraph;
	}

	@Override
	public DatastoreMetadataFactory<TitanNodeMetadata, String, TitanRelationMetadata, String> getMetadataFactory() {
		return new TitanMetadataFactory();
	}

	@Override
	public void init(Collection<TypeMetadata> registeredMetadata) {
		Configuration configuration = new BaseConfiguration();
		configuration.setProperty("storage.backend", "cassandra");
		configuration.setProperty("storage.hostname", host);
		if (port > 0) {
			configuration.setProperty("storage.port", port);
		}
		if (keyspace != null) {
			configuration.setProperty("storage.keyspace", keyspace);
		}
		titanGraph = TitanFactory.open(configuration);
	}

	@Override
	public TitanStoreSession createSession() {
		return new TitanStoreSession(titanGraph);
	}

	@Override
	public void close() {
		titanGraph.shutdown();
		titanGraph = null;
	}
}
