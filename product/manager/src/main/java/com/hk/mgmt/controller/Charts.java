package com.hk.mgmt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/charts")
public class Charts {

@GetMapping("/echart/radar")
public String echartRadar() {
    return "charts/echart/radar";
}
    

@GetMapping("/echart/heatmap")
public String echartHeatmap() {
    return "charts/echart/heatmap";
}
    

@GetMapping("/echart/gauge")
public String echartGauge() {
    return "charts/echart/gauge";
}
    

@GetMapping("/echart/geo-map")
public String echartGeoMap() {
    return "charts/echart/geo-map";
}
    

@GetMapping("/echart/other")
public String echartOther() {
    return "charts/echart/other";
}
    

@GetMapping("/echart/pie")
public String echartPie() {
    return "charts/echart/pie";
}
    

@GetMapping("/echart/scatter")
public String echartScatter() {
    return "charts/echart/scatter";
}
    

@GetMapping("/echart/area")
public String echartArea() {
    return "charts/echart/area";
}
    

@GetMapping("/echart/candlestick")
public String echartCandlestick() {
    return "charts/echart/candlestick";
}
    

@GetMapping("/echart/bar")
public String echartBar() {
    return "charts/echart/bar";
}
    

@GetMapping("/echart/line")
public String echartLine() {
    return "charts/echart/line";
}
    

@GetMapping("/apex/radar")
public String apexRadar() {
    return "charts/apex/radar";
}
    

@GetMapping("/apex/sparklines")
public String apexSparklines() {
    return "charts/apex/sparklines";
}
    

@GetMapping("/apex/heatmap")
public String apexHeatmap() {
    return "charts/apex/heatmap";
}
    

@GetMapping("/apex/mixed")
public String apexMixed() {
    return "charts/apex/mixed";
}
    

@GetMapping("/apex/pie")
public String apexPie() {
    return "charts/apex/pie";
}
    

@GetMapping("/apex/slope")
public String apexSlope() {
    return "charts/apex/slope";
}
    

@GetMapping("/apex/column")
public String apexColumn() {
    return "charts/apex/column";
}
    

@GetMapping("/apex/scatter")
public String apexScatter() {
    return "charts/apex/scatter";
}
    

@GetMapping("/apex/polar-area")
public String apexPolarArea() {
    return "charts/apex/polar-area";
}
    

@GetMapping("/apex/boxplot")
public String apexBoxplot() {
    return "charts/apex/boxplot";
}
    

@GetMapping("/apex/area")
public String apexArea() {
    return "charts/apex/area";
}
    

@GetMapping("/apex/radialbar")
public String apexRadialbar() {
    return "charts/apex/radialbar";
}
    

@GetMapping("/apex/funnel")
public String apexFunnel() {
    return "charts/apex/funnel";
}
    

@GetMapping("/apex/treemap")
public String apexTreemap() {
    return "charts/apex/treemap";
}
    

@GetMapping("/apex/candlestick")
public String apexCandlestick() {
    return "charts/apex/candlestick";
}
    

@GetMapping("/apex/range")
public String apexRange() {
    return "charts/apex/range";
}
    

@GetMapping("/apex/bar")
public String apexBar() {
    return "charts/apex/bar";
}
    

@GetMapping("/apex/line")
public String apexLine() {
    return "charts/apex/line";
}
    

@GetMapping("/apex/bubble")
public String apexBubble() {
    return "charts/apex/bubble";
}
    

@GetMapping("/apex/timeline")
public String apexTimeline() {
    return "charts/apex/timeline";
}
    
}
    