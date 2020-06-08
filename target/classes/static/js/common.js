/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function getURLVar(key) {
    var value = [];

    var query = String(document.location).split('?');

    if (query[1]) {
        var part = query[1].split('&');

        for (i = 0; i < part.length; i++) {
            var data = part[i].split('=');

            if (data[0] && data[1]) {
                value[data[0]] = data[1];
            }
        }

        if (value[key]) {
            return value[key];
        } else {
            return '';
        }
    }
}

$(document).ready(function () {
    const handledPis = new Set();
    const imgs = [...document.getElementsByClassName('check_img_errs')];
    imgs.forEach(img => {
        if (handledPis.has(img)) {
            return
        }

        img.addEventListener('error', () => {
            img.src = 'assets/image/error_product.png'
        })
    });

    // Highlight any found errors
    $('.text-danger').each(function () {
        var element = $(this).parent().parent();

        if (element.hasClass('form-group')) {
            element.addClass('has-error');
        }
    });

    // Currency
    $('#form-currency .currency-select').on('click', function (e) {
        e.preventDefault();

        $('#form-currency input[name=\'code\']').attr('value', $(this).attr('name'));

        $('#form-currency').submit();
    });

    // Language
    $('#form-language .language-select').on('click', function (e) {
        e.preventDefault();

        $('#form-language input[name=\'code\']').attr('value', $(this).attr('name'));

        $('#form-language').submit();
    })

    /* Search */
    $('#search input[name=\'search\']').parent().find('button').on('click', function () {
        var url = $('base').attr('href') + 'index.php?route=product/search';

        var value = $('header #search input[name=\'search\']').val();

        if (value) {
            url += '&search=' + encodeURIComponent(value);
        }

        location = url;
    });

    $('#search input[name=\'search\']').on('keydown', function (e) {
        if (e.keyCode == 13) {
            $('header #search input[name=\'search\']').parent().find('button').trigger('click');
        }
    });

    // Menu
    $('#menu .dropdown-menu').each(function () {
        var menu = $('#menu').offset();
        var dropdown = $(this).parent().offset();

        var i = (dropdown.left + $(this).outerWidth()) - (menu.left + $('#menu').outerWidth());

        if (i > 0) {
            $(this).css('margin-left', '-' + (i + 5) + 'px');
        }
    });

// Product List
    $('#list-view').click(function () {
        $(".products-category > .clearfix.visible-lg-block").remove();

        $('#content .product-layout').attr('class', 'product-layout product-list col-xs-12');

        localStorage.setItem('display', 'list');

        $('.btn-group').find('#list-view').addClass('selected');
        $('.btn-group').find('#grid-view').removeClass('selected');
    });

    // Checkout
    $(document).on('keydown', '#collapse-checkout-option input[name=\'email\'], #collapse-checkout-option input[name=\'password\']', function (e) {
        if (e.keyCode == 13) {
            $('#collapse-checkout-option #button-login').trigger('click');
        }
    });

    // tooltips on hover
    $('[data-toggle=\'tooltip\']').tooltip({container: 'body'});

    // Makes tooltips work on ajax generated content
    $(document).ajaxStop(function () {
        $('[data-toggle=\'tooltip\']').tooltip({container: 'body'});
    });
});

// Cart add remove functions
var cart = {
    'buyNow': function (product_id, quantity, el) {
        $.ajax({
            url: 'index.php?route=checkout/cart/add',
            type: 'post',
            data: 'product_id=' + product_id + '&quantity=' + (typeof (quantity) != 'undefined' ? quantity : 1),
            dataType: 'json',
            beforeSend: function () {
                $('#cart > button').button('loading');
                $(el).attr('disabled', true);
            },
            complete: function () {
                $('#cart > button').button('reset');
            },
            success: function (json) {
                $(el).attr('disabled', false);
                $('.alert, .text-danger').remove();

                if (json['redirect']) {
                    location = json['redirect'];
                }

                if (json['success']) {
                    location = 'index.php?route=checkout/checkout';
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                alert(thrownError + "\r\n" + xhr.statusText + "\r\n" + xhr.responseText);
            }
        });
    },
    'add': function (product_id, quantity, el) {
        $.ajax({
            url: 'index.php?route=checkout/cart/add',
            type: 'post',
            data: 'product_id=' + product_id + '&quantity=' + (typeof (quantity) != 'undefined' ? quantity : 1),
            dataType: 'json',
            beforeSend: function () {
                $('#cart > button').button('loading');
                $(el).attr('disabled', true);
            },
            complete: function () {
                $('#cart > button').button('reset');
            },
            success: function (json) {
                $(el).attr('disabled', false);
                $('.alert, .text-danger').remove();

                if (json['redirect']) {
                    location = json['redirect'];
                }

                if (json['success']) {
                    $('#content').parent().before('<div class="alert alert-success"><i class="fa fa-check-circle"></i> ' + json['success'] + ' <button type="button" class="close" data-dismiss="alert">&times;</button></div>');

                    // Need to set timeout otherwise it wont update the total
                    setTimeout(function () {
                        $('#cart > button').html('<div class="pull-left flip"><h4></h4></div><span id="cart-total"> ' + json['total'] + '</span> <i class="fa fa-caret-down"></i>');
                        $('#home-cart .heading span').html(json["total"].split(" ")[0]);
                        $('.quantity_mobile').html(json["total"].split(" ")[0]);
                    }, 100);

                    $('html, body').animate({scrollTop: 0}, 'slow');

                    $('#cart > ul').load('index.php?route=common/cart/info ul li');
                    $('#home-cart > ul').load('index.php?route=common/cart/info ul li');

                    $('#alert-position').empty();
                    $('#alert-position').append('<div class="alert alert-success"><i class="fa fa-check-circle"></i> ' + json['success'] + ' <button type="button" class="close" data-dismiss="alert">&times;</button></div>');

                    $(el).find('i').addClass('icon-added');
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                alert(thrownError + "\r\n" + xhr.statusText + "\r\n" + xhr.responseText);
            }
        });
    },
    'update': function (key, quantity) {
        $.ajax({
            url: 'index.php?route=checkout/cart/edit',
            type: 'post',
            data: 'key=' + key + '&quantity=' + (typeof (quantity) != 'undefined' ? quantity : 1),
            dataType: 'json',
            beforeSend: function () {
                $('#cart > button').button('loading');
            },
            complete: function () {
                $('#cart > button').button('reset');
            },
            success: function (json) {
                // Need to set timeout otherwise it wont update the total
                setTimeout(function () {
                    $('#cart > button').html('<div class="pull-left flip"><h4></h4></div><span id="cart-total"> ' + json['total'] + '</span> <i class="fa fa-caret-down"></i>');
                }, 100);

                if (getURLVar('route') == 'checkout/cart' || getURLVar('route') == 'checkout/checkout') {
                    location = 'index.php?route=checkout/cart';
                } else {
                    $('#cart > ul').load('index.php?route=common/cart/info ul li');
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                alert(thrownError + "\r\n" + xhr.statusText + "\r\n" + xhr.responseText);
            }
        });
    },
    'remove': function (key) {
        $.ajax({
            url: 'index.php?route=checkout/cart/remove',
            type: 'post',
            data: 'key=' + key,
            dataType: 'json',
            beforeSend: function () {
                $('#cart > button').button('loading');
            },
            complete: function () {
                $('#cart > button').button('reset');
            },
            success: function (json) {
                // Need to set timeout otherwise it wont update the total
                setTimeout(function () {
                    $('#cart > button').html('<div class="pull-left flip"><h4></h4></div><span id="cart-total"> ' + json['total'] + '</span> <i class="fa fa-caret-down"></i>');
                    $('#home-cart .heading span').html(json["total"].split(" ")[0]);
                }, 100);

                if (getURLVar('route') == 'checkout/cart' || getURLVar('route') == 'checkout/checkout') {
                    location = 'index.php?route=checkout/cart';
                } else {
                    $('#cart > ul').load('index.php?route=common/cart/info ul li');
                    $('#home-cart > ul').load('index.php?route=common/cart/info ul li');
                }
                $('#alert-position').empty();
                $('#alert-position').append('<div class="alert alert-success"><i class="fa fa-check-circle"></i> ' + json['success'] + ' <button type="button" class="close" data-dismiss="alert">&times;</button></div>');
            },
            error: function (xhr, ajaxOptions, thrownError) {
                alert(thrownError + "\r\n" + xhr.statusText + "\r\n" + xhr.responseText);
            }
        });
    }
}

var voucher = {
    'add': function () {

    },
    'remove': function (key) {
        $.ajax({
            url: 'index.php?route=checkout/cart/remove',
            type: 'post',
            data: 'key=' + key,
            dataType: 'json',
            beforeSend: function () {
                $('#cart > button').button('loading');
            },
            complete: function () {
                $('#cart > button').button('reset');
            },
            success: function (json) {
                // Need to set timeout otherwise it wont update the total
                setTimeout(function () {
                    $('#cart > button').html('<div class="pull-left flip"><h4></h4></div><span id="cart-total"> ' + json['total'] + '</span> <i class="fa fa-caret-down"></i>');
                }, 100);

                if (getURLVar('route') == 'checkout/cart' || getURLVar('route') == 'checkout/checkout') {
                    location = 'index.php?route=checkout/cart';
                } else {
                    $('#cart > ul').load('index.php?route=common/cart/info ul li');
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                alert(thrownError + "\r\n" + xhr.statusText + "\r\n" + xhr.responseText);
            }
        });
    }
}

var wishlist = {
    'add': function (product_id, el) {
        $.ajax({
            url: 'index.php?route=account/wishlist/add',
            type: 'post',
            data: 'product_id=' + product_id,
            dataType: 'json',
            success: function (json) {
                $('.alert').remove();

                if (json['redirect']) {
                    location = json['redirect'];
                }

                if (json['success']) {
                    $('#content').parent().before('<div class="alert alert-success"><i class="fa fa-check-circle"></i> ' + json['success'] + ' <button type="button" class="close" data-dismiss="alert">&times;</button></div>');
                    $(el).find('i').addClass('icon-added');
                }

                $('#wishlist-total span').html(json['total']);
                $('#wishlist-total').attr('title', json['total']);
                $('#alert-position').empty();
                $('#alert-position').append('<div class="alert alert-success"><i class="fa fa-check-circle"></i> ' + json['success'] + ' <button type="button" class="close" data-dismiss="alert">&times;</button></div>');
                $('html, body').animate({scrollTop: 0}, 'slow');
            },
            error: function (xhr, ajaxOptions, thrownError) {
                alert(thrownError + "\r\n" + xhr.statusText + "\r\n" + xhr.responseText);
            }
        });
    },
    'remove': function () {

    }
}

var compare = {
    'add': function (product_id) {
        $.ajax({
            url: 'index.php?route=product/compare/add',
            type: 'post',
            data: 'product_id=' + product_id,
            dataType: 'json',
            success: function (json) {
                $('.alert').remove();

                if (json['success']) {
                    $('#content').parent().before('<div class="alert alert-success"><i class="fa fa-check-circle"></i> ' + json['success'] + ' <button type="button" class="close" data-dismiss="alert">&times;</button></div>');

                    $('#compare-total').html(json['total']);

                    $('html, body').animate({scrollTop: 0}, 'slow');
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                alert(thrownError + "\r\n" + xhr.statusText + "\r\n" + xhr.responseText);
            }
        });
    },
    'remove': function () {

    }
}

/* Agree to Terms */
$(document).delegate('.agree', 'click', function (e) {
    e.preventDefault();

    $('#modal-agree').remove();

    var element = this;

    $.ajax({
        url: $(element).attr('href'),
        type: 'get',
        dataType: 'html',
        success: function (data) {
            html = '<div id="modal-agree" class="modal">';
            html += '  <div class="modal-dialog modal-dialog-centered">';
            html += '    <div class="modal-content">';
            html += '      <div class="modal-header">';
            html += '        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>';
            html += '        <h4 class="modal-title">' + $(element).text() + '</h4>';
            html += '      </div>';
            html += '      <div class="modal-body">' + data + '</div>';
            html += '    </div';
            html += '  </div>';
            html += '</div>';

            $('body').append(html);

            $('#modal-agree').modal('show');
        }
    });
});

// Autocomplete */
(function ($) {
    $.fn.autocomplete = function (option) {
        return this.each(function () {
            this.timer = null;
            this.items = new Array();

            $.extend(this, option);

            $(this).attr('autocomplete', 'off');

            // Focus
            $(this).on('focus', function () {
                this.request();
            });

            // Blur
            $(this).on('blur', function () {
                setTimeout(function (object) {
                    object.hide();
                }, 200, this);
            });

            // Keydown
            $(this).on('keydown', function (event) {
                switch (event.keyCode) {
                    case 27: // escape
                        this.hide();
                        break;
                    default:
                        this.request();
                        break;
                }
            });

            // Click
            this.click = function (event) {
                event.preventDefault();

                value = $(event.target).parent().attr('data-value');

                if (value && this.items[value]) {
                    this.select(this.items[value]);
                }
            }

            // Show
            this.show = function () {
                var pos = $(this).position();

                $(this).siblings('ul.dropdown-menu').css({
                    top: pos.top + $(this).outerHeight(),
                    left: pos.left
                });

                $(this).siblings('ul.dropdown-menu').show();
            }

            // Hide
            this.hide = function () {
                $(this).siblings('ul.dropdown-menu').hide();
            }

            // Request
            this.request = function () {
                clearTimeout(this.timer);

                this.timer = setTimeout(function (object) {
                    object.source($(object).val(), $.proxy(object.response, object));
                }, 200, this);
            }

            // Response
            this.response = function (json) {
                html = '';

                if (json.length) {
                    for (i = 0; i < json.length; i++) {
                        this.items[json[i]['value']] = json[i];
                    }

                    for (i = 0; i < json.length; i++) {
                        if (!json[i]['category']) {
                            html += '<li data-value="' + json[i]['value'] + '"><a href="#">' + json[i]['label'] + '</a></li>';
                        }
                    }

                    // Get all the ones with a categories
                    var category = new Array();

                    for (i = 0; i < json.length; i++) {
                        if (json[i]['category']) {
                            if (!category[json[i]['category']]) {
                                category[json[i]['category']] = new Array();
                                category[json[i]['category']]['name'] = json[i]['category'];
                                category[json[i]['category']]['item'] = new Array();
                            }

                            category[json[i]['category']]['item'].push(json[i]);
                        }
                    }

                    for (i in category) {
                        html += '<li class="dropdown-header">' + category[i]['name'] + '</li>';

                        for (j = 0; j < category[i]['item'].length; j++) {
                            html += '<li data-value="' + category[i]['item'][j]['value'] + '"><a href="#">&nbsp;&nbsp;&nbsp;' + category[i]['item'][j]['label'] + '</a></li>';
                        }
                    }
                }

                if (html) {
                    this.show();
                } else {
                    this.hide();
                }

                $(this).siblings('ul.dropdown-menu').html(html);
            }

            $(this).after('<ul class="dropdown-menu"></ul>');
            $(this).siblings('ul.dropdown-menu').delegate('a', 'click', $.proxy(this.click, this));

        });
    }
})(window.jQuery);

function loadMore(page, type, platform, bestproduct, bestSell,promotion,position) {
    $.ajax({
        method: 'get',
        url: '/api/detail',
        data: {
            type: type,
            page: page,
            platform: platform,
            bestproduct: bestproduct,
            bestsell: bestSell,
            promotion:promotion
        },
        success: function (data) {
            var po ='.'+position+ ' .row';
           $(po).append(data);
           
        }
    });
}

function loadMoreMaxPrice(page, el, price) {
    $.ajax({
        method: 'get',
        url: 'index.php?route=common/home/loadMore',
        data: {
            category: 13,
            page: page,
            filter_price: price
        },
        success: function (data) {
            $(el).parent('.list-container').find('#max-price').append(data);
        }
    });
}

function filterMaxPrice(el, price) {
    $('#max-price').empty();
    price_filter_page = 0;
    $(el).closest('.list-container').find('.view-more').remove();
    $.ajax({
        method: 'get',
        url: 'index.php?route=common/home/loadMore',
        data: {
            category: 13,
            page: 0,
            filter_price: price
        },
        success: function (data) {
            $(el).closest('.list-container').find('#max-price').append(data);
            $(el).closest('.list-container').append('<div class="view-more btn-aqua" onclick="loadMoreMaxPrice(++price_filter_page,this,' + price + ')">Tải thêm sản phẩm</div>')
        }
    });
}

function loadGameDescription(pid, el) {
    $(el).closest('.container').find('.item-detail').load('index.php?route=common/new_homepage/getGameDescription&pid=' + pid + ' div div');
    $(el).parent().find('.active').removeClass('active');
    $(el).addClass('active');
}

$(document).ready(function () {
    $("#filter_name").autocomplete("getdata.php?lan=vi", {
        width: 450,
        resultsClass: "ac_results col-lg-7",
        matchContains: true
    });

    $("#home-cart").hover(
            function () {
                $('#dropdown-detail-cart').css("display", "block");
            }, function () {
        $('#dropdown-detail-cart').css("display", "none");
    }
    );

    $(".home-mini-profile").hover(
            function () {
                $('.drop-mini-profile').css("display", "block");
            }, function () {
        $('.drop-mini-profile').css("display", "none");
    }
    );

});

$(document).ready(function () {
    $('#dismiss, .overlay').on('click', function () {
        $('#sidebar').removeClass('active');
        $('.overlay').removeClass('active');
        $('body').css('position', 'unset');
    });

    $('#sidebarCollapse').on('click', function () {
        $('#sidebar').addClass('active');
        $('.overlay').addClass('active');
        $('.collapse.in').toggleClass('in');
        $('a[aria-expanded=true]').attr('aria-expanded', 'false');
        $('body').css('position', 'fixed');
    });
});
function getInfoRecharge() {
    $.ajax({
        url: "http://localhost:8080"+ "/user/recharge/info",
        type: "get",
        data: {
        }
        ,
        success: function (event) {
            if (event.status === "Đang đợi") {
                $("#status-check").text("Đang đợi chuyển khoản");
                $("#loader").attr("style", "");
            } else if (event.status === "Thành công") {
                $("#loader").attr("style", "display: none");
                $("#status-check").text("Thành công");
                $("#status-check").attr("class", "alert alert-success");

            } else {
                $("#loader").attr("style", "display: none");
                $("#status-check").text("Thất bại do quá thời gian chuyển khoản");
                $("#status-check").attr("class", "alert alert-danger");
            }

        }
    })
}