package com.moonx.progressbar.console;

import com.moonx.progressbar.ProgressRenderer;
import com.moonx.progressbar.ProgressState;

public final class SpinnerProgressRenderer implements ProgressRenderer {

    private static final char[] SPIN = {
            '⠋','⠙','⠹','⠸','⠼',
            '⠴','⠦','⠧','⠇','⠏'
    };

    private static final long THROTTLE_MS = 100;

    private final String label;
    private int idx;
    private long last;

    public SpinnerProgressRenderer(String label) {
        this.label = label;
    }

    @Override
    public void render(ProgressState s) {
        long now = System.currentTimeMillis();
        if (now - last < THROTTLE_MS) return;
        last = now;

        char frame = SPIN[idx++ % SPIN.length];

        System.out.print("\r" + frame + " " + label);
        System.out.flush(); // 🔥 INI WAJIB
    }

    @Override
    public void finish() {
        System.out.print("\r");
        System.out.println(label + " ✓");
    }
}
