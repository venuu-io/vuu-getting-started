package org.example.module;

import io.venuu.vuu.core.table.DataTable;
import io.venuu.vuu.provider.Provider;

public class MyExampleProvider implements Provider {

    private final DataTable table;

    public MyExampleProvider(final DataTable table){
        this.table = table;
    }

    @Override
    public void doStart() {

    }

    @Override
    public void doStop() {

    }

    @Override
    public void doInitialize() {

    }

    @Override
    public void doDestroy() {

    }

    @Override
    public String lifecycleId() {
        return null;
    }

    @Override
    public String toString() {
        return Provider.super.toString();
    }

    @Override
    public void subscribe(String key) {

    }
}
