package com.hk.mgmt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tables")
public class Tables {

@GetMapping("/static")
public String tablesStatic() {
    return "tables/static";
}
    

@GetMapping("/custom")
public String custom() {
    return "tables/custom";
}
    

@GetMapping("/datatables/child-rows")
public String datatablesChildRows() {
    return "tables/datatables/child-rows";
}
    

@GetMapping("/datatables/fixed-columns")
public String datatablesFixedColumns() {
    return "tables/datatables/fixed-columns";
}
    

@GetMapping("/datatables/select")
public String datatablesSelect() {
    return "tables/datatables/select";
}
    

@GetMapping("/datatables/fixed-header")
public String datatablesFixedHeader() {
    return "tables/datatables/fixed-header";
}
    

@GetMapping("/datatables/columns")
public String datatablesColumns() {
    return "tables/datatables/columns";
}
    

@GetMapping("/datatables/scroll")
public String datatablesScroll() {
    return "tables/datatables/scroll";
}
    

@GetMapping("/datatables/ajax")
public String datatablesAjax() {
    return "tables/datatables/ajax";
}
    

@GetMapping("/datatables/range-search")
public String datatablesRangeSearch() {
    return "tables/datatables/range-search";
}
    

@GetMapping("/datatables/javascript")
public String datatablesJavascript() {
    return "tables/datatables/javascript";
}
    

@GetMapping("/datatables/basic")
public String datatablesBasic() {
    return "tables/datatables/basic";
}
    

@GetMapping("/datatables/column-searching")
public String datatablesColumnSearching() {
    return "tables/datatables/column-searching";
}
    

@GetMapping("/datatables/rows-add")
public String datatablesRowsAdd() {
    return "tables/datatables/rows-add";
}
    

@GetMapping("/datatables/rendering")
public String datatablesRendering() {
    return "tables/datatables/rendering";
}
    

@GetMapping("/datatables/export-data")
public String datatablesExportData() {
    return "tables/datatables/export-data";
}
    

@GetMapping("/datatables/checkbox-select")
public String datatablesCheckboxSelect() {
    return "tables/datatables/checkbox-select";
}
    
}
    