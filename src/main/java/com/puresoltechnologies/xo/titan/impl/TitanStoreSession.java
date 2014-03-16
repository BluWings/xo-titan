package com.puresoltechnologies.xo.titan.impl;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.buschmais.cdo.api.CdoException;
import com.buschmais.cdo.api.ResultIterator;
import com.buschmais.cdo.spi.datastore.DatastorePropertyManager;
import com.buschmais.cdo.spi.datastore.DatastoreSession;
import com.buschmais.cdo.spi.datastore.DatastoreTransaction;
import com.buschmais.cdo.spi.datastore.TypeMetadataSet;
import com.buschmais.cdo.spi.metadata.type.EntityTypeMetadata;
import com.puresoltechnologies.xo.titan.impl.metadata.TitanNodeMetadata;
import com.puresoltechnologies.xo.titan.impl.metadata.TitanRelationMetadata;
import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

public class TitanStoreSession
		implements
		DatastoreSession<Object, Vertex, TitanNodeMetadata, String, Object, Edge, TitanRelationMetadata, String> {

	private static final String XO_DISCRIMINATORS_PROPERTY = "xo_discriminators";

	private final TitanGraph titanGraph;

	public TitanStoreSession(TitanGraph titanGraph) {
		this.titanGraph = titanGraph;
	}

	@Override
	public DatastoreTransaction getDatastoreTransaction() {
		return new TitanStoreTransaction(titanGraph);
	}

	@Override
	public boolean isEntity(Object o) {
		return Vertex.class.isAssignableFrom(o.getClass());
	}

	@Override
	public boolean isRelation(Object o) {
		return Edge.class.isAssignableFrom(o.getClass());
	}

	@Override
	public Set<String> getEntityDiscriminators(Vertex vertex) {
		Object discriminatorsObject = vertex
				.getProperty(XO_DISCRIMINATORS_PROPERTY);
		if (discriminatorsObject == null) {
			throw new CdoException(
					"A vertex was found without discriminators. Does another framework alter the database?");
		}
		if (!String.class.equals(discriminatorsObject.getClass())) {
			throw new CdoException(
					"A vertex was found with discriminators property with type '"
							+ discriminatorsObject.getClass().getName()
							+ "' instead of String. Does another framework alter the database?");
		}
		String discriminatorsString = (String) discriminatorsObject;
		String[] discriminatorsArray = discriminatorsString.split(",");
		Set<String> discriminators = new HashSet<>();
		for (String discriminator : discriminatorsArray) {
			discriminators.add(discriminator);
		}
		return discriminators;
	}

	@Override
	public String getRelationDiscriminator(Edge edge) {
		return edge.getLabel();
	}

	@Override
	public Object getEntityId(Vertex vertex) {
		return vertex == null ? null : vertex.getId();
	}

	@Override
	public Object getRelationId(Edge edge) {
		return edge.getId();
	}

	@Override
	public Vertex createEntity(
			TypeMetadataSet<EntityTypeMetadata<TitanNodeMetadata>> types,
			Set<String> discriminators) {
		Vertex vertex = titanGraph.addVertex(null);
		for (EntityTypeMetadata<TitanNodeMetadata> type : types) {
			// TODO
		}
		return vertex;
	}

	@Override
	public void deleteEntity(Vertex vertex) {
		vertex.remove();
	}

	@Override
	public ResultIterator<Vertex> findEntity(
			EntityTypeMetadata<TitanNodeMetadata> type, String discriminator,
			Object value) {
		// TODO
		return null;
	}

	@Override
	public <QL> ResultIterator<Map<String, Object>> executeQuery(QL query,
			Map<String, Object> parameters) {
		// TODO
		return null;
	}

	@Override
	public void migrateEntity(Vertex vertex,
			TypeMetadataSet<EntityTypeMetadata<TitanNodeMetadata>> types,
			Set<String> discriminators,
			TypeMetadataSet<EntityTypeMetadata<TitanNodeMetadata>> targetTypes,
			Set<String> targetDiscriminators) {
		// TODO
	}

	@Override
	public void flushEntity(Vertex vertex) {
		// intentionally left empty
	}

	@Override
	public void flushRelation(Edge edge) {
		// intentionally left empty
	}

	@Override
	public DatastorePropertyManager<Vertex, Edge, ?, TitanRelationMetadata> getDatastorePropertyManager() {
		return new TitanStorePropertyManager();
	}

}
