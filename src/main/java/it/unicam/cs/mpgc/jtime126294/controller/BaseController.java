package it.unicam.cs.mpgc.jtime126294.controller;

import it.unicam.cs.mpgc.jtime126294.model.impl.JTimeManager;

public abstract class BaseController {
    protected JTimeManager model;

    public void setModel(JTimeManager model) {
        this.model = model;
        onModelSet();
    }

    protected abstract void onModelSet();
}
