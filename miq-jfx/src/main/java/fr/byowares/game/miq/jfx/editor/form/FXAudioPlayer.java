/*
 * Copyright BYOWares
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.byowares.game.miq.jfx.editor.form;

import atlantafx.base.controls.ProgressSliderSkin;
import atlantafx.base.controls.ToggleSwitch;
import atlantafx.base.theme.Styles;
import fr.byowares.game.miq.core.model.song.Song;
import fr.byowares.game.miq.jfx.audio.AudioPlayer;
import fr.byowares.game.miq.jfx.i18n.I18NMIQ;
import fr.byowares.game.utils.jfx.Resources;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignA;
import org.kordamp.ikonli.materialdesign2.MaterialDesignP;
import org.kordamp.ikonli.materialdesign2.MaterialDesignV;

import java.util.List;
import java.util.function.Consumer;

/**
 * Graphical elements to allow to choose a source, and play it.
 *
 * @since XXX
 */
public class FXAudioPlayer {
    private static final String NB_SEC = "1";
    private static final Duration ONE_SECOND = Duration.seconds(Double.parseDouble(NB_SEC));

    private final Song song;
    private final HBox root;

    private final ToggleSwitch sourceToggle;
    private final Button bLoad;
    private final VolumeSlider audioVolumeSlider;
    private final VolumeSlider voiceVolumeSlider;
    private final VolumeSlider musicVolumeSlider;
    private final List<VolumeSlider> volumeSliders;

    private final FontIcon iconPlay;
    private final FontIcon iconPause;
    private final Button bPlayPause;
    private final Button bStart;
    private final Button bBackward;
    private final Button bForward;
    private final Button bEnd;

    private final Slider timeSlider;
    private boolean isTimeSliderBeingManuallyUpdated;
    private double timeSliderManualValue;

    private AudioPlayer player;

    /**
     * @param song The song used to instantiate this Player.
     */
    FXAudioPlayer(final Song song) {
        final I18NMIQ i18n = I18NMIQ.get();
        this.song = song;
        this.root = new HBox(2d);

        this.sourceToggle = new ToggleSwitch();
        this.sourceToggle.getStylesheets().add(Resources.CSS_SMALL_TOGGLE_SWITCH);
        i18n.bind(this.sourceToggle.textProperty(), "player.source");

        this.bLoad = new Button(null, new FontIcon(BootstrapIcons.ARROW_COUNTERCLOCKWISE));
        this.bLoad.getStyleClass().addAll(Styles.SMALL, Styles.FLAT);
        this.bLoad.setTooltip(new Tooltip());
        i18n.bind(this.bLoad.getTooltip().textProperty(), "player.load");

        final HBox sourceHbox = new HBox(5d, this.sourceToggle, this.bLoad);
        sourceHbox.setAlignment(Pos.CENTER_LEFT);

        this.iconPlay = new FontIcon(BootstrapIcons.PLAY_FILL);
        this.iconPause = new FontIcon(BootstrapIcons.PAUSE_FILL);
        this.bPlayPause = newFlatButton(this.iconPlay, "player.play");
        this.bStart = newFlatButton(new FontIcon(BootstrapIcons.SKIP_START_FILL), "player.move_start");
        this.bBackward = newFlatButton(new FontIcon(BootstrapIcons.SKIP_BACKWARD_FILL), "player.move_backward", NB_SEC);
        this.bForward = newFlatButton(new FontIcon(BootstrapIcons.SKIP_FORWARD_FILL), "player.move_forward", NB_SEC);
        this.bEnd = newFlatButton(new FontIcon(BootstrapIcons.SKIP_END_FILL), "player.move_end");

        this.bPlayPause.setOnAction(e -> {
            if (this.bPlayPause.getGraphic() == this.iconPlay) {
                this.player.play();
                this.bPlayPause.setGraphic(this.iconPause);
                i18n.bind(this.bPlayPause.getTooltip().textProperty(), "player.pause");
            } else {
                this.player.pause();
                this.bPlayPause.setGraphic(this.iconPlay);
                i18n.bind(this.bPlayPause.getTooltip().textProperty(), "player.play");
            }
        });
        this.bStart.setOnAction(e -> this.player.setPosition(Duration.ZERO));
        this.bBackward.setOnAction(e -> this.player.moveBackward(ONE_SECOND));
        this.bForward.setOnAction(e -> this.player.moveForward(ONE_SECOND));
        this.bEnd.setOnAction(e -> this.player.setPosition(this.player.getDuration()));
        final HBox controlHbox = new HBox(this.bStart, this.bBackward, this.bPlayPause, this.bForward, this.bEnd);
        controlHbox.setAlignment(Pos.CENTER);

        final FontIcon voiceIcon = new FontIcon(MaterialDesignA.ACCOUNT_VOICE);
        final FontIcon pianoIcon = new FontIcon(MaterialDesignP.PIANO);

        this.voiceVolumeSlider = new VolumeSlider(this::updateVoiceVolume, this::setVoiceMute, voiceIcon);
        this.musicVolumeSlider = new VolumeSlider(this::updateMusicVolume, this::setMusicMute, pianoIcon);
        this.audioVolumeSlider = new VolumeSlider(this::updateVoiceVolume, this::setVoiceMute, null);
        this.volumeSliders = List.of(this.voiceVolumeSlider, this.musicVolumeSlider, this.audioVolumeSlider);

        this.timeSlider = new Slider();
        this.timeSlider.setSkin(new ProgressSliderSkin(this.timeSlider));

        this.timeSlider.valueChangingProperty().addListener((obs, ov, nv) -> {
            this.isTimeSliderBeingManuallyUpdated = nv;
            if (!this.isTimeSliderBeingManuallyUpdated) { // User action has just ended
                this.player.setPosition(Duration.seconds(this.timeSliderManualValue));
            }
        });
        this.timeSlider.valueProperty().addListener((obs, ov, nv) -> {
            if (this.isTimeSliderBeingManuallyUpdated) this.timeSliderManualValue = nv.doubleValue();
            else {
                final double change = Math.abs(nv.doubleValue() - ov.doubleValue());
                if (change > ONE_SECOND.toSeconds()) this.player.setPosition(Duration.seconds(nv.doubleValue()));
            }
        });
        this.root.getChildren().addAll(new VBox(sourceHbox, controlHbox, this.timeSlider));

        this.setEnablePlayer(false);

        this.sourceToggle.selectedProperty().addListener(
                (obs, ov, nv) -> this.bLoad.setDisable(this.player != null && !this.bLoad.isDisabled()));
        this.bLoad.setOnAction(e -> {
            this.volumeSliders.forEach(x -> {
                x.reset();
                this.root.getChildren().remove(x.root);
            });
            if (this.player != null) this.player.dispose();

            if (this.sourceToggle.isSelected()) {
                this.root.getChildren().addAll(this.voiceVolumeSlider.root, this.musicVolumeSlider.root);
                this.player = new AudioPlayer(this.timeSlider, this.song.getVoiceSource(), this.song.getMusicSource());

            } else {
                this.root.getChildren().add(this.audioVolumeSlider.root);
                this.player = new AudioPlayer(this.timeSlider, this.song.getAudioSource(), null);

            }
            this.setEnablePlayer(true);
            this.bPlayPause.setGraphic(this.iconPlay);
            this.bLoad.setDisable(true);
        });

        final boolean hasAudioSource = song.getAudioSource() != null;
        final boolean hasVoiceAndMusicSources = song.getVoiceSource() != null && song.getMusicSource() != null;
        this.sourceToggle.setDisable(!(hasAudioSource && hasVoiceAndMusicSources));

        if (hasAudioSource) {
            this.sourceToggle.setSelected(false);
            this.bLoad.setDisable(false);
        }
        if (hasVoiceAndMusicSources) {
            this.sourceToggle.setSelected(true);
            this.bLoad.setDisable(false);
        }
    }

    private static Button newFlatButton(
            final FontIcon icon,
            final String i18nKey,
            final Object... args
    ) {
        final Button button = new Button(null, icon);
        button.getStyleClass().add(Styles.FLAT);
        button.setTooltip(new Tooltip());
        I18NMIQ.get().bind(button.getTooltip().textProperty(), i18nKey, args);
        return button;
    }

    private void setEnablePlayer(final boolean enable) {
        this.bPlayPause.setDisable(!enable);
        this.bStart.setDisable(!enable);
        this.bBackward.setDisable(!enable);
        this.bForward.setDisable(!enable);
        this.bEnd.setDisable(!enable);

        this.timeSlider.setDisable(!enable);
    }

    private void setVoiceMute(final boolean mute) {
        this.player.setVoiceMute(mute);
    }

    private void setMusicMute(final boolean mute) {
        this.player.setMusicMute(mute);
    }

    /** @return The root containing all graphical elements. */
    HBox getRoot() {
        return this.root;
    }

    private void updateVoiceVolume(
            final ObservableValue<? extends Number> obs,
            final Number ov,
            final Number nv
    ) {
        this.player.setVoiceVolume(nv.doubleValue() / VolumeSlider.MAX_VOLUME);
    }

    private void updateMusicVolume(
            final ObservableValue<? extends Number> obs,
            final Number ov,
            final Number nv
    ) {
        this.player.setMusicVolume(nv.doubleValue() / VolumeSlider.MAX_VOLUME);
    }

    /** Free all resources associated with player. */
    void dispose() {
        if (this.player != null) this.player.dispose();
    }

    /** A volume slider controller. */
    private static class VolumeSlider {

        private static final double MIN_VOLUME = 0d;
        private static final double MAX_VOLUME = 100d;
        private static final double NB_SUBDIVISION = 4.0;
        private static final double ONE_THIRD = 1d / 3d;
        private static final double TWO_THIRD = 2D * ONE_THIRD;

        private final VBox root;
        private final Slider slider;
        private final Button bVolume;
        private final FontIcon volumeHighU;
        private final FontIcon volumeMediumU;
        private final FontIcon volumeLowU;
        private final FontIcon volumeHighM;
        private final FontIcon volumeMediumM;
        private final FontIcon volumeLowM;
        private boolean mute;

        private VolumeSlider(
                final ChangeListener<Number> volumeListener,
                final Consumer<Boolean> muteConsumer,
                final FontIcon icon
        ) {
            this.slider = new Slider();
            this.slider.setOrientation(Orientation.VERTICAL);
            this.slider.getStyleClass().add(Styles.SMALL);
            this.slider.setMin(MIN_VOLUME);
            this.slider.setMax(MAX_VOLUME);
            this.slider.setValue(this.slider.getMax());
            this.slider.setMajorTickUnit((this.slider.getMax() - this.slider.getMin()) / NB_SUBDIVISION);
            this.slider.setMinorTickCount((int) NB_SUBDIVISION);
            this.slider.setShowTickMarks(false);
            this.slider.setShowTickLabels(false);
            this.slider.setSkin(new ProgressSliderSkin(this.slider));

            this.volumeHighU = new FontIcon(MaterialDesignV.VOLUME_HIGH);
            this.volumeMediumU = new FontIcon(MaterialDesignV.VOLUME_MEDIUM);
            this.volumeLowU = new FontIcon(MaterialDesignV.VOLUME_LOW);
            this.volumeHighM = new FontIcon(MaterialDesignV.VOLUME_OFF);
            this.volumeMediumM = new FontIcon(MaterialDesignV.VOLUME_VARIANT_OFF);
            this.volumeLowM = this.volumeMediumM;

            this.mute = false;
            this.bVolume = new Button(null, this.volumeHighU);
            this.bVolume.getStyleClass().add(Styles.FLAT);
            this.bVolume.setOnAction(e -> {
                this.mute = !this.mute;
                this.updateVolumeButton();
                muteConsumer.accept(this.mute);
            });
            this.bVolume.setAlignment(Pos.CENTER);

            this.slider.valueProperty().addListener(volumeListener);
            this.slider.valueProperty().addListener((obs, ov, nv) -> this.updateVolumeButton());

            this.root = new VBox(this.slider, this.bVolume);
            if (icon != null) this.root.getChildren().addFirst(icon);
            this.root.setAlignment(Pos.BOTTOM_CENTER);
        }

        private void updateVolumeButton() {
            final double ratio = this.slider.getValue() / MAX_VOLUME;
            if (ratio < ONE_THIRD) this.bVolume.setGraphic(this.mute ? this.volumeLowM : this.volumeLowU);
            else if (ratio < TWO_THIRD) this.bVolume.setGraphic(this.mute ? this.volumeMediumM : this.volumeMediumU);
            else this.bVolume.setGraphic(this.mute ? this.volumeHighM : this.volumeHighU);
        }

        private void reset() {
            this.slider.setValue(this.slider.getMax());
            this.mute = false;
            this.updateVolumeButton();
        }
    }
}
