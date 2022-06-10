package com.msimplelogic.model;

import java.io.Serializable;

public class OnlineSchemeModel implements Serializable {
    private String item_min_qty;
    private String item_max_qty;
    private String item_pkg_qty1;
    private String item_pkg_qty2;
    private String item_scheme;
    private String item_name;
    private String item_number;
    private String item_rp;
    private String item_mrp;
    private String item_quantity;
    private String item_amount;
    private String item_image_url;
    private String item_remarks;
    private String detail1;
    private String detail2;
    private String detail3;
    private String detail4;
    private String detail5;

    private String scheme_id;
    private String scheme_name;
    private String scheme_type;
    private String scheme_amount;
    private String scheme_description;
    private String scheme_buy_qty;
    private String scheme_get_qty;
    private String scheme_min_qty;
    private String scheme_topsellingproduct;

    public OnlineSchemeModel() {

    }

    public OnlineSchemeModel(String item_name, String item_number, String item_rp, String item_mrp, String item_quantity, String item_amount, String item_image_url,
                           String item_min_qty, String item_max_qty, String item_pkg_qty1, String item_pkg_qty2, String scheme_id, String scheme_name, String scheme_type,
                           String scheme_amount, String scheme_description, String scheme_buy_qty, String scheme_get_qty, String scheme_topsellingproduct, String scheme_min_qty) {
        this.item_name = item_name;
        this.item_number = item_number;
        this.item_rp = item_rp;
        this.item_mrp = item_mrp;
        this.item_quantity = item_quantity;
        this.item_amount = item_amount;
        this.item_image_url = item_image_url;
        this.item_min_qty = item_min_qty;
        this.item_max_qty = item_max_qty;
        this.item_pkg_qty1 = item_pkg_qty1;
        this.item_pkg_qty2 = item_pkg_qty2;

        this.scheme_id = scheme_id;
        this.scheme_name = scheme_name;
        this.scheme_type = scheme_type;
        this.scheme_amount = scheme_amount;
        this.scheme_description = scheme_description;
        this.scheme_buy_qty = scheme_buy_qty;
        this.scheme_get_qty = scheme_get_qty;
        this.scheme_topsellingproduct = scheme_topsellingproduct;
        this.scheme_min_qty = scheme_min_qty;
    }

    public String getDetail1() {
        return detail1;
    }

    public void setDetail1(String detail1) {
        this.detail1 = detail1;
    }

    public String getDetail2() {
        return detail2;
    }

    public void setDetail2(String detail2) {
        this.detail2 = detail2;
    }

    public String getDetail3() {
        return detail3;
    }

    public void setDetail3(String detail3) {
        this.detail3 = detail3;
    }

    public String getDetail4() {
        return detail4;
    }

    public void setDetail4(String detail4) {
        this.detail4 = detail4;
    }

    public String getDetail5() {
        return detail5;
    }

    public void setDetail5(String detail5) {
        this.detail5 = detail5;
    }

    public String getItem_remarks() {
        return item_remarks;
    }

    public void setItem_remarks(String item_remarks) {
        this.item_remarks = item_remarks;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_number() {
        return item_number;
    }

    public void setItem_number(String item_number) {
        this.item_number = item_number;
    }

    public String getItem_rp() {
        return item_rp;
    }

    public void setItem_rp(String item_rp) {
        this.item_rp = item_rp;
    }

    public String getItem_mrp() {
        return item_mrp;
    }

    public void setItem_mrp(String item_mrp) {
        this.item_mrp = item_mrp;
    }

    public String getItem_quantity() {
        return item_quantity;
    }

    public void setItem_quantity(String item_quantity) {
        this.item_quantity = item_quantity;
    }

    public String getItem_amount() {
        return item_amount;
    }

    public void setItem_amount(String item_amount) {
        this.item_amount = item_amount;
    }

    public String getItem_image_url() {
        return item_image_url;
    }

    public void setItem_image_url(String item_image_url) {
        this.item_image_url = item_image_url;
    }

    public String getItem_min_qty() {
        return item_min_qty;
    }

    public void setItem_min_qty(String item_min_qty) {
        this.item_min_qty = item_min_qty;
    }

    public String getItem_max_qty() {
        return item_max_qty;
    }

    public void setItem_max_qty(String item_max_qty) {
        this.item_max_qty = item_max_qty;
    }

    public String getItem_pkg_qty1() {
        return item_pkg_qty1;
    }

    public void setItem_pkg_qty1(String item_pkg_qty1) {
        this.item_pkg_qty1 = item_pkg_qty1;
    }

    public String getItem_pkg_qty2() {
        return item_pkg_qty2;
    }

    public void setItem_pkg_qty2(String item_pkg_qty2) {
        this.item_pkg_qty2 = item_pkg_qty2;
    }

    public String getItem_scheme() {
        return item_scheme;
    }

    public void setItem_scheme(String item_scheme) {
        this.item_scheme = item_scheme;
    }


    public String getScheme_name() {
        return scheme_name;
    }

    public void setScheme_name(String scheme_name) {
        this.scheme_name = scheme_name;
    }

    public String getScheme_id() {
        return scheme_id;
    }

    public void setScheme_id(String scheme_id) {
        this.scheme_id = scheme_id;
    }

    public String getScheme_type() {
        return scheme_type;
    }

    public void setScheme_type(String scheme_type) {
        this.scheme_type = scheme_type;
    }

    public String getScheme_amount() {
        return scheme_amount;
    }

    public void setScheme_amount(String scheme_amount) {
        this.scheme_amount = scheme_amount;
    }

    public String getScheme_description() {
        return scheme_description;
    }

    public void setScheme_description(String scheme_description) {
        this.scheme_description = scheme_description;
    }

    public String getScheme_buy_qty() {
        return scheme_buy_qty;
    }

    public void setScheme_buy_qty(String scheme_buy_qty) {
        this.scheme_buy_qty = scheme_buy_qty;
    }

    public String getScheme_get_qty() {
        return scheme_get_qty;
    }

    public void setScheme_get_qty(String scheme_get_qty) {
        this.scheme_get_qty = scheme_get_qty;
    }

    public String getScheme_topsellingproduct() {
        return scheme_topsellingproduct;
    }

    public void setScheme_topsellingproduct(String scheme_topsellingproduct) {
        this.scheme_topsellingproduct = scheme_topsellingproduct;
    }

    public String getScheme_min_qty() {
        return scheme_min_qty;
    }

    public void setScheme_min_qty(String scheme_min_qty) {
        this.scheme_min_qty = scheme_min_qty;
    }
}
