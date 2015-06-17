package com.MVlab.BrickBreaker.utils;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * Created by MV on 14.06.2015.
 */
public class AudioManager {
    public static final AudioManager instance = new AudioManager();
    private Music playingMusic;

    private AudioManager() {  }

    public void play(Sound sound) {
        play(sound, 1);
    }

    public void play(Sound sound, float volume) {
        play(sound, volume, 1);
    }

    public void play(Sound sound, float volume, float pitch) {
        play(sound, volume, pitch, 0);
    }

    public void play(Sound sound, float volume, float pitch, float pan) {
        if (!GamePreferences.instance.sound) return;
        sound.play(GamePreferences.instance.volSound * volume, pitch, pan);
    }

    public void play (Music music) {
        play(music, 1);
    }

    public void play (Music music, float volume) {
        stopMusic();
        playingMusic = music;
        if (GamePreferences.instance.music) {
            music.setLooping(true);
            music.setVolume(GamePreferences.instance.volMusic * volume);
            music.play();
        }
    }
    public void stopMusic () {
        if (playingMusic != null) playingMusic.stop();
    }

    public void onSettingsUpdated () {
        if (playingMusic == null) return;
        playingMusic.setVolume(GamePreferences.instance.volMusic * 0.10f);
        if (GamePreferences.instance.music) {
            if (!playingMusic.isPlaying()) playingMusic.play();
        } else {
            playingMusic.pause();
        }
    }
}
