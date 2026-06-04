package com.hk.mgmt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/plugins")
public class Plugins {

@GetMapping("/live-favicon")
public String liveFavicon() {
    return "plugins/live-favicon";
}
    

@GetMapping("/masonry")
public String masonry() {
    return "plugins/masonry";
}
    

@GetMapping("/animation")
public String animation() {
    return "plugins/animation";
}
    

@GetMapping("/sweet-alerts")
public String sweetAlerts() {
    return "plugins/sweet-alerts";
}
    

@GetMapping("/tree-view")
public String treeView() {
    return "plugins/tree-view";
}
    

@GetMapping("/clipboard")
public String clipboard() {
    return "plugins/clipboard";
}
    

@GetMapping("/loading-buttons")
public String loadingButtons() {
    return "plugins/loading-buttons";
}
    

@GetMapping("/pdf-viewer")
public String pdfViewer() {
    return "plugins/pdf-viewer";
}
    

@GetMapping("/pass-meter")
public String passMeter() {
    return "plugins/pass-meter";
}
    

@GetMapping("/text-diff")
public String textDiff() {
    return "plugins/text-diff";
}
    

@GetMapping("/idle-timer")
public String idleTimer() {
    return "plugins/idle-timer";
}
    

@GetMapping("/tour")
public String tour() {
    return "plugins/tour";
}
    

@GetMapping("/i18")
public String i18() {
    return "plugins/i18";
}
    

@GetMapping("/video-player")
public String videoPlayer() {
    return "plugins/video-player";
}
    

@GetMapping("/sortable")
public String sortable() {
    return "plugins/sortable";
}
    
}
    