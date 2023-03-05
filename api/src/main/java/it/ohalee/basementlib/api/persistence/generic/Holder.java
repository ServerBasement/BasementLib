package it.ohalee.basementlib.api.persistence.generic;

import it.ohalee.basementlib.api.persistence.generic.connection.Connector;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public abstract class Holder {

    @Getter
    protected final Connector connector;

    public void close() {
        connector.close();
    }

}
