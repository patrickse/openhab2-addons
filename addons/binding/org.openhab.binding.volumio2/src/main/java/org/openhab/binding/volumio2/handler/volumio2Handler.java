/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.volumio2.handler;

import static org.openhab.binding.volumio2.volumio2BindingConstants.*;

import java.net.URISyntaxException;
import java.util.Collection;

import org.eclipse.smarthome.config.discovery.DiscoveryListener;
import org.eclipse.smarthome.config.discovery.DiscoveryResult;
import org.eclipse.smarthome.config.discovery.DiscoveryService;
import org.eclipse.smarthome.core.library.types.NextPreviousType;
import org.eclipse.smarthome.core.library.types.PercentType;
import org.eclipse.smarthome.core.library.types.PlayPauseType;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.json.JSONException;
import org.json.JSONObject;
import org.openhab.binding.volumio2.volumio2BindingConstants;
import org.openhab.binding.volumio2.internal.Volumio2Service;
import org.openhab.binding.volumio2.internal.config.Volumio2Config;
import org.openhab.binding.volumio2.internal.json.VolumioJsonObject;
import org.openhab.binding.volumio2.internal.mapping.Volumio2PlayerStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * The {@link volumio2Handler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Patrick Sernetz - Initial contribution
 */
public class volumio2Handler extends BaseThingHandler implements DiscoveryListener {

    private Logger logger = LoggerFactory.getLogger(volumio2Handler.class);
    private Volumio2Config config;
    private Volumio2Service volumio;

    public volumio2Handler(Thing thing) {
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
                    logger.error("Unknown channel: {}", channelUID.getId());
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
        logger.debug("PlayerVolume: {}", command);

        int level = ((PercentType) command).intValue();

        volumio.setVolume(level);
    }

    private void handlePlaybackCommands(Command command) {

        logger.debug("PlayerCommand: {}", command);

        if (command == PlayPauseType.PLAY) {
            volumio.play();
        }
        if (command == PlayPauseType.PAUSE) {
            volumio.pause();
        }
        if (command == NextPreviousType.NEXT) {
            volumio.next();
        }
        if (command == NextPreviousType.PREVIOUS) {
            volumio.previous();
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
                    logger.debug("updateStatus(ThingStatus.ONLINE)");
                    updateStatus(ThingStatus.ONLINE);
                    volumio.getState();
                }
            });

            volumio.addEventEmitter(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... arg0) {
                    logger.debug("updateStatus(ThingStatus.OFFLINE)");
                    updateStatus(ThingStatus.OFFLINE);
                }
            });

            volumio.addEventEmitter("pushState", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    try {
                        VolumioJsonObject obj = new VolumioJsonObject((JSONObject) args[0]);

                        if (obj.getStringType("title") != null) {
                            updateState(volumio2BindingConstants.CHANNEL_TITLE, obj.getStringType("title"));
                        }

                        if (obj.getStringType("artist") != null) {
                            updateState(volumio2BindingConstants.CHANNEL_ARTIST, obj.getStringType("artist"));
                        }

                        if (obj.getStringType("album") != null) {
                            updateState(volumio2BindingConstants.CHANNEL_ALBUM, obj.getStringType("album"));
                        }

                        if (obj.getStringType("volume") != null) {
                            updateState(volumio2BindingConstants.CHANNEL_VOLUME, new PercentType(obj.getInt("volume")));
                        }

                        if (obj.getStringType("status") != null) {
                            updateState(volumio2BindingConstants.CHANNEL_PLAYER,
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
                        logger.error("Could not get title: {}", e);
                    }

                }
            });

            volumio.connect();

        } catch (URISyntaxException e) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
                    "Cannot connect to device URI is not valid");
            logger.error("Cannot connect to device URI is not valid");
            e.printStackTrace();
        }

    }

    @Override
    public void dispose() {
        volumio.disconnect();
    }

    @Override
    public void thingDiscovered(DiscoveryService source, DiscoveryResult result) {

    }

    @Override
    public void thingRemoved(DiscoveryService source, ThingUID thingUID) {

    }

    @Override
    public Collection<ThingUID> removeOlderResults(DiscoveryService source, long timestamp,
            Collection<ThingTypeUID> thingTypeUIDs) {

        return null;
    }
}
