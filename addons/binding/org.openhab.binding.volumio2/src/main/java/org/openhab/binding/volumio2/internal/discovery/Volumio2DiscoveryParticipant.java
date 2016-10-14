package org.openhab.binding.volumio2.internal.discovery;

import static org.openhab.binding.volumio2.volumio2BindingConstants.THING_TYPE_VOLUMIO2_HOST;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import javax.jmdns.ServiceInfo;

import org.eclipse.smarthome.config.discovery.DiscoveryResult;
import org.eclipse.smarthome.config.discovery.DiscoveryResultBuilder;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.io.transport.mdns.discovery.MDNSDiscoveryParticipant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Volumio2DiscoveryParticipant implements MDNSDiscoveryParticipant {

    private static final Logger logger = LoggerFactory.getLogger(Volumio2DiscoveryParticipant.class);

    // private static final String SERVICE_TYPE = "_fbx-api._tcp.local.";
    private static final String SERVICE_TYPE = "_Volumio._tcp.local.";

    @Override
    public Set<ThingTypeUID> getSupportedThingTypeUIDs() {
        return Collections.singleton(THING_TYPE_VOLUMIO2_HOST);
    }

    @Override
    public String getServiceType() {
        return SERVICE_TYPE;
    }

    @Override
    public DiscoveryResult createResult(ServiceInfo service) {

        logger.debug("createResult: {}", service);

        if (service.getPropertyString("UUID") != null) {
            ThingUID uid = getThingUID(service);

            HashMap<String, Object> properties = new HashMap<String, Object>();
            properties.put("hostname", service.getServer());

            String volumioName = service.getPropertyString("volumioName");

            if (uid != null) {
                DiscoveryResult discoveryResult = DiscoveryResultBuilder.create(uid).withProperties(properties)
                        .withLabel(volumioName).build();
                return discoveryResult;
            } else {
                return null;
            }
        }

        return null;

    }

    @Override
    public ThingUID getThingUID(ServiceInfo service) {
        logger.debug("return new ThingUID({}, {});", THING_TYPE_VOLUMIO2_HOST, service.getPropertyString("UUID"));
        return new ThingUID(THING_TYPE_VOLUMIO2_HOST, service.getPropertyString("UUID"));
    }

}
