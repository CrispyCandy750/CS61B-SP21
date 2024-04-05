package gh2;

import edu.princeton.cs.algs4.StdAudio;
import edu.princeton.cs.algs4.StdDraw;

public class GuitarHero {

    private static final String KEYBOARD = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
    private static final double[] CONCERTs;

    static {
        CONCERTs = new double[KEYBOARD.length()];
        for (int i = 0; i < KEYBOARD.length(); i++) {
            CONCERTs[i] = 440.0 * Math.pow(2, (i - 24.0) / 12.0);
        }
    }

    public static void main(String[] args) {
        /* create all strings, for concerts of all characters in keyboard. */
        GuitarString[] strings = new GuitarString[KEYBOARD.length()];
        for (int i = 0; i < strings.length; i++) {
            strings[i] = new GuitarString(CONCERTs[i]);
        }
        /* All the Strings are empty which is static. */

        while (true) {

            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int index = KEYBOARD.indexOf(key);
                if (index != -1) {
                    /* The string starts vibrating as soon as the key is pressed. */
                    strings[index].pluck();
                }
            }

            /* compute the superposition of samples */
            /* The final sound is the sound produced by all the strings together. */
            double sample = 0;
            for (int i = 0; i < strings.length; i++) {
                sample += strings[i].sample();
            }

            /* play the sample on standard audio */
            StdAudio.play(sample);

            /* advance the simulation of each guitar string by one step */
            /* Each time a sound is produced, the string moves backward slightly. */
            for (int i = 0; i < strings.length; i++) {
                strings[i].tic();
            }
        }
    }
}

/*
* 下面说一下对这个过程的模拟：GuitarString是弦的意思
* 1. CONCERTs表示每个键模拟的弦的震动的频率，在GuitarString里面通过Sample rate / frequency 来得到buffer的大小
* 2. 一开始我们初始化所有的Strings，此时弦里面的sample都是0，这样就表示弦没有按动
* 3. 然后进入while循环，while的基本作用是取出每个弦的开头的sample，然后合成，再play（因为每次的声音都是所有弦合在一起的声音）
* 4. 然后每个弦都tic()，表示下一次要发出的声音
* 5. 由于String里面的DECAY的存在，音频会逐渐削弱，最终停止
* 6. 在while的过程中，如果按下一个键，那么就表示拨动了弦，此时对应的弦就会pluck()，表示重新震动，震动的幅度都是随机的
* */
