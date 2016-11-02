package org.openhab.binding.volumio2.internal.mapping;

import org.eclipse.smarthome.core.library.types.PlayPauseType;

public class Volumio2PlayerStatus {

    public static PlayPauseType mapStatus(String status) {

        PlayPauseType playPauseStatus = null;

        switch (status) {
            case VolumioStatus.PLAY:
                playPauseStatus = PlayPauseType.PLAY;
                break;
            case VolumioStatus.PAUSE:
                playPauseStatus = PlayPauseType.PAUSE;
                break;
            default:
                playPauseStatus = PlayPauseType.PAUSE;
        }

        return playPauseStatus;

    }

}
