package com.hk.mgmt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/apps")
public class Apps {

@GetMapping("/manage")
public String manage() {
    return "apps/manage";
}
    

@GetMapping("/file-manager")
public String fileManager() {
    return "apps/file-manager";
}
    

@GetMapping("/issue-tracker")
public String issueTracker() {
    return "apps/issue-tracker";
}
    

@GetMapping("/companies")
public String companies() {
    return "apps/companies";
}
    

@GetMapping("/vote-list")
public String voteList() {
    return "apps/vote-list";
}
    

@GetMapping("/outlook")
public String outlook() {
    return "apps/outlook";
}
    

@GetMapping("/pin-board")
public String pinBoard() {
    return "apps/pin-board";
}
    

@GetMapping("/chat")
public String chat() {
    return "apps/chat";
}
    

@GetMapping("/api-keys")
public String apiKeys() {
    return "apps/api-keys";
}
    

@GetMapping("/calendar")
public String calendar() {
    return "apps/calendar";
}
    

@GetMapping("/clients")
public String clients() {
    return "apps/clients";
}
    

@GetMapping("/social-feed")
public String socialFeed() {
    return "apps/social-feed";
}
    

@GetMapping("/email/compose")
public String emailCompose() {
    return "apps/email/compose";
}
    

@GetMapping("/email/details")
public String emailDetails() {
    return "apps/email/details";
}
    

@GetMapping("/email/inbox")
public String emailInbox() {
    return "apps/email/inbox";
}
    

@GetMapping("/blog/grid")
public String blogGrid() {
    return "apps/blog/grid";
}
    

@GetMapping("/blog/add")
public String blogAdd() {
    return "apps/blog/add";
}
    

@GetMapping("/blog/list")
public String blogList() {
    return "apps/blog/list";
}
    

@GetMapping("/blog/article")
public String blogArticle() {
    return "apps/blog/article";
}
    

@GetMapping("/projects/team-board")
public String projectsTeamBoard() {
    return "apps/projects/team-board";
}
    

@GetMapping("/projects/details")
public String projectsDetails() {
    return "apps/projects/details";
}
    

@GetMapping("/projects/grid")
public String projectsGrid() {
    return "apps/projects/grid";
}
    

@GetMapping("/projects/list")
public String projectsList() {
    return "apps/projects/list";
}
    

@GetMapping("/projects/kanban")
public String projectsKanban() {
    return "apps/projects/kanban";
}
    

@GetMapping("/projects/activity")
public String projectsActivity() {
    return "apps/projects/activity";
}
    

@GetMapping("/forum/view")
public String forumView() {
    return "apps/forum/view";
}
    

@GetMapping("/forum/post")
public String forumPost() {
    return "apps/forum/post";
}
    

@GetMapping("/users/roles")
public String usersRoles() {
    return "apps/users/roles";
}
    

@GetMapping("/users/role-details")
public String usersRoleDetails() {
    return "apps/users/role-details";
}
    

@GetMapping("/users/permissions")
public String usersPermissions() {
    return "apps/users/permissions";
}
    

@GetMapping("/users/contacts")
public String usersContacts() {
    return "apps/users/contacts";
}
    

@GetMapping("/ecommerce/customers")
public String ecommerceCustomers() {
    return "apps/ecommerce/customers";
}
    

@GetMapping("/ecommerce/refunds")
public String ecommerceRefunds() {
    return "apps/ecommerce/refunds";
}
    

@GetMapping("/ecommerce/products")
public String ecommerceProducts() {
    return "apps/ecommerce/products";
}
    

@GetMapping("/ecommerce/sellers")
public String ecommerceSellers() {
    return "apps/ecommerce/sellers";
}
    

@GetMapping("/ecommerce/order-details")
public String ecommerceOrderDetails() {
    return "apps/ecommerce/order-details";
}
    

@GetMapping("/ecommerce/warehouse")
public String ecommerceWarehouse() {
    return "apps/ecommerce/warehouse";
}
    

@GetMapping("/ecommerce/product-add")
public String ecommerceProductAdd() {
    return "apps/ecommerce/product-add";
}
    

@GetMapping("/ecommerce/cart")
public String ecommerceCart() {
    return "apps/ecommerce/cart";
}
    

@GetMapping("/ecommerce/sales")
public String ecommerceSales() {
    return "apps/ecommerce/sales";
}
    

@GetMapping("/ecommerce/reviews")
public String ecommerceReviews() {
    return "apps/ecommerce/reviews";
}
    

@GetMapping("/ecommerce/categories")
public String ecommerceCategories() {
    return "apps/ecommerce/categories";
}
    

@GetMapping("/ecommerce/purchased-orders")
public String ecommercePurchasedOrders() {
    return "apps/ecommerce/purchased-orders";
}
    

@GetMapping("/ecommerce/attributes")
public String ecommerceAttributes() {
    return "apps/ecommerce/attributes";
}
    

@GetMapping("/ecommerce/product-details")
public String ecommerceProductDetails() {
    return "apps/ecommerce/product-details";
}
    

@GetMapping("/ecommerce/seller-details")
public String ecommerceSellerDetails() {
    return "apps/ecommerce/seller-details";
}
    

@GetMapping("/ecommerce/product-stocks")
public String ecommerceProductStocks() {
    return "apps/ecommerce/product-stocks";
}
    

@GetMapping("/ecommerce/order-add")
public String ecommerceOrderAdd() {
    return "apps/ecommerce/order-add";
}
    

@GetMapping("/ecommerce/orders")
public String ecommerceOrders() {
    return "apps/ecommerce/orders";
}
    

@GetMapping("/ecommerce/products-grid")
public String ecommerceProductsGrid() {
    return "apps/ecommerce/products-grid";
}
    

@GetMapping("/ecommerce/settings")
public String ecommerceSettings() {
    return "apps/ecommerce/settings";
}
    

@GetMapping("/ecommerce/checkout")
public String ecommerceCheckout() {
    return "apps/ecommerce/checkout";
}
    

@GetMapping("/ecommerce/product-views")
public String ecommerceProductViews() {
    return "apps/ecommerce/product-views";
}
    

@GetMapping("/ecommerce/marketplace")
public String ecommerceMarketplace() {
    return "apps/ecommerce/marketplace";
}
    

@GetMapping("/invoice/details")
public String invoiceDetails() {
    return "apps/invoice/details";
}
    

@GetMapping("/invoice/create")
public String invoiceCreate() {
    return "apps/invoice/create";
}
    

@GetMapping("/invoice/list")
public String invoiceList() {
    return "apps/invoice/list";
}
    
}
    