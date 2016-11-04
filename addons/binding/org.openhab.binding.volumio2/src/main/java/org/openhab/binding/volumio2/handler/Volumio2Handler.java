/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.volumio2.handler;

import static org.openhab.binding.volumio2.Volumio2BindingConstants.*;

import java.net.URISyntaxException;

import org.eclipse.smarthome.core.library.types.NextPreviousType;
import org.eclipse.smarthome.core.library.types.PercentType;
import org.eclipse.smarthome.core.library.types.PlayPauseType;
import org.eclipse.smarthome.core.library.types.RewindFastforwardType;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.json.JSONException;
import org.json.JSONObject;
import org.openhab.binding.volumio2.Volumio2BindingConstants;
import org.openhab.binding.volumio2.internal.Volumio2Service;
import org.openhab.binding.volumio2.internal.config.Volumio2Config;
import org.openhab.binding.volumio2.internal.json.VolumioJsonObject;
import org.openhab.binding.volumio2.internal.mapping.Volumio2PlayerStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * The {@link Volumio2Handler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Patrick Sernetz - Initial contribution
 */
public class Volumio2Handler extends BaseThingHandler {

    private Logger log = LoggerFactory.getLogger(Volumio2Handler.class);
    private Volumio2Config config;
    private Volumio2Service volumio;

    public Volumio2Handler(Thing thing) {
        super(thing);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {

        try {
            switch (channelUID.getId()) {
                case CHANNEL_PLAYER:
                    handlePlaybackCommands(command);
                    break;
                case CHANNEL_VOLUME:
                    handleVolumeCommand(command);
                    break;
                // case CHANNEL_REBOOT:
                //
                // if (command == OnOffType.ON) {
                // volumio.reboot();
                // }
                //
                // break;
                // case CHANNEL_SHUTDOWN:
                // if (command == OnOffType.ON) {
                // volumio.shutdown();
                // }
                //
                // break;

                case CHANNEL_PLAYLIST:
                    playPlaylist(command);
                    break;

                default:
                    log.error("Unknown channel: {}", channelUID.getId());
            }
        } catch (Exception e) {
            updateStatus(ThingStatus.OFFLINE);
            e.printStackTrace();
        }

        updateStatus(ThingStatus.ONLINE);

    }

    private void playPlaylist(Command command) {
        if (command instanceof StringType) {
            String playlistName = command.toString();
            volumio.playPlaylist(playlistName);
        }
    }

    private void handleVolumeCommand(Command command) {
        log.debug("PlayerVolume: {}", command);

        int level = ((PercentType) command).intValue();

        volumio.setVolume(level);
    }

    private void handlePlaybackCommands(Command command) {

        log.debug("PlayerCommand: {}", command);

        if (command instanceof PlayPauseType) {

            PlayPauseType playPauseCmd = (PlayPauseType) command;

            switch (playPauseCmd) {
                case PLAY:
                    volumio.play();
                    break;
                case PAUSE:
                    volumio.pause();
                    break;
            }

        } else if (command instanceof NextPreviousType) {

            NextPreviousType nextPreviousType = (NextPreviousType) command;

            switch (nextPreviousType) {
                case PREVIOUS:
                    volumio.previous();
                    break;
                case NEXT:
                    volumio.next();
                    break;
            }

        } else if (command instanceof RewindFastforwardType) {

            RewindFastforwardType fastforwardType = (RewindFastforwardType) command;

            switch (fastforwardType) {
                case FASTFORWARD:
                    log.warn("Not implemented yet");
                    break;
                case REWIND:
                    log.warn("Not implemented yet");
                    break;
            }

        } else {

            log.error("Command is not handled: {}", command);

        }

    }

    @Override
    public void initialize() {

        config = getConfigAs(Volumio2Config.class);

        try {
            volumio = new Volumio2Service(config.getHostname());

            volumio.addEventEmitter(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... arg0) {
                    log.debug("updateStatus(ThingStatus.ONLINE)");
                    updateStatus(ThingStatus.ONLINE);
                    volumio.getState();
                }
            });

            volumio.addEventEmitter(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... arg0) {
                    log.debug("updateStatus(ThingStatus.OFFLINE)");
                    updateStatus(ThingStatus.OFFLINE);
                }
            });

            volumio.addEventEmitter("pushState", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    try {
                        VolumioJsonObject obj = new VolumioJsonObject((JSONObject) args[0]);

                        if (obj.getStringType("title") != null) {
                            updateState(Volumio2BindingConstants.CHANNEL_TITLE, obj.getStringType("title"));
                        }

                        if (obj.getStringType("artist") != null) {
                            updateState(Volumio2BindingConstants.CHANNEL_ARTIST, obj.getStringType("artist"));
                        }

                        if (obj.getStringType("album") != null) {
                            updateState(Volumio2BindingConstants.CHANNEL_ALBUM, obj.getStringType("album"));
                        }

                        if (obj.getStringType("volume") != null) {
                            updateState(Volumio2BindingConstants.CHANNEL_VOLUME, new PercentType(obj.getInt("volume")));
                        }

                        if (obj.getStringType("status") != null) {
                            updateState(Volumio2BindingConstants.CHANNEL_PLAYER,
                                    Volumio2PlayerStatus.mapStatus(obj.getString("status")));
                        }

                        if (obj.getStringType("trackType") != null) {
                            updateState(CHANNEL_TRACK_TYPE, obj.getStringType("trackType"));
                        }

                        if (obj.getStringType("position") != null) {
                            updateState(CHANNEL_POSITION, obj.getStringType("position"));
                        }

                        if (obj.getStringType("albumart") != null) {
                            updateState(CHANNEL_ALBUM_ART, obj.getRawType("albumart"));
                        }

                    } catch (JSONException e) {
                        log.error("Could not get title: {}", e);
                    }

                }
            });

            volumio.connect();

        } catch (URISyntaxException e) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
                    "Cannot connect to device URI is not valid");
            log.error("Cannot connect to device URI is not valid");
            e.printStackTrace();
        }

    }

    @Override
    public void dispose() {
        volumio.disconnect();
    }

}
