package org.openhab.binding.volumio2.internal.mapping;

import org.eclipse.smarthome.core.library.types.PlayPauseType;

public class Volumio2PlayerStatus {

    public static final String PLAY = "play";

    public static final String PAUSE = "pause";

    public static PlayPauseType mapStatus(String status) {

        PlayPauseType playPauseStatus = null;

        switch (status) {
            case PLAY:
                playPauseStatus = PlayPauseType.PLAY;
                break;
            case PAUSE:
                playPauseStatus = PlayPauseType.PAUSE;
                break;
            default:
                playPauseStatus = PlayPauseType.PAUSE;
        }

        return playPauseStatus;

    }

}
