package edu.eci.arsw.highlandersim;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class HighlanderismTest {
    ControlFrame frame;
    public HighlanderismTest() {
    }

    @Before
    public void setUp() {
        frame = new ControlFrame();
    }

    @Test
    public void testInvariant(){
        int ans;
        frame.iniciar();

        for (int i=0;i<50;i++){
            frame.pausar();
            ans = frame.getSuma();
            assertEquals(300,ans);
            frame.reanudar();
        }
        frame.detener();

    }

}