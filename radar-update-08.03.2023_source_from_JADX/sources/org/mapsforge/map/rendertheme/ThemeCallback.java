package org.mapsforge.map.rendertheme;

import org.mapsforge.map.rendertheme.renderinstruction.RenderInstruction;

public interface ThemeCallback {
    int getColor(RenderInstruction renderInstruction, int i);
}
