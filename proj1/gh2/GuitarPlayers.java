package gh2;

import com.sun.javafx.charts.Legend;
import org.junit.Test;

public class GuitarPlayers {
    @Test
    public void SuperMario() {
        GuitarPlayer player = new GuitarPlayer(new java.io.File("mid/Super Mario 64 - Medley.mid"));
        player.play();
    }

    @Test
    public void Zelda() {
        GuitarPlayer player = new GuitarPlayer(new java.io.File("mid/The Legend of Zelda Ocarina of Time - Song of Storms.mid"));
        player.play();
    }
}
