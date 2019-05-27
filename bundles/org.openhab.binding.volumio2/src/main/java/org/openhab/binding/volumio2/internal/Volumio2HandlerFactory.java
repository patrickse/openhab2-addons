/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.volumio2.internal;

import static org.openhab.binding.volumio2.Volumio2BindingConstants.THING_TYPE_VOLUMIO2;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.audio.AudioHTTPServer;
import org.eclipse.smarthome.core.audio.AudioSink;
import org.eclipse.smarthome.core.net.HttpServiceUtil;
import org.eclipse.smarthome.core.net.NetUtil;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerFactory;
import org.openhab.binding.volumio2.handler.Volumio2Handler;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link Volumio2HandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Patrick Sernetz - Initial Contribution
 */
@Component(configurationPid = "binding.volumio2", service = ThingHandlerFactory.class)
public class Volumio2HandlerFactory extends BaseThingHandlerFactory {

    private static final Logger log = LoggerFactory.getLogger(Volumio2HandlerFactory.class);

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
