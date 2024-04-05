package gh2;

import deque.ArrayDeque;
import deque.Deque;

//Note: This file will not compile until you complete the Deque implementations

public class GuitarString {
    /**
     * Constants. Do not change. In case you're curious, the keyword final
     * means the values cannot be changed at runtime. We'll discuss this and
     * other topics in lecture on Friday.
     */
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .996; // energy decay factor

    /* Buffer for storing sound data. */
    private Deque<Double> buffer;

    /* Create a guitar string of the given frequency.  */
    public GuitarString(double frequency) {

        /* Sample Rate / frequency is  */
        long capacity = Math.round(SR / frequency);

        buffer = new ArrayDeque<>();
        for (int i = 0; i < capacity; i++) {
            buffer.addLast(0.0);
        }
    }


    /** Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {

        int capacity = buffer.size();

        for (int i = 0; i < capacity; i++) {
            buffer.removeFirst();
            buffer.addLast(Math.random() - 0.5);
        }
    }

    /**
     * Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm.
     */
    public void tic() {
        Double front = buffer.removeFirst();
        Double rear = (front + buffer.get(0)) * 0.5 * DECAY;
        buffer.addLast(rear);
    }

    /** Return the double at the front of the buffer. */
    public double sample() {
        return buffer.get(0);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < buffer.size(); i++) {
            stringBuilder.append(buffer.get(i) + " ");
        }
        return stringBuilder.toString().trim();
    }
}

/*
* Karplus-Strong算法是一种经典的数字音频合成算法，用于模拟弦乐器（如吉他、琴）的音色。
* 该算法由Alexander Strong和Kevin Karplus于1983年提出，是一种简单而有效的物理模型算法。
* 该算法的基本思想是：
*   1. 初始化波形：首先，创建一个固定长度的缓冲区（通常表示为弦的长度），并用随机数或者其他方法填充缓冲区，以模拟弦的初始状态。
*   2. 模拟振动：然后，通过在缓冲区的前端读取采样值，并以某种规则计算后续采样值，来模拟弦的振动。
*      这通常涉及到对缓冲区中的采样值进行加权平均，并在末尾追加新的采样值，以模拟振动过程中的能量衰减和频率振动。
*   3. 重复步骤2：重复上述过程，直到需要的时长的音频被合成。
* 关于Karplus-Strong算法的一些关键点和特征：
*   1. 能量衰减：在模拟振动过程中，采样值会不断地经过一个衰减因子，使得振动逐渐减弱，模拟出弦乐器音色中的振动衰减特性。
*   2. 频率控制：振动的频率由缓冲区的长度决定，更长的缓冲区对应更低的音调，更短的缓冲区对应更高的音调。
*   3. 白噪声初始条件：通常，为了模拟弦乐器的初始状态，缓冲区会被初始化为一些随机值，以产生类似于白噪声的初始条件。
* Karplus-Strong算法的优点在于它的简单性和效率，能够以较低的计算成本产生出逼真的弦乐器音色。因此，它被广泛应用于数字音频合成、音乐教育和音乐软件等领域。
* */

/*
* 在这段代码中，`capacity` 的计算涉及到了两个变量：`SR` 和 `frequency`。
* `SR`：代表采样率（Sampling Rate），通常以赫兹（Hz）为单位。采样率表示每秒钟对声音信号进行采样的次数，常见的标准值是44100Hz，表示每秒对声音信号进行44100次采样。
* `frequency`：代表弦的振动频率，通常以赫兹（Hz）为单位。振动频率表示弦每秒钟震动的次数，它决定了弦发出的音调。
* 在这里，`capacity` 的计算通过将采样率除以频率来确定缓冲区的长度。由于采样率表示每秒钟的采样次数，而频率表示每秒钟的振动次数，
* 所以用采样率除以频率可以得到完成一个完整振动所需的采样数。这个值会被四舍五入到最接近的整数，因为缓冲区的长度需要是整数。
* 最终得到的 `capacity` 表示了缓冲区的长度，即存储音频数据的空间大小，它与弦的频率直接相关。
* 在这个类的上下文中，`capacity` 表示了要创建的缓冲区的大小，用于存储模拟的吉他弦振动所产生的音频数据。
* */
