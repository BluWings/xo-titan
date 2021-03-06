package com.puresoltechnologies.xo.titan.test.relation.typed;

import com.puresoltechnologies.xo.titan.api.annotation.EdgeDefinition.Incoming;
import com.puresoltechnologies.xo.titan.api.annotation.EdgeDefinition.Outgoing;

public interface BaseType {

	@Outgoing
	C getC();

	@Incoming
	D getD();

	int getVersion();

	void setVersion(int version);

}
