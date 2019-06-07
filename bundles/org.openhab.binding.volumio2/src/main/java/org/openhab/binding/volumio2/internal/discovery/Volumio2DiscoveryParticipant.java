/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.openhab.binding.volumio2.internal.discovery;

import static org.openhab.binding.volumio2.Volumio2BindingConstants.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jmdns.ServiceInfo;

import org.eclipse.smarthome.config.discovery.DiscoveryResult;
import org.eclipse.smarthome.config.discovery.DiscoveryResultBuilder;
import org.eclipse.smarthome.config.discovery.mdns.MDNSDiscoveryParticipant;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Patrick Sernetz - Initial Contribution
 */
@Component(service = MDNSDiscoveryParticipant.class, immediate = true)
public class Volumio2DiscoveryParticipant implements MDNSDiscoveryParticipant {

    private static final Logger log = LoggerFactory.getLogger(Volumio2DiscoveryParticipant.class);

    private final Pattern VOLUMIO_NAME_PATTERN = Pattern.compile("(.*)._Volumio._tcp.local.");

    @Override
    public Set<ThingTypeUID> getSupportedThingTypeUIDs() {
        return Collections.singleton(THING_TYPE_VOLUMIO2);
    }

    @Override
    public String getServiceType() {
        return DISCOVERY_SERVICE_TYPE;
    }

    @Override
    public DiscoveryResult createResult(ServiceInfo service) {

        ThingUID thingUID = getThingUID(service);

        if (Objects.nonNull(thingUID)) {
            if (
                    Objects.nonNull(service.getHostAddresses()) &&
                    service.getHostAddresses().length > 0 &&
                    !service.getHostAddresses()[0].isEmpty()
            ) {
                log.debug("Discoverd Volumio2 Device: {}", service);

                Map<String, Object> properties = new HashMap<>(4);
                properties.put("hostname", service.getServer());
                properties.put("port", service.getPort());
                properties.put("protocol", "http");

                return DiscoveryResultBuilder
                        .create(thingUID)
                        .withProperties(properties)
                        .withLabel(thingUID.getId())
                        .build();
            }
        }

        /**
        String volumioName = service.getPropertyString(DISCOVERY_NAME_PROPERTY);
//        String uuid = service.getPropertyString(DISCOVERY_UUID_PROPERTY);
        HashMap<String, Object> properties = new HashMap<String, Object>();
        DiscoveryResult discoveryResult = null;
        ThingUID thingUID = getThingUID(service);

        log.debug("Service Device: {}", service);
        log.debug("Thing UID: {}", thingUID);

        if (thingUID != null) {
            properties.put("hostname", service.getServer());
            properties.put("port", service.getPort());
            properties.put("protocol", "http");
            discoveryResult = DiscoveryResultBuilder.create(thingUID).withProperties(properties).withLabel(volumioName)
                    .build();

            log.debug("DiscoveryResult: {}", discoveryResult);
        }
         **/

        return null;

    }

    @Override
    public ThingUID getThingUID(ServiceInfo service) {

        log.debug("Service: {}", service);

        if (Objects.nonNull(service)) {
            Matcher m = VOLUMIO_NAME_PATTERN.matcher(service.getName());
            if (m.find()) {
                return new ThingUID(THING_TYPE_VOLUMIO2, m.group(1));
            }
        }

        return null;

        /**
        Collections.list(service.getPropertyNames()).forEach(s -> log.debug("PropertyName: {}", s));

        String volumioName = service.getPropertyString("volumioName");
        if (volumioName == null) {
            return null;
        }

        String uuid = service.getPropertyString("UUID");
        if (uuid == null) {
            return null;
        }

        String uuidAndServername = String.format("%s-%s", uuid, volumioName);
        log.debug("return new ThingUID({}, {});", THING_TYPE_VOLUMIO2, uuidAndServername);
        return new ThingUID(THING_TYPE_VOLUMIO2, uuidAndServername);
         **/
    }

}
