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

package org.openhab.binding.volumio2;

import org.eclipse.smarthome.core.thing.ThingTypeUID;

/**
 * The {@link volumio2Binding} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Patrick Sernetz - Initial Contribution
 */
public class Volumio2BindingConstants {

    public static final String BINDING_ID = "volumio2";

    // List of all Thing Type UID
    public final static ThingTypeUID THING_TYPE_VOLUMIO2 = new ThingTypeUID(BINDING_ID, "player");

    // List of all Channel ids
    public final static String CHANNEL_TITLE = "title";
    public final static String CHANNEL_ARTIST = "artist";
    public final static String CHANNEL_ALBUM = "album";
    public final static String CHANNEL_VOLUME = "volume";
    public final static String CHANNEL_PLAYER = "player";
    public final static String CHANNEL_COVER_ART = "albumArt";
    public final static String CHANNEL_TRACK_TYPE = "trackType";
    public final static String CHANNEL_PLAY_RADIO_STREAM = "playRadioStream";
    public final static String CHANNEL_PLAY_PLAYLIST = "playPlaylist";
    public final static String CHANNEL_CLEAR_QUEUE = "clearQueue";
    public final static String CHANNEL_PLAY_RANDOM = "random";
    public final static String CHANNEL_PLAY_REPEAT = "repeat";
    public final static String CHANNEL_PLAY_URI = "playURI";
    public final static String CHANNEL_PLAY_FILE = "playFile";
    public final static String CHANNEL_SYSTEMCOMMAND = "systemCommand";
    public final static String CHANNEL_STOP = "stopCommand";

    // Discovery Properties

    public final static String DISCOVERY_SERVICE_TYPE = "_Volumio._tcp.local.";
    public final static String DISCOVERY_NAME_PROPERTY = "volumioName";
    public final static String DISCOVERY_UUID_PROPERTY = "UUID";

    // Config

    public final static String CONFIG_PROPERTY_HOSTNAME = "hostname";
    public final static String CONFIG_PROPERTY_PORT = "port";
    public final static String CONFIG_PROPERTY_PROTOCOL = "protocol";
    public final static String CONFIG_PROPERTY_TIMEOUT = "timeout";

}
