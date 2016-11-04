package org.openhab.binding.volumio2.internal.mapping;

/**
 * @see https://github.com/volumio/Volumio2-UI/blob/master/src/app/services/player.service.js
 *
 * @author Patrick Sernetz <patrick@sernetz.com>
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

    public static final String RANDOM = "random";

    public static final String REPEAT = "repeat";

    /* Search */

    public static final String SEARCH = "search";

    /* Volume */

    public static final String VOLUME = "volume";

    public static final String MUTE = "mute";

    public static final String UNMUTE = "unmute";

    /* MultiRoom */

    public static final String GET_MULTIROOM_DEVICES = "getMultiRoomDevices";

    /* Queue */

    public static final String REPLACE_AND_PLAY = "replaceAndPlay";
    public static final String ADD_PLAY = "addPlay";

    /* ... */
    public static final String SHUTDOWN = "shutdown";

    public static final String REBOOT = "reboot";

    public static final String PLAY_PLAYLIST = "playPlaylist";

    public static final String PLAY_FAVOURITES = "playFavourites";

    public static final String PLAY_RADIO_FAVOURITES = "playRadioFavourites";

}
