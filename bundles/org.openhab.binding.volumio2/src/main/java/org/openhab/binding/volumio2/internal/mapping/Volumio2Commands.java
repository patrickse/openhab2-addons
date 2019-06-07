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

package org.openhab.binding.volumio2.internal.mapping;

/**
 * @see https://github.com/volumio/Volumio2-UI/blob/master/src/app/services/player.service.js
 * @see https://github.com/volumio/Volumio2/blob/master/app/plugins/user_interface/websocket/index.js
 *
 * @author Patrick Sernetz - Initial Contribution
 *
 */
public class Volumio2Commands {

    /* Player Status */

    public static final String GET_STATE = "getState";

    /* Player Controls */

    public static final String PLAY = "play";

    public static final String PAUSE = "pause";

    public static final String STOP = "stop";

    public static final String PREVIOUS = "prev";

    public static final String NEXT = "next";

    public static final String SEEK = "seek";

    public static final String RANDOM = "setRandom";

    public static final String REPEAT = "setRepeat";

    /* Search */

    public static final String SEARCH = "search";

    /* Volume */

    public static final String VOLUME = "volume";

    public static final String MUTE = "mute";

    public static final String UNMUTE = "unmute";

    /* MultiRoom */

    public static final String GET_MULTIROOM_DEVICES = "getMultiRoomDevices";

    /* Queue */

    /**
     * Replace the complete queue and play add/play the delivered entry.
     */
    public static final String REPLACE_AND_PLAY = "replaceAndPlay";

    public static final String ADD_PLAY = "addPlay";

    public static final String CLEAR_QUEUE = "clearQueue";

    /* ... */
    public static final String SHUTDOWN = "shutdown";

    public static final String REBOOT = "reboot";

    public static final String PLAY_PLAYLIST = "playPlaylist";

    public static final String PLAY_FAVOURITES = "playFavourites";

    public static final String PLAY_RADIO_FAVOURITES = "playRadioFavourites";

}
