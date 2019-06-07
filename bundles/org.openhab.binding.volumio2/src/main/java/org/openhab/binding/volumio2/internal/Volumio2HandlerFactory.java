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

package org.openhab.binding.volumio2.internal;

import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerFactory;
import org.openhab.binding.volumio2.handler.Volumio2Handler;
import org.osgi.service.component.annotations.Component;

import java.util.Collections;
import java.util.Set;

import static org.openhab.binding.volumio2.Volumio2BindingConstants.THING_TYPE_VOLUMIO2;

/**
 * The {@link Volumio2HandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Patrick Sernetz - Initial Contribution
 */
@Component(configurationPid = "binding.volumio2", service = ThingHandlerFactory.class)
public class Volumio2HandlerFactory extends BaseThingHandlerFactory {

//    private static final Logger log = LoggerFactory.getLogger(Volumio2HandlerFactory.class);

    private final static Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Collections.singleton(THING_TYPE_VOLUMIO2);

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    @Override
    protected ThingHandler createHandler(Thing thing) {

        ThingTypeUID thingTypeUID = thing.getThingTypeUID();
        //callbackUrl = createCallbackUrl();
        if (thingTypeUID.equals(THING_TYPE_VOLUMIO2)) {

            // Initialize Handler
            Volumio2Handler handler = new Volumio2Handler(thing);

            return handler;
        }

        return null;
    }
}
