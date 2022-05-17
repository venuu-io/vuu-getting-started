package org.example;

import io.venuu.toolbox.jmx.MetricsProvider;
import io.venuu.toolbox.jmx.MetricsProviderImpl;
import io.venuu.toolbox.lifecycle.LifecycleContainer;
import io.venuu.toolbox.time.Clock;
import io.venuu.toolbox.time.DefaultClock;
import io.venuu.vuu.core.VuuSecurityOptions;
import io.venuu.vuu.core.VuuServer;
import io.venuu.vuu.core.VuuServerConfig;
import io.venuu.vuu.core.VuuWebSocketOptions;
import io.venuu.vuu.core.module.ViewServerModule;
import io.venuu.vuu.core.module.authn.AuthNModule;
import io.venuu.vuu.core.module.metrics.MetricsModule;
import io.venuu.vuu.core.module.simul.SimulationModule;
import io.venuu.vuu.core.module.typeahead.TypeAheadModule;
import io.venuu.vuu.core.module.vui.VuiStateModule;
import io.venuu.vuu.net.AlwaysHappyLoginValidator;
import io.venuu.vuu.net.Authenticator;
import io.venuu.vuu.net.LoggedInTokenValidator;
import io.venuu.vuu.net.auth.AlwaysHappyAuthenticator;
import io.venuu.vuu.net.http.VuuHttp2ServerOptions;
import io.venuu.vuu.state.MemoryBackedVuiStateStore;
import io.venuu.vuu.state.VuiStateStore;

/**
 * Hello world!
 *
 */
public class VuuExampleMain
{
    public static void main( String[] args )
    {

        final MetricsProvider metrics = new MetricsProviderImpl();
        final Clock clock = new DefaultClock();
        final LifecycleContainer lifecycle = new LifecycleContainer(clock);

        final VuiStateStore store = new MemoryBackedVuiStateStore(100);

        //store.add(VuiState(VuiHeader("chris", "latest", "chris.latest", clock.now()), VuiJsonState("{ uiState : ['chris','foo'] }")))

        lifecycle.autoShutdownHook();

        final Authenticator authenticator = new AlwaysHappyAuthenticator();
        final LoggedInTokenValidator loginTokenValidator = new LoggedInTokenValidator();

        final VuuServerConfig config = new VuuServerConfig(
                        VuuHttp2ServerOptions.apply()
                        .withWebRoot(".")
                        .withSsl("src/main/resources/certs/cert.pem",
                                "src/main/resources/certs/key.pem")
                        .withDirectoryListings(true)
                        .withPort(8443),
                VuuWebSocketOptions.apply()
                        .withUri("websocket")
                        .withWsPort(8090),
                VuuSecurityOptions.apply()
                        .withAuthenticator(authenticator)
                        .withLoginValidator(new AlwaysHappyLoginValidator()),
                        new scala.collection.mutable.ListBuffer<ViewServerModule>().toList()
        ).withModule(SimulationModule.apply(clock, lifecycle))
         .withModule(MetricsModule.apply(clock, lifecycle, metrics))
         .withModule(VuiStateModule.apply(store, clock, lifecycle))
         .withModule(TypeAheadModule.apply(clock, lifecycle))
         .withModule(AuthNModule.apply(authenticator, loginTokenValidator, clock, lifecycle));


        final VuuServer vuuServer = new VuuServer(config, lifecycle, clock, metrics);

        //  LifecycleGraphviz("vuu", lifecycle.dependencyGraph)

        lifecycle.start();

        vuuServer.join();
    }
}
