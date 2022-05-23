package org.example.module;

import io.venuu.vuu.api.TableDef;
import io.venuu.vuu.core.module.DefaultModule;
import io.venuu.vuu.core.module.ModuleFactory;
import io.venuu.vuu.core.module.ViewServerModule;
import io.venuu.vuu.core.table.Columns;

import java.util.ArrayList;

import static java.util.Arrays.asList;
import static scala.jdk.javaapi.CollectionConverters.asScala;

public class MyExampleModule extends DefaultModule {

    private final String NAME = "MY_MOD";

    public ViewServerModule create(){
        return ModuleFactory.withNamespace(NAME)
                .addTable(TableDef.apply(
                        "myTable",
                        "id",
                        Columns.fromNames(asScala(asList("id:String", "foo:String", "myInt:Int")).toSeq()),
                        asScala(new ArrayList<String>()).toSeq()
                ),
                    (table, vs) -> new MyExampleProvider(table)
                ).asModule();
    }
}
