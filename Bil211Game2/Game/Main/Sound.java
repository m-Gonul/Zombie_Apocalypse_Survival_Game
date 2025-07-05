package Bil211Game2.Game.Main;

import java.net.URL;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;

public class Sound implements Runnable {
    // Sound Thread ve durumu
    private Thread soundThread;
    private boolean running = false;
    
    // Komut kuyrukları
    private BlockingQueue<SoundCommand> commandQueue = new LinkedBlockingQueue<>();
    
    // Ses URL'leri
    private URL[] soundURL = new URL[30];
    
    // Müzik için Clip
    private Clip musicClip;
    
    // Ses ayarları
    private float volume = 0.5f;
    
    // Ses sabitleri
    public static final int TITLE_SCREEN_MUSIC = 0;
    public static final int PHASE1_MUSIC = 1;
    public static final int PHASE3_MUSIC = 2;
    public static final int PHASE5_MUSIC = 3;
    public static final int PHASE7_MUSIC = 4;
    public static final int PHASE10_MUSIC = 5;
    
    // Ses efektleri
    public static final int ACID_ZOMBIE_ACID_SOUND = 13;
    public static final int PISTOL_SOUND = 14;
    public static final int RIFLE_SOUND = 15;
    public static final int SHOTGUN_SOUND = 16;
    public static final int SNIPER_SOUND = 17;
    public static final int ROCKET_SOUND = 18;
    public static final int ROCKET_BANG_SOUND = 19;
    public static final int ZOMBIE_HURT = 20;
    public static final int PLAYER_HURT = 21;
    public static final int FANFARE_SOUND = 22;

    public Sound() {
        // Ses dosyalarını yükle
        loadSounds();
        
        // Sound thread'i başlat
        startSoundThread();
    }
    
    private void loadSounds() {
        soundURL[TITLE_SCREEN_MUSIC] = getClass().getResource("/Bil211Game2/Resources/Sound/Music/Halls of the Undead - Kevin MacLeod.wav");
        soundURL[PHASE1_MUSIC] = getClass().getResource("/Bil211Game2/Resources/Sound/Music/TheWasteland.wav");
        soundURL[PHASE3_MUSIC] = getClass().getResource("/Bil211Game2/Resources/Sound/Music/TheWasteland.wav");
        soundURL[PHASE5_MUSIC] = getClass().getResource("/Bil211Game2/Resources/Sound/Music/TheWasteland.wav");
        soundURL[PHASE7_MUSIC] = getClass().getResource("/Bil211Game2/Resources/Sound/Music/TheWasteland.wav");
        soundURL[PHASE10_MUSIC] = getClass().getResource("/Bil211Game2/Resources/Sound/Music/TheWasteland.wav");

        soundURL[ACID_ZOMBIE_ACID_SOUND] = getClass().getResource("/Bil211Game2/Resources/Sound/SE/315846__gneube__zombie-roar.wav");
        soundURL[PISTOL_SOUND] = getClass().getResource("/Bil211Game2/Resources/Sound/SE/627087clutvhsilenced-pistol-shot.wav");
        soundURL[RIFLE_SOUND] = getClass().getResource("/Bil211Game2/Resources/Sound/SE/404561__superphat__assaultrifle2.wav");
        soundURL[SHOTGUN_SOUND] = getClass().getResource("/Bil211Game2/Resources/Sound/SE/564480__lumikon__shotgun-shot-sfx.wav");
        soundURL[SNIPER_SOUND] = getClass().getResource("/Bil211Game2/Resources/Sound/SE/182051qubodupsniper-rifle-shot-sound-effect.wav");
        soundURL[ROCKET_SOUND] = getClass().getResource("/Bil211Game2/Resources/Sound/SE/517169__mrthenoronha__rocket-launcher-1-8-bit.wav");
        soundURL[ROCKET_BANG_SOUND] = getClass().getResource("/Bil211Game2/Resources/Sound/SE/268557cydonexplosion001.wav");
        soundURL[ZOMBIE_HURT] = getClass().getResource("/Bil211Game2/Resources/Sound/SE/396797__scorpion67890__mutant-scream.wav");
        soundURL[PLAYER_HURT] = getClass().getResource("/Bil211Game2/Resources/Sound/SE/262279__dirtjm__grunts-male.wav");
        soundURL[FANFARE_SOUND] = getClass().getResource("/Bil211Game2/Resources/Sound/SE/401144fullmetaljedipiece-of-shred.wav");
    }
    
    // Thread'i başlat
    private void startSoundThread() {
        if (soundThread == null || !soundThread.isAlive()) {
            running = true;
            soundThread = new Thread(this);
            soundThread.setDaemon(true);
            soundThread.start();
        }
    }
    
    // Sound thread'inin çalışacağı metot
    @Override
    public void run() {
        try {
            while (running) {
                try {
                    // Kuyruktan bir komut al ve işle
                    SoundCommand command = commandQueue.take();
                    processCommand(command);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Komut işleme
    private void processCommand(SoundCommand command) {
        try {
            switch (command.type) {
                case LOAD_MUSIC:
                    loadMusic(command.soundIndex);
                    break;
                case PLAY_EFFECT:
                    playEffect(command.soundIndex);
                    break;
                case PLAY_MUSIC:
                    playMusic();
                    break;
                case LOOP_MUSIC:
                    loopMusic();
                    break;
                case STOP_MUSIC:
                    stopMusic();
                    break;
                case SET_VOLUME:
                    setVolumeInternal(command.volumeLevel);
                    break;
                case CLEANUP:
                    cleanupResources();
                    running = false; // Thread'i durdur
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Müzik yükleme
    private void loadMusic(int soundIndex) {
        try {
            if (musicClip != null) {
                musicClip.stop();
                musicClip.close();
                musicClip = null;
            }
            
            AudioInputStream ain = AudioSystem.getAudioInputStream(soundURL[soundIndex]);
            musicClip = AudioSystem.getClip();
            musicClip.open(ain);
            
            // Ses seviyesini ayarla
            applyVolumeToClip(musicClip);
            
            // Stream'i kapat
            ain.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Efekt çalma
    private void playEffect(int soundIndex) {
        try {
            Clip effectClip = AudioSystem.getClip();
            AudioInputStream ain = AudioSystem.getAudioInputStream(soundURL[soundIndex]);
            effectClip.open(ain);
            
            // Ses seviyesini ayarla
            applyVolumeToClip(effectClip);
            
            // Tamamlandığında kaynakları serbest bırak
            effectClip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    effectClip.close();
                }
            });
            
            // Efekti çal
            effectClip.start();
            
            // Stream'i kapat
            ain.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Ses seviyesini Clip'e uygula
    private void applyVolumeToClip(Clip clip) {
        try {
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
                gainControl.setValue(dB);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Müziği çal
    private void playMusic() {
        if (musicClip != null) {
            musicClip.setFramePosition(0);
            musicClip.start();
        }
    }
    
    // Müziği döngüye al
    private void loopMusic() {
        if (musicClip != null) {
            musicClip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
    
    // Müziği durdur
    private void stopMusic() {
        if (musicClip != null) {
            musicClip.stop();
        }
    }
    
    // Ses seviyesini ayarla
    private void setVolumeInternal(float level) {
        volume = level;
        if (musicClip != null) {
            applyVolumeToClip(musicClip);
        }
    }
    
    // Kaynakları temizle
    private void cleanupResources() {
        if (musicClip != null) {
            musicClip.stop();
            musicClip.close();
            musicClip = null;
        }
    }
    
    // -- PUBLIC API METHODS --
    
    // Müzik yükle (GamePanel'ın çağıracağı public metot)
    public void setFile(int i) {
        try {
            commandQueue.put(new SoundCommand(CommandType.LOAD_MUSIC, i));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    // Ses efekti çal
    public void playSE(int i) {
        try {
            commandQueue.put(new SoundCommand(CommandType.PLAY_EFFECT, i));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    // Müziği çal
    public void play() {
        try {
            commandQueue.put(new SoundCommand(CommandType.PLAY_MUSIC));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    // Müziği döngüye al
    public void loop() {
        try {
            commandQueue.put(new SoundCommand(CommandType.LOOP_MUSIC));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    // Müziği durdur
    public void stop() {
        try {
            commandQueue.put(new SoundCommand(CommandType.STOP_MUSIC));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    // Ses seviyesini ayarla
    public void setVolume(float volume) {
        try {
            commandQueue.put(new SoundCommand(CommandType.SET_VOLUME, volume));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    // Tüm kaynakları temizle ve thread'i durdur
    public void cleanup() {
        try {
            commandQueue.put(new SoundCommand(CommandType.CLEANUP));
            // Thread'in kapanmasını bekle
            if (soundThread != null) {
                soundThread.join(1000); // En fazla 1 saniye bekle
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    // -- HELPER CLASSES --
    
    // Komut tipleri
    private enum CommandType {
        LOAD_MUSIC, PLAY_EFFECT, PLAY_MUSIC, LOOP_MUSIC, STOP_MUSIC, SET_VOLUME, CLEANUP
    }
    
    // Komut sınıfı
    private class SoundCommand {
        CommandType type;
        int soundIndex;
        float volumeLevel;
        
        // Standart komut constructor
        SoundCommand(CommandType type) {
            this.type = type;
        }
        
        // Ses indeksi ile komut constructor
        SoundCommand(CommandType type, int soundIndex) {
            this.type = type;
            this.soundIndex = soundIndex;
        }
        
        // Ses seviyesi ile komut constructor
        SoundCommand(CommandType type, float volumeLevel) {
            this.type = type;
            this.volumeLevel = volumeLevel;
        }
    }
}